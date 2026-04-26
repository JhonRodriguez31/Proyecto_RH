INSERT INTO dbo.Empleado (codigo_empleado, nombres, apellidos, dni, telefono, correo, direccion, fecha_nacimiento, fecha_ingreso, estado, dias_vacaciones_disponibles) VALUES
('EMP001', 'Juan', 'Perez', '71234567', '987654321', 'juan.perez@empresa.com', 'Av. Lima 123', '1990-05-15', '2020-01-10', 'ACTIVO', 15),
('EMP002', 'Maria', 'Gomez', '72345678', '987654322', 'maria.gomez@empresa.com', 'Calle Arequipa 456', '1988-11-20', '2019-03-15', 'ACTIVO', 10),
('EMP003', 'Carlos', 'Ruiz', '73456789', '987654323', 'carlos.ruiz@empresa.com', 'Jr. Trujillo 789', '1992-02-10', '2021-06-01', 'ACTIVO', 20),
('EMP004', 'Ana', 'Torres', '74567890', '987654324', 'ana.torres@empresa.com', 'Av. Piura 321', '1995-08-25', '2022-01-15', 'ACTIVO', 5),
('EMP005', 'Luis', 'Flores', '75678901', '987654325', 'luis.flores@empresa.com', 'Calle Cusco 654', '1985-12-05', '2018-09-20', 'ACTIVO', 25),
('EMP006', 'Rosa', 'Vargas', '76789012', '987654326', 'rosa.vargas@empresa.com', 'Jr. Tacna 987', '1993-04-30', '2021-11-01', 'ACTIVO', 12),
('EMP007', 'Jorge', 'Castro', '77890123', '987654327', 'jorge.castro@empresa.com', 'Av. Ica 147', '1991-07-18', '2020-08-10', 'ACTIVO', 18),
('EMP008', 'Diana', 'Rios', '78901234', '987654328', 'diana.rios@empresa.com', 'Calle Loreto 258', '1996-09-12', '2023-02-01', 'ACTIVO', 0),
('EMP009', 'Pedro', 'Mendoza', '79012345', '987654329', 'pedro.mendoza@empresa.com', 'Jr. Puno 369', '1989-10-22', '2017-05-15', 'ACTIVO', 30),
('EMP010', 'Lucia', 'Silva', '70123456', '987654330', 'lucia.silva@empresa.com', 'Av. Junin 753', '1994-03-08', '2022-07-20', 'ACTIVO', 8);
GO

-- 2. Contrato
INSERT INTO dbo.Contrato (empleado_id, cargo, area, tipo_contrato, fecha_inicio, fecha_fin, sueldo_base, sistema_pension, estado) VALUES
(1, 'Gerente de TI', 'Sistemas', 'INDEFINIDO', '2020-01-10', NULL, 8000.00, 'AFP Integra', 'ACTIVO'),
(2, 'Analista Contable', 'Contabilidad', 'PLAZO FIJO', '2019-03-15', '2024-03-15', 3500.00, 'ONP', 'ACTIVO'),
(3, 'Desarrollador Senior', 'Sistemas', 'INDEFINIDO', '2021-06-01', NULL, 6000.00, 'AFP Prima', 'ACTIVO'),
(4, 'Asistente de RRHH', 'Recursos Humanos', 'PLAZO FIJO', '2022-01-15', '2023-01-15', 2000.00, 'AFP Habitat', 'INACTIVO'),
(5, 'Jefe de Ventas', 'Comercial', 'INDEFINIDO', '2018-09-20', NULL, 7000.00, 'AFP Profuturo', 'ACTIVO'),
(6, 'Diseñador Gráfico', 'Marketing', 'PLAZO FIJO', '2021-11-01', '2024-11-01', 3000.00, 'AFP Integra', 'ACTIVO'),
(7, 'Soporte Técnico', 'Sistemas', 'INDEFINIDO', '2020-08-10', NULL, 2500.00, 'ONP', 'ACTIVO'),
(8, 'Practicante Legal', 'Legal', 'PRACTICAS', '2023-02-01', '2024-02-01', 1025.00, 'NO APLICA', 'ACTIVO'),
(9, 'Contador General', 'Contabilidad', 'INDEFINIDO', '2017-05-15', NULL, 7500.00, 'AFP Prima', 'ACTIVO'),
(10, 'Ejecutivo de Ventas', 'Comercial', 'PLAZO FIJO', '2022-07-20', '2023-07-20', 2500.00, 'AFP Habitat', 'INACTIVO');
GO

