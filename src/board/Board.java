package chess;

import chess.Chess.Player;
import chess.Piece.PieceFile;
import chess.ReturnPlay.Message;

import java.util.ArrayList;

public class Board {

    public static Piece[][] board;

    public enum currentPlayer {
        white, black
    };

    static Player currPlayer = Player.white;
    static boolean drawRequested = false;
    static boolean moveMade = false;
    private static int[] enPassantTarget; // {file, rank} of the potential en passant target

    public Board() {
        // New Board

        // CONE
        board = new Piece[8][8];

        // CONVENTION WILL BE FILERANK -> ARRAY[FILE][RANK-1]

        // Place all black pieces
        // board[PieceFile.b.ordinal()][7] = new Knight(PieceFile.b, 8, Player.black);

        board[PieceFile.a.ordinal()][7] = new Rook(PieceFile.a, 8, Player.black);
        /*
         * b8
         * board[PieceFile.c.ordinal()][7] = new Bishop(PieceFile.c, 8, Player.black);
         * // // c8
         * 
         * 
         * board[PieceFile.f.ordinal()][7] = new Bishop(PieceFile.f, 8, Player.black);
         * // // f8
         */// board[PieceFile.g.ordinal()][7] = new Knight(PieceFile.g, 8, Player.black);
        board[PieceFile.h.ordinal()][7] = new Rook(PieceFile.h, 8, Player.black); //

        /*
         * board[0][6] = new Pawn(PieceFile.a, 7, Player.black);
         * board[1][6] = new Pawn(PieceFile.b, 7, Player.black);
         * board[2][6] = new Pawn(PieceFile.c, 7, Player.black);
         * board[3][6] = new Pawn(PieceFile.d, 7, Player.black);
         * board[4][6] = new Pawn(PieceFile.e, 7, Player.black);
         * board[5][6] = new Pawn(PieceFile.f, 7, Player.black);
         * board[6][6] = new Pawn(PieceFile.g, 7, Player.black);
         * board[7][6] = new Pawn(PieceFile.h, 7, Player.black);
         */

        board[PieceFile.d.ordinal()][7] = new Queen(PieceFile.d, 8, Player.black); //
        // // d8
        board[PieceFile.e.ordinal()][7] = new King(PieceFile.e, 8, Player.black); /// e8
        //// Place all white pieces

        board[0][0] = new Rook(PieceFile.a, 1, Player.white);
        // board[1][0] = new Knight(PieceFile.b, 1, Player.white);
        board[2][0] = new Bishop(PieceFile.c, 1, Player.white);

        board[5][0] = new Bishop(PieceFile.f, 1, Player.white);
        board[6][0] = new Knight(PieceFile.g, 1, Player.white);

        board[7][0] = new Rook(PieceFile.h, 1, Player.white);

        /*
         * board[0][1] = new Pawn(PieceFile.a, 2, Player.white);
         * board[1][1] = new Pawn(PieceFile.b, 2, Player.white);
         * board[2][1] = new Pawn(PieceFile.c, 2, Player.white);
         * board[3][1] = new Pawn(PieceFile.d, 2, Player.white);
         * board[4][1] = new Pawn(PieceFile.e, 2, Player.white);
         * board[5][1] = new Pawn(PieceFile.f, 2, Player.white);
         * board[6][1] = new Pawn(PieceFile.g, 2, Player.white);
         * board[7][1] = new Pawn(PieceFile.h, 2, Player.white);
         */

        board[4][0] = new King(PieceFile.e, 1, Player.white);
        board[3][0] = new Queen(PieceFile.d, 1, Player.white);
    }

