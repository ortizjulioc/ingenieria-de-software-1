package ventanas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;


public class PanelDeDibujo extends JPanel {

    public enum Herramienta {
        SELECCION,
        LINEA,
        RECTANGULO,
        CIRCULO,
        OVALO,
        TRIANGULO,
        ROMBO,
        TRAPECIO,
        PENTAGONO,
        HEXAGONO,
        ESTRELLA,
        NUBE,
        ARCO,
        CORAZON,
        FLECHA_ARRIBA,
        FLECHA_ABAJO,
        FLECHA_IZQUIERDA,
        FLECHA_DERECHA,
        DIBUJO_LIBRE,
        BORRADOR,
        CUBETA
    }

    private final java.util.List<Figura> figuras = new ArrayList<>();
    private Figura figuraActual = null;

    private Herramienta herramienta = Herramienta.SELECCION;
    private Color colorLinea = Color.BLACK;
    private Color colorRelleno = Color.WHITE;

    // Grosor del pincel para Dibujo Libre
    private float grosorActual = 2.0f;

    // Borrador: tamaño y color (por defecto blanco)
    private float tamBorrador = 12.0f;
    private Color colorBorrador = Color.WHITE;

  
    private Figura figuraSeleccionada = null;
    private boolean arrastrando = false;
    private boolean redimensionando = false;
    private int handleActivo = -1; // 0..7
    private Point puntoAnterior = null;
    private double aspectRatioInicial = 1.0;

    
    private Point mousePos = null;

    
    private final Deque<java.util.List<Figura>> undoStack = new ArrayDeque<>();
    private final Deque<java.util.List<Figura>> redoStack = new ArrayDeque<>();
    private boolean modificado = false;

   
    private java.util.List<Figura> portapapeles = new ArrayList<>();

