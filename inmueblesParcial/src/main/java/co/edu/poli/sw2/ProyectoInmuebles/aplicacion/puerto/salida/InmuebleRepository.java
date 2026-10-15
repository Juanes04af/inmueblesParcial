package co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.salida;

import java.util.List;

import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Inmueble;

/**
 * Puerto de salida que define las operaciones necesarias
 * para almacenar y recuperar inmuebles.
 * <p>
 * La aplicacion conoce esta interfaz, pero no conoce PostgreSQL ni JDBC.
 *
 * @author Equipo Gestion de Inmuebles
 */
public interface InmuebleRepository {

    /**
     * Guarda un nuevo inmueble con su detalle.
     *
     * @param inmueble inmueble a almacenar
     * @return inmueble almacenado
     * @throws co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.InmuebleDuplicadoException si el CHIP ya existe
     */
    Inmueble crear(Inmueble inmueble);

    /**
     * Actualiza un inmueble existente y reemplaza su detalle.
     *
     * @param inmueble inmueble modificado
     * @return inmueble actualizado
     * @throws co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.InmuebleNoEncontradoException si no existe
     */
    Inmueble modificar(Inmueble inmueble);

    /**
     * Elimina un inmueble.
     *
     * @param inmueble inmueble a eliminar
     * @throws co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.InmuebleNoEncontradoException si no existe
     */
    void eliminar(Inmueble inmueble);

    /**
     * Busca un inmueble por CHIP.
     *
     * @param chip CHIP del inmueble
     * @return inmueble encontrado
     * @throws co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.InmuebleNoEncontradoException si no existe
     */
    Inmueble buscarPorChip(String chip);

    /**
     * Obtiene todos los inmuebles.
     *
     * @return lista de inmuebles ordenada por CHIP
     */
    List<Inmueble> listar();

    /**
     * Obtiene los inmuebles de un propietario.
     *
     * @param identificacionPropietario identificacion del propietario
     * @return inmuebles del propietario ordenados por CHIP
     */
    List<Inmueble> listarPorPropietario(String identificacionPropietario);
}
