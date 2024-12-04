import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayBoard extends JFrame {
    private Image backgroundImage;

    public PlayBoard() {
        setTitle("Tic Tac Toe");
        setSize(1440, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        backgroundImage = Toolkit.getDefaultToolkit().getImage("image/background.jpg");
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        JPanel gridContainer = new JPanel();
        gridContainer.setOpaque(false);
        gridContainer.setLayout(new GridLayout(3, 3, 10, 10));


        for (int i = 0; i < 9; i++) {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(100, 100));
            button.setFont(new Font("Arial", Font.BOLD, 36));
            button.setBackground(Color.WHITE); 
            button.setFocusPainted(false); 
            button.setBorderPainted(false);
            
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (button.getText().isEmpty()) { 
                        button.setText("X"); 
                    }
                }
            });
            gridContainer.add(button);
        }


        backgroundPanel.add(gridContainer);
        add(backgroundPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlayBoard playBoard = new PlayBoard();
            playBoard.setVisible(true);
        });
    }
}
