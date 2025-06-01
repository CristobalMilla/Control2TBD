package com.example.control2TBD.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;

    private String nickname;

    private String contrasenia;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point ubicacion;

    private String rol;


    // Constructor sin id
    public UsuarioEntity(String nickname, String contrasenia, Point ubicacion, String rol) {
        this.nickname = nickname;
        this.contrasenia = contrasenia;
        this.ubicacion = ubicacion;
        this.rol = rol;
    }
}