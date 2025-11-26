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
    private Color colorRelleno = null;

    // Grosor del pincel para Dibujo Libre
    private float grosorActual = 2.0f;

    // Borrador: tamaño y color (por defecto blanco)
    private float tamBorrador = 12.0f;
    private Color colorBorrador = Color.WHITE;
    // ImageHandler gestiona la carga, visualización y limpieza de una imagen de fondo
    private ImageHandler imageHandler = new ImageHandler();

    private Figura figuraSeleccionada = null;
    private boolean arrastrando = false;
    private boolean redimensionando = false;
    private int handleActivo = -1; // 0..7
    private Point puntoAnterior = null;

    // Capa de relleno tipo Paint (cubeta)
    private BufferedImage fillLayer = null;
    // Datos para mover también el relleno cuando se arrastran figuras
    private BufferedImage fillSelection = null;     // trozo de relleno seleccionado
    private Rectangle fillSelectionBounds = null;   // posición original del trozo
    private Point fillSelectionOffset = new Point(0, 0); // desplazamiento acumulado
    private double aspectRatioInicial = 1.0;

    private Point mousePos = null;

    // ==== Selección múltiple tipo "marquee" ====
    /**
     * Figuras actualmente seleccionadas (una o varias).
     */
    private java.util.List<Figura> seleccionMultiple = new ArrayList<>();
    /**
     * ¿El usuario está arrastrando un rectángulo de selección?
     */
    private boolean seleccionando = false;
    /**
     * Punto donde empezó el arrastre de selección.
     */
    private Point inicioSeleccion = null;
    /**
     * Rectángulo temporal mientras se arrastra para seleccionar.
     */
    private Rectangle rectSeleccionTemporal = null;

    private final Deque<java.util.List<Figura>> undoStack = new ArrayDeque<>();
    private final Deque<java.util.List<Figura>> redoStack = new ArrayDeque<>();
    private boolean modificado = false;

    // =====================
    // VARIABLES PARA RECORTE DE IMAGEN (TIPO PAINT)
    // =====================
    // Indica si el modo recorte está activado
    private boolean cropMode = false;

    // Punto donde el usuario empieza a arrastrar el mouse
    private Point startPoint;

    // Rectángulo visual del área que se va a recortar
    private Rectangle cropRectangle;

    private java.util.List<Figura> portapapeles = new ArrayList<>();

    public PanelDeDibujo() {

        imageHandler = new ImageHandler();   // MUY IMPORTANTE
        setBackground(Color.WHITE);
        setDoubleBuffered(true);

        //---------------------------------------------------
        MouseAdapter mouse = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                // Si el modo recorte está activado, se guarda el punto inicial
                if (cropMode) {
                    startPoint = e.getPoint();
                    cropRectangle = new Rectangle();
                }

                requestFocusInWindow();
                puntoAnterior = e.getPoint();
                Point p = puntoAnterior;

                // Cubeta (flood fill)
                if (herramienta == Herramienta.CUBETA) {
                    aplicarCubeta(p);
                    return;
                }

                // ===================== SELECCIÓN =====================
                if (herramienta == Herramienta.SELECCION) {

                    // 0) Si ya hay algo seleccionado, comprobar primero si se hizo click en un handle
                    if (seleccionMultiple != null && seleccionMultiple.size() == 1) {
                        Figura sel = seleccionMultiple.get(0);
                        Rectangle b = sel.getBounds();
                        handleActivo = detectarHandle(b, p);

                        if (handleActivo >= 0 && sel instanceof FiguraRellenable) {
                            figuraSeleccionada = sel;
                            redimensionando = true;
                            arrastrando = false;
                            aspectRatioInicial = calcAspect(b);
                            pushUndo();
                            repaint();
                            return;
                        }
                    }

                    // 1) ¿Click dentro del grupo ya seleccionado? -> mover todo el grupo
                    Rectangle bbSel = getBoundsSeleccionMultiple();
                    if (bbSel != null && bbSel.contains(p)) {
                        figuraSeleccionada = null;
                        redimensionando = false;
                        arrastrando = true;

                        // Preparar el trozo de relleno que se mueve con el grupo
                        prepararSeleccionRelleno(bbSel);

                        pushUndo();
                        repaint();
                        return;
                    }

                    // 2) ¿Click sobre una figura concreta? -> selección individual
                    Figura f = obtenerFiguraEnPunto(p);
                    figuraSeleccionada = f;
                    seleccionMultiple.clear();
                    rectSeleccionTemporal = null;
                    seleccionando = false;

                    if (figuraSeleccionada != null) {
                        seleccionMultiple.add(figuraSeleccionada);

                        handleActivo = detectarHandle(figuraSeleccionada.getBounds(), p);
                        if (handleActivo >= 0 && figuraSeleccionada instanceof FiguraRellenable) {
                            redimensionando = true;
                            arrastrando = false;
                            aspectRatioInicial = calcAspect(figuraSeleccionada.getBounds());
                            pushUndo();
                        } else {
                            arrastrando = true;

                            // Preparar relleno para una sola figura
                            prepararSeleccionRelleno(figuraSeleccionada.getBounds());

                            pushUndo();
                        }
                    } else {
                        // 3) Click en espacio vacío -> empezar rectángulo de selección múltiple
                        seleccionando = true;
                        inicioSeleccion = p;
                        rectSeleccionTemporal = new Rectangle(p.x, p.y, 0, 0);
                        seleccionMultiple.clear();
                    }

                    repaint();
                    return;
                }

                // ===================== DIBUJO LIBRE =====================
                if (herramienta == Herramienta.DIBUJO_LIBRE) {
                    DibujoLibre dl = new DibujoLibre(p, grosorActual);
                    dl.setColorLinea(colorLinea);
                    figuraActual = dl;
                    figuras.add(figuraActual);
                    figuraSeleccionada = figuraActual;
                    pushUndo();
                    modificado = true;
                    repaint();
                    return;
                }

                // ===================== BORRADOR =====================
                if (herramienta == Herramienta.BORRADOR) {
                    mousePos = p;
                    Borrador b = new Borrador(p, tamBorrador);
                    b.setColorLinea(colorBorrador);
                    figuraActual = b;
                    figuras.add(figuraActual);
                    figuraSeleccionada = figuraActual;
                    pushUndo();
                    modificado = true;
                    repaint();
                    return;
                }

                // ===================== FIGURAS =====================
                switch (herramienta) {
                    case LINEA -> {
                        figuraActual = new Linea(p);
                        figuraActual.setColorLinea(colorLinea);
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case RECTANGULO -> {
                        Rectangulo r = new Rectangulo(p);
                        r.setColorLinea(colorLinea);
                        r.setColorRelleno(colorRelleno);
                        figuraActual = r;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case CIRCULO -> {
                        Circulo c = new Circulo(p);
                        c.setColorLinea(colorLinea);
                        c.setColorRelleno(colorRelleno);
                        figuraActual = c;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case OVALO -> {
                        Ovalo o = new Ovalo(p);
                        o.setColorLinea(colorLinea);
                        o.setColorRelleno(colorRelleno);
                        figuraActual = o;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case TRIANGULO -> {
                        Triangulo t = new Triangulo(p);
                        t.setColorLinea(colorLinea);
                        t.setColorRelleno(colorRelleno);
                        figuraActual = t;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case ROMBO -> {
                        Rombo r = new Rombo(p);
                        r.setColorLinea(colorLinea);
                        r.setColorRelleno(colorRelleno);
                        figuraActual = r;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case TRAPECIO -> {
                        Trapecio t = new Trapecio(p);
                        t.setColorLinea(colorLinea);
                        t.setColorRelleno(colorRelleno);
                        figuraActual = t;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case PENTAGONO -> {
                        Pentagono pe = new Pentagono(p);
                        pe.setColorLinea(colorLinea);
                        pe.setColorRelleno(colorRelleno);
                        figuraActual = pe;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case HEXAGONO -> {
                        Hexagono h = new Hexagono(p);
                        h.setColorLinea(colorLinea);
                        h.setColorRelleno(colorRelleno);
                        figuraActual = h;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case ESTRELLA -> {
                        Estrella s = new Estrella(p);
                        s.setColorLinea(colorLinea);
                        s.setColorRelleno(colorRelleno);
                        figuraActual = s;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case NUBE -> {
                        Nube n = new Nube(p);
                        n.setColorLinea(colorLinea);
                        n.setColorRelleno(colorRelleno);
                        figuraActual = n;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case ARCO -> {
                        Arco a = new Arco(p);
                        a.setColorLinea(colorLinea);
                        a.setColorRelleno(colorRelleno);
                        figuraActual = a;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case CORAZON -> {
                        Corazon c = new Corazon(p);
                        c.setColorLinea(colorLinea);
                        c.setColorRelleno(colorRelleno);
                        figuraActual = c;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case FLECHA_ARRIBA -> {
                        FlechaArriba fa = new FlechaArriba(p);
                        fa.setColorLinea(colorLinea);
                        fa.setColorRelleno(colorRelleno);
                        figuraActual = fa;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case FLECHA_ABAJO -> {
                        FlechaAbajo fb = new FlechaAbajo(p);
                        fb.setColorLinea(colorLinea);
                        fb.setColorRelleno(colorRelleno);
                        figuraActual = fb;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case FLECHA_IZQUIERDA -> {
                        FlechaIzquierda fi = new FlechaIzquierda(p);
                        fi.setColorLinea(colorLinea);
                        fi.setColorRelleno(colorRelleno);
                        figuraActual = fi;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    case FLECHA_DERECHA -> {
                        FlechaDerecha fd = new FlechaDerecha(p);
                        fd.setColorLinea(colorLinea);
                        fd.setColorRelleno(colorRelleno);
                        figuraActual = fd;
                        figuras.add(figuraActual);
                        figuraSeleccionada = figuraActual;
                        pushUndo();
                    }
                    default -> {
                    }
                }

                modificado = true;
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {

                // Mientras arrastra en modo recorte: sólo actualizar el rectángulo y salir
                if (cropMode && startPoint != null) {
                    int x = Math.min(startPoint.x, e.getX());
                    int y = Math.min(startPoint.y, e.getY());
                    int w = Math.abs(startPoint.x - e.getX());
                    int h = Math.abs(startPoint.y - e.getY());

                    cropRectangle = new Rectangle(x, y, w, h);
                    repaint();
                    return;
                }

                Point p = e.getPoint();

                if (herramienta == Herramienta.SELECCION) {
                    // 1) Arrastrando para crear el rectángulo de selección
                    if (seleccionando && inicioSeleccion != null) {
                        int x = Math.min(inicioSeleccion.x, p.x);
                        int y = Math.min(inicioSeleccion.y, p.y);
                        int w = Math.abs(inicioSeleccion.x - p.x);
                        int h = Math.abs(inicioSeleccion.y - p.y);

                        rectSeleccionTemporal = new Rectangle(x, y, w, h);
                        seleccionMultiple = obtenerFigurasEnRectangulo(rectSeleccionTemporal);
                        repaint();
                        return;
                    }

                    // 2) Redimensionar figura individual
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
                        }
                    }

                    // 3) Arrastrar selección (una o varias figuras)
                    if (arrastrando) {
                        int dx = p.x - puntoAnterior.x;
                        int dy = p.y - puntoAnterior.y;

                        if (seleccionMultiple != null && !seleccionMultiple.isEmpty()) {
                            for (Figura f : seleccionMultiple) {
                                f.desplazar(dx, dy);
                            }
                        } else if (figuraSeleccionada != null) {
                            figuraSeleccionada.desplazar(dx, dy);
                        }

                        if (fillSelection != null && fillSelectionOffset != null) {
                            fillSelectionOffset.x += dx;
                            fillSelectionOffset.y += dy;
                        }

                        puntoAnterior = p;
                        repaint();
                    }

                    return;
                }

                // Dibujo libre
                if (herramienta == Herramienta.DIBUJO_LIBRE && figuraActual instanceof DibujoLibre dl) {
                    dl.agregarPunto(p);
                    repaint();
                    return;
                }

                // Borrador
                if (herramienta == Herramienta.BORRADOR && figuraActual instanceof Borrador b) {
                    mousePos = p;
                    b.agregarPunto(p);
                    repaint();
                    return;
                }

                // Actualizar figura actual
                if (figuraActual != null) {
                    figuraActual.actualizar(p);
                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mousePos = e.getPoint();
                if (herramienta == Herramienta.BORRADOR) {
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mousePos = null;
                if (herramienta == Herramienta.BORRADOR) {
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                if (figuraActual != null
                        && herramienta != Herramienta.DIBUJO_LIBRE
                        && herramienta != Herramienta.BORRADOR
                        && herramienta != Herramienta.CUBETA) {

                    figuraSeleccionada = figuraActual;
                    seleccionMultiple.clear();
                    seleccionMultiple.add(figuraSeleccionada);
                }
                figuraActual = null;
                arrastrando = false;
                redimensionando = false;
                handleActivo = -1;
                puntoAnterior = null;

                // Si había relleno "en el aire", pegarlo en la capa fillLayer
                if (fillSelection != null && fillSelectionBounds != null) {
                    ensureFillLayer();

                    Graphics2D g2 = fillLayer.createGraphics();
                    g2.setComposite(AlphaComposite.SrcOver);
                    int finalX = fillSelectionBounds.x + fillSelectionOffset.x;
                    int finalY = fillSelectionBounds.y + fillSelectionOffset.y;
                    g2.drawImage(fillSelection, finalX, finalY, null);
                    g2.dispose();

                    fillSelection = null;
                    fillSelectionBounds = null;
                    fillSelectionOffset = new Point(0, 0);
                }

                // Finalizar selección tipo Paint
                if (herramienta == Herramienta.SELECCION && seleccionando) {
                    seleccionando = false;
                    if (seleccionMultiple == null || seleccionMultiple.isEmpty()) {
                        rectSeleccionTemporal = null;
                    }
                    repaint();
                }

                // Recorte
                if (cropMode && cropRectangle != null) {
                    recortarImagen();
                    cropMode = false;
                    repaint();
                }
            }
        };

        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        // Atajos de teclado
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "del");
        getActionMap().put("del", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        });
        // Ctrl+Y
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "redo");
        getActionMap().put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redo();
            }
        });
        // Ctrl+C
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "copy");
        getActionMap().put("copy", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copiarSeleccion();
            }
        });
        // Ctrl+V
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "paste");
        getActionMap().put("paste", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pegar();
            }
        });

        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0), "incStroke");
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), "incStroke");
        getActionMap().put("incStroke", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setGrosorActual(grosorActual + 1f);
                ajustarGrosorSeleccionado(grosorActual);
            }
        });
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "decStroke");
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), "decStroke");
        getActionMap().put("decStroke", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setGrosorActual(Math.max(1f, grosorActual - 1f));
                ajustarGrosorSeleccionado(grosorActual);
            }
        });
    }

    public Figura getFiguraSeleccionada() {
        return figuraSeleccionada;
    }

    public void setFiguraSeleccionada(Figura f) {
        this.figuraSeleccionada = f;
        repaint();
    }

    public void activarModoRecorte() {
        cropMode = true;
    }

    /**
     * Recorta la imagen según el rectángulo que el usuario seleccionó. La
     * imagen recortada se vuelve a escalar automáticamente al tamaño completo
     * del lienzo.
     */
    private void recortarImagen() {

        // Si no hay imagen o no hay área seleccionada, salir
        if (!imageHandler.hasImage() || cropRectangle == null) {
            return;
        }

        // Obtener la imagen original
        BufferedImage original = imageHandler.getImagen();

        // Calcular relación entre imagen real y tamaño del panel
        double scaleX = (double) original.getWidth() / getWidth();
        double scaleY = (double) original.getHeight() / getHeight();

        // Convertir coordenadas del panel a coordenadas reales de la imagen
        int x = (int) (cropRectangle.x * scaleX);
        int y = (int) (cropRectangle.y * scaleY);
        int w = (int) (cropRectangle.width * scaleX);
        int h = (int) (cropRectangle.height * scaleY);

        // Evitar errores si se sale de la imagen original
        x = Math.max(0, x);
        y = Math.max(0, y);
        w = Math.min(w, original.getWidth() - x);
        h = Math.min(h, original.getHeight() - y);

        // Crear la nueva imagen recortada
        BufferedImage recortada = original.getSubimage(x, y, w, h);
        // Sustituir la imagen original por la recortada
        imageHandler.setImagen(recortada);

        // Eliminar el rectángulo visual
        cropRectangle = null;
    }

    private static final int HANDLE_SIZE = 8;

    private static double calcAspect(Rectangle b) {
        return (b.height == 0) ? 1.0 : (double) b.width / (double) b.height;
    }

    private static int detectarHandle(Rectangle b, Point p) {
        Rectangle[] hs = handles(b);
        for (int i = 0; i < hs.length; i++) {
            if (hs[i].contains(p)) {
                return i;
            }
        }
        return -1;
    }

    private static Rectangle[] handles(Rectangle b) {
        int hs = HANDLE_SIZE;
        int x = b.x, y = b.y, w = b.width, h = b.height;
        return new Rectangle[]{
            new Rectangle(x - hs / 2, y - hs / 2, hs, hs), // NW 0
            new Rectangle(x + w / 2 - hs / 2, y - hs / 2, hs, hs), // N  1
            new Rectangle(x + w - hs / 2, y - hs / 2, hs, hs), // NE 2
            new Rectangle(x - hs / 2, y + h / 2 - hs / 2, hs, hs), // W  3
            new Rectangle(x + w - hs / 2, y + h / 2 - hs / 2, hs, hs), // E  4
            new Rectangle(x - hs / 2, y + h - hs / 2, hs, hs), // SW 5
            new Rectangle(x + w / 2 - hs / 2, y + h - hs / 2, hs, hs), // S  6
            new Rectangle(x + w - hs / 2, y + h - hs / 2, hs, hs) // SE 7
        };
    }

    private static Rectangle ajustarBoundsConHandle(Rectangle b, int handle, Point p, double aspect) {
        int x1 = b.x, y1 = b.y, x2 = b.x + b.width, y2 = b.y + b.height;

        switch (handle) {
            case 0 -> {
                x1 = p.x;
                y1 = p.y;
            }
            case 1 -> {
                y1 = p.y;
            }
            case 2 -> {
                x2 = p.x;
                y1 = p.y;
            }
            case 3 -> {
                x1 = p.x;
            }
            case 4 -> {
                x2 = p.x;
            }
            case 5 -> {
                x1 = p.x;
                y2 = p.y;
            }
            case 6 -> {
                y2 = p.y;
            }
            case 7 -> {
                x2 = p.x;
                y2 = p.y;
            }
        }
        int nx = Math.min(x1, x2);
        int ny = Math.min(y1, y2);
        int nw = Math.abs(x2 - x1);
        int nh = Math.abs(y2 - y1);

        if (nh == 0) {
            nh = 1;
        }
        double cur = (double) nw / nh;

        if (cur > aspect) {
            nh = (int) Math.round(nw / aspect);
        } else {
            nw = (int) Math.round(nh * aspect);
        }

        // Reubica segun handle para mantener la esquina opuesta fija
        switch (handle) {
            case 0 -> {
                nx = x2 - nw;
                ny = y2 - nh;
            }
            case 1 -> {
                nx = x1;
                ny = y2 - nh;
            }
            case 2 -> {
                nx = x1;
                ny = y2 - nh;
            }
            case 3 -> {
                nx = x2 - nw;
                ny = y1;
            }
            case 4 -> {
                nx = x1;
                ny = y1;
            }
            case 5 -> {
                nx = x2 - nw;
                ny = y1;
            }
            case 6 -> {
                nx = x1;
                ny = y1;
            }
            case 7 -> {
                nx = x1;
                ny = y1;
            }
        }
        return new Rectangle(nx, ny, nw, nh);
    }

    // ==== Pintado ====
    @Override

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

