package ui;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.avanti.desarrollo.dao.UsuarioDAO;
import mx.desarrollo.entity.Usuario;

import java.io.IOException;
import java.time.LocalDateTime;
import helper.LoginHelper;

@WebFilter(urlPatterns = {"/admin/*", "/profesor/*"})
public class SecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req  = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        Usuario us = (Usuario) req.getSession().getAttribute("usuarioLogueado");

        if (us == null) {
            String token = readCookie(req, LoginHelper.COOKIE_NAME);
            if (token != null && !token.isEmpty()) {
                UsuarioDAO dao = new UsuarioDAO();
                Usuario uTok = dao.findByToken(token);
                if (uTok != null && uTok.getTokenExpira() != null && LocalDateTime.now().isBefore(uTok.getTokenExpira())) {
                    req.getSession().setAttribute("usuarioLogueado", uTok);
                    us = uTok;
                } else {
                    dao.clearTokenByValue(token);
                    clearCookie(resp, req, LoginHelper.COOKIE_NAME);
                }
            }
        }

        if (us == null) {
            resp.sendRedirect(req.getContextPath() + "/login.xhtml?expired=1");
            return;
        }

        boolean quiereAdmin = req.getRequestURI().contains("/admin/");
        boolean esAdmin = (us.getEsAdmin() != null && us.getEsAdmin() == 1);

        if (quiereAdmin && !esAdmin) {
            resp.sendRedirect(req.getContextPath() + "/login.xhtml?denied=1");
            return;
        }

        chain.doFilter(request, response);
    }

    private static String readCookie(HttpServletRequest req, String name) {
        Cookie[] cs = req.getCookies();
        if (cs == null) return null;
        for (Cookie c : cs) if (name.equals(c.getName())) return c.getValue();
        return null;
    }

    private static void clearCookie(HttpServletResponse resp, HttpServletRequest req, String name) {
        String path = req.getContextPath();
        if (path == null || path.isEmpty()) path = "/";
        Cookie bye = new Cookie(name, "");
        bye.setHttpOnly(true);
        bye.setSecure(req.isSecure());
        bye.setPath(path);
        bye.setMaxAge(0);
        resp.addCookie(bye);
        resp.addHeader("Set-Cookie", name + "=; Max-Age=0; Path=" + path +
                "; HttpOnly" + (req.isSecure() ? "; Secure" : "") + "; SameSite=Lax");
    }
}
