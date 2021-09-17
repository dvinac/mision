package com.dvinac.hansolo.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dvinac.hansolo.apirest.models.entity.MensajeHansolo;
import com.dvinac.hansolo.apirest.models.entity.Satelite;
import com.dvinac.hansolo.apirest.models.services.ISatService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http//localhost:4200", "*"})
public class HanSoloController {
	
	
	// Esto es un CRUD basico que permite consultar, crear, eliminar y editar un satelite
	@Autowired
	private ISatService satService;
	
	@GetMapping("/satelites")
	public ResponseEntity<?> consultarSat(){
		return ResponseEntity.ok().body(satService.findAll());
	}
	
	@GetMapping("satelites/{id}")
	public ResponseEntity<?> consultaUno(@PathVariable Long id){
		
		Satelite satelite = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			satelite = satService.findById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(satelite == null) {
			response.put("mensaje", "El satelite ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Satelite>(satelite, HttpStatus.OK);
		
	}
	
	@PostMapping("/satelites")
	public ResponseEntity<?> crearSatelite(@RequestBody Satelite satelite){
		Satelite sateliteDb = satService.guardar(satelite);
		return ResponseEntity.status(HttpStatus.CREATED).body(sateliteDb);
	}
	
	// Editar un satelite ingresando el id
	
	@PutMapping("/satelites/{id}")
	public ResponseEntity<?> editar(@RequestBody Satelite satelite, BindingResult result, @PathVariable Long id){
		
		Satelite sateliteActual = satService.findById(id);
		Satelite sateliteUpdated = null;

		Map<String, Object> response = new HashMap<>();

		if(result.hasErrors()) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (sateliteActual == null) {
			response.put("mensaje", "Error: no se pudo editar, el satelite ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {

			sateliteActual.setCordX(satelite.getCordX());
			sateliteActual.setCordY(satelite.getCordY());
			sateliteActual.setDistancia(satelite.getDistancia());
			sateliteActual.setMensaje(satelite.getMensaje());
			sateliteActual.setSecuencia(satelite.getSecuencia());

			sateliteUpdated = satService.guardar(sateliteActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el satelite en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El satelite ha sido actualizado con éxito!");
		response.put("satelite", sateliteUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);						
	}
	
	// Eliminar un satelite ingresando el id del satelite	
	@DeleteMapping("/satelites/{id}")
	public ResponseEntity<?> eliminarSat(@PathVariable Long id){
		satService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/topsecret")
	public ResponseEntity<?> crearMensaje(@RequestBody List<Satelite> satelites){
		
		int cantSat = satelites.size();
		double distancias[] = new double[cantSat];
		double cordsX[] = new double[cantSat];
		double cordsY[] = new double[cantSat];
		
		String mensajesSat[] = new String[cantSat];
				
		
		MensajeHansolo mensajeHansolo = new MensajeHansolo();
		
		for(int i = 0; i < satelites.size() ; i++) {
			distancias[i] = satelites.get(i).getDistancia();
			cordsX[i] = satelites.get(i).getCordX();
			cordsY[i] = satelites.get(i).getCordY();			
			mensajesSat[i] = satelites.get(i).getMensaje();			
		}
		
		List<Double> hanSoloCords = mensajeHansolo.getLocation(distancias, cordsX, cordsY);
		
		String msgHanSolo = mensajeHansolo.getMessage(mensajesSat);
		
		if(hanSoloCords.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		mensajeHansolo.setOrigenX(hanSoloCords.get(0));
		mensajeHansolo.setOrigenY(hanSoloCords.get(1));
		mensajeHansolo.setMensaje(msgHanSolo);
		mensajeHansolo.setSecuenciaEnvio(satelites.get(0).getSecuencia());
						
		MensajeHansolo mensajeHansoloDb = satService.guardaMsjHanSolo(mensajeHansolo);
		return ResponseEntity.status(HttpStatus.CREATED).body(mensajeHansoloDb);
	}
	
	@PostMapping("/topsecret_split/{satellite_name}")
	public ResponseEntity<?> topSecretSplit(@RequestBody Satelite satelite, BindingResult result, @PathVariable String satellite_name){
		
		Satelite sateliteActual = satService.findByNombre(satellite_name);
		Satelite sateliteUpdated = null;

		Map<String, Object> response = new HashMap<>();

		if(result.hasErrors()) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (sateliteActual == null) {
			// si el satelite no existe lo crea como nuevo satelite
			Satelite sateliteDb = satService.guardar(satelite);
			return ResponseEntity.status(HttpStatus.CREATED).body(sateliteDb);
		} else {
			// si ya existe lo edita
			try {

				sateliteActual.setCordX(satelite.getCordX());
				sateliteActual.setCordY(satelite.getCordY());
				sateliteActual.setDistancia(satelite.getDistancia());
				sateliteActual.setMensaje(satelite.getMensaje());
				sateliteActual.setSecuencia(satelite.getSecuencia());

				sateliteUpdated = satService.guardar(sateliteActual);

			} catch (DataAccessException e) {
				response.put("mensaje", "Error al actualizar el satelite en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			response.put("mensaje", "El satelite ha sido actualizado con éxito!");
			response.put("satelite", sateliteUpdated);

		}
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/topsecret_split/{satellite_name}")
	public ResponseEntity<?> getTopSecreteSplit(@PathVariable String satellite_name){
		
		
		Satelite sateliteActual = satService.findByNombre(satellite_name);

		Map<String, Object> response = new HashMap<>();
		
		if (sateliteActual == null) {
			response.put("mensaje", "Error: el satelite: "
					.concat(satellite_name.concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		} 
			
		
		MensajeHansolo mensajeHansolo = new MensajeHansolo();		
		
		// 2. Verifica que existan los tres satelites para poder invocar los metodos getLocation y getMessage
		List<Satelite> satelites = satService.findAll();
				
		if(satelites.size() < 3) {
			response.put("mensaje", "Error: falta información para determinar la ubicación y el mensaje ");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);			
		}
				
		if(satelites.size() == 3) {
			
			int cantSat = satelites.size();
			double distancias[] = new double[cantSat];
			double cordsX[] = new double[cantSat];
			double cordsY[] = new double[cantSat];
			
			String mensajesSat[] = new String[cantSat];											
			
			for(int i = 0; i < satelites.size() ; i++) {
				distancias[i] = satelites.get(i).getDistancia();
				cordsX[i] = satelites.get(i).getCordX();
				cordsY[i] = satelites.get(i).getCordY();			
				mensajesSat[i] = satelites.get(i).getMensaje();			
			}
			
			List<Double> hanSoloCords = mensajeHansolo.getLocation(distancias, cordsX, cordsY);
			
			String msgHanSolo = mensajeHansolo.getMessage(mensajesSat);
			
			if(hanSoloCords.isEmpty()) {
				response.put("mensaje", "Error: No se logró determinar la ubicación del emisor ");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);				
			}
			
			mensajeHansolo.setOrigenX(hanSoloCords.get(0));
			mensajeHansolo.setOrigenY(hanSoloCords.get(1));
			mensajeHansolo.setMensaje(msgHanSolo);
			mensajeHansolo.setSecuenciaEnvio(satelites.get(0).getSecuencia());
							
						
		}
		
		MensajeHansolo mensajeHansoloDb = satService.guardaMsjHanSolo(mensajeHansolo);
		return ResponseEntity.status(HttpStatus.CREATED).body(mensajeHansoloDb);
	}

}