//Fondo (imagen)
        if (imageHandler != null && imageHandler.hasImage()) {
            imageHandler.drawImage(g2, getWidth(), getHeight());
        } else {
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

// Capa de relleno fija
        if (fillLayer != null) {
            g2.drawImage(fillLayer, 0, 0, null);
        }

// Si hay una selección de relleno moviéndose, dibujarla encima
        if (fillSelection != null && fillSelectionBounds != null) {
            int dx = (fillSelectionOffset != null) ? fillSelectionOffset.x : 0;
            int dy = (fillSelectionOffset != null) ? fillSelectionOffset.y : 0;
            g2.drawImage(
                    fillSelection,
                    fillSelectionBounds.x + dx,
                    fillSelectionBounds.y + dy,
                    null
            );
        }

// Luego las figuras
        for (Figura f : figuras) {
            f.dibujar(g2);
        }
//---------------------------------------------------------------------------------
        // 1.5) Rectángulo guía mientras se dibuja una figura (tipo Paint)
        if (figuraActual != null
                && herramienta != Herramienta.DIBUJO_LIBRE
                && herramienta != Herramienta.BORRADOR
                && herramienta != Herramienta.CUBETA
                && herramienta != Herramienta.SELECCION) {

            Rectangle b = figuraActual.getBounds();
            if (b != null) {
                Stroke old = g2.getStroke();
                Color oldC = g2.getColor();
                float[] dash = {4f, 4f};
                g2.setColor(new Color(0, 120, 215));
                g2.setStroke(new BasicStroke(
                        1.0f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10f,
                        dash,
                        0f
                ));
                g2.drawRect(b.x, b.y, b.width, b.height);
                g2.setStroke(old);
                g2.setColor(oldC);
            }
        }

//----------------------------------------------------  
        // Silueta del borrador (overlay)
        if (herramienta == Herramienta.SELECCION && seleccionando && rectSeleccionTemporal != null) {
            Stroke old = g2.getStroke();
            Color oldC = g2.getColor();
            float[] dash = {4f, 4f};
            g2.setColor(new Color(0, 120, 215));
            g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dash, 0f));
            g2.drawRect(rectSeleccionTemporal.x, rectSeleccionTemporal.y,
                    rectSeleccionTemporal.width, rectSeleccionTemporal.height);
            g2.setStroke(old);
            g2.setColor(oldC);
        }

        // 2) Rectángulo que rodea a la selección actual (una o varias figuras)
