package Arboles;

import java.util.List;

public interface IDiccionario<K extends Comparable<K>, V> {
    void insertar(K clave, V valor);

    V eliminar(K clave);

    V buscar(K clave);

    boolean contiene(K clave);

    void vaciar();

    int size();

    int sizeRecursivo();

    boolean esVacio();

    List<K> recorridoEnInOrden();

    List<K> recorridoEnPreOrden();

    List<K> recorridoEnPostOrden();

    List<K> recorridoPorNiveles();

}
