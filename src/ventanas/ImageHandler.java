package ventanas;

import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Rectangle;


public class ImageHandler {
    
    // ===== Atributo principal =====

    private BufferedImage imagen;


    public ImageHandler() {
        this.imagen = null;
    }

    // ===== Métodos públicos =====

    public boolean hasImage() {
        return imagen != null;
    }


    public boolean loadImage(JFrame parent) {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar imagen de fondo");
        
        ImagePreview preview = new ImagePreview(fileChooser);
        fileChooser.setAccessory(preview);
        
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true; 
                }

                String nombre = f.getName().toLowerCase();
                return nombre.endsWith(".png") || nombre.endsWith(".jpg") || 
                       nombre.endsWith(".jpeg") || nombre.endsWith(".gif") || 
                       nombre.endsWith(".bmp");
            }
            
            @Override
            public String getDescription() {
                return "Archivos de imagen (*.png, *.jpg, *.jpeg, *.gif, *.bmp)";
            }
        });

        int resultado = fileChooser.showOpenDialog(parent);
        
        try {

            if (preview != null) {
               
            }
        } catch (Exception e) {

        }
        fileChooser.setAccessory(null); 
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            
            try {

                imagen = ImageIO.read(archivoSeleccionado);
                
                if (imagen == null) {
                    JOptionPane.showMessageDialog(parent,
                        "No se pudo cargar la imagen. Formato no soportado.",
                        "Error al cargar imagen",
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                
                return true;
                
            } catch (IOException ex) {

                JOptionPane.showMessageDialog(parent,
                    "Error al leer el archivo: " + ex.getMessage(),
                    "Error de lectura",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        return false;
    }


    public void drawImage(Graphics g, int panelWidth, int panelHeight) {
        if (imagen == null) {
            return;
        }

        int imgWidth = imagen.getWidth();
        int imgHeight = imagen.getHeight();

        double escalaAncho = (double) panelWidth / imgWidth;
        double escalaAlto = (double) panelHeight / imgHeight;
        
        double escala = Math.min(escalaAncho, escalaAlto);

        int nuevoAncho = (int) (imgWidth * escala);
        int nuevoAlto = (int) (imgHeight * escala);

        int x = (panelWidth - nuevoAncho) / 2;
        int y = (panelHeight - nuevoAlto) / 2;

        g.drawImage(imagen, x, y, nuevoAncho, nuevoAlto, null);
    }

    public void clear() {
        if (imagen != null) {
            imagen.flush(); 
            imagen = null;
        }
    }
    

    public Rectangle getImageBounds(int panelWidth, int panelHeight) {
        if (imagen == null) return null;

        int imgWidth = imagen.getWidth();
        int imgHeight = imagen.getHeight();

        double escalaAncho = (double) panelWidth / imgWidth;
        double escalaAlto = (double) panelHeight / imgHeight;
        double escala = Math.min(escalaAncho, escalaAlto);

        int nuevoAncho = (int) (imgWidth * escala);
        int nuevoAlto = (int) (imgHeight * escala);

        int x = (panelWidth - nuevoAncho) / 2;
        int y = (panelHeight - nuevoAlto) / 2;

        return new Rectangle(x, y, nuevoAncho, nuevoAlto);
    }
    
    public BufferedImage getImagen() {
        return imagen;
    }

    public void setImagen(BufferedImage img) {
        imagen = img;
    }
}