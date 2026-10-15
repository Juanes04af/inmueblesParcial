package co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion;

/**
 * Excepcion lanzada cuando ya existe un inmueble con el mismo CHIP.
 *
 * @author Equipo Gestion de Inmuebles
 */
public class InmuebleDuplicadoException extends InmuebleException {

    private static final long serialVersionUID = 1L;

    /**
     * Construye la excepcion.
     *
     * @param chip CHIP duplicado
     */
    public InmuebleDuplicadoException(String chip) {
        super("Ya existe un inmueble registrado con el CHIP '" + chip + "'.");
    }
}
