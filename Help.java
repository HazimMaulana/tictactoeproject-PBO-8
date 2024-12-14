import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;

public class Help extends JPanel {
    private Image backgroundImage;

    public Help(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        backgroundImage = Toolkit.getDefaultToolkit().getImage("image/BG.png");
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 10, 20));
        JLabel titleLabel = new JLabel("HELP", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Bebas Neue", Font.BOLD, 100));
        titleLabel.setForeground(new Color(255, 255, 255));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel wrapPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 70));
        wrapPanel.setOpaque(false);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setOpaque(false);
        inputPanel.setPreferredSize(new Dimension(1200, 500));
        inputPanel.setMaximumSize(new Dimension(1200, 500));

        JLabel namaLabel = new JLabel("PETUNJUK PERMAINAN");
        namaLabel.setFont(new Font("Bebas Neue", Font.BOLD, 36));
        namaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        namaLabel.setForeground(Color.YELLOW);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(true); // Set to true to make the panel opaque
        textPanel.setBackground(new Color(0, 0, 0, 100)); // Black color with 50% transparency
        textPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        textPanel.setBackground(new Color(0, 0, 0, 100));
        textPanel.add(createTextLabel("1. Permainan dimulai dengan papan kosong 3x3"));
        textPanel.add(createTextLabel("2. Pemain bergantian menempatkan simbol X atau O di kotak yang kosong"));
        textPanel.add(createTextLabel("3. Pemain pertama membuat tiga simbol sejajar menang (horizontal, vertikal, diagonal)"));
        textPanel.add(createTextLabel("4. Jika semua kotak terisi tanpa pemenang, permainan seri"));

        JButton backButton = new JButton("BACK");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setFont(new Font("Bebas Neue", Font.BOLD, 24));
        backButton.setMaximumSize(new Dimension(130, 40));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);

        JPanel buttonSelect = new JPanel();
        buttonSelect.setOpaque(false);

        buttonSelect.add(backButton);

        backButton.addActionListener(e -> {
            mainFrame.switchToScreen("beranda");
        });

        hoverButton(backButton);

        inputPanel.add(namaLabel);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        inputPanel.add(textPanel);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        inputPanel.add(buttonSelect);

        wrapPanel.add(inputPanel);

        backgroundPanel.add(titleLabel, BorderLayout.NORTH);
        backgroundPanel.add(wrapPanel, BorderLayout.CENTER);
        add(backgroundPanel, BorderLayout.CENTER);
    }

    private void hoverButton(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
    }

    private JLabel createTextLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Bebas Neue", Font.PLAIN, 28));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
}
