package com.mycompany.guia2_punto6.logica;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
public class MovimientoPaquete implements Serializable 
{   
    // Atributos
    @Id
    private int idMovimiento;
    
    @Temporal(TemporalType.TIMESTAMP)
    private  Date fechaHora;
    
    @ManyToOne
    private Paquete unPaquete;
    
    //Constructores

    public MovimientoPaquete(int idMovimiento, Date fechaHora, Paquete unPaquete) {
        this.idMovimiento = idMovimiento;
        this.fechaHora = fechaHora;
        this.unPaquete = unPaquete;
    }
    

    public MovimientoPaquete() {
    }
    // Getters y Setters

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Paquete getUnPaquete() {
        return unPaquete;
    }

    public void setUnPaquete(Paquete unPaquete) {
        this.unPaquete = unPaquete;
    }
    
    
    
    
}
