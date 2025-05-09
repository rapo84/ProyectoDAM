# 📱 Proyecto Final de DAM - Gestión de Locales de Comida  

## 📌 Descripción del Proyecto  
Este proyecto es una aplicación móvil desarrollada en **Android Studio con Java**, diseñada para ayudar a gestionar pequeños locales de restauración (hostelería). Su objetivo es proporcionar una herramienta eficiente para la administración de empleados, inventario, mesas y pedidos.  

El desarrollo de la aplicación se realizará en **varias etapas**, incorporando nuevas funcionalidades progresivamente.  

---

## Funcionalidades Principales  

### ✅ Primera Etapa: Gestión Básica de RRHH  
- **Inicio de sesión del Super Usuario**:  
  - Permite configurar la aplicación en el dispositivo, asegurando su asociación con un local específico.  

- **Gestión de empleados (Rol Administrador)**:  
  - Agregar nuevos empleados.  
  - Eliminar empleados.  
  - Listar empleados registrados.  

- **Autenticación con Firebase**:  
  - Implementación de Firebase Authentication para gestionar la autenticación de usuarios.  
  - Los empleados registrados podrán acceder a la app luego de autenticarse.  
  - Asignación de permisos y claims personalizados mediante Firebase Admin SDK.  

- **Conexión con la Base de Datos**:  
  - Uso de **Firebase Realtime Database** para almacenar y recuperar la información de los empleados.  

- **Servidor remoto con Firebase Functions**:  
  - Servidor en Firebase Functions para la gestión de usuarios en Firebase Auth.  

---

### ✅ Segunda Etapa: Gestión de Inventarios  

🔹 **Gestión de Inventario**:  
- Registro y control de productos disponibles en el local.  
- Modificación y eliminación de productos.
- actualizacion de productos segun la actividad y consumo del establecimiento

### ✅ Tercera Etapa: Control y Manejo de las ordenes en mesas

🔹 **Gestión de Mesas y Pedidos**:  
- Asignación de mesas y estado de ocupación.  
- Registro de pedidos y seguimiento de su estado.
- Actualizacion de inventarios al el servicio de una mesa  

### ✅ Cuarta Etapa: Impresion de tickets

🔹 **Impresion de tickets**:  
- Configuracion de sistema de enlace con la impresora  
- Aplicacion del formato para la impresion del ticket
- logica para imprimir la informacion deseada  

---

## 🛠️ Tecnologías Utilizadas  
- **Android Studio (Java)**  
- **Firebase Realtime Database** (Almacenamiento de datos)  
- **Firebase Authentication** (Autenticación de usuarios)  
- ~~**Node.js** (Servidor local)~~ **obsoleto**
- **Firebase Admin SDK** (Gestión de usuarios y permisos)
- **Firebase Functions** (servidor en la nube)

---

## 📢 Actualizaciones
Actualmente la app esta en etapa de testing

## 👥 Colaboradores  

- [Héctor](https://github.com/enlace-de-hector)  
- [Jessica](https://github.com/enlace-de-jessica)

## 📖 Instrucciones de Uso 
- en estapas iniciales de la app funcionabamos con un servidor local pero con el desarrollo de la app terminamos migrando a un servidor en la nube
- de igual manera dejamos en repositorio del antiguo servidor por motivos academicos
[Servidor Local](https://github.com/rapo84/ServerFirebaseSDK)

## ⚠️ IMPORTANTE ⚠️
  - Antiguamente usabamos un servidor local para la gestion de la app, en la actualidad usamos servicios en la nube para esta funcion, especificamente Firebase Functions
  - tanto al servidor local como al proyecto de android studio le faltan los archivos con los token de seguridad, lo cuales no se han subido por razones obvias
