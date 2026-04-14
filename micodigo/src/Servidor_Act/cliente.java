package Servidor_Act;


import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class cliente {

    public static void main(String[] args) {

        String host = "localhost";
        int puerto = 12345;

        try (
            Socket socket = new Socket(host, puerto);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            Scanner teclado = new Scanner(System.in);
        ) {

            System.out.println("Conectado al servidor");

            // Nombre
            System.out.print(entrada.readLine() + " ");
            String nombre = teclado.nextLine();
            salida.println(nombre);

            // Hilo para recibir mensajes
            new Thread(() -> {
                try {
                    String mensaje;
                    while ((mensaje = entrada.readLine()) != null) {
                        System.out.println(mensaje);
                    }
                } catch (Exception e) {
                    System.out.println("Conexión cerrada");
                }
            }).start();

            // Enviar mensajes
            while (true) {
                String mensaje = teclado.nextLine();
                salida.println(mensaje);
            }

        } catch (Exception e) {
            System.out.println("No se pudo conectar");
        }
    }
}