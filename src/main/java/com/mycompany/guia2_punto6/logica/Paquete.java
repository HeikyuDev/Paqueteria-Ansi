package com.mycompany.guia2_punto6.logica;

import java.sql.Date;

public class Paquete {
    //Atributos
    private int idPaquete;
    private String estadoPaquete;
    private Date fechaEntrega;
    private double volumenPaquete;
    private String direccionDestinatario;
    private String nombreDestinatario;
    private double pesoPaquete;
    
    // Constructores

    public Paquete(int idPaquete, String estadoPaquete, Date fechaEntrega, double volumenPaquete, String direccionDestinatario, String nombreDestinatario, double pesoPaquete) {
        this.idPaquete = idPaquete;
        this.estadoPaquete = estadoPaquete;
        this.fechaEntrega = fechaEntrega;
        this.volumenPaquete = volumenPaquete;
        this.direccionDestinatario = direccionDestinatario;
        this.nombreDestinatario = nombreDestinatario;
        this.pesoPaquete = pesoPaquete;
    }

    public Paquete() {
    }
    
    // Getters y Setters

    public int getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(int idPaquete) {
        this.idPaquete = idPaquete;
    }

    public String getEstadoPaquete() {
        return estadoPaquete;
    }

    public void setEstadoPaquete(String estadoPaquete) {
        this.estadoPaquete = estadoPaquete;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public double getVolumenPaquete() {
        return volumenPaquete;
    }

    public void setVolumenPaquete(double volumenPaquete) {
        this.volumenPaquete = volumenPaquete;
    }

    public String getDireccionDestinatario() {
        return direccionDestinatario;
    }

    public void setDireccionDestinatario(String direccionDestinatario) {
        this.direccionDestinatario = direccionDestinatario;
    }

    public String getNombreDestinatario() {
        return nombreDestinatario;
    }

    public void setNombreDestinatario(String nombreDestinatario) {
        this.nombreDestinatario = nombreDestinatario;
    }

    public double getPesoPaquete() {
        return pesoPaquete;
    }

    public void setPesoPaquete(double pesoPaquete) {
        this.pesoPaquete = pesoPaquete;
    }
    
}
