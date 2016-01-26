package com.bnpparibas.grp.ledletter.fontcreator.status;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;
import java.util.concurrent.TimeUnit;

/**
 * @author morinb.
 */
public abstract class StatusBar extends JPanel implements StatusDirtyChangedListener {
   private static final EventListenerList ell = new EventListenerList();
   /**
    * Defines the displayed bar message
    *
    * @param message the message to display.
    */
   public abstract void setMessageFor(String message, long duration, TimeUnit unit);
   /**
    * @return
    */
   public abstract boolean isDirty();

   /**
    * @param dirty
    */
   public abstract void setDirty(boolean dirty);

   /**
    * Display the progress bar value
    *
    * @param min
    * @param value
    * @param max
    */
   public abstract void setProgress(int min, int value, int max);

   /**
    * Display the progress bar in an indeterminate state.
    */
   public abstract void setProgressIndeterminate(boolean indeterminate);


   /**
    * Hides the progress bar
    */
   public abstract void hideProgress();

   public abstract void clearMessage();
   
   public void addStatusDirtyChangedListener(StatusDirtyChangedListener l) {
      ell.add(StatusDirtyChangedListener.class, l);
   }
   public void removeStatusDirtyChangedListener(StatusDirtyChangedListener l) {
      ell.remove(StatusDirtyChangedListener.class, l);
   }
   public void fireStatusDirtyChanged(boolean dirty) {
      for (StatusDirtyChangedListener l : ell.getListeners(StatusDirtyChangedListener.class)) {
         l.dirtyChanged(dirty);
      }
   }

   @Override
   public void dirtyChanged(boolean newValue) {
      setDirty(newValue);
   }
}
