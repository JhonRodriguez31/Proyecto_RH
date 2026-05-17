package com.project.models;

public abstract class Beneficio {
    protected int id;
    protected String nombre;
    protected String descripcion;
    protected double monto; // En IntelliJ lo llamamos 'monto' para que el DAO lo encuentre

    public Beneficio() {}

    // Estos métodos son los que IntelliJ dice que "no encuentra" en rojo
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public abstract String obtenerDetalleEspecial();

    @Override
    public String toString() {
        // Esto hace que en la lista se vea "Nombre (S/ 150.0)"
        return (nombre != null ? nombre : "Cargando...") + " (S/ " + monto + ")";
    }
}