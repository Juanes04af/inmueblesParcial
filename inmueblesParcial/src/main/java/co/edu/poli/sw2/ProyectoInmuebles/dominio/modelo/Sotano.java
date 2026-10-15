package co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Sotano de una {@link Casa}.
 * <p>
 * Es parte de la casa (composicion): no existe por si solo.
 * Se guarda en la tabla {@code sotano}, cuya llave es el CHIP de la casa.
 *
 * @author Equipo Gestion de Inmuebles
 */
public class Sotano implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Usos validos de un sotano. */
    public static final List<String> TIPOS_USO = Collections.unmodifiableList(
            Arrays.asList("PARQUEADERO", "BODEGA", "VIVIENDA", "OTRO"));

    /** Area del sotano en metros cuadrados. */
    private double areaM2;

    /** Uso que se le da al sotano; uno de {@link #TIPOS_USO}. */
    private String tipoUso;

    /**
     * Crea un sotano.
     *
     * @param areaM2  area en metros cuadrados
     * @param tipoUso tipo de uso
     */
    public Sotano(double areaM2, String tipoUso) {
        this.areaM2 = areaM2;
        this.tipoUso = tipoUso;
    }

    /**
     * Obtiene el area.
     *
     * @return area en metros cuadrados
     */
    public double getAreaM2() { return areaM2; }

    /**
     * Modifica el area.
     *
     * @param areaM2 nueva area en metros cuadrados
     */
    public void setAreaM2(double areaM2) { this.areaM2 = areaM2; }

    /**
     * Obtiene el tipo de uso.
     *
     * @return tipo de uso
     */
    public String getTipoUso() { return tipoUso; }

    /**
     * Modifica el tipo de uso.
     *
     * @param tipoUso nuevo tipo de uso
     */
    public void setTipoUso(String tipoUso) { this.tipoUso = tipoUso; }
}
