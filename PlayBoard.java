import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayBoard extends JFrame {
    private Image backgroundImage;
    private JLabel timerLabel;
    private int timeLeft = 300;
    private Timer timer;

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

        timerLabel = new JLabel("05:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 36));
        timerLabel.setForeground(Color.GRAY);

        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // timerPanel.setOpaque(true);
        timerPanel.add(timerLabel);

        startTimer();

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
        add(timerPanel, BorderLayout.NORTH);
        add(backgroundPanel, BorderLayout.CENTER);
    }

    private void startTimer() {
        Thread timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (timeLeft > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    timeLeft--;
                    int minutes = timeLeft / 60;
                    int seconds = timeLeft % 60;
                    SwingUtilities.invokeLater(() -> {
                        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
                    });
                }
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "Waktu habis!", "Game Over", JOptionPane.WARNING_MESSAGE);
                });
            }
        });

        timerThread.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlayBoard playBoard = new PlayBoard();
            playBoard.setVisible(true);
        });
    }
}
