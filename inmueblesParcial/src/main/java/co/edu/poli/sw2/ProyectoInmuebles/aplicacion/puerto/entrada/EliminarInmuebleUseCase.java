package co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada;

import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Inmueble;

/**
 * Puerto de entrada encargado de eliminar un inmueble.
 *
 * @author Equipo Gestion de Inmuebles
 */
public interface EliminarInmuebleUseCase {

    /**
     * Elimina un inmueble junto con su detalle (casa/apartamento y sotano).
     *
     * @param inmueble inmueble a eliminar
     * @throws co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.InmuebleNoEncontradoException si no existe
     */
    void eliminar(Inmueble inmueble);
}
