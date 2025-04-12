package com.mycompany.guia2_punto6.logica;

import java.io.Serializable;
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
public class Paquete implements Serializable {
    //Atributos
    @Id
    private int idPaquete;
    
        
    @Temporal(TemporalType.DATE)
    private Date fechaEntrega;
    
    @Basic
    private String estadoPaquete;
    private double volumenPaquete;
    private String direccionDestinatario;
    private String nombreDestinatario; 
    private double pesoPaquete;
    
    @ManyToOne
    private Envio unEnvio;
    
    @OneToMany(mappedBy = "unPaquete")
    private List<MovimientoPaquete> unaListaMovimientos;
    
    @ManyToOne
    private Jaula unaJaula;
    
    // Constructores

    public Paquete(int idPaquete, Date fechaEntrega, String estadoPaquete, double volumenPaquete, String direccionDestinatario, String nombreDestinatario, double pesoPaquete, Envio unEnvio, List<MovimientoPaquete> unaListaMovimientos, Jaula unaJaula) {
        this.idPaquete = idPaquete;
        this.fechaEntrega = fechaEntrega;
        this.estadoPaquete = estadoPaquete;
        this.volumenPaquete = volumenPaquete;
        this.direccionDestinatario = direccionDestinatario;
        this.nombreDestinatario = nombreDestinatario;
        this.pesoPaquete = pesoPaquete;
        this.unEnvio = unEnvio;
        this.unaListaMovimientos = unaListaMovimientos;
        this.unaJaula = unaJaula;
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

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getEstadoPaquete() {
        return estadoPaquete;
    }

    public void setEstadoPaquete(String estadoPaquete) {
        this.estadoPaquete = estadoPaquete;
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

    public Envio getUnEnvio() {
        return unEnvio;
    }

    public void setUnEnvio(Envio unEnvio) {
        this.unEnvio = unEnvio;
    }

    public List<MovimientoPaquete> getUnaListaMovimientos() {
        return unaListaMovimientos;
    }

    public void setUnaListaMovimientos(List<MovimientoPaquete> unaListaMovimientos) {
        this.unaListaMovimientos = unaListaMovimientos;
    }

    public Jaula getUnaJaula() {
        return unaJaula;
    }

    public void setUnaJaula(Jaula unaJaula) {
        this.unaJaula = unaJaula;
    }
    
    
    
    
    
    
    

    

    
    
}
