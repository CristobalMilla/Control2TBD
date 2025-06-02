package com.example.control2TBD.Repository;

import com.example.control2TBD.Entity.SectorEntity;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import java.util.List;
import java.util.Map;


@Repository
public class SectorRepository {
    @Autowired
    private Sql2o sql2o;
    private final GeometryFactory geometryFactory;
    private final WKTReader wktReader;

    @Autowired
    public SectorRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
        this.geometryFactory = new GeometryFactory();
        this.wktReader = new WKTReader(geometryFactory);
    }

    private SectorEntity mapToSectorEntity(Map<String, Object> row) {
        SectorEntity sector = new SectorEntity();
        sector.setId_sector((Integer) row.get("id_sector"));
        String wkt = (String) row.get("ubicacion_wkt");
        if (wkt != null) {
            try {
                Polygon polygon = (Polygon) wktReader.read(wkt);
                polygon.setSRID(4326);
                sector.setUbicacion(polygon);
            } catch (ParseException e) {
                throw new RuntimeException("Error al parsear la geometría: " + e.getMessage(), e);
            }
        }

        return sector;
    }
    private String polygonToWKT(Polygon polygon) {
        if (polygon == null) {
            return null;
        }
        return polygon.toText();
    }
    public List<SectorEntity> findAll() {
        String sql = "SELECT id_sector, ST_AsText(ubicacion) as ubicacion_wkt FROM sector_entity";
        try (Connection conn = sql2o.open()) {
            List<Map<String, Object>> results = conn.createQuery(sql)
                    .executeAndFetchTable()
                    .asList();

            return results.stream().map(this::mapToSectorEntity).toList();
        }

    }
    public SectorEntity findById(Integer id) {
        String sql = "SELECT id_sector, ST_AsText(ubicacion) as ubicacion_wkt FROM sector_entity WHERE id_sector = :id";
        try (Connection conn = sql2o.open()) {
            List<Map<String, Object>> results = conn.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchTable()
                    .asList();
            if (results.isEmpty()) {
                return null;
            }
            return mapToSectorEntity(results.get(0));
        }
    }

    public void deleteById(Integer id) {
        String sql = "DELETE FROM sector_entity WHERE id_sector = :id";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }
    public SectorEntity update(SectorEntity sector) {
        String sql = "UPDATE sector_entity SET ubicacion = ST_GeomFromText(:ubicacion, 4326) WHERE id_sector = :id";
        try (Connection conn = sql2o.open()) {
            String wkt = polygonToWKT(sector.getUbicacion());
            conn.createQuery(sql)
                    .addParameter("ubicacion", wkt)
                    .addParameter("id", sector.getId_sector())
                    .executeUpdate();

            return sector;
        }
    }
    public SectorEntity save(SectorEntity sector) {
        String sql = "INSERT INTO sector_entity (ubicacion) VALUES (ST_GeomFromText(:ubicacion, 4326)) RETURNING id_sector";
        try (Connection conn = sql2o.open()) {
            String wkt = polygonToWKT(sector.getUbicacion());
            Integer generatedId = conn.createQuery(sql)
                    .addParameter("ubicacion", wkt)
                    .executeAndFetchFirst(Integer.class);

            sector.setId_sector(generatedId);
            return sector;
        }
    }
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM sector_entity WHERE id_sector = :id";
        try (Connection conn = sql2o.open()) {
            Integer count = conn.createQuery(sql)
                    .addParameter("id", id)
                    .executeScalar(Integer.class);
            return count != null && count > 0;
        }
    }

}