package com.example.control2TBD.Entity;

import java.time.LocalDate;
import java.util.Date;

public class TareaEntity {
    private Long id_tarea;
    private String titulo;
    private String descripcion;
    private Date fecha_vencimiento;
    private Long id_usuario;
    private Long id_sector;
    private String estado;

    public TareaEntity() {
    }

    public TareaEntity(Long id_tarea, String titulo, String descripcion, Date fecha_vencimiento, Long id_usuario, Long id_sector, String estado) {
        this.id_tarea = id_tarea;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha_vencimiento = fecha_vencimiento;
        this.id_usuario = id_usuario;
        this.id_sector = id_sector;
        this.estado = estado;
    }

    public Long getId_tarea() {
        return id_tarea;
    }

    public void setId_tarea(Long id_tarea) {
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

    public Date getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public void setFecha_vencimiento(Date fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }

    public Long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Long getId_sector() {
        return id_sector;
    }

    public void setId_sector(Long id_sector) {
        this.id_sector = id_sector;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
