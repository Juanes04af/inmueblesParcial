package co.edu.poli.sw2.ProyectoInmuebles.aplicacion.servicio;

import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.DatosInmueble;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.ModificarInmuebleUseCase;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.salida.InmuebleRepository;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.salida.PropietarioRepository;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.InmuebleValidacionException;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Inmueble;

/**
 * Servicio encargado de modificar inmuebles.
 * <p>
 * El CHIP es la identidad del inmueble y no se puede cambiar. Todo lo
 * demas se reemplaza, incluso el tipo (una casa puede pasar a apartamento)
 * y el sotano. Si un dato es invalido, el inmueble original no se altera.
 *
 * @author Equipo Gestion de Inmuebles
 */
public class ModificarInmuebleServicio implements ModificarInmuebleUseCase {

    private final InmuebleRepository inmuebleRepository;
    private final PropietarioRepository propietarioRepository;

    /**
     * Construye el servicio.
     *
     * @param inmuebleRepository    puerto de salida para persistir inmuebles
     * @param propietarioRepository puerto de salida para consultar propietarios
     */
    public ModificarInmuebleServicio(InmuebleRepository inmuebleRepository,
                                     PropietarioRepository propietarioRepository) {
        this.inmuebleRepository = inmuebleRepository;
        this.propietarioRepository = propietarioRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Inmueble modificar(Inmueble existente, DatosInmueble datos) {
        if (existente == null) {
            throw new InmuebleValidacionException("Seleccione un inmueble para modificar.");
        }
        if (datos != null && datos.getChip() != null && !datos.getChip().isBlank()
                && !ValidadorInmueble.normalizarChip(datos.getChip()).equals(existente.getChip())) {
            throw new InmuebleValidacionException(
                    "El CHIP no se puede modificar. Para cambiarlo elimine el inmueble y creelo de nuevo.");
        }
        Inmueble modificado = FabricaInmueble.construir(existente.getChip(), datos, propietarioRepository);
        return inmuebleRepository.modificar(modificado);
    }
}
