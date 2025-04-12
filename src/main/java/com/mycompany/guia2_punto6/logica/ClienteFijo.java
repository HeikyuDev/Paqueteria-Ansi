package com.mycompany.guia2_punto6.logica;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ClienteFijo extends Cliente implements Serializable
{
    @Id
    private int codigoCliente;
    
    @OneToMany (mappedBy = "unClienteFijo") // Indico donde esta el uno
    private List<Envio> listaEnvios; // Dudoso que este bien
    
    // Constructores: 

    public ClienteFijo(int codigoCliente, List<Envio> listaEnvios, String nombre, String apellido, String telefono) {
        super(nombre, apellido, telefono);
        this.codigoCliente = codigoCliente;
        this.listaEnvios = listaEnvios;
    }

    public ClienteFijo(int codigoCliente, List<Envio> listaEnvios) {
        this.codigoCliente = codigoCliente;
        this.listaEnvios = listaEnvios;
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

    public List<Envio> getListaEnvios() {
        return listaEnvios;
    }

    public void setListaEnvios(List<Envio> listaEnvios) {
        this.listaEnvios = listaEnvios;
    }
}
