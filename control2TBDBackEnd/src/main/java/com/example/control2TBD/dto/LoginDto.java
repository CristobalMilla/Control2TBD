package com.example.control2TBD.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 LoginDto es una clase de transferencia de datos que representa los datos de inicio de sesión de un usuario.*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    private String nickname;
    private String contrasenia;

}