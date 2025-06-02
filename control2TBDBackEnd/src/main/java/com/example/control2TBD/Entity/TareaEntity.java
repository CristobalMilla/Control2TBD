package com.example.control2TBD.Entity;

import java.time.LocalDate;

public class TareaEntity {
    private int id_tarea;
    private String titulo;
    private String descripcion;
    private LocalDate fecha_vencimiento;
    private int id_usuario;
    private int id_sector;
    private String estado;

    public TareaEntity() {
    }

    public TareaEntity(int id_tarea, String titulo, String descripcion, LocalDate fecha_vencimiento, int id_usuario, int id_sector, String estado) {
        this.id_tarea = id_tarea;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha_vencimiento = fecha_vencimiento;
        this.id_usuario = id_usuario;
        this.id_sector = id_sector;
        this.estado = estado;
    }

    public int getId_tarea() {
        return id_tarea;
    }

    public void setId_tarea(int id_tarea) {
        this.id_tarea = id_tarea;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public void setFecha_vencimiento(LocalDate fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_sector() {
        return id_sector;
    }

    public void setId_sector(int id_sector) {
        this.id_sector = id_sector;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
