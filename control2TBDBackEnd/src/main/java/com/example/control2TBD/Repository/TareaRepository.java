package com.example.control2TBD.Repository;

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
}
