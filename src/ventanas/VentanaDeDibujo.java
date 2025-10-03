package ventanas;

import java.awt.*;
import java.io.File;
import javax.swing.*;

public class VentanaDeDibujo extends JFrame {

    private PanelDeDibujo panelDeDibujo;

    public VentanaDeDibujo(String title) throws HeadlessException {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        panelDeDibujo = new PanelDeDibujo();

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        JToggleButton btnRect = new JToggleButton("Rectángulo");
        JToggleButton btnLinea = new JToggleButton("Línea");
        JToggleButton btnTriangulo = new JToggleButton("Triángulo");
        JToggleButton btnCirculo = new JToggleButton("Círculo");
        JToggleButton btnPentagono = new JToggleButton("Pentágono");
        JToggleButton btnHexagono = new JToggleButton("Hexágono");
        JToggleButton btnBorrador = new JToggleButton("Borrador");

        JButton btnColorLinea = new JButton("Color Línea");
        JButton btnColorRelleno = new JButton("Color Relleno");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnGuardar = new JButton("Guardar");

        btnLimpiar.addActionListener(e -> panelDeDibujo.limpiar());

        btnColorLinea.addActionListener(e -> {
            Color nuevo = JColorChooser.showDialog(this, "Elige color de línea", Color.BLACK);
            if (nuevo != null) {
                panelDeDibujo.setColorLinea(nuevo);
            }
        });

        btnColorRelleno.addActionListener(e -> {
            Color nuevo = JColorChooser.showDialog(this, "Elige color de relleno", Color.WHITE);
            if (nuevo != null) {
                panelDeDibujo.setColorRelleno(nuevo);
            }
        });

        btnRect.addActionListener(e -> {
            if (btnRect.isSelected()) {
                btnLinea.setSelected(false);
                btnTriangulo.setSelected(false);
                btnCirculo.setSelected(false);
                btnPentagono.setSelected(false);
                btnHexagono.setSelected(false);
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.RECTANGULO);
            } else {
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.LIBRE);
            }
        });

        btnLinea.addActionListener(e -> {
            if (btnLinea.isSelected()) {
                btnRect.setSelected(false);
                btnTriangulo.setSelected(false);
                btnCirculo.setSelected(false);
                btnPentagono.setSelected(false);
                btnHexagono.setSelected(false);
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.LINEA);
            } else {
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.LIBRE);
            }
        });

        btnTriangulo.addActionListener(e -> {
            if (btnTriangulo.isSelected()) {
                btnRect.setSelected(false);
                btnLinea.setSelected(false);
                btnCirculo.setSelected(false);
                btnPentagono.setSelected(false);
                btnHexagono.setSelected(false);
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.TRIANGULO);
            } else {
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.LIBRE);
            }
        });

        btnCirculo.addActionListener(e -> {
            if (btnCirculo.isSelected()) {
                btnRect.setSelected(false);
                btnLinea.setSelected(false);
                btnTriangulo.setSelected(false);
                btnPentagono.setSelected(false);
                btnHexagono.setSelected(false);
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.CIRCULO);
            } else {
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.LIBRE);
            }
        });

        btnPentagono.addActionListener(e -> {
            if (btnPentagono.isSelected()) {
                btnRect.setSelected(false);
                btnLinea.setSelected(false);
                btnTriangulo.setSelected(false);
                btnCirculo.setSelected(false);
                btnHexagono.setSelected(false);
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.PENTAGONO);
            } else {
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.LIBRE);
            }
        });

        btnHexagono.addActionListener(e -> {
            if (btnHexagono.isSelected()) {
                btnRect.setSelected(false);
                btnLinea.setSelected(false);
                btnTriangulo.setSelected(false);
                btnCirculo.setSelected(false);
                btnPentagono.setSelected(false);
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.HEXAGONO);
            } else {
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.LIBRE);
            }
        });

        // ✅ Nuevo botón Guardar
        btnGuardar.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar dibujo como...");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG Image", "png"));
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JPG Image", "jpg"));
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File archivo = fileChooser.getSelectedFile();
                String formato = "png"; // por defecto
                String nombre = archivo.getName().toLowerCase();
                if (nombre.endsWith(".jpg") || nombre.endsWith(".jpeg")) {
                    formato = "jpg";
                } else if (nombre.endsWith(".png")) {
                    formato = "png";
                } else {
                    archivo = new File(archivo.getAbsolutePath() + ".png");
                }
                panelDeDibujo.guardarComoImagen(archivo.getAbsolutePath(), formato);
            }
        });

        btnBorrador.addActionListener(e -> {
            if (btnBorrador.isSelected()) {
                btnRect.setSelected(false);
                btnLinea.setSelected(false);
                btnTriangulo.setSelected(false);
                btnCirculo.setSelected(false);
                btnPentagono.setSelected(false);
                btnHexagono.setSelected(false);
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.BORRADOR);
            } else {
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.LIBRE);
            }
        });

        toolbar.add(btnRect);
        toolbar.add(btnLinea);
        toolbar.add(btnTriangulo);
        toolbar.add(btnCirculo);
        toolbar.add(btnPentagono);
        toolbar.add(btnHexagono);
        toolbar.add(btnColorLinea);
        toolbar.add(btnColorRelleno);
        toolbar.addSeparator();
        toolbar.add(btnLimpiar);
        toolbar.add(btnGuardar); // ✅ botón de guardar
        toolbar.add(btnBorrador);

        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        add(panelDeDibujo, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()
                -> new VentanaDeDibujo("Mi Ventana De Dibujo").setVisible(true)
        );
    }
}
