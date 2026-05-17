package com.project.models;

/**
 * Línea de detalle de planilla.
 * conceptoNombre y conceptoTipo se llenan desde JOIN en el DAO.
 */
public class PlanillaDetalle {

    private int    id;
    private int    planilla_id;
    private int    concepto_id;
    private double monto;
    private double cantidad;
    private String descripcion;

    // Extra para la vista
    private String conceptoNombre;
    private String conceptoTipo;   // INGRESO | DESCUENTO

    public PlanillaDetalle() {}

    public PlanillaDetalle(int planilla_id, int concepto_id,
                           double monto, double cantidad, String descripcion) {
        this.planilla_id = planilla_id;
        this.concepto_id = concepto_id;
        this.monto = monto;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
    }

    // ── Getters / Setters ──────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPlanilla_id() { return planilla_id; }
    public void setPlanilla_id(int planilla_id) { this.planilla_id = planilla_id; }

    public int getConcepto_id() { return concepto_id; }
    public void setConcepto_id(int concepto_id) { this.concepto_id = concepto_id; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getConceptoNombre() { return conceptoNombre; }
    public void setConceptoNombre(String conceptoNombre) { this.conceptoNombre = conceptoNombre; }

    public String getConceptoTipo() { return conceptoTipo; }
    public void setConceptoTipo(String conceptoTipo) { this.conceptoTipo = conceptoTipo; }

    public boolean esIngreso() {
        return "INGRESO".equalsIgnoreCase(conceptoTipo);
    }
}
