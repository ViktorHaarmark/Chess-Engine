package chess.engine.board;


public record MoveTransition(Board fromBoard, Board toBoard, Move move, MoveStatus moveStatus) {

    public Move getMove() {
        return this.move;
    }

    public Board getToBoard() {
        return this.toBoard;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }
}
