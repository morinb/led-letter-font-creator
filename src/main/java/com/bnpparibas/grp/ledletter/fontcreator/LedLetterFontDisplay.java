package com.bnpparibas.grp.ledletter.fontcreator;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

/**
 * @author morinb.
 */
public class LedLetterFontDisplay extends JComponent implements LedLetterFontDisplayModelListener {

   private final int c;
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

   public void addLedLetterChangedListener(LedLetterChangedListener l) {
      ell.add(LedLetterChangedListener.class, l);
   }

   public void removeLedLetterChangedListener(LedLetterChangedListener l) {
      ell.remove(LedLetterChangedListener.class, l);
   }

   public void fireLedLetterChanged(int c, Image image) {
      for (LedLetterChangedListener l : ell.getListeners(LedLetterChangedListener.class)) {
         l.letterChanged(c, image);
      }
   }

   private Cell[][] cells;

   protected Cell[][] getCells() {
      return cells;
   }

   public int getTranslateX() {
      return translateX;
   }

   public int getTranslateY() {
      return translateY;
   }

   private boolean selectCellAtMouse(int computedX, int computedY, boolean select) {
      for (Cell[] rects : cells) {
         for (Cell r : rects) {
            if (r.contains(computedX, computedY)) {
               r.setSelected(select);
               LedLetterFontDisplay.this.repaint();
               return true;
            }
         }
      }
      return false;
   }

   public LedLetterFontDisplay(int c, LedLetterFontDisplayModel model) {
      super();
      this.c = c;

      this.setBackground(transparent);
      this.setForeground(Color.black);
      setModel(model);
      LedLetterFontDisplayMouseListener listener = new LedLetterFontDisplayMouseListener(this);
      this.addMouseWheelListener(listener);
      this.addMouseMotionListener(listener);
      this.addMouseListener(listener);

   }

   private Color transparent = new Color(0.0f, 0.0f, 0.0f, 0.0f);

   protected Image buildImage() {
      BufferedImage image = new BufferedImage(model.getGridDimension().width, model.getGridDimension().height, BufferedImage.TYPE_INT_ARGB);
      final Graphics2D g2 = image.createGraphics();
      g2.setPaint(transparent);
      g2.fillRect(0, 0, image.getWidth() + 1, image.getHeight() + 1);
      g2.setPaint(Color.black);
      for (int row = 0; row < verticalCellNumber; row++) {
         for (int col = 0; col < horizontalCellNumber; col++) {
            if (cells[row][col].isSelected()) {
               g2.drawLine(col, row, col, row);
            }
         }
      }
      g2.dispose();
      return image;
   }

   public void fireNewlyCreated() {
      fireLedLetterChanged(c, buildImage());
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
      BufferedImage image = new BufferedImage(gridWidth, gridHeight, BufferedImage.TYPE_INT_ARGB);

      Graphics2D g2 = image.createGraphics();

      // Fill background
      g2.setPaint(getBackground());
      g2.fillRect(0, 0, getWidth(), getHeight());

      Graphics2D clone = (Graphics2D) g2.create();
      //region Draw grid 
      {

         g2.setPaint(getForeground());
         g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));

         for (int row = 0; row < verticalCellNumber; row++) {
            for (int col = 0; col < horizontalCellNumber; col++) {
               Cell c = cells[row][col];

               if (c.isSelected()) {
                  g2.setPaint(getForeground());
               } else {
                  g2.setPaint(getBackground());
               }

               g2.fillRect(c.x + thickness / 2, c.y + thickness / 2, c.width - thickness / 2 + 1, c.height - thickness / 2 + 1);

               g2.setPaint(Color.darkGray);
               g2.drawRect(c.x + thickness / 2, c.y + thickness / 2, c.width - thickness / 2 + 1, c.height - thickness / 2 + 1);
            }
         }

         g2.setPaint(getForeground());

         Graphics2D gg2 = (Graphics2D) g;
         if (gridWidth < getWidth()) {
            translateX = (getWidth() - gridWidth * model.getZoomLevel()) / 2;

         }
         if (gridHeight < getHeight()) {
            translateY = (getHeight() - gridHeight * model.getZoomLevel()) / 2;
         }
         if (translateX != 0 || translateY != 0) {
            gg2.translate(translateX, translateY);
         }

         gg2.scale(model.getZoomLevel(), model.getZoomLevel());

         gg2.drawImage(image, 0, 0, gridWidth + 1, gridHeight + 1, null);
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

   public int getC() {
      return c;
   }
}
