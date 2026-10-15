package co.edu.poli.sw2.ProyectoInmuebles.aplicacion.servicio;

import java.util.List;

import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.ListarInmueblesUseCase;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.salida.InmuebleRepository;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.InmuebleValidacionException;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Inmueble;

/**
 * Servicio encargado de listar inmuebles, todos o los de un propietario.
 *
 * @author Equipo Gestion de Inmuebles
 */
public class ListarInmueblesServicio implements ListarInmueblesUseCase {

    private final InmuebleRepository inmuebleRepository;

    /**
     * Construye el servicio.
     *
     * @param inmuebleRepository puerto de salida utilizado
     */
    public ListarInmueblesServicio(InmuebleRepository inmuebleRepository) {
        this.inmuebleRepository = inmuebleRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Inmueble> listar() {
        return inmuebleRepository.listar();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Inmueble> listarPorPropietario(String identificacionPropietario) {
        if (identificacionPropietario == null || identificacionPropietario.isBlank()) {
            throw new InmuebleValidacionException("Seleccione un propietario.");
        }
        return inmuebleRepository.listarPorPropietario(identificacionPropietario.trim());
    }
}
