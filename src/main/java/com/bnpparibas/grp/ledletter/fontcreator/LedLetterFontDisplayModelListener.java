package com.bnpparibas.grp.ledletter.fontcreator;

import java.awt.Dimension;
import java.util.EventListener;

/**
 * @author morinb.
 */
public interface LedLetterFontDisplayModelListener extends EventListener{
   void cellDimensionChanged(Dimension oldDimension, Dimension newDimension);

   void gridThicknessChanged(int oldthickness, int newThickness);
   
   void gridDimensionChanged(Dimension oldDimension, Dimension newDimension);

   void zoomLevelChanged(int oldZoomLevel, int newZoomLevel);
}
