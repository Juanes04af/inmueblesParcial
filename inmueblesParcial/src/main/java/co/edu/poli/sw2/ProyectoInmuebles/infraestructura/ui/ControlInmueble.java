package co.edu.poli.sw2.ProyectoInmuebles.infraestructura.ui;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.BuscarInmuebleUseCase;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.CrearInmuebleUseCase;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.DatosInmueble;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.EliminarInmuebleUseCase;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.ListarInmueblesUseCase;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.ListarPropietariosUseCase;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.ModificarInmuebleUseCase;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.InmuebleException;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Apartamento;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Casa;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Inmueble;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Propietario;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Sotano;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Adaptador de entrada JavaFX para la gestion de inmuebles.
 *
 * <p>
 * El controlador recibe los casos de uso de la aplicacion mediante
 * puertos de entrada y no conoce directamente la implementacion
 * de persistencia ni la base de datos. Solo traduce lo que hace el
 * usuario en llamadas a los casos de uso y muestra el resultado.
 * </p>
 *
 * @author Equipo Gestion de Inmuebles
 */
public class ControlInmueble implements Initializable {

    private static final String CASA = "CASA";
    private static final String APARTAMENTO = "APARTAMENTO";

    // ---------------------------------------------------------- formulario

    /** Campo del CHIP; tambien se usa para buscar. */
    @FXML private TextField txtChip;

    /** Selector de la fecha de compra. */
    @FXML private DatePicker dpFechaCompra;

    /** Campo de la direccion. */
    @FXML private TextField txtDireccion;

    /** Lista de estados posibles. */
    @FXML private ComboBox<String> cbEstado;

    /** Lista de propietarios para asignar al inmueble. */
    @FXML private ComboBox<Propietario> cbPropietario;

    /** Lista de tipos: CASA o APARTAMENTO. */
    @FXML private ComboBox<String> cbTipo;

    /** Seccion visible solo para apartamentos. */
    @FXML private VBox seccionApartamento;

    /** Campo del numero de piso del apartamento. */
    @FXML private TextField txtNumeroPiso;

    /** Seccion visible solo para casas. */
    @FXML private VBox seccionCasa;

    /** Campo de la cantidad de pisos de la casa. */
    @FXML private TextField txtCantidadPisos;

    /** Casilla que indica si la casa tiene sotano. */
    @FXML private CheckBox chkSotano;

    /** Seccion visible solo si la casa tiene sotano. */
    @FXML private VBox seccionSotano;

    /** Campo del area del sotano. */
    @FXML private TextField txtAreaSotano;

    /** Lista de usos del sotano. */
    @FXML private ComboBox<String> cbTipoUso;

    // ---------------------------------------------------------- tabla y filtro

    /** Filtro de propietario sobre la tabla. */
    @FXML private ComboBox<Propietario> cbFiltroPropietario;

    /** Etiqueta para mensajes al usuario. */
    @FXML private Label lblMensaje;

    /** Tabla principal de inmuebles. */
    @FXML private TableView<Inmueble> tablaInmuebles;

    /** Columna del CHIP. */
    @FXML private TableColumn<Inmueble, String> colChip;

    /** Columna del tipo. */
    @FXML private TableColumn<Inmueble, String> colTipo;

    /** Columna de la fecha de compra. */
    @FXML private TableColumn<Inmueble, String> colFechaCompra;

    /** Columna de la direccion. */
    @FXML private TableColumn<Inmueble, String> colDireccion;

    /** Columna del estado. */
    @FXML private TableColumn<Inmueble, String> colEstado;

    /** Columna del propietario. */
    @FXML private TableColumn<Inmueble, String> colPropietario;

    /** Columna con el detalle de la casa o del apartamento. */
    @FXML private TableColumn<Inmueble, String> colDetalle;

    // ---------------------------------------------------------- casos de uso

    private final CrearInmuebleUseCase crearInmuebleUseCase;
    private final ModificarInmuebleUseCase modificarInmuebleUseCase;
    private final EliminarInmuebleUseCase eliminarInmuebleUseCase;
    private final BuscarInmuebleUseCase buscarInmuebleUseCase;
    private final ListarInmueblesUseCase listarInmueblesUseCase;
    private final ListarPropietariosUseCase listarPropietariosUseCase;

    /** Inmueble seleccionado actualmente en la tabla. */
    private Inmueble inmuebleSeleccionado;

