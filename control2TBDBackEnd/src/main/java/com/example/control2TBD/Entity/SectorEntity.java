package com.example.control2TBD.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Polygon;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectorEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_sector;
    
    @Column(columnDefinition = "geometry(Polygon, 4326)")
    private Polygon ubicacion;
}