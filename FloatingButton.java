import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.SwingUtilities;
import javax.swing.plaf.LayerUI;

public class FloatingButton extends LayerUI<Component>{

    private Image image;
    private Shape shape;
    private int size;

    public FloatingButton() {
    }

    public FloatingButton(int size) {
        this.size = size;
    }

    public FloatingButton(Image image) {
        this.size = 30;
        this.image = image;
    }

    public FloatingButton(Image image, int size) {
        this.image = image;
        this.size = size;
    }

    @Override
    public void installUI(JComponent c) {
        if(c instanceof JLayer) {
            ((JLayer)c).setLayerEventMask(AWTEvent.MOUSE_EVENT_MASK);
        }
    }

    @Override
    public void uninstallUI(JComponent c) {
        if(c instanceof JLayer) {
            ((JLayer)c).setLayerEventMask(0);
        }
    }

    @Override
    public void paint(Graphics g, JComponent c) {    // This is a paint function that runs when a LayerUi object is initialised
        super.paint(g, c);                          //
        Graphics2D graphic = (Graphics2D)g.create();
        graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int x = c.getWidth() - size - 35;
        int y = c.getHeight() - size - 15;
        shape = new Ellipse2D.Double(x, y, size, size);
        graphic.setColor(Color.LIGHT_GRAY);
        graphic.fill(shape);

        if (image != null) {
            int iconSize = image.getWidth(c);
            int iconX = (size - iconSize) / 2;
            int iconY = (size - iconSize) / 2;

            graphic.drawImage(image, x + iconX, y + iconY, null);
        }
        graphic.dispose();
        c.repaint();
    }

    @Override
    protected void processMouseEvent(MouseEvent e, JLayer<? extends Component> l) {
        if(shape == null) return;

        if(!SwingUtilities.isLeftMouseButton(e)) return;

        Point mousepoint = SwingUtilities.convertMouseEvent(e.getComponent(), e, l.getView()).getPoint();
        if(shape.contains(mousepoint)){
            if(e.getID() == MouseEvent.MOUSE_CLICKED) {
                l.repaint(shape.getBounds());
                ButtonClicked();
            }
        }
    }
    
    public void ButtonClicked() {} //Custom Body to be implemented when initialised as object
}
