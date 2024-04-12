import java.util.*;

//reversi class
public class Reversi {
    private int[][] board;
    private int row;
    private int col;
    public int turn;
    private int player;
    private int computer;

    //get current player based on turn
	public int getCurrentPlayer() {
        if (turn % 2 == 0) {
            return player == 2 ? player : computer;
        }
        else {

            return player == 2 ? computer : player;
        }
    }

    public int getPlayer() {
        return player;
    }

    public int getComputer() {
        return computer;
    }

    public int[][] getBoard() {
		return board;
	}

    //choose which side each player is on and initiazlize board
	public Reversi(int num, String side) {
		board = new int[num][num];
		row = num;
		col = num;
		turn = 0;

        if ("x".equals(side)) {
            player = 2;
            computer = 1;
        } else if ("o".equals(side)) {
            player = 1;
            computer = 2;
        }
		create8Board();
        //create4Board();
	}

    //for debugging
    /*private void create4Board() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = 0;
            }
        }
        board[1][1] = 1;
		board[1][2] = 2;
		board[2][1] = 2;
		board[2][2] = 1;
    }*/
	
	private void create8Board() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = 0;
            }
        }
		board[3][3] = 1;
		board[3][4] = 2;
		board[4][3] = 2;
		board[4][4] = 1;
	}

    public void skipTurn() {
        turn++;
    }

    public void updateTurn() {
        turn++;
        if (getAvailableMoves(getCurrentPlayer()).isEmpty()) {
            turn++;
        }
    }
    
    //if move is valid for the current player, make the move and update the board
	public boolean makeMove(int x, int y, int currentPlayer) {
        if (!isValidMove(x, y, currentPlayer)) {
            return false;
        }

        board[x][y] = currentPlayer;
        flip(x, y, currentPlayer);
    
        updateTurn();
    
        return true;
    }    
	
    //check if the move is valid for the current player
    private boolean isValidMove(int x, int y, int player) {
        if (board[x][y] != 0) return false;

        boolean valid = false;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                if (isDirectionValid(x, y, dx, dy, player)) {
                    valid = true;
                }
            }
        }
        return valid;
    }

    //helper function to test valid move
	private boolean isDirectionValid(int x, int y, int dx, int dy, int player) {
        int opponent = player == 1 ? 2 : 1;
        int nx = x + dx;
        int ny = y + dy;
        boolean foundOpponent = false;

        while (nx >= 0 && nx < row && ny >= 0 && ny < col) {
            if (board[nx][ny] == opponent) {
                foundOpponent = true;
            } else if (board[nx][ny] == player && foundOpponent) {
                return true;
            } else {
                return false;
            }
            nx += dx;
            ny += dy;
        }

        return false;
    }

    //when move is made, flip the appropriate discs
    private void flip(int x, int y, int player) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                if (isDirectionValid(x, y, dx, dy, player)) {
                    flipInDirection(x, y, dx, dy, player);
                }
            }
        }
    }
    
    //helper function to flip discs
    private void flipInDirection(int x, int y, int dx, int dy, int player) {
        int nx = x + dx;
        int ny = y + dy;
        int opponent = player == 1 ? 2 : 1;

        while (nx >= 0 && nx < row && ny >= 0 && ny < col && board[nx][ny] == opponent) {
            board[nx][ny] = player;
            nx += dx;
            ny += dy;
        }
    }

    //check if game is over by checking if neither player has an available move
	public boolean isGameOver() {
		return getAvailableMoves(player).isEmpty() && getAvailableMoves(computer).isEmpty();
	}

    //get all of the available moves for the current player
	public List<int[]> getAvailableMoves(int currentPlayer) {
        List<int[]> availableMoves = new ArrayList<>();

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (isValidMove(i, j, currentPlayer)) {
                    availableMoves.add(new int[] {i, j});
                }
            }
        }

        return availableMoves;
    }

    //count the number of discs for a player
	public int countDiscs(int player) {
        int count = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (board[i][j] == player) {
                    count++;
                }
            }
        }
        return count;
    }

    //count the number of discs for each player and determine the winner
    public String getWinner() {
        int playerCount = countDiscs(player);
        int computerCount = countDiscs(computer);
        if (playerCount > computerCount) return "Player wins!";
        if (computerCount > playerCount) return "Computer wins!";
        return "It's a tie!";
    }

    //computer that plays randomly
	public void randomComputer(int currentPlayer) {
		List<int[]> availableMoves = getAvailableMoves(currentPlayer);
	
		if (!availableMoves.isEmpty()) {
			int[] move = availableMoves.get(new Random().nextInt(availableMoves.size()));
			makeMove(move[0], move[1], currentPlayer);
		}
        else {
            updateTurn();
        }
	}

    //evaluation for minimax method
    private int evaluateBoard(int player) {
        int opponent = player == 1 ? 2 : 1;
        return countDiscs(player) - countDiscs(opponent);
    }
    
    //copy current board; necessary for minimax and heurtic-minimax methods
    private int[][] copyBoard() {
        int[][] newBoard = new int[row][col];
        for (int i = 0; i < row; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, col);
        }
        return newBoard;
    }
    
    //make a temporary move; necessary for minimax and heurtic-minimax methods
    private void makeTemporaryMove(int x, int y, int currentPlayer) {
        if (board[x][y] != 0 || !isValidMove(x, y, currentPlayer)) {
            return; // Or handle this scenario appropriately.
        }
        board[x][y] = currentPlayer;
        flip(x, y, currentPlayer);
    }

    //computer that makes decisions with minimax-method
    public void minimaxComputer(int currentPlayer) {
        if (getAvailableMoves(currentPlayer).isEmpty()) {
            updateTurn();
        }

        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;
    
        for (int[] move : getAvailableMoves(currentPlayer)) {
            int[][] originalBoard = copyBoard();
            makeTemporaryMove(move[0], move[1], currentPlayer);
    
            int score = minimax(5, currentPlayer == 1 ? 2 : 1, false);
    
            board = originalBoard;
    
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
    
        if (bestMove != null) {
            makeMove(bestMove[0], bestMove[1], currentPlayer);
        }
        else {
            int[] move = getAvailableMoves(currentPlayer).get(new Random().nextInt(getAvailableMoves(currentPlayer).size()));
            makeMove(move[0], move[1], currentPlayer);
        }
    }

    //helper function for minimax
    private int minimax(int depth, int currentPlayer, boolean isMaximizing) {
        if (depth == 0 || isGameOver()) {
            return evaluateBoard(computer);
        }
    
        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int[] move : getAvailableMoves(currentPlayer)) {
            int[][] originalBoard = copyBoard();
            makeTemporaryMove(move[0], move[1], currentPlayer);
    
            int score = minimax(depth - 1, currentPlayer == 1 ? 2 : 1, !isMaximizing);
    
            board = originalBoard;

            if (isMaximizing) {
                bestScore = Math.max(bestScore, score);
            } else {
                bestScore = Math.min(bestScore, score);
            }
        }
    
        return bestScore;
    }
    
    //used to evaluate the board score using a heuristic approach for the Reversi game.
    private int heuristic(int[][] board) {
        int[][] h = {
            {100, -20, 20, 20, 20, 20, -20, 100},
            {-20, -50, 10, 5, 5, 10, -50, -20},
            {20, 5, 10, 10, 10, 10, 5, 20},
            {20, 5, 10, 1, 1, 10, 5, 20},
            {20, 5, 10, 1, 1, 10, 5, 20},
            {20, 5, 10, 10, 10, 10, 5, 20},
            {-20, -50, 10, 5, 5, 10, -50, -20},
            {100, -20, 20, 20, 20, 20, -20, 100}
        };
        
        int score = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == computer) {
                    score += h[i][j];
                } else if (board[i][j] == player) {
                    score -= h[i][j];
                }
            }
        }
        return score;
    }

    //computer that makes decisions with heuristic minimax with alpha-beta pruning
    public void heuristicMinimaxComputer(int currentPlayer) {
        if (getAvailableMoves(currentPlayer).isEmpty()) {
            updateTurn();
        }

        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (int[] move : getAvailableMoves(currentPlayer)) {
            int[][] originalBoard = copyBoard();
            makeTemporaryMove(move[0], move[1], currentPlayer);
            int score = heuristicMinimax(5, currentPlayer == 1 ? 2 : 1, alpha, beta, false);
            board = originalBoard;

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        if (bestMove != null) {
            makeMove(bestMove[0], bestMove[1], currentPlayer);
        }
        else {
            int[] move = getAvailableMoves(currentPlayer).get(new Random().nextInt(getAvailableMoves(currentPlayer).size()));
            makeMove(move[0], move[1], currentPlayer);
        }
    }

    //helper function for heuristic minimax
    private int heuristicMinimax(int depth, int currentPlayer, int alpha, int beta, boolean isMaximizing) {
        if (depth == 0 || isGameOver()) {
            return heuristic(board);
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int[] move : getAvailableMoves(currentPlayer)) {
                int[][] originalBoard = copyBoard();
                makeTemporaryMove(move[0], move[1], currentPlayer);
                int eval = heuristicMinimax(depth - 1, currentPlayer == 1 ? 2 : 1, alpha, beta, false);
                board = originalBoard;
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int[] move : getAvailableMoves(currentPlayer)) {
                int[][] originalBoard = copyBoard();
                makeTemporaryMove(move[0], move[1], currentPlayer);
                int eval = heuristicMinimax(depth - 1, currentPlayer == 1 ? 2 : 1, alpha, beta, true);
                board = originalBoard;
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return minEval;
        }
    }
}

