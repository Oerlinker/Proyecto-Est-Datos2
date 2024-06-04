package Arboles;

import java.util.ArrayList;
import java.util.List;

public class NodoB<K extends Comparable<K>, V> {
    List<K> claves;
    List<V> valores;
    List<NodoB<K, V>> hijos;
    boolean esHoja;

    public NodoB(boolean esHoja, int orden) {
        this.claves = new ArrayList<>(orden - 1);
        this.valores = new ArrayList<>(orden - 1);
        this.hijos = new ArrayList<>(orden);
        this.esHoja = esHoja;
        for (int i = 0; i < orden; i++) {
            this.hijos.add(null);
        }
    }

    public static NodoB nodoVacio() {
        return null;
    }

    public static boolean esNodoVacio(NodoB nodo) {
        return nodo == nodoVacio();
    }

    public boolean esLleno(int orden) {
        return claves.size() == orden - 1;
    }

    public boolean esHoja() {
        return esHoja;
    }
}
