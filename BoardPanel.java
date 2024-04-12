import javax.swing.*;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.*;

public class BoardPanel extends JPanel {
    private final int size;
    private JButton[][] buttons;
    private Reversi reversi;
    private ImageIcon blackIcon;
    private ImageIcon whiteIcon;
    private ImageIcon greyIcon;
    private String level;

    //board panel
    public BoardPanel(int size, Reversi reversi, String level) {
        this.size = size;
        this.reversi = reversi;
        this.buttons = new JButton[size][size];
        this.level = level;
        setLayout(new GridLayout(size, size));

        blackIcon = new ImageIcon(getClass().getResource("/images/black.png"));
        whiteIcon = new ImageIcon(getClass().getResource("/images/white.png"));
        greyIcon = new ImageIcon(getClass().getResource("/images/grey.png"));

        initializeBoard();
        updateBoard();
    }

    //initialize board
    //all tiles are buttons
    private void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                JButton button = new JButton();
                button.setBackground(new Color(0, 102, 0));
                int finalI = i;
                int finalJ = j;
                button.addActionListener(e -> handleButtonClick(finalI, finalJ));
                buttons[i][j] = button;
                this.add(button);
            }
        }
    }

    //action when a button is clicked
    private void handleButtonClick(int x, int y) {
        //if player clicks a valid move on their turn, make move
        if (reversi.getCurrentPlayer() == reversi.getPlayer() && isAvailableMove(reversi.getAvailableMoves(reversi.getPlayer()), x, y)) {
            if (reversi.makeMove(x, y, reversi.getCurrentPlayer())) {
                updateBoard();
                if (!reversi.isGameOver()) {
                    // check if computer has available moves
                    if (reversi.getAvailableMoves(reversi.getComputer()).isEmpty()) {
                        reversi.skipTurn();
                        updateBoard();
                    }
                    //make computer move after the player makes a move and the game is not over
                    if (reversi.getCurrentPlayer() == reversi.getComputer()) {
                        makeComputerMove();
                    }
                }
                //if game is over, display results
                else {
                    displayResults();
                }
            }
        }
    }
    
    //computer move
    public void makeComputerMove() {
        Timer timer = new Timer(500, e -> {
            if (reversi.isGameOver()) {
                ((Timer) e.getSource()).stop();
                displayResults();
                return;
            }
            if (!reversi.getAvailableMoves(reversi.getComputer()).isEmpty()) {
                executeComputerMoveBasedOnLevel();
                updateBoard();
            } else {
                reversi.skipTurn();
            }
            updateBoard();
            if (reversi.getCurrentPlayer() == reversi.getPlayer() && !reversi.getAvailableMoves(reversi.getPlayer()).isEmpty()) {
                ((Timer) e.getSource()).stop();
            }
            else if (!reversi.isGameOver()) {
                makeComputerMove();
            }
            else if (reversi.isGameOver()) {
                displayResults();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    
    //choose computer choice based on level
    private void executeComputerMoveBasedOnLevel() {
        if (level.equals("easy")) {
            reversi.randomComputer(reversi.getComputer());
        } else if (level.equals("medium")) {
            reversi.minimaxComputer(reversi.getComputer());
        } else if (level.equals("hard")) {
            reversi.heuristicMinimaxComputer(reversi.getComputer());
        }
    }
    
    //update the GUI board
    public void updateBoard() {
        int[][] boardState = reversi.getBoard();
        List<int[]> availableMoves = reversi.getAvailableMoves(reversi.getCurrentPlayer());
    
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (boardState[i][j] == 2) { 
                    buttons[i][j].setIcon(scaleIcon(blackIcon, buttons[i][j].getWidth(), buttons[i][j].getHeight()));
                    buttons[i][j].setEnabled(true);
                } else if (boardState[i][j] == 1) { 
                    buttons[i][j].setIcon(scaleIcon(whiteIcon, buttons[i][j].getWidth(), buttons[i][j].getHeight()));
                    buttons[i][j].setEnabled(true);
                } else {
                    buttons[i][j].setIcon(null);
                    if (isAvailableMove(availableMoves, i, j)) {
                        buttons[i][j].setIcon(scaleIcon(greyIcon, buttons[i][j].getWidth(), buttons[i][j].getHeight()));
                        buttons[i][j].setEnabled(true);
                    } else {
                        buttons[i][j].setEnabled(false);
                    }
                }
            }
        }
    }
    
    //called when game is over, either when both players don't have any moves or the board is filled
    private void displayResults() {
        int playerCount = reversi.countDiscs(reversi.getPlayer());
        int computerCount = reversi.countDiscs(reversi.getComputer());
        String resultMessage = "Game Over! Player: " + playerCount + ", Computer: " + computerCount + ". " + reversi.getWinner();

        JOptionPane.showMessageDialog(this, resultMessage);
    }
    
    //check if the move is available for the current player
    private boolean isAvailableMove(List<int[]> availableMoves, int x, int y) {
        for (int[] move : availableMoves) {
            if (move[0] == x && move[1] == y) {
                return true;
            }
        }
        return false;
    }

    private ImageIcon scaleIcon(ImageIcon icon, int width, int height) {
        if (width <= 0 || height <= 0) {
            return icon;
        }
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}