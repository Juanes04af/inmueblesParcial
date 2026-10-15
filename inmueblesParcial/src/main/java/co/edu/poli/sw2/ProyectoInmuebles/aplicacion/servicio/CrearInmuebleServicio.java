package co.edu.poli.sw2.ProyectoInmuebles.aplicacion.servicio;

import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.CrearInmuebleUseCase;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.DatosInmueble;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.salida.InmuebleRepository;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.salida.PropietarioRepository;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.InmuebleValidacionException;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Inmueble;

/**
 * Servicio de aplicacion encargado de crear inmuebles.
 * <p>
 * Valida los datos, construye la casa o el apartamento y lo guarda
 * a traves del puerto de salida.
 *
 * @author Equipo Gestion de Inmuebles
 */
public class CrearInmuebleServicio implements CrearInmuebleUseCase {

    private final InmuebleRepository inmuebleRepository;
    private final PropietarioRepository propietarioRepository;

    /**
     * Construye el servicio.
     *
     * @param inmuebleRepository    puerto de salida para persistir inmuebles
     * @param propietarioRepository puerto de salida para consultar propietarios
     */
    public CrearInmuebleServicio(InmuebleRepository inmuebleRepository,
                                 PropietarioRepository propietarioRepository) {
        this.inmuebleRepository = inmuebleRepository;
        this.propietarioRepository = propietarioRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Inmueble crear(DatosInmueble datos) {
        if (datos == null) {
            throw new InmuebleValidacionException("No se recibieron datos del inmueble.");
        }
        String chip = ValidadorInmueble.normalizarChip(datos.getChip());
        Inmueble inmueble = FabricaInmueble.construir(chip, datos, propietarioRepository);
        return inmuebleRepository.crear(inmueble);
    }
}
