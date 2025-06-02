
package com.example.control2TBD.Service;

import com.example.control2TBD.Entity.SectorEntity;
import com.example.control2TBD.Repository.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class SectorService {

    @Autowired
    private SectorRepository sectorRepository;


    public List<SectorEntity> findAll() {
        return sectorRepository.findAll();
    }


    public SectorEntity findById(Integer id) {
        return sectorRepository.findById(id);
    }


    public SectorEntity save(SectorEntity sector) {
        return sectorRepository.save(sector);
    }


    public SectorEntity update(SectorEntity sector) {
        return sectorRepository.save(sector);
    }


    public void deleteById(Integer id) {
        sectorRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return sectorRepository.existsById(id);
    }

}