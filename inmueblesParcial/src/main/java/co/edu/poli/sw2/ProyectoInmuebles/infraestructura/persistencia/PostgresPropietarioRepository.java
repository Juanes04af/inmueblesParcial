package co.edu.poli.sw2.ProyectoInmuebles.infraestructura.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.salida.PropietarioRepository;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.ConexionBDException;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Propietario;

/**
 * Adaptador de salida que consulta los propietarios en PostgreSQL.
 *
 * @author Equipo Gestion de Inmuebles
 */
public class PostgresPropietarioRepository implements PropietarioRepository {

    private static final String SELECT_BASE =
            "SELECT identificacion, nombre, correo_electronico FROM propietario";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Propietario> listar() {
        List<Propietario> propietarios = new ArrayList<>();
        try (Connection conexion = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(SELECT_BASE + " ORDER BY nombre");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                propietarios.add(mapear(rs));
            }
            return propietarios;
        } catch (SQLException e) {
            throw new ConexionBDException("No fue posible listar los propietarios.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Propietario> buscarPorIdentificacion(String identificacion) {
        try (Connection conexion = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(SELECT_BASE + " WHERE identificacion = ?")) {
            ps.setString(1, identificacion);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new ConexionBDException("No fue posible buscar el propietario.", e);
        }
    }

    /**
     * Convierte una fila de PostgreSQL en un {@link Propietario}.
     *
     * @param rs resultado SQL
     * @return propietario construido
     * @throws SQLException si ocurre un error de lectura
     */
    private Propietario mapear(ResultSet rs) throws SQLException {
        return new Propietario(rs.getString("identificacion"), rs.getString("nombre"),
                rs.getString("correo_electronico"));
    }
}
