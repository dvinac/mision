package com.dvinac.hansolo.apirest.models.services;

import java.util.List;
import java.util.Optional;
import com.dvinac.hansolo.apirest.models.entity.MensajeHansolo;
import com.dvinac.hansolo.apirest.models.entity.Satelite;

public interface ISatService {
	
	// interfase para el manejo de los datos del satelite
	
	public List<Satelite> findAll();
	
	public Satelite findById(Long id);

	public Satelite guardar(Satelite satelite);
	
	public void deleteById(Long id);
				
	// datos del mensaje emitido
	
	public Optional<MensajeHansolo> findBySecuenciaEnvio(Long secuencia);
	
	public MensajeHansolo guardaMsjHanSolo(MensajeHansolo mensajeHansolo);

	public Satelite findByNombre(String satellite_name);
}
