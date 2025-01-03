package chess;

import java.util.ArrayList;

public class Rook extends Piece {
    public boolean castling = true;
    public Rook(PieceFile file, int rank, Chess.Player player) {
        this.file = file;
        this.rank = rank;
        this.player = player;
    }

    public ArrayList<int[]> getMoves(Piece[][] b) {
        ArrayList<int[]> moves = new ArrayList<>();
        int currentFile = file.ordinal();
        int currentRank = rank;

        basicMovement(b, moves, currentFile, currentRank);

        return moves;
    }

    public void basicMovement(Piece[][] b, ArrayList<int[]> getMoves, int file, int rank) {

        moveHorizontally(b, getMoves, file, rank, 1, 0);
        moveHorizontally(b, getMoves, file, rank, -1, 0);
        moveVertically(b, getMoves, file, rank, 0, 1);
        moveVertically(b, getMoves, file, rank, 0, -1);
    }

    private void moveHorizontally(Piece[][] b, ArrayList<int[]> getMoves, int file, int rank, int newFile, int drank) {
        for (int i = file + newFile; i >= 0 && i < 8; i += newFile) {
            if (processMove(b, getMoves, i, rank)) {
                break;
            }
        }
    }

    private void moveVertically(Piece[][] b, ArrayList<int[]> getMoves, int file, int y, int newfile, int dy) {
        for (int j = y + dy; j > 0 && j <= 8; j += dy) {

            if (processMove(b, getMoves, file, j)) {
                break;
            }
        }
    }

    private boolean processMove(Piece[][] b, ArrayList<int[]> getMoves, int file, int rank) {
        PieceFile pieceFile = PieceFile.values()[file];
        int[] p = { file, rank };

        if (Board.getPieceAt(pieceFile, rank) == null) {
            getMoves.add(p);
        } else if (!(Board.getPieceAt(pieceFile, rank).player.equals(this.player))) {
            getMoves.add(p);
            return true;
        } else {
            return true;
        }
        return false;
    }
    @Override
    public Rook clone() {
        // Implement the clone method for the Pawn class
        return (Rook) super.clone();
    }
}