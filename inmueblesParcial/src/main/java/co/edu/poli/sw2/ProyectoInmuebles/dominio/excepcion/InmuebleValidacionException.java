package co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion;

/**
 * Excepcion lanzada cuando un dato del inmueble no cumple
 * las reglas de validacion.
 *
 * @author Equipo Gestion de Inmuebles
 */
public class InmuebleValidacionException extends InmuebleException {

    private static final long serialVersionUID = 1L;

    /**
     * Construye la excepcion.
     *
     * @param mensaje regla que no fue cumplida
     */
    public InmuebleValidacionException(String mensaje) {
        super(mensaje);
    }
}
