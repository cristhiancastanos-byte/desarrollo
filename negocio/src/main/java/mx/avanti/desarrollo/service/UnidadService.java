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
        if (id == null) throw new IllegalArgumentException("ID inválido.");
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

    public void eliminar(Integer id) {
        if (id == null) throw new IllegalArgumentException("Debes seleccionar una unidad.");
        long deps = dao.countAsignaciones(id);
        if (deps > 0) {
            throw new IllegalStateException("No se puede eliminar: la unidad tiene asignaciones activas.");
        }
        dao.delete(id);
    }

    public Unidad obtenerPorId(Integer id) {
        return dao.findById(id);
    }

    public List<Unidad> buscarPorNombre(String filtro) {
        return dao.findByNombreLike(filtro);
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (nombre.trim().length() > 50)
            throw new IllegalArgumentException("Máximo 50 caracteres.");
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
