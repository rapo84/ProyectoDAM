package com.example.m13actividad2.utils;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m13actividad2.Actividades.EmpleadoActivity;
import com.example.m13actividad2.Actividades.OpcionesDeAdmin;
import com.example.m13actividad2.Modelos.Persona;
import com.example.m13actividad2.network.ApiResponse;
import com.example.m13actividad2.interfaces.ApiService;
import com.example.m13actividad2.network.ClaimsResponse;
import com.example.m13actividad2.interfaces.LocalesCallback;
import com.example.m13actividad2.network.RetrofitClient;
import com.example.m13actividad2.interfaces.RoleCheckCallback;
import com.example.m13actividad2.network.RoleRequest;
import com.example.m13actividad2.network.TokenRequest;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utilidad {

    /*
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                        SECCION PARA METODOS DE VERIFIACION DE DATOS
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    */

    /*
    PARA VERIFICAR FORMATO DE LOS CORREOS ELECTRONICOS
     */
    public static boolean patronEmail(String email) {
        String PatronEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && Pattern.matches(PatronEmail, email);
    }

    /*
    PARA VERIFICAR FORMATO DE LAS CONTRASEÑAS (DEBE TENER AL MENOS 1 NUMERO 1 MAYUSCULA Y 1 CARACTER ESPECIAL
     */
    public static boolean patronPswd(String pswd) {
        String PatronContrasenia = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{6,}$";
        return pswd != null && Pattern.matches(PatronContrasenia, pswd);
    }

    public static boolean patronDNI(String DNI){
        String PatronDni = "^[0-9]{8}[a-zA-Z]{1}$";
        return DNI != null && Pattern.matches(PatronDni, DNI);
    }
    /*////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                        GESTIONAR ARCHIVOS EN LOCAL PARA LA APP
        variables dentro del archivo: "NombreLocal", "NumMesas", "Impresora"
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////*/

    public static void guardarNombrenegocioLocalmente(Context context, String localname) {
        if (!localname.trim().isEmpty()) {
            SharedPreferences prefs = context.getSharedPreferences("UserData", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("NombreLocal", localname);
            editor.apply();
            String confirm = recupernombrelocal(context);
            Log.i("Local enlazado:", confirm);
        }else{
            Toast.makeText(context,"El nombre del local no puede estar vacio",Toast.LENGTH_SHORT).show();
        }
    }
    public static String recupernombrelocal(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String nombreLocal = prefs.getString("NombreLocal", null); // Devuelve null si no existe la clave

        if (nombreLocal == null) {
            return ""; // Devuelve una cadena vacía en lugar de null para evitar posibles errores  MANEJAR CON UN TOAST DONDE LO LLAMEMOS
        } else {
            return nombreLocal;
        }
    }

    public static void guardarDatoLocalmente(Context context, String DatoGurdar, String claveObjeto) {
        if (!DatoGurdar.trim().isEmpty()) {
            SharedPreferences prefs = context.getSharedPreferences("UserData", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(claveObjeto, DatoGurdar);
            editor.apply();
            String confirm = recuperDatoslocal(context, claveObjeto);
            Log.i("Local enlazado:", confirm);
        }else{
            Toast.makeText(context,"El nombre del local no puede estar vacio",Toast.LENGTH_SHORT).show();
        }
    }

    public static String recuperDatoslocal(Context context, String claveObjeto) {
        SharedPreferences prefs = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String DatoObtenido = prefs.getString(claveObjeto, null); // Devuelve null si no existe la clave

        if (DatoObtenido == null) {
            return ""; // Devuelve una cadena vacía en lugar de null para evitar posibles errores  MANEJAR CON UN TOAST DONDE LO LLAMEMOS
        } else {
            return DatoObtenido;
        }
    }

    public static void guardarnumMEsas(Context context, int numMesas) {
        if (numMesas > 0) {
            SharedPreferences prefs = context.getSharedPreferences("UserData", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("NumMesas", numMesas);
            editor.apply();
            int num = recupernumMesas(context);
            String confirm = String.valueOf(num);
            Log.i("# de mesas guaradado:", "num de mesas= " + confirm);
        }else{
            Toast.makeText(context,"el valor debe ser mayor a 0",Toast.LENGTH_SHORT).show();
        }
    }

    public static int recupernumMesas(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        int NumMesas = prefs.getInt("NumMesas", 0); // Devuelve null si no existe la clave

        if (NumMesas == 0) {
            return 0; // Devuelve un 0 en lugar de null para evitar posibles errores  MANEJAR CON UN TOAST DONDE LO LLAMEMOS
        } else {
            return NumMesas;
        }
    }

    //esto no lo vamos a usar pero lo tengo porque tengo que borrarlo por alguna razon pero en teoria no lo usaremos y debemos borrarlo  -------------------------------------------------
    public static void borrarNombreLocal(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("NombreLocal"); // Elimina la clave "NombreLocal"
        editor.apply(); // Aplica los cambios (mejor que commit() en la mayoría de los casos)
    }



    /*
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                        ASIGNAR Y VERIFICAR CLAIMS PERSONALIZADOS
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    */


    /*
                    ////////// ASIGNAR CLAIMS DE USUARIOS DEL AUTH//////////
    solicitamos al servidor que le asigne claims personalizados al usuario que estamos resgistrando
     */
    public static void peticionrole(String uid, String local, String categoria) {
        Log.d("peticionrole", "⚠️ UID: " + uid + " Local: " + local + " Role: " + categoria);   // LOG DE CONTROL ELIMINAR LUEGO

        ApiService apiService= RetrofitClient.getApiService();                                        // configuramos la conexion de retrofit para hacer la peticion al servidor
        RoleRequest roleRequest = new RoleRequest(uid, local, categoria);                             // creamos el objeto con los datos que vamos a pasar
        apiService.setRole(roleRequest).enqueue(new Callback<ApiResponse>() {                         // pasamos los datos de la peticion al servidor

        // recibimos la respuesta del servidor
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("Retrofit", "✅ Rol asignado correctamente");
                } else {
                    try {
                        String errorResponse = response.errorBody().string();
                        Log.e("Retrofit", "❌ Error en la respuesta: " + errorResponse);
                    } catch (IOException e) {
                        Log.e("Retrofit", "❌ Error al leer la respuesta de error", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("Retrofit", "❌ Error al hacer la petición", t);
            }
        });
    }

    //comentario git hub

    /*
    ////////VERIFICAR CLAIMS DE USUARIOS DEL AUTH  ESTE SE USA SOLO PARA CAMBIAR DE ACTIVIDAD SEGUN SU CATEGORIA////////////
            PRIMERO USAMOS ESTE METODO QUE LLAMA LUEGO A ExtractUserRole  */
    public static void checkUserRoleparaSeccion(Context context) {

        String nombrelocal = recupernombrelocal(context);                                      // obtenemos el nombre del local guardado localmente en el dispositivo
        Log.e("chequeo login", "⚠️ el nombre del local es: "+ nombrelocal);             // LOG PARA CONFIRMAR EL NOMBRE DEL LOCAL QUE SE ESTE USANDO (SE PUEDE BORRAR LUEGO)--------------------------------------------------

        /*  Llama al método ExtractUserRole para extraer los claims del usuario actualmente logueado y poder usarlos
            para saber que categoria posee y a que lcoal pertenece y en base a esto otorgale el acceso a su
             respectivo sector de la app*/
        ExtractUserRole(context, new RoleCheckCallback() {

            /*  el onrolreceived es el medio para recibir la respuetsa del metodo "ExtractUserRole" que al ser asincrono no puede enviar una
                respuesta normalmente */
            @Override
            public void onRoleReceived(String role, String localex) {
                Log.e("chequeo comparacion", "⚠️ el rol es: "+ role + ", el local es; " + localex);  //SOLO PARA CHEQUEO QUE ESTE RECIBIENDO DATOS Y QUE SEAN CORRECTOS (SE PUEDE BORRAR)-------------------------------------------
                if ("Admin".equals(role) && nombrelocal.equals(localex)) {
                    // El usuario tiene el rol de Admin y pertence al local del movil donde se logueo
                    Toast.makeText(context, "Acceso concedido", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, OpcionesDeAdmin.class);
                    context.startActivity(intent);
                    if (context instanceof Activity) { // se usa el finish() de esta manera porque estamos trabajando pasando un contexto y un contexto por si solo no contiene el metodo finish este solo lo tiene si el cotexto es una actividad
                        ((Activity) context).finish();
                    }

                } else if("Empleado".equals(role)&& nombrelocal.equals(localex)) {
                    // El usuario tiene el rol de empleado y pertenece al local desde donde se logueo
                    Toast.makeText(context, "Acceso concedido", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, EmpleadoActivity.class);
                    context.startActivity(intent);
                    if (context instanceof Activity) {  // se usa el finish() de esta manera porque estamos trabajando pasando un contexto y un contexto por si solo no contiene el metodo finish este solo lo tiene si el cotexto es una actividad
                        ((Activity) context).finish();
                    }

                }else{
                    // El usuario no tiene el rol correcto o no pertenece al local del movil donde se logueo
                    Toast.makeText(context, "Acceso denegado", Toast.LENGTH_SHORT).show();
                }

            }
            /*  este metodo solo existe porque quise usar una sola interfaz para las 2 respuestas que necesitaba del rolecheckcallback
                pero en este metodo "checkUserRoleparaSeccion" no funciona para nada pero al ser interfaz debo implementarlo obligatoriamente*/
            @Override
            public void onRoleChecked(boolean tieneElRol) {
            }
        });
    }

    /*  ///////////VERIFICAMOS LOS CLAIMS DEL USUARIO PERO ESTE SERIA PARA SABER SI TIENE LOCAL Y CATEGOIA CORRECTAS, LO USAREMOS PARA LOS PERMISOS DE LA BBDD//////////  */
    public static void checkUserRoleParaSaberSiTienePermisos(Context context, String rolesperado, String localesperado) {
        // Llama al método checkUserRole
        checkUserRole(context, rolesperado, localesperado,new RoleCheckCallback() {
            @Override
            public void onRoleChecked(boolean tieneElRol) {
                if (tieneElRol) {
                    // Si el usuario tiene el rol y local correctos, ejecuta la acción permitida
                    Log.d("Permisos", "✅ El usuario tiene los permisos requeridos. Ejecutando acción permitida.");
                    // AQUI GESTIONAMOS LA LOGICA QUE QUERAMOS
                } else {
                    // Si el usuario no tiene los permisos, ejecuta una acción alternativa
                    Log.d("Permisos", "❌ El usuario NO tiene los permisos requeridos. Acción denegada.");
                    // // AQUI GESTIONAMOS LA LOGICA QUE QUERAMOS O MOSTRAMOS UN MENSAJE DE ERROR
                }
            }
            /*  este metodo solo existe porque quise usar una sola interfaz para las 2 respuestas que necesitaba del rolecheckcallback
                pero en este metodo "checkUserRoleParaSaberSiTienePermisos" no funciona para nada pero al ser interfaz debo implementarlo obligatoriamente*/
            @Override
            public void onRoleReceived(String role, String localex) {
            }
        });
    }

    /*  /////////// EL SIGUIENTE METODO ES EL QUE GENERA LA RESPUESTA QUE NECESITAMOS EN checkUserRoleParaSaberSiTienePermisos PARA PODER GESTIONAR NUESTRA LOGICA (RETORNA UN BOOLEANO MEDIANTE EL "onRoleChecked"     */
    public static void checkUserRole(Context context, String categoria, String localEsperado, RoleCheckCallback callback) {
        // Obtiene el usuario autenticado desde Firebase Auth
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Verifica si el usuario está autenticado
        if (user != null) {
            // Solicita el token del usuario, forzando la renovación
            user.getIdToken(true) // 'true' para forzar la renovación del token
                    .addOnCompleteListener(task -> {
                        // Verifica si la solicitud del token fue exitosa
                        if (task.isSuccessful()) {
                            // Si la obtención del token fue exitosa, lo guardamos
                            String idToken = task.getResult().getToken();

                            // llamamos Retrofit para hacer una solicitud al servidor
                            ApiService apiService= RetrofitClient.getApiService();
                            TokenRequest tokenRequest = new TokenRequest(idToken); // Prepara el token para enviarlo

                            // Realiza la solicitud asincrónica al servidor para obtener los claims
                            apiService.getClaims(tokenRequest).enqueue(new Callback<ClaimsResponse>() {
                                @Override
                                public void onResponse(Call<ClaimsResponse> call, Response<ClaimsResponse> response) {
                                    // Variable para determinar si el usuario tiene permisos
                                    boolean aceptado = false;

                                    // Verifica si la respuesta es exitosa y contiene datos
                                    if (response.isSuccessful() && response.body() != null) {
                                        ClaimsResponse claimsResponse = response.body(); // Obtiene los claims del usuario

                                        // Log para mostrar los claims obtenidos (SE PUEDE BORRAR LUEGO) ---------------------------------------------------------------------------------------------------------
                                        Log.i("⚠️ ️Firebase", "Claims del usuario: " + claimsResponse.toString());

                                        // Extrae el rol y el local del usuario desde los claims
                                        String role = claimsResponse.getRole().trim();  // El rol del usuario
                                        String local = claimsResponse.getLocal().trim();  // El local asignado al usuario
                                        Log.i("⚠️ Firebase", "Claims categoria: " + role);     // LOS PARA COMPROBAR DATOS! SE PUEDE BORRAR LUEGO! ---------------------------------------------------------------
                                        Log.i("⚠️ Firebase", "Claims local: " + local);        // LOS PARA COMPROBAR DATOS! SE PUEDE BORRAR LUEGO! ---------------------------------------------------------------

                                        // Verifica si ambos claims están presentes y si coinciden con los valores esperados
                                        if (role != null && local != null) {
                                            // Compara el rol y el local con los valores esperados
                                            if (categoria.equals(role) && localEsperado.equals(local)) {
                                                Log.d("Firebase", "✅  El usuario tiene el rol y local correctos");
                                                aceptado = true; // Usuario con los permisos correctos
                                            } else {
                                                Toast.makeText(context,"El usuario NO tiene el rol o local correctos",Toast.LENGTH_SHORT).show();
                                                Log.e("Firebase", "❌ El usuario NO tiene el rol o local correctos");  // LOS PARA COMPROBAR DATOS! SE PUEDE BORRAR LUEGO! ---------------------------------------------------------------
                                            }
                                        } else {
                                            Toast.makeText(context,"Faltan claims: rol o local no encontrados",Toast.LENGTH_SHORT).show();
                                            Log.e("Error", "❌ Faltan claims: rol o local no encontrados");    // LOS PARA COMPROBAR DATOS! SE PUEDE BORRAR LUEGO! ---------------------------------------------------------------
                                        }
                                    } else {
                                        Toast.makeText(context,"Ha ocurrido un error inesperado",Toast.LENGTH_SHORT).show();
                                        Log.e("Error", "❌ Error al obtener claims: " + (response.errorBody() != null ? response.errorBody().toString() : "Respuesta vacía"));  // este no se borra porque en caso de saltar indica el error
                                    }

                                    // Llama al callback con el resultado de la verificación
                                    if (callback != null) {
                                        callback.onRoleChecked(aceptado);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ClaimsResponse> call, Throwable t) {
                                    // En caso de error en la solicitud HTTP, se loguea el error
                                    Log.e("Error", "❌ Error al hacer la petición: " + t.getMessage());  // no se borra, ofrece informacion

                                    // Llama al callback con el resultado de la verificación (false por error)
                                    if (callback != null) {
                                        callback.onRoleChecked(false);
                                    }
                                }
                            });
                        } else {
                            // Si la obtención del token falló, se loguea el error
                            Log.e("Firebase", "❌ Error al obtener el token", task.getException()); // no se borra, ofrece informacion

                            // Llama al callback con el resultado de la verificación (false por error)
                            if (callback != null) {
                                callback.onRoleChecked(false);
                            }
                        }
                    });
        } else {
            // Si el usuario no está autenticado, se loguea el error
            Log.e("Firebase", "❌ El usuario no está autenticado.");

            // Llama al callback con el resultado de la verificación (false, ya que no hay usuario)
            if (callback != null) {
                callback.onRoleChecked(false);
            }
        }
    }

    /*  /////////// EL SIGUIENTE METODO ES EL QUE GENERA LA RESPUESTA QUE NECESITAMOS EN checkUserRoleparaSeccion PARA poder enviarlo al apartado de admin o empleado segun convenga///////////////*/
    public static void ExtractUserRole(Context context, RoleCheckCallback callback) {
        // Obtiene el usuario autenticado desde Firebase Auth
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Verifica si el usuario está autenticado
        if (user != null) {
            // Solicita el token del usuario, forzando la renovación
            user.getIdToken(true) // 'true' para forzar la renovación del token
                    .addOnCompleteListener(task -> {
                        // Verifica si la solicitud del token fue exitosa
                        if (task.isSuccessful()) {
                            // Si la obtención del token fue exitosa, lo guardamos
                            String idToken = task.getResult().getToken();

                            // llamamos Retrofit para hacer una solicitud al servidor
                            ApiService apiService= RetrofitClient.getApiService();
                            TokenRequest tokenRequest = new TokenRequest(idToken); // Prepara el token para enviarlo

                            // Realiza la solicitud asincrónica al servidor para obtener los claims
                            apiService.getClaims(tokenRequest).enqueue(new Callback<ClaimsResponse>() {
                                @Override
                                public void onResponse(Call<ClaimsResponse> call, Response<ClaimsResponse> response) {
                                    // Variable para determinar si el usuario tiene permisos
                                    String CateRol="";
                                    String localx="";

                                    // Verifica si la respuesta es exitosa y contiene datos
                                    if (response.isSuccessful() && response.body() != null) {
                                        ClaimsResponse claimsResponse = response.body(); // Obtiene los claims del usuario

                                        // Log para mostrar los claims obtenidos (útil para depuración)
                                        Log.i("Firebase", "⚠️ Claims del usuario: " + claimsResponse.toString());  //SOLO PARA VERIFICAR DATOS, SE PEUDE BORRAR -------------------------------------------

                                        // Extrae el rol y el local del usuario desde los claims
                                        CateRol = claimsResponse.getRole().trim();  // El rol del usuario
                                        localx = claimsResponse.getLocal().trim();  // El local asignado al usuario
                                        Log.i("Firebase", "⚠️ Claims categoria: " + CateRol);  //SOLO PARA VERIFICAR DATOS, SE PEUDE BORRAR ---------------------------------------------------------------
                                        Log.i("Firebase", "⚠️ Claims local: " + localx);       //SOLO PARA VERIFICAR DATOS, SE PEUDE BORRAR ---------------------------------------------------------------

                                    } else {
                                        Toast.makeText(context,"Ha ocurrido un error inesperado",Toast.LENGTH_SHORT).show();
                                        Log.e("Error", "❌ Error al obtener claims: " + (response.errorBody() != null ? response.errorBody().toString() : "Respuesta vacía"));  // no borrar, ofrece info del error
                                    }

                                    // Llama al callback con el resultado de la verificación
                                    if (callback != null) {
                                        callback.onRoleReceived(CateRol, localx);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ClaimsResponse> call, Throwable t) {
                                    // En caso de error en la solicitud HTTP, se loguea el error
                                    Log.e("Error", "❌ Error al hacer la petición: " + t.getMessage());        // no borrar, ofrece info del error

                                    // Llama al callback con el resultado de la verificación (false por error)
                                    if (callback != null) {
                                        callback.onRoleChecked(false);
                                    }
                                }
                            });
                        } else {
                            // Si la obtención del token falló, se loguea el error
                            Log.e("Firebase", "❌ Error al obtener el token", task.getException());        // no borrar, ofrece info del error

                            // Llama al callback con el resultado de la verificación (false por error)
                            if (callback != null) {
                                callback.onRoleChecked(false);
                            }
                        }
                    });
        } else {
            // Si el usuario no está autenticado, se loguea el error
            Log.e("Firebase", "❌ El usuario no está autenticado.");

            // Llama al callback con el resultado de la verificación (false, ya que no hay usuario)
            if (callback != null) {
                callback.onRoleChecked(false);
            }
        }
    }




    /*
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                   REGISTRAR USUARIOS TANTO EN BBDD COMO EN AUTH
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    */


    /* REGISTRA UN USUARIO EN FIREBASE AUTH Y SI EL REGISTRO ES CORRECTO LLAMA A 2 METODOS ADICIONALES 1 PARA REGISTRAR EL EMPLEADO EN LA BBDD Y EL OTRO PARA ASIGNARLE LOS CLAIMS PERSONALIZADOS*/
    public static void RegUsuarioEnAtuh(Context context, String Nombre,String Apellidos, String Email,String Dni,String Pswd, String Categoria, String horario) {
        //Obtenemos el nombre del local al que pertenece el dispositivo
        String nombrelocal = Utilidad.recupernombrelocal(context);
        if (nombrelocal.isEmpty()){
            Toast.makeText(context, "No existe un nombre de local almacenado. Por favor, ingrese uno desde las opciones de soporte técnico.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(Email, Pswd)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // si el usuario se crea correctamente obtenemos su uid para poder almacenarlo en la bbdd
                        AuthResult authResult = task.getResult(); // Obtén el AuthResult (que viene siendo el resultado de registrar al nuevo user
                        FirebaseUser user = authResult.getUser(); // Obtén el nuevo usuario
                        String uid = user.getUid(); // Obtenemos el UID del nuevo usuario

                        //guardamos el empleado en la bbdd
                        RegEmpleado(context, nombrelocal, uid, Nombre, Apellidos, Email, Dni, Pswd, Categoria, horario);      // llamamos al metodo

                        //le asignamos los claims respectivos tanto del local que pertenece como a su categoria
                        Utilidad.peticionrole(uid, nombrelocal, Categoria);                                         // llamamos al metodo
                        Log.e("control de nombre local", "⚠️ el nombre de local actual es: "+nombrelocal);   // SOLO PARA CONTROL DE DATOS (SE PUEDE BORRAR LUEGO)-------------------------------------------------------------------
                        if (uid != null) {
                            Log.d("FirebaseAuth", "✅ Empleado creado: " + uid);                          // SOLO PARA CONTROL DE DATOS (SE PUEDE BORRAR LUEGO)-------------------------------------------------------------------
                            Toast.makeText(context, "Usuario registrado con éxito", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // Error al crear usuario
                        Log.e("FirebaseAuth", "❌ Error al registrar Empleado", task.getException());    // no borar, ofrece informacion
                        Toast.makeText(context, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    /* ////////////////// añadimos el nuevo empleado a la base de datos ubicandolo en su respectivo local con su respectiva categoria y sus permisos/////////////// */
    public static void RegEmpleado(Context context, String nombrelocal, String uid, String Nombre, String Apellidos, String  Email, String  Dni, String Pswd, String Categoria, String horario){
        // Crear un mapa con los datos del Empleado
        Map<String, String> Empleado = new HashMap<>();
        Empleado.put("nombre", Nombre);
        Empleado.put("apellidos", Apellidos);
        Empleado.put("email", Email);
        Empleado.put("dni", Dni);
        Empleado.put("password", Pswd);
        Empleado.put("categoria", Categoria);
        Empleado.put("horario", horario);
        Empleado.put("uid", uid);



        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Locales").child(nombrelocal).child("Empleados");

        // Agregamos el empelado en el nodo correspondiente a la categoria
        mDatabase.child(Categoria).child(Dni).setValue(Empleado)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firebase", "✅ se ha agreagdo el empleado correctamente");                // no borrar ofrece confirmacion por consola

                    } else {
                        Log.e("Firebase", "❌ Error al agregar el nodo Admin.", task.getException());    // no borrar ofrece, informacion por consola
                    }
                });

    }


    /*
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                    REGISTRAR USUARIOS TANTO EN BBDD COMO EN AUTH
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    */


    /* /////////// Metodo para leer los trabajadores de la bbdd para rellenar un Recyclerview y actualizando el contendido del adaptador que se este usando par el recyclerview/////////////   */
    public static void leerTrabajadores(Context context, List<Persona> lista, RecyclerView.Adapter adapter) {
        String nombrelocal= Utilidad.recupernombrelocal(context);           //Obtenemos el nombre del local a donde pertenece el dispositivo

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Locales").child(nombrelocal).child("Empleados");  // obtenemos la referencia a la bbdd

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lista.clear();                                                                      // Limpiamos la lista antes de agregar nuevos datos
                for (DataSnapshot categoriaSnapshot : dataSnapshot.getChildren()) {                 // Iterar sobre "Admin" y "Empleado" para poder leer todos los empleados
                    for (DataSnapshot empleadoSnapshot : categoriaSnapshot.getChildren()) {         // Iterar sobre cada empleado
                        String nombre = empleadoSnapshot.child("nombre").getValue(String.class);
                        String apellido = empleadoSnapshot.child("apellidos").getValue(String.class);
                        String dni = empleadoSnapshot.child("dni").getValue(String.class);
                        String email = empleadoSnapshot.child("email").getValue(String.class);
                        String categoria = empleadoSnapshot.child("categoria").getValue(String.class);
                        String uid = empleadoSnapshot.child("uid").getValue(String.class);
                        String horario = empleadoSnapshot.child("horario").getValue(String.class);
                        //no obtenemos el pswd porque no lo vamos a mostrar ni por pantalla ni lo usaremos para nada que no sea loguearse

                        lista.add(new Persona(uid, dni, nombre, apellido, categoria, email,horario));
                        Log.d("Empleado", "⚠️ UID: "+ uid +", Nombre: " + nombre + ", Apellido: " + apellido + ", Categoría: " + categoria + ", DNI: " + dni + ", Horario: " + horario);   // VERIFICACION DE DATOS, SE PUEDE BORRAR LUEGO ----------------------------------------------------------------
                    }
                }
                adapter.notifyDataSetChanged();                                                     // Notificar al RecyclerView que los datos han cambiado y que debe volver a cargar
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "❌ Error al leer datos", databaseError.toException());     // NO borrar, ofrece informacion
            }
        });
    }

    /*   /////////////// METODO PARA CERRAR LA SESION DEL USUARIO QUE ESTE LOGUEADO Y REDIRIGIRSE A UNA ACTIVIDAD QUE LE PASEMOS   //////////////////// */
    public static void cerrarSesionYRedirigir(Context contexto, Class<?> nuevaActividad) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();

        Intent intent = new Intent(contexto, nuevaActividad);
        contexto.startActivity(intent);

        // Si el contexto es una actividad, la cerramos  (esto por que si pasamos el contexto solamente, este no tiene metodo finish y dara error
        if (contexto instanceof Activity) {
            ((Activity) contexto).finish();
        }

        Toast.makeText(contexto, "Se ha cerrado la sesión del usuario", Toast.LENGTH_SHORT).show();
    }
/*
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                obtener lista de locales en BBDD y verificar qeu no se repita esto porque si agregamos
                    una clave a la BBDD con un mismo esta reiniciara todos los datos de ese local
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

    // primero obtenemos lal ista de todos los locales que existen en la bbdd
    public static void ObtenerLocalesExistentes(LocalesCallback callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Locales");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> localexist = new ArrayList<>();

                for (DataSnapshot localesSnapshot : dataSnapshot.getChildren()) {
                    String local = localesSnapshot.getKey();
                    localexist.add(local);
                }

                Log.d("Locales", "Locales obtenidos: " + localexist.toString());

                // 📌 Llamamos al callback (una vez terminada la consulta a firebase) y ahora si pasando la lista llena
                callback.onCallback(localexist);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "❌ Error al leer datos", databaseError.toException());
            }
        });
    }

    // ahora verificamos si el nombre que vamos a introducir ya existe en la BBDD y procedemos en funcion a la respuesta
    public static boolean VerificarExistlocal(Context context, String nombreIngreso, ArrayList<String> listalocales) {
        boolean existe = false;
        Log.i("Debug", "Locales obtenidos: " + listalocales.toString());
        Log.i("Debug", "Nombre a verificar: " + nombreIngreso);


        // Recorremos la lista y verificar si el nombre ya existe
        for (String nombre : listalocales) {
            if (nombreIngreso.trim().equalsIgnoreCase(nombre.trim())) {
                existe = true; // Marcamos que el nombre ya existe
                break; // si ya hubo coincidencia rompemos el bucle
            }
        }

        if (existe) {
            Log.i("Comprobacion", "❌ Se rechaza agregar a la BBDD porque ya existe el nodo local.");
            Toast.makeText(context, "Ya existe un NODO-LOCAL con ese nombre dentro de la BBDD", Toast.LENGTH_LONG).show();
        } else {
            AgregarLocalBdDd(context, nombreIngreso);
            Log.i("Comprobacion", "✅ Se agrega el nuevo local a la BBDD.");
        }

        return existe;
    }


    // si el nombre no coincide con ningu en la bbdd se procede a agregarlo
    public static void AgregarLocalBdDd(Context context, String nombrelocal){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Locales");

        // Agregamos el local en la BBDD
        mDatabase.child(nombrelocal).setValue("")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firebase", "✅ Se ha agreagdo el Local correctamente");
                        Toast.makeText(context, "Se ha agregado el local a la BBDD",Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("Firebase", "❌ Error al agregar el nodo Admin.", task.getException());
                        Toast.makeText(context, "No Se ha podido agregar el nuevo local a la BBDD",Toast.LENGTH_LONG).show();
                    }
                });
    }


}
