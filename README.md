# Gestión de Inmuebles — Arquitectura Hexagonal

Aplicación de escritorio en **JavaFX** para gestionar **inmuebles** (casas y apartamentos) sobre **PostgreSQL**, construida con **arquitectura hexagonal (puertos y adaptadores)**.

Permite crear, consultar, actualizar y eliminar inmuebles. La lógica de negocio no depende de la interfaz gráfica ni de la base de datos.

**Autores:** _(completar)_
**Asignatura:** Ingeniería de Software 2 — Politécnico Grancolombiano

---

## 1. Alcance

- **CRUD solo sobre inmuebles:** crear, modificar, eliminar, buscar por CHIP y listar.
- **Dos tipos de inmueble:** `Casa` (cantidad de pisos y sótano opcional) y `Apartamento` (número de piso). Un inmueble puede cambiar de tipo al modificarlo.
- **Propietarios predeterminados:** vienen cargados en la base de datos y solo se consultan. Se puede filtrar la tabla para ver **todos los inmuebles de un propietario**.
- **Un patrón de diseño:** **Singleton**, aplicado a la conexión con la base de datos (`ConexionBD`).

### Modelo de dominio

| Entidad | Atributos |
|---|---|
| `Inmueble` *(abstracta)* | chip, fechaCompra, direccion, estado, propietario |
| `Apartamento` → Inmueble | numeroPiso |
| `Casa` → Inmueble | cantidadPisos, sotano *(0..1)* |
| `Sotano` | areaM2, tipoUso |
| `Propietario` | identificacion, nombre, correoElectronico |

- Un `Propietario` tiene **0..\*** inmuebles; cada `Inmueble` tiene **1** propietario.
- `Casa` ◆— `Sotano`: **composición 1 a 0..1** (el sótano se borra con la casa).
- Estados válidos: `DISPONIBLE`, `VENDIDO`, `ARRENDADO`, `EN_MANTENIMIENTO`.
- Usos de sótano válidos: `PARQUEADERO`, `BODEGA`, `VIVIENDA`, `OTRO`.

---

## 2. Arquitectura hexagonal

| Capa | Paquete | Contenido |
|---|---|---|
| **Dominio** | `dominio.modelo` | `Inmueble`, `Casa`, `Apartamento`, `Sotano`, `Propietario` |
| | `dominio.excepcion` | `InmuebleException` y sus hijas |
| **Aplicación** | `aplicacion.puerto.entrada` | Casos de uso (interfaces) y `DatosInmueble` |
| | `aplicacion.puerto.salida` | `InmuebleRepository`, `PropietarioRepository` |
| | `aplicacion.servicio` | Servicios que implementan los casos de uso, `ValidadorInmueble`, `FabricaInmueble` |
| **Infraestructura** | `infraestructura.ui` | `ControlInmueble` — adaptador de entrada JavaFX |
| | `infraestructura.persistencia` | `PostgresInmuebleRepository`, `PostgresPropietarioRepository`, `ConexionBD`, `CargadorEnv` — adaptadores de salida |
| | `infraestructura.web` | `App` — punto de entrada y *wiring* |

Reglas que cumple el código:

- El **dominio** no importa nada de las otras capas.
- La **aplicación** solo conoce el dominio y sus propios puertos (no sabe que existe JDBC ni JavaFX).
- `ControlInmueble` recibe **interfaces** (`CrearInmuebleUseCase`, …), nunca los servicios concretos.
- `App` es el único lugar donde se crean las clases concretas y se conectan entre sí.

📐 Diagramas: [arquitectura hexagonal](docs/diagramas/arquitectura-hexagonal.jpeg) · [modelo de clases UML](docs/diagramas/modelo-clases-uml.jpeg) · y en la **[Wiki](../../wiki)**.

---

## 3. Patrón Singleton: `ConexionBD`

`ConexionBD` garantiza una única instancia encargada de la configuración de conexión, usando *double-checked locking*:

