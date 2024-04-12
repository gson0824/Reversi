import javax.swing.*;
import java.awt.BorderLayout;

//GameWindow class
public class GameWindow extends JFrame {

    public GameWindow(BoardPanel boardPanel) {
        setTitle("Othello Game");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));
        
        //reset button to reset game
        JButton resetButton = new JButton("Reset Game");
        resetButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to reset the game?",
                "Reset Game",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                this.dispose();
                Main.initializeGame();
            }
        });

        //exit button to exit game
        JButton exitButton = new JButton("Exit Game");
        exitButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to exit the game?",
            "Exit Game",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        menuPanel.add(resetButton);
        menuPanel.add(exitButton);

        setLayout(new BorderLayout());
        add(boardPanel, BorderLayout.CENTER);
        add(menuPanel, BorderLayout.EAST);
    }
}
