import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Beranda extends JFrame {
    private Image backgroundImage;

    public Beranda() {
        setTitle("Tic Tac Toe");
        setSize(1440, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel("TIC TAC TOE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Bebas Neue", Font.BOLD, 100));
        titleLabel.setForeground(new Color(255, 255, 255));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(Box.createRigidArea(new Dimension(0,75)));
        buttonPanel.setOpaque(false);

        JButton satuPlayer = createButtonMenu("1 PLAYER", 300, 70);
        JButton duaPlayer = createButtonMenu("2 PLAYER", 300, 70);
        JButton topScore = createButtonMenu("TOP SCORE", 400, 70);
        JButton help = createButtonMenu("HELP", 175, 70);
        JButton exit = createButtonMenu("EXIT", 160, 70);

        ActionListener ActionMenu = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == satuPlayer || e.getSource() == duaPlayer) {
                    dispose(); // Tutup Beranda
                    PlayBoard playBoard = new PlayBoard(); // Buka PlayBoard
                    playBoard.setVisible(true);
                } else if (e.getSource() == help) {
                    dispose();
                    Help help = new Help();
                    help.setVisible(true);
                } else if (e.getSource() == exit) {
                    System.exit(0);
                }
            }
        };

        satuPlayer.addActionListener(ActionMenu);
        duaPlayer.addActionListener(ActionMenu);
        topScore.addActionListener(ActionMenu);
        help.addActionListener(ActionMenu);
        exit.addActionListener(ActionMenu);

        hoverButton(satuPlayer, Color.BLUE, Color.WHITE);
        hoverButton(duaPlayer, Color.BLUE, Color.WHITE);
        hoverButton(topScore, Color.BLUE, Color.WHITE);
        hoverButton(help, Color.BLUE, Color.WHITE);
        hoverButton(exit, Color.RED, Color.WHITE);

        buttonPanel.add(satuPlayer);
        buttonPanel.add(duaPlayer);
        buttonPanel.add(topScore);
        buttonPanel.add(help);
        buttonPanel.add(exit);

        backgroundPanel.add(titleLabel, BorderLayout.NORTH);
        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);
        add(backgroundPanel, BorderLayout.CENTER);
    }

    public JButton createButtonMenu(String text, int width, int height) {
        JButton playerButton = new JButton();

        playerButton.setBorder(BorderFactory.createEmptyBorder());
        playerButton.setLayout(new BorderLayout());
        playerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerButton.setMaximumSize(new Dimension(width, height));
        playerButton.setPreferredSize(new Dimension(width, height));
        playerButton.setContentAreaFilled(false);

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Bebas Neue", Font.BOLD, 60));
        label.setForeground(Color.WHITE);

        playerButton.add(label, BorderLayout.CENTER);
        return playerButton;
    }

    private void hoverButton(JButton button, Color hoverColor, Color defaultColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.getComponent(0).setForeground(hoverColor);
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.getComponent(0).setForeground(defaultColor);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Beranda homePage = new Beranda();
            homePage.setVisible(true);
        });
    }
}
