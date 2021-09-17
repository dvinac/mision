package com.dvinac.hansolo.apirest.models.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.dvinac.hansolo.apirest.models.entity.MensajeHansolo;

public interface MensajeHansoloRepository extends CrudRepository<MensajeHansolo, Long>{
	
	public Optional<MensajeHansolo> findBySecuenciaEnvio(Long secuencia);
	
	

}
