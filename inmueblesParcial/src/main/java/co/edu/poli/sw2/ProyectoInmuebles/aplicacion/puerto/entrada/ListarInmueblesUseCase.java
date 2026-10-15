package co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada;

import java.util.List;

import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Inmueble;

/**
 * Puerto de entrada encargado de listar inmuebles.
 *
 * @author Equipo Gestion de Inmuebles
 */
public interface ListarInmueblesUseCase {

    /**
     * Obtiene todos los inmuebles registrados.
     *
     * @return lista de inmuebles ordenada por CHIP
     */
    List<Inmueble> listar();

    /**
     * Obtiene los inmuebles de un propietario
     * (caso de un propietario con varios inmuebles).
     *
     * @param identificacionPropietario identificacion del propietario
     * @return inmuebles del propietario; lista vacia si no tiene
     */
    List<Inmueble> listarPorPropietario(String identificacionPropietario);
}
