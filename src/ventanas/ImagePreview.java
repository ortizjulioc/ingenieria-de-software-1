package ventanas; 

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

public class ImagePreview extends JComponent implements PropertyChangeListener {

    private Image image;
    private final int width = 200;
    private final int height = 200;

    public ImagePreview(JFileChooser fc) {
        setPreferredSize(new Dimension(width, height));
        fc.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String property = evt.getPropertyName();

        if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(property)) {
            File file = (File) evt.getNewValue();

            if (file != null && file.isFile()) {
                try {
                    image = ImageIO.read(file);
                } catch (Exception e) {
                    image = null;
                }
                repaint();
            } else {
                image = null;
                repaint();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            int imgWidth = image.getWidth(this);
            int imgHeight = image.getHeight(this);

            if (imgWidth > 0 && imgHeight > 0) {
                double scale = Math.min((double) width / imgWidth, (double) height / imgHeight);

                int newW = (int) (imgWidth * scale);
                int newH = (int) (imgHeight * scale);

                int x = (width - newW) / 2;
                int y = (height - newH) / 2;

                g.drawImage(image, x, y, newW, newH, this);
            }
        }
    }
}
