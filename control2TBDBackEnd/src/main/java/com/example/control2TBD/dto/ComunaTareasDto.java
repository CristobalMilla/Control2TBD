package com.example.control2TBD.dto;

public class ComunaTareasDto {
    private String comuna;
    private Integer cantidad;

    public ComunaTareasDto() {
    }

    public ComunaTareasDto(String comuna, Integer cantidad) {
        this.comuna = comuna;
        this.cantidad = cantidad;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
