import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        initializeGame();
    }

    public static void initializeGame() {
        SwingUtilities.invokeLater(() -> {
            //ask player for side
            Object[] options = {"Black (X)", "White (O)"};
            int choice = JOptionPane.showOptionDialog(null, 
                "Choose your side", 
                "Side Selection", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                options, 
                options[0]);
            String side = choice == 0 ? "x" : "o";

            //ask player for level
            //easy- random; medium- minimax, hard- heuristic-minimax
            Object[] levels = {"Easy", "Medium", "Hard"};
            int levelChoice = JOptionPane.showOptionDialog(null,
                "Select Computer Level",
                "Computer Difficulty",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                levels,
                levels[0]);
            String level = levelChoice == 0 ? "easy" : (levelChoice == 1 ? "medium" : "hard");
            
            //initialize game and board
            Reversi reversi = new Reversi(8, side);
            BoardPanel boardPanel = new BoardPanel(8, reversi, level);
            GameWindow window = new GameWindow(boardPanel);
            window.setVisible(true);
            boardPanel.updateBoard();
            
            //if player chooses white, computer makes first move
            if ("o".equals(side)) {
                SwingUtilities.invokeLater(boardPanel::makeComputerMove);
            }            
        });
    }
}