
package Servidor_Act;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ManejadorCliente extends Thread {

    private static List<ManejadorCliente> clientes = new ArrayList<>();

    private Socket socket;
    private PrintWriter salida;
    private BufferedReader entrada;
    private String nombre;

    public ManejadorCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);

            // Pedir nombre
            salida.println("Ingresa tu nombre:");
            nombre = entrada.readLine();

            System.out.println(nombre + " se conectó");
            clientes.add(this);

            enviarATodos(nombre + " se unió al chat", this);

            String mensaje;

            while ((mensaje = entrada.readLine()) != null) {
                String msgFinal = nombre + ": " + mensaje;
                System.out.println(msgFinal);

                enviarATodos(msgFinal, this);
            }

        } catch (Exception e) {
            System.out.println("Cliente desconectado");
        } finally {
            clientes.remove(this);
            enviarATodos(nombre + " salió del chat", this);
        }
    }

    private void enviarATodos(String mensaje, ManejadorCliente remitente) {
        for (ManejadorCliente cliente : clientes) {
            if (cliente != remitente) {
                cliente.salida.println(mensaje);
            }
        }
    }
}