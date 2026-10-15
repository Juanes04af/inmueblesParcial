package co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entidad que representa al propietario de uno o varios inmuebles.
 * <p>
 * Un propietario puede tener muchos inmuebles (relacion 1 a 0..*).
 * Los propietarios vienen predeterminados en la tabla {@code propietario};
 * el sistema solo los consulta, no tiene CRUD sobre ellos.
 *
 * @author Equipo Gestion de Inmuebles
 */
public class Propietario implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Numero de identificacion (cedula o NIT). Es la llave del propietario. */
    private String identificacion;

    /** Nombre completo del propietario. */
    private String nombre;

    /** Correo electronico de contacto. */
    private String correoElectronico;

    /**
     * Crea un propietario con todos sus atributos.
     *
     * @param identificacion    numero de identificacion
     * @param nombre            nombre completo
     * @param correoElectronico correo electronico de contacto
     */
    public Propietario(String identificacion, String nombre, String correoElectronico) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
    }

    /**
     * Devuelve la identificacion del propietario.
     *
     * @return el numero de identificacion
     */
    public String getIdentificacion() { return identificacion; }

    /**
     * Devuelve el nombre del propietario.
     *
     * @return el nombre completo
     */
    public String getNombre() { return nombre; }

    /**
     * Devuelve el correo electronico del propietario.
     *
     * @return el correo de contacto
     */
    public String getCorreoElectronico() { return correoElectronico; }

    /**
     * Dos propietarios son iguales si tienen la misma identificacion.
     * Esto permite que los ComboBox de la interfaz seleccionen el
     * propietario correcto aunque el objeto venga de otra consulta.
     *
     * @param o objeto a comparar
     * @return {@code true} si representan al mismo propietario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Propietario)) {
            return false;
        }
        return Objects.equals(identificacion, ((Propietario) o).identificacion);
    }

    /**
     * Calcula el hash a partir de la identificacion.
     *
     * @return valor hash
     */
    @Override
    public int hashCode() { return Objects.hash(identificacion); }

    /**
     * Devuelve una descripcion legible del propietario. Es el formato
     * que se muestra en los controles de seleccion de la interfaz.
     *
     * @return el nombre seguido de la identificacion
     */
    @Override
    public String toString() { return nombre + " (" + identificacion + ")"; }
}
