package com.lab.beans;

import java.math.BigDecimal;

public class TicketTipo {

    private int idTicketTipo;

    private int idEvento;

    private String nombre;

    private BigDecimal precio;

    private int cupoTotal;

    private int cupoDisponible;

    public int getIdTicketTipo() {
        return idTicketTipo;
    }

    public void setIdTicketTipo(int idTicketTipo) {
        this.idTicketTipo = idTicketTipo;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getCupoTotal() {
        return cupoTotal;
    }

    public void setCupoTotal(int cupoTotal) {
        this.cupoTotal = cupoTotal;
    }

    public int getCupoDisponible() {
        return cupoDisponible;
    }

    public void setCupoDisponible(int cupoDisponible) {
        this.cupoDisponible = cupoDisponible;
    }
}
