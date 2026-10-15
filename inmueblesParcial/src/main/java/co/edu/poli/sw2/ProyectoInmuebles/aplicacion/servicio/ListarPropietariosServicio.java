package co.edu.poli.sw2.ProyectoInmuebles.aplicacion.servicio;

import java.util.List;

import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.entrada.ListarPropietariosUseCase;
import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.salida.PropietarioRepository;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Propietario;

/**
 * Servicio encargado de consultar los propietarios predeterminados.
 *
 * @author Equipo Gestion de Inmuebles
 */
public class ListarPropietariosServicio implements ListarPropietariosUseCase {

    private final PropietarioRepository propietarioRepository;

    /**
     * Construye el servicio.
     *
     * @param propietarioRepository puerto de salida utilizado
     */
    public ListarPropietariosServicio(PropietarioRepository propietarioRepository) {
        this.propietarioRepository = propietarioRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Propietario> listar() {
        return propietarioRepository.listar();
    }
}
