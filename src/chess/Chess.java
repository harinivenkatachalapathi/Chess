package chess;

import java.util.ArrayList;

import chess.ReturnPiece.PieceType;

class ReturnPiece {
	static enum PieceType {
		WP, WR, WN, WB, WQ, WK,
		BP, BR, BN, BB, BK, BQ
	};

	static enum PieceFile {
		a, b, c, d, e, f, g, h
	};

	PieceType pieceType;
	PieceFile pieceFile;
	int pieceRank; // 1..8

	public String toString() {
		return "" + pieceFile + pieceRank + ":" + pieceType;
	}

	public boolean equals(Object other) {
		if (other == null || !(other instanceof ReturnPiece)) {
			return false;
		}
		ReturnPiece otherPiece = (ReturnPiece) other;
		return pieceType == otherPiece.pieceType &&
				pieceFile == otherPiece.pieceFile &&
				pieceRank == otherPiece.pieceRank;
	}
}

class ReturnPlay {
	enum Message {
		ILLEGAL_MOVE, DRAW,
		RESIGN_BLACK_WINS, RESIGN_WHITE_WINS,
		CHECK, CHECKMATE_BLACK_WINS, CHECKMATE_WHITE_WINS,
		STALEMATE
	};

	ArrayList<ReturnPiece> piecesOnBoard;
	Message message;
}

public class Chess {

	enum Player {
		white, black
	}

	/**
	 * Plays the next move for whichever player has the turn.
	 * 
	 * @param move String for next move, e.g. "a2 a3"
	 * 
	 * @return A ReturnPlay instance that contains the result of the move.
	 *         See the section "The Chess class" in the assignment description for
	 *         details of
	 *         the contents of the returned ReturnPlay instance.
	 */
	public static ReturnPlay play(String move) {

		/* FILL IN THIS METHOD */
		ArrayList<ReturnPiece> pieces = new ArrayList<ReturnPiece>();
		Board.move(move);

		// LOGIC TO STORE EVERY PIECE ON BOARD AS A RETURNPIECE
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (Board.board[i][j] != null) {
					ReturnPiece temp = new ReturnPiece(); // Create a new ReturnPiece object
					temp.pieceFile = ReturnPiece.PieceFile.values()[i];
					temp.pieceRank = j+1;
					if (Board.board[i][j].player == Player.white) {
						if (Board.board[i][j] instanceof Pawn) {
							temp.pieceType = PieceType.WP;
						} else if (Board.board[i][j] instanceof Rook) {
							temp.pieceType = PieceType.WR;
						} else if (Board.board[i][j] instanceof Bishop) {
							temp.pieceType = PieceType.WB;
						} else if (Board.board[i][j] instanceof Knight) {
							temp.pieceType = PieceType.WN;
						} else if (Board.board[i][j] instanceof Queen) {
							temp.pieceType = PieceType.WQ;
						} else if (Board.board[i][j] instanceof King) {
							temp.pieceType = PieceType.WK;
						}
					} else {
						if (Board.board[i][j] instanceof Pawn) {
							temp.pieceType = PieceType.BP;
						} else if (Board.board[i][j] instanceof Rook) {
							temp.pieceType = PieceType.BR;
						} else if (Board.board[i][j] instanceof Bishop) {
							temp.pieceType = PieceType.BB;
						} else if (Board.board[i][j] instanceof Knight) {
							temp.pieceType = PieceType.BN;
						} else if (Board.board[i][j] instanceof Queen) {
							temp.pieceType = PieceType.BQ;
						} else if (Board.board[i][j] instanceof King) {
							temp.pieceType = PieceType.BK;
						}
					}
					pieces.add(temp);
				}
			}
		}

		/* FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY */
		/* WHEN YOU FILL IN THIS METHOD, YOU NEED TO RETURN A ReturnPlay OBJECT */
		ReturnPlay end = new ReturnPlay();
		end.piecesOnBoard = pieces;
		end.message = null;
		return end;
	}

	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {
		/* FILL IN THIS METHOD */
		Board board = new Board();

		//
	}
}
