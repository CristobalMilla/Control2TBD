package com.example.control2TBD.Controller;

import com.example.control2TBD.DTO.TareasHechasPorUnUsuarioEnSectorDTO;
import com.example.control2TBD.Entity.TareaEntity;
import com.example.control2TBD.Service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService tareaService;

    @Autowired
    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    // Crear Tarea
    @PostMapping
    public void createTarea(@RequestBody TareaEntity tarea) {
        tareaService.createTarea(tarea);
    }

    // Editar Tarea
    @PutMapping("/{id}")
    public void updateTarea(@PathVariable("id") int id, @RequestBody TareaEntity tarea) {
        tarea.setId_tarea(id);
        tareaService.updateTarea(tarea);
    }

    // Eliminar Tarea
    @DeleteMapping("/{id}")
    public void deleteTarea(@PathVariable("id") int id) {
        tareaService.deleteTarea(id);
    }

    // Marcar Tarea como Completada
    @PutMapping("/{id}/complete")
    public void markTareaCompleted(@PathVariable("id") int id) {
        tareaService.markTareaCompleted(id);
    }

    // Obtener lista de todas las tareas
    @GetMapping
    public List<TareaEntity> getAllTareas(@RequestParam(value = "estado", required = false) String estado) {
        if(estado != null) {
            return tareaService.getTareasByEstado(estado);
        }
        return tareaService.getAllTareas();
    }

    // Obtener tarea por Id
    @GetMapping("/{id}")
    public TareaEntity getTareaById(@PathVariable("id") int id) {
        return tareaService.getTareaById(id);
    }

    // ¿Cuántas tareas ha hecho el usuario por sector?
    @GetMapping("/porSector/{idUsuario}")
    public List<TareasHechasPorUnUsuarioEnSectorDTO> getTaresHechasPorUnUsuarioEnCadaSector(@PathVariable("idUsuario") long id_usuario) {
        return tareaService.getTaresHechasPorUnUsuarioEnCadaSector(id_usuario);
    }

    // ¿Cuál es la tarea más cercana al usuario (que esté pendiente)?
    @GetMapping("/masCercana/{idUsuario}")
    public TareaEntity getMasCercanaAUnUsuario(@PathVariable("idUsuario") long id_usuario) {
        return tareaService.getMasCercanaAUnUsuario(id_usuario);
    }

    // ¿Cuál es el sector con más tareas completadas en un radio de 2 kilómetros del usuario?
    @GetMapping("/sectorMasCompletadas/{idUsuario}")
    public SectorEntity getSectorCercanoConMasTareasCompletadas(@PathVariable("idUsuario") long id_usuario){
        return tareaService.getSectorCercanoConMasTareasCompletadas(id_usuario);
    }
}