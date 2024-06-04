package Arboles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class FDiccionario extends JFrame {
    private IDiccionario<String, String> diccionario;
    private JComboBox<String> comboBoxArboles;
    private JTextField txtPalabra;
    private JTextField txtDefinicion;
    private JTextArea textArea;
    private JButton btnInsertar;
    private JButton btnEliminar;
    private JButton btnBuscar;
    private JButton btnContiene;
    private JButton btnLimpiar;
    private JButton btnVaciar;
    private JButton btnContarPalabras;
    private static final String FILE_NAME = "diccionario.dat";

    public FDiccionario() {
        initComponents();
    }

    private void initComponents() {
        // Configurar el JFrame
        setTitle("Diccionario de Árboles");
        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de selección del tipo de árbol
        JPanel panelArbol = new JPanel();
        panelArbol.setLayout(new GridLayout(1, 3));
        panelArbol.add(new JLabel("Seleccionar tipo de árbol:"));

        comboBoxArboles = new JComboBox<>(new String[]{"binario", "avl", "mvias","b"});
        panelArbol.add(comboBoxArboles);

        JButton btnSeleccionar = new JButton("Seleccionar");
        btnSeleccionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarArbol();
            }
        });
        panelArbol.add(btnSeleccionar);

        add(panelArbol, BorderLayout.NORTH);

        // Panel de entrada
        JPanel panelEntrada = new JPanel();
        panelEntrada.setLayout(new GridLayout(2, 2));

        panelEntrada.add(new JLabel("Palabra:"));
        txtPalabra = new JTextField();
        panelEntrada.add(txtPalabra);

        panelEntrada.add(new JLabel("Definición:"));
        txtDefinicion = new JTextField();
        panelEntrada.add(txtDefinicion);

        add(panelEntrada, BorderLayout.CENTER);

        // Panel para los botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(1, 7));

        btnInsertar = new JButton("Insertar");
        btnInsertar.setEnabled(false);
        btnInsertar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertar();
            }
        });
        panelBotones.add(btnInsertar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminar();
            }
        });
        panelBotones.add(btnEliminar);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setEnabled(false);
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscar();
            }
        });
        panelBotones.add(btnBuscar);

        btnContiene = new JButton("Contiene");
        btnContiene.setEnabled(false);
        btnContiene.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contiene();
            }
        });
        panelBotones.add(btnContiene);

        btnVaciar = new JButton("Vaciar");
        btnVaciar.setEnabled(false);
        btnVaciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vaciar();
            }
        });
        panelBotones.add(btnVaciar);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setEnabled(true);
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiar();
            }
        });
        panelBotones.add(btnLimpiar);

        btnContarPalabras = new JButton("Contar Palabras");
        btnContarPalabras.setEnabled(false);
        btnContarPalabras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contarPalabras();
            }
        });
        panelBotones.add(btnContarPalabras);

        add(panelBotones, BorderLayout.SOUTH);

        // Área de texto para mostrar resultados con JScrollPane
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(800, 200));
        add(scrollPane, BorderLayout.EAST);

        // Ajustar el tamaño de la ventana para que todos los componentes se ajusten correctamente
        pack();
    }

    private void seleccionarArbol() {
        String tipoArbol = (String) comboBoxArboles.getSelectedItem();
        diccionario = DiccionarioFabrica.crearDiccionario(tipoArbol);
        diccionario.insertar(normalizarPalabra("gato"), "Animal mamífero de la familia de los felinos.");
        diccionario.insertar(normalizarPalabra("perro"), "Animal mamífero de la familia de los cánidos.");
        diccionario.insertar(normalizarPalabra("tigre"), "Mamífero carnívoro félido asiático");
        diccionario.insertar(normalizarPalabra("abstracción"), "Proceso de reducir la complejidad al enfocarse en las características esenciales en lugar de los detalles específicos.");
        diccionario.insertar(normalizarPalabra("algoritmo"), "Conjunto de instrucciones paso a paso para realizar una tarea específica.");
        diccionario.insertar(normalizarPalabra("binario"), "Sistema numérico de base 2, utilizando solo los dígitos 0 y 1.");
        diccionario.insertar(normalizarPalabra("buffer"), "Área de memoria utilizada para almacenar datos temporalmente.");
        diccionario.insertar(normalizarPalabra("compilador"), "Programa que traduce el código fuente de un lenguaje de programación a un lenguaje de máquina.");
        diccionario.insertar(normalizarPalabra("depuración"), "Proceso de encontrar y corregir errores en un programa de computadora.");
        diccionario.insertar(normalizarPalabra("encriptación"), "Proceso de convertir información en un código para prevenir el acceso no autorizado.");
        diccionario.insertar(normalizarPalabra("framework"), "Conjunto de herramientas y bibliotecas que facilitan el desarrollo de aplicaciones.");
        diccionario.insertar(normalizarPalabra("heap"), "Área de memoria dinámica utilizada para la asignación de memoria en tiempo de ejecución.");
        diccionario.insertar(normalizarPalabra("indice"), "Estructura de datos que mejora la velocidad de acceso a los datos en una base de datos.");
        diccionario.insertar(normalizarPalabra("java"), "Lenguaje de programación de alto nivel utilizado para desarrollar aplicaciones multiplataforma.");
        diccionario.insertar(normalizarPalabra("kernel"), "Núcleo del sistema operativo que controla todas las operaciones del sistema.");
        diccionario.insertar(normalizarPalabra("librería"), "Colección de funciones y procedimientos reutilizables en programación.");
        diccionario.insertar(normalizarPalabra("microprocesador"), "Unidad central de procesamiento de una computadora en un solo chip integrado.");
        diccionario.insertar(normalizarPalabra("nodo"), "Unidad fundamental en estructuras de datos como listas enlazadas, árboles y grafos.");
        diccionario.insertar(normalizarPalabra("objeto"), "Entidad en programación orientada a objetos que contiene datos y métodos.");
        diccionario.insertar(normalizarPalabra("polimorfismo"), "Capacidad de una función o un método para comportarse de diferentes maneras según el contexto.");
        diccionario.insertar(normalizarPalabra("query"), "Instrucción utilizada para consultar y manipular datos en una base de datos.");
        diccionario.insertar(normalizarPalabra("recursión"), "Técnica en la que una función se llama a sí misma para resolver un problema.");
        diccionario.insertar(normalizarPalabra("stack"), "Estructura de datos que sigue el principio LIFO (Last In, First Out).");
        diccionario.insertar(normalizarPalabra("token"), "Unidad léxica en programación que representa una secuencia de caracteres con significado.");
        diccionario.insertar(normalizarPalabra("unicode"), "Estándar de codificación de caracteres que permite representar texto en cualquier idioma.");
        diccionario.insertar(normalizarPalabra("variable"), "Elemento en programación que almacena un valor y cuyo contenido puede cambiar durante la ejecución del programa.");
        diccionario.insertar(normalizarPalabra("websocket"), "Protocolo de comunicación que permite una comunicación interactiva entre un navegador y un servidor.");
        diccionario.insertar(normalizarPalabra("xml"), "Lenguaje de marcado utilizado para almacenar y transportar datos.");
        diccionario.insertar(normalizarPalabra("yaml"), "Formato de serialización de datos legible por humanos y utilizado para la configuración.");
        diccionario.insertar(normalizarPalabra("zocalo"), "Punto de conexión en redes de computadoras que permite la comunicación entre dispositivos.");
        diccionario.insertar(normalizarPalabra("api"), "Interfaz de programación de aplicaciones que permite la interacción entre diferentes programas.");
        diccionario.insertar(normalizarPalabra("bytecode"), "Código intermedio generado por un compilador que puede ser ejecutado por una máquina virtual.");
        diccionario.insertar(normalizarPalabra("cache"), "Almacenamiento temporal de datos para acelerar el acceso a la información frecuente.");

        cargarDiccionario();

        // Habilitar los botones de insertar, eliminar, buscar, contiene, vaciar y contar palabras
        btnInsertar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnBuscar.setEnabled(true);
        btnContiene.setEnabled(true);
        btnVaciar.setEnabled(true);
        btnContarPalabras.setEnabled(true);

        textArea.append("Árbol seleccionado: " + tipoArbol + "\n");
    }

    private void insertar() {
        String palabra = normalizarPalabra(txtPalabra.getText().toLowerCase());
        String definicion = txtDefinicion.getText();
        diccionario.insertar(palabra, definicion);
        textArea.append("Insertado: Palabra=" + palabra + ", Definición=" + definicion + "\n");
        guardarDiccionario();
    }

    private void eliminar() {
        String palabra = normalizarPalabra(txtPalabra.getText().toLowerCase());
        String definicion = diccionario.eliminar(palabra);
        if (definicion != null) {
            textArea.append("Eliminado: Palabra=" + palabra + ", Definición=" + definicion + "\n");
        } else {
            textArea.append("Palabra no encontrada: " + palabra + "\n");
        }
        guardarDiccionario();
    }

    private void buscar() {
        String palabra = normalizarPalabra(txtPalabra.getText().toLowerCase());
        if (diccionario.esVacio()) {
            textArea.append("El árbol está vacío.\n");
        } else if (!diccionario.contiene(palabra)) {
            textArea.append("Palabra no encontrada: " + palabra + "\n");
        } else {
            String definicion = diccionario.buscar(palabra);
            textArea.append("Encontrado: Palabra=" + palabra + ", Definición=" + definicion + "\n");
        }
    }

    private void contiene() {
        String palabra = normalizarPalabra(txtPalabra.getText().toLowerCase());
        if (diccionario.contiene(palabra)) {
            textArea.append("La palabra \"" + palabra + "\" se encuentra en el diccionario.\n");
        } else {
            textArea.append("La palabra \"" + palabra + "\" no se encuentra en el diccionario.\n");
        }
    }

    private void limpiar() {
        txtPalabra.setText("");
        txtDefinicion.setText("");
    }

    private void vaciar() {
        diccionario.vaciar();
        textArea.append("El diccionario ha sido vaciado.\n");
        guardarDiccionario();
    }

    private void contarPalabras() {
        int cantidad = diccionario.size();
        textArea.append("El diccionario contiene " + cantidad + " palabras.\n");
    }

    private void guardarDiccionario() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            Map<String, String> map = new HashMap<>();
            for (String palabra : diccionario.recorridoPorNiveles()) {
                map.put(palabra, diccionario.buscar(palabra));
            }
            oos.writeObject(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarDiccionario() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                Map<String, String> map = (Map<String, String>) ois.readObject();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    diccionario.insertar(entry.getKey(), entry.getValue());
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private String normalizarPalabra(String palabra) {
        String normalized = Normalizer.normalize(palabra, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

    public static void main(String[] args) {
        // Crear y mostrar la GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FDiccionario().setVisible(true);
            }
        });
    }
}
