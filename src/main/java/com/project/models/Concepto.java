package com.project.models;

public class Concepto {
	
	
	private int id;
	private String nombre;
	private String tipo;
	private int afecta_base;
	private int activo ;
	
	public Concepto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public Concepto(int id, String nombre, String tipo, int afecta_base, int activo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.tipo = tipo;
		this.afecta_base = afecta_base;
		this.activo = activo;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public int getAfecta_base() {
		return afecta_base;
	}


	public void setAfecta_base(int afecta_base) {
		this.afecta_base = afecta_base;
	}


	public int getActivo() {
		return activo;
	}


	public void setActivo(int activo) {
		this.activo = activo;
	}

}


/*CREATE TABLE dbo.Concepto (
id INT IDENTITY(1,1) PRIMARY KEY,
nombre NVARCHAR(100) NOT NULL,
tipo NVARCHAR(20) NOT NULL,
afecta_base BIT NOT NULL DEFAULT(0),
activo BIT NOT NULL DEFAULT(1),
CONSTRAINT CHK_Concepto_Tipo CHECK (tipo IN ('INGRESO','DESCUENTO')),
CONSTRAINT UQ_Concepto_Nombre UNIQUE (nombre)
);*/