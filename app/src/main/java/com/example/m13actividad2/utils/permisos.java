package com.example.m13actividad2.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class permisos {

    public static final int REQUEST_ALL_PERMISSIONS = 101;

    // Lista de permisos necesarios para el funcionamiento de Bluetooth (se pueden agregar mas)
    public static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    // Verifica si todos los permisos han sido concedidos
    public static boolean checkPermissions(Activity activity) {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    // Solicita los permisos necesarios
    public static void requestPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS, REQUEST_ALL_PERMISSIONS);
    }

    public static boolean handlePermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_ALL_PERMISSIONS) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(activity, "Es necesario conceder todos los permisos para el funcionamiento de la app.", Toast.LENGTH_LONG).show();
                    return false; // en caso de que algun permiso sea denegado
                }
            }
            // en caso de que todos los permisos fuesen concedidos
            return true;
        }
        return false;
    }
}