- La clase es `final`, para que no se pueda extender.
- El constructor es `private`, así que nadie puede hacer `new ConexionBD()`.
- El campo `instancia` es `static volatile`, lo que lo hace seguro entre hilos.
- `getInstancia()` es el único punto de acceso.

Cada llamada a `obtenerConexion()` abre una conexión JDBC nueva, que quien la usa debe cerrar (los repositorios lo hacen con `try-with-resources` o en un `finally`).

```java
try (Connection cn = ConexionBD.getInstancia().obtenerConexion()) { ... }
```

### Orden de lectura de credenciales

1. Variables de entorno del sistema operativo (`DB_URL`, `DB_USER`, `DB_PASSWORD`).
2. Archivo `.env` en la raíz del proyecto, leído por `CargadorEnv`.
3. Archivo `db.properties` en el classpath (opcional).

Si ninguna fuente aporta la URL, se usa `jdbc:postgresql://localhost:5432/inmueblesdb`.

---

## 4. Tecnologías

| Componente | Versión |
|---|---|
| Java | 11 o superior |
| JavaFX (controls + fxml) | 17.0.9 |
| Maven / javafx-maven-plugin | 3.9 / 0.0.8 |
| PostgreSQL | 14 o superior |
| PostgreSQL JDBC | 42.7.3 |
| JUnit | 5.10.2 |
| IDE | Eclipse o NetBeans (con Git integrado) |

---

## 5. Base de datos

### 5.1 Modelo relacional

La herencia se guarda con **una tabla por clase**: `inmueble` tiene los datos comunes, y `casa` / `apartamento` los propios, unidas por el `chip`. El sótano va en su propia tabla y se borra en cascada con la casa.

```
propietario (identificacion PK, nombre, correo_electronico)
      │ 1
      │ 0..*
inmueble (chip PK, fecha_compra, direccion, estado, tipo, propietario_id FK)
      │                               │
apartamento (chip PK/FK,          casa (chip PK/FK, cantidad_pisos)
             numero_piso)              │ 1
                                       │ 0..1
                                  sotano (casa_chip PK/FK, area_m2, tipo_uso)
```

### 5.2 Crear la base de datos

En **pgAdmin**: clic derecho en *Databases → Create → Database…*, nombre `inmueblesdb`.
O en **psql**:

```sql
CREATE DATABASE inmueblesdb;
```

Luego abre el *Query Tool* sobre `inmueblesdb` (o `psql -U postgres -d inmueblesdb`) y ejecuta los siguientes scripts.

### 5.3 Crear las tablas

```sql
-- Propietarios (predeterminados, sin CRUD)
CREATE TABLE propietario (
    identificacion      VARCHAR(20)  PRIMARY KEY,
    nombre              VARCHAR(100) NOT NULL,
    correo_electronico  VARCHAR(120) NOT NULL UNIQUE
);

-- Datos comunes de todo inmueble (tabla padre)
CREATE TABLE inmueble (
    chip            VARCHAR(20)  PRIMARY KEY,
    fecha_compra    DATE         NOT NULL CHECK (fecha_compra <= CURRENT_DATE),
    direccion       VARCHAR(150) NOT NULL,
    estado          VARCHAR(20)  NOT NULL
                    CHECK (estado IN ('DISPONIBLE', 'VENDIDO', 'ARRENDADO', 'EN_MANTENIMIENTO')),
    tipo            VARCHAR(15)  NOT NULL CHECK (tipo IN ('CASA', 'APARTAMENTO')),
    propietario_id  VARCHAR(20)  NOT NULL REFERENCES propietario (identificacion)
);

-- Subclase Apartamento
CREATE TABLE apartamento (
    chip         VARCHAR(20) PRIMARY KEY REFERENCES inmueble (chip) ON DELETE CASCADE,
    numero_piso  INTEGER     NOT NULL CHECK (numero_piso >= 1)
);

-- Subclase Casa
CREATE TABLE casa (
    chip            VARCHAR(20) PRIMARY KEY REFERENCES inmueble (chip) ON DELETE CASCADE,
    cantidad_pisos  INTEGER     NOT NULL CHECK (cantidad_pisos >= 1)
);

-- Sotano: composicion 1 a 0..1 con Casa
CREATE TABLE sotano (
    casa_chip  VARCHAR(20)   PRIMARY KEY REFERENCES casa (chip) ON DELETE CASCADE,
    area_m2    NUMERIC(10,2) NOT NULL CHECK (area_m2 > 0),
    tipo_uso   VARCHAR(20)   NOT NULL
               CHECK (tipo_uso IN ('PARQUEADERO', 'BODEGA', 'VIVIENDA', 'OTRO'))
);

CREATE INDEX idx_inmueble_propietario ON inmueble (propietario_id);
```

