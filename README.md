# 📁 Software de Recursos Humanos

## 📌 Descripción

Este proyecto fue desarrollado de manera colaborativa utilizando Git para el control de versiones.
Cada integrante del equipo trabajó en un caso específico dentro del repositorio, organizando el desarrollo mediante ramas y carpetas independientes.

---

## 👥 Integrantes del equipo

* Josseph David Bellido Quispe
  Código: N00452940

* Cristhian Vega Carhuamaca
  Código: N00531783

* Jaqueline Ramirez Lopez
  Codigo: N00531783

* Raydberg Gabriel Chuquival Gil
  Código: N00535385

* Jhon Wilson Rodriguez Quezada
  Código: N00535457

---

## 🛠️ Comandos de Git utilizados

Durante el desarrollo del proyecto se utilizaron los siguientes comandos:

### 🔹 Clonar repositorio

Se utilizó para copiar el repositorio remoto al entorno local:

```bash
https://github.com/JhonRodriguez31/Proyecto_RH.git
```

---

### 🔹 Crear ramas

Cada integrante creó su propia rama para trabajar de forma independiente:

```bash
git checkout -b nombre-rama
```

# Arquitectura del Proyecto

## Enfoque general

Este proyecto se desarrollará como una
*
*aplicación
de
escritorio
en
JavaFX
**, organizada de forma
*
*modular
por
funcionalidad
**, con el objetivo de aprender y aplicar correctamente los principios de la
*
*Programación
Orientada
a
Objetos (
POO)
**.

La arquitectura propuesta combina:

-
*
*MVC
** para la interfaz gráfica
-
*
*Service
** para la lógica de negocio
-
*
*DAO
** para el acceso a base de datos

De esta manera, se evita mezclar la interfaz, la lógica y la persistencia en una sola clase.

---

## Patrón arquitectónico usado

La estructura general del sistema será:

```text
View -> Controller -> Service -> DAO -> Base de Datos
```

### Componentes

#### Model

Representa las entidades del negocio.

Ejemplos:

-
`Employee`
-
`User`
-
`Attendance`
-
`Payroll`
-
`VacationRequest`

#### View

Corresponde a las vistas de la aplicación:

- archivos
  `.fxml`
- estilos
  `.css`

#### Controller

Controla los eventos de la interfaz y comunica la vista con la lógica del sistema.

Ejemplos:

-
`LoginController`
-
`EmployeeFormController`
-
`AttendanceController`

#### Service

Contiene la lógica de negocio del sistema.

Ejemplos:

- validar reglas
- calcular sueldos
- aprobar vacaciones
- detectar tardanzas
- registrar accesos

#### DAO

Se encarga del acceso a la base de datos mediante JDBC.

Ejemplos:

- guardar empleados
- buscar usuarios
- listar asistencias
- actualizar planillas

---

### Beneficios

- mejor organización del código
- mayor mantenibilidad
- mayor reutilización
- separación clara de responsabilidades
- facilita aprender encapsulación, abstracción e interfaces

---

## Organización modular

El sistema estará dividido por módulos funcionales.

### Módulos principales

-
`auth`
-
`employee`
-
`attendance`
-
`payroll`
-
`vacation`
-
`benefit`
-
`document`
-
`report`
-
`dashboard`
-
`common`
-
`config`

Esta organización permite que cada módulo tenga sus propias clases, reglas y acceso a datos.

---

# Estructura del Proyecto

## Estructura recomendada

