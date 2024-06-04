package Arboles;

import java.util.List;

public interface IArbolBusqueda<K extends Comparable<K>, V> {
    public void insertar(K claveAInsertar, V valorAInsertar);

    public V eliminar(K claveAEliminar);

    public V buscar(K claveABuscar);

    public boolean contiene(K claveABuscar);

    public int size();

    public int sizeRecursivo();

    public int altura();

    public void vaciar();

    public boolean esArbolVacio();

    public int nivel();

    List<K> recorridoEnInOrden();

    List<K> recorridoEnPreOrden();

    List<K> recorridoEnPostOrden();

    List<K> recorridoPorNiveles();
}