// 2) Rectángulo que rodea a la selección actual (una o varias figuras),
//    solo si TODAS son rellenables
        Rectangle bbSel = getBoundsSeleccionMultiple();
        if (herramienta == Herramienta.SELECCION && bbSel != null && seleccionSoloRellenables()) {
            Stroke old = g2.getStroke();
            Color oldC = g2.getColor();
            float[] dash = {6f, 6f};
            g2.setColor(new Color(0, 120, 215));
            g2.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dash, 0f));
            g2.drawRect(bbSel.x, bbSel.y, bbSel.width, bbSel.height);
            g2.setStroke(old);
            g2.setColor(oldC);
        }

        // 3) Si solo hay una figura seleccionada y es rellenable, muestra los handles (como antes)
        // 3) Si solo hay una figura seleccionada y es rellenable, muestra los handles
        if (figuraSeleccionada != null
                && seleccionMultiple.size() == 1
                && figuraSeleccionada instanceof FiguraRellenable) {

            Rectangle b = figuraSeleccionada.getBounds();
            Stroke old = g2.getStroke();
            float[] dash = {6f, 6f};
            g2.setColor(new Color(220, 50, 50));
            g2.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dash, 0f));
            g2.drawRect(b.x, b.y, b.width, b.height);
            g2.setStroke(old);

            for (Rectangle h : handles(b)) {
                g2.fillRect(h.x, h.y, h.width, h.height);
            }
        }

        // 5. Área de recorte (DEBE IR AL FINAL)
        if (cropMode && cropRectangle != null) {
            // Oscurecer el área fuera del recorte
            Composite oldComp = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2.setColor(Color.BLACK);

            // Dibujar rectángulos alrededor del área de recorte
            g2.fillRect(0, 0, getWidth(), cropRectangle.y); // Arriba
            g2.fillRect(0, cropRectangle.y, cropRectangle.x, cropRectangle.height); // Izquierda
            g2.fillRect(cropRectangle.x + cropRectangle.width, cropRectangle.y,
                    getWidth() - cropRectangle.x - cropRectangle.width, cropRectangle.height); // Derecha
            g2.fillRect(0, cropRectangle.y + cropRectangle.height,
                    getWidth(), getHeight() - cropRectangle.y - cropRectangle.height); // Abajo

            g2.setComposite(oldComp);

            // Línea punteada azul
            float[] dash = {10f};
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dash, 0.0f));
            g2.setColor(Color.CYAN);
            g2.draw(cropRectangle);

            // Mostrar dimensiones
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            String dim = cropRectangle.width + " × " + cropRectangle.height;
            g2.drawString(dim, cropRectangle.x + 5, cropRectangle.y - 5);
        }
    }

    public void setHerramienta(Herramienta h) {
        this.herramienta = h;
        if (h != Herramienta.SELECCION) {
            figuraSeleccionada = null;
            seleccionMultiple.clear();
            seleccionando = false;
            rectSeleccionTemporal = null;
            repaint();
        }
    }

    public Herramienta getHerramienta() {
        return herramienta;
    }

    public void setColorLinea(Color c) {
        this.colorLinea = c;
    }

    public void setColorRelleno(Color c) {
        this.colorRelleno = c;
    }

    public void setGrosorActual(float g) {
        this.grosorActual = Math.max(1f, g);
    }

    public float getGrosorActual() {
        return grosorActual;
    }

    public void setTamBorrador(float t) {
        this.tamBorrador = Math.max(1f, t);
        repaint();
    }

    public float getTamBorrador() {
        return tamBorrador;
    }

    public void setColorBorrador(Color c) {
        this.colorBorrador = (c != null ? c : Color.WHITE);
        repaint();
    }

    public Color getColorBorrador() {
        return colorBorrador;
    }

    public void ajustarGrosorSeleccionado(float g) {
        if (figuraSeleccionada instanceof DibujoLibre dl) {
            dl.setGrosor(g);
            repaint();
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

    // ==== Copiar / Pegar ====
    public void copiarSeleccion() {
        portapapeles.clear();
        if (figuraSeleccionada != null) {
            portapapeles.add(figuraSeleccionada.clonarConDesplazamiento(0, 0));
        }
    }

    /**
     * Devuelve todas las figuras que intersectan el rectángulo dado.
     */
    private java.util.List<Figura> obtenerFigurasEnRectangulo(Rectangle r) {
        java.util.List<Figura> res = new ArrayList<>();
        if (r == null) {
            return res;
        }
        for (Figura f : figuras) {
            Rectangle b = f.getBounds();
            if (b != null && b.intersects(r)) {
                res.add(f);
            }
        }
        return res;
    }

    /**
     * Bounding box de la selección múltiple (o null si no hay nada
     * seleccionado).
     */
    private Rectangle getBoundsSeleccionMultiple() {
        if (seleccionMultiple == null || seleccionMultiple.isEmpty()) {
            return null;
        }
        Rectangle bb = new Rectangle(seleccionMultiple.get(0).getBounds());
        for (int i = 1; i < seleccionMultiple.size(); i++) {
            bb = bb.union(seleccionMultiple.get(i).getBounds());
        }
        return bb;
    }

    public void pegar() {
        if (portapapeles.isEmpty()) {
            return;
        }
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
        fillLayer = null;      // limpiamos también los rellenos de la cubeta
        modificado = true;
        repaint();
    }

    // ==== Undo/Redo ====
    private void pushUndo() {
        java.util.List<Figura> snap = new ArrayList<>();
        for (Figura f : figuras) {
            snap.add(f.clonarConDesplazamiento(0, 0));
        }
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
                for (Object o : lista) {
                    figuras.add((Figura) o);
                }
                figuraSeleccionada = null;
                modificado = false;
                repaint();
            } else {
                throw new IOException("Formato de proyecto inválido.");
            }
        }
    }

    public boolean tieneImagen() {
        return imageHandler != null && imageHandler.hasImage();
    }

    public void cargarImagen(JFrame parent) {
        if (imageHandler.loadImage(parent)) {
            repaint();
        }
    }

    // Elimina la imagen actual del lienzo y todas las figuras dibujadas encima de ella
    public void eliminarImagenYContenido() {
        if (imageHandler != null && imageHandler.hasImage()) {
            pushUndo();
            Rectangle areaImagen = imageHandler.getImageBounds(getWidth(), getHeight());

            if (areaImagen != null) {
                figuras.removeIf(figura -> areaImagen.intersects(figura.getBounds()));
            }
            imageHandler.clear();
            figuraSeleccionada = null;
            modificado = true;
            repaint();
        }
    }

    public BufferedImage getImagen() {
        return imageHandler != null ? imageHandler.getImagen() : null;
    }

    public void setImagen(BufferedImage img) {
        if (imageHandler != null) {
            pushUndo(); // Guarda estado previo para undo
            imageHandler.setImagen(img);
            modificado = true;
            repaint();
        }
    }

    public void limpiarImagen() {
        if (imageHandler != null) {
            imageHandler.clear();
            repaint();
        }
    }

    public boolean isModificado() {
        return modificado;
    }

    //-------------------------------------------------
    // Asegura que la capa de relleno tenga el tamaño del panel
    private void ensureFillLayer() {
        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) {
            return;
        }

        if (fillLayer == null || fillLayer.getWidth() != w || fillLayer.getHeight() != h) {
            fillLayer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = fillLayer.createGraphics();
            g2.setComposite(AlphaComposite.Clear);
            g2.fillRect(0, 0, w, h);
            g2.dispose();
        }
    }

