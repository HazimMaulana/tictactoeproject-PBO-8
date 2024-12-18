import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class PlayBoard extends JPanel {
    private Image backgroundImage;
    private int timeLeft;
    private JButton[][] buttons = new JButton[3][3];
    private boolean isXTurn = true;
    private boolean isTimerStarted = false;
    private String name, name1, name2;
    private JLabel timerLabel;
    private JLabel playerName1, playerName2, playerTime;
    private JButton backButton;
    private boolean isAgainstBot = true;
    Thread timerThread;

    public void setName1(String name1) {
        this.name = name1;
    }

    public void updateMatchData(String name1, String name2, String time, boolean isAgainstBot) {
        playerName1.setText(name1);
        playerName2.setText(name2);
        playerTime.setText(time);
		this.timeLeft = Integer.parseInt(time);
		this.isAgainstBot = isAgainstBot;
    }

    public PlayBoard(MainFrame mainFrame, String name1, String name2, String time) {
        //this.isAgainstBot = isAgainstBot;
        setLayout(new BorderLayout());
        this.name = name1;
        DBCon dbCon = new DBCon();

        // mainFrame.playData(name1, name2, time);
        // mainFrame.switchToScreen("playboard");

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

        timerLabel = new JLabel(time, SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 36));
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setOpaque(false);
        timerPanel.add(timerLabel, BorderLayout.CENTER);
        this.playerTime = timerLabel;

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

                int[] finalPosition = new int[2];
        		finalPosition[0] = row;
        		finalPosition[1] = col;

                button.addActionListener(e -> {
                    if (button.getText().isEmpty()) {
                        button.setText(isXTurn ? "X" : "O");
                        isXTurn = isAgainstBot || !isXTurn;

                        checkResult(finalPosition[0], finalPosition[1], dbCon, mainFrame);
						if(isAgainstBot) {
							int[] botButtonIndex = getRandomEmptyButtonIndex();
							int botRow = botButtonIndex[0];
							int botCol = botButtonIndex[1];

							finalPosition[0] = botRow;
                    		finalPosition[1] = botCol;

							JButton botButton = buttons[botRow][botCol];
							botButton.setText("O");
                            
                            checkResult(finalPosition[0], finalPosition[1], dbCon, mainFrame);

                            
						}

                        if (!isTimerStarted) {
                            isTimerStarted = true;
                            startTimer(mainFrame);
                        }

                        if (checkWinCondition(finalPosition[0], finalPosition[1])) {
                            String winner = button.getText();
                            if (winner.equals("X")) {
                                JOptionPane.showMessageDialog(this, playerName1.getText() + " menang!", "Game Over",
                                        JOptionPane.INFORMATION_MESSAGE);
                                        try {
                                            dbCon.updateDatabase(playerName1.getText());
                                        } catch (SQLException e1) {
                                            e1.printStackTrace();
                                        }
                                resetGame();
                            } else {
                                JOptionPane.showMessageDialog(this, playerName2.getText() + " menang!", "Game Over",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                            SwingUtilities.invokeLater(() -> backButton.setVisible(true));
                            resetGame();
                            mainFrame.switchToScreen("duaPlayerFrame");
                        } else if (isBoardFull()) {
                            JOptionPane.showMessageDialog(this, "Permainan Seri!", "Game Over",
                                    JOptionPane.INFORMATION_MESSAGE);
                                    SwingUtilities.invokeLater(() -> backButton.setVisible(true));
                                    resetGame();
                                    mainFrame.switchToScreen("satuPlayerFrame");
                            isTimerStarted = false;
                        }


                    }
                });
                buttons[row][col] = button;
                gridContainer.add(button);
                index++;
            }
        }

        backButton = new JButton("BACK");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setFont(new Font("Bebas Neue", Font.BOLD, 24));
        backButton.setMaximumSize(new Dimension(130, 40));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> mainFrame.switchToScreen("duaPlayerFrame"));

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 30));
        backPanel.setOpaque(false);
        backPanel.setBorder(new EmptyBorder(0, 30, 0, 0));
        backPanel.add(backButton);

        JPanel gridWrapper = new JPanel();
        gridWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        gridWrapper.setOpaque(false);
        gridWrapper.add(gridContainer);

        JPanel profile = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        ImageIcon profilePict = new ImageIcon("image/profile.png");
        Image scaledProfileImage = scaleImage(profilePict.getImage(), 130, 130);
        profilePict = new ImageIcon(scaledProfileImage);

        JLabel profileLabel = new JLabel(profilePict);

        //System.out.println(name);
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
        backgroundPanel.add(backPanel, BorderLayout.NORTH);
        backgroundPanel.add(profile, BorderLayout.CENTER);
        backgroundPanel.add(gridWrapper, BorderLayout.SOUTH);

        add(backgroundPanel, BorderLayout.CENTER);
    }

    private void checkResult(int i, int j, DBCon dbCon, MainFrame mainFrame) {
        if (checkWinCondition(i, j)) {
            JButton button = buttons[i][j];
            String winner = button.getText();
            if (winner.equals("X")) {
                JOptionPane.showMessageDialog(this, playerName1.getText() + " menang!", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
                        try {
                            dbCon.updateDatabase(playerName1.getText());
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
            } else {
                JOptionPane.showMessageDialog(this, playerName2.getText() + " menang!", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
                
            }
            mainFrame.switchToScreen("satuPlayerFrame");
            //resetGame();
        } else if (isBoardFull()) {
            JOptionPane.showMessageDialog(this, "Permainan Seri!", "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
                    mainFrame.switchToScreen("satuPlayerFrame");
                    //resetGame();
            isTimerStarted = false;
        }
    }

    private void startTimer(MainFrame mainFrame) {
        SwingUtilities.invokeLater(() -> backButton.setVisible(false));
        
        timerThread = new Thread(() -> {
            
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
                mainFrame.switchToScreen("duaPlayerFrame");
                //resetGame();
            });
        });

        timerThread.start();
    }

    private int[] getRandomEmptyButtonIndex() {
        List<int[]> indices = new ArrayList<int[]>();

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                if (buttons[i][j].getText().isEmpty()) {
                    indices.add(new int[]{i, j});
                }
            }
        }

        Random random = new Random();
        int index = random.nextInt(indices.size());

        System.out.println(indices.size());
        return indices.get(index);
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
        timerThread.interrupt();
        isXTurn = true;
        // timeLeft = 30;
        // timerLabel.setText("00:30");
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