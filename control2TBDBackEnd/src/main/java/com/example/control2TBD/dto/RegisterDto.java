package com.example.control2TBD.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 RegisterDto es una clase de transferencia de datos que representa los datos de registro de un usuario.*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {

    private String nickname;
    private String contrasenia;
    private double lat;
    private double lng;
    private String rol;

}