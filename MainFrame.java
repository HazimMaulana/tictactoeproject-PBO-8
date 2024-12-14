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

        SatuPlayer satuPlayer = new SatuPlayer(this);
        cardPanel.add(satuPlayer, "satuPlayerFrame");

        DuaPlayer duaPlayer = new DuaPlayer(this);
        cardPanel.add(duaPlayer, "duaPlayerFrame");

        Beranda beranda = new Beranda(this);
        cardPanel.add(beranda, "beranda");

        PlayBoard playBoard = new PlayBoard(this, "name1", name2, time);
        // System.out.println("di mainframe, name1 " + name1);
        cardPanel.add(playBoard, "playboard");

        TopScore topScore = new TopScore(this);
        cardPanel.add(topScore, "topScore");

        Help help = new Help(this);
        cardPanel.add(help, "help");

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

    public String getPlayerName1() {
        return name1;
    }

    public String getPlayerName2() {
        return name2;
    }

    public String getPlayTime() {
        return time;
    }

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
