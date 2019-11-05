package proyecto;
class Arbol {
    Nodo raiz = null;
    void borrar(int llave)
    {
        Nodo Rec, Rec2, Ant, Ant2;
        if (raiz != null)
            if ( buscar(llave) != -1 )
            {
                Rec = raiz;
                Ant = Rec;
                while (Rec.llave != llave)
                {
                    Ant = Rec;
                    if (Rec.llave > llave)
                        Rec = Rec.izquierda;
                    else 
                        Rec = Rec.derecha;
                }
                if (Rec != Ant)
                    if (Rec.derecha == null && Rec.izquierda == null)
                        if (Rec.llave < Ant.llave)
                            Ant.izquierda = null;
                        else 
                            Ant.derecha = null;
                    else
                        if (Rec.derecha == null && Rec.izquierda != null)
                            if (Rec.llave < Ant.llave)
                                Ant.izquierda = Rec.izquierda;
                            else 
                                Ant.derecha = Rec.izquierda;
                        else 
                            if (Rec.derecha != null && Rec.izquierda == null)
                                if (Rec.llave < Ant.llave)
                                    Ant.izquierda = Rec.derecha;
                                else 
                                    Ant.derecha = Rec.derecha;
                if (Rec.derecha != null && Rec.izquierda != null)
                {
                    Rec2 = Rec.derecha;
                    Ant = Rec;
                    Ant2 = Rec2;
                    while (Rec2.izquierda != null)
                    {
                        Ant2 = Rec2;
                        Rec2 = Rec.izquierda;
                    }
                    if (Rec2 == Ant2)
                    {
                        Rec.derecha = Rec2.derecha;
                        Rec.llave = Rec2.llave;
                    }
                    else 
                    {
                        Rec.llave = Rec2.llave;
                        Ant2.izquierda = Rec2.derecha;
                    }
                }                
            }
            else 
                if (raiz.izquierda == null && raiz.derecha ==null)
                    raiz = null;
                else 
                    if (raiz.izquierda != null && raiz.derecha == null)
                        raiz = raiz.izquierda;
                    else 
                        if (raiz.izquierda == null && raiz.derecha != null)
                            raiz = raiz.derecha;
                        else 
                            if (raiz.izquierda != null && raiz.derecha!= null)
                            {
                                Rec = raiz;
                                Rec2 = Rec.derecha;
                                Ant = Rec;
                                Ant2 = Rec2;
                                while (Rec2.izquierda != null)
                                {
                                    Ant2 = Rec2;
                                    Rec2 = Rec.izquierda;
                                }
                                if (Rec2 == Ant2)
                                {
                                    Rec.derecha = Rec2.derecha;
                                    Rec.llave = Rec2.llave;
                                }
                                else 
                                {
                                    Rec.llave = Rec2.llave;
                                    Ant2.izquierda = Rec2.derecha;
                                }
                            }
    }
    int buscar(int llave){
        int direccion = -1;
        Nodo recorre, anterior;
        if(raiz != null){
            recorre = raiz;
            anterior = raiz;
            while(anterior.llave != llave && recorre != null){
                anterior = recorre;
                if(recorre.llave > llave){
                    recorre = recorre.izquierda;
                }
                else{
                    recorre = recorre.derecha;
                }
            }
            if(anterior.llave == llave){
                direccion = anterior.direccion;
            }
        }
        return direccion;
    }
    void insertar(int llave, int direccion){
        Nodo nuevo, anterior = null, recorre;
        if(raiz == null){
            raiz = new Nodo(llave, direccion);
        }
        else{
            nuevo = new Nodo(llave, direccion);
            recorre = raiz;
            while(recorre != null){
                anterior = recorre;
                if(recorre.llave > nuevo.llave){
                    recorre = recorre.izquierda;
                }
                else{
                    recorre = recorre.derecha;
                }
            }
            if(anterior.llave > nuevo.llave){
                anterior.izquierda = nuevo;
            }
            else{
                anterior.derecha = nuevo;
            }
        }
    }
}
