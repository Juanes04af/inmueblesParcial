package co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada;

import java.util.List;

import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Propietario;

/**
 * Puerto de entrada que consulta los propietarios predeterminados.
 * La interfaz lo usa para llenar las listas de seleccion.
 *
 * @author Equipo Gestion de Inmuebles
 */
public interface ListarPropietariosUseCase {

    /**
     * Obtiene todos los propietarios.
     *
     * @return lista de propietarios ordenada por nombre
     */
    List<Propietario> listar();
}
