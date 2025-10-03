package ventanas;

import java.awt.*;
import java.io.File;
import javax.swing.*;

public class VentanaDeDibujo extends JFrame {

    private PanelDeDibujo panelDeDibujo;

    public VentanaDeDibujo(String title) throws HeadlessException {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);

        panelDeDibujo = new PanelDeDibujo();

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        // --- FIGURAS ---
        String[] figuras = {"Rectángulo", "Línea", "Triángulo", "Círculo", "Pentágono", "Hexágono"};
        JComboBox<String> comboFiguras = new JComboBox<>(figuras);
        comboFiguras.setToolTipText("Seleccionar figura");
        comboFiguras.addActionListener(e -> {
            String seleccion = (String) comboFiguras.getSelectedItem();
            switch (seleccion) {
                case "Rectángulo" ->
                    panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.RECTANGULO);
                case "Línea" ->
                    panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.LINEA);
                case "Triángulo" ->
                    panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.TRIANGULO);
                case "Círculo" ->
                    panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.CIRCULO);
                case "Pentágono" ->
                    panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.PENTAGONO);
                case "Hexágono" ->
                    panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.HEXAGONO);
            }
        });
        toolbar.add(new JLabel("Figuras: "));
        toolbar.add(comboFiguras);
        toolbar.addSeparator();

        // --- PINCELES ---
        String[] pinceles = {"Lápiz fino", "Pincel medio", "Pincel grueso"};
        JComboBox<String> comboPinceles = new JComboBox<>(pinceles);
        comboPinceles.setToolTipText("Seleccionar tipo de pincel");
        comboPinceles.addActionListener(e -> {
            String seleccion = (String) comboPinceles.getSelectedItem();
            panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.LIBRE);
            switch (seleccion) {
                case "Lápiz fino" ->
                    panelDeDibujo.setGrosor(2);
                case "Pincel medio" ->
                    panelDeDibujo.setGrosor(6);
                case "Pincel grueso" ->
                    panelDeDibujo.setGrosor(12);
            }
        });
        toolbar.add(new JLabel("Pinceles: "));
        toolbar.add(comboPinceles);
        toolbar.addSeparator();

        // --- COLORES ---
        JButton btnColorLinea = new JButton("Color Línea");
        btnColorLinea.addActionListener(e -> {
            Color nuevo = JColorChooser.showDialog(this, "Elige color de línea", Color.BLACK);
            if (nuevo != null) {
                panelDeDibujo.setColorLinea(nuevo);
            }
        });

        JButton btnColorRelleno = new JButton("Color Relleno");
        btnColorRelleno.addActionListener(e -> {
            Color nuevo = JColorChooser.showDialog(this, "Elige color de relleno", Color.WHITE);
            if (nuevo != null) {
                panelDeDibujo.setColorRelleno(nuevo);
            }
        });

        toolbar.add(btnColorLinea);
        toolbar.add(btnColorRelleno);
        toolbar.addSeparator();

        // --- ACCIONES ---
        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> panelDeDibujo.limpiar());

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar dibujo como...");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG Image", "png"));
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JPG Image", "jpg"));
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File archivo = fileChooser.getSelectedFile();
                String formato = "png";
                String nombre = archivo.getName().toLowerCase();
                if (nombre.endsWith(".jpg") || nombre.endsWith(".jpeg")) {
                    formato = "jpg";
                } else if (!nombre.endsWith(".png")) {
                    archivo = new File(archivo.getAbsolutePath() + ".png");
                }
                panelDeDibujo.guardarComoImagen(archivo.getAbsolutePath(), formato);
            }
        });

        JButton btnBorrador = new JButton("Borrador");
        btnBorrador.addActionListener(e -> panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.BORRADOR));

        toolbar.add(btnLimpiar);
        toolbar.add(btnGuardar);
        toolbar.add(btnBorrador);

        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        add(panelDeDibujo, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(()
                -> new VentanaDeDibujo("Mi Ventana De Dibujo").setVisible(true)
        );
    }
}
