package com.example.control2TBD.Controller;

import com.example.control2TBD.Entity.SectorEntity;
import com.example.control2TBD.Entity.TareaEntity;
import com.example.control2TBD.Service.TareaService;
import com.example.control2TBD.dto.TareasHechasPorUnUsuarioEnSectorDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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


    // 7) Funcion que devuelve todas las tareas que ha realizado cada usuario por cada sector
    @GetMapping("/allTareasPerUserPerSector")
    public List<Map<String, Object>> getTareasPerUsuarioPerSector(){
        return tareaService.getTareasPerUsuarioPerSector();
    }

    // 8) Funcion que obtiene el sector con mas tareas compleadas en un rango de 5 KM del usuario
    @GetMapping("/SectorMostCompletedByUser")
    public int getSectorWithMostCompletedTareasNearby(@RequestParam int id_usuario){
        return tareaService.getSectorWithMostCompletedTareasNearby(id_usuario);
    }

    // 9) Funcion que devuelve el promedio de las distancias entre todas las tareas completadas y la ubicacion del usuario
    @GetMapping("/AverageCompletedUser")
    public Double getAverageDistanceToCompletedTareas(@RequestParam int id_usuario){
        return tareaService.getAverageDistanceToCompletedTareas(id_usuario);
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

    // Obtener tareas próximas a vencer para un usuario
    @GetMapping("/proximasAVencer/{idUsuario}")
    public List<TareaEntity> getTareasProximasAVencer(@PathVariable("idUsuario") long id_usuario) {
        return tareaService.getTareasProximasAVencer(id_usuario);
    }
}