package chess.pgn;

import chess.engine.GUI.GameHistoryPanel;


public class PgnUtility {
    PgnUtility() {
        throw new IllegalStateException("Utility class");
    }

    public String getFenFromPgn(String pgn) { return null; }

    public static String getPgnFromGameHistoryPanel(GameHistoryPanel gameHistoryPanel) {
        GameHistoryPanel.DataModel model = (GameHistoryPanel.DataModel) gameHistoryPanel.getModel();
        StringBuilder pgn = new StringBuilder();
        for (int i = 0; i < model.getRowCount(); i++) {
            pgn.append(i+1).append(". ").append(model.getValueAt(i, 0)).append(" ").append(model.getValueAt(i, 1)).append(" ");
        }
        return pgn.toString();
    }
}
