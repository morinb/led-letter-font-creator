package com.bnpparibas.grp.ledletter.fontcreator;

import com.bnpparibas.grp.ledletter.fontcreator.status.StatusDirtyChangedListener;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author morinb.
 */
public class LedLetterFontDisplay extends JComponent implements LedLetterFontDisplayModelListener {

   private LedLetterFontDisplayModel model;
   //region shortcut value issued from the model.
   private int thickness;
   private int cellWidth;
   private int cellHeight;
   private int horizontalCellNumber;
   private int verticalCellNumber;
   private int gridWidth;
   private int gridHeight;

   private int translateX = 0;
   private int translateY = 0;
   //endregion

   private static final EventListenerList ell = new EventListenerList();

   public void addStatusDirtyChangedListener(StatusDirtyChangedListener l) {
      ell.add(StatusDirtyChangedListener.class, l);
   }
   public void removeStatusDirtyChangedListener(StatusDirtyChangedListener l) {
      ell.remove(StatusDirtyChangedListener.class, l);
   }

   public void fireStatusDirtyChanged(boolean dirty) {
      for (StatusDirtyChangedListener l : ell.getListeners(StatusDirtyChangedListener.class)) {
         l.dirtyChanged(dirty);
      }
   }

   private Cell[][] cells;

   public LedLetterFontDisplay(LedLetterFontDisplayModel model) {
      super();
      this.setBackground(Color.white);
      this.setForeground(Color.black);
      setModel(model);

      this.addMouseWheelListener(e -> {
         if (e.isControlDown()) {
            // -1 for wheel up, 1 for wheel down.
            // We want zoom in on wheel up, zoom out on wheel down, so we inverse the wheel rotation value. 
            int newZoom = model.getZoomLevel() - e.getWheelRotation();
            if (newZoom < 1) {
               newZoom = 1;
            }
            if (newZoom > 16) {
               newZoom = 16;
            }
            model.setZoomLevel(newZoom);

         }
      });


      this.addMouseListener(new MouseListener() {
         @Override
         public void mouseClicked(MouseEvent e) {
            Point p = e.getLocationOnScreen();
            SwingUtilities.convertPointFromScreen(p, LedLetterFontDisplay.this);
            final int computedX = ((int) (p.getX() - translateX)) / model.getZoomLevel();
            final int computedY = ((int) (p.getY() - translateY)) / model.getZoomLevel();

            for (Cell[] rects : cells) {
               for (Cell r : rects) {
                  if (r.contains(computedX, computedY)) {
                     r.setSelected(!r.isSelected());
                     fireStatusDirtyChanged(true);
                     LedLetterFontDisplay.this.repaint();
                     return;
                  }
               }
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

   }


   public LedLetterFontDisplayModel getModel() {
      return model;
   }

   public void setModel(LedLetterFontDisplayModel model) {
      if (this.model != null) {
         this.model.removeLedLetterFontDisplayModelListener(this);
      }
      this.model = model;
      this.model.addLedLetterFontDisplayModelListener(this);

      // Compute preferred sizes.
      thickness = model.getGridThickness();
      cellWidth = model.getCellDimension().width;
      cellHeight = model.getCellDimension().height;
      horizontalCellNumber = model.getGridDimension().width;
      verticalCellNumber = model.getGridDimension().height;

      gridWidth = horizontalCellNumber * cellWidth + (horizontalCellNumber + 1) * thickness;
      gridHeight = verticalCellNumber * cellHeight + (verticalCellNumber + 1) * thickness;

      setPreferredSize(new Dimension(gridWidth, gridHeight));
      recomputeCells();
      repaint();
   }

   private void recomputeCells() {
      cells = new Cell[verticalCellNumber][];
      for (int row = 0; row < verticalCellNumber; row++) {
         cells[row] = new Cell[horizontalCellNumber];
         for (int col = 0; col < horizontalCellNumber; col++) {
            int y = row * (thickness + cellWidth) + thickness;
            int x = col * (thickness + cellHeight) + thickness;
            //g2.drawLine(0, y, gridWidth - thickness , y);
            final int semiThickness = thickness > 1 ? thickness / 2 : 0;

            cells[row][col] = new Cell(
                  x
                  , y
                  , cellWidth - thickness
                  , cellHeight - thickness
                  , row, col, false
            );
         }
      }
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      // Fill background
      g2.setPaint(getBackground());
      g2.fillRect(0, 0, getWidth(), getHeight());

      Graphics2D clone = (Graphics2D) g2.create();
      //region Draw grid 
      {


         if (gridWidth < getWidth()) {
            translateX = (getWidth() - gridWidth * model.getZoomLevel()) / 2;

         }
         if (gridHeight < getHeight()) {
            translateY = (getHeight() - gridHeight * model.getZoomLevel()) / 2;
         }
         if (translateX != 0 || translateY != 0) {
            g2.translate(translateX, translateY);
         }

         g2.scale(model.getZoomLevel(), model.getZoomLevel());


         g2.setPaint(getForeground());
         g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));

         for (int row = 0; row < verticalCellNumber; row++) {
            for (int col = 0; col < horizontalCellNumber; col++) {
               Cell c = cells[row][col];
               g2.setPaint(getForeground());
               g2.drawRect(c.x + thickness / 2, c.y + thickness / 2, c.width - thickness / 2 + 1, c.height - thickness / 2 + 1);
               if (c.isSelected()) {
                  g2.setPaint(getForeground());
               } else {
                  g2.setPaint(getBackground());
               }

               g2.fillRect(c.x + thickness / 2, c.y + thickness / 2, c.width - thickness / 2 + 1, c.height - thickness / 2 + 1);
            }
         }

         g2.setPaint(getForeground());
         g2.drawRect(0, 0, gridWidth, gridHeight);

         // debug
      }
      //endregion
   }

   //region LedLetterFontDisplayModelListener implementation

   @Override
   public void cellDimensionChanged(Dimension oldDimension, Dimension newDimension) {
      if (oldDimension.equals(newDimension)) {
         return;
      }
      this.cellWidth = newDimension.width;
      this.cellHeight = newDimension.height;
      recomputeCells();
      repaint();
   }

   @Override
   public void gridThicknessChanged(int oldthickness, int newThickness) {
      if (oldthickness == newThickness) {
         return;
      }
      this.thickness = newThickness;
      recomputeCells();
      repaint();
   }

   @Override
   public void gridDimensionChanged(Dimension oldDimension, Dimension newDimension) {
      if (oldDimension.equals(newDimension)) {
         return;
      }
      this.gridWidth = newDimension.width;
      this.gridHeight = newDimension.height;
      recomputeCells();
      repaint();
   }

   @Override
   public void zoomLevelChanged(int oldZoomLevel, int newZoomLevel) {

      setPreferredSize(new Dimension(this.gridWidth * newZoomLevel + newZoomLevel * thickness, this.gridHeight * newZoomLevel + newZoomLevel * thickness));
      repaint();
   }
   //endregion

   public boolean[][] getValues() {
      boolean[][] res = new boolean[verticalCellNumber][];
      for (int row = 0; row < verticalCellNumber; row++) {
         res[row] = new boolean[horizontalCellNumber];
         for (int col = 0; col < horizontalCellNumber; col++) {
            res[row][col] = cells[row][col].isSelected();
         }
      }
      return res;
   }
   
   public void setValues(boolean[][] values) {
      for (int row = 0; row < verticalCellNumber; row++) {
         for (int col = 0; col < horizontalCellNumber; col++) {
            cells[row][col].setSelected(values[row][col]);
         }
      }
   }
}