-- 3. DocumentoEmpleado
INSERT INTO dbo.DocumentoEmpleado (empleado_id, nombre_archivo, tipo_archivo, archivo_url) VALUES
(1, 'DNI_Juan_Perez.pdf', 'DNI', 'https://storage.empresa.com/docs/dni_juan.pdf'),
(2, 'CV_Maria_Gomez.pdf', 'CV', 'https://storage.empresa.com/docs/cv_maria.pdf'),
(3, 'Contrato_Carlos.pdf', 'CONTRATO', 'https://storage.empresa.com/docs/contrato_carlos.pdf'),
(4, 'Antecedentes_Ana.pdf', 'ANTECEDENTES', 'https://storage.empresa.com/docs/antecedentes_ana.pdf'),
(5, 'DNI_Luis_Flores.pdf', 'DNI', 'https://storage.empresa.com/docs/dni_luis.pdf'),
(6, 'Portafolio_Rosa.pdf', 'OTROS', 'https://storage.empresa.com/docs/portafolio_rosa.pdf'),
(7, 'Certificado_Jorge.pdf', 'CERTIFICADO', 'https://storage.empresa.com/docs/cert_jorge.pdf'),
(8, 'Constancia_Diana.pdf', 'CONSTANCIA', 'https://storage.empresa.com/docs/const_diana.pdf'),
(9, 'Titulo_Pedro.pdf', 'TITULO', 'https://storage.empresa.com/docs/titulo_pedro.pdf'),
(10, 'DNI_Lucia_Silva.pdf', 'DNI', 'https://storage.empresa.com/docs/dni_lucia.pdf');
GO

-- 4. Usuario
INSERT INTO dbo.Usuario (empleado_id, rol, auth_uid, username, email) VALUES
(1, 'ADMIN', 'uid_001', 'jperez', 'juan.perez@empresa.com'),
(2, 'USER', 'uid_002', 'mgomez', 'maria.gomez@empresa.com'),
(3, 'USER', 'uid_003', 'cruiz', 'carlos.ruiz@empresa.com'),
(4, 'HR', 'uid_004', 'atorres', 'ana.torres@empresa.com'),
(5, 'MANAGER', 'uid_005', 'lflores', 'luis.flores@empresa.com'),
(6, 'USER', 'uid_006', 'rvargas', 'rosa.vargas@empresa.com'),
(7, 'IT', 'uid_007', 'jcastro', 'jorge.castro@empresa.com'),
(8, 'USER', 'uid_008', 'drios', 'diana.rios@empresa.com'),
(9, 'FINANCE', 'uid_009', 'pmendoza', 'pedro.mendoza@empresa.com'),
(10, 'USER', 'uid_010', 'lsilva', 'lucia.silva@empresa.com');
GO

