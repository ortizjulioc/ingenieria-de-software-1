package ventanas;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class PanelDeDibujo extends JPanel {
    //---------------------------------------------------------------------------------------------------------------

    public enum Herramienta {
        LIBRE, RECTANGULO, LINEA, TRIANGULO, CIRCULO,HEXAGONO,PENTAGONO}

    private final List<Figura> figuras = new ArrayList<>();
    private Figura figuraActual;
    private Herramienta herramientaActual = Herramienta.LIBRE;
    private Color colorLinea = Color.BLACK;
    private Color colorRelleno = Color.WHITE;

    public PanelDeDibujo() {
        setBackground(Color.WHITE);

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //----------------------------------------------------------------------------------------------------------------------
                Point p = e.getPoint();
                switch (herramientaActual) {
                    case LIBRE ->
                        figuraActual = new DibujoLibre(p);
                    case RECTANGULO ->
                        figuraActual = new Rectangulo(p);
                    case LINEA ->
                        figuraActual = new Linea(p);
                    case TRIANGULO ->
                        figuraActual = new Triangulo(p);
                    case HEXAGONO ->
                        figuraActual = new Hexagono(p);
                    case CIRCULO ->
                        figuraActual = new Circulo(p);
                    case PENTAGONO ->
                        figuraActual = new Pentagono(p);
                        
                }

                if (figuraActual != null) {
                    figuraActual.setColorLinea(colorLinea);
                    figuraActual.setColorRelleno(colorRelleno);
                    figuras.add(figuraActual);
                }
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (figuraActual != null) {
                    figuraActual.actualizar(e.getPoint());
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (figuraActual != null) {
                    figuraActual.actualizar(e.getPoint());
                    figuraActual = null;
                    repaint();
                }
            }
        };

        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    public void setHerramienta(Herramienta herramienta) {
        this.herramientaActual = herramienta;
    }

    public void setColorLinea(Color color) {
        this.colorLinea = color;
    }

    public void setColorRelleno(Color color) {
        this.colorRelleno = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Figura f : figuras) {
            f.dibujar(g);
        }
    }

    public void limpiar() {
        figuras.clear();
        repaint();
    }
}
