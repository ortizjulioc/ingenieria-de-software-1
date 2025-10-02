package ventanas;

import java.awt.*;
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

        
        JToggleButton btnLinea = new JToggleButton("Línea");
        JButton btnColorLinea = new JButton("Color Línea");
        JButton btnColorRelleno = new JButton("Color Relleno");
        JButton btnLimpiar = new JButton("Limpiar");
        JToggleButton btnTriangulo = new JToggleButton("Triángulo");
        JToggleButton btnCirculo = new JToggleButton("Círculo");
        JToggleButton btnRect = new JToggleButton("Rectángulo");
        JToggleButton btnHexagono = new JToggleButton("Hexagono");
        JToggleButton btnPentagono = new JToggleButton("Pentagono");

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
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.RECTANGULO);
            } else if (!btnLinea.isSelected()) {
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.LIBRE);
            }
        });

        btnLinea.addActionListener(e -> {
            if (btnLinea.isSelected()) {
                btnRect.setSelected(false);
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.LINEA);
            } else if (!btnRect.isSelected()) {
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.LIBRE);
            }
        });

        btnTriangulo.addActionListener(e -> {
            if (btnTriangulo.isSelected()) {
                btnRect.setSelected(false);
                btnLinea.setSelected(false);
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.TRIANGULO);
            } else if (!btnRect.isSelected() && !btnLinea.isSelected()) {
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.LIBRE);
            }
        });

        
        
        
        btnCirculo.addActionListener(e -> {
            if (btnCirculo.isSelected()) {
                btnRect.setSelected(false);
                btnLinea.setSelected(false);
                btnTriangulo.setSelected(false);
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.CIRCULO);
            } else if (!btnRect.isSelected() && !btnLinea.isSelected() && !btnTriangulo.isSelected()) {
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.LIBRE);
            }
        });
        
        
           btnHexagono.addActionListener(e -> {
            if (btnHexagono.isSelected()) {
                btnRect.setSelected(false);
                btnLinea.setSelected(false);
                btnTriangulo.setSelected(false);
                btnCirculo.setSelected(false);
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.HEXAGONO);
            } else if (!btnRect.isSelected() && !btnLinea.isSelected() && !btnTriangulo.isSelected()) {
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
            } else if (!btnRect.isSelected() && !btnLinea.isSelected() && !btnTriangulo.isSelected()) {
                panelDeDibujo.setHerramienta(PanelDeDibujo.Herramienta.LIBRE);
            }
        });
           
           
        
        
        
        
        

        toolbar.add(btnRect);
        toolbar.add(btnLinea);
        toolbar.add(btnColorLinea);
        toolbar.add(btnColorRelleno);
        toolbar.addSeparator();
        toolbar.add(btnLimpiar);
        toolbar.add(btnTriangulo);
        toolbar.add(btnCirculo); 
        toolbar.add(btnHexagono);
        toolbar.add(btnPentagono);

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
