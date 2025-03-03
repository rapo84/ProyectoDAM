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

- **Servidor Local con Node.js**:  
  - Servidor creado con Node.js para la gestión de usuarios en Firebase Auth.  

---

### 🔄 Próximas Etapas de Desarrollo  

🔹 **Gestión de Inventario**:  
- Registro y control de productos disponibles en el local.  
- Modificación y eliminación de productos.  

🔹 **Gestión de Mesas y Pedidos**:  
- Asignación de mesas y estado de ocupación.  
- Registro de pedidos y seguimiento de su estado.  

Estas funciones serán implementadas en futuras actualizaciones de la app.  

---

## 🛠️ Tecnologías Utilizadas  
- **Android Studio (Java)**  
- **Firebase Realtime Database** (Almacenamiento de datos)  
- **Firebase Authentication** (Autenticación de usuarios)  
- **Node.js** (Servidor local)  
- **Firebase Admin SDK** (Gestión de usuarios y permisos)  

---

## 📢 Actualizaciones
Este archivo README.md se actualizará conforme avance el desarrollo del proyecto, incorporando detalles sobre nuevas funcionalidades implementadas.

## 👥 Colaboradores  

- [Héctor](https://github.com/enlace-de-hector)  
- [Jessica](https://github.com/enlace-de-jessica)

## 📖 Instrucciones de Uso 
Para que la app funcione se debe descargar tambien el repositorio con el servidor local 
[Servidor Local](https://github.com/rapo84/ServerFirebaseSDK)

## ⚠️ IMPORTANTE ⚠️
  - Este proyecto solo funcion con el servidor en ejecucion en local
  - tanto al servidor como al proyecto de android studio le faltan los archivos con los token de seguridad, lo cuales no se han subido por razones obvias