### 5.4 Datos iniciales

Los propietarios son obligatorios (la app no los crea). Los inmuebles son de ejemplo; **Laura Gómez tiene tres** para demostrar el caso de un propietario con varios inmuebles.

```sql
INSERT INTO propietario (identificacion, nombre, correo_electronico) VALUES
('1010101010', 'Laura Gómez',      'laura.gomez@correo.com'),
('2020202020', 'Carlos Rodríguez', 'carlos.rodriguez@correo.com'),
('3030303030', 'Ana Martínez',     'ana.martinez@correo.com'),
('4040404040', 'Jorge Ramírez',    'jorge.ramirez@correo.com'),
('5050505050', 'Sofía Herrera',    'sofia.herrera@correo.com');

INSERT INTO inmueble (chip, fecha_compra, direccion, estado, tipo, propietario_id) VALUES
('AAA0001AAAA', '2018-03-15', 'Calle 100 # 15-20 Apto 502', 'ARRENDADO',  'APARTAMENTO', '1010101010'),
('AAA0002BBBB', '2020-07-01', 'Carrera 7 # 45-10',          'DISPONIBLE', 'CASA',        '1010101010'),
('AAA0003CCCC', '2022-11-20', 'Calle 26 # 68-35 Apto 1203', 'DISPONIBLE', 'APARTAMENTO', '1010101010'),
('AAA0004DDDD', '2015-05-09', 'Transversal 60 # 120-15',    'VENDIDO',    'CASA',        '2020202020');

INSERT INTO apartamento (chip, numero_piso) VALUES
('AAA0001AAAA', 5),
('AAA0003CCCC', 12);

INSERT INTO casa (chip, cantidad_pisos) VALUES
('AAA0002BBBB', 2),
('AAA0004DDDD', 3);

INSERT INTO sotano (casa_chip, area_m2, tipo_uso) VALUES
('AAA0002BBBB', 45.50, 'PARQUEADERO');
```

### 5.5 Verificar

```sql
SELECT i.chip, i.tipo, i.direccion, p.nombre AS propietario,
       a.numero_piso, c.cantidad_pisos, s.area_m2, s.tipo_uso
FROM inmueble i
JOIN propietario p      ON p.identificacion = i.propietario_id
LEFT JOIN apartamento a ON a.chip = i.chip
LEFT JOIN casa c        ON c.chip = i.chip
LEFT JOIN sotano s      ON s.casa_chip = i.chip
ORDER BY p.nombre, i.chip;
```

Deben salir 4 filas: 3 de Laura Gómez y 1 de Carlos Rodríguez.

### 5.6 Cómo usa el código la base de datos

- **Crear** y **modificar** escriben en varias tablas dentro de **una transacción** (si algo falla se hace *rollback*).
- **Modificar** actualiza `inmueble`, borra el detalle anterior (`casa`/`apartamento`; el sótano cae en cascada) y lo vuelve a insertar. Por eso un inmueble puede cambiar de tipo o ganar/perder sótano.
- **Eliminar** borra la fila de `inmueble`; las tablas hijas se limpian por `ON DELETE CASCADE`.
- Los errores de PostgreSQL se traducen a excepciones de dominio por **SQLState**: `23505` → CHIP duplicado, `23503` → propietario inexistente, `23514`/`23502` → dato inválido.

