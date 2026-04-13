
package com.mycompany.proyectotda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente2 {
    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 12345;

        try {
            Socket socket = new Socket(host, puerto);
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner teclado = new Scanner(System.in);

            System.out.println("Conectado al servidor con éxito.");
            System.out.println("Ya puedes empezar a escribir mensajes:");

            // --- HILO DE ESCUCHA ---
            // Este hilo correrá en paralelo SOLO para imprimir los mensajes que lleguen del servidor
            Thread hiloEscucha = new Thread(new Runnable() {
                public void run() {
                    try {
                        String mensajeServidor;
                        while ((mensajeServidor = entrada.readLine()) != null) {
                            System.out.println(mensajeServidor);
                        }
                    } catch (IOException e) {
                        System.out.println("Conexión cerrada por el servidor.");
                    }
                }
            });
            hiloEscucha.start(); // Iniciamos el proceso paralelo

            // --- HILO PRINCIPAL (ENVÍO) ---
            // Mientras el hilo de arriba escucha, este bucle infinito se queda leyendo tu teclado
            while (true) {
                String mensaje = teclado.nextLine();
                salida.println(mensaje);
                
                // Una forma de salir limpiamente
                if (mensaje.equalsIgnoreCase("salir")) {
                    break;
                }
            }

            // Cerramos recursos si el usuario escribe "salir"
            salida.close();
            entrada.close();
            socket.close();
            teclado.close();

        } catch (Exception e) {
            System.out.println("No se pudo conectar al servidor. Asegúrate de que esté encendido.");
        }
    }
}