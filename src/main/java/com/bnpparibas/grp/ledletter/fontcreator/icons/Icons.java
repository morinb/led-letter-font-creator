package com.bnpparibas.grp.ledletter.fontcreator.icons;

import javax.swing.ImageIcon;
import java.awt.Image;

/**
 * @author morinb.
 */
public final class Icons {

   public static final ImageIcon NEW = new ImageIcon(Icons.class.getResource("/images/new.png"));
   public static final ImageIcon SAVE = new ImageIcon(Icons.class.getResource("/images/save.png"));
   public static final ImageIcon EXIT = new ImageIcon(Icons.class.getResource("/images/exit.png"));
   public static final ImageIcon ABOUT = resize(16, 16, new ImageIcon(Icons.class.getResource("/images/about.png")));
   public static final ImageIcon OPEN = new ImageIcon(Icons.class.getResource("/images/open.png"));

   private static ImageIcon resize(int width, int height, ImageIcon imageIcon) {
      if (width == imageIcon.getIconWidth() && imageIcon.getIconHeight() == height) {
         return imageIcon;
      }


      Image img = imageIcon.getImage();
      Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

      return new ImageIcon(newImg);
   }
}
