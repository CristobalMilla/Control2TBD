# Control2TBD

**Trabajo de Control 2 para Taller de Base de Datos**

Este proyecto se compone de tres partes principales:

1. 🛠️ Población de la Base de Datos
2. 🔧 Backend
3. 🎨 Frontend

---

## ✅ Requisitos Previos

Antes de comenzar, asegúreate de tener instalados:

* ☕ **Java JDK 17** – Para el backend
* 🌐 **Node.js** y **npm** – Para el frontend con Vue
* 🐘 **PostgreSQL** – Motor de base de datos
* 💥 **Windows** – Sistema operativo recomendado

---

## 1️⃣ Población de la Base de Datos

1. Crear una base de datos llamada `control2TBD` en PostgreSQL
2. Ejecutar el contenido del archivo `Sql.txt` (ubicado en la raíz del proyecto)
3. Ejecutar el archivo `PoblarSectoresComunas.sql` (tambien en la raíz)

---

## 2️⃣ Backend

📂 Ruta: `Control2TBD/control2TBDBackEnd`

### 🔧 Configurar variables de entorno

Abre una terminal en la carpeta del backend y ejecuta:

```bash
set DATABASE_URL=jdbc:postgresql://localhost:5432/control2TBD
set DATABASE_USER=tu_usuario
set DATABASE_PASSWORD=tu_contraseña
```

### ▶️ Ejecutar el backend

Usa el Maven Wrapper incluido en el proyecto:

```bash
.\mvnw spring-boot:run
```

En caso de que esto no funcione, simplemente establecer las variables en el `application.properties` del backend y ejecutar con
botón "run" de Intellij o VS Code.

---

## 3️⃣ Frontend

📂 Ruta: `Control2TBD/control2TBDFrontEnd/control2TBDFrontend`

### 🔽️ Instalar dependencias

```bash
npm install
```

### ▶️ Ejecutar servidor de desarrollo

```bash
npm run dev
```

---

