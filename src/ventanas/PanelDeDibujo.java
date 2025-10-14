package ventanas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class PanelDeDibujo extends JPanel {

    public enum Herramienta {
        LIBRE, RECTANGULO, LINEA, TRIANGULO, CIRCULO, PENTAGONO, HEXAGONO, 
        ESTRELLA, BORRADOR, OVALO, ROMBO, FLECHA_ARRIBA, FLECHA_ABAJO, 
        FLECHA_DERECHA, FLECHA_IZQUIERDA, TRAPECIO, ARCO, NUBE,
    }

    private final List<Figura> figuras = new ArrayList<>();
    private Figura figuraActual;
    private Herramienta herramientaActual = Herramienta.LIBRE;
    private Color colorLinea = Color.BLACK;
    private Color colorRelleno = Color.WHITE;
    private int grosor = 2;

    private Figura figuraSeleccionada = null;
    private Figura figuraCopiada = null;

    private Point cursorActual = null;

    public PanelDeDibujo() {
        setBackground(Color.WHITE);

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();

                if (e.isShiftDown()) {
                    figuraActual = null;
                    figuraSeleccionada = obtenerFiguraEnPunto(p);
                    repaint();
                    return;
                }

                switch (herramientaActual) {
                    case LIBRE -> figuraActual = new DibujoLibre(p, grosor);
                    case BORRADOR -> {
                        figuraActual = new DibujoLibre(p, 20);
                        figuraActual.setColorLinea(colorRelleno);
                    }
                    case RECTANGULO -> figuraActual = new Rectangulo(p);
                    case LINEA -> figuraActual = new Linea(p);
                    case TRIANGULO -> figuraActual = new Triangulo(p);
                    case CIRCULO -> figuraActual = new Circulo(p);
                    case PENTAGONO -> figuraActual = new Pentagono(p);
                    case HEXAGONO -> figuraActual = new Hexagono(p);
                    case ESTRELLA -> figuraActual = new Estrella(p);
                    case OVALO -> figuraActual = new Ovalo(p);
                    case ROMBO -> figuraActual = new Rombo(p);
                    case FLECHA_ARRIBA -> figuraActual = new FlechaArriba(p);
                    case FLECHA_ABAJO -> figuraActual = new FlechaAbajo(p);
                    case FLECHA_DERECHA -> figuraActual = new FlechaDerecha(p);
                    case FLECHA_IZQUIERDA -> figuraActual = new FlechaIzquierda(p);
                    case TRAPECIO -> figuraActual = new Trapecio(p);
                    case ARCO -> figuraActual = new Arco(p);
                    case NUBE -> figuraActual = new Nube(p);

                }

                if (figuraActual != null) {
                    if (herramientaActual != Herramienta.BORRADOR) {
                        figuraActual.setColorLinea(colorLinea);
                        figuraActual.setColorRelleno(colorRelleno);
                    }
                    figuras.add(figuraActual);
                    figuraSeleccionada = null;
                }

                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                cursorActual = e.getPoint();
                if (figuraActual != null) {
                    figuraActual.actualizar(e.getPoint());
                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                cursorActual = e.getPoint();
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                figuraActual = null;
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

    public void limpiar() {
        figuras.clear();
        figuraSeleccionada = null;
        repaint();
    }

    public void guardarComoImagen(String ruta, String formato) {
        BufferedImage imagen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = imagen.createGraphics();
        paint(g2);
        g2.dispose();
        try {
            ImageIO.write(imagen, formato, new File(ruta));
            JOptionPane.showMessageDialog(this, "Imagen guardada en: " + ruta);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void copiarFiguraSeleccionada() {
        if (figuraSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Usa SHIFT + Clic sobre una figura para seleccionarla.");
            return;
        }
        figuraCopiada = figuraSeleccionada.clonarConDesplazamiento(0, 0);
        JOptionPane.showMessageDialog(this, "Figura copiada.");
    }

    public void pegarFigura() {
        if (figuraCopiada == null) {
            JOptionPane.showMessageDialog(this, "No hay figura copiada.");
            return;
        }
        Figura nueva = figuraCopiada.clonarConDesplazamiento(20, 20);
        if (nueva != null) {
            figuras.add(nueva);
            figuraSeleccionada = nueva;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Figura f : figuras) {
            f.dibujar(g);
        }

        if (figuraSeleccionada != null) {
            Rectangle r = figuraSeleccionada.getBounds();
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(r.x - 3, r.y - 3, r.width + 6, r.height + 6);
        }

        if (herramientaActual == Herramienta.BORRADOR && cursorActual != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.GRAY);
            int tam = 20;
            g2.drawOval(cursorActual.x - tam / 2, cursorActual.y - tam / 2, tam, tam);
        }
    }

    private Figura obtenerFiguraEnPunto(Point p) {
        for (int i = figuras.size() - 1; i >= 0; i--) {
            Figura f = figuras.get(i);
            if (f.getBounds().contains(p)) {
                return f;
            }
        }
        return null;
    }
}
