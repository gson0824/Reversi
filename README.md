![sc](https://github.com/gson0824/Reversi/assets/128443982/e32c88e0-b224-4df6-9c3b-8a4fd398a7c1)

GyeongJun Son Reversi project

--------------------------------------------------------------------------------------

Compile: javac Main.java GameWindow.java

Run: java Main

--------------------------------------------------------------------------------------

This project is an implementation of the classic Reversi game (also known as Othello) in Java, featuring a graphical user interface (GUI) and an intelligent computer opponent. Players can enjoy the game against an AI that uses heuristic analysis and the minimax algorithm to determine its moves, providing varying levels of challenge.

The AI's decision-making process is based on the minimax algorithm, enhanced with alpha-beta pruning for efficiency. At the "hard" difficulty level, it evaluates the game board using a heuristic function that assigns scores to each board tile based on its strategic value (corners and edges are typically more valuable). This score is then used to predict the most advantageous moves.
