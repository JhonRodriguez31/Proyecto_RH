package com.project.services.impl;

import com.project.DAO.ContratoDAO;
import com.project.DAO.PlanillaDAO;
import com.project.DAO.PlanillaDetalleDAO;
import com.project.DAO.impl.ContratoDAOImpl;
import com.project.DAO.impl.PlanillaDAOImpl;
import com.project.DAO.impl.PlanillaDetalleDAOImpl;
import com.project.models.Concepto;
import com.project.models.Contrato;
import com.project.models.Planilla;
import com.project.models.PlanillaDetalle;
import com.project.services.PlanillaService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Reglas de negocio del módulo de planilla:
 *
 *  INGRESOS
 *  ────────
 *  concepto_id=0  SUELDO      → sueldo_base del contrato
 *
 *  DESCUENTOS
 *  ──────────
 *  concepto_id=4  AFP/ONP     → 13% del sueldo base
 *  concepto_id=5  TARDANZA    → parametro (minutos * valor_minuto)
 *  concepto_id=6  FALTA       → parámetro (días * valor_día)
 */
public class PlanillaServiceImpl implements PlanillaService {

   //conceptos segun la tabla Concepto
	private static final int CONCEPTO_SUELDO   = 1;
	private static final int CONCEPTO_AFP      = 6;
	private static final int CONCEPTO_TARDANZA = 9;
	private static final int CONCEPTO_FALTA    = 10;

    private static final double PORCENTAJE_PENSION = 0.13;  

    private final PlanillaDAO       planillaDAO;
    private final PlanillaDetalleDAO detalleDAO;
    private final ContratoDAO       contratoDAO;

    public PlanillaServiceImpl() {
        this.planillaDAO  = new PlanillaDAOImpl();
        this.detalleDAO   = new PlanillaDetalleDAOImpl();
        this.contratoDAO  = new ContratoDAOImpl();
    }

    @Override
    public List<Planilla> listarPlanillas() {
        return planillaDAO.listar();
    }

    @Override
    public Planilla obtenerPlanilla(Integer id) {
        return planillaDAO.obtenerPorId(id);
    }

    @Override
    public List<PlanillaDetalle> obtenerDetalles(Integer planillaId) {
        return detalleDAO.listarPorPlanilla(planillaId);
    }

    public void generarPlanilla(Planilla planilla,
                                int minutosExtra,
                                int diasFalta,
                                Integer usuarioId) {

        // ── Validaciones previas ──────────────────────────
        if (planilla.getPeriodo() == null || !planilla.getPeriodo().matches("\\d{4}-\\d{2}")) {
            throw new IllegalArgumentException("El periodo debe tener formato YYYY-MM (ej: 2025-05).");
        }

        // Obtener contrato activo si no se pasó contrato_id
        Contrato contrato = contratoDAO.obtenerActivoPorEmpleado(planilla.getEmpleado_id());
        if (contrato == null) {
            throw new IllegalStateException(
                    "El empleado no tiene un contrato activo. No se puede generar la planilla.");
        }

        planilla.setContrato_id(contrato.getId());
        planilla.setEstado("GENERADA");

        // ── Guardar cabecera ──────────────────────────────
        Integer planillaId = planillaDAO.registrar(planilla);
        if (planillaId == null) {
            throw new RuntimeException("No se pudo crear la planilla.");
        }
        planilla.setId(planillaId);

        // ── Calcular y guardar detalles ───────────────────
        List<PlanillaDetalle> detalles = calcularDetalles(
                planillaId, contrato, minutosExtra, diasFalta);

        for (PlanillaDetalle d : detalles) {
            detalleDAO.registrarDetalle(d);
        }
    }

    // ────────────────────────────────────────────────────────────
    // (previsualizar antes de guardar)
    // ────────────────────────────────────────────────────────────
    public List<PlanillaDetalle> calcularDetallesPreview(Integer empleadoId,
                                                         int minutosExtra,
                                                         int diasFalta) {
        Contrato contrato = contratoDAO.obtenerActivoPorEmpleado(empleadoId);
        if (contrato == null) {
            throw new IllegalStateException("El empleado no tiene contrato activo.");
        }
        return calcularDetalles(-1, contrato, minutosExtra, diasFalta);
    }

    // ── Lógica de cálculo ─────────────────────────────────────

