package co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo;

import java.time.LocalDate;

/**
 * Inmueble de tipo casa.
 * <p>
 * Extiende {@link Inmueble} con la cantidad de pisos y un {@link Sotano}
 * opcional. La relacion con el sotano es una <b>composicion 1 a 0..1</b>:
 * el sotano no existe sin la casa y se elimina con ella
 * ({@code ON DELETE CASCADE} en la tabla {@code sotano}).
 *
 * @author Equipo Gestion de Inmuebles
 * @see Inmueble
 * @see Sotano
 */
public class Casa extends Inmueble {

    private static final long serialVersionUID = 1L;

    /** Cantidad de pisos de la casa. */
    private int cantidadPisos;

    /** Sotano de la casa; {@code null} si no tiene. */
    private Sotano sotano;

    /**
     * Crea una casa con todos sus atributos.
     *
     * @param chip          CHIP del inmueble
     * @param fechaCompra   fecha de compra
     * @param direccion     direccion
     * @param estado        estado del inmueble
     * @param propietario   propietario
     * @param cantidadPisos cantidad de pisos
     * @param sotano        sotano de la casa, o {@code null} si no tiene
     */
    public Casa(String chip, LocalDate fechaCompra, String direccion, String estado,
                Propietario propietario, int cantidadPisos, Sotano sotano) {
        super(chip, fechaCompra, direccion, estado, propietario);
        this.cantidadPisos = cantidadPisos;
        this.sotano = sotano;
    }

    /**
     * Obtiene la cantidad de pisos.
     *
     * @return cantidad de pisos
     */
    public int getCantidadPisos() { return cantidadPisos; }

    /**
     * Modifica la cantidad de pisos.
     *
     * @param cantidadPisos nueva cantidad de pisos
     */
    public void setCantidadPisos(int cantidadPisos) { this.cantidadPisos = cantidadPisos; }

    /**
     * Obtiene el sotano.
     *
     * @return el sotano, o {@code null} si la casa no tiene
     */
    public Sotano getSotano() { return sotano; }

    /**
     * Asigna o quita el sotano.
     *
     * @param sotano nuevo sotano, o {@code null} para quitarlo
     */
    public void setSotano(Sotano sotano) { this.sotano = sotano; }

    /**
     * Indica si la casa tiene sotano.
     *
     * @return {@code true} si tiene sotano
     */
    public boolean tieneSotano() { return sotano != null; }

    /**
     * {@inheritDoc}
     *
     * @return siempre {@code "CASA"}
     */
    @Override
    public String getTipo() { return "CASA"; }
}
