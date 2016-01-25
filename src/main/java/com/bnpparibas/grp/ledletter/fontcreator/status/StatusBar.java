package com.bnpparibas.grp.ledletter.fontcreator.status;

import javax.swing.JPanel;
import java.util.concurrent.TimeUnit;

/**
 * @author morinb.
 */
public abstract class StatusBar extends JPanel {
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
}
