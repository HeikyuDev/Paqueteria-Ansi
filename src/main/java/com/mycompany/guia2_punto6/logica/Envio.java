package com.mycompany.guia2_punto6.logica;

import java.sql.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Envio {
    
    // Atributos
    @Id
    private int idEnvio;
    @Basic
    private boolean tarifaRecogidaAplicada;
    private double valorTarifa;
    
    @Temporal(TemporalType.DATE)
    private Date fechaEnvio; // Trabajara solo con la fecha (No la hora)
    
    @ManyToOne
    private ClienteFijo unClienteFijo;
    
    @ManyToOne
    private ClienteEsporadico unClienteEsporadico;
    
    @OneToMany (mappedBy = "unEnvio")
    private List<Paquete> unaListaPaquetes;
    
    // Constructores 

    public Envio(int idEnvio, boolean tarifaRecogidaAplicada, double valorTarifa, Date fechaEnvio, ClienteFijo unClienteFijo, ClienteEsporadico unClienteEsporadico, List<Paquete> unaListaPaquetes) {
        this.idEnvio = idEnvio;
        this.tarifaRecogidaAplicada = tarifaRecogidaAplicada;
        this.valorTarifa = valorTarifa;
        this.fechaEnvio = fechaEnvio;
        this.unClienteFijo = unClienteFijo;
        this.unClienteEsporadico = unClienteEsporadico;
        this.unaListaPaquetes = unaListaPaquetes;
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

    public ClienteFijo getUnClienteFijo() {
        return unClienteFijo;
    }

    public void setUnClienteFijo(ClienteFijo unClienteFijo) {
        this.unClienteFijo = unClienteFijo;
    }

    public ClienteEsporadico getUnClienteEsporadico() {
        return unClienteEsporadico;
    }

    public void setUnClienteEsporadico(ClienteEsporadico unClienteEsporadico) {
        this.unClienteEsporadico = unClienteEsporadico;
    }

    public List<Paquete> getUnaListaPaquetes() {
        return unaListaPaquetes;
    }

    public void setUnaListaPaquetes(List<Paquete> unaListaPaquetes) {
        this.unaListaPaquetes = unaListaPaquetes;
    }

    
    
    
    
}
