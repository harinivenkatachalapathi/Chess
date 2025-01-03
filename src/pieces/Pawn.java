package chess;

import java.util.ArrayList;

public class Pawn extends Piece {
    public boolean enpassant;

    public boolean hasMoved;  // Additional field to track whether the pawn has moved

    public Pawn(PieceFile file, int rank, Chess.Player player, boolean enpassant) {
        this.file = file;
        this.rank = rank;
        this.player = player;
        this.hasMoved = false;  // Initialize hasMoved to false
        this.enpassant = enpassant;
    }

    public boolean getEnpassant() {
        return enpassant;
    }

    public boolean hasItMoved(){
        return hasMoved;
    }

    public void setEnpassant(boolean b) {
        this.enpassant = b;
    }

    public ArrayList<int[]> getMoves(Piece[][] b) {
        ArrayList<int[]> moves = new ArrayList<>();
        int currentFile = file.ordinal();
        int currentRank = rank;

        // Basic movement
        int forwardDirection = (player == Chess.Player.white) ? 1 : -1;

        // Move one square forward
        int newRank = currentRank + forwardDirection;
        if (isValidMove(currentFile, newRank) && isEmpty(b, currentFile, newRank)) {
            moves.add(new int[]{currentFile, newRank});

            // Move two squares forward if the pawn hasn't moved yet
            if (!hasMoved) {
                newRank = currentRank + 2 * forwardDirection;
                if (isValidMove(currentFile, newRank) && isEmpty(b, currentFile, newRank)) {
                    moves.add(new int[]{currentFile, newRank});
                }
            }
        }

        // Capture diagonally
        int[] leftCapture = {currentFile - 1, currentRank + forwardDirection};
        int[] rightCapture = {currentFile + 1, currentRank + forwardDirection};

        if (isValidMove(leftCapture[0], leftCapture[1]) && isEnemy(b, leftCapture[0], leftCapture[1])) {
            moves.add(leftCapture);
        }

        if (isValidMove(rightCapture[0], rightCapture[1]) && isEnemy(b, rightCapture[0], rightCapture[1])) {
            moves.add(rightCapture);
        }

        return moves;
    }

    public void enpassant(Board b, ArrayList<int[]> getMoves) {
        int row = this.file.ordinal();
        int col = this.rank;
    
        if (row == 3 || row == 4) {
            int direction = (row == 3) ? -1 : 1;
    
            if (col > 0) {
                Piece left = Board.getPieceAt(Piece.PieceFile.values()[row + direction], col - 1);
                if (left instanceof Pawn && !left.player.equals(this.player) && ((Pawn) left).getEnpassant()) {
                    getMoves.add(new int[] {row + direction, col - 1});
                }
            }
    
            if (col < 7) {
                Piece right = Board.getPieceAt(Piece.PieceFile.values()[row + direction], col + 1);
                if (right instanceof Pawn && !right.player.equals(this.player) && ((Pawn) right).getEnpassant()) {
                    getMoves.add(new int[] {row + direction, col + 1});
                }
            }
        }
    }
    
    public void promote(String promotion, Piece[][] b, int newFile, int newRank) {
        if ((player == Chess.Player.white && newRank == 7) || (player == Chess.Player.black && newRank == 0)) {
            Piece promotedPiece;
            switch (promotion.toUpperCase()) {
                case "N":
                    promotedPiece = new Knight(file, rank, player);
                    break;
                case "R":
                    promotedPiece = new Rook(file, rank, player);
                    break;
                case "B":
                    promotedPiece = new Bishop(file, rank, player);
                    break;
                default:
                    promotedPiece = new Queen(file, rank, player);
                    break;
            }

            b[file.ordinal()][rank] = null;
            b[newFile][newRank] = promotedPiece;
        }
    }
    
    private boolean isValidMove(int file, int rank) {
        return file >= 0 && file <= 7 && rank >= 0 && rank <= 7;
    }

    private boolean isEmpty(Piece[][] b, int file, int rank) {
        return b[file][rank-1] == null;
    }

    private boolean isEnemy(Piece[][] b, int file, int rank) {
        Piece piece = Board.getPieceAt(PieceFile.values()[file], rank);
        return piece != null && piece.player != this.player;
    }
}
