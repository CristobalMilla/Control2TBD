package com.example.control2TBD.Controller;

import com.example.control2TBD.Entity.SectorEntity;
import com.example.control2TBD.Service.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sectores")
public class SectorController {

    @Autowired
    private SectorService sectorService;

    @GetMapping
    public List<SectorEntity> findAll() {
        return sectorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SectorEntity> findById(@PathVariable Integer id) {
        SectorEntity sector = sectorService.findById(id);
        if (sector == null) {
            return ResponseEntity.notFound().build();
        }
        else{
            return ResponseEntity.ok(sector);
        }
    }


    @PostMapping
    public SectorEntity save(@RequestBody SectorEntity sector) {
        return sectorService.save(sector);
    }


    @PutMapping("/{id}")
    public ResponseEntity<SectorEntity> update(@PathVariable Integer id, @RequestBody SectorEntity sector) {
        if (!sectorService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        sector.setId_sector(id);
        SectorEntity updatedSector = sectorService.update(sector);
        return ResponseEntity.ok(updatedSector);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        if (!sectorService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        sectorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/exists/{id}")
    public boolean existsById(@PathVariable Integer id) {
        return sectorService.existsById(id);
    }
}