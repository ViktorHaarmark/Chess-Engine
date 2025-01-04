package chess.engine.GUI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mouse extends MouseAdapter{

    public int x, y;
    public boolean pressed;

    @Override
    public void mousePressed(final MouseEvent e) {
        
        pressed = true;
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        pressed = false;
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        x = e.getX();
        y = e.getY();

    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

}
