package co.edu.poli.sw2.ProyectoInmuebles.infraestructura.persistencia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.poli.sw2.ProyectoInmuebles.aplicacion.puerto.salida.InmuebleRepository;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.ConexionBDException;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.InmuebleDuplicadoException;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.InmuebleNoEncontradoException;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.excepcion.InmuebleValidacionException;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Apartamento;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Casa;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Inmueble;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Propietario;
import co.edu.poli.sw2.ProyectoInmuebles.dominio.modelo.Sotano;

/**
 * Adaptador de salida que implementa la persistencia de inmuebles
 * utilizando PostgreSQL y JDBC.
 *
 * <p>
 * La herencia se guarda con una tabla por clase: {@code inmueble} tiene los
 * datos comunes, y {@code casa} / {@code apartamento} los propios, unidas
 * por el CHIP. El sotano va en la tabla {@code sotano}. Crear y modificar se
 * hacen en una transaccion, para que un fallo parcial no deje registros
 * huerfanos. Los errores de PostgreSQL se traducen a excepciones de dominio
 * segun su SQLState.
 * </p>
 *
 * @author Equipo Gestion de Inmuebles
 */
public class PostgresInmuebleRepository implements InmuebleRepository {

    private static final String SQLSTATE_UNIQUE = "23505";
    private static final String SQLSTATE_FOREIGN_KEY = "23503";
    private static final String SQLSTATE_CHECK = "23514";
    private static final String SQLSTATE_NOT_NULL = "23502";

    /** Consulta que une todas las tablas para reconstruir el inmueble completo. */
    private static final String SELECT_BASE =
            "SELECT i.chip, i.fecha_compra, i.direccion, i.estado, i.tipo, "
          + "p.identificacion, p.nombre, p.correo_electronico, "
          + "a.numero_piso, c.cantidad_pisos, s.area_m2, s.tipo_uso "
          + "FROM inmueble i "
          + "INNER JOIN propietario p ON p.identificacion = i.propietario_id "
          + "LEFT JOIN apartamento a ON a.chip = i.chip "
          + "LEFT JOIN casa c ON c.chip = i.chip "
          + "LEFT JOIN sotano s ON s.casa_chip = i.chip ";

    /**
     * {@inheritDoc}
     */
    @Override
    public Inmueble crear(Inmueble inmueble) {
        String sql = "INSERT INTO inmueble "
                   + "(chip, fecha_compra, direccion, estado, tipo, propietario_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        Connection conexion = null;
        try {
            conexion = ConexionBD.getInstancia().obtenerConexion();
            conexion.setAutoCommit(false);

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, inmueble.getChip());
                ps.setDate(2, Date.valueOf(inmueble.getFechaCompra()));
                ps.setString(3, inmueble.getDireccion());
                ps.setString(4, inmueble.getEstado());
                ps.setString(5, inmueble.getTipo());
                ps.setString(6, inmueble.getPropietario().getIdentificacion());
                ps.executeUpdate();
            }
            insertarDetalle(conexion, inmueble);