    private List<PlanillaDetalle> calcularDetalles(int planillaId,
                                                   Contrato contrato,
                                                   int minutosExtra,
                                                   int diasFalta) {
        List<PlanillaDetalle> detalles = new ArrayList<>();
        double sueldoBase = contrato.getSueldoBase().doubleValue();

        // 1. SUELDO BASE
        PlanillaDetalle linSueldo = new PlanillaDetalle();
        linSueldo.setPlanilla_id(planillaId);
        linSueldo.setConcepto_id(CONCEPTO_SUELDO);
        linSueldo.setMonto(sueldoBase);
        linSueldo.setCantidad(1);
        linSueldo.setDescripcion("Sueldo mensual - " + contrato.getCargo());
        linSueldo.setConceptoNombre("SUELDO");
        linSueldo.setConceptoTipo("INGRESO");
        detalles.add(linSueldo);

        // 2. DESCUENTO AFP/ONP (13%)
        double descuentoPension = round(sueldoBase * PORCENTAJE_PENSION);
        String nombrePension = contrato.getSistemaPension() != null
                ? contrato.getSistemaPension() : "AFP";
        PlanillaDetalle linPension = new PlanillaDetalle();
        linPension.setPlanilla_id(planillaId);
        linPension.setConcepto_id(CONCEPTO_AFP);
        linPension.setMonto(-descuentoPension);  // negativo = descuento
        linPension.setCantidad(1);
        linPension.setDescripcion(nombrePension + " 13% sobre sueldo base");
        linPension.setConceptoNombre(nombrePension);
        linPension.setConceptoTipo("DESCUENTO");
        detalles.add(linPension);

        // 3. DESCUENTO POR TARDANZA (proporcional: sueldo / 30 días / 8h / 60 min)
        if (minutosExtra > 0) {
            double valorMinuto = sueldoBase / 30.0 / 8.0 / 60.0;
            double descTardanza = round(valorMinuto * minutosExtra);
            PlanillaDetalle linTardanza = new PlanillaDetalle();
            linTardanza.setPlanilla_id(planillaId);
            linTardanza.setConcepto_id(CONCEPTO_TARDANZA);
            linTardanza.setMonto(-descTardanza);
            linTardanza.setCantidad(minutosExtra);
            linTardanza.setDescripcion("Descuento por " + minutosExtra + " min. de tardanza");
            linTardanza.setConceptoNombre("TARDANZA");
            linTardanza.setConceptoTipo("DESCUENTO");
            detalles.add(linTardanza);
        }

        // 4. DESCUENTO POR FALTA (un día = sueldo / 30)
        if (diasFalta > 0) {
            double valorDia = sueldoBase / 30.0;
            double descFalta = round(valorDia * diasFalta);
            PlanillaDetalle linFalta = new PlanillaDetalle();
            linFalta.setPlanilla_id(planillaId);
            linFalta.setConcepto_id(CONCEPTO_FALTA);
            linFalta.setMonto(-descFalta);
            linFalta.setCantidad(diasFalta);
            linFalta.setDescripcion("Descuento por " + diasFalta + " día(s) de falta");
            linFalta.setConceptoNombre("FALTA");
            linFalta.setConceptoTipo("DESCUENTO");
            detalles.add(linFalta);
        }

        return detalles;
    }

    // Redondea a 2 decimales
    private double round(double valor) {
        return BigDecimal.valueOf(valor)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    // ── Métodos de la interfaz ────────────────────────────────

    @Override
    public Integer registrarPlanilla(Planilla planilla,
                                  List<PlanillaDetalle> detalles,
                                  Integer usuarioId) {
        planilla.setEstado("GENERADA");
        Integer planillaId = planillaDAO.registrar(planilla);
        if (planillaId == null) throw new RuntimeException("No se pudo guardar la planilla.");
        planilla.setId(planillaId);
        for (PlanillaDetalle d : detalles) {
            d.setPlanilla_id(planillaId);
            detalleDAO.registrarDetalle(d);
        }
		return planillaId;
    }

    @Override
    public void actualizarPlanilla(Planilla planilla,
                                   List<PlanillaDetalle> detalles,
                                   Integer usuarioId) {
        planillaDAO.actualizar(planilla);
        detalleDAO.eliminarPorPlanilla(planilla.getId());
        for (PlanillaDetalle d : detalles) {
        	d.setPlanilla_id(planilla.getId());
        	
            detalleDAO.registrarDetalle(d);
        }
    }

    @Override
    public void eliminarPlanilla(Integer id) {
        planillaDAO.eliminar(id);
    }
}
