package proyecto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class RecuperaS {
    ArchivoPremisas premisas = new ArchivoPremisas();
    RandomAccessFile lector;
    Arbol arbol;
    Premisas[] arrayReglas;
    public RecuperaS() throws IOException, ClassNotFoundException {
        try {
            lector = new RandomAccessFile("maestroPremisa", "r");
            arbol = premisas.recuperarArbol();
//            System.out.println("olaxdxd: "+premisas.getArray(arbol.raiz,lector,0)[2].premisas);
            arrayReglas = premisas.getArray(arbol.raiz,lector,0);
//            cuentaReglas();
            listSintomas();

        } catch (FileNotFoundException e){
            e.getMessage();
        }

    }
    public String[] cuentaReglas(){
        int cont = 0;
//        String sp = "";
        String s[] = new String[100];
        String sp[] = new String[100];
        for (int i = 0; ((i < arrayReglas.length)); i++) {
            if ((arrayReglas[i] != null)){
                s[cont] = arrayReglas[cont].premisas;
                s[cont].replace("^"," ").replace("→"," ");
//                s[cont] = s[cont].replace("^"," ");
                s[cont] = s[cont].replace("→"," ");
                sp = s[cont].split(" ");
                System.out.println(s[cont]);
                cont++;
            }else {
                break;
            }
        }
        return s;
    }
    public void listSintomas(){
        String[] sintomas = cuentaReglas();
        System.out.println("Ingresa Algún(os) sintoma(s), separados por comas");
        int cont = 0;
        String[] sp = new String[100];
        String[] datosEquiparacion = new String[100];
        for (int i = 0; i < sintomas.length; i++) {
            if ((arrayReglas[i] != null)) {
                sp[i] = sintomas[i].split(" ")[0].replace("^","\t");
                datosEquiparacion[i] = sintomas[i].split(" ")[0].replace("^"," ");
                cont++;
            }else
                break;
        }
        for (int i = 0; i < arrayReglas.length; i++)
            if ((arrayReglas[i] != null))
                System.out.print(sp[i]+"\t");

        System.out.println();
        equiparacion(datosEquiparacion);
    }
    public void equiparacion(String[] datos){
        String sintomasPX = "";
        Scanner scanner = new Scanner(System.in);
        sintomasPX = scanner.next();
        String[] sp;
        int[] cc = new int[100];
        sp = sintomasPX.split(",");
        for (int i = 0; i < datos.length; i++) {
            if ((datos[i] != null)){
                for (int j = 0; j <sp.length ; j++)
                    if (sp[j]!=null)
                        if (datos[j].contains(sp[i])){
                            cc[i] = j;
                            System.out.println("Reglas en el conjunto conflicto: "+datos[j]+" Regla no."+cc[i]);

                        }
//                        System.out.println(datos[j].contains(sp[i])+"\n"+datos[j]+"\n"+sp[i]);
//                System.out.println(sp[i].charAt(2) );

            }
        }
    }
}
