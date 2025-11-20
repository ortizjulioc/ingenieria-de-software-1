
package ventanas;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageHandler {
    
    
     private BufferedImage image;
    
    /**
     * Abre diálogo y carga la imagen seleccionada
     * @param parent
     * @return 
     */
    public boolean loadImage(JFrame parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(
            "Imágenes", "jpg", "jpeg", "png", "gif", "bmp"));
        
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
        try {
            image = ImageIO.read(fileChooser.getSelectedFile());
            return true;
            } catch (IOException e) {
                // manejo de error
            }
        }
        return false;
    }
    
    /**
     * Dibuja la imagen en el panel (ajustada al tamaño)
     * @param g
     * @param panelWidth
     * @param panelHeight
     */
    public void drawImage(Graphics g, int panelWidth, int panelHeight) {
        if (image == null) return;
        
        // Calcular escala para mantener proporción
        double scale = Math.min(
            (double) panelWidth / image.getWidth(),
            (double) panelHeight / image.getHeight()
        );
        
        int width = (int) (image.getWidth() * scale);
        int height = (int) (image.getHeight() * scale);
        int x = (panelWidth - width) / 2;
        int y = (panelHeight - height) / 2;
        
        g.drawImage(image, x, y, width, height, null);
    }
    
    public void clear() {
        image = null;
    }
    
    public boolean hasImage() {
        return image != null;
    }

}
