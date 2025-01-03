package chess;

import java.util.ArrayList;

public class Queen extends Piece {

    public Queen(PieceFile file, int rank, Chess.Player player) {
        this.file = file;
        this.rank = rank;
        this.player = player;
    }

    public ArrayList<int[]> getMoves(Piece[][] b) {
        ArrayList<int[]> moves = new ArrayList<>();
        int currentFile = file.ordinal();
        int currentRank = rank;

        lineMovement(b, moves, currentFile, currentRank);
        diagMovement(b, moves, currentFile, currentRank);

        return moves;
    }

    public void lineMovement(Piece[][] b, ArrayList<int[]> getMoves, int file, int rank) {

        moveHorizontally(b, getMoves, file, rank, 1, 0);
        moveHorizontally(b, getMoves, file, rank, -1, 0);
        moveVertically(b, getMoves, file, rank, 0, 1);
        moveVertically(b, getMoves, file, rank, 0, -1);
    }

    public void diagMovement(Piece[][] b, ArrayList<int[]> getMoves, int file, int rank) {

        moveDiagonally(b, getMoves, file, rank, 1, 1);
        moveDiagonally(b, getMoves, file, rank, 1, -1);
        moveDiagonally(b, getMoves, file, rank, -1, 1);
        moveDiagonally(b, getMoves, file, rank, -1, -1);
    }   

    private void moveHorizontally(Piece[][] b, ArrayList<int[]> getMoves, int file, int rank, int newFile, int newRank) {
        for (int i = file + newFile; i >= 0 && i < 8; i += newFile) {
            if (processMove(b, getMoves, i, rank)) {
                break;
            }
        }
    }

    private void moveVertically(Piece[][] b, ArrayList<int[]> getMoves, int file, int rank, int newFile, int newRank) {
        for (int j = rank + newRank; j > 0 && j <= 8; j += newRank) {

            if (processMove(b, getMoves, file, j)) {
                break;
            }
        }
    }

    private void moveDiagonally(Piece[][] b, ArrayList<int[]> getMoves, int file, int rank, int newFile, int newRank) {
        for (int i = file + newFile; i >= 0 && i < 8; i += newFile){
            if (processMove(b, getMoves, file, i)) {
                break;
            }
        for (int j = rank + newRank; j >= 0 && j < 8; j += newRank) {
            if (processMove(b, getMoves, rank, j)) {
                break;
            }
        }
        }
    } 

    private boolean processMove(Piece[][] b, ArrayList<int[]> getMoves, int file, int rank) {
        PieceFile pieceFile = PieceFile.values()[file];
        int[] p = { file, rank };
        
        if (isValidMove(p[0], p[1]) && (b[p[0]][p[1]] == null || !b[p[0]][p[1]].player.equals(this.player))) {
            getMoves.add(p);
            return b[p[0]][p[1]] != null;
        }
        return true;
    }

    private boolean isValidMove(int file, int rank) {
        return file >= 0 && file < 8 && rank >= 0 && rank < 8;
    }
}
