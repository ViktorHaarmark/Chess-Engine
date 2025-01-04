package chess.engine.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import chess.engine.GUI.Table.MoveLog;
import chess.engine.board.Move;
import chess.engine.pieces.Piece;

public class TakenPiecePanel extends JPanel {

    private final JPanel northPanel;
    private final JPanel southPanel;

    private static final Color PANEL_COLOR = Color.DARK_GRAY;
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(80, 80);
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

    public TakenPiecePanel() {
        super(new BorderLayout());
        this.setBackground(PANEL_COLOR);
        this.setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8, 2));
        this.southPanel = new JPanel(new GridLayout(8, 2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        this.add(this.northPanel, BorderLayout.NORTH);
        this.add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIMENSION);
    }

    public void redo(final MoveLog moveLog) {

        this.southPanel.removeAll();
        this.northPanel.removeAll();

        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final List<Piece> blackTakenPieces = new ArrayList<>();

        for(final Move move : moveLog.getMoves()) {
            if(move.isCapture()) {
                final Piece takenPiece = move.getCapturedPiece();
                if(takenPiece.getPieceColor().isWhite()) {
                    whiteTakenPieces.add(takenPiece);
                }
                else if (takenPiece.getPieceColor().isBlack()) {
                    blackTakenPieces.add(takenPiece);
                } else {
                    throw new RuntimeException("Show not reach here, piece is either white or black");
                }
            }
        }

        Collections.sort(whiteTakenPieces, (o1, o2) -> Integer.compare(o1.getPieceValue(), o2.getPieceValue()));
        Collections.sort(blackTakenPieces, (o1, o2) -> Integer.compare(o1.getPieceValue(), o2.getPieceValue()));

        addTakenPieces(whiteTakenPieces, southPanel);
        addTakenPieces(blackTakenPieces, northPanel);
        validate();
    }

    private void addTakenPieces(final List<Piece> pieceList, final JPanel Panel) {
        for (final Piece takenPiece : pieceList) {
            String pieceIconPath = "/art/pieces/";
            String fileString = pieceIconPath + takenPiece.getPieceColor().toString() + "_" + takenPiece.toString() + ".png";
                
            try {
                InputStream resourceStream = getClass().getResourceAsStream(fileString);
                if (resourceStream == null) {
                    throw new IOException("Resource not found: " + fileString);
                }
                final BufferedImage image = ImageIO.read(resourceStream);
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel();
                Panel.add(imageLabel);
                    /*int newWidth = 100;
                    int newHeight = 100;
                    Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH); */
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
