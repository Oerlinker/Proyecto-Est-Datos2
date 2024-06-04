package Arboles;

public class DiccionarioFabrica {
    public static <K extends Comparable<K>, V> IDiccionario<K, V> crearDiccionario(String tipo) {
        switch (tipo.toLowerCase()) {
            case "binario":
                return new Diccionario<>(new ArbolBinarioBusqueda<K, V>());
            case "mvias":
                return new Diccionario<>(new ArbolMviasBusqueda<K, V>());
            case "avl":
                return new Diccionario<>(new AVL<K, V>());
            case "b":
                return new Diccionario<>(new ArbolB<K,V>(4));
            default:
                throw new IllegalArgumentException("Tipo de árbol no soportado: " + tipo);
        }
    }
}
