IF NOT EXISTS (SELECT name
               FROM sys.databases
               WHERE
                   name =
                   'PlanillaCoreDB')
    BEGIN
        CREATE DATABASE PlanillaCoreDB;
    END
GO

USE PlanillaCoreDB;
GO

CREATE TABLE dbo.Empleado
(
    id                          INT IDENTITY (1,1) PRIMARY KEY,
    codigo_empleado             NVARCHAR(20)  NOT NULL,
    nombres                     NVARCHAR(120) NOT NULL,
    apellidos                   NVARCHAR(120) NOT NULL,
    dni                         NVARCHAR(20)  NOT NULL,
    telefono                    NVARCHAR(30)  NULL,
    correo                      NVARCHAR(150) NULL,
    direccion                   NVARCHAR(200) NULL,
    fecha_nacimiento            DATE          NULL,
    fecha_ingreso               DATE          NOT NULL,
    foto_url                    NVARCHAR(255) NULL,
    estado                      NVARCHAR(20)  NOT NULL DEFAULT ('ACTIVO'),
    dias_vacaciones_disponibles INT           NOT NULL DEFAULT (0)
);


GO

CREATE TABLE dbo.Contrato
(
    id              INT IDENTITY (1,1) PRIMARY KEY,
    empleado_id     INT            NOT NULL,
    cargo           NVARCHAR(100)  NOT NULL,
    area            NVARCHAR(100)  NULL,
    tipo_contrato   NVARCHAR(30)   NOT NULL,
    fecha_inicio    DATE           NOT NULL,
    fecha_fin       DATE           NULL,
    sueldo_base     DECIMAL(10, 2) NOT NULL,
    sistema_pension NVARCHAR(20)   NULL,
    estado          NVARCHAR(20)   NOT NULL DEFAULT ('ACTIVO'),
    CONSTRAINT FK_Contrato_Empleado FOREIGN KEY (empleado_id) REFERENCES dbo.Empleado (id)
);
GO

CREATE TABLE dbo.DocumentoEmpleado
(
    id             INT IDENTITY (1,1) PRIMARY KEY,
    empleado_id    INT           NOT NULL,
    nombre_archivo NVARCHAR(150) NOT NULL,
    tipo_archivo   NVARCHAR(20)  NOT NULL,
    archivo_url    NVARCHAR(255) NOT NULL,
    fecha_subida   DATETIME2     NOT NULL DEFAULT SYSDATETIME(),
    CONSTRAINT FK_DocumentoEmpleado_Empleado FOREIGN KEY (empleado_id) REFERENCES dbo.Empleado (id)
);
GO

CREATE TABLE dbo.Usuario
(
    id              INT IDENTITY (1,1) PRIMARY KEY,
    empleado_id     INT           NOT NULL,
    rol             NVARCHAR(30)  NOT NULL,
    auth_uid        NVARCHAR(100) NULL,
    username        NVARCHAR(100) NOT NULL,
    email           NVARCHAR(150) NOT NULL,
    bloqueado_hasta DATETIME2     NULL,
    activo          BIT           NOT NULL DEFAULT (1),
    ultimo_acceso   DATETIME2     NULL,
    CONSTRAINT FK_Usuario_Empleado FOREIGN KEY (empleado_id) REFERENCES dbo.Empleado (id)
);
GO

CREATE TABLE dbo.Vacacion
(
    id               INT IDENTITY (1,1) PRIMARY KEY,
    empleado_id      INT           NOT NULL,
    fecha_solicitud  DATE          NOT NULL,
    fecha_inicio     DATE          NOT NULL,
    fecha_fin        DATE          NOT NULL,
    dias_solicitados INT           NOT NULL,
    estado           NVARCHAR(20)  NOT NULL DEFAULT ('PENDIENTE'),
    observacion      NVARCHAR(200) NULL,
    revisado_por     INT           NULL,
    CONSTRAINT FK_Vacacion_Empleado FOREIGN KEY (empleado_id) REFERENCES dbo.Empleado (id),
    CONSTRAINT CHK_Vacacion_Fechas CHECK (fecha_fin >=
                                          fecha_inicio)
);
GO

CREATE TABLE dbo.Asistencia
(
    id                   INT IDENTITY (1,1) PRIMARY KEY,
    empleado_id          INT           NOT NULL,
    fecha                DATE          NOT NULL,
    hora_entrada         DATETIME2     NULL,
    hora_salida          DATETIME2     NULL,
    estado               NVARCHAR(20)  NOT NULL,
    minutos_tardanza     INT           NOT NULL DEFAULT (0),
    motivo_justificacion NVARCHAR(200) NULL,
    corregido_por        INT           NULL,
    observacion          NVARCHAR(200) NULL,
    CONSTRAINT FK_Asistencia_Empleado FOREIGN KEY (empleado_id) REFERENCES dbo.Empleado (id)
);
GO