    public static ReturnPlay move(String requestedMove) {
        ReturnPlay returnPlay = new ReturnPlay();
        ArrayList<ReturnPiece> pieces = new ArrayList<>();

        int i = 0;

        // Turn original position into PieceFile and Rank
        while (requestedMove.charAt(i) == ' ') {
            requestedMove = requestedMove.substring(i + 1);
        }
        if (requestedMove.contains("resign")) { // If player requests resign then resign
            if (currPlayer == Player.white) {
                returnPlay.message = Message.RESIGN_BLACK_WINS;
            } else {
                returnPlay.message = Message.RESIGN_WHITE_WINS;

            }

        } else { // If no resign then move
            PieceFile firstFile = parsePieceFile(requestedMove.charAt(0));
            int firstRank = Integer.parseInt(requestedMove.substring(1, 2));
            requestedMove = requestedMove.substring(2);

            i = 0;
            while (requestedMove.charAt(i) == ' ') {
                requestedMove = requestedMove.substring(i + 1);
            }

            PieceFile newFile = parsePieceFile(requestedMove.charAt(0));
            int newRank = requestedMove.charAt(1) - '0';
            if (board[firstFile.ordinal()][firstRank - 1] == null) { // If original spot is empty
                returnPlay.message = Message.ILLEGAL_MOVE;
            } else {
                if (board[firstFile.ordinal()][firstRank - 1].player == currPlayer) {
                    // Check if the piece is allowed to go there

                    Piece piece = board[firstFile.ordinal()][firstRank - 1];
                    ArrayList<int[]> possibleMoves;
                    if (piece instanceof Pawn) {
                        possibleMoves = ((Pawn) piece).getMoves(board, enPassantTarget);
                    } else {
                        possibleMoves = piece.getMoves(board);
                    }

                    int[] targetMove = new int[] { newFile.ordinal(), newRank };

                    if (containsMove(possibleMoves, targetMove)) { // If the requested move is within possible moves
                        // Capture the piece at the target move (if any)
                        Piece capturedPiece = board[newFile.ordinal()][newRank - 1];

                        if (isCheck(currPlayer)) { // If the current player is in check
                            // Make the move
                            board[newFile.ordinal()][newRank - 1] = board[firstFile.ordinal()][firstRank - 1];
                            board[firstFile.ordinal()][firstRank - 1] = null;
                            board[newFile.ordinal()][newRank - 1].file = newFile; // Update file and rank
                            board[newFile.ordinal()][newRank - 1].rank = newRank;
                            moveMade = true;
                            if (board[newFile.ordinal()][newRank - 1] instanceof King) {
                                King temp = (King) board[newFile.ordinal()][newRank - 1];
                                temp.castling = false;
                            }
                            if (board[newFile.ordinal()][newRank - 1] instanceof Rook) {
                                Rook temp = (Rook) board[newFile.ordinal()][newRank - 1];
                                temp.castling = false;
                            }
                            if (isCheck(currPlayer)) { // if they're still in check after the move

                                board[firstFile.ordinal()][firstRank - 1] = board[newFile.ordinal()][newRank - 1];
                                board[newFile.ordinal()][newRank - 1] = capturedPiece; // Restore the captured piece
                                board[firstFile.ordinal()][firstRank - 1].file = firstFile; // Restore file and rank
                                board[firstFile.ordinal()][firstRank - 1].rank = firstRank;

                                returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
                                moveMade = false;
                                if (board[newFile.ordinal()][newRank - 1] instanceof King) {
                                    King temp = (King) board[newFile.ordinal()][newRank - 1];
                                    temp.castling = true;
                                }
                                if (board[newFile.ordinal()][newRank - 1] instanceof Rook) {
                                    Rook temp = (Rook) board[newFile.ordinal()][newRank - 1];
                                    temp.castling = true;
                                }
                            }

                        } else {
                            if (piece instanceof King && Math.abs(newFile.ordinal() - firstFile.ordinal()) == 2) {

                                handleCastling(firstFile.ordinal(), firstRank, newFile.ordinal(), newRank);
                                moveMade = true;
                                if (board[newFile.ordinal()][newRank - 1] instanceof King) {
                                    King temp = (King) board[newFile.ordinal()][newRank - 1];
                                    temp.castling = false;
                                }
                                if (board[newFile.ordinal()][newRank - 1] instanceof Rook) {
                                    Rook temp = (Rook) board[newFile.ordinal()][newRank - 1];
                                    temp.castling = false;
                                }
                            } else {
                                board[newFile.ordinal()][newRank - 1] = board[firstFile.ordinal()][firstRank - 1];
                                board[firstFile.ordinal()][firstRank - 1] = null;
                                board[newFile.ordinal()][newRank - 1].file = newFile; // Update file and rank
                                board[newFile.ordinal()][newRank - 1].rank = newRank;
                                moveMade = true;
                                if (board[newFile.ordinal()][newRank - 1] instanceof King) {
                                    King temp = (King) board[newFile.ordinal()][newRank - 1];
                                    temp.castling = false;
                                }
                                if (board[newFile.ordinal()][newRank - 1] instanceof Rook) {
                                    Rook temp = (Rook) board[newFile.ordinal()][newRank - 1];
                                    temp.castling = false;
                                }
                            }

                            if (isCheck(currPlayer)) { // if they're in check after the move

                                board[firstFile.ordinal()][firstRank - 1] = board[newFile.ordinal()][newRank - 1];
                                board[newFile.ordinal()][newRank - 1] = capturedPiece; // Restore the captured piece
                                board[firstFile.ordinal()][firstRank - 1].file = firstFile; // Restore file and rank
                                board[firstFile.ordinal()][firstRank - 1].rank = firstRank;

                                returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
                                moveMade = false;
                                if (board[newFile.ordinal()][newRank - 1] instanceof King) {
                                    King temp = (King) board[newFile.ordinal()][newRank - 1];
                                    temp.castling = true;
                                }
                                if (board[newFile.ordinal()][newRank - 1] instanceof Rook) {
                                    Rook temp = (Rook) board[newFile.ordinal()][newRank - 1];
                                    temp.castling = true;
                                }
                            }
                        }

                        if (board[newFile.ordinal()][newRank - 1] instanceof Pawn) { // if the moved piece was a pawn
                            Pawn pawn = (Pawn) board[newFile.ordinal()][newRank - 1];
                            if (!pawn.hasMoved) { //
                                pawn.hasMoved = true;
                            }
                            if ((currPlayer == Player.white && newRank == 8)
                                    || (currPlayer == Player.black && newRank == 1)) {
                                requestedMove = requestedMove.substring(i + 2);
                                if (requestedMove != "") {
                                    while (requestedMove.charAt(i) == ' ') {
                                        requestedMove = requestedMove.substring(i + 1);
                                    }
                                }

                                // Prompt for pawn promotion (you can modify this part based on your user input)
                                String promotion = requestedMove; // Get user input for promotion (N, R, B, Q, for
                                                                  // Knight, Rook, Bishop, Queen)

                                // Perform pawn promotion
                                pawn.promote(promotion, board, newFile.ordinal(), newRank - 1);
                            }
                        }

                        Player current = Board.currPlayer;
                        if (moveMade) {
                            if (currPlayer == Player.white) { // If move was made, change to next player's turn
                                currPlayer = Player.black;
                            } else {
                                currPlayer = Player.white;
                            }
                            current = Board.currPlayer;

                            if (isCheck(current)) {
                                returnPlay.message = ReturnPlay.Message.CHECK;
                            }

                            if (isCheckmate(current)) {
                                if (current == Player.white) {
                                    returnPlay.message = ReturnPlay.Message.CHECKMATE_BLACK_WINS;

                                } else {
                                    returnPlay.message = ReturnPlay.Message.CHECKMATE_WHITE_WINS;

                                }
                            }
                        }

                    } else {
                        returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE; //
                        moveMade = false;
                    }

                } else {
                    returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE; // Wrong Player
                    moveMade = false;
                }

            }
        }

        if (returnPlay.message == null) {
            i = 0;
            while (requestedMove.charAt(i) == ' ') {
                requestedMove = requestedMove.substring(i + 1);
            }
            if (requestedMove.contains("draw?")) {
                returnPlay.message = ReturnPlay.Message.DRAW;
            }
        }

        // Populate pieces list for ReturnPlay
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                if (board[file][rank] != null) {
                    ReturnPiece temp = new ReturnPiece();
                    temp.pieceFile = ReturnPiece.PieceFile.values()[file];
                    temp.pieceRank = rank + 1;
                    temp.pieceType = getPieceType(board[file][rank]);
                    pieces.add(temp);
                }
            }
        }
        returnPlay.piecesOnBoard = pieces;
        return returnPlay;
    }

    private static ReturnPiece.PieceType getPieceType(Piece piece) {
        if (piece.player == Player.white) {
            if (piece instanceof Pawn) {
                return ReturnPiece.PieceType.WP;
            } else if (piece instanceof Rook) {
                return ReturnPiece.PieceType.WR;
            } else if (piece instanceof Bishop) {
                return ReturnPiece.PieceType.WB;
            } else if (piece instanceof Knight) {
                return ReturnPiece.PieceType.WN;
            } else if (piece instanceof Queen) {
                return ReturnPiece.PieceType.WQ;
            } else if (piece instanceof King) {
                return ReturnPiece.PieceType.WK;
            }
        } else {
            if (piece instanceof Pawn) {
                return ReturnPiece.PieceType.BP;
            } else if (piece instanceof Rook) {
                return ReturnPiece.PieceType.BR;
            } else if (piece instanceof Bishop) {
                return ReturnPiece.PieceType.BB;
            } else if (piece instanceof Knight) {
                return ReturnPiece.PieceType.BN;
            } else if (piece instanceof Queen) {
                return ReturnPiece.PieceType.BQ;
            } else if (piece instanceof King) {
                return ReturnPiece.PieceType.BK;
            }
        }
        return null;
    }

    static PieceFile parsePieceFile(char fileChar) {
        return PieceFile.valueOf(String.valueOf(fileChar));
    }

    public static Piece getPieceAt(PieceFile file, int rank) {
        return board[file.ordinal()][rank - 1];

    }

    public Piece[][] accessBoard() {
        return board;
    }

    private static boolean containsMove(ArrayList<int[]> moves, int[] targetMove) {
        for (int[] move : moves) {
            if (move[0] == targetMove[0] && move[1] == targetMove[1]) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCheck(Player kingPlayer) {
        // Identify the position of the king
        int kingFile = -1;
        int kingRank = -1;

        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Piece piece = board[file][rank];
                if (piece instanceof King && piece.player == kingPlayer) {
                    kingFile = file;
                    kingRank = rank;
                    break;
                }
            }
        }

        if (kingFile == -1 || kingRank == -1) {
            // King not found, something went wrong
            return false;
        }

        // Iterate through opponent pieces
        Player opponentPlayer = (kingPlayer == Player.white) ? Player.black : Player.white;

        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Piece opponentPiece = board[file][rank];

                if (opponentPiece != null && opponentPiece.player == opponentPlayer) {
                    // Check if opponent piece can attack the king
                    ArrayList<int[]> possibleMoves = opponentPiece.getMoves(board);
                    int[] kingPosition = new int[] { kingFile, kingRank + 1 };

                    if (containsMove(possibleMoves, kingPosition)) {
                        // King is in check
                        return true;
                    }
                }
            }
        }

        // King is not in check
        return false;
    }

    private static void handleCastling(int kingFirstFile, int kingFirstRank, int kingNewFile, int kingNewRank) {
        int rookNewFile = (kingNewFile > kingFirstFile) ? kingFirstFile + 1 : kingFirstFile - 1;

        board[kingNewFile][kingNewRank - 1] = board[kingFirstFile][kingFirstRank - 1]; // Move king to new spot
        board[kingFirstFile][kingFirstRank - 1] = null; // Make old spot empty
        board[kingNewFile][kingNewRank - 1].file = Piece.PieceFile.values()[kingNewFile - 1]; // Update king file
        board[kingNewFile][kingNewRank - 1].rank = kingNewRank; // Update king rank

        // Update the rook's file and rank
        if (kingNewFile > kingFirstFile) { // If using right rook
            board[rookNewFile][kingFirstRank - 1] = board[7][kingFirstRank - 1];
            board[7][kingFirstRank - 1] = null;
            board[rookNewFile][kingNewRank - 1].file = Piece.PieceFile.values()[rookNewFile];

        } else {
            board[rookNewFile][kingFirstRank - 1] = board[0][kingFirstRank - 1];
            board[0][kingFirstRank - 1] = null;
            board[rookNewFile][kingNewRank - 1].file = Piece.PieceFile.values()[rookNewFile];
        }
    }

    public static boolean isCheckmate(Player kingPlayer) {
        if (!isCheck(kingPlayer)) { // if not already in check
            return false;
        }

        for (Piece[] array : board) {
            for (Piece piece : array) {
                if (piece != null && piece.player == kingPlayer) { // For each friendly piece on the board
                    ArrayList<int[]> moves = piece.getMoves(board); // Generate the piece's moves
                    for (int[] move : moves) { // Simulate every move
                        PieceFile firstFile = piece.file;
                        int firstRank = piece.rank;
                        PieceFile newFile = Piece.PieceFile.values()[move[0]];
                        int newRank = move[1];

                        // Make the move
                        Piece capturedPiece = board[newFile.ordinal()][newRank - 1];
                        board[newFile.ordinal()][newRank - 1] = board[firstFile.ordinal()][firstRank - 1];
                        board[firstFile.ordinal()][firstRank - 1] = null;
                        if (board[newFile.ordinal()][newRank - 1] != null) {
                            board[newFile.ordinal()][newRank - 1].file = newFile; // Update file and rank
                            board[newFile.ordinal()][newRank - 1].rank = newRank;
                        }

                        if (!isCheck(kingPlayer)) {

                            board[firstFile.ordinal()][firstRank - 1] = board[newFile.ordinal()][newRank - 1];
                            board[newFile.ordinal()][newRank - 1] = capturedPiece;
                            board[firstFile.ordinal()][firstRank - 1].file = firstFile; // Restore file and rank
                            board[firstFile.ordinal()][firstRank - 1].rank = firstRank;

                            if (capturedPiece != null) {
                                capturedPiece.file = newFile; // Restore file and rank
                                capturedPiece.rank = newRank;
                            }
                            return false;
                        }

                        // Restore the board to its original state
                        board[firstFile.ordinal()][firstRank - 1] = board[newFile.ordinal()][newRank - 1];
                        board[newFile.ordinal()][newRank - 1] = capturedPiece;
                        board[firstFile.ordinal()][firstRank - 1].file = firstFile; // Restore file and rank
                        board[firstFile.ordinal()][firstRank - 1].rank = firstRank;

                        if (capturedPiece != null) {
                            capturedPiece.file = newFile; // Restore file and rank
                            capturedPiece.rank = newRank;
                        }
                    }
                }
            }
        }

        return true;
    }
}
