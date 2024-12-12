import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SmoothImageIcon {
    public static ImageIcon createSmoothIcon(String imagePath, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image originalImage = originalIcon.getImage();

        // Buat BufferedImage untuk rendering berkualitas tinggi
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImage.createGraphics();

        // Aktifkan rendering berkualitas tinggi
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Gambar ulang gambar ke ukuran yang diinginkan
        g2.drawImage(originalImage, 0, 0, width, height, null);
        g2.dispose();

        return new ImageIcon(resizedImage);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Smooth Image Example");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel(createSmoothIcon("image/profile.png", 130, 130));
        frame.add(label);

        frame.setVisible(true);
    }
}
