package com.example.control2TBD.dto;

public class TareasHechasPorUnUsuarioEnSectorDTO {
    private long id_sector;
    private int tareas_hechas;

    public TareasHechasPorUnUsuarioEnSectorDTO(long id_sector, int tareas_hechas) {
        this.id_sector = id_sector;
        this.tareas_hechas = tareas_hechas;
    }

    public long getId_sector() {
        return id_sector;
    }

    public int gettareas_hechas() {
        return tareas_hechas;
    }

    public void setId_sector(long id_sector) {
        this.id_sector = id_sector;
    }

    public void settareas_hechas(int tareas_hechas) {
        this.tareas_hechas = tareas_hechas;
    }
}
