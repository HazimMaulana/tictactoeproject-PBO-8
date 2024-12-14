// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.awt.image.BufferedImage;
// import java.sql.SQLException;
// import java.util.Random;

// public class PlayBoardWithBot extends JPanel {
//     private Image backgroundImage;
//     private int timeLeft;
//     private JButton[][] buttons = new JButton[3][3];
//     private boolean isXTurn = true;
//     private boolean isTimerStarted = false;
//     private String name, name1;
//     private JLabel timerLabel;
//     private JLabel playerName1, playerTime;
//     private JButton backButton;
//     private Thread timerThread;
//     private Random random = new Random();

//     public void updateMatchData(String name1, String time) {
//         playerName1.setText(name1);
//         playerTime.setText(time);
//         this.timeLeft = Integer.parseInt(time);
//     }

//     public PlayBoardWithBot(MainFrame mainFrame, String name1, String time) {
//         setLayout(new BorderLayout());
//         this.name1 = name1;
//         DBCon dbCon = new DBCon();

//         mainFrame.playData(name1, "Bot", time);
//         mainFrame.switchToScreen("playboardWithBot");

//         backgroundImage = Toolkit.getDefaultToolkit().getImage("image/play-board.jpg");
//         JPanel backgroundPanel = new JPanel() {
//             @Override
//             protected void paintComponent(Graphics g) {
//                 super.paintComponent(g);
//                 g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
//             }
//         };
//         backgroundPanel.setLayout(new BorderLayout());

//         JPanel timerPanel = new RoundedPanel(Color.GRAY, 20);
//         timerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

//         timerLabel = new JLabel(time, SwingConstants.CENTER);
//         timerLabel.setFont(new Font("Arial", Font.BOLD, 36));
//         timerLabel.setForeground(Color.WHITE);
//         timerPanel.add(timerLabel, BorderLayout.CENTER);
//         this.playerTime = timerLabel;

//         JPanel gridContainer = new JPanel();
//         gridContainer.setOpaque(false);
//         gridContainer.setLayout(new GridLayout(3, 3, 10, 10));

//         for (int row = 0; row < 3; row++) {
//             for (int col = 0; col < 3; col++) {
//                 JButton button = new JButton();
//                 button.setPreferredSize(new Dimension(100, 100));
//                 button.setFont(new Font("Arial", Font.BOLD, 36));
//                 button.setBackground(Color.WHITE);
//                 button.setFocusPainted(false);
//                 button.setBorderPainted(false);

//                 int finalRow = row;
//                 int finalCol = col;
//                 button.addActionListener(e -> {
//                     if (button.getText().isEmpty() && isXTurn) {
//                         button.setText("X");
//                         isXTurn = false;

//                         if (!isTimerStarted) {
//                             isTimerStarted = true;
//                             startTimer(mainFrame);
//                         }

//                         if (checkWinCondition(finalRow, finalCol)) {
//                             JOptionPane.showMessageDialog(this, playerName1.getText() + " menang!", "Game Over",
//                                     JOptionPane.INFORMATION_MESSAGE);
//                             try {
//                                 dbCon.updateDatabase(playerName1.getText());
//                             } catch (SQLException e1) {
//                                 e1.printStackTrace();
//                             }
//                             resetGame();
//                             timerThread.interrupt();
//                             mainFrame.switchToScreen("mainMenu");
//                         } else if (isBoardFull()) {
//                             JOptionPane.showMessageDialog(this, "Permainan Seri!", "Game Over",
//                                     JOptionPane.INFORMATION_MESSAGE);
//                             resetGame();
//                             mainFrame.switchToScreen("mainMenu");
//                         } else {
//                             botMove();
//                         }
//                     }
//                 });
//                 buttons[row][col] = button;
//                 gridContainer.add(button);
//             }
//         }

