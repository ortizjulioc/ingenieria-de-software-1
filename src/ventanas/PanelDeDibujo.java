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
    public enum Herramienta { LIBRE, RECTANGULO, LINEA, TRIANGULO, CIRCULO, PENTAGONO, HEXAGONO, BORRADOR }

    private final List<Figura> figuras = new ArrayList<>();
    private Figura figuraActual;
    private Herramienta herramientaActual = Herramienta.LIBRE;
    private Color colorLinea = Color.BLACK;
    private Color colorRelleno = Color.WHITE;

    // ✅ Posición del cursor (para mostrar el indicador del borrador)
    private Point cursorActual = null;

    public PanelDeDibujo() {
        setBackground(Color.WHITE);

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                switch (herramientaActual) {
                    case LIBRE -> figuraActual = new DibujoLibre(p);
                    case RECTANGULO -> figuraActual = new Rectangulo(p);
                    case LINEA -> figuraActual = new Linea(p);
                    case TRIANGULO -> figuraActual = new Triangulo(p);
                    case CIRCULO -> figuraActual = new Circulo(p);
                    case PENTAGONO -> figuraActual = new Pentagono(p);
                    case HEXAGONO -> figuraActual = new Hexagono(p);
                    case BORRADOR -> figuraActual = new DibujoLibre(p);
                }

                if (figuraActual != null) {
                    if (herramientaActual == Herramienta.BORRADOR) {
                        figuraActual.setColorLinea(colorRelleno); // borrador pinta con relleno
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
                cursorActual = e.getPoint(); // mover indicador mientras arrastro
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
                cursorActual = e.getPoint(); // actualizar posición del cursor
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cursorActual = null; // ocultar al salir del panel
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Figura f : figuras) {
            f.dibujar(g);
        }

        // ✅ Dibujar el indicador del borrador
        if (herramientaActual == Herramienta.BORRADOR && cursorActual != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.GRAY); // color guía
            int grosor = 20; // mismo grosor que el borrador
            int x = cursorActual.x - grosor / 2;
            int y = cursorActual.y - grosor / 2;
            g2.drawOval(x, y, grosor, grosor);
        }
    }

    public void limpiar() {
        figuras.clear();
        repaint();
    }

    // ✅ Guardar como PNG/JPG
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
