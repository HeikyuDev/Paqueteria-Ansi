package com.mycompany.guia2_punto6.logica;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Delegacion implements Serializable {
    
    @Id
    private int idDelegacion;
    
    @Basic
    private String nombreDelegacion;
    private String direccionDelegacion;
    
    @OneToMany(mappedBy = "unaDelegacion")
    private List<Jaula> unaListaJaula;
    
    @ManyToOne
    private Ciudad unaCiudad;
    
    // Cosntructores

    public Delegacion(int idDelegacion, String nombreDelegacion, String direccionDelegacion, List<Jaula> unaListaJaula, Ciudad unaCiudad) {
        this.idDelegacion = idDelegacion;
        this.nombreDelegacion = nombreDelegacion;
        this.direccionDelegacion = direccionDelegacion;
        this.unaListaJaula = unaListaJaula;
        this.unaCiudad = unaCiudad;
    }       

    public Delegacion() {
    }
    
    // Getters y Setters

    public int getIdDelegacion() {
        return idDelegacion;
    }

    public void setIdDelegacion(int idDelegacion) {
        this.idDelegacion = idDelegacion;
    }

    public String getNombreDelegacion() {
        return nombreDelegacion;
    }

    public void setNombreDelegacion(String nombreDelegacion) {
        this.nombreDelegacion = nombreDelegacion;
    }

    public String getDireccionDelegacion() {
        return direccionDelegacion;
    }

    public void setDireccionDelegacion(String direccionDelegacion) {
        this.direccionDelegacion = direccionDelegacion;
    }

    public List<Jaula> getUnaListaJaula() {
        return unaListaJaula;
    }

    public void setUnaListaJaula(List<Jaula> unaListaJaula) {
        this.unaListaJaula = unaListaJaula;
    }

    public Ciudad getUnaCiudad() {
        return unaCiudad;
    }

    public void setUnaCiudad(Ciudad unaCiudad) {
        this.unaCiudad = unaCiudad;
    }                
}
