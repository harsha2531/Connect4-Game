package org.example.service;

public class AiPlayer extends lk.ijse.dep.service.Player {
    @Override
    public void movePiece(int col) {
        col = predictColumn();
        board.updateMove(col,Piece.GREEN);

        board.getBoardUI().update(col,false);

        Winner winner = board.findWinner();
        if(winner.getWinningPiece() == Piece.GREEN){
            board.getBoardUI().notifyWinner(winner);
        }else{
            if(!board.existLegalMoves()){
                board.getBoardUI().notifyWinner(winner);
            }
        }

    }

    private int predictColumn() {
        boolean isUserWarning = false;
        int tiedColumn = 0;


        for(int col = 0; col < 6; ++col){ //Iterate through columns
            if(board.isLegalMove(col)){
                int row = board.findNextAvailableSpot(col);
                board.updateMove(col,Piece.GREEN);
                int heuristicValue = miniMax(0,false);
                board.updateMove(col,row,Piece.EMPTY);
                if(heuristicValue == 1){
                    return col; // If the AI can win return this column
                }
                if(heuristicValue == -1){
                    isUserWarning = true;
                }else {
                    tiedColumn = col; // Store a column where there is no immediate win or loss
                }
            }
        }
        if(isUserWarning && board.isLegalMove(tiedColumn)){
            return tiedColumn; // block the user's win
        }else {
            int randomCol;

            do{
                randomCol = (int) (Math.random() * 6.0);
            }while (!board.isLegalMove(randomCol));

            return randomCol; // If there is bo winning move or blocking move choose a random column
        }
    }

    private int miniMax(int depth, boolean maximizingPlayer) {
        Winner winner = board.findWinner();
        // Base case: return a score if the game is over
        if(winner.getWinningPiece() == Piece.GREEN){
            return 1; // AI Wins
        }else if(winner.getWinningPiece() == Piece.BLUE){
            return -1; // User Wins
        } else if (board.existLegalMoves() && depth == 2) {
            return 0; // Game tied
        }

        int bestScore; // Initialize a variable to store the best score

        if(maximizingPlayer){
            // If it's the AI's turn(maximizing player)
            bestScore = Integer.MIN_VALUE;// Initialize the best score to a very low value to get updated
            for(int col = 0; col < 6; ++col){
                if(board.isLegalMove(col)){
                    int row = board.findNextAvailableSpot(col); // Find the next available spot
                    board.updateMove(col,Piece.GREEN);// Simulate making move for the AI
                    int score = miniMax(depth + 1 , false);// Recursively evaluate the game state for the user's turn
                    board.updateMove(col , row, Piece.EMPTY); // Undo the simulated move

                    // update best score if the current score is greater
                    if(score > bestScore){
                        bestScore = score;
                    }
                }
            }
        }else {
            // If it's the user's turn
            // Initialize the best score to a very high value to get updated
            bestScore = Integer.MAX_VALUE;
            for(int col = 0; col < 6; ++col){
                if(board.isLegalMove(col)){
                    int row = board.findNextAvailableSpot(col);// Find the next available row in the column
                    board.updateMove(col,Piece.BLUE); // Simulate making a move for the user
                    int score = miniMax(depth + 1 , true); // Recursively evaluate the game state for the AI's turn
                    board.updateMove(col,row,Piece.EMPTY);// Undo the simulated move

                    // Update the best score if the current score is smaller
                    if(score < bestScore){
                        bestScore = score;
                    }
                }
            }
        }
        // Return the best score, which represents the best score the AI can achieve for the current game state.
        return bestScore;
    }

    public AiPlayer(Board board) {
        super(board);
    }
}
