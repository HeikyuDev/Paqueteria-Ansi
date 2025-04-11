package com.mycompany.guia2_punto6.logica;

import java.sql.Date;

public class Envio {
    
    // Atributos
    private int idEnvio;
    private boolean tarifaRecogidaAplicada;
    private double valorTarifa;
    private Date fechaEnvio; // Trabajara solo con la fecha (No la hora)
    
    // Constructores ds

    public Envio(int idEnvio, boolean tarifaRecogidaAplicada, double valorTarifa, Date fechaEnvio) {
        this.idEnvio = idEnvio;
        this.tarifaRecogidaAplicada = tarifaRecogidaAplicada;
        this.valorTarifa = valorTarifa;
        this.fechaEnvio = fechaEnvio;
    }

    public Envio() {
    }
    
    // Getters y setters

    public int getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(int idEnvio) {
        this.idEnvio = idEnvio;
    }

    public boolean isTarifaRecogidaAplicada() {
        return tarifaRecogidaAplicada;
    }

    public void setTarifaRecogidaAplicada(boolean tarifaRecogidaAplicada) {
        this.tarifaRecogidaAplicada = tarifaRecogidaAplicada;
    }

    public double getValorTarifa() {
        return valorTarifa;
    }

    public void setValorTarifa(double valorTarifa) {
        this.valorTarifa = valorTarifa;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
    
    
    
}
