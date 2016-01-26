package com.bnpparibas.grp.ledletter.fontcreator;

import java.awt.Image;
import java.util.EventListener;

/**
 * @author morinb.
 */
public interface LedLetterChangedListener extends EventListener {
   void letterChanged(int c, Image image);
}
