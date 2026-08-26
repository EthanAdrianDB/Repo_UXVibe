package mx.edu.utez.uxvibe.model.dao;

import java.util.List;

/**
 * Interfaz genérica para el patrón DAO (Data Access Object).
 * Define las operaciones básicas del CRUD para cualquier entidad.
 * @param <T> Tipo de objeto modelo (Evaluador, Prueba, Participante, etc.).
 * @param <K> Tipo de dato de la clave primaria (generalmente Integer).
 */
public interface Dao<T, K> {
    /** Inserta una nueva entidad en la base de datos. */
    boolean create(T entidad);

    /** Obtiene todas las entidades registradas. */
    List<T> getAll();

    /** Busca una entidad por su clave primaria. */
    T getById(K id);

    /** Actualiza los datos de una entidad existente. */
    boolean update(T entidad);

    /** Elimina una entidad por su clave primaria. */
    boolean delete(K id);
}
