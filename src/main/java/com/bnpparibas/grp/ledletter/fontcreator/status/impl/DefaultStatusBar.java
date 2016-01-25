package com.bnpparibas.grp.ledletter.fontcreator.status.impl;

import com.bnpparibas.grp.ledletter.fontcreator.status.StatusBar;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.FlowLayout;
import java.util.concurrent.TimeUnit;

import static com.bnpparibas.grp.ledletter.fontcreator.utils.ColorUtils.deriveColorAlpha;

/**
 * @author morinb.
 */
public class DefaultStatusBar extends StatusBar {
   private static final int FADEOUT_DURATION = 1000;
   private static final int FADEOUT_ITERATION = 50;
   private static final double FADEOUT_ALPHA_STEP = FADEOUT_DURATION / FADEOUT_ITERATION;
   private final JSeparator separatorCheckbox;
   private final JSeparator separatorProgressBar;
   private final JLabel labelMessage;
   private final JProgressBar progressBar;
   private final JCheckBox checkBoxDirty;

   private Timer fadeOutTimer;
   private Timer waitTimer;


   public DefaultStatusBar() {
      labelMessage = new JLabel("");
      checkBoxDirty = new JCheckBox();
      checkBoxDirty.setToolTipText("Modified");
      checkBoxDirty.setEnabled(false);
      progressBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);
      
      separatorCheckbox = new JSeparator(SwingConstants.VERTICAL);
      separatorProgressBar = new JSeparator(SwingConstants.VERTICAL);

      this.setLayout(new FlowLayout(FlowLayout.LEADING));
      this.add(progressBar);
      this.add(separatorProgressBar);
      this.add(checkBoxDirty);
      this.add(separatorCheckbox);
      this.add(labelMessage);

      fadeOutTimer = new Timer(FADEOUT_ITERATION, e -> {
         int alpha = labelMessage.getForeground().getAlpha();
         alpha = (int) (alpha - FADEOUT_ALPHA_STEP);
         if (alpha < 0) {
            alpha = 0;
         }
         labelMessage.setForeground(deriveColorAlpha(labelMessage.getForeground(), alpha));
         if (alpha == 0) {
            // Reset the label
            fadeOutTimer.stop();
            labelMessage.setText("");
            labelMessage.setForeground(deriveColorAlpha(labelMessage.getForeground(), 255));
         }
      });

      hideProgress();
   }


   @Override
   public void setMessageFor(String message, long duration, TimeUnit unit) {
      labelMessage.setText(message);

      fadeOutTextIn(duration, unit);
   }

   @Override
   public void clearMessage() {
      labelMessage.setText("");
      resetWaitTimer();
      fadeOutTimer.stop();
   }

   private void resetWaitTimer() {
      if (waitTimer != null) {
         waitTimer.stop();
         waitTimer = null;
      }
   }
   private void fadeOutTextIn(long duration, TimeUnit unit) {
      resetWaitTimer();

      // Starts in duration unit.
      waitTimer = new Timer((int) unit.toMillis(duration), e -> {
         fadeOutTimer.start();
      });
      waitTimer.start();


   }


   @Override
   public boolean isDirty() {
      return checkBoxDirty.isSelected();
   }

   @Override
   public void setDirty(boolean dirty) {
      checkBoxDirty.setSelected(dirty);
   }

   @Override
   public void setProgress(int min, int value, int max) {
      progressBar.setMinimum(min);
      progressBar.setMaximum(max);
      progressBar.setValue(value);
      separatorProgressBar.setVisible(true);
      progressBar.setVisible(true);
   }

   @Override
   public void setProgressIndeterminate(boolean indeterminate) {
      progressBar.setIndeterminate(indeterminate);
   }

   @Override
   public void hideProgress() {
      setProgress(0, 0, 100);
      progressBar.setVisible(false);
      separatorProgressBar.setVisible(false);
   }
}