---

## 6. Estructura del proyecto

```
gestion-inmuebles/                       ← raíz del repositorio
├── README.md
├── docs/
│   ├── diagramas/
│   │   ├── arquitectura-hexagonal.jpeg
│   │   └── modelo-clases-uml.jpeg
│   └── wiki/                            ← páginas listas para copiar a la Wiki
│       ├── Home.md
│       ├── Arquitectura.md
│       ├── Modelo-de-clases.md
│       └── Video.md
└── ProyectoInmuebles/                   ← proyecto Maven
    ├── .env                             ← tus credenciales (NO se sube)
    ├── .env.example                     ← plantilla pública
    ├── .gitignore
    ├── pom.xml
    └── src/
        ├── main/
        │   ├── java/
        │   │   ├── module-info.java
        │   │   └── co/edu/poli/sw2/ProyectoInmuebles/
        │   │       ├── dominio/
        │   │       │   ├── modelo/
        │   │       │   │   ├── Inmueble.java
        │   │       │   │   ├── Apartamento.java
        │   │       │   │   ├── Casa.java
        │   │       │   │   ├── Sotano.java
        │   │       │   │   └── Propietario.java
        │   │       │   └── excepcion/
        │   │       │       ├── InmuebleException.java
        │   │       │       ├── InmuebleDuplicadoException.java
        │   │       │       ├── InmuebleNoEncontradoException.java
        │   │       │       ├── InmuebleValidacionException.java
        │   │       │       └── ConexionBDException.java
        │   │       ├── aplicacion/
        │   │       │   ├── puerto/
        │   │       │   │   ├── entrada/
        │   │       │   │   │   ├── DatosInmueble.java
        │   │       │   │   │   ├── CrearInmuebleUseCase.java
        │   │       │   │   │   ├── BuscarInmuebleUseCase.java
        │   │       │   │   │   ├── ModificarInmuebleUseCase.java
        │   │       │   │   │   ├── EliminarInmuebleUseCase.java
        │   │       │   │   │   ├── ListarInmueblesUseCase.java
        │   │       │   │   │   └── ListarPropietariosUseCase.java
        │   │       │   │   └── salida/
        │   │       │   │       ├── InmuebleRepository.java
        │   │       │   │       └── PropietarioRepository.java
        │   │       │   └── servicio/
        │   │       │       ├── CrearInmuebleServicio.java
        │   │       │       ├── BuscarInmuebleServicio.java
        │   │       │       ├── ModificarInmuebleServicio.java
        │   │       │       ├── EliminarInmuebleServicio.java
        │   │       │       ├── ListarInmueblesServicio.java
        │   │       │       ├── ListarPropietariosServicio.java
        │   │       │       ├── ValidadorInmueble.java
        │   │       │       └── FabricaInmueble.java
        │   │       └── infraestructura/
        │   │           ├── persistencia/
        │   │           │   ├── CargadorEnv.java
        │   │           │   ├── ConexionBD.java
        │   │           │   ├── PostgresInmuebleRepository.java
        │   │           │   └── PostgresPropietarioRepository.java
        │   │           ├── ui/
        │   │           │   └── ControlInmueble.java
        │   │           └── web/
        │   │               └── App.java
        │   └── resources/
        │       └── co/edu/poli/sw2/vista/
        │           ├── inmueble.fxml
        │           └── styles.css
        └── test/java/co/edu/poli/sw2/ProyectoInmuebles/
            ├── dominio/modelo/ModeloDominioTest.java
            ├── aplicacion/servicio/
            │   ├── RepositorioEnMemoria.java
            │   ├── PropietariosEnMemoria.java
            │   ├── DatosDePrueba.java
            │   ├── ValidadorInmuebleTest.java
            │   ├── CrearInmuebleServicioTest.java
            │   ├── ModificarInmuebleServicioTest.java
            │   ├── EliminarInmuebleServicioTest.java
            │   └── BuscarYListarInmuebleServicioTest.java
            └── infraestructura/persistencia/
                ├── ConexionBDTest.java
                ├── ConexionBDIT.java
                └── PostgresInmuebleRepositoryIT.java
```

