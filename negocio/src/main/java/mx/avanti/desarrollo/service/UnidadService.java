package mx.avanti.desarrollo.service;

import mx.avanti.desarrollo.dao.UnidadDAO;
import mx.desarrollo.entity.Unidad;

import java.util.List;
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

    public Unidad actualizar(Integer id, String nombre, String tipo, Integer horas) {
        if (id == null) throw new IllegalArgumentException("Selecciona una unidad.");
        validarNombre(nombre);
        validarTipo(tipo);
        validarHoras(horas);
        Unidad u = dao.findById(id);
        if (u == null) throw new IllegalArgumentException("La unidad no existe.");
        u.setNombre(nombre.trim());
        u.setTipo(tipo.trim().toUpperCase());
        u.setHoras(horas);
        dao.save(u);
        return u;
    }

    public Unidad obtener(Integer id) {
        if (id == null) return null;
        return dao.findById(id);
    }

    public List<Unidad> listar() {
        return dao.findAll();
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (nombre.trim().length() > 50)
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
