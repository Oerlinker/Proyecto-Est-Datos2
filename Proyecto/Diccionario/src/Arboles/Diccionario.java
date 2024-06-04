package Arboles;

import java.util.List;

public class Diccionario<K extends Comparable<K>, V> implements IDiccionario<K, V> {
    private IArbolBusqueda<K, V> arbol;

    public Diccionario(IArbolBusqueda<K, V> arbol) {
        this.arbol = arbol;
    }

    @Override
    public void insertar(K clave, V valor) {

        arbol.insertar(clave, valor);
    }

    @Override
    public V eliminar(K clave) {
        return arbol.eliminar(clave);
    }

    @Override
    public V buscar(K clave) {
        return arbol.buscar(clave);
    }

    @Override
    public boolean contiene(K clave) {

        return arbol.contiene(clave);
    }


    @Override
    public void vaciar() {
        arbol.vaciar();
    }

    @Override
    public int size() {
        return arbol.size();
    }

    public int sizeRecursivo() {
        return 0;
    }

    @Override
    public boolean esVacio() {
        return arbol.esArbolVacio();
    }

    @Override
    public List<K> recorridoEnInOrden() {
        return arbol.recorridoEnInOrden();
    }

    @Override
    public List<K> recorridoEnPreOrden() {

        return arbol.recorridoEnPreOrden();
    }

    @Override
    public List<K> recorridoEnPostOrden() {

        return arbol.recorridoEnPostOrden();
    }

    @Override
    public List<K> recorridoPorNiveles() {

        return arbol.recorridoPorNiveles();
    }
}
