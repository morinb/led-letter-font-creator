package com.bnpparibas.grp.ledletter.fontcreator;

import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.tabbedpanel.titledtab.TitledTab;

import javax.swing.ImageIcon;
import java.awt.Image;

/**
 * @author morinb.
 */
public class TabbedPaneIconUpdater implements LedLetterChangedListener {

   private TabbedPanel tabbedPane;
   private final int c;
   private final int tabIndex;


   public TabbedPaneIconUpdater(TabbedPanel tabbedPane, int c, int tabIndex) {
      this.tabbedPane = tabbedPane;
      this.c = c;
      this.tabIndex = tabIndex;
   }

   @Override
   public void letterChanged(int c, Image image) {
      if (this.c == c) {
         ImageIcon icon = new ImageIcon(image);
         final TitledTab titledTab = (TitledTab) this.tabbedPane.getTabAt(tabIndex);
         titledTab.setIcon(icon);
         titledTab.getProperties().getHighlightedProperties().setIcon(icon);
      }
   }
}
