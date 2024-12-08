import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayBoard extends JFrame {
    private Image backgroundImage;
    private JLabel timerLabel;
    private int timeLeft = 300;
    private JButton[][] buttons = new JButton[3][3]; // Grid untuk menyimpan tombol
    private boolean isXTurn = true; // Menentukan giliran pemain

    public PlayBoard() {
        setTitle("Tic Tac Toe");
        setSize(1440, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        backgroundImage = Toolkit.getDefaultToolkit().getImage("image/background.jpg");
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));

        timerLabel = new JLabel("05:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 36));
        timerLabel.setForeground(Color.GRAY);
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        startTimer();

        JPanel gridContainer = new JPanel();
        gridContainer.setOpaque(false);
        gridContainer.setLayout(new GridLayout(3, 3, 10, 10));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(100, 100));
                button.setFont(new Font("Arial", Font.BOLD, 36));
                button.setBackground(Color.WHITE);
                button.setFocusPainted(false);
                button.setBorderPainted(false);

                int finalRow = row;
                int finalCol = col;
                button.addActionListener(e -> {
                    if (button.getText().isEmpty()) {
                        button.setText(isXTurn ? "X" : "O");
                        isXTurn = !isXTurn; // Beralih giliran
                        if (checkWinCondition(finalRow, finalCol)) {
                            String winner = button.getText();
                            JOptionPane.showMessageDialog(this, winner + " menang!", "Game Over",
                                    JOptionPane.INFORMATION_MESSAGE);
                            resetGame();
                        } else if (isBoardFull()) {
                            JOptionPane.showMessageDialog(this, "Permainan Seri!", "Game Over",
                                    JOptionPane.INFORMATION_MESSAGE);
                            resetGame();
                        }
                    }
                });
                buttons[row][col] = button;
                gridContainer.add(button);
            }
        }

        JPanel gridWrapper = new JPanel();
        gridWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        gridWrapper.setOpaque(false);
        gridWrapper.add(gridContainer);

        backgroundPanel.add(Box.createVerticalStrut(getHeight() / 2 - 200));
        backgroundPanel.add(timerLabel);
        backgroundPanel.add(Box.createVerticalStrut(20));
        backgroundPanel.add(gridWrapper);

        add(backgroundPanel);
    }

    private void startTimer() {
        Thread timerThread = new Thread(() -> {
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
                resetGame();
            });
        });

        timerThread.start();
    }

    private boolean checkWinCondition(int row, int col) {
        String player = buttons[row][col].getText();

        // Cek baris
        if (buttons[row][0].getText().equals(player) &&
                buttons[row][1].getText().equals(player) &&
                buttons[row][2].getText().equals(player)) {
            return true;
        }

        // Cek kolom
        if (buttons[0][col].getText().equals(player) &&
                buttons[1][col].getText().equals(player) &&
                buttons[2][col].getText().equals(player)) {
            return true;
        }

        // Cek diagonal utama
        if (buttons[0][0].getText().equals(player) &&
                buttons[1][1].getText().equals(player) &&
                buttons[2][2].getText().equals(player)) {
            return true;
        }

        // Cek diagonal sekunder
        if (buttons[0][2].getText().equals(player) &&
                buttons[1][1].getText().equals(player) &&
                buttons[2][0].getText().equals(player)) {
            return true;
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetGame() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");
            }
        }
        isXTurn = true;
        timeLeft = 300;
        timerLabel.setText("05:00");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlayBoard playBoard = new PlayBoard();
            playBoard.setVisible(true);
        });
    }
}
