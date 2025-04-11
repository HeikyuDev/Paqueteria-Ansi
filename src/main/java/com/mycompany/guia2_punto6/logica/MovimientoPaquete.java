package com.mycompany.guia2_punto6.logica;

import java.util.Date;



public class MovimientoPaquete 
{   
    // Atributos
    private int idMovimiento;
    private  Date fechaHora;
    
    //Constructores

    public MovimientoPaquete(int idMovimiento, Date fechaHora) {
        this.idMovimiento = idMovimiento;
        this.fechaHora = fechaHora;
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
    
    
}
