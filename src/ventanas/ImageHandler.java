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

/**
 * Clase encargada de gestionar la carga, visualización y eliminación
 * de una imagen de fondo en el panel de dibujo.
 * 
 * Permite al usuario insertar imágenes desde archivos del sistema
 * y las dibuja ajustadas al tamaño del lienzo.
 */
public class ImageHandler {
    
    // ===== Atributo principal =====
    /**
     * Almacena la imagen de fondo cargada por el usuario.
     * Es null si no hay imagen cargada.
     */
    private BufferedImage imagen;

    /**
     * Constructor por defecto.
     * Inicializa el handler sin ninguna imagen cargada.
     */
    public ImageHandler() {
        this.imagen = null;
    }

    // ===== Métodos públicos =====

    /**
     * Verifica si hay una imagen cargada actualmente.
     * 
     * @return true si existe una imagen, false en caso contrario
     */
    public boolean hasImage() {
        return imagen != null;
    }

    /**
     * Abre un diálogo de selección de archivos para cargar una imagen.
     * Soporta formatos: PNG, JPG, JPEG, GIF, BMP
     * 
     * @param parent Ventana padre para el diálogo de selección
     * @return true si la imagen se cargó exitosamente, false en caso contrario
     */
    public boolean loadImage(JFrame parent) {
        // Crear el selector de archivos
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar imagen de fondo");
        
        ImagePreview preview = new ImagePreview(fileChooser);
        fileChooser.setAccessory(preview);
        
        // Filtro para mostrar solo archivos de imagen
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true; // Permitir navegar por carpetas
                }
                // Aceptar solo archivos de imagen comunes
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

        // Mostrar el diálogo y esperar la selección del usuario
        int resultado = fileChooser.showOpenDialog(parent);
        
        try {
            // Intentar limpiar el preview si tiene método público
            if (preview != null) {
               
            }
        } catch (Exception e) {
            // Si no existe el método, ignorar
        }
        fileChooser.setAccessory(null); 
        
        // Si el usuario seleccionó un archivo (no canceló)
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            
            try {
                // Cargar la imagen desde el archivo
                imagen = ImageIO.read(archivoSeleccionado);
                
                // Verificar si la carga fue exitosa
                if (imagen == null) {
                    JOptionPane.showMessageDialog(parent,
                        "No se pudo cargar la imagen. Formato no soportado.",
                        "Error al cargar imagen",
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                
                // Imagen cargada exitosamente
                return true;
                
            } catch (IOException ex) {
                // Error al leer el archivo
                JOptionPane.showMessageDialog(parent,
                    "Error al leer el archivo: " + ex.getMessage(),
                    "Error de lectura",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        // El usuario canceló la selección
        return false;
    }

    /**
     * Dibuja la imagen de fondo en el panel, ajustándola al tamaño del lienzo.
     * La imagen se escala manteniendo sus proporciones y se centra en el panel.
     * 
     * @param g Contexto gráfico donde se dibujará la imagen
     * @param panelWidth Ancho del panel de dibujo
     * @param panelHeight Alto del panel de dibujo
     */
    public void drawImage(Graphics g, int panelWidth, int panelHeight) {
        // Solo dibujar si hay una imagen cargada
        if (imagen == null) {
            return;
        }

        // Obtener dimensiones originales de la imagen
        int imgWidth = imagen.getWidth();
        int imgHeight = imagen.getHeight();

        // Calcular escala para ajustar la imagen al panel manteniendo proporciones
        double escalaAncho = (double) panelWidth / imgWidth;
        double escalaAlto = (double) panelHeight / imgHeight;
        
        // Usar la escala menor para que la imagen completa quepa en el panel
        double escala = Math.min(escalaAncho, escalaAlto);

        // Calcular nuevas dimensiones escaladas
        int nuevoAncho = (int) (imgWidth * escala);
        int nuevoAlto = (int) (imgHeight * escala);

        // Calcular posición para centrar la imagen
        int x = (panelWidth - nuevoAncho) / 2;
        int y = (panelHeight - nuevoAlto) / 2;

        // Dibujar la imagen escalada y centrada
        g.drawImage(imagen, x, y, nuevoAncho, nuevoAlto, null);
    }

    /**
     * Elimina la imagen de fondo actual.
     * Después de llamar a este método, hasImage() retornará false.
     */
    public void clear() {
        if (imagen != null) {
            imagen.flush(); // Liberar recursos de memoria
            imagen = null;
        }
    }
    
    /**
     * Calcula y devuelve el área exacta (posición y tamaño)
     * que ocupa la imagen dentro del panel.
     *
     * La imagen se ajusta al tamaño del panel manteniendo
     * su proporción original y se centra automáticamente.
     *
     * @param panelWidth ancho actual del panel
     * @param panelHeight alto actual del panel
     * @return Rectangle que representa el área ocupada por la imagen
     */
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