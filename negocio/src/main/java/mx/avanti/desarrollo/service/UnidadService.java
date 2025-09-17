package mx.avanti.desarrollo.service;

import mx.avanti.desarrollo.dao.UnidadDAO;
import mx.desarrollo.entity.Unidad;

import java.util.Set;

public class UnidadService {

    private final UnidadDAO dao = new UnidadDAO();
    private static final Set<String> TIPOS = Set.of("CLASE", "TALLER", "LABORATORIO");

    public Unidad crear(String nombre, String tipo, Integer horas) {
        validarNombre(nombre);
        validarTipo(tipo);
        validarHoras(horas);

        Unidad u = new Unidad();
        u.setNombre(nombre.trim());
        u.setTipo(tipo.trim().toUpperCase());
        u.setHoras(horas);

        dao.save(u);
        return u;
    }

    // ===== Reglas =====
    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        String n = nombre.trim();
        if (n.length() > 50)
            throw new IllegalArgumentException("El nombre no debe exceder 50 caracteres.");
    }

    private void validarTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty())
            throw new IllegalArgumentException("El tipo es obligatorio.");
        String t = tipo.trim().toUpperCase();
        if (!TIPOS.contains(t))
            throw new IllegalArgumentException("Tipo inválido. Usa: CLASE, TALLER o LABORATORIO.");
    }

    private void validarHoras(Integer h) {
        if (h == null) throw new IllegalArgumentException("Las horas son obligatorias.");
        if (h < 0 || h > 4) throw new IllegalArgumentException("Las horas deben estar entre 0 y 4.");
    }
}
