package com.mycompany.proyectotda;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Servidor2 {
    // Lista global para guardar los "canales de salida" de todos los clientes conectados
    private static Set<PrintWriter> escritoresDeClientes = new HashSet<>();

    public static void main(String[] args) {
        int puerto = 12345;

        System.out.println("Servidor iniciado en el puerto " + puerto);
        System.out.println("Esperando clientes...");

        try (ServerSocket servidor = new ServerSocket(puerto)) {
            // El ciclo infinito (while true) hace que el servidor NUNCA deje de esperar conexiones
            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Nuevo cliente conectado desde: " + cliente.getInetAddress());
                
                // Por cada cliente que llega, creamos un Hilo nuevo para atenderlo
                // Así el servidor principal queda libre inmediatamente para aceptar al siguiente
                ManejadorDeCliente manejador = new ManejadorDeCliente(cliente);
                manejador.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- CLASE INTERNA PARA MANEJAR HILOS ---
    // Esta clase hereda de Thread. Cada objeto de esta clase es un proceso en paralelo.
    private static class ManejadorDeCliente extends Thread {
        private Socket socket;
        private PrintWriter salida;
        private BufferedReader entrada;

        public ManejadorDeCliente(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                salida = new PrintWriter(socket.getOutputStream(), true);

                // Guardamos el canal de salida de este cliente en la lista global
                synchronized (escritoresDeClientes) {
                    escritoresDeClientes.add(salida);
                }

                String mensaje;
                // Escuchamos continuamente los mensajes de ESTE cliente
                while ((mensaje = entrada.readLine()) != null) {
                    System.out.println("Log del Servidor - Mensaje recibido: " + mensaje);
                    
                    // Cuando recibimos un mensaje, lo recorremos en la lista y lo enviamos a TODOS
                    synchronized (escritoresDeClientes) {
                        for (PrintWriter escritor : escritoresDeClientes) {
                            escritor.println("Cliente dice: " + mensaje);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Un cliente se ha desconectado abruptamente.");
            } finally {
                // Si el cliente cierra el programa, lo sacamos de la lista y cerramos su socket
                if (salida != null) {
                    synchronized (escritoresDeClientes) {
                        escritoresDeClientes.remove(salida);
                    }
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

