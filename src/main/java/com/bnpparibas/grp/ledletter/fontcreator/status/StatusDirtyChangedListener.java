package com.bnpparibas.grp.ledletter.fontcreator.status;

import java.util.EventListener;

/**
 * @author morinb.
 */
public interface StatusDirtyChangedListener extends EventListener {
   void dirtyChanged(boolean newValue);
}
