package co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.salida;

import java.util.List;
import java.util.Optional;

import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Propietario;

/**
 * Puerto de salida para consultar propietarios. Es de solo lectura,
 * porque los propietarios vienen predeterminados en la base de datos.
 *
 * @author Equipo Gestion de Inmuebles
 */
public interface PropietarioRepository {

    /**
     * Obtiene todos los propietarios.
     *
     * @return lista de propietarios ordenada por nombre
     */
    List<Propietario> listar();

    /**
     * Busca un propietario por su identificacion.
     *
     * @param identificacion identificacion del propietario
     * @return el propietario, o vacio si no existe
     */
    Optional<Propietario> buscarPorIdentificacion(String identificacion);
}
