package chess.engine.GUI;

import chess.engine.Players.PlayerType;
import chess.engine.Players.ai.AlphaBeta;
import chess.engine.Players.ai.MoveStrategy;
import chess.engine.board.*;
import chess.engine.pieces.Piece;
import chess.pgn.PgnUtility;
import lombok.Getter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.*;

public class Table extends Observable {

    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private final GameHistoryPanel gameHistoryPanel;
    private final TakenPiecePanel takenPiecePanel;
    private final MoveLog moveLog;
    private final GameSetup gameSetup;


    private Board chessBoard;
    private BoardDirection boardDirection;
    private boolean showLegalMoves;
    private ChessMove computerMove;

    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;

    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(1100, 800);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(800, 800);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);

    private static final Table INSTANCE = new Table();

    private Table() {
        this.gameFrame = new JFrame("Chess");
        this.gameFrame.setLayout(new BorderLayout());
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //        window.setResizable(false);

        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.chessBoard = BoardSetup.createStandardBoard();
        this.boardDirection = BoardDirection.NORMAL;
        this.showLegalMoves = true;
        this.computerMove = null;
        this.boardPanel = new BoardPanel();
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecePanel = new TakenPiecePanel();
        this.moveLog = new MoveLog();
        this.addObserver(new TableGameAIWatcher());
        this.gameSetup = new GameSetup(this.gameFrame, true);
        this.gameFrame.add(this.takenPiecePanel, BorderLayout.WEST);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);


        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameFrame.setVisible(true);
    }

    public static Table get() {
        return INSTANCE;
    }

    public void show() {
        Table.get().getMoveLog().clear();
        Table.get().getGameHistoryPanel().redo(chessBoard, Table.get().getMoveLog());
        Table.get().getTakenPiecePanel().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(chessBoard);
    }

    private GameSetup getGameSetup() {
        return this.gameSetup;
    }

    private Board getGameBoard() {
        return this.chessBoard;
    }

    private BoardPanel getBoardPanel() {
        return this.boardPanel;
    }

    private GameHistoryPanel getGameHistoryPanel() {
        return this.gameHistoryPanel;
    }

    private TakenPiecePanel getTakenPiecePanel() {
        return this.takenPiecePanel;
    }

    private MoveLog getMoveLog() {
        return this.moveLog;
    }

    public void updateGameBoard(final Board board) {
        this.chessBoard = board;
    }

    public void updateComputerMove(final Move move) {
        this.computerMove = move;
    }

    private void moveMadeUpdate() {
        setChanged();
        notifyObservers(PlayerType.HUMAN);
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferenceMenu());
        tableMenuBar.add(createOptionsMenu());

        return tableMenuBar;

    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");

        final JMenuItem openPGN = new JMenuItem("Load PGN file");
        openPGN.addActionListener(e -> System.out.println("Open up that pgn file"));
        fileMenu.add(openPGN);

        fileMenu.addSeparator();

        final JMenuItem savePGN = new JMenuItem("Save PGN file");
        savePGN.addActionListener(e -> System.out.println(PgnUtility.getPgnFromGameHistoryPanel(this.getGameHistoryPanel())));
        fileMenu.add(savePGN);

        return fileMenu;
    }

    private JMenu createPreferenceMenu() {
        final JMenu preferenceMenu = new JMenu("Preference");

        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip board");
        flipBoardMenuItem.addActionListener(e -> {
            boardDirection = boardDirection.opposite();
            boardPanel.drawBoard(chessBoard);
        });
        preferenceMenu.add(flipBoardMenuItem);

        preferenceMenu.addSeparator();

        final JCheckBoxMenuItem highlightLegalMovesCheckBox = new JCheckBoxMenuItem("Highlight legal moves", true);

        highlightLegalMovesCheckBox.addActionListener(e -> showLegalMoves = highlightLegalMovesCheckBox.isSelected());
        preferenceMenu.add(highlightLegalMovesCheckBox);

        return preferenceMenu;
    }

    private JMenu createOptionsMenu() {
        final JMenu optionsMenu = new JMenu("Options");

        final JMenuItem setupGameMenuItem = new JMenuItem("Setup Game");

        setupGameMenuItem.addActionListener(e -> {
            Table.get().getGameSetup().promptUser();
            Table.get().setupUpdate(Table.get().getGameSetup());
        });
        optionsMenu.add(setupGameMenuItem);

        optionsMenu.addSeparator();

        final JMenuItem takebackMoveMenuItem = new JMenuItem("Takeback Move");
        takebackMoveMenuItem.addActionListener(e -> {
            if (this.moveLog.size() == 0) {
                System.out.println("This is the starting board");
                return;
            }

            Move lastMove = this.moveLog.removeMove(this.moveLog.size() - 1);
            this.RedoMove(lastMove);

        });

        optionsMenu.add(takebackMoveMenuItem);

        optionsMenu.addSeparator();

        final JMenuItem NewGameMenuItem = new JMenuItem("New Game");
        NewGameMenuItem.addActionListener(e -> {
            while (this.moveLog.size() > 0) {
                Move lastMove = this.moveLog.removeMove(this.moveLog.size() - 1);
                this.RedoMove(lastMove);
            }
        });
        optionsMenu.add(NewGameMenuItem);

        return optionsMenu;
    }

    private void RedoMove(Move lastMove) {
        this.updateGameBoard(lastMove.getBoard());
        this.getGameHistoryPanel().redo(this.chessBoard, this.getMoveLog());
        this.getTakenPiecePanel().redo(this.getMoveLog());
        this.getBoardPanel().drawBoard(this.chessBoard);
        this.moveMadeUpdate();

    }

    private void setupUpdate(final GameSetup gameSetup) {
        setChanged();
        notifyObservers(gameSetup);
    }

    private static class TableGameAIWatcher implements Observer {

        @Override
        public void update(Observable o, Object arg) {
            if (Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().getCurrentPlayer()) &&
                    !Table.get().chessBoard.getCurrentPlayer().isInCheckMate() &&
                    !Table.get().chessBoard.getCurrentPlayer().isInStaleMate()) {
                final AIThinkTank thinkTank = new AIThinkTank();
                thinkTank.execute();

            }
            if (Table.get().chessBoard.getCurrentPlayer().isInCheckMate()) {
                System.out.println("Game Over, " + Table.get().chessBoard.getCurrentPlayer() + " is in checkmate!");
            }
            if (Table.get().chessBoard.getCurrentPlayer().isInStaleMate()) {
                System.out.println("Game Over, " + Table.get().chessBoard.getCurrentPlayer() + " is in stalemate!");
            }
        }

    }

    private static class AIThinkTank extends SwingWorker<Move, String> {

        private AIThinkTank() {

        }

        @Override
        protected Move doInBackground() throws RuntimeException {
            final MoveStrategy engine = new AlphaBeta(Table.get().getGameSetup().getSearchDepth());
            final Move bestMove = engine.execute(Table.get().getGameBoard());
            return bestMove;
        }

        @Override
        public void done() {
            try {
                final Move bestMove = get();
                Table.get().updateComputerMove(bestMove);
                Table.get().updateGameBoard(Table.get().getGameBoard().getCurrentPlayer().makeMove(bestMove).toBoard());
                Table.get().getMoveLog().addMove(bestMove);
                Table.get().getGameHistoryPanel().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
                Table.get().getTakenPiecePanel().redo(Table.get().getMoveLog());
                Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
                Table.get().moveMadeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);

            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard(final Board board) {
            removeAll();
            for (final TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }

    }

    @Getter
    public static class MoveLog {

        private final List<Move> moves;

        MoveLog() {

            this.moves = new ArrayList<>();
        }

        public void addMove(final Move move) {
            this.moves.add(move);
        }

        public int size() {
            return this.moves.size();
        }

        public void clear() {
            this.moves.clear();
        }

        private Move removeMove(int index) {
            return this.moves.remove(index);
        }


    }

    class TilePanel extends JPanel {
        private final Color LightTileColor = new Color(210, 165, 125);
        private final Color darkTileColor = new Color(175, 115, 70);
        private final int tileId;

        TilePanel(final BoardPanel boardPanel,
                  final int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);

            addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    } else if (SwingUtilities.isLeftMouseButton(e)) {
                        if (sourceTile == null) { //first click
                            sourceTile = chessBoard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();
                            if (humanMovedPiece == null) {
                                sourceTile = null;
                            }
                        } else { //second click
                            destinationTile = chessBoard.getTile(tileId);
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
                            final MoveTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
                            if (transition.moveStatus().isDone()) {
                                chessBoard = transition.toBoard();
                                moveLog.addMove(move);
                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(() -> {
                            gameHistoryPanel.redo(chessBoard, moveLog);
                            takenPiecePanel.redo(moveLog);

                            if (gameSetup.isAIPlayer(chessBoard.getCurrentPlayer())) {
                                Table.get().moveMadeUpdate();
                            }
                            boardPanel.drawBoard(chessBoard);

                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }

            });

            validate();

        }

        public void drawTile(final Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highlightLegalMoves(board);
            validate();
            repaint();
        }

        private void highlightLegalMoves(final Board board) {
            if (showLegalMoves) {
                for (final Move move : pieceLegalMoves(board)) {
                    if (move.getDestinationCoordinate() == this.tileId) {
                        String fileString = "/art/misc/blue_dot.png";
                        try {
                            InputStream resourceStream = getClass().getResourceAsStream(fileString);
                            if (resourceStream == null) {
                                throw new IOException("Resource not found: " + fileString);
                            }
                            final BufferedImage image = ImageIO.read(resourceStream);
                            int newWidth = 10;
                            int newHeight = 10;
                            Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                            add(new JLabel(new ImageIcon(scaledImage)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves(final Board board) {
            Collection<Move> pieceLegalMoves = new ArrayList<>();
            if (humanMovedPiece != null && humanMovedPiece.getPieceColor() == board.getCurrentPlayer().getColor()) {
                Collection<Move> legalMoveList = board.getCurrentPlayer().getPossibleMoves();
                for (Move move : legalMoveList) {
                    if (move.getMovedPiece().equals(humanMovedPiece)) {
                        pieceLegalMoves.add(move);
                    }
                }
            }
            return pieceLegalMoves;
        }

        private void assignTileColor() {
            setBackground((this.tileId / 8 + this.tileId % 8) % 2 == 0 ? LightTileColor : darkTileColor);
        }


        private void assignTilePieceIcon(final Board board) {
            this.removeAll();
            if (board.getTile(this.tileId).isTileOccupied()) {
                String pieceIconPath = "/art/pieces/";
                String fileString = pieceIconPath + board.getTile(this.tileId).getPiece().getPieceColor().toString()
                        + "_" + board.getTile(this.tileId).getPiece().toString() + ".png";

                try {
                    InputStream resourceStream = getClass().getResourceAsStream(fileString);
                    if (resourceStream == null) {
                        throw new IOException("Resource not found: " + fileString);
                    }
                    final BufferedImage image = ImageIO.read(resourceStream);
                    int newWidth = 100;
                    int newHeight = 100;
                    Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                    add(new JLabel(new ImageIcon(scaledImage)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }


}
