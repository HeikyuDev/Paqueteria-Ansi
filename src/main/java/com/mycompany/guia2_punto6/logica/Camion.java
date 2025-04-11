package com.mycompany.guia2_punto6.logica;

public class Camion {
    
    // Atributos
    private String matricula;
    private String tipo;
    private String ciudadOrigen;
    private String ciudadDestino;
    
    // Constructor

    public Camion(String matricula, String tipo, String ciudadOrigen, String ciudadDestino) {
        this.matricula = matricula;
        this.tipo = tipo;
        this.ciudadOrigen = ciudadOrigen;
        this.ciudadDestino = ciudadDestino;
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
    
    
    
}
