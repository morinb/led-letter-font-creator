package com.bnpparibas.grp.ledletter.fontcreator;

import com.bnpparibas.grp.ledletter.fontcreator.icons.Icons;
import com.google.common.collect.Sets;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.util.Set;

/**
 * @author morinb.
 */
public class AddLetterDialog extends JDialog {
   private Character selectedLetter = null;

   private static final Set<Character> ASCII_8BIT_LETTERS = Sets.newLinkedHashSet();

   static {
      for (char c = 32; c <= 255; c++) {
         ASCII_8BIT_LETTERS.add(c);
      }
   }

   //region GUI Components
   private final JComboBox<Character> comboBox;

   private final JButton buttonOk = new JButton("Ok");
   private final JButton buttonClose = new JButton("Close");

   //endregion

   public AddLetterDialog(Frame owner, Set<Character> alreadyCreatedLetters) {
      super(owner, "Add Character", true);
      this.setIconImage(Icons.FONTS.getImage());

      Set notCreatedLetters = Sets.difference(ASCII_8BIT_LETTERS, alreadyCreatedLetters);
      Character[] array = new Character[notCreatedLetters.size()];
      notCreatedLetters.toArray(array);
      comboBox = new JComboBox<>(array);

      buttonClose.addActionListener(e -> dispose());
      buttonOk.addActionListener(e -> {
         selectedLetter = (char) comboBox.getSelectedItem();
         dispose();
      });

      this.setLayout(new BorderLayout());

      final String colSpecs = "$b, $rg, $b";
      final String rowSpecs = "p, $ug, $b";

      final FormLayout layout = new FormLayout(colSpecs, rowSpecs);
      final PanelBuilder pb = new PanelBuilder(layout);
      pb.border(Borders.DIALOG);

      pb.add(comboBox, CC.xyw(1, 1, 3));

      pb.add(buttonOk, CC.xy(1, 3));
      pb.add(buttonClose, CC.xy(3, 3));

      this.add(pb.build(), BorderLayout.CENTER);
   }

   public Character getSelectedLetter() {
      return selectedLetter;
   }
}
