package com.example.control2TBD.Service;

import com.example.control2TBD.Entity.SectorEntity;
import com.example.control2TBD.Entity.TareaEntity;
import com.example.control2TBD.Repository.SectorRepository;
import com.example.control2TBD.Repository.TareaRepository;
import com.example.control2TBD.dto.ComunaTareasDto;
import com.example.control2TBD.dto.TareasHechasPorUnUsuarioEnSectorDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;

    private final SectorRepository sectorRepository;

    @Autowired
    public TareaService(TareaRepository tareaRepository, SectorRepository sectorRepository) {
        this.tareaRepository = tareaRepository;
        this.sectorRepository = sectorRepository;
    }

    public void createTarea(TareaEntity tarea) {
        tareaRepository.createTarea(tarea);
    }

    public void updateTarea(TareaEntity tarea) {
        tareaRepository.updateTarea(tarea);
    }

    public void deleteTarea(Long id_tarea) {
        tareaRepository.deleteTarea(id_tarea);
    }

    public void markTareaCompleted(Long id_tarea) {
        tareaRepository.markTareaCompleted(id_tarea);
    }

    public List<TareaEntity> getAllTareas() {
        return tareaRepository.getAllTareas();
    }

    public List<TareaEntity> getTareasByEstado(String estado) {
        return tareaRepository.getTareasByEstado(estado);
    }

    public TareaEntity getTareaById(Long id_tarea) {
        return tareaRepository.getTareaById(id_tarea);
    }

    //4) obtiene el promedio de distancia de las tareas completadas respecto a la
    //ubicación del usuario
    public Double obtenerPromedioDistanciaTareasCompletadasPorUsuario(Long idUsuario) {
        return tareaRepository.obtenerPromedioDistanciaTareasCompletadasPorUsuario(idUsuario);
    }

    //5) ordenar los sectores geográficos por comuna q se concentran la mayoría de las tareas pendientes
    public List<ComunaTareasDto> obtenerCantidadTareasPendientesPorComuna() {
        return tareaRepository.obtenerCantidadTareasPendientesPorComuna();
    }

    //6) la tarea pendiente + cercana a la ubi del id del usuario
    public TareaEntity obtenerTareaPendienteMasCercana(Long id_usuario) {
        return tareaRepository.obtenerTareaPendienteMasCercana(id_usuario);
    }

    // 7)
    public List<Map<String, Object>> getTareasPerUsuarioPerSector(){
        return tareaRepository.getTareasPerUsuarioPerSector();
    }
    // 8)
    public Long getSectorWithMostCompletedTareasNearby(Long id_usuario){
        return tareaRepository.getSectorWithMostCompletedTareasNearby(id_usuario);
    }
    // 9)
    public Double getAverageDistanceToCompletedTareas(Long id_usuario){
        return tareaRepository.getAverageDistanceToCompletedTareas(id_usuario);
    }

    public List<TareasHechasPorUnUsuarioEnSectorDTO> getTareasHechasPorUnUsuarioEnCadaSector(long id_usuario) {
        return tareaRepository.getTareasHechasPorUnUsuarioEnCadaSector(id_usuario);
    }

    public TareaEntity getMasCercanaAUnUsuario(Long id_usuario){
        return tareaRepository.getTareaMasCercanaAUnUsuario(id_usuario);
    }

    public SectorEntity getSectorCercanoConMasTareasCompletadas(Long id_usuario){
        Long id_sector = tareaRepository.getSectorCercanoConMasTareasCompletadas(id_usuario);
        return sectorRepository.findById(id_sector);
    }

    public List<TareaEntity> getTareasProximasAVencer(Long id_usuario) {
        return tareaRepository.getTareasProximasAVencer(id_usuario);
    }
}