    public PanelDeDibujo() {
        setBackground(Color.WHITE);
        setDoubleBuffered(true);

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
                puntoAnterior = e.getPoint();

                if (herramienta == Herramienta.CUBETA) {
                    Figura f = obtenerFiguraEnPunto(puntoAnterior);
                    if (f instanceof FiguraRellenable fr) {
                        pushUndo();
                        fr.setColorRelleno(colorRelleno);
                        figuraSeleccionada = f;
                        modificado = true;
                        repaint();
                    }
                    return;
                }

                if (herramienta == Herramienta.SELECCION) {
                    Figura f = obtenerFiguraEnPunto(puntoAnterior);
                    figuraSeleccionada = f;

                    if (figuraSeleccionada != null) {
                        handleActivo = detectarHandle(figuraSeleccionada.getBounds(), puntoAnterior);
                        if (handleActivo >= 0 && figuraSeleccionada instanceof FiguraRellenable) {
                            redimensionando = true;
                            aspectRatioInicial = calcAspect(figuraSeleccionada.getBounds());
                            pushUndo();
                        } else {
                            arrastrando = true;
                            pushUndo();
                        }
                    }
                    repaint();
                    return;
                }

                if (herramienta == Herramienta.DIBUJO_LIBRE) {
                    DibujoLibre dl = new DibujoLibre(puntoAnterior, grosorActual);
                    dl.setColorLinea(colorLinea);
                    figuraActual = dl;
                    figuras.add(figuraActual);
                    figuraSeleccionada = figuraActual;
                    pushUndo();
                    modificado = true;
                    repaint();
                    return;
                }

                if (herramienta == Herramienta.BORRADOR) {
                   
                    mousePos = puntoAnterior;
                    Borrador b = new Borrador(puntoAnterior, tamBorrador);
                    b.setColorLinea(colorBorrador); 
                    figuraActual = b;
                    figuras.add(figuraActual);
                    figuraSeleccionada = figuraActual;
                    pushUndo();
                    modificado = true;
                    repaint();
                    return;
                }

              
                switch (herramienta) {
                    case LINEA -> {
                        figuraActual = new Linea(puntoAnterior);
                        figuraActual.setColorLinea(colorLinea);
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case RECTANGULO -> {
                        Rectangulo r = new Rectangulo(puntoAnterior);
                        r.setColorLinea(colorLinea);
                        r.setColorRelleno(colorRelleno);
                        figuraActual = r;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case CIRCULO -> {
                        Circulo c = new Circulo(puntoAnterior);
                        c.setColorLinea(colorLinea);
                        c.setColorRelleno(colorRelleno);
                        figuraActual = c;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case OVALO -> {
                        Ovalo o = new Ovalo(puntoAnterior);
                        o.setColorLinea(colorLinea);
                        o.setColorRelleno(colorRelleno);
                        figuraActual = o;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case TRIANGULO -> {
                        Triangulo t = new Triangulo(puntoAnterior);
                        t.setColorLinea(colorLinea);
                        t.setColorRelleno(colorRelleno);
                        figuraActual = t;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case ROMBO -> {
                        Rombo r = new Rombo(puntoAnterior);
                        r.setColorLinea(colorLinea);
                        r.setColorRelleno(colorRelleno);
                        figuraActual = r;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case TRAPECIO -> {
                        Trapecio t = new Trapecio(puntoAnterior);
                        t.setColorLinea(colorLinea);
                        t.setColorRelleno(colorRelleno);
                        figuraActual = t;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case PENTAGONO -> {
                        Pentagono p = new Pentagono(puntoAnterior);
                        p.setColorLinea(colorLinea);
                        p.setColorRelleno(colorRelleno);
                        figuraActual = p;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case HEXAGONO -> {
                        Hexagono h = new Hexagono(puntoAnterior);
                        h.setColorLinea(colorLinea);
                        h.setColorRelleno(colorRelleno);
                        figuraActual = h;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case ESTRELLA -> {
                        Estrella s = new Estrella(puntoAnterior);
                        s.setColorLinea(colorLinea);
                        s.setColorRelleno(colorRelleno);
                        figuraActual = s;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case NUBE -> {
                        Nube n = new Nube(puntoAnterior);
                        n.setColorLinea(colorLinea);
                        n.setColorRelleno(colorRelleno);
                        figuraActual = n;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case ARCO -> {
                        Arco a = new Arco(puntoAnterior);
                        a.setColorLinea(colorLinea);
                        a.setColorRelleno(colorRelleno);
                        figuraActual = a;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case CORAZON -> {
                        Corazon c = new Corazon(puntoAnterior);
                        c.setColorLinea(colorLinea);
                        c.setColorRelleno(colorRelleno);
                        figuraActual = c;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case FLECHA_ARRIBA -> {
                        FlechaArriba f = new FlechaArriba(puntoAnterior);
                        f.setColorLinea(colorLinea);
                        f.setColorRelleno(colorRelleno);
                        figuraActual = f;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case FLECHA_ABAJO -> {
                        FlechaAbajo f = new FlechaAbajo(puntoAnterior);
                        f.setColorLinea(colorLinea);
                        f.setColorRelleno(colorRelleno);
                        figuraActual = f;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case FLECHA_IZQUIERDA -> {
                        FlechaIzquierda f = new FlechaIzquierda(puntoAnterior);
                        f.setColorLinea(colorLinea);
                        f.setColorRelleno(colorRelleno);
                        figuraActual = f;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case FLECHA_DERECHA -> {
                        FlechaDerecha f = new FlechaDerecha(puntoAnterior);
                        f.setColorLinea(colorLinea);
                        f.setColorRelleno(colorRelleno);
                        figuraActual = f;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    default -> {}
                }
                modificado = true;
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = e.getPoint();

                if (herramienta == Herramienta.SELECCION) {
                    if (figuraSeleccionada != null) {
                        if (redimensionando && (figuraSeleccionada instanceof Rectangulo rr)) {
                            Rectangle b = figuraSeleccionada.getBounds();
                            Rectangle nb = ajustarBoundsConHandle(b, handleActivo, p, aspectRatioInicial);
                            rr.iniciarProporcional();
                            rr.actualizar(new Point(nb.x + nb.width, nb.y + nb.height));
                            rr.terminarProporcional();
                            repaint();
                        } else if (redimensionando) {
                            Rectangle b = figuraSeleccionada.getBounds();
                            Rectangle nb = ajustarBoundsConHandle(b, handleActivo, p, aspectRatioInicial);
                            figuraSeleccionada.desplazar(nb.x - b.x, nb.y - b.y);
                            figuraSeleccionada.actualizar(new Point(nb.x + nb.width, nb.y + nb.height));
                            repaint();
                        } else if (arrastrando) {
                            int dx = p.x - puntoAnterior.x;
                            int dy = p.y - puntoAnterior.y;
                            figuraSeleccionada.desplazar(dx, dy);
                            puntoAnterior = p;
                            repaint();
                        }
                    }
                    return;
                }

                if (herramienta == Herramienta.DIBUJO_LIBRE && figuraActual instanceof DibujoLibre dl) {
                    dl.agregarPunto(p);
                    repaint();
                    return;
                }

                if (herramienta == Herramienta.BORRADOR && figuraActual instanceof Borrador b) {
                    
                    mousePos = p;
                    b.agregarPunto(p);
                    repaint();
                    return;
                }

                if (figuraActual != null) {
                    figuraActual.actualizar(p);
                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mousePos = e.getPoint();
                if (herramienta == Herramienta.BORRADOR) repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mousePos = null;
                if (herramienta == Herramienta.BORRADOR) repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                figuraActual = null;
                arrastrando = false;
                redimensionando = false;
                handleActivo = -1;
                puntoAnterior = null;
            }
        };

        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        // Atajos de teclado
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "del");
        getActionMap().put("del", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                if (figuraSeleccionada != null) {
                    pushUndo();
                    figuras.remove(figuraSeleccionada);
                    figuraSeleccionada = null;
                    modificado = true;
                    repaint();
                }
            }
        });
        // Ctrl+Z
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "undo");
        getActionMap().put("undo", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { undo(); }
        });
        // Ctrl+Y
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "redo");
        getActionMap().put("redo", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { redo(); }
        });
        // Ctrl+C
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "copy");
        getActionMap().put("copy", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { copiarSeleccion(); }
        });
        // Ctrl+V
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "paste");
        getActionMap().put("paste", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { pegar(); }
        });
      
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0), "incStroke");
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), "incStroke");
        getActionMap().put("incStroke", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { setGrosorActual(grosorActual + 1f); ajustarGrosorSeleccionado(grosorActual); }
        });
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "decStroke");
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), "decStroke");
        getActionMap().put("decStroke", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { setGrosorActual(Math.max(1f, grosorActual - 1f)); ajustarGrosorSeleccionado(grosorActual); }
        });
    }

    
    private static final int HANDLE_SIZE = 8;

    private static double calcAspect(Rectangle b) {
        return (b.height == 0) ? 1.0 : (double) b.width / (double) b.height;
    }

    private static int detectarHandle(Rectangle b, Point p) {
        Rectangle[] hs = handles(b);
        for (int i = 0; i < hs.length; i++) if (hs[i].contains(p)) return i;
        return -1;
    }

    private static Rectangle[] handles(Rectangle b) {
        int hs = HANDLE_SIZE;
        int x = b.x, y = b.y, w = b.width, h = b.height;
        return new Rectangle[] {
                new Rectangle(x - hs/2,       y - hs/2,       hs, hs), // NW 0
                new Rectangle(x + w/2 - hs/2, y - hs/2,       hs, hs), // N  1
                new Rectangle(x + w - hs/2,   y - hs/2,       hs, hs), // NE 2
                new Rectangle(x - hs/2,       y + h/2 - hs/2, hs, hs), // W  3
                new Rectangle(x + w - hs/2,   y + h/2 - hs/2, hs, hs), // E  4
                new Rectangle(x - hs/2,       y + h - hs/2,   hs, hs), // SW 5
                new Rectangle(x + w/2 - hs/2, y + h - hs/2,   hs, hs), // S  6
                new Rectangle(x + w - hs/2,   y + h - hs/2,   hs, hs)  // SE 7
        };
    }

    private static Rectangle ajustarBoundsConHandle(Rectangle b, int handle, Point p, double aspect) {
        int x1 = b.x, y1 = b.y, x2 = b.x + b.width, y2 = b.y + b.height;

        switch (handle) {
            case 0 -> { x1 = p.x; y1 = p.y; }
            case 1 -> { y1 = p.y; }
            case 2 -> { x2 = p.x; y1 = p.y; }
            case 3 -> { x1 = p.x; }
            case 4 -> { x2 = p.x; }
            case 5 -> { x1 = p.x; y2 = p.y; }
            case 6 -> { y2 = p.y; }
            case 7 -> { x2 = p.x; y2 = p.y; }
        }
        int nx = Math.min(x1, x2);
        int ny = Math.min(y1, y2);
        int nw = Math.abs(x2 - x1);
        int nh = Math.abs(y2 - y1);

        if (nh == 0) nh = 1;
        double cur = (double) nw / nh;

        if (cur > aspect) nh = (int) Math.round(nw / aspect);
        else             nw = (int) Math.round(nh * aspect);

        // Reubica segun handle para mantener la esquina opuesta fija
        switch (handle) {
            case 0 -> { nx = x2 - nw; ny = y2 - nh; }
            case 1 -> { nx = x1;      ny = y2 - nh; }
            case 2 -> { nx = x1;      ny = y2 - nh; }
            case 3 -> { nx = x2 - nw; ny = y1;      }
            case 4 -> { nx = x1;      ny = y1;      }
            case 5 -> { nx = x2 - nw; ny = y1;      }
            case 6 -> { nx = x1;      ny = y1;      }
            case 7 -> { nx = x1;      ny = y1;      }
        }
        return new Rectangle(nx, ny, nw, nh);
    }

    // ==== Pintado ====
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Figura f : figuras) f.dibujar(g2);

        // Silueta del borrador (overlay)
        if (herramienta == Herramienta.BORRADOR && mousePos != null) {
            Composite old = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
            g2.setColor(colorBorrador);
            int d = Math.round(tamBorrador);
            g2.fillOval(mousePos.x - d/2, mousePos.y - d/2, d, d);
            g2.setComposite(old);

            // Contorno de la silueta
            g2.setColor(new Color(0,0,0,120));
            g2.setStroke(new BasicStroke(1f));
            g2.drawOval(mousePos.x - d/2, mousePos.y - d/2, d, d);
        }

    
        if (figuraSeleccionada != null && (figuraSeleccionada instanceof FiguraRellenable)) {
            Rectangle b = figuraSeleccionada.getBounds();
            Stroke old = g2.getStroke();
            float[] dash = {6f, 6f};
            g2.setColor(new Color(220, 50, 50));
            g2.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dash, 0f));
            g2.drawRect(b.x, b.y, b.width, b.height);
            g2.setStroke(old);

            for (Rectangle h : handles(b)) g2.fillRect(h.x, h.y, h.width, h.height);
        }
    }

    
    public void setHerramienta(Herramienta h) { this.herramienta = h; }
    public Herramienta getHerramienta() { return herramienta; }
    public void setColorLinea(Color c) { this.colorLinea = c; }
    public void setColorRelleno(Color c) { this.colorRelleno = c; }
    public void setGrosorActual(float g) { this.grosorActual = Math.max(1f, g); }
    public float getGrosorActual() { return grosorActual; }

    public void setTamBorrador(float t) { this.tamBorrador = Math.max(1f, t); repaint(); }
    public float getTamBorrador() { return tamBorrador; }
    public void setColorBorrador(Color c) { this.colorBorrador = (c != null ? c : Color.WHITE); repaint(); }
    public Color getColorBorrador() { return colorBorrador; }

    
    public void ajustarGrosorSeleccionado(float g) {
        if (figuraSeleccionada instanceof DibujoLibre dl) {
            dl.setGrosor(g);
            repaint();
        }
    }

 
    private Figura obtenerFiguraEnPunto(Point p) {
        for (int i = figuras.size() - 1; i >= 0; i--) {
            Figura f = figuras.get(i);
            if (f.getBounds().contains(p)) return f;
        }
        return null;
    }

    // ==== Copiar / Pegar ====
    public void copiarSeleccion() {
        portapapeles.clear();
        if (figuraSeleccionada != null) {
            portapapeles.add(figuraSeleccionada.clonarConDesplazamiento(0, 0));
        }
    }

    public void pegar() {
        if (portapapeles.isEmpty()) return;
        pushUndo();
        figuras.add(portapapeles.get(0).clonarConDesplazamiento(20, 20));
        figuraSeleccionada = figuras.get(figuras.size() - 1);
        modificado = true;
        repaint();
    }

    // ==== Limpiar ====
    public void limpiarLienzo() {
        figuras.clear();
        figuraSeleccionada = null;
        modificado = true;
        repaint();
    }

    // ==== Undo/Redo ====
    private void pushUndo() {
        java.util.List<Figura> snap = new ArrayList<>();
        for (Figura f : figuras) snap.add(f.clonarConDesplazamiento(0, 0));
        undoStack.push(snap);
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(new ArrayList<>(figuras));
            figuras.clear();
            figuras.addAll(undoStack.pop());
            figuraSeleccionada = null;
            repaint();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(new ArrayList<>(figuras));
            figuras.clear();
            figuras.addAll(redoStack.pop());
            figuraSeleccionada = null;
            repaint();
        }
    }

    // ==== Exportar PNG sin overlays ====
    public void exportarComoPNG(File f) throws Exception {
        Figura sel = figuraSeleccionada;
        figuraSeleccionada = null; // oculta overlays
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        paint(g2);
        g2.dispose();
        ImageIO.write(img, "png", f);
        figuraSeleccionada = sel;
    }

    // ==== Guardar / Abrir proyecto ====
    public void guardarProyecto(File f) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
            oos.writeObject(figuras);
            modificado = false;
        }
    }

    @SuppressWarnings("unchecked")
    public void abrirProyecto(File f) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object obj = ois.readObject();
            if (obj instanceof java.util.List<?> lista) {
                figuras.clear();
                for (Object o : lista) figuras.add((Figura) o);
                figuraSeleccionada = null;
                modificado = false;
                repaint();
            } else throw new IOException("Formato de proyecto inválido.");
        }
    }

    public boolean isModificado() { return modificado; }
}
