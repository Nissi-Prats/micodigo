package Servidor_Act;

import java.net.ServerSocket;
import java.net.Socket;

public class servidor {

    public static void main(String[] args) {

        int puerto = 12345;

        try (ServerSocket servidor = new ServerSocket(puerto)) {

            System.out.println("Servidor iniciado...");
            System.out.println("Esperando clientes...");

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress());

                new ManejadorCliente(cliente).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}