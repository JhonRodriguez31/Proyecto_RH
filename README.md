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

Este proyecto se desarrollará como una **aplicación de escritorio en JavaFX**, organizada de forma **modular por funcionalidad**, con el objetivo de aprender y aplicar correctamente los principios de la **Programación Orientada a Objetos (POO)**.

La arquitectura propuesta combina:

- **MVC** para la interfaz gráfica
- **Service** para la lógica de negocio
- **DAO** para el acceso a base de datos

De esta manera, se evita mezclar la interfaz, la lógica y la persistencia en una sola clase.

---

## Patrón arquitectónico usado

La estructura general del sistema será:

```text
View -> Controller -> Service -> DAO -> Base de Datos
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

# Tecnologías a usar

- Java 21
- JavaFX
- FXML
- CSS
- Maven
- JDBC
- SQLServer
