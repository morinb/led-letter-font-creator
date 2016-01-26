package com.bnpparibas.grp.ledletter.fontcreator;

import javax.swing.SwingUtilities;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * @author morinb.
 */
public class LedLetterFontDisplayMouseListener implements MouseMotionListener, MouseListener, MouseWheelListener {
   private int pressedButton;

   LedLetterFontDisplay display;

   public LedLetterFontDisplayMouseListener(LedLetterFontDisplay display) {
      this.display = display;
   }

   @Override
   public void mouseClicked(MouseEvent e) {
      pressedButton = e.getButton();
      Point p = e.getLocationOnScreen();
      SwingUtilities.convertPointFromScreen(p, display);
      final int computedX = ((int) (p.getX() - display.getTranslateX())) / display.getModel().getZoomLevel();
      final int computedY = ((int) (p.getY() - display.getTranslateY())) / display.getModel().getZoomLevel();
      selectCellAtMouse(computedX, computedY, MouseEvent.BUTTON1 == pressedButton);
   }

   @Override
   public void mousePressed(MouseEvent e) {
      pressedButton = e.getButton();
   }

   @Override
   public void mouseReleased(MouseEvent e) {
      pressedButton = -1;
      new Thread(() -> {
         display.fireLedLetterChanged(display.getC(), display.buildImage());
      }, "Refresh image " + (char) display.getC()).start();

   }

   @Override
   public void mouseEntered(MouseEvent e) {
   }

   @Override
   public void mouseExited(MouseEvent e) {
   }

   @Override
   public void mouseDragged(MouseEvent e) {
      Point p = e.getLocationOnScreen();
      SwingUtilities.convertPointFromScreen(p, display);
      final int computedX = ((int) (p.getX() - display.getTranslateX())) / display.getModel().getZoomLevel();
      final int computedY = ((int) (p.getY() - display.getTranslateY())) / display.getModel().getZoomLevel();

      selectCellAtMouse(computedX, computedY, MouseEvent.BUTTON1 == pressedButton);
   }

   @Override
   public void mouseMoved(MouseEvent e) {
   }

   @Override
   public void mouseWheelMoved(MouseWheelEvent e) {
      if (e.isControlDown()) {
         // -1 for wheel up, 1 for wheel down.
         // We want zoom in on wheel up, zoom out on wheel down, so we inverse the wheel rotation value. 
         int newZoom = display.getModel().getZoomLevel() - e.getWheelRotation();
         if (newZoom < 1) {
            newZoom = 1;
         }
         if (newZoom > 16) {
            newZoom = 16;
         }
         display.getModel().setZoomLevel(newZoom);
      }
   }

   private boolean selectCellAtMouse(int computedX, int computedY, boolean select) {
      for (Cell[] rects : display.getCells()) {
         for (Cell r : rects) {
            if (r.contains(computedX, computedY)) {
               r.setSelected(select);
               display.repaint();
               return true;
            }
         }
      }
      return false;
   }
}
