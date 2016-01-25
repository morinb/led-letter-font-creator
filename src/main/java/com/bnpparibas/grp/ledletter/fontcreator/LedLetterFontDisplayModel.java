package com.bnpparibas.grp.ledletter.fontcreator;

import javax.swing.event.EventListenerList;
import java.awt.Dimension;

/**
 * @author morinb.
 */
public class LedLetterFontDisplayModel {
   private Dimension cellDimension;
   private int gridThickness;
   private Dimension gridDimension;
   private int zoomLevel = 1;

   public LedLetterFontDisplayModel() {
      this(new Dimension(7, 7));
   }

   public LedLetterFontDisplayModel(Dimension gridDimension) {
      this(new Dimension(5, 5), gridDimension);
   }

   public LedLetterFontDisplayModel(Dimension cellDimension, Dimension gridDimension) {
      this(cellDimension, gridDimension, 1);
   }

   public LedLetterFontDisplayModel(Dimension cellDimension, Dimension gridDimension, int gridThickness) {
      this.cellDimension = cellDimension;
      this.gridDimension = gridDimension;
      this.gridThickness = gridThickness;
   }

   public Dimension getCellDimension() {
      return cellDimension;
   }

   public void setCellDimension(Dimension cellDimension) {
      final Dimension old = this.cellDimension;
      this.cellDimension = cellDimension;
      fireCellDimensionChanged(old, this.cellDimension);
   }

   public Dimension getGridDimension() {
      return gridDimension;
   }

   public void setGridDimension(Dimension gridDimension) {
      final Dimension old = this.gridDimension;
      this.gridDimension = gridDimension;
      fireGridDimensionChanged(old, this.gridDimension);
   }

   public int getGridThickness() {
      return gridThickness;
   }

   public void setGridThickness(int gridThickness) {
      final int old = this.gridThickness;
      this.gridThickness = gridThickness;
      fireGridThicknessChanged(old, this.gridThickness);
   }

   public int getZoomLevel() {
      return zoomLevel;
   }

   public void setZoomLevel(int zoomLevel) {
      final int old = this.zoomLevel;
      this.zoomLevel = zoomLevel;
      fireZoomLevelChanged(old, this.zoomLevel);
   }

   //region Eventlisteners
   private final EventListenerList eel = new EventListenerList();

   public void addLedLetterFontDisplayModelListener(LedLetterFontDisplayModelListener l) {
      eel.add(LedLetterFontDisplayModelListener.class, l);
   }

   public void removeLedLetterFontDisplayModelListener(LedLetterFontDisplayModelListener l) {
      eel.remove(LedLetterFontDisplayModelListener.class, l);
   }

   protected void fireCellDimensionChanged(Dimension oldDimension, Dimension newDimension) {
      for (LedLetterFontDisplayModelListener l : eel.getListeners(LedLetterFontDisplayModelListener.class)) {
         l.cellDimensionChanged(oldDimension, newDimension);
      }
   }

   protected void fireGridThicknessChanged(int oldthickness, int newThickness) {
      for (LedLetterFontDisplayModelListener l : eel.getListeners(LedLetterFontDisplayModelListener.class)) {
         l.gridThicknessChanged(oldthickness, newThickness);
      }
   }

   protected void fireGridDimensionChanged(Dimension oldDimension, Dimension newDimension) {
      for (LedLetterFontDisplayModelListener l : eel.getListeners(LedLetterFontDisplayModelListener.class)) {
         l.gridDimensionChanged(oldDimension, newDimension);
      }
   }

   protected void fireZoomLevelChanged(int oldZoomLevel, int newZoomLevel) {
      for (LedLetterFontDisplayModelListener l : eel.getListeners(LedLetterFontDisplayModelListener.class)) {
         l.zoomLevelChanged(oldZoomLevel, newZoomLevel);
      }
   }

   //endregion
}