    /**
     * Construye el adaptador de entrada con todos los casos de uso
     * necesarios para gestionar inmuebles.
     *
     * @param crearInmuebleUseCase      caso de uso de creacion
     * @param modificarInmuebleUseCase  caso de uso de modificacion
     * @param eliminarInmuebleUseCase   caso de uso de eliminacion
     * @param buscarInmuebleUseCase     caso de uso de busqueda
     * @param listarInmueblesUseCase    caso de uso de listado
     * @param listarPropietariosUseCase caso de uso de consulta de propietarios
     */
    public ControlInmueble(CrearInmuebleUseCase crearInmuebleUseCase,
                           ModificarInmuebleUseCase modificarInmuebleUseCase,
                           EliminarInmuebleUseCase eliminarInmuebleUseCase,
                           BuscarInmuebleUseCase buscarInmuebleUseCase,
                           ListarInmueblesUseCase listarInmueblesUseCase,
                           ListarPropietariosUseCase listarPropietariosUseCase) {
        this.crearInmuebleUseCase = crearInmuebleUseCase;
        this.modificarInmuebleUseCase = modificarInmuebleUseCase;
        this.eliminarInmuebleUseCase = eliminarInmuebleUseCase;
        this.buscarInmuebleUseCase = buscarInmuebleUseCase;
        this.listarInmueblesUseCase = listarInmueblesUseCase;
        this.listarPropietariosUseCase = listarPropietariosUseCase;
    }