-- 5. Vacacion
INSERT INTO dbo.Vacacion (empleado_id, fecha_solicitud, fecha_inicio, fecha_fin, dias_solicitados, estado, observacion, revisado_por) VALUES
(1, '2023-01-10', '2023-02-01', '2023-02-15', 15, 'APROBADO', 'Vacaciones anuales', 1),
(2, '2023-03-05', '2023-04-10', '2023-04-14', 5, 'APROBADO', 'Descanso medico', 4),
(3, '2023-06-20', '2023-07-01', '2023-07-10', 10, 'PENDIENTE', 'Viaje familiar', NULL),
(5, '2023-08-15', '2023-09-01', '2023-09-07', 7, 'RECHAZADO', 'Cierre de mes', 1),
(6, '2023-09-10', '2023-10-01', '2023-10-15', 15, 'APROBADO', 'Matrimonio', 4),
(7, '2023-10-05', '2023-11-01', '2023-11-05', 5, 'PENDIENTE', 'Estudios', NULL),
(9, '2023-11-20', '2023-12-20', '2024-01-03', 15, 'APROBADO', 'Fin de año', 1),
(10, '2023-02-15', '2023-03-01', '2023-03-07', 7, 'APROBADO', 'Descanso', 4),
(1, '2023-07-10', '2023-08-01', '2023-08-05', 5, 'APROBADO', 'Viaje corto', 1),
(2, '2023-09-01', '2023-10-01', '2023-10-05', 5, 'PENDIENTE', 'Temas personales', NULL);
GO

-- 6. Asistencia
INSERT INTO dbo.Asistencia (empleado_id, fecha, hora_entrada, hora_salida, estado, minutos_tardanza, motivo_justificacion) VALUES
(1, '2023-10-02', '2023-10-02 08:00:00', '2023-10-02 17:00:00', 'PRESENTE', 0, NULL),
(2, '2023-10-02', '2023-10-02 08:15:00', '2023-10-02 17:00:00', 'TARDANZA', 15, 'Trafico'),
(3, '2023-10-02', '2023-10-02 08:05:00', '2023-10-02 17:15:00', 'PRESENTE', 5, NULL),
(4, '2023-10-02', NULL, NULL, 'FALTA', 0, 'Enfermedad'),
(5, '2023-10-02', '2023-10-02 08:00:00', '2023-10-02 18:00:00', 'PRESENTE', 0, NULL),
(6, '2023-10-02', '2023-10-02 08:30:00', '2023-10-02 17:00:00', 'TARDANZA', 30, 'Cita medica en la mañana'),
(7, '2023-10-02', '2023-10-02 08:00:00', '2023-10-02 17:00:00', 'PRESENTE', 0, NULL),
(8, '2023-10-02', '2023-10-02 09:00:00', '2023-10-02 16:00:00', 'PRESENTE', 0, NULL),
(9, '2023-10-02', '2023-10-02 08:00:00', '2023-10-02 17:30:00', 'PRESENTE', 0, NULL),
(10, '2023-10-02', '2023-10-02 08:10:00', '2023-10-02 17:00:00', 'TARDANZA', 10, NULL);
GO

-- 7. Beneficio
INSERT INTO dbo.Beneficio (nombre, descripcion, monto, estado) VALUES
('Bono de Alimentación', 'Bono mensual para gastos de comida', 200.00, 'ACTIVO'),
('Vale de Despensa', 'Vales para compras en supermercados', 150.00, 'ACTIVO'),
('Bono de Movilidad', 'Subsidio de transporte', 100.00, 'ACTIVO'),
('Seguro Vida Ley', 'Seguro de vida obligatorio', NULL, 'ACTIVO'),
('Bono de Productividad', 'Bono por metas cumplidas', 500.00, 'ACTIVO'),
('Bono Escolaridad', 'Apoyo para útiles escolares', 300.00, 'ACTIVO'),
('EPS', 'Seguro de salud privado', NULL, 'ACTIVO'),
('Bono Nocturno', 'Adicional por trabajo en turno noche', 150.00, 'ACTIVO'),
('Gimnasio', 'Suscripción mensual a gimnasio afiliado', 80.00, 'ACTIVO'),
('Bono Aniversario', 'Bono otorgado por aniversario de la empresa', 250.00, 'ACTIVO');
GO

