package ventanas;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class PanelDeDibujo extends JPanel {

    public enum Herramienta {
        LIBRE, RECTANGULO, LINEA, TRIANGULO, CIRCULO, PENTAGONO, HEXAGONO, BORRADOR
    }

    private final List<Figura> figuras = new ArrayList<>();
    private Figura figuraActual;
    private Herramienta herramientaActual = Herramienta.LIBRE;
    private Color colorLinea = Color.BLACK;
    private Color colorRelleno = Color.WHITE;
    private int grosor = 2; // ✅ nuevo: grosor del lápiz/pincel

    private Point cursorActual = null;

    public PanelDeDibujo() {
        setBackground(Color.WHITE);

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                switch (herramientaActual) {
                    case LIBRE, BORRADOR ->
                        figuraActual = new DibujoLibre(p, grosor);
                    case RECTANGULO ->
                        figuraActual = new Rectangulo(p);
                    case LINEA ->
                        figuraActual = new Linea(p);
                    case TRIANGULO ->
                        figuraActual = new Triangulo(p);
                    case CIRCULO ->
                        figuraActual = new Circulo(p);
                    case PENTAGONO ->
                        figuraActual = new Pentagono(p);
                    case HEXAGONO ->
                        figuraActual = new Hexagono(p);
                }

                if (figuraActual != null) {
                    if (herramientaActual == Herramienta.BORRADOR) {
                        figuraActual.setColorLinea(colorRelleno);
                    } else {
                        figuraActual.setColorLinea(colorLinea);
                        figuraActual.setColorRelleno(colorRelleno);
                    }
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
                cursorActual = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (figuraActual != null) {
                    figuraActual.actualizar(e.getPoint());
                    figuraActual = null;
                    repaint();
                }
                cursorActual = e.getPoint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                cursorActual = e.getPoint();
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cursorActual = null;
                repaint();
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

    public void setGrosor(int grosor) {
        this.grosor = grosor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Figura f : figuras) {
            f.dibujar(g);
        }

        // Indicador del borrador
        if (herramientaActual == Herramienta.BORRADOR && cursorActual != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.GRAY);
            int tam = 20;
            int x = cursorActual.x - tam / 2;
            int y = cursorActual.y - tam / 2;
            g2.drawOval(x, y, tam, tam);
        }
    }

    public void limpiar() {
        figuras.clear();
        repaint();
    }

    public void guardarComoImagen(String ruta, String formato) {
        BufferedImage imagen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imagen.createGraphics();
        paint(g2d);
        g2d.dispose();
        try {
            File archivo = new File(ruta);
            ImageIO.write(imagen, formato, archivo);
            System.out.println("Imagen guardada en: " + archivo.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
