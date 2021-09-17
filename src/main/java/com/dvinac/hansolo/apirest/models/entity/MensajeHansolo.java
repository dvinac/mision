package com.dvinac.hansolo.apirest.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.web.bind.annotation.CrossOrigin;

@Entity
@Table(name="mensajes_hansolo")
@CrossOrigin(origins = {"http//localhost:4200", "*"})
public class MensajeHansolo implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String mensaje;
	
	private Double origenX;
		
	private Double origenY;
	
	@Column(name="secuencia_envio")
	private Long secuenciaEnvio;	
		
	public List<Double> getLocation(double distancias[], double cordsX[], double cordsY[] ) {
		
		// Calculo de la interseccion de tres circunferencias		
		// la entrada son los datos de cada circunferencia centro(x,y) radio r -> distancias
		double x0, y0, x1, y1, x2, y2;
		double r0, r1, r2;
		double EPSILON = 0.00001;
		double a, dx, dy, d, h, rx, ry; 
		double point2_x, point2_y;
		
		List<Double> cords = new ArrayList<Double>();
		
		x0 = cordsX[0]; 
		y0 = cordsY[0];
		x1 = cordsX[1];
		y1 = cordsY[1];
		x2 = cordsX[2];
		y2 = cordsY[2];
		r0 = distancias[0];
		r1 = distancias[1];
		r2 = distancias[2];
		
		if( r0 > 0 && r1 > 0 && r2 > 0) {
					
			dx = x1 - x0;
			dy = y1 - y0; 
						 			
			d = Math.sqrt((dy*dy) + (dx*dx)); 			 
			
			if (d > (r0 + r1)) { 				 
				return null; 
			}
			
			if (d < Math.abs(r0 - r1)) { 				 
				return null; 
			} 
					
			a = ((r0*r0) - (r1*r1) + (d*d)) / (2.0 * d) ; 
			 
			point2_x = x0 + (dx * a/d); point2_y = y0 + (dy * a/d); 
			 
			h = Math.sqrt((r0*r0) - (a*a)); 
			 
			rx = -dy * (h/d); ry = dx * (h/d); 
			 
			double intersectionPoint1_x = point2_x + rx;
			double intersectionPoint2_x = point2_x - rx; 
			double intersectionPoint1_y = point2_y + ry; 
			double intersectionPoint2_y = point2_y - ry; 
						 
			dx = intersectionPoint1_x - x2; 
			dy = intersectionPoint1_y - y2; 
			double d1 = Math.sqrt((dy*dy) + (dx*dx)); 
			dx = intersectionPoint2_x - x2;
			dy = intersectionPoint2_y - y2;
			double d2 = Math.sqrt((dy*dy) + (dx*dx)); 
			
			if(Math.abs(d1 - r2) < EPSILON) {				
				cords.add(intersectionPoint1_x);
				cords.add(intersectionPoint1_y);
				return cords;
			} else { 
				if(Math.abs(d2 - r2) < EPSILON) {
					cords.add(intersectionPoint1_x);
					cords.add(intersectionPoint1_y);					
					return cords; 
				} else { 
					return null; 
				} 			
			} 
		} else {
			return null;
		}
	}
	
	
	public String getMessage( String messages[] ) {
		
		String mensajeHansolo = new String();
		int cantMsjs = messages.length;
    	String[] items1;
    	
    	List<String> msg1 = new ArrayList<String>(); 
    	List<String> msg2 = new ArrayList<String>();
    	List<String> msg3 = new ArrayList<String>();
    	    	    	
    	
    	for(int i = 0; i < cantMsjs; i++) {
    		items1 = messages[i].split(",");
    		
    		if(i == 0) {
    			for(int j = 0; j < items1.length; j++) {
            		if(items1[j].isEmpty()) {
            			msg1.add("");
            		} else {
            			msg1.add(items1[j]);
            		}    			    	
            	}
    		}
    		if(i == 1) {
    			for(int j = 0; j < items1.length; j++) {
            		if(items1[j].isEmpty()) {
            			msg2.add("");
            		} else {
            			msg2.add(items1[j]);
            		}    			    	
            	}
    		}
    		if(i == 2) {
    			for(int j = 0; j < items1.length; j++) {
            		if(items1[j].isEmpty()) {
            			msg3.add("");
            		} else {
            			msg3.add(items1[j]);
            		}    			    	
            	}
    		}
    	}
    	
    	int maxIndex = 0;
    	
    	
    	if(msg1.size() >= msg2.size()) {
    		if(msg1.size() >= msg3.size()) {
    			maxIndex = msg1.size();
    		} else {
    			maxIndex = msg3.size();
    		}
    		 
    	} else if(msg2.size() > msg3.size()) {
    		maxIndex = msg2.size();
    	} else {
    		maxIndex = msg3.size();    	
    	}
    	    	    	    	
    	if(maxIndex > msg1.size()) {
    		for(int i = msg1.size(); i <  maxIndex; i++) {
    			msg1.add(""); // igualar las listas con el maximo de campos
    		}
    	}
    	if(maxIndex > msg2.size()) {
    		for(int i = msg2.size(); i <  maxIndex; i++) {
    			msg2.add(""); // igualar las listas con el maximo de campos
    		}
    	}
    	if(maxIndex > msg3.size()) {
    		for(int i = msg3.size(); i <  maxIndex; i++) {
    			msg3.add(""); // igualar las listas con el maximo de campos
    		}
    	}
    		 	    	    	    
    	for(int i = 0; i < msg2.size(); i++) {
    		if(msg1.get(i) != "") {
    			mensajeHansolo = mensajeHansolo + " " + msg1.get(i); 
    		} else {
    			if(msg2.get(i) != "") {
    				mensajeHansolo = mensajeHansolo + " " + msg2.get(i);
    			} else {
    				if(msg3.get(i) != "") {
    					mensajeHansolo = mensajeHansolo + " " + msg2.get(i);
    				}
    			}
    		}
    	}
    	
    	return mensajeHansolo;
	}
		
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Double getOrigenX() {
		return origenX;
	}

	public void setOrigenX(Double origenX) {
		this.origenX = origenX;
	}

	public Double getOrigenY() {
		return origenY;
	}

	public void setOrigenY(Double origenY) {
		this.origenY = origenY;
	}
		
	
	public Long getSecuenciaEnvio() {
		return secuenciaEnvio;
	}



	public void setSecuenciaEnvio(Long secuenciaEnvio) {
		this.secuenciaEnvio = secuenciaEnvio;
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
