package proyecto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Proyecto {

    public static void main(String[] args) throws IOException, FileNotFoundException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        ArchivoPremisas premisas = new ArchivoPremisas();
        int origen = 0;
        int accion = 0;
        while (accion != 6) {
            System.out.println("Escribe 1 para recuperar de manera secuencial");
            System.out.println("Escribe 2 para recuperar de manera aleatoria");
            System.out.println("Escribe 3 para actualizar un registro");
            System.out.println("Escribe 4 para borrar un registro");
            System.out.println("Escribe 5 para insertar nuevo registro");
            System.out.println("Escribe 6 para salir");

            accion = sc.nextInt();
            switch (accion) {
                case 1:
                    premisas.recuperarSecuencial();

                    break;
                case 2:
                    premisas.recuperarAleatorio();

                    break;
                case 3:
                    premisas.actualizar();

                    break;
                case 4:
                    premisas.borrar();

                    break;
                case 5:
                    premisas.insertar();

                    break;
                case 6:
                    System.out.println("Adiós");
                    break;
                default:
                    System.out.println("No existe la opción " + accion);
                    break;
            }
        }
    }
}
