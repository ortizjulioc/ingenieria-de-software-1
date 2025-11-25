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
   // ImageHandler gestiona la carga, visualización y limpieza de una imagen de fondo
     private ImageHandler imageHandler = new ImageHandler();
  
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

        MouseAdapter mouse;
        mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                
                // Si el modo recorte está activado
                // Se guarda el punto inicial del arrastre del mouse
                    
                if (cropMode) {
                    startPoint = e.getPoint();
                    cropRectangle = new Rectangle();
                }
                
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
                
                // Mientras arrastra el mouse en modo recorte
                // se va formando el rectángulo visual
                if (cropMode && startPoint != null) {
                int x = Math.min(startPoint.x, e.getX());
                int y = Math.min(startPoint.y, e.getY());
                int w = Math.abs(startPoint.x - e.getX());
                int h = Math.abs(startPoint.y - e.getY());
              
                // Se crea el rectángulo con las nuevas coordenadas
                cropRectangle = new Rectangle(x, y, w, h);
                repaint();
                return;
               }
                
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
              
                repaint();
                      
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
                
                // Al soltar el mouse, si estaba en modo recorte,
                // se procede a recortar la imagen
                if (cropMode && cropRectangle != null) {
                    recortarImagen();  // Se ejecuta el recorte
                    cropMode = false;  // Se desactiva el modo recorte
                    repaint();          // Se redibuja el panel
                }          
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
 * Recorta la imagen según el rectángulo que el usuario seleccionó.
 * La imagen recortada se vuelve a escalar automáticamente
 * al tamaño completo del lienzo.
 */
    private void recortarImagen() {

        // Si no hay imagen o no hay área seleccionada, salir
        if (!imageHandler.hasImage() || cropRectangle == null) return;

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
        
         if (imageHandler != null && imageHandler.hasImage()) { 
          imageHandler.drawImage(g, getWidth(), getHeight()); 
       }
         
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        
        if (imageHandler != null && imageHandler.hasImage()) {
            imageHandler.drawImage(g, getWidth(), getHeight());
        }

        

        
          // Dibujar todas las figuras

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
 
        // dibuja seleccion de figura)
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

    public boolean isModificado() { return modificado; }
}