CREATE TABLE dbo.Beneficio
(
    id          INT IDENTITY (1,1) PRIMARY KEY,
    nombre      NVARCHAR(100)  NOT NULL,
    descripcion NVARCHAR(200)  NULL,
    monto       DECIMAL(10, 2) NULL,
    estado      NVARCHAR(20)   NOT NULL DEFAULT ('ACTIVO')
);
GO

CREATE TABLE dbo.EmpleadoBeneficio
(
    id               INT IDENTITY (1,1) PRIMARY KEY,
    empleado_id      INT           NOT NULL,
    beneficio_id     INT           NOT NULL,
    fecha_asignacion DATE          NOT NULL,
    observacion      NVARCHAR(200) NULL,
    CONSTRAINT FK_EmpleadoBeneficio_Empleado FOREIGN KEY (empleado_id) REFERENCES dbo.Empleado (id),
    CONSTRAINT FK_EmpleadoBeneficio_Beneficio FOREIGN KEY (beneficio_id) REFERENCES dbo.Beneficio (id),
    CONSTRAINT UQ_Empleado_Beneficio UNIQUE (empleado_id,
                                             beneficio_id)
);
GO

CREATE TABLE dbo.Concepto
(
    id          INT IDENTITY (1,1) PRIMARY KEY,
    nombre      NVARCHAR(100) NOT NULL,
    tipo        NVARCHAR(20)  NOT NULL,
    afecta_base BIT           NOT NULL DEFAULT (0),
    activo      BIT           NOT NULL DEFAULT (1),
    CONSTRAINT CHK_Concepto_Tipo CHECK (tipo IN
                                        ('INGRESO',
                                         'DESCUENTO')),
    CONSTRAINT UQ_Concepto_Nombre UNIQUE (nombre)
);
GO

CREATE TABLE dbo.Planilla
(
    id               INT IDENTITY (1,1) PRIMARY KEY,
    empleado_id      INT          NOT NULL,
    contrato_id      INT          NOT NULL,
    periodo          CHAR(7)      NOT NULL,
    fecha_generacion DATETIME2    NOT NULL DEFAULT SYSDATETIME(),
    estado           NVARCHAR(20) NOT NULL DEFAULT ('GENERADA'),
    CONSTRAINT FK_Planilla_Empleado FOREIGN KEY (empleado_id) REFERENCES dbo.Empleado (id),
    CONSTRAINT FK_Planilla_Contrato FOREIGN KEY (contrato_id) REFERENCES dbo.Contrato (id),
    CONSTRAINT UQ_Planilla_Empleado_Periodo UNIQUE (empleado_id,
                                                    periodo)
);
GO

CREATE TABLE dbo.PlanillaDetalle
(
    id            INT IDENTITY (1,1) PRIMARY KEY,
    planilla_id   INT            NOT NULL,
    concepto_id   INT            NOT NULL,
    monto         DECIMAL(10, 2) NOT NULL,
    cantidad      DECIMAL(10, 2) NULL,
    descripcion   NVARCHAR(200)  NULL,
    vacacion_id   INT            NULL,
    asistencia_id INT            NULL,
    CONSTRAINT FK_PlanillaDetalle_Planilla FOREIGN KEY (planilla_id) REFERENCES dbo.Planilla (id),
    CONSTRAINT FK_PlanillaDetalle_Concepto FOREIGN KEY (concepto_id) REFERENCES dbo.Concepto (id),
    CONSTRAINT FK_PlanillaDetalle_Vacacion FOREIGN KEY (vacacion_id) REFERENCES dbo.Vacacion (id),
    CONSTRAINT FK_PlanillaDetalle_Asistencia FOREIGN KEY (asistencia_id) REFERENCES dbo.Asistencia (id),
    CONSTRAINT CHK_PlanillaDetalle_Monto CHECK (monto <>
                                                0)
);
GO

CREATE TABLE dbo.PagoPlanilla
(
    id           INT IDENTITY (1,1) PRIMARY KEY,
    planilla_id  INT            NOT NULL,
    fecha_pago   DATE           NOT NULL,
    monto_pagado DECIMAL(10, 2) NOT NULL,
    medio_pago   NVARCHAR(50)   NULL,
    referencia   NVARCHAR(150)  NULL,
    CONSTRAINT FK_PagoPlanilla_Planilla FOREIGN KEY (planilla_id) REFERENCES dbo.Planilla (id)
);
GO