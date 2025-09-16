package helper;

import mx.avanti.desarrollo.dao.UsuarioDAO;
import mx.desarrollo.entity.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

public class LoginHelper {
    private final UsuarioDAO dao = new UsuarioDAO();

    public static final String MSG_BIENVENIDO = "Bienvenido, %s. Redirigiendo…";
    public static final String MSG_INVALIDAS  = "Credenciales inválidas";
    public static final String MSG_EXPIRADA   = "Sesión expirada, por favor inicia sesión nuevamente";
    public static final String MSG_DENEGADO   = "Acceso denegado: no tienes permisos para esta sección";

    private static final int MAX_INTENTOS = 5;
    private static final int MINUTOS_BLOQUEO = 15;

    public static final String COOKIE_NAME = "SAUAP_TOKEN";
    private static final int TOKEN_BYTES = 32;
    private static final int TOKEN_HORAS = 2;

    private static final ConcurrentHashMap<String, ShadowAttempts> SHADOW = new ConcurrentHashMap<>();
    private static class ShadowAttempts { int count = 0; LocalDateTime until = null; }

    public static class AuthResult {
        public enum Status { OK, INVALID, LOCKED }
        public final Status status;
        public final Usuario usuario;
        public final String mensaje;
        public AuthResult(Status s, Usuario u, String m) { status=s; usuario=u; mensaje=m; }
        public static AuthResult ok(Usuario u)          { return new AuthResult(Status.OK, u, null); }
        public static AuthResult invalid(String m)      { return new AuthResult(Status.INVALID, null, m); }
        public static AuthResult locked(String m)       { return new AuthResult(Status.LOCKED,  null, m); }
    }

    public AuthResult login(String correo, String passPlano) {
        if (correo != null) correo = correo.trim();
        if (passPlano != null) passPlano = passPlano.trim();
        if (correo == null || correo.isEmpty()) return AuthResult.invalid(MSG_INVALIDAS);

        var ahora = LocalDateTime.now();
        var HHmm = DateTimeFormatter.ofPattern("HH:mm");

        Usuario u = dao.findByCorreo(correo);
        if (u != null) {
            if (u.getBloqueadoHasta() != null && ahora.isBefore(u.getBloqueadoHasta())) {
                return AuthResult.locked("Cuenta bloqueada por intentos fallidos. Inténtalo a las "
                        + u.getBloqueadoHasta().format(HHmm) + ".");
            }
            String guardada = u.getContrasena();
            if (guardada == null || guardada.isEmpty()) return AuthResult.invalid(MSG_INVALIDAS);
            boolean esHash = guardada.startsWith("$2a$") || guardada.startsWith("$2b$") || guardada.startsWith("$2y$");
            boolean ok = esHash ? BCrypt.checkpw(passPlano, guardada) : guardada.equals(passPlano);
            if (ok) {
                if (!esHash) { u.setContrasena(BCrypt.hashpw(passPlano, BCrypt.gensalt(10))); dao.save(u); }
                dao.resetLoginState(u.getId());
                SHADOW.remove(correo);
                return AuthResult.ok(u);
            } else {
                int n = dao.addFailedAttempt(u.getId());
                int quedan = Math.max(0, MAX_INTENTOS - n);
                if (n >= MAX_INTENTOS) {
                    var hasta = ahora.plusMinutes(MINUTOS_BLOQUEO);
                    dao.lockUntil(u.getId(), hasta);
                    return AuthResult.locked("Cuenta bloqueada por " + MINUTOS_BLOQUEO +
                            " minutos. Inténtalo a las " + hasta.format(HHmm) + ".");
                }
                return AuthResult.invalid("Credenciales inválidas. Intentos restantes: " + quedan + ".");
            }
        }

        ShadowAttempts sa = SHADOW.computeIfAbsent(correo, k -> new ShadowAttempts());
        if (sa.until != null && ahora.isBefore(sa.until)) {
            return AuthResult.locked("Cuenta bloqueada por intentos fallidos. Inténtalo a las " + sa.until.format(HHmm) + ".");
        }
        sa.count++;
        if (sa.count >= MAX_INTENTOS) {
            sa.until = ahora.plusMinutes(MINUTOS_BLOQUEO);
            sa.count = 0;
            return AuthResult.locked("Cuenta bloqueada por " + MINUTOS_BLOQUEO +
                    " minutos. Inténtalo a las " + sa.until.format(HHmm) + ".");
        }
        int quedan = MAX_INTENTOS - sa.count;
        return AuthResult.invalid("Credenciales inválidas. Intentos restantes: " + quedan + ".");
    }

    public static class TokenBundle {
        public final String token;
        public final LocalDateTime expira;
        public TokenBundle(String t, LocalDateTime e){ token=t; expira=e; }
    }

    public TokenBundle emitirToken(Usuario u) {
        byte[] raw = new byte[TOKEN_BYTES];
        new SecureRandom().nextBytes(raw);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(raw); // URL-safe
        LocalDateTime exp = LocalDateTime.now().plusHours(TOKEN_HORAS);
        dao.setToken(u.getId(), token, exp);
        return new TokenBundle(token, exp);
    }

    public boolean esAdmin(Usuario u) {
        return u != null && u.getEsAdmin() != null && u.getEsAdmin() == 1;
    }
}
