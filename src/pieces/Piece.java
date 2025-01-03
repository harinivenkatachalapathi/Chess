package chess;

import java.util.ArrayList;

abstract class Piece implements Cloneable {
    static enum PieceFile {
        a, b, c, d, e, f, g, h
    };

    PieceFile file;
    int rank;
    Chess.Player player;

public abstract ArrayList<int[]> getMoves(Piece[][] b);    
@Override
    public Piece clone() {
        try {
            return (Piece) super.clone();
        } catch (CloneNotSupportedException e) {
            // This should not happen since Piece implements Cloneable
            throw new InternalError(e);
        }
    }
}