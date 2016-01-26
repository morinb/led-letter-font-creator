package com.bnpparibas.grp.ledletter.fontcreator;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import java.awt.Image;

/**
 * @author morinb.
 */
public class TabbedPaneIconUpdater implements LedLetterChangedListener {

   private JTabbedPane tabbedPane;
   private final int c;
   private final int tabIndex;


   public TabbedPaneIconUpdater(JTabbedPane tabbedPane, int c, int tabIndex) {
      this.tabbedPane = tabbedPane;
      this.c = c;
      this.tabIndex = tabIndex;
   }

   @Override
   public void letterChanged(int c, Image image) {
      if (this.c == c) {
         this.tabbedPane.setIconAt(tabIndex, new ImageIcon(image));
         this.tabbedPane.revalidate();
      }
   }
}
