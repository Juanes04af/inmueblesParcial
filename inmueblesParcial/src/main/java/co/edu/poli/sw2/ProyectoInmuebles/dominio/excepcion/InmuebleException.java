package co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion;

/**
 * Excepcion base del dominio de inmuebles. Todas las demas la extienden,
 * asi la interfaz puede atraparlas con un solo {@code catch}.
 *
 * @author Equipo Gestion de Inmuebles
 */
public class InmuebleException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Construye una excepcion con mensaje.
     *
     * @param mensaje descripcion del error
     */
    public InmuebleException(String mensaje) {
        super(mensaje);
    }

    /**
     * Construye una excepcion con mensaje y causa.
     *
     * @param mensaje descripcion del error
     * @param causa   causa original
     */
    public InmuebleException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
