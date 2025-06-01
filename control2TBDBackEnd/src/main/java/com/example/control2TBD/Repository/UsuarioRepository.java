package com.example.control2TBD.Repository;

import com.example.control2TBD.Entity.UsuarioEntity;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class UsuarioRepository {

    private final Sql2o sql2o;
    private final GeometryFactory geometryFactory;

    @Autowired
    public UsuarioRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
        this.geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    private UsuarioEntity mapRowToUsuarioEntity(Map<String, Object> row) {
        if (row == null) {
            return null;
        }
        UsuarioEntity user = new UsuarioEntity();
        user.setId_usuario((Long) row.get("id_usuario"));
        user.setNickname((String) row.get("nickname"));
        user.setContrasenia((String) row.get("contrasenia"));
        user.setRol((String) row.get("rol"));
        String ubicacionWkt = (String) row.get("ubicacion_wkt");
        if (ubicacionWkt != null && !ubicacionWkt.isEmpty()) {
            try {
                WKTReader reader = new WKTReader(this.geometryFactory);
                user.setUbicacion((Point) reader.read(ubicacionWkt));
            } catch (ParseException e) {
                System.err.println("Error al parsear WKT de ubicación: " + e.getMessage());
                user.setUbicacion(null);
            }
        } else {
            user.setUbicacion(null);
        }
        return user;
    }

    public UsuarioEntity findByNickname(String nickname) {
        String sql = "SELECT id_usuario, nickname, contrasenia, ST_AsText(ubicacion) as ubicacion_wkt, rol FROM usuario_entity WHERE nickname = :nickname";
        try (Connection conn = sql2o.open()) {
            List<Map<String, Object>> results = conn.createQuery(sql)
                    .addParameter("nickname", nickname)
                    .executeAndFetchTable().asList();
            if (results.isEmpty()) {
                return null;
            }
            return mapRowToUsuarioEntity(results.get(0));
        }
    }

    public UsuarioEntity save(UsuarioEntity usuario) {
        String ubicacionWkt = (usuario.getUbicacion() != null) ? usuario.getUbicacion().toText() : null;

        if (usuario.getId_usuario() == null) { 
            String insertSql = "INSERT INTO usuario_entity (nickname, contrasenia, ubicacion, rol) " +
                               "VALUES (:nickname, :contrasenia, ST_GeomFromText(:ubicacionWkt, 4326), :rol)";
            try (Connection conn = sql2o.open()) {
                Long newId = conn.createQuery(insertSql, true)
                        .addParameter("nickname", usuario.getNickname())
                        .addParameter("contrasenia", usuario.getContrasenia())
                        .addParameter("ubicacionWkt", ubicacionWkt)
                        .addParameter("rol", usuario.getRol())
                        .executeUpdate()
                        .getKey(Long.class);
                usuario.setId_usuario(newId);
                return usuario;
            }
        } else {
            String updateSql = "UPDATE usuario_entity SET nickname = :nickname, contrasenia = :contrasenia, " +
                               "ubicacion = ST_GeomFromText(:ubicacionWkt, 4326), rol = :rol WHERE id_usuario = :id_usuario";
            try (Connection conn = sql2o.open()) {
                conn.createQuery(updateSql)
                        .addParameter("nickname", usuario.getNickname())
                        .addParameter("contrasenia", usuario.getContrasenia())
                        .addParameter("ubicacionWkt", ubicacionWkt)
                        .addParameter("rol", usuario.getRol())
                        .addParameter("id_usuario", usuario.getId_usuario())
                        .executeUpdate();
                return usuario;
            }
        }
    }

    public List<UsuarioEntity> findAll() {
        String sql = "SELECT id_usuario, nickname, contrasenia, ST_AsText(ubicacion) as ubicacion_wkt, rol FROM usuario_entity";
        try (Connection conn = sql2o.open()) {
            List<Map<String, Object>> rows = conn.createQuery(sql)
                .executeAndFetchTable().asList();
            return rows.stream()
                       .map(this::mapRowToUsuarioEntity)
                       .collect(Collectors.toList());
        }
    }

    public Optional<UsuarioEntity> findById(Long id) {
        String sql = "SELECT id_usuario, nickname, contrasenia, ST_AsText(ubicacion) as ubicacion_wkt, rol FROM usuario_entity WHERE id_usuario = :id";
        try (Connection conn = sql2o.open()) {
            List<Map<String, Object>> results = conn.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchTable().asList();
            if (results.isEmpty()) {
                return Optional.empty();
            }
            return Optional.ofNullable(mapRowToUsuarioEntity(results.get(0)));
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM usuario_entity WHERE id_usuario = :id";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM usuario_entity WHERE id_usuario = :id";
        try (Connection conn = sql2o.open()) {
            Long count = conn.createQuery(sql)
                    .addParameter("id", id)
                    .executeScalar(Long.class);
            return count != null && count > 0;
        }
    }
    
    public long count() {
        String sql = "SELECT COUNT(*) FROM usuario_entity";
        try (Connection conn = sql2o.open()) {
            Long count = conn.createQuery(sql).executeScalar(Long.class);
            return count != null ? count : 0L;
        }
    }

    public void deleteAll() {
        String sql = "DELETE FROM usuario_entity";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).executeUpdate();
        }
    }
}