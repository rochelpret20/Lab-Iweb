package com.lab.beans;

public class ReservaItem {

    private int idItem;

    private int idUsuario;

    private int idTicketTipo;

    private int cantidad;

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdTicketTipo() {
        return idTicketTipo;
    }

    public void setIdTicketTipo(int idTicketTipo) {
        this.idTicketTipo = idTicketTipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
