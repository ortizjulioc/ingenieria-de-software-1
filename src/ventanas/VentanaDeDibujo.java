package ventanas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class VentanaDeDibujo extends JFrame {

    private final PanelDeDibujo panel;          // Vista de lienzo 
    private final ModeloDibujo modelo;          // Modelo global 
    private final DibujoController controller;  // Controlador 

    private JPanel panelPropiedades;
    private boolean propsVisible = true;

   
    private Color colorLinea = Color.BLACK;
    private Color colorRelleno = Color.WHITE;

    
    private final JLabel statusLabel = new JLabel("Listo");

    public VentanaDeDibujo() {
        super("Editor de Dibujo 2D – MVC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 760);
        setLocationRelativeTo(null);

        // ===== MVC wiring =====
        panel = new PanelDeDibujo();
        modelo = new ModeloDibujo();
        controller = new DibujoController(modelo, panel);

        getContentPane().add(panel, BorderLayout.CENTER);

        setJMenuBar(crearMenu());
        getContentPane().add(crearToolbar(), BorderLayout.NORTH);
        getContentPane().add(crearPanelPropiedades(), BorderLayout.EAST);
        getContentPane().add(crearStatusBar(), BorderLayout.SOUTH);

        
        MouseAdapter mouseStatus = new MouseAdapter() {
            @Override public void mouseMoved(MouseEvent e) { updateStatus(e.getPoint()); }
            @Override public void mouseDragged(MouseEvent e) { updateStatus(e.getPoint()); }
            @Override public void mouseExited(MouseEvent e) { statusLabel.setText(dimTexto()); }
        };
        panel.addMouseMotionListener(mouseStatus);
        panel.addMouseListener(mouseStatus);

        
        colorLinea = modelo.getColorLinea();
        colorRelleno = modelo.getColorRelleno();
    }

    private void updateStatus(Point p) {
        if (p == null) { statusLabel.setText(dimTexto()); return; }
        statusLabel.setText("x: " + p.x + "  y: " + p.y + "   |   " + dimTexto());
    }

    private String dimTexto() {
        return "Lienzo: " + panel.getWidth() + "×" + panel.getHeight();
    }

    // ===== Menú =====
    private JMenuBar crearMenu() {
        JMenuBar mb = new JMenuBar();

        JMenu mArchivo = new JMenu("Archivo");
        JMenuItem itNuevo = new JMenuItem("Nuevo");
        itNuevo.addActionListener(e -> {
            if (controller.isModificado()) {
                int r = JOptionPane.showConfirmDialog(this,
                        "¿Deseas guardar los cambios actuales?",
                        "Nuevo dibujo", JOptionPane.YES_NO_CANCEL_OPTION);
                if (r == JOptionPane.CANCEL_OPTION || r == JOptionPane.CLOSED_OPTION) return;
                if (r == JOptionPane.YES_OPTION) {
                    JFileChooser fc = new JFileChooser();
                    fc.setDialogTitle("Guardar proyecto");
                    if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                        try { controller.guardarProyecto(fc.getSelectedFile()); }
                        catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage()); return; }
                    } else return;
                }
            }
            controller.limpiarLienzo();
        });
        JMenuItem itAbrir = new JMenuItem("Abrir...");
        itAbrir.addActionListener(e -> {
            if (controller.isModificado()) {
                int r = JOptionPane.showConfirmDialog(this,
                        "Tienes cambios sin guardar. ¿Continuar y descartarlos?",
                        "Abrir proyecto", JOptionPane.YES_NO_OPTION);
                if (r != JOptionPane.YES_OPTION) return;
            }
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Abrir proyecto");
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try { controller.abrirProyecto(fc.getSelectedFile()); }
                catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error al abrir: " + ex.getMessage()); }
            }
        });
        JMenuItem itGuardar = new JMenuItem("Guardar proyecto...");
        itGuardar.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Guardar proyecto");
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try { controller.guardarProyecto(fc.getSelectedFile()); }
                catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage()); }
            }
        });
        JMenuItem itExportar = new JMenuItem("Exportar PNG...");
        itExportar.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Exportar como PNG");
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try { controller.exportarComoPNG(fc.getSelectedFile()); }
                catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage()); }
            }
        });

        mArchivo.add(itNuevo);
        mArchivo.add(itAbrir);
        mArchivo.addSeparator();
        mArchivo.add(itGuardar);
        mArchivo.add(itExportar);
        mb.add(mArchivo);

        JMenu mEditar = new JMenu("Editar");
        JMenuItem itUndo = new JMenuItem("Deshacer (Ctrl+Z)");
        itUndo.addActionListener(e -> controller.undo());
        JMenuItem itRedo = new JMenuItem("Rehacer (Ctrl+Y)");
        itRedo.addActionListener(e -> controller.redo());
        JMenuItem itCopy = new JMenuItem("Copiar (Ctrl+C)");
        itCopy.addActionListener(e -> controller.copiarSeleccion());
        JMenuItem itPaste = new JMenuItem("Pegar (Ctrl+V)");
        itPaste.addActionListener(e -> controller.pegar());
        JMenuItem itLimpiar = new JMenuItem("Limpiar todo");
        itLimpiar.addActionListener(e -> {
            int r = JOptionPane.showConfirmDialog(this, "¿Borrar todo el lienzo?", "Limpiar", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) controller.limpiarLienzo();
        });

        mEditar.add(itUndo);
        mEditar.add(itRedo);
        mEditar.addSeparator();
        mEditar.add(itCopy);
        mEditar.add(itPaste);
        mEditar.addSeparator();
        mEditar.add(itLimpiar);
        mb.add(mEditar);

        
        JMenu mAyuda = new JMenu("Ayuda");
        JMenuItem itGuia = new JMenuItem("Guía de uso");
        itGuia.addActionListener(e -> mostrarDialogoAyuda());
        JMenuItem itAcerca = new JMenuItem("Acerca de…");
        itAcerca.addActionListener(e -> mostrarDialogoAcercaDe());
        mAyuda.add(itGuia);
        mAyuda.addSeparator();
        mAyuda.add(itAcerca);
        mb.add(mAyuda);

        return mb;
    }

 
    private JToolBar crearToolbar() {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);
        tb.setRollover(true);
        tb.setBorder(new EmptyBorder(6, 8, 6, 8));
        tb.setBackground(new Color(245, 247, 250)); // gris muy claro

        // Helper de estilo
        java.util.function.Consumer<AbstractButton> stylize = btn -> {
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
            btn.setOpaque(true);
            btn.setBackground(new Color(245, 247, 250));
            btn.addChangeListener(e -> {
                if (btn.isSelected()) btn.setBackground(new Color(225, 230, 236));
            });
            btn.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    if(!btn.isSelected()) btn.setBackground(new Color(232, 236, 240));
                }
                @Override public void mouseExited(MouseEvent e) {
                    if(!btn.isSelected()) btn.setBackground(new Color(245, 247, 250));
                }
            });
        };

        ButtonGroup group = new ButtonGroup();

        // Grupo 1
        JToggleButton btSel = new JToggleButton(new ShapeIcon(IconType.CURSOR));
        btSel.setToolTipText("Selección (V)");
        btSel.addActionListener(e -> controller.setHerramienta(ModeloDibujo.Herramienta.SELECCION));
        btSel.setSelected(true);
        stylize.accept(btSel); group.add(btSel); tb.add(btSel);

        JToggleButton btLapiz = new JToggleButton(new ShapeIcon(IconType.PENCIL));
        btLapiz.setToolTipText("Dibujo libre (B)");
        btLapiz.addActionListener(e -> controller.setHerramienta(ModeloDibujo.Herramienta.DIBUJO_LIBRE));
        stylize.accept(btLapiz); group.add(btLapiz); tb.add(btLapiz);

        JToggleButton btBorr = new JToggleButton(new ShapeIcon(IconType.ERASER));
        btBorr.setToolTipText("Borrador (E)");
        btBorr.addActionListener(e -> controller.setHerramienta(ModeloDibujo.Herramienta.BORRADOR));
        stylize.accept(btBorr); group.add(btBorr); tb.add(btBorr);

        tb.addSeparator(new Dimension(12,0));

        // Grupo 2 – Figuras (menú)
        JButton btFig = new JButton(new ShapeIcon(IconType.SHAPES));
        btFig.setToolTipText("Figuras");
        stylize.accept(btFig);

        JPopupMenu menuFig = crearMenuFiguras(group, btSel, btLapiz, btBorr);
        btFig.addActionListener(e -> menuFig.show(btFig, 0, btFig.getHeight()));
        tb.add(btFig);

        tb.addSeparator(new Dimension(12,0));

        // Grupo 3 – Colores (swatches)
        ColorSwatchButton swLinea = new ColorSwatchButton(colorLinea);
        swLinea.setToolTipText("Color de línea");
        swLinea.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Selecciona color de línea", colorLinea);
            if (c != null) { colorLinea = c; controller.setColorLinea(c); swLinea.setColor(c); }
        });
        tb.add(swLinea);

        ColorSwatchButton swRelleno = new ColorSwatchButton(colorRelleno);
        swRelleno.setToolTipText("Color de relleno");
        swRelleno.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Selecciona color de relleno", colorRelleno);
            if (c != null) { colorRelleno = c; controller.setColorRelleno(c); swRelleno.setColor(c); }
        });
        tb.add(swRelleno);

        JToggleButton btCubeta = new JToggleButton(new ShapeIcon(IconType.BUCKET));
        btCubeta.setToolTipText("Cubeta de pintura");
        stylize.accept(btCubeta); group.add(btCubeta); tb.add(btCubeta);
        btCubeta.addActionListener(e -> controller.setHerramienta(ModeloDibujo.Herramienta.CUBETA));

        tb.addSeparator(new Dimension(12,0));

        // Grupo 4 – Propiedades (toggle panel)
        JButton toggleProps = new JButton(new ShapeIcon(IconType.PANELS));
        toggleProps.setToolTipText("Mostrar/Ocultar propiedades");
        stylize.accept(toggleProps);
        toggleProps.addActionListener(e -> togglePropiedades());
        tb.add(toggleProps);

        return tb;
    }

    private void togglePropiedades() {
        propsVisible = !propsVisible;
        panelPropiedades.setVisible(propsVisible);
        revalidate();
        repaint();
    }

    // Menú de figuras (desplegable)
    private JPopupMenu crearMenuFiguras(ButtonGroup group, JToggleButton... toUnselect) {
        JPopupMenu popup = new JPopupMenu();

        JMenu basicas = new JMenu("Formas básicas");
        basicas.setIcon(new ShapeIcon(IconType.CAT_BASIC));
        basicas.add(itemFigura("Línea", ModeloDibujo.Herramienta.LINEA, IconType.LINE, toUnselect));
        basicas.add(itemFigura("Rectángulo", ModeloDibujo.Herramienta.RECTANGULO, IconType.RECT, toUnselect));
        basicas.add(itemFigura("Círculo", ModeloDibujo.Herramienta.CIRCULO, IconType.CIRC, toUnselect));
        basicas.add(itemFigura("Óvalo", ModeloDibujo.Herramienta.OVALO, IconType.OVAL, toUnselect));
        basicas.add(itemFigura("Triángulo", ModeloDibujo.Herramienta.TRIANGULO, IconType.TRI, toUnselect));

        JMenu complejas = new JMenu("Formas complejas");
        complejas.setIcon(new ShapeIcon(IconType.CAT_COMPLEX));
        complejas.add(itemFigura("Corazón", ModeloDibujo.Herramienta.CORAZON, IconType.HEART, toUnselect));
        complejas.add(itemFigura("Rombo", ModeloDibujo.Herramienta.ROMBO, IconType.RHOMB, toUnselect));
        complejas.add(itemFigura("Trapecio", ModeloDibujo.Herramienta.TRAPECIO, IconType.TRAP, toUnselect));
        complejas.add(itemFigura("Pentágono", ModeloDibujo.Herramienta.PENTAGONO, IconType.PENTA, toUnselect));
        complejas.add(itemFigura("Hexágono", ModeloDibujo.Herramienta.HEXAGONO, IconType.HEXA, toUnselect));
        complejas.add(itemFigura("Estrella", ModeloDibujo.Herramienta.ESTRELLA, IconType.STAR, toUnselect));

        JMenu flechas = new JMenu("Flechas");
        flechas.setIcon(new ShapeIcon(IconType.CAT_ARROWS));
        flechas.add(itemFigura("Flecha 1 (↑)", ModeloDibujo.Herramienta.FLECHA_ARRIBA, IconType.ARROW_UP, toUnselect));
        flechas.add(itemFigura("Flecha 2 (↓)", ModeloDibujo.Herramienta.FLECHA_ABAJO, IconType.ARROW_DOWN, toUnselect));
        flechas.add(itemFigura("Flecha 3 (←)", ModeloDibujo.Herramienta.FLECHA_IZQUIERDA, IconType.ARROW_LEFT, toUnselect));
        flechas.add(itemFigura("Flecha 4 (→)", ModeloDibujo.Herramienta.FLECHA_DERECHA, IconType.ARROW_RIGHT, toUnselect));

        JMenu otros = new JMenu("Otros");
        otros.setIcon(new ShapeIcon(IconType.CAT_MISC));
        otros.add(itemFigura("Nube", ModeloDibujo.Herramienta.NUBE, IconType.CLOUD, toUnselect));
        otros.add(itemFigura("Arco", ModeloDibujo.Herramienta.ARCO, IconType.ARC, toUnselect));

        popup.add(basicas);
        popup.add(complejas);
        popup.add(flechas);
        popup.add(otros);
        return popup;
    }

    private JMenuItem itemFigura(String name, ModeloDibujo.Herramienta tool, IconType icon, JToggleButton... toUnselect) {
        JMenuItem it = new JMenuItem(name, new ShapeIcon(icon));
        it.addActionListener(e -> {
            controller.setHerramienta(tool);
            for (JToggleButton b : toUnselect) b.setSelected(false);
        });
        return it;
    }

    // ===== Panel de propiedades (derecha) =====
    private JPanel crearPanelPropiedades() {
        panelPropiedades = new JPanel(new GridBagLayout());
        panelPropiedades.setBorder(new TitledBorder("Propiedades"));
        panelPropiedades.setPreferredSize(new Dimension(260, 0));
        panelPropiedades.setBackground(Color.WHITE);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 10, 8, 10);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0;

        // Grosor pincel
        panelPropiedades.add(new JLabel("Grosor pincel"), c);
        c.gridy++;
        JSpinner spGrosor = new JSpinner(new SpinnerNumberModel(2, 1, 50, 1));
        spGrosor.addChangeListener(e -> {
            float g = ((Integer) spGrosor.getValue()).floatValue();
            controller.setGrosorActual(g);
        });
        panelPropiedades.add(spGrosor, c);

        // Tamaño borrador
        c.gridy++;
        panelPropiedades.add(new JLabel("Tamaño borrador"), c);
        c.gridy++;
        JSpinner spBorr = new JSpinner(new SpinnerNumberModel(12, 1, 100, 1));
        spBorr.addChangeListener(e -> controller.setTamBorrador(((Integer) spBorr.getValue()).floatValue()));
        panelPropiedades.add(spBorr, c);

        // Color borrador
        c.gridy++;
        panelPropiedades.add(new JLabel("Color borrador"), c);
        c.gridy++;
        ColorSwatchButton swBorr = new ColorSwatchButton(Color.WHITE);
        swBorr.addActionListener(e -> {
            Color cPick = JColorChooser.showDialog(this, "Selecciona color de borrador", swBorr.getColor());
            if (cPick != null) { swBorr.setColor(cPick); controller.setColorBorrador(cPick); }
        });
        panelPropiedades.add(swBorr, c);

        // Color de línea
        c.gridy++;
        panelPropiedades.add(new JLabel("Color de línea"), c);
        c.gridy++;
        ColorSwatchButton swLineaProp = new ColorSwatchButton(colorLinea);
        swLineaProp.addActionListener(e -> {
            Color cPick = JColorChooser.showDialog(this, "Selecciona color de línea", swLineaProp.getColor());
            if (cPick != null) {
                colorLinea = cPick;
                swLineaProp.setColor(cPick);
                controller.setColorLinea(cPick);
            }
        });
        panelPropiedades.add(swLineaProp, c);

        // Color de relleno
        c.gridy++;
        panelPropiedades.add(new JLabel("Color de relleno"), c);
        c.gridy++;
        ColorSwatchButton swRellenoProp = new ColorSwatchButton(colorRelleno);
        swRellenoProp.addActionListener(e -> {
            Color cPick = JColorChooser.showDialog(this, "Selecciona color de relleno", swRellenoProp.getColor());
            if (cPick != null) {
                colorRelleno = cPick;
                swRellenoProp.setColor(cPick);
                controller.setColorRelleno(cPick);
            }
        });
        panelPropiedades.add(swRellenoProp, c);

        // Spacer
        c.gridy++; c.weighty = 1.0;
        panelPropiedades.add(Box.createVerticalGlue(), c);

        return panelPropiedades;
    }

    private JComponent crearStatusBar() {
        JPanel status = new JPanel(new BorderLayout());
        status.setBorder(new EmptyBorder(4, 8, 4, 8));
        status.setBackground(new Color(250, 250, 252));
        statusLabel.setForeground(new Color(90, 98, 110));
        status.add(statusLabel, BorderLayout.WEST);
        statusLabel.setText(dimTexto());
        return status;
    }

    // ======= Iconos vectoriales internos (ShapeIcon) =======
    enum IconType {
        CURSOR, PENCIL, ERASER, SHAPES, BUCKET, PANELS,
        CAT_BASIC, CAT_COMPLEX, CAT_ARROWS, CAT_MISC,
        LINE, RECT, CIRC, OVAL, TRI,
        HEART, RHOMB, TRAP, PENTA, HEXA, STAR,
        ARROW_UP, ARROW_DOWN, ARROW_LEFT, ARROW_RIGHT,
        CLOUD, ARC
    }

    static class ShapeIcon implements Icon {
        private final IconType type;
        private final int w, h;
        ShapeIcon(IconType t) { this(t, 18, 18); }
        ShapeIcon(IconType t, int w, int h) { this.type = t; this.w = w; this.h = h; }

        @Override public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(x, y);
            g2.setColor(new Color(60, 72, 88));
            g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            switch (type) {
                case CURSOR -> { Polygon p = new Polygon(new int[]{2, 2, 12}, new int[]{2, 14, 8}, 3); g2.fillPolygon(p); }
                case PENCIL -> { g2.drawLine(2, h-4, w-6, 4); g2.fillRect(w-8, 2, 4, 4); g2.fillRect(2, h-6, 4, 4); }
                case ERASER -> { g2.drawRoundRect(2, 4, w-6, h-8, 4, 4); g2.drawLine(5, h-4, w-6, h-4); }
                case SHAPES -> { g2.drawRect(1, 1, 9, 9); g2.drawOval(7, 7, 10, 10); }
                case BUCKET -> { g2.drawRect(4, 2, 10, 8); g2.drawLine(4, 6, 14, 6); g2.fillOval(2, 12, 6, 4); }
                case PANELS -> { g2.drawRect(2, 2, w-4, h-4); g2.drawLine(2, 8, w-2, 8); g2.drawLine(w/2, 8, w/2, h-2); }

                case CAT_BASIC -> { g2.drawRect(2, 2, 6, 6); g2.drawOval(10, 10, 6, 6); }
                case CAT_COMPLEX -> { g2.drawOval(2, 2, 6, 6); drawStar(g2, 13, 6, 6, 3, 5); }
                case CAT_ARROWS -> { g2.drawLine(3, h/2, w-3, h/2); g2.drawLine(w-6, h/2-3, w-3, h/2); g2.drawLine(w-6, h/2+3, w-3, h/2); }
                case CAT_MISC -> { g2.drawOval(3, 6, 6, 5); g2.drawArc(11, 4, 6, 10, 200, 140); }

                case LINE -> { g2.drawLine(2, h-4, w-2, 4); }
                case RECT -> { g2.drawRect(3, 3, w-6, h-6); }
                case CIRC -> { g2.drawOval(3, 3, w-6, h-6); }
                case OVAL -> { g2.drawOval(2, 5, w-4, h-10); }
                case TRI -> { Polygon tri = new Polygon(new int[]{w/2, 3, w-3}, new int[]{3, h-3, h-3}, 3); g2.drawPolygon(tri); }
                case HEART -> { g2.drawArc(3, 3, 6, 6, 0, 180); g2.drawArc(9, 3, 6, 6, 0, 180); g2.drawLine(3, 6, w/2, h-3); g2.drawLine(w-3, 6, w/2, h-3); }
                case RHOMB -> { Polygon rh = new Polygon(new int[]{w/2, w-4, w/2, 4}, new int[]{3, h/2, h-3, h/2}, 4); g2.drawPolygon(rh); }
                case TRAP -> { Polygon trp = new Polygon(new int[]{4, w-4, w-6, 6}, new int[]{h-4, h-4, 4, 4}, 4); g2.drawPolygon(trp); }
                case PENTA -> {
                    Polygon p5 = new Polygon();
                    p5.addPoint(w/2, 3); p5.addPoint(w-4, h/2-2); p5.addPoint(w-8, h-3); p5.addPoint(8, h-3); p5.addPoint(4, h/2-2);
                    g2.drawPolygon(p5);
                }
                case HEXA -> {
                    Polygon p6 = new Polygon();
                    p6.addPoint(4, h/2); p6.addPoint(8, 3); p6.addPoint(w-8, 3); p6.addPoint(w-4, h/2);
                    p6.addPoint(w-8, h-3); p6.addPoint(8, h-3);
                    g2.drawPolygon(p6);
                }
                case STAR -> { drawStar(g2, w/2, h/2, 7, 3, 5); }
                case ARROW_UP -> { g2.drawLine(w/2, 3, w/2, h-4); g2.drawLine(w/2, 3, 4, 8); g2.drawLine(w/2, 3, w-4, 8); }
                case ARROW_DOWN -> { g2.drawLine(w/2, 4, w/2, h-3); g2.drawLine(w/2, h-3, 4, h-8); g2.drawLine(w/2, h-3, w-4, h-8); }
                case ARROW_LEFT -> { g2.drawLine(3, h/2, w-4, h/2); g2.drawLine(3, h/2, 8, 4); g2.drawLine(3, h/2, 8, h-4); }
                case ARROW_RIGHT -> { g2.drawLine(4, h/2, w-3, h/2); g2.drawLine(w-3, h/2, w-8, 4); g2.drawLine(w-3, h/2, w-8, h-4); }
                case CLOUD -> { g2.drawOval(3, 8, 6, 5); g2.drawOval(7, 6, 6, 6); g2.drawOval(10, 8, 6, 5); }
                case ARC -> { g2.drawArc(3, 3, w-6, h-6, 200, 140); }
            }
            g2.dispose();
        }

        @Override public int getIconWidth() { return w; }
        @Override public int getIconHeight() { return h; }

        private void drawStar(Graphics2D g2, int cx, int cy, int rOuter, int rInner, int points) {
            double angle = Math.PI / points;
            Polygon star = new Polygon();
            for (int i = 0; i < 2 * points; i++) {
                double r = (i % 2 == 0) ? rOuter : rInner;
                double a = i * angle - Math.PI / 2;
                int sx = (int) Math.round(cx + Math.cos(a) * r);
                int sy = (int) Math.round(cy + Math.sin(a) * r);
                star.addPoint(sx, sy);
            }
            g2.drawPolygon(star);
        }
    }

    // ==== Swatch de color ====
    static class ColorSwatchButton extends JButton {
        private Color color;
        ColorSwatchButton(Color c) {
            this.color = c;
            setPreferredSize(new Dimension(28, 28));
            setFocusPainted(false);
            setBorder(BorderFactory.createLineBorder(new Color(210, 214, 220)));
            setContentAreaFilled(false);
            setOpaque(true);
            setBackground(Color.WHITE);
            setToolTipText("Click para elegir color");
        }
        public void setColor(Color c) { this.color = c; repaint(); }
        public Color getColor() { return color; }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color != null ? color : Color.WHITE);
            g2.fillRoundRect(5, 5, getWidth()-10, getHeight()-10, 6, 6);
            g2.setColor(new Color(120, 120, 120));
            g2.drawRoundRect(5, 5, getWidth()-10, getHeight()-10, 6, 6);
        }
    }

    // ==== Diálogos de ayuda (ya existentes en tu versión previa) ====
    private void mostrarDialogoAyuda() {
    JFrame dlg = new JFrame("Guía de uso"); // <- cambiado
    dlg.setSize(720, 600);
    dlg.setLocationRelativeTo(this);
    dlg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    dlg.setExtendedState(JFrame.NORMAL); // permite maximizar

    String accent = "#4F46E5";
    String text = """
    <html><head><style>
    body{font-family:Segoe UI,Roboto,Helvetica,Arial,sans-serif;color:#111827;margin:0}
    .wrap{padding:20px 22px}.hero{background:#F5F7FA;border-bottom:1px solid #E5E7EB;padding:18px 22px}
    .title{margin:0;font-size:20px;color:#111827}.subtitle{margin:6px 0 0 0;color:#6B7280;font-size:13px}
    h3{color:#111827;margin:22px 0 8px;font-size:15px}.card{border:1px solid #E5E7EB;border-radius:10px;padding:14px 16px;background:#fff;margin:10px 0}
    .kbd{background:#F3F4F6;border:1px solid #E5E7EB;padding:2px 6px;border-radius:6px;font-family:ui-monospace, SFMono-Regular, Menlo, monospace}
    ul{margin:6px 0 10px 16px}li{margin:6px 0}.badge{display:inline-block;font-size:11px;padding:2px 8px;border-radius:999px;background:%s;color:#fff}
    .hint{color:#6B7280;font-size:12px}a{color:%s;text-decoration:none}
    </style></head><body>
    <div class="hero"><div class="badge">Ayuda</div><h1 class="title">Cómo usar el Editor de Dibujo</h1>
    <p class="subtitle">Guía rápida de herramientas, atajos y flujo de trabajo.</p></div>
    <div class="wrap">
      <div class="card"><h3>1) Selección y edición</h3>
        <ul><li><b>Selección:</b> elige la herramienta Selección.</li>
        <li><b>Arrastrar:</b> arrastra la figura seleccionada.</li>
        <li><b>Redimensionar:</b> usa los handles del bounding box.</li>
        <li><b>Eliminar:</b> tecla <span class="kbd">Supr</span>.</li></ul>
      </div>
      <div class="card"><h3>2) Dibujo y borrado</h3>
        <ul><li><b>Dibujo libre:</b> ajusta el <b>grosor</b> en Propiedades.</li>
        <li><b>Borrador:</b> ajusta <b>tamaño</b> y <b>color</b> en Propiedades.</li></ul>
      </div>
      <div class="card"><h3>3) Figuras y colores</h3>
        <ul><li><b>Figuras:</b> botón Figuras (categorías).</li>
        <li><b>Colores:</b> swatches de línea y relleno.</li>
        <li><b>Cubeta:</b> rellena figuras rellenables.</li></ul>
      </div>
      <div class="card"><h3>4) Archivo y edición</h3>
        <ul><li><b>Nuevo/Abrir/Guardar/Exportar PNG</b> desde Archivo.</li>
        <li><b>Deshacer/Rehacer:</b> Ctrl+Z / Ctrl+Y.</li>
        <li><b>Copiar/Pegar:</b> Ctrl+C / Ctrl+V.</li>
        <li><b>Limpiar lienzo:</b> en Editar.</li></ul>
      </div>
    </div></body></html>
    """.formatted(accent, accent);

    JEditorPane html = new JEditorPane("text/html", text);
    html.setEditable(false);
    html.setBorder(null);

    JScrollPane scroll = new JScrollPane(html);
    scroll.setBorder(null);
    scroll.getVerticalScrollBar().setUnitIncrement(16);

    dlg.setContentPane(scroll);
    dlg.setVisible(true);
}


    private void mostrarDialogoAcercaDe() {
    JFrame dlg = new JFrame("Acerca de");  // <- cambiado
    dlg.setSize(520, 420);
    dlg.setLocationRelativeTo(this);
    dlg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    dlg.setExtendedState(JFrame.NORMAL); // permite maximizar

    String accent = "#4F46E5";
    String text = """
    <html><head><style>
    body{font-family:Segoe UI,Roboto,Helvetica,Arial,sans-serif;color:#111827;margin:0}
    .wrap{padding:20px 22px}.hero{background:#F5F7FA;border-bottom:1px solid #E5E7EB;padding:18px 22px}
    .title{margin:0;font-size:20px;color:#111827}.subtitle{margin:6px 0 0 0;color:#6B7280;font-size:13px}
    .card{border:1px solid #E5E7EB;border-radius:10px;padding:14px 16px;background:#fff;margin:14px 0}
    .badge{display:inline-block;font-size:11px;padding:2px 8px;border-radius:999px;background:%s;color:#fff}
    .hint{color:#6B7280;font-size:12px}
    </style></head><body>
    <div class="hero"><div class="badge">Acerca de</div><h1 class="title">Editor de Dibujo 2D</h1>
    <p class="subtitle">Proyecto académico — Ingeniería de Software I</p></div>
    <div class="wrap">
      <div class="card">
        <p>Editor para crear y manipular figuras vectoriales con selección, dibujo libre, borrado, colores, relleno, copiar/pegar y deshacer/rehacer.</p>
        <p class="hint">UI con barra compacta, menú de figuras, panel de propiedades y barra de estado.</p>
      </div>
      <div class="card">
        <p><b>Créditos:</b> Equipo del curso</p>
        <p><b>Versión:</b> 1.0</p>
      </div>
    </div></body></html>
    """.formatted(accent);

    JEditorPane html = new JEditorPane("text/html", text);
    html.setEditable(false);
    html.setBorder(null);

    JScrollPane scroll = new JScrollPane(html);
    scroll.setBorder(null);
    scroll.getVerticalScrollBar().setUnitIncrement(16);

    dlg.setContentPane(scroll);
    dlg.setVisible(true);
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaDeDibujo().setVisible(true));
    }
}