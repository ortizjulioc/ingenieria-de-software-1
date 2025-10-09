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

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toolbar.setBackground(new Color(245, 247, 250));

        String[] figuras = {"Rectángulo", "Línea", "Triángulo", "Círculo", "Pentágono", "Hexágono", "Estrella", "Óvalo", "Rombo", "Flecha Arriba", "Flecha Abajo", "Flecha Derecha", "Flecha Izquierda"};
        JComboBox<String> comboFiguras = new JComboBox<>(figuras);
        comboFiguras.setToolTipText("Seleccionar figura geométrica");
        comboFiguras.addActionListener(e -> {
            String sel = (String) comboFiguras.getSelectedItem();
            switch (sel) {
                case "Rectángulo" -> panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.RECTANGULO);
                case "Línea" -> panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.LINEA);
                case "Triángulo" -> panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.TRIANGULO);
                case "Círculo" -> panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.CIRCULO);
                case "Pentágono" -> panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.PENTAGONO);
                case "Hexágono" -> panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.HEXAGONO);
                case "Estrella" -> panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.ESTRELLA);
                case "Óvalo" -> panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.OVALO);
                case "Rombo" -> panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.ROMBO);
                case "Flecha Arriba" -> panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.FLECHA_ARRIBA);
                case "Flecha Abajo" -> panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.FLECHA_ABAJO);
                case "Flecha Derecha" -> panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.FLECHA_DERECHA);
                case "Flecha Izquierda" -> panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.FLECHA_IZQUIERDA);
            }
        });
        toolbar.add(makeSection("Figuras"));
        toolbar.add(comboFiguras);
        toolbar.addSeparator();

        String[] pinceles = {"Dibujo Libre", "Pincel Fino", "Pincel Medio", "Pincel Grueso", "Borrador"};
        JComboBox<String> comboPinceles = new JComboBox<>(pinceles);
        comboPinceles.setToolTipText("Seleccionar tipo de pincel");
        comboPinceles.addActionListener(e -> {
            String sel = (String) comboPinceles.getSelectedItem();
            switch (sel) {
                case "Dibujo Libre" -> {
                    panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.LIBRE);
                    panelDeDibujo.setGrosor(2);
                }
                case "Pincel Fino" -> {
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
                case "Borrador" -> panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.BORRADOR);
            }
        });
        toolbar.add(makeSection("Pinceles"));
        toolbar.add(comboPinceles);
        toolbar.addSeparator();

        JButton btnColorLinea = new JButton("Color Línea");
        JButton btnColorRelleno = new JButton("Color Relleno");
        btnColorLinea.addActionListener(e -> {
            Color nuevo = JColorChooser.showDialog(this, "Elige color de línea", Color.BLACK);
            if (nuevo != null) panelDeDibujo.setColorLinea(nuevo);
        });
        btnColorRelleno.addActionListener(e -> {
            Color nuevo = JColorChooser.showDialog(this, "Elige color de relleno", Color.WHITE);
            if (nuevo != null) panelDeDibujo.setColorRelleno(nuevo);
        });
        toolbar.add(makeSection("Colores"));
        toolbar.add(btnColorLinea);
        toolbar.add(btnColorRelleno);
        toolbar.addSeparator();

        JButton btnCopiar = new JButton("Copiar");
        JButton btnPegar = new JButton("Pegar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnGuardar = new JButton("Guardar");

        btnCopiar.setToolTipText("Shift + clic sobre figura → Copiar");
        btnPegar.setToolTipText("Pegar figura copiada");
        btnLimpiar.setToolTipText("Borrar todo el lienzo");
        btnGuardar.setToolTipText("Guardar dibujo como imagen");

        btnCopiar.addActionListener(e -> panelDeDibujo.copiarFiguraSeleccionada());
        btnPegar.addActionListener(e -> panelDeDibujo.pegarFigura());
        btnLimpiar.addActionListener(e -> panelDeDibujo.limpiar());
        btnGuardar.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Guardar dibujo como...");
            fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG Image", "png"));
            fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JPG Image", "jpg"));
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File archivo = fc.getSelectedFile();
                String formato = "png";
                String nombre = archivo.getName().toLowerCase();
                if (nombre.endsWith(".jpg") || nombre.endsWith(".jpeg")) formato = "jpg";
                else if (!nombre.endsWith(".png")) archivo = new File(archivo.getAbsolutePath() + ".png");
                panelDeDibujo.guardarComoImagen(archivo.getAbsolutePath(), formato);
            }
        });

        toolbar.add(makeSection("Acciones"));
        toolbar.add(btnCopiar);
        toolbar.add(btnPegar);
        toolbar.add(btnLimpiar);
        toolbar.add(btnGuardar);

        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        add(panelDeDibujo, BorderLayout.CENTER);
    }

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
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new VentanaDeDibujo("Mi Ventana De Dibujo").setVisible(true));
    }
}
