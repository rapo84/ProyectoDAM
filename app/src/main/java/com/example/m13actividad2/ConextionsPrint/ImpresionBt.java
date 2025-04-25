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

import com.example.m13actividad2.Modelos.Producto;
import com.example.m13actividad2.interfaces.SelecconImpresoraCallBack;
import com.example.m13actividad2.utils.Utilidad;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

    public static void Imprimir(Context context, List<Producto> productos, Double totalSinIVA, double porcentajeIVA) {
        String nombreLocal = "", correo = "", telefono = "", impresora = "";
        // Recuperar impresora
        impresora = Utilidad.recuperDatoslocal(context, "Impresora");
        if (impresora.isEmpty()) {
            Toast.makeText(context, "No hay impresora guardada", Toast.LENGTH_SHORT).show();
        }

        // Recuperar nombre del local
        nombreLocal = Utilidad.recupernombrelocal(context);
        if (nombreLocal.isEmpty()) {
            Toast.makeText(context, "No hay local guardado", Toast.LENGTH_SHORT).show();
        }
        // Recuperar teléfono
        telefono = Utilidad.recuperDatoslocal(context, "Telefono");
        if (telefono.isEmpty()) {
            Toast.makeText(context, "No hay telefono guardado", Toast.LENGTH_SHORT).show();
        }
        // Recuperar correo
        correo = Utilidad.recuperDatoslocal(context, "Correo");
        if (correo.isEmpty()) {
            Toast.makeText(context, "No hay correo guardado", Toast.LENGTH_SHORT).show();
        }

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

        Set<BluetoothDevice> DispositivosConectados = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : DispositivosConectados) {
            if (device.getName().equals(impresora)) {
                connectAndPrint(context, device,nombreLocal, productos, totalSinIVA, porcentajeIVA, telefono, correo);
                return;
            }
        }

        Toast.makeText(context, "Impresora no encontrada: " + impresora, Toast.LENGTH_SHORT).show();
    }

    private static void connectAndPrint(Context context, BluetoothDevice device, String nombreLocal, List<Producto> productos, double totalSinIVA, double porcentajeIVA, String telefono, String correo ) {
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

            // Fecha y hora actual
            String fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

            // Inicializar impresora
            outputStream.write(new byte[]{0x1B, 0x40});

            // Nombre del local (centrado y grande)
            outputStream.write(new byte[]{0x1B, 0x61, 0x01}); // Centrado
            outputStream.write(new byte[]{0x1D, 0x21, 0x11}); // Doble alto y ancho
            outputStream.write((nombreLocal + "\n").getBytes("UTF-8"));

            // Fecha y hora (centrado, normal)
            outputStream.write(new byte[]{0x1D, 0x21, 0x00});
            outputStream.write((fechaHora + "\n").getBytes("UTF-8"));

            // Línea separadora
            outputStream.write("--------------------------------\n".getBytes("UTF-8"));

            // Encabezados: Cant | Descripción | Precio
            outputStream.write(new byte[]{0x1B, 0x61, 0x00}); // Alinear izquierda
            outputStream.write(String.format("%-5s%-17s%8s\n", "Cant", "Descripcion", "Precio").getBytes("UTF-8"));

            // Lista de productos
            for (Producto p : productos) {
                String linea = String.format("%-5d%-17s%8.2f\n", p.getCantidad(), recortar(p.getNombre(), 17), p.getPrecio());
                outputStream.write(linea.getBytes("UTF-8"));
            }

            // Línea separadora final
            outputStream.write("--------------------------------\n".getBytes("UTF-8"));

            // Totales (alineados derecha)
            double totalConIVA = totalSinIVA + (totalSinIVA * (porcentajeIVA / 100.0));
            outputStream.write(String.format("%-22s%8.2f\n", "Total:", totalSinIVA).getBytes("UTF-8"));
            outputStream.write(String.format("%-22s%8.2f\n", "% IVA (" + porcentajeIVA + "%):", (totalConIVA - totalSinIVA)).getBytes("UTF-8"));
            outputStream.write(String.format("%-22s%8.2f\n", "Total con IVA:", totalConIVA).getBytes("UTF-8"));

            // Espacios antes del mensaje final
            outputStream.write("\n\n".getBytes("UTF-8"));

            // Mensaje final (centrado)
            outputStream.write(new byte[]{0x1B, 0x61, 0x01});
            outputStream.write("Gracias por su visita\n".getBytes("UTF-8"));
            outputStream.write("¡Esperamos que vuelva pronto!\n".getBytes("UTF-8"));
            outputStream.write(("Tel: " + telefono + "\n").getBytes("UTF-8"));
            outputStream.write(("Email: " + correo + "\n").getBytes("UTF-8"));

            // Cortar papel
            outputStream.write(new byte[]{0x0A, 0x0A, 0x1D, 0x56, 0x41, 0x10});

            outputStream.flush();
            Log.i(TAG, "AVISO: Impresion completada");

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



    private static String recortar(String texto, int max) {
        return texto.length() <= max ? texto : texto.substring(0, max - 1) + "…";
    }


}
