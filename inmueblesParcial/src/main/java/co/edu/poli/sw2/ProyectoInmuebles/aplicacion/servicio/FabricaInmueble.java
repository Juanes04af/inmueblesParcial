package co.edu.poli.sw2.ProyectoInmuebles.aplicacion.servicio;

import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.DatosInmueble;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.salida.PropietarioRepository;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.InmuebleValidacionException;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Apartamento;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Casa;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Inmueble;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Propietario;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Sotano;

/**
 * Construye una {@link Casa} o un {@link Apartamento} a partir de
 * {@link DatosInmueble}, validando cada dato con {@link ValidadorInmueble}.
 * <p>
 * La usan los servicios de crear y modificar, para no repetir las mismas
 * reglas en dos lugares. Si algun dato es invalido se lanza la excepcion
 * antes de llegar al repositorio.
 *
 * @author Equipo Gestion de Inmuebles
 */
final class FabricaInmueble {

    /**
     * Evita crear instancias.
     */
    private FabricaInmueble() {
    }

    /**
     * Valida los datos y construye el inmueble.
     *
     * @param chip         CHIP ya normalizado
     * @param datos        datos capturados
     * @param propietarios puerto para comprobar que el propietario exista
     * @return casa o apartamento listo para guardar
     * @throws InmuebleValidacionException si algun dato no es valido
     */
    static Inmueble construir(String chip, DatosInmueble datos, PropietarioRepository propietarios) {
        if (datos == null) {
            throw new InmuebleValidacionException("No se recibieron datos del inmueble.");
        }
        ValidadorInmueble.validarFechaCompra(datos.getFechaCompra());
        ValidadorInmueble.validarDireccion(datos.getDireccion());
        ValidadorInmueble.validarEstado(datos.getEstado());
        ValidadorInmueble.validarTipo(datos.getTipo());

        Propietario propietario = buscarPropietario(datos.getIdentificacionPropietario(), propietarios);
        String direccion = datos.getDireccion().trim();

        if ("APARTAMENTO".equals(datos.getTipo())) {
            int piso = ValidadorInmueble.parsearEnteroPositivo(datos.getNumeroPiso(), "numero de piso");
            return new Apartamento(chip, datos.getFechaCompra(), direccion,
                    datos.getEstado(), propietario, piso);
        }

        int pisos = ValidadorInmueble.parsearEnteroPositivo(datos.getCantidadPisos(), "cantidad de pisos");
        Sotano sotano = null;
        if (datos.isTieneSotano()) {
            double area = ValidadorInmueble.parsearArea(datos.getAreaSotano());
            ValidadorInmueble.validarTipoUso(datos.getTipoUsoSotano());
            sotano = new Sotano(area, datos.getTipoUsoSotano());
        }
        return new Casa(chip, datos.getFechaCompra(), direccion,
                datos.getEstado(), propietario, pisos, sotano);
    }

    /**
     * Obtiene el propietario o lanza una excepcion si no se eligio o no existe.
     *
     * @param identificacion identificacion seleccionada
     * @param propietarios   puerto de propietarios
     * @return propietario encontrado
     */
    private static Propietario buscarPropietario(String identificacion, PropietarioRepository propietarios) {
        if (identificacion == null || identificacion.isBlank()) {
            throw new InmuebleValidacionException("Seleccione el propietario del inmueble.");
        }
        return propietarios.buscarPorIdentificacion(identificacion.trim())
                .orElseThrow(() -> new InmuebleValidacionException(
                        "No existe un propietario con identificacion '" + identificacion + "'."));
    }
}
