package com.project.DAO;

import com.project.models.Beneficio;
import java.util.List;

public interface BeneficioDAO {

    List<Beneficio> obtenerBeneficios();


    boolean insertarBeneficio(Beneficio beneficio);
    boolean actualizarBeneficio(Beneficio beneficio);
    boolean eliminarBeneficio(int id);
}