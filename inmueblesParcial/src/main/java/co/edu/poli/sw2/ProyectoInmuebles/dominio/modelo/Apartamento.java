package co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo;

import java.time.LocalDate;

/**
 * Inmueble de tipo apartamento.
 * <p>
 * Extiende {@link Inmueble} agregando el numero de piso. Sus datos
 * especificos se guardan en la tabla {@code apartamento}, que referencia
 * a {@code inmueble} mediante el CHIP.
 *
 * @author Equipo Gestion de Inmuebles
 * @see Inmueble
 */
public class Apartamento extends Inmueble {

    private static final long serialVersionUID = 1L;

    /** Piso en el que esta ubicado el apartamento. */
    private int numeroPiso;

    /**
     * Crea un apartamento con todos sus atributos.
     *
     * @param chip        CHIP del inmueble
     * @param fechaCompra fecha de compra
     * @param direccion   direccion
     * @param estado      estado del inmueble
     * @param propietario propietario
     * @param numeroPiso  piso donde esta el apartamento
     */
    public Apartamento(String chip, LocalDate fechaCompra, String direccion,
                       String estado, Propietario propietario, int numeroPiso) {
        super(chip, fechaCompra, direccion, estado, propietario);
        this.numeroPiso = numeroPiso;
    }

    /**
     * Obtiene el numero de piso.
     *
     * @return piso del apartamento
     */
    public int getNumeroPiso() { return numeroPiso; }

    /**
     * Modifica el numero de piso.
     *
     * @param numeroPiso nuevo piso
     */
    public void setNumeroPiso(int numeroPiso) { this.numeroPiso = numeroPiso; }

    /**
     * {@inheritDoc}
     *
     * @return siempre {@code "APARTAMENTO"}
     */
    @Override
    public String getTipo() { return "APARTAMENTO"; }
}