> Los recursos (`inmueble.fxml` y `styles.css`) **deben** estar en `src/main/resources`. Si quedan en otra carpeta, Maven no los copia y la aplicación falla al iniciar con `Location is not set`.

---

## 7. Cómo ejecutar el proyecto

### Requisitos

- JDK 11 o superior.
- Maven (o el integrado en Eclipse/NetBeans).
- PostgreSQL corriendo, con la base de datos `inmueblesdb` creada como en la sección 5.

### Pasos

1. **Clonar el repositorio.**
   - Eclipse: *File → Import → Git → Projects from Git → Clone URI*.
   - NetBeans: *Team → Git → Clone*.
2. **Abrir el proyecto Maven** `ProyectoInmuebles`.
   - Eclipse: *File → Import → Maven → Existing Maven Projects*.
   - NetBeans: *File → Open Project*.
3. **Crear tu archivo `.env`** dentro de `ProyectoInmuebles`, copiando `.env.example`:
   ```
   DB_URL=jdbc:postgresql://localhost:5432/inmueblesdb
   DB_USER=postgres
   DB_PASSWORD=tu_password
   ```
   Una variable por línea, sin espacios ni comillas.
4. **Actualizar el proyecto:** Eclipse → clic derecho → *Maven → Update Project* (Alt+F5). NetBeans → *Clean and Build*.
5. **Ejecutar** `infraestructura/web/App.java` (*Run As → Java Application* / *Run File*), o desde consola:

```bash
cd ProyectoInmuebles
mvn clean javafx:run
```

---

## 8. Operaciones CRUD desde la interfaz

| Operación | Caso de uso | En la interfaz |
|---|---|---|
| **Crear** | `CrearInmuebleUseCase` | Llenar CHIP, fecha, dirección, estado, propietario y tipo. Si es **Apartamento** aparece *Número de piso*; si es **Casa** aparece *Cantidad de pisos* y la casilla *Tiene sótano* (área y uso). Clic en **Crear**. |
| **Consultar** | `BuscarInmuebleUseCase` / `ListarInmueblesUseCase` | Escribir el CHIP y clic en **Buscar**, o ver la tabla completa. Al hacer clic en una fila se cargan sus datos. |
| **Actualizar** | `ModificarInmuebleUseCase` | Seleccionar una fila, cambiar los datos (el CHIP queda bloqueado) y clic en **Modificar**. Se puede cambiar el tipo y agregar o quitar el sótano. |
| **Eliminar** | `EliminarInmuebleUseCase` | Seleccionar una fila, clic en **Eliminar** y confirmar. |
| **Propietario con varios inmuebles** | `ListarInmueblesUseCase.listarPorPropietario` | Elegir el propietario sobre la tabla y clic en **Ver sus inmuebles** (con los datos de ejemplo, Laura Gómez tiene 3). **Ver todos** quita el filtro. |

### Reglas de validación (`ValidadorInmueble`)

- CHIP obligatorio, 5–20 letras o números, se guarda en mayúsculas; no se puede modificar.
- Fecha de compra obligatoria y no futura.
- Dirección obligatoria, máximo 150 caracteres.
- Estado, tipo, propietario y uso del sótano deben elegirse de la lista.
- Pisos: entero ≥ 1. Área del sótano: número > 0 (acepta coma decimal).

---

## 9. Javadoc

Todas las clases del dominio, casos de uso, puertos y adaptadores están documentadas.

```bash
cd ProyectoInmuebles
mvn javadoc:javadoc
```

Abrir `target/site/apidocs/index.html`. En Eclipse también: *Project → Generate Javadoc…*

---

## 10. Pruebas

Las pruebas usan **JUnit 5** y están en `src/test/java`, en los mismos paquetes que el código que prueban.

### Pruebas unitarias (no necesitan base de datos)

