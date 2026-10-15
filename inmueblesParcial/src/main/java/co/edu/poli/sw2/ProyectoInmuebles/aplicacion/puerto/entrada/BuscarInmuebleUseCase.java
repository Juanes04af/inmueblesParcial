package co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada;

import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Inmueble;

/**
 * Puerto de entrada encargado de consultar un inmueble por su CHIP.
 *
 * @author Equipo Gestion de Inmuebles
 */
public interface BuscarInmuebleUseCase {

    /**
     * Busca un inmueble por CHIP.
     *
     * @param chip CHIP del inmueble
     * @return inmueble encontrado
     * @throws co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.InmuebleNoEncontradoException si no existe
     */
    Inmueble buscarPorChip(String chip);
}