// Aplica la cubeta tipo Paint usando flood fill
    private void aplicarCubeta(Point p) {
        if (colorRelleno == null) {
            return;   // sin color de relleno, no hacemos nada
        }
        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) {
            return;
        }

        ensureFillLayer();

        // Crear snapshot con fondo + rellenos previos + figuras (para detectar bordes)
        BufferedImage snapshot = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = snapshot.createGraphics();

        // Fondo
        if (imageHandler != null && imageHandler.hasImage()) {
            imageHandler.drawImage(g2, w, h);
        } else {
            g2.setColor(getBackground());
            g2.fillRect(0, 0, w, h);
        }

        // Rellenos ya existentes
        if (fillLayer != null) {
            g2.drawImage(fillLayer, 0, 0, null);
        }

        // Figuras (sus líneas servirán de "pared" para el flood fill)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Figura f : figuras) {
            f.dibujar(g2);
        }
        g2.dispose();

        int x = p.x;
        int y = p.y;
        if (x < 0 || y < 0 || x >= w || y >= h) {
            return;
        }

        int target = snapshot.getRGB(x, y);
        int replacement = colorRelleno.getRGB();
        if (target == replacement) {
            return;
        }

        floodFill(snapshot, fillLayer, x, y, target, replacement);
        modificado = true;
        repaint();
    }