    /**
     * Inicializa los componentes JavaFX despues de cargar el FXML.
     *
     * @param location  ubicacion del recurso
     * @param resources recursos de internacionalizacion
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbEstado.setItems(FXCollections.observableArrayList(Inmueble.ESTADOS));
        cbTipo.setItems(FXCollections.observableArrayList(CASA, APARTAMENTO));
        cbTipoUso.setItems(FXCollections.observableArrayList(Sotano.TIPOS_USO));

        cbTipo.valueProperty().addListener((obs, anterior, nuevo) -> actualizarSecciones());
        chkSotano.selectedProperty().addListener((obs, anterior, nuevo) -> actualizarSecciones());

        configurarTabla();
        cargarPropietarios();
        listarInmuebles();
        actualizarSecciones();

        tablaInmuebles.getSelectionModel().selectedItemProperty()
                .addListener((observable, anterior, nuevo) -> {
                    if (nuevo != null) {
                        inmuebleSeleccionado = nuevo;
                        cargarFormulario(nuevo);
                    }
                });
    }

    /**
     * Configura las columnas de la tabla.
     */
    private void configurarTabla() {
        colChip.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getChip()));
        colTipo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTipo()));
        colFechaCompra.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFechaCompra().toString()));
        colDireccion.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDireccion()));
        colEstado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEstado()));
        colPropietario.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPropietario().getNombre()));
        colDetalle.setCellValueFactory(d -> new SimpleStringProperty(describirDetalle(d.getValue())));
    }

    // ---------------------------------------------------------- acciones CRUD

    /**
     * CREATE: ejecuta el caso de uso de creacion con los datos del formulario.
     */
    @FXML
    private void crearInmueble() {
        try {
            Inmueble creado = crearInmuebleUseCase.crear(leerFormulario());
            listarInmuebles();
            limpiarFormulario();
            mostrarMensaje("Inmueble " + creado.getChip() + " creado correctamente.", false);
        } catch (InmuebleException e) {
            mostrarMensaje(e.getMessage(), true);
        } catch (Exception e) {
            mostrarErrorInesperado(e);
        }
    }

    /**
     * READ: busca un inmueble por el CHIP escrito en el formulario.
     */
    @FXML
    private void buscarInmueble() {
        try {
            Inmueble inmueble = buscarInmuebleUseCase.buscarPorChip(txtChip.getText());
            tablaInmuebles.setItems(FXCollections.observableArrayList(inmueble));
            tablaInmuebles.getSelectionModel().select(inmueble);
            inmuebleSeleccionado = inmueble;
            cargarFormulario(inmueble);
            mostrarMensaje("Inmueble encontrado.", false);
        } catch (InmuebleException e) {
            mostrarMensaje(e.getMessage(), true);
        } catch (Exception e) {
            mostrarErrorInesperado(e);
        }
    }

    /**
     * UPDATE: ejecuta el caso de uso de modificacion sobre el inmueble seleccionado.
     */
    @FXML
    private void modificarInmueble() {
        try {
            if (inmuebleSeleccionado == null) {
                mostrarMensaje("Seleccione un inmueble de la tabla para modificar.", true);
                return;
            }
            Inmueble modificado = modificarInmuebleUseCase.modificar(inmuebleSeleccionado, leerFormulario());
            listarInmuebles();
            limpiarFormulario();
            mostrarMensaje("Inmueble " + modificado.getChip() + " modificado correctamente.", false);
        } catch (InmuebleException e) {
            mostrarMensaje(e.getMessage(), true);
        } catch (Exception e) {
            mostrarErrorInesperado(e);
        }
    }

    /**
     * DELETE: ejecuta el caso de uso de eliminacion, previa confirmacion.
     */
    @FXML
    private void eliminarInmueble() {
        try {
            if (inmuebleSeleccionado == null) {
                mostrarMensaje("Seleccione un inmueble de la tabla para eliminar.", true);
                return;
            }
            if (!confirmar("Eliminar el inmueble " + inmuebleSeleccionado.getChip() + "?")) {
                return;
            }
            String chip = inmuebleSeleccionado.getChip();
            eliminarInmuebleUseCase.eliminar(inmuebleSeleccionado);
            listarInmuebles();
            limpiarFormulario();
            mostrarMensaje("Inmueble " + chip + " eliminado correctamente.", false);
        } catch (InmuebleException e) {
            mostrarMensaje(e.getMessage(), true);
        } catch (Exception e) {
            mostrarErrorInesperado(e);
        }
    }

    /**
     * Muestra todos los inmuebles registrados.
     */
    @FXML
    private void listarInmuebles() {
        try {
            tablaInmuebles.setItems(FXCollections.observableArrayList(listarInmueblesUseCase.listar()));
        } catch (InmuebleException e) {
            mostrarMensaje(e.getMessage(), true);
        } catch (Exception e) {
            mostrarErrorInesperado(e);
        }
    }

    /**
     * Quita el filtro de propietario y vuelve a mostrar todos los inmuebles.
     */
    @FXML
    private void verTodos() {
        cbFiltroPropietario.setValue(null);
        listarInmuebles();
        mostrarMensaje("Mostrando todos los inmuebles.", false);
    }

    /**
     * Muestra solo los inmuebles del propietario elegido en el filtro
     * (caso de un propietario con varios inmuebles).
     */
    @FXML
    private void filtrarPorPropietario() {
        try {
            Propietario propietario = cbFiltroPropietario.getValue();
            if (propietario == null) {
                mostrarMensaje("Seleccione un propietario para filtrar.", true);
                return;
            }
            List<Inmueble> inmuebles =
                    listarInmueblesUseCase.listarPorPropietario(propietario.getIdentificacion());
            tablaInmuebles.setItems(FXCollections.observableArrayList(inmuebles));
            mostrarMensaje(propietario.getNombre() + " tiene " + inmuebles.size() + " inmueble(s).", false);
        } catch (InmuebleException e) {
            mostrarMensaje(e.getMessage(), true);
        } catch (Exception e) {
            mostrarErrorInesperado(e);
        }
    }

    /**
     * Limpia los campos del formulario y la seleccion de la tabla.
     */
    @FXML
    private void limpiarFormulario() {
        txtChip.clear();
        txtChip.setDisable(false);
        dpFechaCompra.setValue(null);
        txtDireccion.clear();
        cbEstado.setValue(null);
        cbPropietario.setValue(null);
        cbTipo.setValue(null);
        txtNumeroPiso.clear();
        txtCantidadPisos.clear();
        chkSotano.setSelected(false);
        txtAreaSotano.clear();
        cbTipoUso.setValue(null);
        tablaInmuebles.getSelectionModel().clearSelection();
        inmuebleSeleccionado = null;
        actualizarSecciones();
    }

    // ---------------------------------------------------------- auxiliares

    /**
     * Carga los propietarios en las listas de seleccion.
     */
    private void cargarPropietarios() {
        try {
            List<Propietario> propietarios = listarPropietariosUseCase.listar();
            cbPropietario.setItems(FXCollections.observableArrayList(propietarios));
            cbFiltroPropietario.setItems(FXCollections.observableArrayList(propietarios));
        } catch (InmuebleException e) {
            mostrarMensaje(e.getMessage(), true);
        } catch (Exception e) {
            mostrarErrorInesperado(e);
        }
    }

    /**
     * Lee los campos del formulario. No valida nada: eso lo hace el caso de uso.
     *
     * @return datos capturados
     */
    private DatosInmueble leerFormulario() {
        Propietario propietario = cbPropietario.getValue();
        return new DatosInmueble()
                .chip(txtChip.getText())
                .fechaCompra(dpFechaCompra.getValue())
                .direccion(txtDireccion.getText())
                .estado(cbEstado.getValue())
                .identificacionPropietario(propietario == null ? null : propietario.getIdentificacion())
                .tipo(cbTipo.getValue())
                .numeroPiso(txtNumeroPiso.getText())
                .cantidadPisos(txtCantidadPisos.getText())
                .tieneSotano(chkSotano.isSelected())
                .areaSotano(txtAreaSotano.getText())
                .tipoUsoSotano(cbTipoUso.getValue());
    }

    /**
     * Carga en el formulario los datos de un inmueble seleccionado.
     * El CHIP queda bloqueado porque no se puede modificar.
     *
     * @param inmueble inmueble que sera mostrado
     */
    private void cargarFormulario(Inmueble inmueble) {
        txtChip.setText(inmueble.getChip());
        txtChip.setDisable(true);
        dpFechaCompra.setValue(inmueble.getFechaCompra());
        txtDireccion.setText(inmueble.getDireccion());
        cbEstado.setValue(inmueble.getEstado());
        cbPropietario.setValue(inmueble.getPropietario());
        cbTipo.setValue(inmueble.getTipo());

        txtNumeroPiso.clear();
        txtCantidadPisos.clear();
        chkSotano.setSelected(false);
        txtAreaSotano.clear();
        cbTipoUso.setValue(null);

        if (inmueble instanceof Apartamento) {
            txtNumeroPiso.setText(String.valueOf(((Apartamento) inmueble).getNumeroPiso()));
        } else if (inmueble instanceof Casa) {
            Casa casa = (Casa) inmueble;
            txtCantidadPisos.setText(String.valueOf(casa.getCantidadPisos()));
            if (casa.tieneSotano()) {
                chkSotano.setSelected(true);
                txtAreaSotano.setText(String.valueOf(casa.getSotano().getAreaM2()));
                cbTipoUso.setValue(casa.getSotano().getTipoUso());
            }
        }
        actualizarSecciones();
    }

    /**
     * Muestra u oculta las secciones segun el tipo y si la casa tiene sotano.
     */
    private void actualizarSecciones() {
        boolean esCasa = CASA.equals(cbTipo.getValue());
        mostrar(seccionCasa, esCasa);
        mostrar(seccionApartamento, APARTAMENTO.equals(cbTipo.getValue()));
        mostrar(seccionSotano, esCasa && chkSotano.isSelected());
    }

    /**
     * Muestra u oculta una seccion sin dejar espacio vacio.
     *
     * @param seccion seccion del formulario
     * @param visible {@code true} para mostrarla
     */
    private void mostrar(VBox seccion, boolean visible) {
        seccion.setVisible(visible);
        seccion.setManaged(visible);
    }

    /**
     * Arma el texto de la columna "Detalle".
     *
     * @param inmueble inmueble de la fila
     * @return descripcion corta del detalle
     */
    private String describirDetalle(Inmueble inmueble) {
        if (inmueble instanceof Apartamento) {
            return "Piso " + ((Apartamento) inmueble).getNumeroPiso();
        }
        Casa casa = (Casa) inmueble;
        String texto = casa.getCantidadPisos() + " piso(s)";
        if (!casa.tieneSotano()) {
            return texto + ", sin sotano";
        }
        return texto + ", sotano " + casa.getSotano().getAreaM2() + " m2 (" + casa.getSotano().getTipoUso() + ")";
    }

    /**
     * Pide confirmacion al usuario.
     *
     * @param pregunta texto de la pregunta
     * @return {@code true} si el usuario acepta
     */
    private boolean confirmar(String pregunta) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, pregunta, ButtonType.YES, ButtonType.NO);
        alerta.setTitle("Confirmar");
        alerta.setHeaderText(null);
        Optional<ButtonType> respuesta = alerta.showAndWait();
        return respuesta.isPresent() && respuesta.get() == ButtonType.YES;
    }

    /**
     * Presenta un mensaje informativo o de error dentro de la interfaz.
     *
     * @param mensaje contenido que se mostrara
     * @param error   indica si corresponde a un error
     */
    private void mostrarMensaje(String mensaje, boolean error) {
        lblMensaje.setText(mensaje);
        lblMensaje.getStyleClass().removeAll("mensaje-ok", "mensaje-error");
        lblMensaje.getStyleClass().add(error ? "mensaje-error" : "mensaje-ok");
    }

    /**
     * Presenta mediante JavaFX un error inesperado.
     *
     * @param excepcion excepcion producida
     */
    private void mostrarErrorInesperado(Exception excepcion) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Ocurrio un error inesperado");
        alert.setContentText(excepcion.getMessage());
        alert.showAndWait();
    }
}
