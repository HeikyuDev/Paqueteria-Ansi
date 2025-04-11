package com.mycompany.guia2_punto6.logica;

public class Delegacion {
    
    private int idDelegacion;
    private String nombreDelegacion;
    private String direccionDelegacion;
    
    // Cosntructores
 
    public Delegacion(int idDelegacion, String nombreDelegacion, String direccionDelegacion) {
        this.idDelegacion = idDelegacion;
        this.nombreDelegacion = nombreDelegacion;
        this.direccionDelegacion = direccionDelegacion;
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

    
    
    
    
}