// Flood fill clásico en 4 direcciones
    private void floodFill(BufferedImage ref, BufferedImage dest,
            int x, int y, int target, int replacement) {

        int w = ref.getWidth();
        int h = ref.getHeight();

        java.util.ArrayDeque<Point> stack = new java.util.ArrayDeque<>();
        stack.push(new Point(x, y));

        while (!stack.isEmpty()) {
            Point p = stack.pop();
            int px = p.x;
            int py = p.y;

            if (px < 0 || py < 0 || px >= w || py >= h) {
                continue;
            }

            if (ref.getRGB(px, py) != target) {
                continue;
            }

            // Marcamos visitado y pintamos en la capa de relleno
            ref.setRGB(px, py, replacement);
            dest.setRGB(px, py, replacement);

            stack.push(new Point(px + 1, py));
            stack.push(new Point(px - 1, py));
            stack.push(new Point(px, py + 1));
            stack.push(new Point(px, py - 1));
        }
    }

    //--------------------------------------------------------------
    private void prepararSeleccionRelleno(Rectangle area) {
        if (fillLayer == null || area == null) {
            fillSelection = null;
            fillSelectionBounds = null;
            fillSelectionOffset = new Point(0, 0);
            return;
        }

        int wImg = fillLayer.getWidth();
        int hImg = fillLayer.getHeight();

        int x = Math.max(0, area.x);
        int y = Math.max(0, area.y);
        int w = Math.min(area.width, wImg - x);
        int h = Math.min(area.height, hImg - y);

        if (w <= 0 || h <= 0) {
            fillSelection = null;
            fillSelectionBounds = null;
            fillSelectionOffset = new Point(0, 0);
            return;
        }

        fillSelectionBounds = new Rectangle(x, y, w, h);
        fillSelectionOffset = new Point(0, 0);

        // Copiar la parte de relleno seleccionada
        fillSelection = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = fillSelection.createGraphics();
        g2.drawImage(fillLayer.getSubimage(x, y, w, h), 0, 0, null);
        g2.dispose();

        // Limpiar esa zona en la capa principal
        Graphics2D g = fillLayer.createGraphics();
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(x, y, w, h);
        g.dispose();
    }

    /**
     * Indica si todas las figuras seleccionadas son rellenables.
     */
    private boolean seleccionSoloRellenables() {
        if (seleccionMultiple == null || seleccionMultiple.isEmpty()) {
            return false;
        }
        for (Figura f : seleccionMultiple) {
            if (!(f instanceof FiguraRellenable)) {
                return false;
            }
        }
        return true;
    }

}
