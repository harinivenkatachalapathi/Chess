package chess;

import java.util.ArrayList;

public class King extends Piece {
    public boolean castling = true;

    public King(PieceFile file, int rank, Chess.Player player) {
        this.file = file;
        this.rank = rank;
        this.player = player;
    }

    public ArrayList<int[]> getMoves(Piece[][] b) {
        ArrayList<int[]> moves = new ArrayList<>();
        int currentFile = file.ordinal();
        int currentRank = rank; // Convert rank to array index in file

        basicMovement(b, moves, currentFile, currentRank);

        castling(b, moves, currentFile, currentRank);

        return moves;
    }

    private void basicMovement(Piece[][] b, ArrayList<int[]> moves, int file, int rank) {
        int[][] potentialMoves = {
                { file - 1, rank }, { file + 1, rank }, { file, rank + 1 }, { file, rank - 1 },
                { file - 1, rank + 1 }, { file - 1, rank - 1 }, { file + 1, rank + 1 }, { file + 1, rank - 1 }
        };

        for (int[] move : potentialMoves) {
            if (isValidMove(move[0], move[1])) {
                if (isEmptyOrEnemy(b, move[0], move[1])) {
                    moves.add(move);
                }
            }
        }
    }

    private boolean isValidMove(int file, int rank) {
        return file >= 0 && file <= 7 && rank > 0 && rank <= 8;
    }

    private boolean isEmptyOrEnemy(Piece[][] b, int file, int rank) {
        Piece piece = Board.getPieceAt(PieceFile.values()[file], rank);
        return piece == null || piece.player != this.player;
    }

    @Override
    public King clone() {
        // Implement the clone method for the Pawn class
        return (King) super.clone();
    }

    private void castling(Piece[][] b, ArrayList<int[]> moves, int currentFile, int rank) {
        if (!this.castling)
            return;

        ArrayList<Piece> rooksThatCanCastle = new ArrayList<>();
        for (Piece[] row : b) {
            for (Piece p : row) {
                if (p instanceof Rook && p.player.equals(this.player) && ((Rook) p).castling) {
                    rooksThatCanCastle.add(p);
                }
            }
        }
        if (rooksThatCanCastle.isEmpty())
            return;

        for (Piece rook : rooksThatCanCastle) {
            int rookFile = rook.file.ordinal();
            int rookRank = rook.rank;

            if (rookRank == rank) {
                int kingDirection = Integer.compare(rookFile, currentFile); // -1 if rook is left, 1 if rook is right

                // Check if squares between king and rook are empty
                int squaresBetween = Math.abs(rookFile - currentFile) - 1;
                boolean squaresEmpty = true;
                for (int i = 1; i <= squaresBetween; i++) {
                    int file = currentFile + i * kingDirection;
                    if (!isEmpty(b, file, rank)) {
                        squaresEmpty = false;
                        break;
                    }
                }

                // If squares are empty, add castling move
                if (squaresEmpty) {
                    int newFile = currentFile + 2 * kingDirection;
                    moves.add(new int[] { newFile, rank });
                }
            }
        }
    }
    private boolean isEmpty(Piece[][] b, int file, int rank) {
        return b[file][rank - 1] == null;
    }
}