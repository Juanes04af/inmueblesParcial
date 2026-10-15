package co.edu.poli.sw2.ProyectoInmuebles.aplicacion.servicio;

import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.EliminarInmuebleUseCase;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.salida.InmuebleRepository;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.InmuebleValidacionException;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Inmueble;

/**
 * Servicio encargado de eliminar un inmueble.
 *
 * @author Equipo Gestion de Inmuebles
 */
public class EliminarInmuebleServicio implements EliminarInmuebleUseCase {

    private final InmuebleRepository inmuebleRepository;

    /**
     * Construye el servicio.
     *
     * @param inmuebleRepository puerto de salida utilizado
     */
    public EliminarInmuebleServicio(InmuebleRepository inmuebleRepository) {
        this.inmuebleRepository = inmuebleRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void eliminar(Inmueble inmueble) {
        if (inmueble == null) {
            throw new InmuebleValidacionException("Seleccione un inmueble para eliminar.");
        }
        inmuebleRepository.eliminar(inmueble);
    }
}
