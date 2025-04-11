package com.mycompany.guia2_punto6.logica;

public class ClienteEsporadico extends Cliente
{
    
    // Atributos
    private String dniCliente;
    
    // Constructores

    public ClienteEsporadico(String dni, String nombre, String apellido, String telefono) {
        super(nombre, apellido, telefono);
        this.dniCliente = dni;
    }

    public ClienteEsporadico(String dni) {
        this.dniCliente = dni;
    }
    
    public ClienteEsporadico() {
    }
    
    // Getters y Setters

    public String getDni() {
        return dniCliente;
    }

    public void setDni(String dni) {
        this.dniCliente = dni;
    }
    
    
    
}
