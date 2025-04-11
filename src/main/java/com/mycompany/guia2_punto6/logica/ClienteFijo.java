package com.mycompany.guia2_punto6.logica;


public class ClienteFijo extends Cliente
{
    private int codigoCliente;

    // Constructores: 
    public ClienteFijo(int codigoCliente, String nombre, String apellido, String telefono) {
        super(nombre, apellido, telefono);
        this.codigoCliente = codigoCliente;
    }

    public ClienteFijo(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public ClienteFijo() {
    }
    
    
    // Getters y Setters

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }  
    
}
