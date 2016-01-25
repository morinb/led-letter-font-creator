package com.bnpparibas.grp.ledletter.fontcreator.status;

import com.bnpparibas.grp.ledletter.fontcreator.status.impl.DefaultStatusBar;

/**
 * @author morinb.
 */
public final class StatusBarFactory {
   public static StatusBar getDefaultStatusBar() {
      return new DefaultStatusBar();
   }
}
