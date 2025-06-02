package com.example.control2TBD.Service;

import com.example.control2TBD.Entity.SectorEntity;
import com.example.control2TBD.Entity.TareaEntity;
import com.example.control2TBD.Repository.TareaRepository;
import com.example.control2TBD.dto.TareasHechasPorUnUsuarioEnSectorDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;

    @Autowired
    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    public void createTarea(TareaEntity tarea) {
        tareaRepository.createTarea(tarea);
    }

    public void updateTarea(TareaEntity tarea) {
        tareaRepository.updateTarea(tarea);
    }

    public void deleteTarea(int id_tarea) {
        tareaRepository.deleteTarea(id_tarea);
    }

    public void markTareaCompleted(int id_tarea) {
        tareaRepository.markTareaCompleted(id_tarea);
    }

    public List<TareaEntity> getAllTareas() {
        return tareaRepository.getAllTareas();
    }

    public List<TareaEntity> getTareasByEstado(String estado) {
        return tareaRepository.getTareasByEstado(estado);
    }

    public TareaEntity getTareaById(int id_tarea) {
        return tareaRepository.getTareaById(id_tarea);
    }

    //4) obtiene el promedio de distancia de las tareas completadas respecto a la
    //ubicación del usuario
    public Double obtenerPromedioDistanciaTareasCompletadasPorUsuario(Long idUsuario) {
        return tareaRepository.obtenerPromedioDistanciaTareasCompletadasPorUsuario(idUsuario);
    }

    // 7)
    public List<Map<String, Object>> getTareasPerUsuarioPerSector(){
        return tareaRepository.getTareasPerUsuarioPerSector();
    }
    // 8)
    public int getSectorWithMostCompletedTareasNearby(int id_usuario){
        return tareaRepository.getSectorWithMostCompletedTareasNearby(id_usuario);
    }
    // 9)
    public Double getAverageDistanceToCompletedTareas(int id_usuario){
        return tareaRepository.getAverageDistanceToCompletedTareas(id_usuario);
    }

    public List<TareasHechasPorUnUsuarioEnSectorDTO> getTaresHechasPorUnUsuarioEnCadaSector(long id_usuario) {
        return tareaRepository.getTareasHechasPorUnUsuarioEnCadaSector(id_usuario);
    }

    public TareaEntity getMasCercanaAUnUsuario(long id_usuario){
        return tareaRepository.getTareaMasCercanaAUnUsuario(id_usuario);
    }

    public SectorEntity getSectorCercanoConMasTareasCompletadas(long id_usuario){
        return tareaRepository.getSectorCercanoConMasTareasCompletadas(id_usuario);
    }

    public List<TareaEntity> getTareasProximasAVencer(long id_usuario) {
        return tareaRepository.getTareasProximasAVencer(id_usuario);
    }
}