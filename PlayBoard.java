import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.sql.SQLException;

import javax.swing.border.EmptyBorder;

public class PlayBoard extends JPanel {
    private Image backgroundImage;
    private JLabel timerLabel;
    private int timeLeft = 30;
    private JButton[][] buttons = new JButton[3][3];
    private boolean isXTurn = true;
    private boolean isTimerStarted = false;
    private String name, name1, name2;
    private JLabel playerName1, playerName2;

    public void setName1(String name1) {
        this.name = name1;
    }

    public void updateMatchData(String name1, String name2, int time) {
        playerName1.setText(name1);
        playerName2.setText(name2);
    }

    public PlayBoard(MainFrame mainFrame, String name1, String name2, String time) {
        setLayout(new BorderLayout());
        this.name = name1;
        DBCon dbCon = new DBCon();

        mainFrame.playData(name1, name2, time);
        mainFrame.switchToScreen("playboard");

        backgroundImage = Toolkit.getDefaultToolkit().getImage("image/play-board.jpg");
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        JPanel timerPanel = new RoundedPanel(Color.GRAY, 20);
        timerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        timerLabel = new JLabel("00:30", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 36));
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setOpaque(false);
        timerPanel.add(timerLabel, BorderLayout.CENTER);

        JPanel gridContainer = new JPanel();
        gridContainer.setOpaque(false);
        gridContainer.setLayout(new GridLayout(3, 3, 10, 10));

        int index = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(100, 100));
                button.setFont(new Font("Arial", Font.BOLD, 36));
                button.setBackground(Color.WHITE);
                button.setFocusPainted(false);
                button.setBorderPainted(false);

                button.putClientProperty("index", index);

                int finalRow = row;
                int finalCol = col;
                button.addActionListener(e -> {
                    if (button.getText().isEmpty()) {
                        button.setText(isXTurn ? "X" : "O");
                        isXTurn = !isXTurn;

                        if (!isTimerStarted) {
                            isTimerStarted = true;
                            startTimer();
                        }

                        if (checkWinCondition(finalRow, finalCol)) {
                            String winner = button.getText();
                            JOptionPane.showMessageDialog(this, winner + " menang!", "Game Over",
                                    JOptionPane.INFORMATION_MESSAGE);
                            if (winner.equals("X")) {
                                try {
                                    dbCon.updateDatabase(playerName1.getText());
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }
                            } else {
                                try {
                                    dbCon.updateDatabase(playerName2.getText());
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }
                            }
                            resetGame();
                        } else if (isBoardFull()) {
                            JOptionPane.showMessageDialog(this, "Permainan Seri!", "Game Over",
                                    JOptionPane.INFORMATION_MESSAGE);
                            resetGame();
                            isTimerStarted = false;
                        }
                    }
                });
                buttons[row][col] = button;
                gridContainer.add(button);
                index++;
            }
        }

        JPanel gridWrapper = new JPanel();
        gridWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        gridWrapper.setOpaque(false);
        gridWrapper.add(gridContainer);

        JPanel profile = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        ImageIcon profilePict = new ImageIcon("image/profile.png");
        Image scaledProfileImage = scaleImage(profilePict.getImage(), 130, 130);
        profilePict = new ImageIcon(scaledProfileImage);

        JLabel profileLabel = new JLabel(profilePict);

        System.out.println(name);
        JPanel profilName = new RoundedPanel(Color.decode("#D9D9D9"), 10);
        profilName.setPreferredSize(new Dimension(150, 50));
        JLabel labelProfile = new JLabel(this.name1);

        this.playerName1 = labelProfile;

        labelProfile.setForeground(Color.BLACK);
        profilName.setBorder(new EmptyBorder(10, 0, 0, 0));
        profilName.setOpaque(false);
        profilName.add(labelProfile);

        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setOpaque(false);
        profilePanel.add(profileLabel, BorderLayout.NORTH);
        profilePanel.add(Box.createVerticalStrut(10));
        profilePanel.add(profilName, BorderLayout.SOUTH);
        profilePanel.setBorder(new EmptyBorder(0, 0, 0, 300));

        JLabel profileLabel1 = new JLabel(profilePict);

        JPanel profilName1 = new RoundedPanel(Color.decode("#D9D9D9"), 10);
        profilName1.setPreferredSize(new Dimension(150, 50));
        profilName1.setBorder(new EmptyBorder(10, 0, 0, 0));
        JLabel labelProfile1 = new JLabel(name);
        this.playerName2 = labelProfile1;
        labelProfile1.setForeground(Color.BLACK);
        profilName1.setOpaque(false);
        profilName1.add(labelProfile1);

        JPanel profilePanel1 = new JPanel(new BorderLayout());
        profilePanel1.setOpaque(false);
        profilePanel1.add(profileLabel1, BorderLayout.NORTH);
        profilePanel1.add(Box.createVerticalStrut(10));
        profilePanel1.add(profilName1, BorderLayout.SOUTH);
        profilePanel1.setBorder(new EmptyBorder(0, 300, 0, 0));

        profile.setOpaque(false);
        profile.setLayout(new FlowLayout());
        profile.add(profilePanel, BorderLayout.WEST);
        profile.add(timerPanel, BorderLayout.SOUTH);
        profile.add(profilePanel1, BorderLayout.EAST);

        backgroundPanel.setBorder(new EmptyBorder(100, 0, 150, 0));
        backgroundPanel.add(profile, BorderLayout.NORTH);
        backgroundPanel.add(gridWrapper, BorderLayout.SOUTH);

        add(backgroundPanel, BorderLayout.CENTER);
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

        if (buttons[row][0].getText().equals(player) &&
                buttons[row][1].getText().equals(player) &&
                buttons[row][2].getText().equals(player)) {
            return true;
        }

        if (buttons[0][col].getText().equals(player) &&
                buttons[1][col].getText().equals(player) &&
                buttons[2][col].getText().equals(player)) {
            return true;
        }

        if (buttons[0][0].getText().equals(player) &&
                buttons[1][1].getText().equals(player) &&
                buttons[2][2].getText().equals(player)) {
            return true;
        }

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
        timeLeft = 30;
        timerLabel.setText("00:30");
    }

    private Image scaleImage(Image image, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return scaledImage;
    }
}