| Clase de prueba | Qué verifica |
|---|---|
| `ModeloDominioTest` | Herencia, tipo de cada subclase, sótano opcional, igualdad por CHIP / identificación |
| `ValidadorInmuebleTest` | Reglas de CHIP, fecha, dirección, listas de valores, pisos y área (`NaN`, `Infinity`, coma decimal) |
| `CrearInmuebleServicioTest` | Crear casa y apartamento, sótano, normalización, propietario inexistente, CHIP duplicado y que un dato inválido **nunca llegue** al repositorio |
| `ModificarInmuebleServicioTest` | Actualizar datos y propietario, cambiar de tipo, CHIP no modificable, dato inválido no altera el original, inexistente |
| `EliminarInmuebleServicioTest` | Eliminar solo el indicado, sin selección, inexistente |
| `BuscarYListarInmuebleServicioTest` | Buscar por CHIP, orden del listado y **propietario con varios inmuebles** |
| `ConexionBDTest` | Estructura del Singleton: misma instancia, 20 hilos, constructor privado, clase `final`, campo `static volatile`, `toString` sin credenciales |

Los casos de uso se prueban con `RepositorioEnMemoria` y `PropietariosEnMemoria`, implementaciones falsas de los puertos de salida. Así la lógica de aplicación se prueba aislada de PostgreSQL.

### Pruebas de integración (necesitan PostgreSQL y el `.env`)

| Clase de prueba | Qué verifica |
|---|---|
| `ConexionBDIT` | Que la conexión real sea válida e independiente en cada llamada |
| `PostgresInmuebleRepositoryIT` | CRUD real, sótano, cambio de tipo, cascada al eliminar, duplicado con *rollback*, propietario inexistente |

Crean inmuebles con CHIP `TESTxxxxxxxx` y los **borran al terminar**.

```bash
mvn test              # solo unitarias (*Test)
mvn test -Dtest=*IT   # integración
```

---

## 11. Seguridad de credenciales

- `.env` está en `.gitignore` y **nunca se sube**. Cada integrante crea el suyo desde `.env.example`.
- `db.properties` también está ignorado.
- `ConexionBD.toString()` devuelve un texto fijo, así las credenciales no se filtran en un `println`.

---

## 12. Solución de problemas

| Síntoma | Causa probable | Solución |
|---|---|---|
| `Location is not set` al iniciar | `inmueble.fxml` no está en `src/main/resources` | Moverlo y hacer *Maven → Update Project* |
| `Faltan las credenciales de la base de datos` | No existe `.env` o le faltan claves | Crear `.env` desde `.env.example` |
| `password authentication failed` | Usuario o clave incorrectos | Revisar el `.env` |
| `Connection refused` | PostgreSQL apagado | Iniciar el servicio `postgresql-x64-XX` en `services.msc` |
| `database "inmueblesdb" does not exist` | La BD no se creó o tiene otro nombre | Sección 5.2, o corregir `DB_URL` |
| La lista de propietarios sale vacía | No se ejecutó el script 5.4 | Insertar los propietarios |
| No se ve `.env` en Eclipse | Eclipse oculta los archivos que empiezan con punto | *Package Explorer → ⋮ → Filters* → desmarcar `.* resources` |

---

## 13. Trabajo con Git desde el IDE

**Eclipse (EGit):** *Window → Show View → Git → Git Staging* → revisar que **`.env` no aparezca** → pasar archivos a *Staged Changes* → mensaje → **Commit and Push**.

**NetBeans:** clic derecho en el proyecto → *Git → Commit…* → mensaje → *Commit*; luego *Git → Remote → Push…*

---

## 14. Diagramas y video

- Diagrama de arquitectura hexagonal: [Wiki — Arquitectura](../../wiki/Arquitectura)
- Diagrama de clases UML: [Wiki — Modelo de clases](../../wiki/Modelo-de-clases)
- Video de desarrollo y demostración: [Wiki — Video](../../wiki/Video) · _(pegar enlace de YouTube/Drive)_
