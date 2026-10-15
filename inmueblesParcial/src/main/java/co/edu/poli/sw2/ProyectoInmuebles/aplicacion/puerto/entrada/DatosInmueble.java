package co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada;

import java.time.LocalDate;

/**
 * Datos capturados en la interfaz para crear o modificar un inmueble.
 * <p>
 * Es el objeto que viaja por los puertos de entrada. Los valores numericos
 * llegan como texto, tal como los escribe el usuario; el servicio los
 * valida y convierte antes de construir el objeto de dominio. Se llena con
 * metodos encadenados:
 * </p>
 * <pre>
 * new DatosInmueble()
 *         .chip("AAA0001AAAA")
 *         .tipo("APARTAMENTO")
 *         .numeroPiso("5") ...
 * </pre>
 *
 * @author Equipo Gestion de Inmuebles
 */
public class DatosInmueble {

    private String chip;
    private LocalDate fechaCompra;
    private String direccion;
    private String estado;
    private String identificacionPropietario;
    private String tipo;
    private String numeroPiso;
    private String cantidadPisos;
    private boolean tieneSotano;
    private String areaSotano;
    private String tipoUsoSotano;

    /**
     * @param chip CHIP del inmueble
     * @return este mismo objeto
     */
    public DatosInmueble chip(String chip) { this.chip = chip; return this; }

    /**
     * @param fechaCompra fecha de compra
     * @return este mismo objeto
     */
    public DatosInmueble fechaCompra(LocalDate fechaCompra) { this.fechaCompra = fechaCompra; return this; }

    /**
     * @param direccion direccion del inmueble
     * @return este mismo objeto
     */
    public DatosInmueble direccion(String direccion) { this.direccion = direccion; return this; }

    /**
     * @param estado estado del inmueble
     * @return este mismo objeto
     */
    public DatosInmueble estado(String estado) { this.estado = estado; return this; }

    /**
     * @param identificacionPropietario identificacion del propietario
     * @return este mismo objeto
     */
    public DatosInmueble identificacionPropietario(String identificacionPropietario) {
        this.identificacionPropietario = identificacionPropietario;
        return this;
    }

    /**
     * @param tipo {@code "CASA"} o {@code "APARTAMENTO"}
     * @return este mismo objeto
     */
    public DatosInmueble tipo(String tipo) { this.tipo = tipo; return this; }

    /**
     * @param numeroPiso piso del apartamento, como texto
     * @return este mismo objeto
     */
    public DatosInmueble numeroPiso(String numeroPiso) { this.numeroPiso = numeroPiso; return this; }

    /**
     * @param cantidadPisos pisos de la casa, como texto
     * @return este mismo objeto
     */
    public DatosInmueble cantidadPisos(String cantidadPisos) { this.cantidadPisos = cantidadPisos; return this; }

    /**
     * @param tieneSotano {@code true} si la casa tiene sotano
     * @return este mismo objeto
     */
    public DatosInmueble tieneSotano(boolean tieneSotano) { this.tieneSotano = tieneSotano; return this; }

    /**
     * @param areaSotano area del sotano en m2, como texto
     * @return este mismo objeto
     */
    public DatosInmueble areaSotano(String areaSotano) { this.areaSotano = areaSotano; return this; }

    /**
     * @param tipoUsoSotano uso del sotano
     * @return este mismo objeto
     */
    public DatosInmueble tipoUsoSotano(String tipoUsoSotano) { this.tipoUsoSotano = tipoUsoSotano; return this; }

    /** @return el CHIP */
    public String getChip() { return chip; }

    /** @return la fecha de compra */
    public LocalDate getFechaCompra() { return fechaCompra; }

    /** @return la direccion */
    public String getDireccion() { return direccion; }

    /** @return el estado */
    public String getEstado() { return estado; }

    /** @return la identificacion del propietario */
    public String getIdentificacionPropietario() { return identificacionPropietario; }

    /** @return el tipo de inmueble */
    public String getTipo() { return tipo; }

    /** @return el numero de piso, como texto */
    public String getNumeroPiso() { return numeroPiso; }

    /** @return la cantidad de pisos, como texto */
    public String getCantidadPisos() { return cantidadPisos; }

    /** @return {@code true} si tiene sotano */
    public boolean isTieneSotano() { return tieneSotano; }

    /** @return el area del sotano, como texto */
    public String getAreaSotano() { return areaSotano; }

    /** @return el uso del sotano */
    public String getTipoUsoSotano() { return tipoUsoSotano; }
}
