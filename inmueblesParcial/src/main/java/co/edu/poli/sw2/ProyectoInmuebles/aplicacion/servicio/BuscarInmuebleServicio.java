package co.edu.poli.sw2.ProyectoInmuebles.aplicacion.servicio;

import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.BuscarInmuebleUseCase;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.salida.InmuebleRepository;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Inmueble;

/**
 * Servicio encargado de buscar un inmueble por su CHIP.
 *
 * @author Equipo Gestion de Inmuebles
 */
public class BuscarInmuebleServicio implements BuscarInmuebleUseCase {

    private final InmuebleRepository inmuebleRepository;

    /**
     * Construye el servicio.
     *
     * @param inmuebleRepository puerto de salida utilizado
     */
    public BuscarInmuebleServicio(InmuebleRepository inmuebleRepository) {
        this.inmuebleRepository = inmuebleRepository;
    }

    /**
     * {@inheritDoc}
     * <p>
     * El CHIP se normaliza antes de buscar, asi " aaa0001aaaa " encuentra
     * el inmueble "AAA0001AAAA".
     */
    @Override
    public Inmueble buscarPorChip(String chip) {
        return inmuebleRepository.buscarPorChip(ValidadorInmueble.normalizarChip(chip));
    }
}
