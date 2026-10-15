package co.edu.poli.sw2.ProyectoInmuebles.aplicacion.servicio;

import java.time.LocalDate;
import java.util.Locale;
import java.util.regex.Pattern;

import co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.InmuebleValidacionException;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Inmueble;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Sotano;

/**
 * Contiene las reglas de validacion utilizadas por
 * los casos de uso relacionados con inmuebles.
 *
 * @author Equipo Gestion de Inmuebles
 */
public final class ValidadorInmueble {

    /** CHIP: solo letras y numeros, entre 5 y 20 caracteres. */
    private static final Pattern FORMATO_CHIP = Pattern.compile("^[A-Z0-9]{5,20}$");

    private static final Pattern TIENE_ALFANUMERICO = Pattern.compile("[A-Za-z0-9]");

    /** Longitud maxima de la direccion (igual a la columna de la BD). */
    private static final int MAX_DIRECCION = 150;

    /**
     * Evita crear instancias del validador.
     */
    private ValidadorInmueble() {
    }

    /**
     * Valida el CHIP y lo normaliza (sin espacios y en mayusculas).
     *
     * @param chip CHIP capturado
     * @return CHIP normalizado
     * @throws InmuebleValidacionException si esta vacio o no tiene el formato esperado
     */
    public static String normalizarChip(String chip) {
        if (chip == null || chip.isBlank()) {
            throw new InmuebleValidacionException("El CHIP es obligatorio.");
        }
        String normalizado = chip.trim().toUpperCase(Locale.ROOT);
        if (!FORMATO_CHIP.matcher(normalizado).matches()) {
            throw new InmuebleValidacionException(
                    "El CHIP debe tener entre 5 y 20 letras o numeros, sin espacios ni simbolos.");
        }
        return normalizado;
    }

    /**
     * Valida la fecha de compra.
     *
     * @param fecha fecha capturada
     * @throws InmuebleValidacionException si es nula o futura
     */
    public static void validarFechaCompra(LocalDate fecha) {
        if (fecha == null) {
            throw new InmuebleValidacionException("La fecha de compra es obligatoria.");
        }
        if (fecha.isAfter(LocalDate.now())) {
            throw new InmuebleValidacionException("La fecha de compra no puede ser futura.");
        }
    }

    /**
     * Valida la direccion.
     *
     * @param direccion direccion capturada
     * @throws InmuebleValidacionException si esta vacia, no tiene letras ni numeros o es muy larga
     */
    public static void validarDireccion(String direccion) {
        if (direccion == null || direccion.isBlank()) {
            throw new InmuebleValidacionException("La direccion es obligatoria.");
        }
        if (!TIENE_ALFANUMERICO.matcher(direccion).find()) {
            throw new InmuebleValidacionException("La direccion debe contener letras o numeros.");
        }
        if (direccion.trim().length() > MAX_DIRECCION) {
            throw new InmuebleValidacionException(
                    "La direccion no puede superar " + MAX_DIRECCION + " caracteres.");
        }
    }

    /**
     * Valida que el estado sea uno de {@link Inmueble#ESTADOS}.
     *
     * @param estado estado capturado
     * @throws InmuebleValidacionException si no es valido
     */
    public static void validarEstado(String estado) {
        if (estado == null || !Inmueble.ESTADOS.contains(estado)) {
            throw new InmuebleValidacionException(
                    "Seleccione un estado valido: " + String.join(", ", Inmueble.ESTADOS) + ".");
        }
    }

    /**
     * Valida el tipo de inmueble.
     *
     * @param tipo tipo capturado
     * @throws InmuebleValidacionException si no es CASA ni APARTAMENTO
     */
    public static void validarTipo(String tipo) {
        if (!"CASA".equals(tipo) && !"APARTAMENTO".equals(tipo)) {
            throw new InmuebleValidacionException("Seleccione el tipo de inmueble: CASA o APARTAMENTO.");
        }
    }

    /**
     * Valida que el uso del sotano sea uno de {@link Sotano#TIPOS_USO}.
     *
     * @param tipoUso uso capturado
     * @throws InmuebleValidacionException si no es valido
     */
    public static void validarTipoUso(String tipoUso) {
        if (tipoUso == null || !Sotano.TIPOS_USO.contains(tipoUso)) {
            throw new InmuebleValidacionException(
                    "Seleccione el uso del sotano: " + String.join(", ", Sotano.TIPOS_USO) + ".");
        }
    }

    /**
     * Convierte y valida un numero entero mayor o igual a 1
     * (numero de piso o cantidad de pisos).
     *
     * @param texto valor capturado
     * @param campo nombre del campo para el mensaje de error
     * @return valor convertido
     * @throws InmuebleValidacionException si esta vacio, no es entero o es menor que 1
     */
    public static int parsearEnteroPositivo(String texto, String campo) {
        if (texto == null || texto.isBlank()) {
            throw new InmuebleValidacionException("El campo '" + campo + "' es obligatorio.");
        }
        try {
            int valor = Integer.parseInt(texto.trim());
            if (valor < 1) {
                throw new InmuebleValidacionException("El campo '" + campo + "' debe ser mayor o igual a 1.");
            }
            return valor;
        } catch (NumberFormatException e) {
            throw new InmuebleValidacionException("El campo '" + campo + "' debe ser un numero entero.");
        }
    }

    /**
     * Convierte y valida el area del sotano. Acepta coma o punto decimal.
     *
     * @param texto valor capturado
     * @return area en metros cuadrados
     * @throws InmuebleValidacionException si esta vacia, no es numero o no es mayor que cero
     */
    public static double parsearArea(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new InmuebleValidacionException("El area del sotano es obligatoria.");
        }
        double area;
        try {
            area = Double.parseDouble(texto.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new InmuebleValidacionException("El area del sotano debe ser un numero valido.");
        }
        if (Double.isNaN(area) || Double.isInfinite(area)) {
            throw new InmuebleValidacionException("El area del sotano debe ser un numero valido.");
        }
        if (area <= 0) {
            throw new InmuebleValidacionException("El area del sotano debe ser mayor que cero.");
        }
        return area;
    }
}