//         backButton = new JButton("BACK");
//         backButton.setFont(new Font("Bebas Neue", Font.BOLD, 24));
//         backButton.setBackground(Color.RED);
//         backButton.setForeground(Color.WHITE);
//         backButton.addActionListener(e -> mainFrame.switchToScreen("mainMenu"));

//         JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//         backPanel.setOpaque(false);
//         backPanel.add(backButton);

//         JPanel gridWrapper = new JPanel();
//         gridWrapper.setLayout(new FlowLayout(FlowLayout.CENTER));
//         gridWrapper.setOpaque(false);
//         gridWrapper.add(gridContainer);

//         JPanel profilePanel = new JPanel(new BorderLayout());
//         profilePanel.setOpaque(false);
//         profilePanel.add(timerPanel, BorderLayout.NORTH);
//         profilePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

//         backgroundPanel.add(backPanel, BorderLayout.NORTH);
//         backgroundPanel.add(profilePanel, BorderLayout.CENTER);
//         backgroundPanel.add(gridWrapper, BorderLayout.SOUTH);

//         add(backgroundPanel, BorderLayout.CENTER);
//     }

//     private void startTimer(MainFrame mainFrame) {
//         if (timerThread != null && timerThread.isAlive()) {
//             timerThread.interrupt();
//         }

//         timeLeft = Integer.parseInt(timerLabel.getText().replaceAll("[^0-9]", ""));
//         timerThread = new Thread(() -> {
//             while (timeLeft > 0) {
//                 try {
//                     Thread.sleep(1000);
//                 } catch (InterruptedException e) {
//                     return;
//                 }

//                 timeLeft--;
//                 SwingUtilities.invokeLater(() -> {
//                     timerLabel.setText(String.format("%02d:%02d", timeLeft / 60, timeLeft % 60));
//                 });
//             }

//             SwingUtilities.invokeLater(() -> {
//                 JOptionPane.showMessageDialog(null, "Waktu habis!", "Game Over", JOptionPane.WARNING_MESSAGE);
//                 resetGame();
//                 mainFrame.switchToScreen("mainMenu");
//             });
//         });
//         timerThread.start();
//     }

//     private void botMove() {
//         while (true) {
//             int row = random.nextInt(3);
//             int col = random.nextInt(3);

//             if (buttons[row][col].getText().isEmpty()) {
//                 buttons[row][col].setText("O");
//                 isXTurn = true;
//                 if (checkWinCondition(row, col)) {
//                     JOptionPane.showMessageDialog(this, "Bot menang!", "Game Over",
//                             JOptionPane.INFORMATION_MESSAGE);
//                     resetGame();
//                 }
//                 break;
//             }
//         }
//     }

//     private boolean checkWinCondition(int row, int col) {
//         String player = buttons[row][col].getText();

//         if (buttons[row][0].getText().equals(player) &&
//                 buttons[row][1].getText().equals(player) &&
//                 buttons[row][2].getText().equals(player)) {
//             return true;
//         }

//         if (buttons[0][col].getText().equals(player) &&
//                 buttons[1][col].getText().equals(player) &&
//                 buttons[2][col].getText().equals(player)) {
//             return true;
//         }

//         if (buttons[0][0].getText().equals(player) &&
//                 buttons[1][1].getText().equals(player) &&
//                 buttons[2][2].getText().equals(player)) {
//             return true;
//         }

//         if (buttons[0][2].getText().equals(player) &&
//                 buttons[1][1].getText().equals(player) &&
//                 buttons[2][0].getText().equals(player)) {
//             return true;
//         }

//         return false;
//     }

//     private boolean isBoardFull() {
//         for (int row = 0; row < 3; row++) {
//             for (int col = 0; col < 3; col++) {
//                 if (buttons[row][col].getText().isEmpty()) {
//                     return false;
//                 }
//             }
//         }
//         return true;
//     }

//     private void resetGame() {
//         for (int row = 0; row < 3; row++) {
//             for (int col = 0; col < 3; col++) {
//                 buttons[row][col].setText("");
//             }
//         }
//         isXTurn = true;
//     }
// }