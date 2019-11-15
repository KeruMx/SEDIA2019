package proyecto;

public class Nodo {

    int llave, direccion;
    Nodo derecha, izquierda;

    Nodo(int llave, int direccion, Nodo izquierda, Nodo derecha) {
        this.llave = llave;
        this.direccion = direccion;
        this.izquierda = izquierda;
        this.derecha = derecha;
    }
    Nodo(int llave, int direccion) {
        this.llave = llave;
        this.direccion = direccion;
        this.izquierda = this.derecha = null;
    }
}
