package com.bnpparibas.grp.ledletter.fontcreator;

import com.bnpparibas.grp.ledletter.fontcreator.icons.Icons;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.util.List;

/**
 * @author morinb.
 */
public class AddLetterDialog extends JDialog {
   int selectedLetter;

   private static final Color INVALID = new Color(1.0f, 0.0f, 0.0f, 0.25f);
   private static final Color VALID = new JSpinner().getBackground();
   
   
   //region GUI Components
   private final JSpinner spinner = new JSpinner(new SpinnerNumberModel(30, 0, 255, 1));
   private final JLabel label = new JLabel(" ");

   private final JButton buttonOk = new JButton("Ok");
   private final JButton buttonClose = new JButton("Close");

   //endregion

   public AddLetterDialog(Frame owner, List<Integer> alreadyCreatedLetters) {
      super(owner, "Add Character", true);
      this.setIconImage(Icons.FONTS.getImage());

      spinner.addChangeListener(e -> {
         final int letter = (int) spinner.getValue();
         label.setText("" + (char) letter);
         if (alreadyCreatedLetters.contains(letter)) {
            spinner.getEditor().setBackground(INVALID);
            buttonOk.setEnabled(false);
         } else {
            spinner.getEditor().setBackground(VALID);
            buttonOk.setEnabled(true);
         }
      });
      buttonClose.addActionListener(e -> dispose());
      buttonOk.addActionListener(e -> selectedLetter = (int) spinner.getValue());

      this.setLayout(new BorderLayout());

      final String colSpecs = "$b, $rg, $b";
      final String rowSpecs = "p, $ug, $b";

      final FormLayout layout = new FormLayout(colSpecs, rowSpecs);
      layout.setColumnGroups(new int[][]{{1, 3}});
      final PanelBuilder pb = new PanelBuilder(layout);


      pb.add(spinner, CC.xy(1, 1));
      pb.add(label, CC.xy(3, 1));

      pb.add(buttonOk, CC.xy(1, 3));
      pb.add(buttonClose, CC.xy(3, 3));

      this.add(pb.build(), BorderLayout.CENTER);
   }

   public int getSelectedLetter() {
      return selectedLetter;
   }
}
