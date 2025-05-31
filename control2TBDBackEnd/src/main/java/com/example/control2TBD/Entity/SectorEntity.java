package com.example.control2TBD.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.locationtech.jts.geom.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectorEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_sector;
    
    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point ubicacion;
}