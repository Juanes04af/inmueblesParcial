package co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada;

import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Inmueble;

/**
 * Puerto de entrada encargado de registrar inmuebles.
 *
 * @author Equipo Gestion de Inmuebles
 */
public interface CrearInmuebleUseCase {

    /**
     * Crea una casa o un apartamento a partir de los datos capturados.
     *
     * @param datos datos del formulario
     * @return inmueble creado
     * @throws co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.InmuebleValidacionException si algun dato no es valido
     * @throws co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.InmuebleDuplicadoException  si el CHIP ya existe
     */
    Inmueble crear(DatosInmueble datos);
}
