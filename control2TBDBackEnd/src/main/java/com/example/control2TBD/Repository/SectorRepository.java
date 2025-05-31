package com.example.control2TBD.Repository;

import com.example.control2TBD.Entity.SectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SectorRepository extends JpaRepository<SectorEntity, Integer> {

}