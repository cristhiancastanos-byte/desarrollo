package mx.avanti.desarrollo.service;

import jakarta.persistence.EntityManager;
import mx.avanti.desarrollo.dao.JpaUtil;
import mx.avanti.desarrollo.dao.ProfesorDAO;
import mx.avanti.desarrollo.dao.UsuarioDAO;
import mx.desarrollo.entity.Profesor;
import mx.desarrollo.entity.Usuario;

import java.security.SecureRandom;
import java.util.regex.Pattern;

public class ProfesorService {

    private final ProfesorDAO pdao = new ProfesorDAO();
    private final UsuarioDAO udao = new UsuarioDAO();

    private static final Pattern RFC_PERSONA = Pattern.compile("^[A-ZÑ&]{4}\\d{6}[A-Z0-9]{3}$");
    private static final Pattern EMAIL = Pattern.compile("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$");

    public String crearProfesorYUsuario(String nombre, String apP, String apM, String rfc, String correo) {

        validarNombre(nombre);
        validarApellido("Apellido paterno", apP);
        validarApellido("Apellido materno", apM);
        validarRFC(rfc);
        validarCorreo(correo);

        final String rfcUp = rfc.trim().toUpperCase();
        final String correoNorm = correo.trim().toLowerCase();

        if (pdao.existsByRFC(rfcUp)) {
            throw new IllegalArgumentException("El RFC ya está registrado.");
        }
        if (udao.existsByCorreo(correoNorm)) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }

        final String tempPass = generarPassword(8);

        EntityManager em = JpaUtil.em();
        try {
            em.getTransaction().begin();

            Usuario u = new Usuario();
            u.setCorreo(correoNorm);
            u.setContrasena(tempPass);
            u.setEsAdmin(0);
            u.setIntentosFallidos(0);
            em.persist(u);

            Profesor p = new Profesor();
            p.setNombre(nombre.trim());
            p.setApellidoPaterno(apP.trim());
            p.setApellidoMaterno(apM.trim());
            p.setRfc(rfcUp);
            p.setUsuario(u);
            em.persist(p);

            em.getTransaction().commit();
            return tempPass;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // ========= Reglas =========
    private void validarNombre(String s) {
        if (s == null || s.trim().isEmpty())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (s.trim().length() > 50)
            throw new IllegalArgumentException("El nombre no debe exceder 50 caracteres.");
    }
    private void validarApellido(String campo, String s) {
        if (s == null || s.trim().isEmpty())
            throw new IllegalArgumentException(campo + " es obligatorio.");
        if (s.trim().length() > 50)
            throw new IllegalArgumentException(campo + " no debe exceder 50 caracteres.");
    }
    private void validarRFC(String r) {
        if (r == null || r.trim().isEmpty())
            throw new IllegalArgumentException("El RFC es obligatorio.");
        String up = r.trim().toUpperCase();
        if (up.length() != 13 || !RFC_PERSONA.matcher(up).matches())
            throw new IllegalArgumentException("RFC inválido. Debe tener 13 caracteres y formato correcto.");
    }
    private void validarCorreo(String c) {
        if (c == null || c.trim().isEmpty())
            throw new IllegalArgumentException("El correo es obligatorio.");
        if (!EMAIL.matcher(c.trim()).matches())
            throw new IllegalArgumentException("Correo inválido.");
    }
    private String generarPassword(int len) {
        final String abc = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789@#$%";
        SecureRandom r = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) sb.append(abc.charAt(r.nextInt(abc.length())));
        return sb.toString();
    }
}
