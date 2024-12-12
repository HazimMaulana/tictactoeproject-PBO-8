import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private String name1, name2, time;

    public MainFrame() {
        setTitle("Tic Tac Toe MainFrame");
        setSize(1440, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        DuaPlayer duaPlayer = new DuaPlayer(this);
        cardPanel.add(duaPlayer, "duaPlayerFrame");

        Beranda beranda = new Beranda(this);
        cardPanel.add(beranda, "beranda");

        PlayBoard playBoard = new PlayBoard(this, name1, name2, time);
        cardPanel.add(playBoard, "playboard");

        add(cardPanel);

        cardLayout.show(cardPanel, "beranda");
    }

    // Method to switch screens
    public void switchToScreen(String screenName) {
        cardLayout.show(cardPanel, screenName);
    }

    public void playData(String name1, String name2, String time){
        this.name1 = name1;
        this.name2 = name2;
        this.time = time;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
