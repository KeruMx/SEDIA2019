    package proyecto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class ArchivoPremisas {

    private final int registerLength;
    private Arbol arbol;
    private int direccionSiguiente, direccionActual, borrados, desbordados, ordenados, direccionReorganizados;
    private Indice indice;
    private Premisas premisas;
    Premisas[] premisasRecuperacion = new Premisas[100];

    ArchivoPremisas() throws IOException, FileNotFoundException, ClassNotFoundException {
        //registerLength = 42;
        registerLength = 204;
        premisas = new Premisas();
        indice = new Indice();
        recuperarArbol();
        recuperarControl();
    }

    public void actualizar() throws FileNotFoundException, IOException {
        RandomAccessFile escritor;
        Scanner sc = new Scanner(System.in);
        StringBuffer buffer;
        String nombre;
        System.out.println("Llave a actualizar:");
        indice.llave = premisas.llave = sc.nextInt();
        indice.direccion = arbol.buscar(indice.llave);
        if (indice.direccion == -1) {
            System.out.println("No existe un registro con la llave " + indice.llave);
        } else {
            System.out.println("Premisa:");
            nombre = sc.nextLine();
            nombre = sc.nextLine();
            buffer = new StringBuffer(nombre);
            buffer.setLength(100); //linea a cambiar para la longitud de la premisa
            premisas.premisas = buffer.toString();

            escritor = new RandomAccessFile("maestroPremisa", "rw");
            escritor.seek(indice.direccion * registerLength);
            escribePremisas(premisas, escritor);
            escritor.close();
        }
    }

    public void borrar() throws FileNotFoundException, IOException, ClassNotFoundException {
        RandomAccessFile escritor;
        Scanner sc = new Scanner(System.in);
        System.out.println("Llave a borrar:");
        indice.llave = premisas.llave = sc.nextInt();
        indice.direccion = arbol.buscar(indice.llave);
        System.out.println(indice.direccion);
        if (indice.direccion == -1) {
            System.out.println("No existe un registro con la llave " + indice.llave);
        } else {
            premisas.llave = 0;
            premisas.premisas = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789ABCDEFGHIJKLMNOPQRSTU"; //linea a cambiar esta relaciona con la longitd de las premisas que es de 15

            escritor = new RandomAccessFile("maestroPremisa", "rw");
            escritor.seek(indice.direccion * registerLength);
            escribePremisas(premisas, escritor);
            escritor.close();
            marcarIndice(indice.llave);
            arbol.borrar(indice.llave);
            borrados++;
            recuperarSecuencial(0);
            reescribirControl();
        }
    }

    private void escribeIndice(Indice aEscribir, RandomAccessFile raf) throws IOException {
        raf.writeInt(aEscribir.llave);
        raf.writeInt(aEscribir.direccion);
    }

    private void escribePremisas(Premisas aEscribir, RandomAccessFile raf) throws IOException {
        raf.writeInt(aEscribir.llave);
        raf.writeChars(aEscribir.premisas);

    }

    public void insertar() throws IOException {
        Scanner sc = new Scanner(System.in);
        boolean existe = true;
        String nombre;
        RandomAccessFile lector = null, escritorIndice, escritor;
        StringBuffer buffer = null;
        System.out.println("Llave :");
        indice.llave = premisas.llave = sc.nextInt();
        indice.direccion = direccionSiguiente;
        if (arbol.buscar(premisas.llave) != -1) {
            System.out.println("La llave ya existe");
        } else {
            System.out.println("Premisa:");
            nombre = sc.nextLine();
            nombre = sc.nextLine();
            buffer = new StringBuffer(nombre);
            buffer.setLength(100); ///linea cambiar a longitud 100
            premisas.premisas = buffer.toString();

            try {
                lector = new RandomAccessFile("maestroPremisa", "r");
            } catch (FileNotFoundException e) {
                existe = false;
            }
            if (existe) {
                lector.close();
                escritor = new RandomAccessFile("maestroPremisa", "rw");
                escritorIndice = new RandomAccessFile("indicePremisa", "rw");
                escritor.seek(escritor.length());
                escribePremisas(premisas, escritor);
                escritorIndice.seek(escritorIndice.length());
                escribeIndice(indice, escritorIndice);
                escritor.close();
                escritorIndice.close();
                desbordados++;
            } else {
                escritor = new RandomAccessFile("maestroPremisa", "rw");
                escritorIndice = new RandomAccessFile("indicePremisa", "rw");
                escribePremisas(premisas, escritor);
                escribeIndice(indice, escritorIndice);
                escritor.close();
                escritorIndice.close();
                ordenados++;
            }
            arbol.insertar(premisas.llave, direccionSiguiente);
            direccionSiguiente++;
            reescribirControl();
        }
    }

    private void marcarIndice(int llave) throws IOException, ClassNotFoundException {
        boolean existe = true;
        RandomAccessFile lector = null;
        long apuntadorFinal, ultimoApuntador;
        boolean marcado = false;
        try {
            lector = new RandomAccessFile("indicePremisa", "rw");
        } catch (FileNotFoundException e) {
            existe = false;
        }
        if (existe) {
            apuntadorFinal = lector.length();
            while ((ultimoApuntador = lector.getFilePointer()) != apuntadorFinal && !marcado) {
                indice = recuperaIndice(lector);
                if (indice.llave == llave) {
                    lector.seek(ultimoApuntador);
                    indice.direccion = -1;
                    escribeIndice(indice, lector);
                }
            }
            lector.close();
        }
    }

    private Premisas[] muestraPremisa(Premisas l,int op) {
            System.out.println(l.premisas);
            return premisasRecuperacion;

    }

    private void recorreArbol(Nodo nodo, RandomAccessFile raf,int op) throws IOException {
        if (nodo.izquierda != null) {
            recorreArbol(nodo.izquierda, raf,op);
        }
        if (direccionActual != nodo.direccion) {
            raf.seek(nodo.direccion * registerLength);
            direccionActual = nodo.direccion;
        }
        premisas = recuperaPremisa(raf);
        muestraPremisa(premisas,0);
        direccionActual++;
        if (nodo.derecha != null) {
            recorreArbol(nodo.derecha, raf,op);
        }
    }
    Premisas[] recorreArbolArray(Nodo nodo, RandomAccessFile raf,int op) throws IOException {
        if (nodo.izquierda != null) {
            recorreArbolArray(nodo.izquierda, raf,op=op+1);
        }
        if (direccionActual != nodo.direccion) {
            raf.seek(nodo.direccion * registerLength);
            direccionActual = nodo.direccion;
        }
        premisasRecuperacion[op] = recuperaPremisa(raf);
        direccionActual++;
        if (nodo.derecha != null) {
            recorreArbolArray(nodo.derecha, raf,op=op+1);
        }
        return premisasRecuperacion;
    }
    Premisas[] getArray(Nodo nodo, RandomAccessFile raf,int op) throws IOException {
        if (nodo.izquierda != null) {
            recorreArbolArray(nodo.izquierda, raf,op=op+1);
        }
        if (direccionActual != nodo.direccion) {
            raf.seek(nodo.direccion * registerLength);
            direccionActual = nodo.direccion;
        }
//        premisas = recuperaPremisa(raf);
        premisasRecuperacion[op] = recuperaPremisa(raf);
        direccionActual++;
        if (nodo.derecha != null) {
            recorreArbolArray(nodo.derecha, raf,op=op+1);
        }
        return premisasRecuperacion;
    }

    private void recorreArbolReestructurar(Nodo nodo, RandomAccessFile lector, RandomAccessFile escritor) throws IOException {
        if (nodo.izquierda != null) {
            recorreArbolReestructurar(nodo.izquierda, lector, escritor);
        }
        System.out.println(nodo.llave + " " + nodo.direccion);
        if (direccionActual != nodo.direccion) {
            lector.seek(nodo.direccion * registerLength);
            direccionActual = nodo.direccion;
        }
        premisas = recuperaPremisa(lector);
        escribePremisas(premisas, escritor);
        direccionActual++;
        nodo.direccion = direccionReorganizados;
        direccionReorganizados++;
        if (nodo.derecha != null) {
            recorreArbolReestructurar(nodo.derecha, lector, escritor);
        }
    }

    public void recuperarAleatorio() throws FileNotFoundException, IOException {
        RandomAccessFile lector = null;
        Scanner sc = new Scanner(System.in);
        System.out.println("Llave a recuperar:");
        indice.llave = sc.nextInt();
        indice.direccion = arbol.buscar(indice.llave);
        if (indice.direccion == -1) {
            System.out.println("No existe un registro con la llave " + indice.llave);
        } else {
            lector = new RandomAccessFile("maestroPremisa", "r");
            lector.seek(indice.direccion * registerLength);
            premisas = recuperaPremisa(lector);
            muestraPremisa(premisas,0);
            lector.close();
        }
    }

    Arbol recuperarArbol() throws IOException {
        boolean existe = true;
        RandomAccessFile lector = null;
        arbol = new Arbol();
        try {
            lector = new RandomAccessFile("indicePremisa", "r");
        } catch (FileNotFoundException e) {
            existe = false;
        }
        if (existe) {
            while (lector.getFilePointer() != lector.length()) {
                indice = recuperaIndice(lector);
                if (indice.direccion != -1) {
                    arbol.insertar(indice.llave, indice.direccion);
                }
            }
            lector.close();
        }
        return arbol;
    }

    private void recuperarControl() throws IOException {
        boolean existe = true;
        RandomAccessFile lector = null;
        try {
            lector = new RandomAccessFile("controlPremisa", "r");
        } catch (FileNotFoundException e) {
            existe = false;
        }
        if (existe) {
            direccionSiguiente = lector.readInt();
            borrados = lector.readInt();
            desbordados = lector.readInt();
            ordenados = lector.readInt();
            lector.close();
        } else {
            direccionSiguiente = borrados = desbordados = ordenados = 0;
        }
    }

    private Indice recuperaIndice(RandomAccessFile raf) throws IOException {
        Indice i = new Indice();
        i.llave = raf.readInt();
        i.direccion = raf.readInt();
        return i;
    }

    Premisas recuperaPremisa(RandomAccessFile lector) throws IOException {
        int c;
        char nombreT[] = new char[100];
        Premisas l = new Premisas();
        l.llave = lector.readInt();
        for (c = 0; c < nombreT.length; c++) {
            nombreT[c] = lector.readChar();
        }
        l.premisas = new String(nombreT).replace('\0', ' ');

        return l;
    }

    public void recuperarSecuencial(int op) throws FileNotFoundException, IOException {
        boolean existe = true;
        RandomAccessFile lector = null;
        direccionActual = 0;
        try {
            lector = new RandomAccessFile("maestroPremisa", "r");
        } catch (FileNotFoundException e) {
            existe = false;
        }
        if (existe) {
            if (op==0){
                recorreArbol(arbol.raiz, lector,0);
            }
            else
            {
                recorreArbol(arbol.raiz,lector,1);
            }
            lector.close();
        }
    }

    private void reescribirControl() throws IOException {
        boolean existe = true;
        RandomAccessFile lector = null;
        try {
            lector = new RandomAccessFile("controlPremisa", "rw");
        } catch (FileNotFoundException e) {
            existe = false;
        }
        if (existe) {
            lector.writeInt(direccionSiguiente);
            lector.writeInt(borrados);
            lector.writeInt(desbordados);
            lector.writeInt(ordenados);
            lector.close();
        }
        //System.out.println(direccionSiguiente);
        //System.out.println(borrados);
        // System.out.println(desbordados);
        // System.out.println(ordenados);
        if (0.4 * ordenados < borrados + desbordados) {
            reestructurar();
        }
    }

    private void reestructurar() throws IOException {
        boolean existe = true;
        RandomAccessFile lector = null, escritor;
        File file, newName;
        direccionReorganizados = direccionActual = 0;
        try {
            lector = new RandomAccessFile("maestroPremisa", "r");
        } catch (FileNotFoundException e) {
            existe = false;
        }
        if (existe) {
            escritor = new RandomAccessFile("tmp", "rw");
            recorreArbolReestructurar(arbol.raiz, lector, escritor);
            lector.close();
            escritor.close();
            file = new File("tmp");
            newName = new File("maestroPremisa");
            newName.delete();
            file.renameTo(newName);

            lector = new RandomAccessFile("indicePremisa", "rw");
            escritor = new RandomAccessFile("tmp", "rw");
            while (lector.getFilePointer() != lector.length()) {
                indice = recuperaIndice(lector);
                indice.direccion = arbol.buscar(indice.llave);
                if (indice.direccion != -1) {
                    escribeIndice(indice, escritor);
                }
            }
            lector.close();
            escritor.close();
            file = new File("tmp");
            newName = new File("indicePremisa");
            newName.delete();
            file.renameTo(newName);
        }
        ordenados = ordenados + desbordados - borrados;
        direccionSiguiente = ordenados;
        desbordados = borrados = 0;
        reescribirControl();
    }
}
