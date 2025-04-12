package com.mycompany.guia2_punto6.logica;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Jaula implements Serializable {
    //Atributos
    @Id
    private int idJaula;
    
    @OneToMany(mappedBy = "unaJaula")
    private List<Paquete> unaListaPaquetes;
    
    @ManyToOne 
    private Camion unCamion;
    
    @ManyToOne
    private Delegacion unaDelegacion;
    // Constructores

    public Jaula() {
    }

    public Jaula(int idJaula, List<Paquete> unaListaPaquetes, Camion unCamion, Delegacion unaDelegacion) {
        this.idJaula = idJaula;
        this.unaListaPaquetes = unaListaPaquetes;
        this.unCamion = unCamion;
        this.unaDelegacion = unaDelegacion;
    }

    
    
    
    // Getters y Setters

    public int getIdJaula() {
        return idJaula;
    }

    public void setIdJaula(int idJaula) {
        this.idJaula = idJaula;
    }

    public List<Paquete> getUnaListaPaquetes() {
        return unaListaPaquetes;
    }

    public void setUnaListaPaquetes(List<Paquete> unaListaPaquetes) {
        this.unaListaPaquetes = unaListaPaquetes;
    }

    public Camion getUnCamion() {
        return unCamion;
    }

    public void setUnCamion(Camion unCamion) {
        this.unCamion = unCamion;
    }

    public Delegacion getUnaDelegacion() {
        return unaDelegacion;
    }

    public void setUnaDelegacion(Delegacion unaDelegacion) {
        this.unaDelegacion = unaDelegacion;
    }
    
    
    
    
}