```text
src/
└── main/
    ├── java/
    │   └── com/project/projectpoo/
    │       ├── App.java
    │       ├── config/
    │       │   ├── DatabaseConfig.java
    │       │   ├── AppConfig.java
    │       │   └── SecurityConfig.java
    │       │
    │       ├── common/
    │       │   ├── exception/
    │       │   ├── util/
    │       │   ├── enums/
    │       │   └── base/
    │       │
    │       ├── auth/
    │       │   ├── model/
    │       │   ├── dto/
    │       │   ├── dao/
    │       │   ├── service/
    │       │   ├── controller/
    │       │   └── validator/
    │       │
    │       ├── employee/
    │       │   ├── model/
    │       │   ├── dto/
    │       │   ├── dao/
    │       │   ├── service/
    │       │   ├── controller/
    │       │   └── validator/
    │       │
    │       ├── attendance/
    │       │   ├── model/
    │       │   ├── dto/
    │       │   ├── dao/
    │       │   ├── service/
    │       │   ├── controller/
    │       │   └── validator/
    │       │
    │       ├── payroll/
    │       │   ├── model/
    │       │   ├── dto/
    │       │   ├── dao/
    │       │   ├── service/
    │       │   ├── controller/
    │       │   └── validator/
    │       │
    │       ├── vacation/
    │       │   ├── model/
    │       │   ├── dto/
    │       │   ├── dao/
    │       │   ├── service/
    │       │   ├── controller/
    │       │   └── validator/
    │       │
    │       ├── benefit/
    │       │   ├── model/
    │       │   ├── dao/
    │       │   ├── service/
    │       │   ├── controller/
    │       │   └── validator/
    │       │
    │       ├── document/
    │       │   ├── model/
    │       │   ├── dao/
    │       │   ├── service/
    │       │   ├── controller/
    │       │   └── validator/
    │       │
    │       ├── report/
    │       │   ├── model/
    │       │   ├── service/
    │       │   └── controller/
    │       │
    │       └── dashboard/
    │           ├── controller/
    │           └── service/
    │
    └── resources/
        └── com/project/projectpoo/
            ├── css/
            ├── fxml/
            │   ├── auth/
            │   ├── employee/
            │   ├── attendance/
            │   ├── payroll/
            │   ├── vacation/
            │   ├── benefit/
            │   ├── document/
            │   ├── report/
            │   └── dashboard/
            ├── images/
            └── sql/
```

---

# Estructura interna de cada módulo

Cada módulo seguirá una estructura común:

```text
modulo/
├── model/
├── dto/
├── dao/
├── service/
├── controller/
└── validator/
```

## Responsabilidad de cada carpeta

###
`model/`

Contiene las entidades del negocio.

Ejemplos:

-
`Employee`
-
`User`
-
`Attendance`
-
`Payroll`

###
`dto/`

Contiene objetos para transferencia de datos entre capas.

Ejemplos:

-
`LoginRequestDto`
-
`EmployeeCreateDto`
-
`VacationRequestDto`

###
`dao/`

Contiene interfaces y clases encargadas del acceso a base de datos.

Ejemplos:

-
`EmployeeDao`
-
`EmployeeDaoImpl`

###
`service/`

Contiene la lógica de negocio del módulo.

Ejemplos:

-
`EmployeeService`
-
`AuthService`
-
`PayrollService`

###
`controller/`

Contiene los controladores JavaFX asociados a las vistas.

Ejemplos:

-
`LoginController`
-
`EmployeeListController`

###
`validator/`

Contiene validaciones específicas del módulo.

Ejemplos:

-
`EmployeeValidator`
-
`AuthValidator`

---

# Ejemplo de interfaz DAO

```java
public interface EmployeeDao {
    void save(Employee employee);

    void update(Employee employee);

    Employee findById(int id);

    Employee findByDni(String dni);

    List<Employee> findAllActive();

    void deactivate(int id);
}
```

## Implementación

```java
public class EmployeeDaoImpl implements EmployeeDao {
    // Implementación con JDBC
}
```

---

# Variables de entorno

## Configuracion

```java
public class EnvConfig {

    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMalformed()
            .ignoreIfMissing()
            .load();

    public static String get(String key) {
        String systemValue = System.getenv(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }
        return dotenv.get(key);
    }
}
```

### Ejemplo de uso

```java
String host = EnvConfig.get("DB_HOST");
String port = EnvConfig.get("DB_PORT");
String db = EnvConfig.get("DB_NAME");
String user = EnvConfig.get("DB_USER");
String pass = EnvConfig.get("DB_PASSWORD");
```

# Recomendaciones de diseño

## Buenas prácticas

- mantener los
  `Controller` lo más limpios posible
- poner la lógica de negocio en
  `Service`
- poner el acceso a base de datos en
  `DAO`
- usar
  `model` solo para representar entidades
- usar
  `validator` para validar entradas y reglas simples
- usar
  `dto` cuando sea necesario transferir datos entre capas

## Evitar

- escribir SQL dentro del
  `Controller`
- calcular sueldos dentro del
  `DAO`
- generar PDF dentro del
  `model`
- poner toda la lógica dentro del
  `Controller`
- mezclar responsabilidades en una sola clase

---

# Tecnologías sugeridas

-
*
*Java
21
**
-
*
*JavaFX
**
-
*
*FXML
**
-
*
*CSS
**
-
*
*Maven
**
-
*
*JDBC
**
-
*
*MySQL
**

---
