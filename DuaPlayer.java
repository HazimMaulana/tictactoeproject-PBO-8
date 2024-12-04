import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;

public class DuaPlayer extends JFrame {
    private Image backgroundImage;
    public DuaPlayer() {
        setTitle("2 Player");
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

        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 10, 20));
        JLabel titleLabel = new JLabel("TIC TAC TOE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Bebas Neue", Font.BOLD, 100));
        titleLabel.setForeground(new Color(255, 255, 255));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
    
        JPanel wrapPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 70));
        wrapPanel.setOpaque(false);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setOpaque(false);
        inputPanel.setPreferredSize(new Dimension(400, 500));
        inputPanel.setMaximumSize(new Dimension(400, 500));

        JLabel namaLabel1 = new JLabel("PLAYER 1 NAME");
        namaLabel1.setFont(new Font("Bebas Neue", Font.BOLD, 24));
        namaLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        namaLabel1.setForeground(Color.WHITE);
        
        JTextField namePlayer1 = new JTextField(30);
        namePlayer1.setMaximumSize(new Dimension(300, 40));
        namePlayer1.setFont(new Font("Bebas Neue", Font.PLAIN, 20));
        namePlayer1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel namaLabel2 = new JLabel("PLAYER 2 NAME");
        namaLabel2.setFont(new Font("Bebas Neue", Font.BOLD, 24));
        namaLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        namaLabel2.setForeground(Color.WHITE);
        
        JTextField namePlayer2 = new JTextField(30);
        namePlayer2.setMaximumSize(new Dimension(300, 40));
        namePlayer2.setFont(new Font("Bebas Neue", Font.PLAIN, 20));
        namePlayer2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel timeLabel = new JLabel("SELECT PLAY TIME (SECOND)");
        timeLabel.setFont(new Font("Bebas Neue", Font.BOLD, 24));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeLabel.setForeground(Color.WHITE);

        JComboBox<String> timePlay = new JComboBox<>(new String[]{"30", "45", "60", "90", "120"});
        timePlay.setMaximumSize(new Dimension(300, 40));
        timePlay.setFont(new Font("Bebas Neue", Font.PLAIN, 20));
        timePlay.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = new JButton("START");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setFont(new Font("Bebas Neue", Font.BOLD, 24));
        startButton.setMaximumSize(new Dimension(130, 40));
        startButton.setBackground(Color.GREEN);
        startButton.setForeground(Color.WHITE);

        JButton backButton = new JButton("BACK");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setFont(new Font("Bebas Neue", Font.BOLD, 24));
        backButton.setMaximumSize(new Dimension(130, 40));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);

        JPanel buttonSelect = new JPanel();
        buttonSelect.setOpaque(false);

        buttonSelect.add(backButton);
        buttonSelect.add(startButton);

        startButton.addActionListener(e -> {
            String playerName1 = namePlayer1.getText().trim();
            String playerName2 = namePlayer2.getText().trim();
            String selectedTime = (String) timePlay.getSelectedItem();
            if (playerName1.isEmpty() && playerName2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your name!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if(playerName1.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter player 1 name!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (playerName2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter player 2 name!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // JOptionPane.showMessageDialog(this, "Player: " + playerName + "\nTime: " + selectedTime + " minutes");
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            Beranda beranda = new Beranda();
            beranda.setVisible(true);
        });

        hoverButton(backButton);
        hoverButton(startButton);

        inputPanel.add(namaLabel1);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(namePlayer1);
        inputPanel.add(Box.createRigidArea(new Dimension(0,20)));
        inputPanel.add(namaLabel2);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(namePlayer2);
        inputPanel.add(Box.createRigidArea(new Dimension(0,20)));
        inputPanel.add(timeLabel);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(timePlay);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 30)));
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
}
