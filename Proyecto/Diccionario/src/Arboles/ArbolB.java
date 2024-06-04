package Arboles;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ArbolB<K extends Comparable<K>, V> implements IArbolBusqueda<K, V> {
    private NodoB<K, V> raiz;
    private int orden;

    public ArbolB(int orden) {
        this.orden = orden;
        this.raiz = new NodoB<>(true, orden);
    }

    @Override
    public void insertar(K claveAInsertar, V valorAInsertar) {
        NodoB<K, V> r = raiz;
        if (r.esLleno(orden)) {
            NodoB<K, V> s = new NodoB<>(false, orden);
            raiz = s;
            s.hijos.set(0, r);
            dividir(s, 0, r);
            insertarNoLleno(s, claveAInsertar, valorAInsertar);
        } else {
            insertarNoLleno(r, claveAInsertar, valorAInsertar);
        }
    }

    private void insertarNoLleno(NodoB<K, V> x, K clave, V valor) {
        int i = x.claves.size() - 1;
        if (x.esHoja()) {
            x.claves.add(null);
            x.valores.add(null);
            while (i >= 0 && clave.compareTo(x.claves.get(i)) < 0) {
                x.claves.set(i + 1, x.claves.get(i));
                x.valores.set(i + 1, x.valores.get(i));
                i--;
            }
            x.claves.set(i + 1, clave);
            x.valores.set(i + 1, valor);
        } else {
            while (i >= 0 && clave.compareTo(x.claves.get(i)) < 0) {
                i--;
            }
            i++;
            NodoB<K, V> hijo = x.hijos.get(i);
            if (hijo.esLleno(orden)) {
                dividir(x, i, hijo);
                if (clave.compareTo(x.claves.get(i)) > 0) {
                    i++;
                }
            }
            insertarNoLleno(x.hijos.get(i), clave, valor);
        }
    }

    private void dividir(NodoB<K, V> x, int i, NodoB<K, V> y) {
        NodoB<K, V> z = new NodoB<>(y.esHoja(), orden);
        for (int j = 0; j < orden / 2 - 1; j++) {
            z.claves.add(y.claves.remove(orden / 2));
            z.valores.add(y.valores.remove(orden / 2));
        }
        if (!y.esHoja()) {
            for (int j = 0; j < orden / 2; j++) {
                z.hijos.set(j, y.hijos.remove(orden / 2));
            }
        }
        x.hijos.add(i + 1, z);
        x.claves.add(i, y.claves.remove(orden / 2 - 1));
        x.valores.add(i, y.valores.remove(orden / 2 - 1));
    }

    @Override
    public V eliminar(K claveAEliminar) {
        if (raiz == null) {
            return null;
        }
        V valorEliminado = eliminar(raiz, claveAEliminar);
        if (raiz.claves.isEmpty() && !raiz.esHoja()) {
            raiz = raiz.hijos.get(0); // Ajusta la raíz si está vacía y tiene hijos
        }
        return valorEliminado;
    }

    private V eliminar(NodoB<K, V> nodo, K claveAEliminar) {
        int indice = buscarPosicionDeClave(nodo, claveAEliminar);
        if (indice >= 0) { // La clave está en este nodo
            if (nodo.esHoja()) {
                return eliminarDeHoja(nodo, indice);
            } else {
                return eliminarDeNodoInterno(nodo, indice);
            }
        } else {
            if (nodo.esHoja()) {
                return null; // La clave no está en el árbol
            }
            boolean ultimaClave = (indice == nodo.claves.size());
            NodoB<K, V> hijoSiguiente = nodo.hijos.get(indice);
            if (hijoSiguiente.claves.size() < orden / 2) {
                llenarNodoHijo(nodo, indice);
            }
            if (ultimaClave && indice > nodo.claves.size()) {
                return eliminar(nodo.hijos.get(indice - 1), claveAEliminar);
            } else {
                return eliminar(nodo.hijos.get(indice), claveAEliminar);
            }
        }
    }

    private V eliminarDeHoja(NodoB<K, V> nodo, int indice) {
        V valorEliminado = nodo.valores.remove(indice);
        nodo.claves.remove(indice);
        return valorEliminado;
    }

    private V eliminarDeNodoInterno(NodoB<K, V> nodo, int indice) {
        K claveAEliminar = nodo.claves.get(indice);
        V valorEliminado = nodo.valores.get(indice);
        if (nodo.hijos.get(indice).claves.size() >= orden / 2) {
            NodoB<K, V> predecesor = nodo.hijos.get(indice);
            while (!predecesor.esHoja()) {
                predecesor = predecesor.hijos.get(predecesor.claves.size());
            }
            K clavePredecesora = predecesor.claves.get(predecesor.claves.size() - 1);
            V valorPredecesor = predecesor.valores.get(predecesor.valores.size() - 1);
            nodo.claves.set(indice, clavePredecesora);
            nodo.valores.set(indice, valorPredecesor);
            eliminar(nodo.hijos.get(indice), clavePredecesora);
        } else if (nodo.hijos.get(indice + 1).claves.size() >= orden / 2) {
            NodoB<K, V> sucesor = nodo.hijos.get(indice + 1);
            while (!sucesor.esHoja()) {
                sucesor = sucesor.hijos.get(0);
            }
            K claveSucesora = sucesor.claves.get(0);
            V valorSucesor = sucesor.valores.get(0);
            nodo.claves.set(indice, claveSucesora);
            nodo.valores.set(indice, valorSucesor);
            eliminar(nodo.hijos.get(indice + 1), claveSucesora);
        } else {
            fusionarNodos(nodo, indice);
            eliminar(nodo.hijos.get(indice), claveAEliminar);
        }
        return valorEliminado;
    }

    private void llenarNodoHijo(NodoB<K, V> nodo, int indice) {
        if (indice != 0 && nodo.hijos.get(indice - 1).claves.size() >= orden / 2) {
            prestarDeAnterior(nodo, indice);
        } else if (indice != nodo.claves.size() && nodo.hijos.get(indice + 1).claves.size() >= orden / 2) {
            prestarDeSiguiente(nodo, indice);
        } else {
            if (indice != nodo.claves.size()) {
                fusionarNodos(nodo, indice);
            } else {
                fusionarNodos(nodo, indice - 1);
            }
        }
    }

    private void prestarDeAnterior(NodoB<K, V> nodo, int indice) {
        NodoB<K, V> hijo = nodo.hijos.get(indice);
        NodoB<K, V> hermano = nodo.hijos.get(indice - 1);

        hijo.claves.add(0, nodo.claves.get(indice - 1));
        hijo.valores.add(0, nodo.valores.get(indice - 1));
        if (!hijo.esHoja()) {
            hijo.hijos.add(0, hermano.hijos.remove(hermano.hijos.size() - 1));
        }

        nodo.claves.set(indice - 1, hermano.claves.remove(hermano.claves.size() - 1));
        nodo.valores.set(indice - 1, hermano.valores.remove(hermano.valores.size() - 1));
    }

    private void prestarDeSiguiente(NodoB<K, V> nodo, int indice) {
        NodoB<K, V> hijo = nodo.hijos.get(indice);
        NodoB<K, V> hermano = nodo.hijos.get(indice + 1);

        hijo.claves.add(nodo.claves.get(indice));
        hijo.valores.add(nodo.valores.get(indice));
        if (!hijo.esHoja()) {
            hijo.hijos.add(hermano.hijos.remove(0));
        }

        nodo.claves.set(indice, hermano.claves.remove(0));
        nodo.valores.set(indice, hermano.valores.remove(0));
    }

    private void fusionarNodos(NodoB<K, V> nodo, int indice) {
        NodoB<K, V> hijo = nodo.hijos.get(indice);
        NodoB<K, V> hermano = nodo.hijos.get(indice + 1);

        hijo.claves.add(nodo.claves.remove(indice));
        hijo.valores.add(nodo.valores.remove(indice));

        hijo.claves.addAll(hermano.claves);
        hijo.valores.addAll(hermano.valores);
        if (!hijo.esHoja()) {
            hijo.hijos.addAll(hermano.hijos);
        }

        nodo.hijos.remove(indice + 1);
    }

    private int buscarPosicionDeClaveElim(NodoB<K, V> nodo, K clave) {
        int i = 0;
        while (i < nodo.claves.size() && clave.compareTo(nodo.claves.get(i)) > 0) {
            i++;
        }
        return (i < nodo.claves.size() && clave.compareTo(nodo.claves.get(i)) == 0) ? i : -1;
    }

    @Override
    public V buscar(K claveABuscar) {
        System.out.println("Buscando en ArbolB: " + claveABuscar);
        NodoB<K, V> nodoActual = raiz;
        while (!NodoB.esNodoVacio(nodoActual)) {
            int posicion = buscarPosicionDeClave(nodoActual, claveABuscar);
            if (posicion >= 0) {
                return nodoActual.valores.get(posicion);
            }
            nodoActual = getHijoAdecuado(nodoActual, claveABuscar);
        }
        return null;
    }

    private int buscarPosicionDeClave(NodoB<K, V> nodo, K clave) {
        for (int i = 0; i < nodo.claves.size(); i++) {
            if (nodo.claves.get(i).compareTo(clave) == 0) {
                return i;
            }
        }
        return -1;
    }

    private NodoB<K, V> getHijoAdecuado(NodoB<K, V> nodo, K clave) {
        int i;
        for (i = 0; i < nodo.claves.size(); i++) {
            if (clave.compareTo(nodo.claves.get(i)) < 0) {
                return nodo.hijos.get(i);
            }
        }
        return nodo.hijos.get(i);
    }

    @Override
    public boolean contiene(K claveABuscar) {

        NodoB<K, V> nodoActual = raiz;
        while (!NodoB.esNodoVacio(nodoActual)) {
            int posicion = buscarPosicionDeClaveCont(nodoActual, claveABuscar);
            if (posicion >= 0) {
                return true;
            }
            nodoActual = getHijoAdecuado(nodoActual, claveABuscar);
        }
        return false;
    }

    private int buscarPosicionDeClaveCont(NodoB<K, V> nodo, K clave) {
        for (int i = 0; i < nodo.claves.size(); i++) {
            if (nodo.claves.get(i).compareTo(clave) == 0) {
                return i;
            }
        }
        return -1;
    }

    private NodoB<K, V> getHijoAdecuadoCont(NodoB<K, V> nodo, K clave) {
        int i;
        for (i = 0; i < nodo.claves.size(); i++) {
            if (clave.compareTo(nodo.claves.get(i)) < 0) {
                return nodo.hijos.get(i);
            }
        }
        return nodo.hijos.get(i);
    }

    @Override
    public int size() {
        return size(raiz);
    }

    private int size(NodoB<K, V> nodo) {
        if (NodoB.esNodoVacio(nodo)) {
            return 0;
        }

        int count = nodo.claves.size();
        if (!nodo.esHoja()) {
            for (NodoB<K, V> hijo : nodo.hijos) {
                if (!NodoB.esNodoVacio(hijo)) {
                    count += size(hijo);
                }
            }
        }

        return count;
    }

    @Override
    public int sizeRecursivo() {
        return sizeRecursivo(raiz);
    }

    private int sizeRecursivo(NodoB<K, V> nodo) {
        if (NodoB.esNodoVacio(nodo)) {
            return 0;
        }

        int count = nodo.claves.size();
        if (!nodo.esHoja()) {
            for (NodoB<K, V> hijo : nodo.hijos) {
                if (!NodoB.esNodoVacio(hijo)) {
                    count += size(hijo);
                }
            }
        }

        return count;
    }

    @Override
    public int altura() {
        return altura(raiz);
    }

    private int altura(NodoB<K, V> nodo) {
        if (NodoB.esNodoVacio(nodo)) {
            return 0;
        }
        if (nodo.esHoja()) {
            return 1;
        }
        int maxAltura = 0;
        for (NodoB<K, V> hijo : nodo.hijos) {
            if (!NodoB.esNodoVacio(hijo)) {
                int alturaHijo = altura(hijo);
                if (alturaHijo > maxAltura) {
                    maxAltura = alturaHijo;
                }
            }
        }
        return 1 + maxAltura;
    }

    @Override
    public void vaciar() {
        this.raiz = new NodoB<>(true, orden);
    }

    @Override
    public boolean esArbolVacio() {
        return NodoB.esNodoVacio(raiz);
    }

    @Override
    public int nivel() {
        return altura() - 1;
    }

    @Override
    public List<K> recorridoEnInOrden() {
        List<K> resultado = new ArrayList<>();
        recorridoEnInOrden(raiz, resultado);
        return resultado;
    }

    private void recorridoEnInOrden(NodoB<K, V> nodo, List<K> resultado) {
        if (NodoB.esNodoVacio(nodo)) {
            return;
        }
        for (int i = 0; i < nodo.claves.size(); i++) {
            if (!NodoB.esNodoVacio(nodo.hijos.get(i))) {
                recorridoEnInOrden(nodo.hijos.get(i), resultado);
            }
            resultado.add(nodo.claves.get(i));
        }
        if (!NodoB.esNodoVacio(nodo.hijos.get(nodo.claves.size()))) {
            recorridoEnInOrden(nodo.hijos.get(nodo.claves.size()), resultado);
        }
    }


    @Override
    public List<K> recorridoEnPreOrden() {
        List<K> resultado = new ArrayList<>();
        recorridoEnPreOrden(raiz, resultado);
        return resultado;
    }

    private void recorridoEnPreOrden(NodoB<K, V> nodo, List<K> resultado) {
        if (NodoB.esNodoVacio(nodo)) {
            return;
        }
        for (int i = 0; i < nodo.claves.size(); i++) {
            resultado.add(nodo.claves.get(i));
            if (!NodoB.esNodoVacio(nodo.hijos.get(i))) {
                recorridoEnPreOrden(nodo.hijos.get(i), resultado);
            }
        }
        if (!NodoB.esNodoVacio(nodo.hijos.get(nodo.claves.size()))) {
            recorridoEnPreOrden(nodo.hijos.get(nodo.claves.size()), resultado);
        }
    }

    @Override
    public List<K> recorridoEnPostOrden() {
        List<K> resultado = new ArrayList<>();
        recorridoEnPostOrden(raiz, resultado);
        return resultado;
    }

    private void recorridoEnPostOrden(NodoB<K, V> nodo, List<K> resultado) {
        if (NodoB.esNodoVacio(nodo)) {
            return;
        }
        for (int i = 0; i < nodo.hijos.size(); i++) {
            if (!NodoB.esNodoVacio(nodo.hijos.get(i))) {
                recorridoEnPostOrden(nodo.hijos.get(i), resultado);
            }
        }
        resultado.addAll(nodo.claves);
    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> resultado = new ArrayList<>();
        if (NodoB.esNodoVacio(raiz)) {
            return resultado;
        }
        Queue<NodoB<K, V>> cola = new LinkedList<>();
        cola.add(raiz);
        while (!cola.isEmpty()) {
            NodoB<K, V> nodo = cola.poll();
            resultado.addAll(nodo.claves);
            for (NodoB<K, V> hijo : nodo.hijos) {
                if (!NodoB.esNodoVacio(hijo)) {
                    cola.add(hijo);
                }
            }
        }
        return resultado;
    }
}
