package co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Entidad abstracta que representa un inmueble dentro del dominio.
 * <p>
 * Contiene los atributos comunes a todos los inmuebles. Todo inmueble
 * es una {@link Casa} o un {@link Apartamento}, y pertenece a exactamente
 * un {@link Propietario}. Sus datos se guardan en la tabla {@code inmueble}.
 *
 * @author Equipo Gestion de Inmuebles
 */
public abstract class Inmueble implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Estados validos de un inmueble. */
    public static final List<String> ESTADOS = Collections.unmodifiableList(
            Arrays.asList("DISPONIBLE", "VENDIDO", "ARRENDADO", "EN_MANTENIMIENTO"));

    /** Codigo Homologado de Identificacion Predial. Identifica al inmueble. */
    private String chip;

    /** Fecha en que se compro el inmueble. */
    private LocalDate fechaCompra;

    /** Direccion del inmueble. */
    private String direccion;

    /** Estado actual del inmueble; uno de {@link #ESTADOS}. */
    private String estado;

    /** Propietario del inmueble (relacion N a 1). */
    private Propietario propietario;

    /**
     * Construye un inmueble con sus datos generales.
     *
     * @param chip        CHIP del inmueble
     * @param fechaCompra fecha de compra
     * @param direccion   direccion
     * @param estado      estado del inmueble
     * @param propietario propietario del inmueble
     */
    protected Inmueble(String chip, LocalDate fechaCompra, String direccion,
                       String estado, Propietario propietario) {
        this.chip = chip;
        this.fechaCompra = fechaCompra;
        this.direccion = direccion;
        this.estado = estado;
        this.propietario = propietario;
    }

    /**
     * Obtiene el CHIP.
     *
     * @return CHIP del inmueble
     */
    public String getChip() { return chip; }

    /**
     * Obtiene la fecha de compra.
     *
     * @return fecha de compra
     */
    public LocalDate getFechaCompra() { return fechaCompra; }

    /**
     * Modifica la fecha de compra.
     *
     * @param fechaCompra nueva fecha de compra
     */
    public void setFechaCompra(LocalDate fechaCompra) { this.fechaCompra = fechaCompra; }

    /**
     * Obtiene la direccion.
     *
     * @return direccion del inmueble
     */
    public String getDireccion() { return direccion; }

    /**
     * Modifica la direccion.
     *
     * @param direccion nueva direccion
     */
    public void setDireccion(String direccion) { this.direccion = direccion; }

    /**
     * Obtiene el estado.
     *
     * @return estado del inmueble
     */
    public String getEstado() { return estado; }

    /**
     * Modifica el estado.
     *
     * @param estado nuevo estado
     */
    public void setEstado(String estado) { this.estado = estado; }

    /**
     * Obtiene el propietario.
     *
     * @return propietario del inmueble
     */
    public Propietario getPropietario() { return propietario; }

    /**
     * Cambia el propietario.
     *
     * @param propietario nuevo propietario
     */
    public void setPropietario(Propietario propietario) { this.propietario = propietario; }

    /**
     * Devuelve el tipo concreto de inmueble.
     *
     * @return {@code "CASA"} o {@code "APARTAMENTO"}
     */
    public abstract String getTipo();

    /**
     * Dos inmuebles son iguales si tienen el mismo CHIP.
     *
     * @param o objeto a comparar
     * @return {@code true} si representan el mismo inmueble
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inmueble)) {
            return false;
        }
        return Objects.equals(chip, ((Inmueble) o).chip);
    }

    /**
     * Calcula el hash a partir del CHIP.
     *
     * @return valor hash
     */
    @Override
    public int hashCode() { return Objects.hash(chip); }
}