-- 8. EmpleadoBeneficio
INSERT INTO dbo.EmpleadoBeneficio (empleado_id, beneficio_id, fecha_asignacion, observacion) VALUES
(1, 1, '2020-01-15', 'Asignación regular'),
(1, 3, '2020-01-15', 'Asignación regular'),
(2, 2, '2019-03-20', 'Acuerdo de contrato'),
(3, 5, '2021-06-05', 'Meta 2021'),
(4, 3, '2022-01-20', 'Sede lejana'),
(5, 1, '2018-09-25', 'Asignación regular'),
(5, 7, '2018-09-25', 'EPS plan base'),
(6, 6, '2022-02-01', 'Hijo en etapa escolar'),
(7, 4, '2020-08-15', 'Por ley'),
(9, 1, '2017-05-20', 'Asignación regular');
GO

-- 9. Concepto
INSERT INTO dbo.Concepto (nombre, tipo, afecta_base, activo) VALUES
('Sueldo Básico', 'INGRESO', 1, 1),
('Horas Extras 25%', 'INGRESO', 1, 1),
('Horas Extras 35%', 'INGRESO', 1, 1),
('Bono Productividad', 'INGRESO', 1, 1),
('Asignación Familiar', 'INGRESO', 1, 1),
('Aporte AFP', 'DESCUENTO', 0, 1),
('Aporte ONP', 'DESCUENTO', 0, 1),
('Impuesto a la Renta', 'DESCUENTO', 0, 1),
('Tardanzas', 'DESCUENTO', 0, 1),
('Faltas Injustificadas', 'DESCUENTO', 0, 1);
GO

-- 10. Planilla
INSERT INTO dbo.Planilla (empleado_id, contrato_id, periodo, estado) VALUES
(1, 1, '2023-10', 'PAGADA'),
(2, 2, '2023-10', 'PAGADA'),
(3, 3, '2023-10', 'GENERADA'),
(4, 4, '2022-12', 'PAGADA'),
(5, 5, '2023-10', 'GENERADA'),
(6, 6, '2023-10', 'EN_PROCESO'),
(7, 7, '2023-10', 'GENERADA'),
(8, 8, '2023-10', 'PAGADA'),
(9, 9, '2023-10', 'GENERADA'),
(10, 10, '2023-06', 'PAGADA');
GO

-- 11. PlanillaDetalle
INSERT INTO dbo.PlanillaDetalle (planilla_id, concepto_id, monto, cantidad, descripcion) VALUES
(1, 1, 8000.00, 30, 'Sueldo Básico'),
(1, 6, -1040.00, NULL, 'Descuento AFP'),
(2, 1, 3500.00, 30, 'Sueldo Básico'),
(2, 7, -455.00, NULL, 'Descuento ONP'),
(2, 9, -15.00, 1, 'Descuento por tardanzas'),
(3, 1, 6000.00, 30, 'Sueldo Básico'),
(3, 4, 500.00, NULL, 'Bono Productividad'),
(4, 1, 2000.00, 30, 'Sueldo Básico'),
(4, 10, -66.67, 1, 'Descuento por falta'),
(5, 1, 7000.00, 30, 'Sueldo Básico');
GO

-- 12. PagoPlanilla
INSERT INTO dbo.PagoPlanilla (planilla_id, fecha_pago, monto_pagado, medio_pago, referencia) VALUES
(1, '2023-10-31', 6960.00, 'Transferencia BCP', 'OPE-001234'),
(2, '2023-10-31', 3030.00, 'Transferencia BCP', 'OPE-001235'),
(4, '2022-12-31', 1933.33, 'Cheque', 'CHQ-987654'),
(8, '2023-10-31', 1025.00, 'Efectivo', 'REC-001'),
(10, '2023-06-30', 2500.00, 'Transferencia BBVA', 'OPE-009876'),
(1, '2023-09-30', 6960.00, 'Transferencia BCP', 'OPE-001111'),
(2, '2023-09-30', 3045.00, 'Transferencia BCP', 'OPE-001112'),
(4, '2022-11-30', 2000.00, 'Transferencia BCP', 'OPE-000999'),
(8, '2023-09-30', 1025.00, 'Efectivo', 'REC-002'),
(10, '2023-05-31', 2500.00, 'Transferencia BBVA', 'OPE-009800');
GO