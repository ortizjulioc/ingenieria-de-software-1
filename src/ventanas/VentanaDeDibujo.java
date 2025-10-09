package ventanas;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class VentanaDeDibujo extends JFrame {

    private PanelDeDibujo panelDeDibujo;

    public VentanaDeDibujo(String title) throws HeadlessException {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 700);
        setLocationRelativeTo(null);

        panelDeDibujo = new PanelDeDibujo();

        // === TOOLBAR PRINCIPAL ===
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toolbar.setBackground(new Color(245, 247, 250));

        // === SECCIÓN FIGURAS ===
        toolbar.add(makeSection("Figuras"));

        String[] figuras = {
            "Rectángulo", "Línea", "Triángulo", "Círculo",
            "Pentágono", "Hexágono", "Óvalo", "Rombo", "Estrella"
        };

        JComboBox<String> comboFiguras = new JComboBox<>(figuras);
        comboFiguras.setToolTipText("Seleccionar figura geométrica");
        comboFiguras.addActionListener(e -> {
            String sel = (String) comboFiguras.getSelectedItem();
            switch (sel) {
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
                case "Óvalo" ->
                    panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.OVALO);
                case "Rombo" ->
                    panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.ROMBO);
                case "Estrella" ->
                    panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.ESTRELLA);

            }
        });
        toolbar.add(comboFiguras);
        toolbar.addSeparator();

        // === SECCIÓN PINCELES ===
        toolbar.add(makeSection("Pinceles"));

        String[] pinceles = {"Dibujo Libre", "Pincel Fino", "Pincel Medio", "Pincel Grueso", "Borrador"};
        JComboBox<String> comboPinceles = new JComboBox<>(pinceles);
        comboPinceles.addActionListener(e -> {
            String sel = (String) comboPinceles.getSelectedItem();
            switch (sel) {
                case "Dibujo Libre", "Pincel Fino" -> {
                    panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.LIBRE);
                    panelDeDibujo.setGrosor(2);
                }
                case "Pincel Medio" -> {
                    panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.LIBRE);
                    panelDeDibujo.setGrosor(6);
                }
                case "Pincel Grueso" -> {
                    panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.LIBRE);
                    panelDeDibujo.setGrosor(12);
                }
                case "Borrador" ->
                    panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.BORRADOR);
            }
        });
        toolbar.add(comboPinceles);
        toolbar.addSeparator();

        // === SECCIÓN COLORES ===
        toolbar.add(makeSection("Colores"));

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

        // === SECCIÓN ACCIONES ===
        toolbar.add(makeSection("Acciones"));

        JButton btnCopiar = new JButton("Copiar");
        JButton btnPegar = new JButton("Pegar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnGuardar = new JButton("Guardar");

        btnCopiar.addActionListener(e -> panelDeDibujo.copiarFiguraSeleccionada());
        btnPegar.addActionListener(e -> panelDeDibujo.pegarFigura());
        btnLimpiar.addActionListener(e -> panelDeDibujo.limpiar());
        btnGuardar.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Guardar dibujo como...");
            fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imagen PNG", "png"));
            fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imagen JPG", "jpg"));
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File archivo = fc.getSelectedFile();
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

        toolbar.add(btnCopiar);
        toolbar.add(btnPegar);
        toolbar.add(btnLimpiar);
        toolbar.add(btnGuardar);

        // === ENSAMBLAR ===
        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        add(panelDeDibujo, BorderLayout.CENTER);
    }

    // === MÉTODO AUXILIAR PARA LOS TÍTULOS ===
    private JLabel makeSection(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 13f));
        label.setOpaque(true);
        label.setBackground(new Color(220, 230, 240));
        label.setBorder(new LineBorder(new Color(180, 190, 200), 1, true));
        label.setForeground(new Color(30, 50, 80));
        label.setBorder(new EmptyBorder(3, 6, 3, 6));
        return label;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new VentanaDeDibujo("Mi Ventana de Dibujo").setVisible(true));
    }
}
