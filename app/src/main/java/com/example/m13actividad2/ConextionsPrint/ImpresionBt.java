package com.example.m13actividad2.ConextionsPrint;

import android.Manifest;
import android.bluetooth.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.example.m13actividad2.interfaces.SelecconImpresoraCallBack;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class ImpresionBt {

    private static final String TAG = "impresionBt";
    private static final UUID UUID_SERIAL_PORT = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static void SeleccionarImpresora(Context context, SelecconImpresoraCallBack callback) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(context, "Este dispositivo no tiene Bluetooth", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(context, "Bluetooth no está habilitado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener los dispositivos emparejados
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // Crear una lista de nombres de dispositivos Bluetooth emparejados
            String[] deviceNames = new String[pairedDevices.size()];
            final BluetoothDevice[] devicesArray = new BluetoothDevice[pairedDevices.size()];
            int index = 0;

            for (BluetoothDevice device : pairedDevices) {
                deviceNames[index] = device.getName();
                devicesArray[index] = device;
                index++;
            }

            // Mostrar un diálogo para que el usuario seleccione un dispositivo
            new AlertDialog.Builder(context)
                    .setTitle("Selecciona una impresora")
                    .setItems(deviceNames, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // El usuario selecciona el dispositivo
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            String nombreImpresora = devicesArray[which].getName();
                            Toast.makeText(context, "Impresora seleccionada: " + nombreImpresora, Toast.LENGTH_SHORT).show();
                            // Llamar al callback para pasar el valor de la impresora seleccionada
                            callback.onImpresoraSeleccionada(nombreImpresora);
                        }
                    })
                    .show();
        } else {
            Toast.makeText(context, "No hay dispositivos Bluetooth emparejados", Toast.LENGTH_SHORT).show();
        }
    }

    public static void Imprimir(Context context, String nombreImpresora, String textoAimprimir) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(context, "Este dispositivo no tiene Bluetooth", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(context, "Bluetooth no está habilitado", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) !=
                android.content.pm.PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permiso BLUETOOTH_CONNECT no concedido", Toast.LENGTH_SHORT).show();
            return;
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            if (device.getName().equals(nombreImpresora)) {
                connectAndPrint(context, device, textoAimprimir);
                return;
            }
        }

        Toast.makeText(context, "Impresora no encontrada: " + nombreImpresora, Toast.LENGTH_SHORT).show();
    }

    private static void connectAndPrint(Context context, BluetoothDevice device, String text) {
        BluetoothSocket socket = null;

        // Verificación de permisos antes de usar el socket
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permiso BLUETOOTH_CONNECT no concedido", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            socket = device.createRfcommSocketToServiceRecord(UUID_SERIAL_PORT);
            socket.connect();

            OutputStream outputStream = socket.getOutputStream();

            outputStream.write(new byte[]{0x1B, 0x40}); // Inicializar
            outputStream.write(new byte[]{0x1B, 0x21, 0x08}); // Negrita
            outputStream.write(new byte[]{0x1B, 0x61, 0x01}); // Centrado
            outputStream.write(text.getBytes("UTF-8"));
            outputStream.write(new byte[]{0x0A, 0x0A, 0x1D, 0x56, 0x41, 0x10}); // Corte
            outputStream.write(new byte[]{0x1B, 0x64, 0x04}); // Avance

            outputStream.flush();
            Toast.makeText(context, "Texto enviado a la impresora", Toast.LENGTH_SHORT).show();

        } catch (SecurityException se) {
            Log.e(TAG, "SecurityException: Permiso no concedido: " + se.getMessage());
            Toast.makeText(context, "Permiso Bluetooth denegado", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e(TAG, "Error al imprimir: " + e.getMessage());
            Toast.makeText(context, "Error al conectar/imprimir: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (socket != null && socket.isConnected()) socket.close();
            } catch (IOException e) {
                Log.e(TAG, "Error al cerrar el socket: " + e.getMessage());
            }
        }
    }

}
