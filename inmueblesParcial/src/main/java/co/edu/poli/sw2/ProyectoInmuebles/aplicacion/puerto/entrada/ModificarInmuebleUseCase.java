package co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada;

import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Inmueble;

/**
 * Puerto de entrada encargado de modificar un inmueble.
 *
 * @author Equipo Gestion de Inmuebles
 */
public interface ModificarInmuebleUseCase {

    /**
     * Modifica un inmueble existente. El CHIP no cambia; el resto de
     * datos (incluido el tipo y el sotano) se reemplaza por los nuevos.
     *
     * @param existente inmueble seleccionado
     * @param datos     nuevos datos
     * @return inmueble modificado
     * @throws co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.InmuebleValidacionException  si algun dato no es valido
     * @throws co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.InmuebleNoEncontradoException si ya no existe
     */
    Inmueble modificar(Inmueble existente, DatosInmueble datos);
}
