package com.bnpparibas.grp.ledletter.fontcreator.utils;

import java.awt.Color;

/**
 * @author morinb.
 */
public class ColorUtils {

   public static Color deriveColorAlpha(Color base, int newAlpha) {
      return new Color(base.getRed(), base.getGreen(), base.getBlue(), newAlpha);
   }

}
