package com.dvinac.hansolo.apirest.models.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="satelites")
public class Satelite implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
		
	@Column(nullable=false, unique=true)
	private String nombre;
	
	@Column(nullable=false)
	private Long cordX;
	
	@Column(nullable=false)
	private Long cordY;
	
	@Column(nullable=false)
	private Double distancia;
	
	@Column(nullable=false)
	private String mensaje;
	
	@Column(name="secuencia")
	private Long secuencia;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getCordX() {
		return cordX;
	}

	public void setCordX(Long cordX) {
		this.cordX = cordX;
	}

	public Long getCordY() {
		return cordY;
	}

	public void setCordY(Long cordY) {
		this.cordY = cordY;
	}

	
	public Double getDistancia() {
		return distancia;
	}

	public void setDistancia(Double distancia) {
		this.distancia = distancia;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Long getSecuencia() {
		return secuencia;
	}

	public void setSecuencia(Long secuencia) {
		this.secuencia = secuencia;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
}
