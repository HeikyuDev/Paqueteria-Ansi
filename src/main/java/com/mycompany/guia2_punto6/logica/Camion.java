package com.mycompany.guia2_punto6.logica;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Camion implements Serializable {
    
    // Atributos
    @Id
    private String matricula;
    @Basic
    private String tipo;
    private String ciudadOrigen;
    private String ciudadDestino;
    
    @OneToMany (mappedBy = "unCamion")
    private List<Jaula> unaListaJaula;
    // Constructor

    public Camion(String matricula, String tipo, String ciudadOrigen, String ciudadDestino, List<Jaula> unaListaJaula) {
        this.matricula = matricula;
        this.tipo = tipo;
        this.ciudadOrigen = ciudadOrigen;
        this.ciudadDestino = ciudadDestino;
        this.unaListaJaula = unaListaJaula;
    }
    

    public Camion() {
    }
    
    // Getters y Setters

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCiudadOrigen() {
        return ciudadOrigen;
    }

    public void setCiudadOrigen(String ciudadOrigen) {
        this.ciudadOrigen = ciudadOrigen;
    }

    public String getCiudadDestino() {
        return ciudadDestino;
    }

    public void setCiudadDestino(String ciudadDestino) {
        this.ciudadDestino = ciudadDestino;
    }

    public List<Jaula> getUnaListaJaula() {
        return unaListaJaula;
    }

    public void setUnaListaJaula(List<Jaula> unaListaJaula) {
        this.unaListaJaula = unaListaJaula;
    }
    
    
    
}
