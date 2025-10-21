package ventanas;

import java.awt.Color;
import java.io.File;

/**
 * Controlador (C de MVC)
 * Orquesta las acciones entre la Vista (PanelDeDibujo) y el Modelo (ModeloDibujo).
 * Mantiene toda la API que antes se llamaba directo sobre PanelDeDibujo, para no romper tu código.
 */
public class DibujoController {

    private final ModeloDibujo modelo;
    private final PanelDeDibujo panel; // Vista de dibujo

    public DibujoController(ModeloDibujo modelo, PanelDeDibujo panel) {
        this.modelo = modelo;
        this.panel = panel;

        // Sincroniza estado inicial del panel con el modelo actual
        syncPanelConModelo();
        // Opcional: podrías registrar listeners del modelo para reflejar cambios automáticos en el panel
        modelo.addPropertyChangeListener(evt -> {
            switch (evt.getPropertyName()) {
                case "herramienta"    -> panel.setHerramienta(mapHerramienta((ModeloDibujo.Herramienta) evt.getNewValue()));
                case "colorLinea"     -> panel.setColorLinea((Color) evt.getNewValue());
                case "colorRelleno"   -> panel.setColorRelleno((Color) evt.getNewValue());
                case "grosorPincel"   -> {
                    float g = (Float) evt.getNewValue();
                    panel.setGrosorActual(g);
                    panel.ajustarGrosorSeleccionado(g);
                }
                case "tamBorrador"    -> panel.setTamBorrador((Float) evt.getNewValue());
                case "colorBorrador"  -> panel.setColorBorrador((Color) evt.getNewValue());
            }
        });
    }

    private void syncPanelConModelo() {
        panel.setHerramienta(mapHerramienta(modelo.getHerramienta()));
        panel.setColorLinea(modelo.getColorLinea());
        panel.setColorRelleno(modelo.getColorRelleno());
        panel.setGrosorActual(modelo.getGrosorPincel());
        panel.ajustarGrosorSeleccionado(modelo.getGrosorPincel());
        panel.setTamBorrador(modelo.getTamBorrador());
        panel.setColorBorrador(modelo.getColorBorrador());
    }

    // ====== Mutadores que la Vista (VentanaDeDibujo) llamará ======
    public void setHerramienta(ModeloDibujo.Herramienta h)       { modelo.setHerramienta(h); }
    public void setColorLinea(Color c)                           { modelo.setColorLinea(c); }
    public void setColorRelleno(Color c)                         { modelo.setColorRelleno(c); }
    public void setGrosorActual(float g)                         { modelo.setGrosorPincel(g); }
    public void setTamBorrador(float t)                          { modelo.setTamBorrador(t); }
    public void setColorBorrador(Color c)                        { modelo.setColorBorrador(c); }

    // ====== Acciones sobre el lienzo (delegadas al Panel) ======
    public void limpiarLienzo()                                  { panel.limpiarLienzo(); modelo.setModificado(false); }
    public void undo()                                           { panel.undo(); }
    public void redo()                                           { panel.redo(); }
    public void copiarSeleccion()                                { panel.copiarSeleccion(); }
    public void pegar()                                          { panel.pegar(); }

    public boolean isModificado()                                { return panel.isModificado(); }
    public void guardarProyecto(File f) throws Exception         { panel.guardarProyecto(f); modelo.setModificado(false); }
    public void abrirProyecto(File f) throws Exception           { panel.abrirProyecto(f);  modelo.setModificado(false); }
    public void exportarComoPNG(File f) throws Exception         { panel.exportarComoPNG(f); }

    // ====== Mapeo de herramientas (Modelo -> Panel) ======
    private PanelDeDibujo.Herramienta mapHerramienta(ModeloDibujo.Herramienta h) {
        return switch (h) {
            case SELECCION        -> PanelDeDibujo.Herramienta.SELECCION;
            case DIBUJO_LIBRE     -> PanelDeDibujo.Herramienta.DIBUJO_LIBRE;
            case BORRADOR         -> PanelDeDibujo.Herramienta.BORRADOR;
            case CUBETA           -> PanelDeDibujo.Herramienta.CUBETA;

            case LINEA            -> PanelDeDibujo.Herramienta.LINEA;
            case RECTANGULO       -> PanelDeDibujo.Herramienta.RECTANGULO;
            case CIRCULO          -> PanelDeDibujo.Herramienta.CIRCULO;
            case OVALO            -> PanelDeDibujo.Herramienta.OVALO;
            case TRIANGULO        -> PanelDeDibujo.Herramienta.TRIANGULO;

            case CORAZON          -> PanelDeDibujo.Herramienta.CORAZON;
            case ROMBO            -> PanelDeDibujo.Herramienta.ROMBO;
            case TRAPECIO         -> PanelDeDibujo.Herramienta.TRAPECIO;
            case PENTAGONO        -> PanelDeDibujo.Herramienta.PENTAGONO;
            case HEXAGONO         -> PanelDeDibujo.Herramienta.HEXAGONO;
            case ESTRELLA         -> PanelDeDibujo.Herramienta.ESTRELLA;

            case FLECHA_ARRIBA    -> PanelDeDibujo.Herramienta.FLECHA_ARRIBA;
            case FLECHA_ABAJO     -> PanelDeDibujo.Herramienta.FLECHA_ABAJO;
            case FLECHA_IZQUIERDA -> PanelDeDibujo.Herramienta.FLECHA_IZQUIERDA;
            case FLECHA_DERECHA   -> PanelDeDibujo.Herramienta.FLECHA_DERECHA;

            case NUBE             -> PanelDeDibujo.Herramienta.NUBE;
            case ARCO             -> PanelDeDibujo.Herramienta.ARCO;
        };
    }
}
