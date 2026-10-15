package co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion;

/**
 * Excepcion lanzada cuando el inmueble solicitado no existe en persistencia.
 *
 * @author Equipo Gestion de Inmuebles
 */
public class InmuebleNoEncontradoException extends InmuebleException {

    private static final long serialVersionUID = 1L;

    /**
     * Construye la excepcion.
     *
     * @param chip CHIP buscado
     */
    public InmuebleNoEncontradoException(String chip) {
        super("No se encontro un inmueble con CHIP '" + chip + "'.");
    }
}
