package com.example.control2TBD.Repository;

import com.example.control2TBD.Entity.SectorEntity;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
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
                Point point = (Point) wktReader.read(wkt);
                point.setSRID(4326);
                sector.setUbicacion(point);
            } catch (ParseException e) {
                throw new RuntimeException("Error al parsear la geometría: " + e.getMessage(), e);
            }
        }

        return sector;
    }
    private String pointToWKT(Point point) {
        if (point == null) {
            return null;
        }
        return String.format("POINT(%f %f)", point.getX(), point.getY());
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
            String wkt = pointToWKT(sector.getUbicacion());
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
            String wkt = pointToWKT(sector.getUbicacion());
            Integer generatedId = conn.createQuery(sql)
                    .addParameter("ubicacion", wkt)
                    .executeAndFetchFirst(Integer.class);

            sector.setId_sector(generatedId);
            return sector;
        }
    }
    public Point createPoint(double longitude, double latitude) {
        Coordinate coordinate = new Coordinate(longitude, latitude);
        Point point = geometryFactory.createPoint(coordinate);
        point.setSRID(4326);
        return point;
    }

}