package com.mycompany.guia2_punto6.logica;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ClienteEsporadico extends Cliente
{
    
    // Atributos
    @Id
    private String dniCliente;
    
    @OneToMany (mappedBy = "unClienteEsporadico")
    private List<Envio> listaEnvios; 
    
    // Constructores

    public ClienteEsporadico(String dniCliente, List<Envio> listaEnvios, String nombre, String apellido, String telefono) {
        super(nombre, apellido, telefono);
        this.dniCliente = dniCliente;
        this.listaEnvios = listaEnvios;
    }

    public ClienteEsporadico(String dniCliente, List<Envio> listaEnvios) {
        this.dniCliente = dniCliente;
        this.listaEnvios = listaEnvios;
    }

    public ClienteEsporadico() {
    }

    
    
    // Getters y Setters

    public String getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }

    public List<Envio> getListaEnvios() {
        return listaEnvios;
    }

    public void setListaEnvios(List<Envio> listaEnvios) {
        this.listaEnvios = listaEnvios;
    }
    
}
