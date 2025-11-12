/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ventanas;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

/**
 *
 * @author josearielpereyra
 * Esta clase implementa el patron de diseño Singleton para que solo se pueda crear una instancia de la ventana de ayuda
 */
public class VentanaDeAyuda extends JDialog{
    
    private static VentanaDeAyuda instancia;
    
    public static VentanaDeAyuda obtenerInstancia(VentanaDeDibujo ventanaPadre) {
        if(VentanaDeAyuda.instancia == null) {
            VentanaDeAyuda.instancia = new VentanaDeAyuda(ventanaPadre);
        }
        
        return VentanaDeAyuda.instancia;
    }
    
    private VentanaDeAyuda(VentanaDeDibujo ventanaPadre) {
        super(ventanaPadre, "Guía de uso", false);
        setSize(720, 600);
        setLocationRelativeTo(ventanaPadre);
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
        setContentPane(scroll);
    }
    
}
