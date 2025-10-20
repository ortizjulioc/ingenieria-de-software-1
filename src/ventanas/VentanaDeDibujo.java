package ventanas;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Ventana principal: añade Dibujo Libre, grosor del pincel,
 * Editar (Deshacer/Rehacer/Copiar/Pegar) y botón de Corazón.
 */
public class VentanaDeDibujo extends JFrame {

    private final PanelDeDibujo panel;
    private JSpinner spinnerGrosor;

    public VentanaDeDibujo() {
        super("Editor de Dibujo 2D");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1150, 720);
        setLocationRelativeTo(null);

        panel = new PanelDeDibujo();
        getContentPane().add(panel, BorderLayout.CENTER);

        setJMenuBar(crearMenu());
        getContentPane().add(crearToolbar(), BorderLayout.NORTH);
    }

    private JMenuBar crearMenu() {
        JMenuBar mb = new JMenuBar();

        // ===== Archivo =====
        JMenu mArchivo = new JMenu("Archivo");

        JMenuItem itNuevo = new JMenuItem("Nuevo");
        itNuevo.addActionListener(e -> {
            if (panel.isModificado()) {
                int r = JOptionPane.showConfirmDialog(this,
                        "¿Deseas guardar los cambios actuales?",
                        "Nuevo dibujo", JOptionPane.YES_NO_CANCEL_OPTION);
                if (r == JOptionPane.CANCEL_OPTION || r == JOptionPane.CLOSED_OPTION) return;
                if (r == JOptionPane.YES_OPTION) {
                    JFileChooser fc = new JFileChooser();
                    fc.setDialogTitle("Guardar proyecto");
                    if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                        try { panel.guardarProyecto(fc.getSelectedFile()); }
                        catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage()); return; }
                    } else return;
                }
            }
            panel.limpiarLienzo();
        });

        JMenuItem itAbrir = new JMenuItem("Abrir...");
        itAbrir.addActionListener(e -> {
            if (panel.isModificado()) {
                int r = JOptionPane.showConfirmDialog(this,
                        "Tienes cambios sin guardar. ¿Continuar y descartarlos?",
                        "Abrir proyecto", JOptionPane.YES_NO_OPTION);
                if (r != JOptionPane.YES_OPTION) return;
            }
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Abrir proyecto");
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try { panel.abrirProyecto(fc.getSelectedFile()); }
                catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error al abrir: " + ex.getMessage()); }
            }
        });

        JMenuItem itGuardar = new JMenuItem("Guardar proyecto...");
        itGuardar.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Guardar proyecto");
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try { panel.guardarProyecto(fc.getSelectedFile()); }
                catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage()); }
            }
        });

        JMenuItem itExportar = new JMenuItem("Exportar PNG...");
        itExportar.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Exportar como PNG");
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try { panel.exportarComoPNG(fc.getSelectedFile()); }
                catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage()); }
            }
        });

        mArchivo.add(itNuevo);
        mArchivo.add(itAbrir);
        mArchivo.addSeparator();
        mArchivo.add(itGuardar);
        mArchivo.add(itExportar);
        mb.add(mArchivo);

        // ===== Editar =====
        JMenu mEditar = new JMenu("Editar");
        JMenuItem itUndo = new JMenuItem("Deshacer (Ctrl+Z)");
        itUndo.addActionListener(e -> panel.undo());
        JMenuItem itRedo = new JMenuItem("Rehacer (Ctrl+Y)");
        itRedo.addActionListener(e -> panel.redo());
        JMenuItem itCopy = new JMenuItem("Copiar (Ctrl+C)");
        itCopy.addActionListener(e -> panel.copiarSeleccion());
        JMenuItem itPaste = new JMenuItem("Pegar (Ctrl+V)");
        itPaste.addActionListener(e -> panel.pegar());
        mEditar.add(itUndo);
        mEditar.add(itRedo);
        mEditar.addSeparator();
        mEditar.add(itCopy);
        mEditar.add(itPaste);
        mb.add(mEditar);

        return mb;
    }

    private JToolBar crearToolbar() {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);
        ButtonGroup group = new ButtonGroup();

        // Helper: crear toggle y agrupar
        java.util.function.BiFunction<String, PanelDeDibujo.Herramienta, JToggleButton> mk =
                (label, tool) -> {
                    JToggleButton b = new JToggleButton(label);
                    b.addActionListener(e -> panel.setHerramienta(tool));
                    group.add(b);
                    tb.add(b);
                    return b;
                };

        // Fila 1: selección + dibujo libre + básicas
        JToggleButton bSel = mk.apply("Selección", PanelDeDibujo.Herramienta.SELECCION);
        bSel.setSelected(true);
        mk.apply("Dibujo libre", PanelDeDibujo.Herramienta.DIBUJO_LIBRE);
        mk.apply("Línea", PanelDeDibujo.Herramienta.LINEA);
        mk.apply("Rectángulo", PanelDeDibujo.Herramienta.RECTANGULO);
        mk.apply("Círculo", PanelDeDibujo.Herramienta.CIRCULO);
        mk.apply("Óvalo", PanelDeDibujo.Herramienta.OVALO);
        mk.apply("Triángulo", PanelDeDibujo.Herramienta.TRIANGULO);
        mk.apply("Corazón", PanelDeDibujo.Herramienta.CORAZON);
        tb.addSeparator();

        // Fila 2: poligonales y especiales
        mk.apply("Rombo", PanelDeDibujo.Herramienta.ROMBO);
        mk.apply("Trapecio", PanelDeDibujo.Herramienta.TRAPECIO);
        mk.apply("Pentágono", PanelDeDibujo.Herramienta.PENTAGONO);
        mk.apply("Hexágono", PanelDeDibujo.Herramienta.HEXAGONO);
        mk.apply("Estrella", PanelDeDibujo.Herramienta.ESTRELLA);
        mk.apply("Nube", PanelDeDibujo.Herramienta.NUBE);
        mk.apply("Arco", PanelDeDibujo.Herramienta.ARCO);
        tb.addSeparator();

        // Fila 3: flechas + cubeta
        mk.apply("Flecha ↑", PanelDeDibujo.Herramienta.FLECHA_ARRIBA);
        mk.apply("Flecha ↓", PanelDeDibujo.Herramienta.FLECHA_ABAJO);
        mk.apply("Flecha ←", PanelDeDibujo.Herramienta.FLECHA_IZQUIERDA);
        mk.apply("Flecha →", PanelDeDibujo.Herramienta.FLECHA_DERECHA);
        mk.apply("Cubeta", PanelDeDibujo.Herramienta.CUBETA);
        tb.addSeparator();

        // Colores y grosor
        JButton cLinea = new JButton("Color línea");
        cLinea.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Selecciona color de línea", Color.BLACK);
            if (c != null) panel.setColorLinea(c);
        });

        JButton cRelleno = new JButton("Color relleno");
        cRelleno.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Selecciona color de relleno", Color.WHITE);
            if (c != null) panel.setColorRelleno(c);
        });

        tb.add(cLinea);
        tb.add(cRelleno);
        tb.add(new JLabel("  Grosor: "));
        spinnerGrosor = new JSpinner(new SpinnerNumberModel(2, 1, 50, 1));
        spinnerGrosor.addChangeListener(e -> {
            float g = ((Integer) spinnerGrosor.getValue()).floatValue();
            panel.setGrosorActual(g);
            panel.ajustarGrosorSeleccionado(g);
        });
        tb.add(spinnerGrosor);

        return tb;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaDeDibujo().setVisible(true));
    }
}
