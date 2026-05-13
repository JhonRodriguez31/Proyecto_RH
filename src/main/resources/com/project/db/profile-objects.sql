-- =============================================
-- Vista: vw_PerfilEmpleado
-- Combina Empleado + Usuario + Contrato activo
-- en una sola consulta para la pantalla Mi Perfil
-- =============================================
SELECT * FROM dbo.Empleado;

USE PlanillaCoreDB;
GO
CREATE OR ALTER VIEW dbo.vw_PerfilEmpleado AS
SELECT
    e.id                          AS empleado_id,
    e.codigo_empleado,
    e.nombres,
    e.apellidos,
    e.dni,
    e.telefono,
    e.direccion,
    e.fecha_nacimiento,
    e.fecha_ingreso,
    e.foto_url,
    e.estado                      AS estado_empleado,
    e.dias_vacaciones_disponibles,
    u.id                          AS usuario_id,
    u.username,
    u.email,
    u.rol,
    u.ultimo_acceso,
    c.cargo,
    c.area,
    c.tipo_contrato,
    c.sueldo_base,
    c.fecha_inicio                AS fecha_inicio_contrato,
    c.fecha_fin                   AS fecha_fin_contrato,
    c.sistema_pension
FROM dbo.Empleado e
INNER JOIN dbo.Usuario u ON u.empleado_id = e.id
LEFT JOIN dbo.Contrato c ON c.empleado_id = e.id AND c.estado = 'ACTIVO';
GO

-- =============================================
-- Procedimiento: sp_ActualizarPerfilEmpleado
-- Actualiza SOLO los campos que el empleado
-- puede editar: teléfono, dirección, foto
-- =============================================
CREATE OR ALTER PROCEDURE dbo.sp_ActualizarPerfilEmpleado
    @empleado_id INT,
    @telefono    NVARCHAR(30),
    @direccion   NVARCHAR(200),
    @foto_url    NVARCHAR(255)
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.Empleado
    SET telefono            = @telefono,
        direccion           = @direccion,
        foto_url            = @foto_url,
        fecha_actualizacion = SYSDATETIME()
    WHERE id = @empleado_id;

    IF @@ROWCOUNT = 0
        THROW 50001, 'Empleado no encontrado', 1;
END
GO
