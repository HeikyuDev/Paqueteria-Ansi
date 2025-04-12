package com.mycompany.guia2_punto6.logica;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Ciudad implements Serializable {
    
    @Id
    private int idCiudad;
    
    @Basic
    private String nombreCiudad;
    
    @OneToMany (mappedBy = "unaCiudad")
    private List<Delegacion> unaListaDelegacion;
    // Constructor

    public Ciudad(int idCiudad, String nombreCiudad, List<Delegacion> unaListaDelegacion) {
        this.idCiudad = idCiudad;
        this.nombreCiudad = nombreCiudad;
        this.unaListaDelegacion = unaListaDelegacion;
    }

    public Ciudad() {
    }    
    
    // Getters y setters

    public int getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public void setNombreCiudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }

    public List<Delegacion> getUnaListaDelegacion() {
        return unaListaDelegacion;
    }

    public void setUnaListaDelegacion(List<Delegacion> unaListaDelegacion) {
        this.unaListaDelegacion = unaListaDelegacion;
    }
    
    
    
}
