package co.edu.poli.sw2.ProyectoInmuebles.infraestructura.web;

/**
 * Clase de arranque de la aplicacion.
 *
 * <p>
 * Existe porque el proyecto se ejecuta en el classpath (sin
 * {@code module-info.java}). Si la clase principal extiende
 * {@link javafx.application.Application}, Java exige que JavaFX este en
 * el module-path y falla con "JavaFX runtime components are missing".
 * Lanzando desde una clase que no extiende {@code Application} se evita
 * ese problema y la aplicacion funciona igual en Eclipse, NetBeans y Maven.
 * </p>
 *
 * @author Equipo Gestion de Inmuebles
 */
public final class Launcher {

    /**
     * Evita crear instancias.
     */
    private Launcher() {
    }

    /**
     * Inicia la aplicacion delegando en {@link App}.
     *
     * @param args argumentos de linea de comandos
     */
    public static void main(String[] args) {
        App.main(args);
    }
}
