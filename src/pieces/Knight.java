package chess;
import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(PieceFile file, int rank, Chess.Player player) {
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

    private void basicMovement(Piece[][] b, ArrayList<int[]> moves, int file, int rank) {
        int[][] potentialMoves = {
                {file - 2, rank + 1}, {file - 1, rank + 2}, {file + 1, rank + 2}, {file + 2, rank + 1},
                {file - 2, rank - 1}, {file - 1, rank - 2}, {file + 1, rank - 2}, {file + 2, rank - 1}
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
        return file >= 0 && file <= 7 && rank > 0 && rank <= 7;
    }

    private boolean isEmptyOrEnemy(Piece[][] b, int file, int rank) {
        Piece piece = Board.getPieceAt(PieceFile.values()[file], rank);
        return piece == null || piece.player != this.player;
    }
}