
package micodigo;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author nissi
 */
public class Cliente {
    public static void main(String[] args) {

        String host = "localhost";
        int puerto = 12345;

        try {
            Socket socket = new Socket(host, puerto);

            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            Scanner teclado = new Scanner(System.in);

            System.out.println("Conectado al servidor.");
            System.out.println("Escribe un mensaje:");

            String mensaje = teclado.nextLine();

            salida.println(mensaje);

            salida.close();
            socket.close();
            teclado.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}