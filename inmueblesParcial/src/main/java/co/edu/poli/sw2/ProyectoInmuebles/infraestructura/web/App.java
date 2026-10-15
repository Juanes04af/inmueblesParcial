package co.edu.poli.sw2.ProyectoInmuebles.infraestructura.web;

import java.io.IOException;

import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.BuscarInmuebleUseCase;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.CrearInmuebleUseCase;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.EliminarInmuebleUseCase;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.ListarInmueblesUseCase;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.ListarPropietariosUseCase;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.ModificarInmuebleUseCase;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.salida.InmuebleRepository;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.salida.PropietarioRepository;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.servicio.BuscarInmuebleServicio;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.servicio.CrearInmuebleServicio;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.servicio.EliminarInmuebleServicio;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.servicio.ListarInmueblesServicio;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.servicio.ListarPropietariosServicio;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.servicio.ModificarInmuebleServicio;
import co.edu.poli.sw2.ProyectoInmuebles.infraestructura.persistencia.PostgresInmuebleRepository;
import co.edu.poli.sw2.ProyectoInmuebles.infraestructura.persistencia.PostgresPropietarioRepository;
import co.edu.poli.sw2.ProyectoInmuebles.infraestructura.ui.ControlInmueble;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Punto de entrada principal de la aplicacion.
 *
 * <p>
 * Esta clase realiza el wiring de la arquitectura hexagonal.
 * Construye los adaptadores de salida, los servicios de aplicacion
 * y finalmente el adaptador de entrada JavaFX. Es el unico lugar
 * que conoce las clases concretas.
 * </p>
 *
 * @author Equipo Gestion de Inmuebles
 */
public class App extends Application {

    /**
     * Construye las dependencias de la aplicacion y carga la interfaz JavaFX.
     *
     * @param stage escenario principal de JavaFX
     * @throws IOException si no es posible cargar el archivo FXML
     */
    @Override
    public void start(Stage stage) throws IOException {

        /*
         * ADAPTADORES DE SALIDA
         */
        InmuebleRepository inmuebleRepository = new PostgresInmuebleRepository();
        PropietarioRepository propietarioRepository = new PostgresPropietarioRepository();

        /*
         * SERVICIOS / CASOS DE USO
         */
        CrearInmuebleUseCase crearInmueble =
                new CrearInmuebleServicio(inmuebleRepository, propietarioRepository);
        ModificarInmuebleUseCase modificarInmueble =
                new ModificarInmuebleServicio(inmuebleRepository, propietarioRepository);
        EliminarInmuebleUseCase eliminarInmueble = new EliminarInmuebleServicio(inmuebleRepository);
        BuscarInmuebleUseCase buscarInmueble = new BuscarInmuebleServicio(inmuebleRepository);
        ListarInmueblesUseCase listarInmuebles = new ListarInmueblesServicio(inmuebleRepository);
        ListarPropietariosUseCase listarPropietarios = new ListarPropietariosServicio(propietarioRepository);

        /*
         * CARGADOR FXML
         */
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/poli/sw2/vista/inmueble.fxml"));

        /*
         * INYECCION DE LOS PUERTOS EN EL ADAPTADOR DE ENTRADA
         */
        loader.setControllerFactory(controllerClass -> {
            if (controllerClass == ControlInmueble.class) {
                return new ControlInmueble(crearInmueble, modificarInmueble, eliminarInmueble,
                        buscarInmueble, listarInmuebles, listarPropietarios);
            }
            try {
                return controllerClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("No fue posible crear el controlador.", e);
            }
        });

        Parent root = loader.load();
        Scene scene = new Scene(root, 1180, 720);

        stage.setTitle("Gestion de Inmuebles - Arquitectura Hexagonal");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Inicia la aplicacion JavaFX.
     *
     * @param args argumentos de linea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }
}
