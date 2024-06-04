package Arboles;

import Excepciones.OrdenInvalidoException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @param <K>
 * @param <V>
 * @author hp
 */
public class ArbolMviasBusqueda<K extends Comparable<K>, V> implements
        IArbolBusqueda<K, V> {
    protected NodoMvias<K, V> raiz;
    protected int orden;
    public static final int ORDEN_MINIMO = 3;
    public static final int POSICION_INVALIDA = -1;


    public ArbolMviasBusqueda() {
        this.orden = ArbolMviasBusqueda.ORDEN_MINIMO;
    }

    public ArbolMviasBusqueda(int orden) throws OrdenInvalidoException {
        if (orden < ArbolMviasBusqueda.ORDEN_MINIMO) {
            throw new OrdenInvalidoException();
        }
        this.orden = orden;
    }

    @Override
    public void insertar(K claveAInsertar, V valorAInsertar) {
        // Verificar si la clave o el valor son nulos
        if (claveAInsertar == null) {
            throw new IllegalArgumentException("No puede insertar claves nulas");
        }
        if (valorAInsertar == null) {
            throw new IllegalArgumentException("No puede insertar valores nulos");
        }

        // Si el árbol está vacío, insertar en la raíz
        if (this.esArbolVacio()) {
            this.raiz = new NodoMvias<>(this.orden, claveAInsertar, valorAInsertar);
            return;
        }

        NodoMvias<K, V> nodoActual = this.raiz;
        while (!NodoMvias.esNodoVacio(nodoActual)) {
            int posicionClave = buscarPosicionDeLaClave(claveAInsertar, nodoActual);
            if (posicionClave != -1) {
                // La clave ya existe, actualizamos el valor
                nodoActual.setValor(posicionClave, valorAInsertar);
                return;
            }

            if (nodoActual.esHoja()) {
                // Insertar la clave y el valor en el nodo actual
                insertarClavesYValoresOrdenados(nodoActual, claveAInsertar, valorAInsertar);
                if (nodoActual.clavesLlenas()) {
                    dividir(nodoActual); // Método para dividir el nodo si está lleno
                }
                return;
            }

            int posicionPorDondeBajar = buscarPosicionPorDondeBajar(nodoActual, claveAInsertar);
            nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
        }
    }

    private int buscarPosicionDeLaClave(K claveAInsertar, NodoMvias<K, V> nodoActual) {
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveAInsertar.compareTo(claveActual) == 0) {
                return i;
            }
        }
        return -1;
    }

    private int buscarPosicionPorDondeBajar(NodoMvias<K, V> nodoActual, K claveAInsertar) {
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveAInsertar.compareTo(claveActual) < 0) {
                return i;
            }
        }
        return nodoActual.nroDeClavesNoVacias();
    }

    private void insertarClavesYValoresOrdenados(NodoMvias<K, V> nodoActual, K claveAInsertar, V valorAInsertar) {
        int i;
        for (i = nodoActual.nroDeClavesNoVacias() - 1; i >= 0 && claveAInsertar.compareTo(nodoActual.getClave(i)) < 0; i--) {
            nodoActual.setClave(i + 1, nodoActual.getClave(i));
            nodoActual.setValor(i + 1, nodoActual.getValor(i));
        }
        nodoActual.setClave(i + 1, claveAInsertar);
        nodoActual.setValor(i + 1, valorAInsertar);
    }

    private void dividir(NodoMvias<K, V> nodoActual) {
        int medio = this.orden / 2;
        K claveDelMedio = nodoActual.getClave(medio);
        V valorDelMedio = nodoActual.getValor(medio);

        NodoMvias<K, V> nuevoNodoDerecho = new NodoMvias<>(this.orden);
        for (int i = medio + 1; i < this.orden - 1; i++) {
            nuevoNodoDerecho.setClave(i - (medio + 1), nodoActual.getClave(i));
            nuevoNodoDerecho.setValor(i - (medio + 1), nodoActual.getValor(i));
            nuevoNodoDerecho.setHijo(i - (medio + 1), nodoActual.getHijo(i));
        }
        nuevoNodoDerecho.setHijo(this.orden - 1 - (medio + 1), nodoActual.getHijo(this.orden - 1));

        NodoMvias<K, V> nuevoNodoIzquierdo = new NodoMvias<>(this.orden);
        for (int i = 0; i < medio; i++) {
            nuevoNodoIzquierdo.setClave(i, nodoActual.getClave(i));
            nuevoNodoIzquierdo.setValor(i, nodoActual.getValor(i));
            nuevoNodoIzquierdo.setHijo(i, nodoActual.getHijo(i));
        }
        nuevoNodoIzquierdo.setHijo(medio, nodoActual.getHijo(medio));

        if (NodoMvias.esNodoVacio(nodoActual.getPadre())) {
            this.raiz = new NodoMvias<>(this.orden, claveDelMedio, valorDelMedio);
            this.raiz.setHijo(0, nuevoNodoIzquierdo);
            this.raiz.setHijo(1, nuevoNodoDerecho);
        } else {
            NodoMvias<K, V> nodoPadre = nodoActual.getPadre();
            insertarClavesYValoresOrdenados(nodoPadre, claveDelMedio, valorDelMedio);

            int posicionClavePadre = buscarPosicionDeLaClave(claveDelMedio, nodoPadre);
            nodoPadre.setHijo(posicionClavePadre, nuevoNodoIzquierdo);
            nodoPadre.setHijo(posicionClavePadre + 1, nuevoNodoDerecho);

            if (nodoPadre.clavesLlenas()) {
                dividir(nodoPadre);
            }
        }
    }

    @Override
    public V eliminar(K claveAEliminar) {
     /* proximanente
       metodos pre requisitos
       * tieneHijosMasAdelanteDeLaPosicion
       * obtener sucesorInOrden
       * obtener predecesorInOrden
       * otros mas ...
     */
        return null;
    }

    private boolean tieneHijosMasAdelanteDeLaPosicion(NodoMvias<K, V> nodo, int posicion) {
        for (int i = posicion + 1; i < this.orden; i++) {
            if (!nodo.esHijoVacio(i)) {
                return true;
            }
        }
        return false;
    }

    private K obtenerSucesorInOrden(NodoMvias<K, V> nodo, int posicion) {
        NodoMvias<K, V> nodoActual = nodo.getHijo(posicion + 1);
        while (!NodoMvias.esNodoVacio(nodoActual.getHijo(0))) {
            nodoActual = nodoActual.getHijo(0);
        }
        return nodoActual.getClave(0);
    }

    private K obtenerPredecesorInOrden(NodoMvias<K, V> nodo, int posicion) {
        NodoMvias<K, V> nodoActual = nodo.getHijo(posicion);
        while (!NodoMvias.esNodoVacio(nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()))) {
            nodoActual.getHijo(nodoActual.nroDeClavesNoVacias());
        }
        return nodoActual.getClave(nodoActual.nroDeClavesNoVacias() - 1);
    }

    @Override
    // este metodo busca la clave por todo el arbol
    public V buscar(K claveABuscar) {
        // verificamos que la claveABuscar sea nula
        if (claveABuscar == null) {
            throw new IllegalArgumentException("no se puede buscar claves nulas ");
        }
        //verificamos si el arbol esta vacio
        if (this.esArbolVacio()) {
            return null;
        }
        // iniciar la busqueda desde la raiz del arbol
        NodoMvias<K, V> nodoActual = this.raiz;
        do {
            boolean cambiarNodo = false;//esto es para saber si estamos cambiando nodo
            //hacemos un for de todas las claves que tiene el nodoActual
            // y si no cambio de nodo
            for (int i = 0; i < nodoActual.nroDeClavesNoVacias() && !cambiarNodo; i++) {
                K claveDelNodoActual = nodoActual.getClave(i);
                // comparamos la claveABuscar con la claveDelNodoActual son iguales
                if (claveABuscar.compareTo(claveDelNodoActual) == 0) {
                    //retornamos el valor asociado
                    return nodoActual.getValor(i);
                }
                //si la claveABuscar es menor a la claveDelNodoActual
                if (claveABuscar.compareTo(claveDelNodoActual) < 0) {
                    // y decimos que cambiamos de nodos
                    cambiarNodo = true;
                    nodoActual = nodoActual.getHijo(i);
                }
            }// necesiita de este bucle para procesar un nodo
            // saliendo del for revisamos si no cambio de nodo
            if (!cambiarNodo) {
                // nos movemos al hijo en la posicion del nroDeClavesNoVacias
                nodoActual = nodoActual.getHijo(nodoActual.nroDeClavesNoVacias());
            }
            //repetir hasta que encuentre el valor o  el nodoActual sea nodoVacio
        } while (!NodoMvias.esNodoVacio(nodoActual));
        //retornamos esto si nunca lo encontramos
        return null;
    }

    @Override
    // este metodo revisa si ya existe la clave en el arbol o no
    public boolean contiene(K claveABuscar) {
        //llamamos al metodo buscar y comapramos si lo
        // que devuelve es diferente de null
        return this.buscar(claveABuscar) != null;
    }

    @Override
    // este metodo devuelve la cantidad de nodos en el arbol
    public int size() {
        // llamamos al metodo privado y retornamos su valor
        return size(this.raiz);
    }

    private int size(NodoMvias<K, V> nodoActual) {
        int cantidadDeNodos = 0; //creamos un contador de nodos
        if (NodoMvias.esNodoVacio(nodoActual)) {
            return cantidadDeNodos;
        }
        if (nodoActual.esHoja()) {
            cantidadDeNodos++;
            return cantidadDeNodos;
        }
        for (int i = 0; i < orden; i++) {
            cantidadDeNodos = size(nodoActual.getHijo(i));
            if (!nodoActual.esHijoVacio(i)) {
                cantidadDeNodos++;
            }
        }
        cantidadDeNodos += size(nodoActual.getHijo(orden));
        return cantidadDeNodos;
    }

    @Override
    public int sizeRecursivo() {
        return metodosizeRecursivo(this.raiz);
    }

    private int metodosizeRecursivo(NodoMvias<K, V> nodoActual) {
        if (NodoMvias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int tamaño = 1;
        for (int i = 0; i < this.orden; i++) {
            tamaño += metodosizeRecursivo(nodoActual.getHijo(i));
        }
        return tamaño;
    }

    @Override
    public int altura() {
        return 0;
    }

    @Override
    public void vaciar() {
        this.raiz = NodoMvias.nodoVacio();
    }

    public boolean esArbolVacio() {
        return NodoMvias.esNodoVacio(this.raiz);
    }


    public int nivel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // este metodo realiza un recorrido a profundidad del arbol y retorna una
    // lista con todas las claves que visito
    public List<K> recorridoEnInOrden() {
        // lista para almacenar las claves visitadas durante el recorrido
        List<K> recorrido = new ArrayList<>();
        //llamamos aun metodo privado que pondra las claves
        this.recorridoEnInOrden(this.raiz, recorrido);
        // retrnamos la lista con los valores
        return recorrido;
    }

    // este metodo privado hace un recorrido en inOrden y las pone en la lista
    private void recorridoEnInOrden(NodoMvias<K, V> nodoActual,
                                    List<K> recorrido) {
        // revisamos si el nodoActual no es un nodoVacio
        if (NodoMvias.esNodoVacio(nodoActual)) {
            return;
        }
        // hacemos un for para ir recorriendo cada nodo
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            // hacemos una llamada recursiva
            this.recorridoEnInOrden(nodoActual.getHijo(i), recorrido);
            //y guardamos en la lista las claves que tiene el nodoActual
            recorrido.add(nodoActual.getClave(i));
        }
        //saliendo del for hacemos una llamada recursiva
        this.recorridoEnInOrden
                (nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()), recorrido);
    }

    // este metodo realiza un recorrido a profundidad del arbol y retorna una
    // lista con todas las claves que visito
    public List<K> recorridoEnPreOrden() {
        // lista para almacenar las claves visitadas durante el recorrido
        List<K> recorrido = new ArrayList<>();
        //llamamos aun metodo privado
        this.recorridoEnPreOrden(this.raiz, recorrido);
        // retornamos la ista modificada
        return recorrido;
    }

    // este metodo privado hace un recorrido en preOrden y las pone en la lista
    private void recorridoEnPreOrden(NodoMvias<K, V> nodoActual,
                                     List<K> recorrido) {
        // revisamos si el nodoActual no es un nodoVacio
        if (NodoMvias.esNodoVacio(nodoActual)) {
            return;
        }
        // hacemos un for para ir recorriendo cada nodo
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            //guardamos en la lista las claves que tiene el nodoActual
            recorrido.add(nodoActual.getClave(i));
            //y hacemos una llamada recursiva
            this.recorridoEnPreOrden(nodoActual.getHijo(i), recorrido);
        }
        //saliendo del for hacemos una llamada recursiva
        this.recorridoEnPreOrden
                (nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()), recorrido);
    }


    // este metodo realiza un recorrido a profundidad del arbol y retorna una
    // lista con todas las claves que visito
    public List<K> recorridoEnPostOrden() {
        // lista para almacenar las claves visitadas durante el recorrido
        List<K> recorrido = new ArrayList<>();
        // llamamos aun metodo privado
        this.recorridoEnPostOrden(this.raiz, recorrido);
        //retornamos la lista ya modificada
        return recorrido;
    }

    private void recorridoEnPostOrden(NodoMvias<K, V> nodoActual,
                                      List<K> recorrido) {
        // revisamos si el nodoActual no es un nodoVacio
        if (NodoMvias.esNodoVacio(nodoActual)) {
            return;
        }
        //hacemos una llamada recursiva
        this.recorridoEnPostOrden(nodoActual.getHijo(0), recorrido);
        // hacemos un for para ir recorriendo cada nodo
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            // volvemos a hacer otra llamada recursiva
            this.recorridoEnPostOrden(nodoActual.getHijo(i + 1), recorrido);
            // y guardamos en la lista las claves que tiene el nodoActual
            recorrido.add(nodoActual.getClave(i));
        }
    }

    //este metodo realiza un recorrido por amplitud de todo el arbol
    // y devuelve una lista con las claves que visito
    public List<K> recorridoPorNiveles() {
        // lista para almacenar las claves visitadas durante el recorrido
        List<K> recorrido = new ArrayList<>();
        // creamos una cola para almacenar los nodos del arbol
        Queue<NodoMvias<K, V>> colaDeNodos = new LinkedList<>();
        // insertamos la raíz del árbol en la cola para comenzar el recorrido
        colaDeNodos.offer(this.raiz); //metimos el primer nodo
        do {
            // sacamos el nodo de la cola
            NodoMvias<K, V> nodoActual = colaDeNodos.poll();
            for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
                K claveDelNodoActual = nodoActual.getClave(i);
                // agregamos la clave del nodoActual a la lista de recorrido
                recorrido.add(claveDelNodoActual);// metemos la clave a la lista
                // verificamos si el nodoActual tiene hijo
                if (!nodoActual.esHijoVacio(i)) {
                    // si lo tiene lo metemos a la cola
                    colaDeNodos.offer(nodoActual.getHijo(i));
                }
            }
            // verificamos si el ultimo hijo no es vacio
            if (!nodoActual.esHijoVacio(nodoActual.nroDeClavesNoVacias())) {
                // si lo tiene o metemos a la cola
                colaDeNodos.offer(nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()));
            }
        } while (!colaDeNodos.isEmpty());// itrea hasta que la cola este vacia
        return recorrido; // retorna la lista
    }
    //NOTA: todos los recorridos a excepcion del por nivels se pueden hacer
    // recursivo
}
