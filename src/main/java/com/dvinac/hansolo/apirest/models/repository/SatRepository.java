package com.dvinac.hansolo.apirest.models.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.dvinac.hansolo.apirest.models.entity.Satelite;

public interface SatRepository extends JpaRepository<Satelite, Long> {

	Satelite findByNombre(String satellite_name);

}