            conexion.commit();
            return inmueble;

        } catch (SQLException e) {
            rollback(conexion);
            throw traducirExcepcion(e, inmueble.getChip(), "No fue posible guardar el inmueble.");
        } catch (RuntimeException e) {
            rollback(conexion);
            throw e;
        } finally {
            cerrar(conexion);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Actualiza los datos comunes, borra el detalle anterior y lo vuelve a
     * insertar. Asi tambien funciona si el inmueble cambia de tipo o si se
     * agrega o quita el sotano.
     */
    @Override
    public Inmueble modificar(Inmueble inmueble) {
        String sql = "UPDATE inmueble "
                   + "SET fecha_compra=?, direccion=?, estado=?, tipo=?, propietario_id=? "
                   + "WHERE chip=?";
        Connection conexion = null;
        try {
            conexion = ConexionBD.getInstancia().obtenerConexion();
            conexion.setAutoCommit(false);

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(inmueble.getFechaCompra()));
                ps.setString(2, inmueble.getDireccion());
                ps.setString(3, inmueble.getEstado());
                ps.setString(4, inmueble.getTipo());
                ps.setString(5, inmueble.getPropietario().getIdentificacion());
                ps.setString(6, inmueble.getChip());
                if (ps.executeUpdate() == 0) {
                    throw new InmuebleNoEncontradoException(inmueble.getChip());
                }
            }
            borrarDetalle(conexion, inmueble.getChip());
            insertarDetalle(conexion, inmueble);

            conexion.commit();
            return inmueble;

        } catch (SQLException e) {
            rollback(conexion);
            throw traducirExcepcion(e, inmueble.getChip(), "No fue posible modificar el inmueble.");
        } catch (RuntimeException e) {
            rollback(conexion);
            throw e;
        } finally {
            cerrar(conexion);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Las tablas hijas tienen {@code ON DELETE CASCADE}, asi que basta
     * con borrar la fila de {@code inmueble}.
     */
    @Override
    public void eliminar(Inmueble inmueble) {
        String sql = "DELETE FROM inmueble WHERE chip=?";
        try (Connection conexion = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, inmueble.getChip());
            if (ps.executeUpdate() == 0) {
                throw new InmuebleNoEncontradoException(inmueble.getChip());
            }
        } catch (SQLException e) {
            throw new ConexionBDException("No fue posible eliminar el inmueble.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Inmueble buscarPorChip(String chip) {
        List<Inmueble> encontrados = consultar(SELECT_BASE + "WHERE i.chip=?", chip);
        if (encontrados.isEmpty()) {
            throw new InmuebleNoEncontradoException(chip);
        }
        return encontrados.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Inmueble> listar() {
        return consultar(SELECT_BASE + "ORDER BY i.chip");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Inmueble> listarPorPropietario(String identificacionPropietario) {
        return consultar(SELECT_BASE + "WHERE i.propietario_id=? ORDER BY i.chip", identificacionPropietario);
    }

    /**
     * Inserta los datos propios de la subclase y, si es una casa con sotano,
     * tambien el sotano.
     *
     * @param conexion conexion dentro de la transaccion
     * @param inmueble inmueble a guardar
     * @throws SQLException si falla algun insert
     */
    private void insertarDetalle(Connection conexion, Inmueble inmueble) throws SQLException {
        if (inmueble instanceof Apartamento) {
            Apartamento apartamento = (Apartamento) inmueble;
            try (PreparedStatement ps = conexion.prepareStatement(
                    "INSERT INTO apartamento (chip, numero_piso) VALUES (?, ?)")) {
                ps.setString(1, apartamento.getChip());
                ps.setInt(2, apartamento.getNumeroPiso());
                ps.executeUpdate();
            }
            return;
        }

        Casa casa = (Casa) inmueble;
        try (PreparedStatement ps = conexion.prepareStatement(
                "INSERT INTO casa (chip, cantidad_pisos) VALUES (?, ?)")) {
            ps.setString(1, casa.getChip());
            ps.setInt(2, casa.getCantidadPisos());
            ps.executeUpdate();
        }
        if (casa.tieneSotano()) {
            try (PreparedStatement ps = conexion.prepareStatement(
                    "INSERT INTO sotano (casa_chip, area_m2, tipo_uso) VALUES (?, ?, ?)")) {
                ps.setString(1, casa.getChip());
                ps.setDouble(2, casa.getSotano().getAreaM2());
                ps.setString(3, casa.getSotano().getTipoUso());
                ps.executeUpdate();
            }
        }
    }

    /**
     * Borra el detalle de casa o apartamento. El sotano cae por cascada desde casa.
     *
     * @param conexion conexion dentro de la transaccion
     * @param chip     CHIP del inmueble
     * @throws SQLException si falla algun delete
     */
    private void borrarDetalle(Connection conexion, String chip) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement("DELETE FROM apartamento WHERE chip=?")) {
            ps.setString(1, chip);
            ps.executeUpdate();
        }
        try (PreparedStatement ps = conexion.prepareStatement("DELETE FROM casa WHERE chip=?")) {
            ps.setString(1, chip);
            ps.executeUpdate();
        }
    }

    /**
     * Ejecuta una consulta y convierte cada fila en un inmueble.
     *
     * @param sql        consulta SQL
     * @param parametros parametros de la consulta, en orden
     * @return lista de inmuebles
     */
    private List<Inmueble> consultar(String sql, String... parametros) {
        List<Inmueble> inmuebles = new ArrayList<>();
        try (Connection conexion = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            for (int i = 0; i < parametros.length; i++) {
                ps.setString(i + 1, parametros[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    inmuebles.add(mapear(rs));
                }
            }
            return inmuebles;
        } catch (SQLException e) {
            throw new ConexionBDException("No fue posible consultar los inmuebles.", e);
        }
    }

    /**
     * Convierte una fila de PostgreSQL en una {@link Casa} o un {@link Apartamento}.
     *
     * @param rs resultado SQL
     * @return inmueble construido
     * @throws SQLException si ocurre un error de lectura
     */
    private Inmueble mapear(ResultSet rs) throws SQLException {
        Propietario propietario = new Propietario(rs.getString("identificacion"),
                rs.getString("nombre"), rs.getString("correo_electronico"));

        if ("APARTAMENTO".equals(rs.getString("tipo"))) {
            return new Apartamento(rs.getString("chip"), rs.getDate("fecha_compra").toLocalDate(),
                    rs.getString("direccion"), rs.getString("estado"), propietario,
                    rs.getInt("numero_piso"));
        }

        Sotano sotano = null;
        String tipoUso = rs.getString("tipo_uso");
        if (tipoUso != null) {
            sotano = new Sotano(rs.getDouble("area_m2"), tipoUso);
        }
        return new Casa(rs.getString("chip"), rs.getDate("fecha_compra").toLocalDate(),
                rs.getString("direccion"), rs.getString("estado"), propietario,
                rs.getInt("cantidad_pisos"), sotano);
    }

    /**
     * Traduce errores de PostgreSQL a excepciones de dominio.
     *
     * @param e       excepcion SQL
     * @param chip    CHIP involucrado
     * @param mensaje mensaje para errores no reconocidos
     * @return excepcion correspondiente
     */
    private RuntimeException traducirExcepcion(SQLException e, String chip, String mensaje) {
        String estado = e.getSQLState();
        if (SQLSTATE_UNIQUE.equals(estado)) {
            return new InmuebleDuplicadoException(chip);
        }
        if (SQLSTATE_FOREIGN_KEY.equals(estado)) {
            return new InmuebleValidacionException("El propietario seleccionado no existe en la base de datos.");
        }
        if (SQLSTATE_CHECK.equals(estado) || SQLSTATE_NOT_NULL.equals(estado)) {
            return new InmuebleValidacionException("Los datos no cumplen las restricciones de la base de datos.");
        }
        return new ConexionBDException(mensaje, e);
    }

    /**
     * Revierte una transaccion.
     *
     * @param conexion conexion activa
     */
    private void rollback(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.rollback();
            } catch (SQLException ignorada) {
                // Se reporta la excepcion original.
            }
        }
    }

    /**
     * Restaura el auto-commit y cierra una conexion.
     *
     * @param conexion conexion activa
     */
    private void cerrar(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.setAutoCommit(true);
                conexion.close();
            } catch (SQLException ignorada) {
                // No hay nada mas que hacer.
            }
        }
    }
}
