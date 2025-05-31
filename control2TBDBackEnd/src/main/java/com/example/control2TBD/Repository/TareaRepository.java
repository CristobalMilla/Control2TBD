package com.example.control2TBD.Repository;

import com.example.control2TBD.DTO.TareasHechasPorUnUsuarioEnSectorDTO;
import com.example.control2TBD.Entity.TareaEntity;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class TareaRepository {

    private final Sql2o sql2o;

    @Autowired
    public TareaRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }
    
    // Crear Tarea
    public void createTarea(TareaEntity tarea) {
        String sql = "INSERT INTO tarea (titulo, descripcion, fecha_vencimiento, id_usuario, id_sector, estado) " +
                     "VALUES (:titulo, :descripcion, :fecha_vencimiento, :id_usuario, :id_sector, :estado)";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                .addParameter("titulo", tarea.getTitulo())
                .addParameter("descripcion", tarea.getDescripcion())
                .addParameter("fecha_vencimiento", tarea.getFecha_vencimiento())
                .addParameter("id_usuario", tarea.getId_usuario())
                .addParameter("id_sector", tarea.getId_sector())
                .addParameter("estado", tarea.getEstado())
                .executeUpdate();
        }
    }
    
    // Editar Tarea: Actualiza título, descripción, fecha de vencimiento y sector
    public void updateTarea(TareaEntity tarea) {
        String sql = "UPDATE tarea SET titulo = :titulo, descripcion = :descripcion, " +
                     "fecha_vencimiento = :fecha_vencimiento, id_sector = :id_sector " +
                     "WHERE id_tarea = :id_tarea";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                .addParameter("titulo", tarea.getTitulo())
                .addParameter("descripcion", tarea.getDescripcion())
                .addParameter("fecha_vencimiento", tarea.getFecha_vencimiento())
                .addParameter("id_sector", tarea.getId_sector())
                .addParameter("id_tarea", tarea.getId_tarea())
                .executeUpdate();
        }
    }
    
    // Eliminar Tarea
    public void deleteTarea(int id_tarea) {
        String sql = "DELETE FROM tarea WHERE id_tarea = :id_tarea";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                .addParameter("id_tarea", id_tarea)
                .executeUpdate();
        }
    }
    
    // Marcar Tarea como Completada
    public void markTareaCompleted(int id_tarea) {
        String sql = "UPDATE tarea SET estado = 'Completada' WHERE id_tarea = :id_tarea";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                .addParameter("id_tarea", id_tarea)
                .executeUpdate();
        }
    }
    
    // Obtener lista de todas las tareas
    public List<TareaEntity> getAllTareas() {
        String sql = "SELECT * FROM tarea";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                       .executeAndFetch(TareaEntity.class);
        }
    }
    
    // Obtener tareas filtradas por estado (pendientes o completadas)
    public List<TareaEntity> getTareasByEstado(String estado) {
        String sql = "SELECT * FROM tarea WHERE estado = :estado"; //pendientes o completadas
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                       .addParameter("estado", estado)
                       .executeAndFetch(TareaEntity.class);
        }
    }

    public TareaEntity getTareaById(int id_tarea) {
        String sql = "SELECT * FROM tarea WHERE id_tarea = :id_tarea";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                       .addParameter("id_tarea", id_tarea)
                       .executeAndFetchFirst(TareaEntity.class);
        }
    }

    // ¿Cuántas tareas ha hecho el usuario por sector?
    // Voy a suponer que hechas == completadas
    public List<TareasHechasPorUnUsuarioEnSectorDTO> getTaresHechasPorUnUsuarioEnCadaSector(long id_usuario) {
        try (Connection conn = sql2o.open()) {
            List<TareasHechasPorUnUsuarioEnSectorDTO> tareasPorSector;
            String query = "SELECT s.id_sector, COALESCE(COUNT(th.id_tarea), 0) AS tareas_hechas " +
                           "FROM (SELECT t.id_sector, t.id_tarea " +
                                 "FROM tarea t " +
                                 "WHERE t.id_usuario = :id_usuario AND t.estado = 'Completada' "+
                                 "GROUP BY t.id_sector, t.id_tarea) AS th " +
                           "RIGHT JOIN sector s ON s.id_sector = th.id_sector " +
                           "GROUP BY s.id_sector ";
            tareasPorSector = conn.createQuery(query)
                    .addParameter("id_usuario", id_usuario)
                    .executeAndFetch(TareasHechasPorUnUsuarioEnSectorDTO.class);
            return tareasPorSector;
        }
        catch (Exception e) {
            return null;
        }
    }

    // ¿Cuál es la tarea más cercana al usuario (que esté pendiente)?
    public TareaEntity getTareaMasCercanaAUnUsuario(long id_usuario) {
        try (Connection conn = sql2o.open()) {
            TareaEntity tareaMasCercana;
            String query = "SELECT t.* " +
                    "FROM tarea t " +
                    "INNER JOIN sector s ON t.id_sector = s.id_sector " +
                    "INNER JOIN usuario u ON t.id_usuario = u.id_usuario " +
                    "WHERE t.estado = 'Pendiente' AND u.id_usuario = :id_usuario " +
                    "ORDER BY ST_Distance(u.ubicacion, s.ubicacion) ASC " +
                    "LIMIT 1";
            tareaMasCercana = conn.createQuery(query)
                    .addParameter("id_usuario", id_usuario).executeAndFetchFirst(TareaEntity.class);
            return tareaMasCercana;
        }
         catch (Exception e) {
            return null;
         }
    }

    // ¿Cuál es el sector con más tareas completadas en un radio de 2 kilómetro del usuario?
    // Se toma el primero a pesar de que pueden haber varios con las mismas tareas completadas
    // Falta la entidad sector
    public SectorEntity getSectorCercanoConMasTareasCompletadas(long id_usuario){
        try (Connection conn = sql2o.open()) {
            SectorEntity sector;
            String query = "SELECT * " +
                           "FROM sector " +
                           "WHERE id_sector " +
                           "IN (SELECT id_sector " +
                               "FROM (SELECT s.id_sector, COUNT(*) tareas_completadas " +
                                     "FROM tarea t " +
                                     "INNER JOIN sector s ON s.id_sector = t.id_sector " +
                                     "WHERE ST_DWithin(s.ubicacion, (SELECT ubicacion FROM usuario WHERE id_usuario = :id_usuario), 2000) " +
                                     "AND t.estado = 'Completada' " +
                                     "GROUP BY s.id_sector " +
                                     "ORDER BY tareas_completadas DESC " +
                                     "LIMIT 1))";
            sector = conn.createQuery(query)
                    .addParameter("id_usuario", id_usuario)
                    .executeAndFetch(SectorEntity.class);
            return sector;
        }
        catch (Exception e) {
            return null;
        }
    }

    // query para todos los sectores con el mismo numero de tareas completadas, lo mismo de arriba pero tomando todos los sectores
    // query = "SELECT * " +
    //         "FROM sector " +
    //         "WHERE id_sector " +
    //         "IN (SELECT tc.id_sector " +
    //	           "FROM (SELECT s.id_sector, COUNT(*) tareas_completadas " +
    //			         "FROM tarea t " +
    //			         "INNER JOIN sector s ON s.id_sector = t.id_sector " +
    //			         "WHERE ST_DWithin(s.ubicacion, (SELECT ubicacion FROM usuario WHERE id_usuario = 1), 2000) " +
    //			         "AND t.estado = 'Completada' " +
    //			         "GROUP BY s.id_sector " +
    //			         "ORDER BY tareas_completadas DESC) AS tc " +
    //         "WHERE tc.tareas_completadas  " +
    //         "= (SELECT MAX(tareas_completadas) " +
    //	       "FROM (SELECT s.id_sector, COUNT(*) tareas_completadas " +
    //	       "FROM tarea t " +
    //	       "INNER JOIN sector s ON s.id_sector = t.id_sector " +
    //	       "WHERE ST_DWithin(s.ubicacion, (SELECT ubicacion FROM usuario WHERE id_usuario = 1), 2000) " +
    //         "AND t.estado = 'Completada' " +
    //	       "GROUP BY s.id_sector " +
    //	       "ORDER BY tareas_completadas DESC)));
}
