package org.example.service;

public class BoardImpl implements Board{
    private BoardUi boardUI;
    private Piece[][] pieces;


    public BoardImpl(BoardUi boardUI){
        this.boardUI = boardUI;
        pieces = new Piece[NUM_OF_COLS][NUM_OF_ROWS];
        initializeBoard();
    }

    private void initializeBoard(){
        for(int col = 0; col < NUM_OF_COLS; col++){
            for(int row = 0; row < NUM_OF_ROWS; row++){
                pieces[col][row] = Piece.EMPTY;
            }
        }
    }

    @Override
    public BoardUi getBoardUI() {
        return boardUI;
    }

    @Override
    public int findNextAvailableSpot(int col){
        for(int row = NUM_OF_ROWS - 1; row >= 0; row--){
            if(pieces[col][row] == Piece.EMPTY) {
                return row; // Found an empty spot in the specified column
            }
        }
        return -1; // No available spot in the specified column
    }

    @Override
    public boolean isLegalMove(int col){
        return findNextAvailableSpot(col) != -1;
    }

    @Override
    public boolean existLegalMoves(){
        for(int col = 0; col < NUM_OF_COLS; col++){
            if(isLegalMove(col)){
                return true; // There's at least one legal move
            }
        }
        return false; // No legal moves left
    }

    @Override
    public void updateMove(int col,Piece move){
        pieces[col][findNextAvailableSpot(col)] = move;
    }

    @Override
    public void updateMove(int col,int row,Piece move){
        pieces[col][row] = move;
    }
    @Override
    public Winner findWinner(){
        // Check for a horizontal win
        for(int row = 0; row < NUM_OF_ROWS; row++){
            for(int col = 0; col < NUM_OF_COLS - 3; col++){
                Piece piece = pieces[col][row];
                if(piece != Piece.EMPTY && piece == pieces[col + 1][row] && piece == pieces[col + 2][row] && piece ==
                        pieces[col + 3][row]){
                    // Horizontal win
                    return new Winner(piece , col , row ,col + 3, row);
                }
            }
        }

        for(int col = 0; col < NUM_OF_COLS; col++){
            // Check for a vertical win
            for(int row = 0; row < NUM_OF_ROWS - 3; row++){
                Piece piece = pieces[col][row];
                if(piece != Piece.EMPTY && piece == pieces[col][row + 1] && piece == pieces[col][row + 2] && piece ==
                        pieces[col][row + 3]){
                    // Vertical win
                    return new Winner(piece,col,row,col,row + 3);
                }
            }
        }
        return new Winner(Piece.EMPTY,-1,-1,-1,-1);
    }
}
