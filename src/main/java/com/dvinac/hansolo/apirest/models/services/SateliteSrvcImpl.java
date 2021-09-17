package com.dvinac.hansolo.apirest.models.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dvinac.hansolo.apirest.models.entity.MensajeHansolo;
import com.dvinac.hansolo.apirest.models.entity.Satelite;
import com.dvinac.hansolo.apirest.models.repository.MensajeHansoloRepository;
import com.dvinac.hansolo.apirest.models.repository.SatRepository;

@Service
public class SateliteSrvcImpl implements ISatService {

	// implementacion del servicio CRUD el objeto satelite
	// permite la consulta, creacion y actualizacion del objeto satelite en la base de datos
	
	@Autowired 
	private SatRepository satRepository;
	
	@Autowired
	private MensajeHansoloRepository mensajeHansoloRepo;
	
	@Override
	@Transactional(readOnly = true)
	public List<Satelite> findAll() {
		return (List<Satelite>) satRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Satelite findById(Long id) {
		return satRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Satelite guardar(Satelite satelite) {
		return satRepository.save(satelite);		
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		satRepository.deleteById(id);
	}

	@Override
	public Optional<MensajeHansolo> findBySecuenciaEnvio(Long secuencia) {
		return mensajeHansoloRepo.findBySecuenciaEnvio(secuencia);
	}

	@Override
	public MensajeHansolo guardaMsjHanSolo(MensajeHansolo mensajeHansolo) {		
		return mensajeHansoloRepo.save(mensajeHansolo);
	}

	@Override
	public Satelite findByNombre(String satellite_name) {		
		return satRepository.findByNombre(satellite_name);
	}
	
}
