package com.bnpparibas.grp.ledletter.fontcreator;

import com.bnpparibas.grp.ledletter.fontcreator.icons.Icons;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.text.Format;
import java.text.NumberFormat;

/**
 * @author morinb.
 */
public class UserModelDialog extends JDialog {
   private LedLetterFontDisplayModel model = null;

   //region GUI Components
   private Format numberFormat = NumberFormat.getIntegerInstance();
   private JFormattedTextField textFieldRowNumber = new JFormattedTextField(numberFormat);
   private JFormattedTextField textFieldColumnNumber = new JFormattedTextField(numberFormat);

   private JFormattedTextField textFieldCellWidth = new JFormattedTextField(numberFormat);
   private JFormattedTextField textFieldCellHeight = new JFormattedTextField(numberFormat);

   private JFormattedTextField textFieldThickness = new JFormattedTextField(numberFormat);

   private JButton buttonOk = new JButton("OK");
   private JButton buttonClose = new JButton("Close");


   //endregion

   public UserModelDialog(Frame owner) {
      super(owner, "New Letters", true);
      this.setIconImage(Icons.FONTS.getImage());
      buttonClose.addActionListener(e -> dispose());
      buttonOk.addActionListener(e -> {
         model = new LedLetterFontDisplayModel(
               new Dimension(Integer.valueOf(textFieldCellWidth.getText()), Integer.valueOf(textFieldCellHeight.getText())),
               new Dimension(Integer.valueOf(textFieldColumnNumber.getText()), Integer.valueOf(textFieldRowNumber.getText())),
               Integer.valueOf(textFieldThickness.getText()));
         dispose();
      });

      initValues(new LedLetterFontDisplayModel());

      this.setLayout(new BorderLayout());

      final String colSpecs = "$tdm, r:p, $lcg, $b, $rg, $b";
      final String rowSpecs = "p, $rg, p, $rg, p, $ug, p, $rg, p, $rg, p, $ug, p, $rg, p, $ug, $b";
      final FormLayout layout = new FormLayout(colSpecs, rowSpecs);
      layout.setColumnGroups(new int[][]{{4, 6}});

      final PanelBuilder pb = new PanelBuilder(layout);
      pb.border(Borders.DIALOG);

      pb.addSeparator("Grid dimensions", CC.xyw(1, 1, 6));
      pb.addLabel("Row number", CC.xy(2, 3));
      pb.add(textFieldRowNumber, CC.xyw(4, 3, 3));
      pb.addLabel("Col number", CC.xy(2, 5));
      pb.add(textFieldColumnNumber, CC.xyw(4, 5, 3));

      pb.addSeparator("Cell dimensions", CC.xyw(1, 7, 6));
      pb.addLabel("Cell width", CC.xy(2, 9));
      pb.add(textFieldCellWidth, CC.xyw(4, 9, 3));
      pb.addLabel("Cell height", CC.xy(2, 11));
      pb.add(textFieldCellHeight, CC.xyw(4, 11, 3));

      pb.addSeparator("Grid property", CC.xyw(1, 13, 6));
      pb.addLabel("Grid thickness", CC.xy(2, 15));
      pb.add(textFieldThickness, CC.xyw(4, 15, 3));

      pb.add(buttonOk, CC.xy(4, 17));
      pb.add(buttonClose, CC.xy(6, 17));

      this.add(pb.build(), BorderLayout.CENTER);
      
      this.getRootPane().setDefaultButton(buttonOk);
   }

   private void initValues(LedLetterFontDisplayModel model) {
      textFieldCellHeight.setText("" + model.getCellDimension().height);
      textFieldCellWidth.setText("" + model.getCellDimension().width);

      textFieldColumnNumber.setText("" + model.getGridDimension().width);
      textFieldRowNumber.setText("" + model.getGridDimension().height);

      textFieldThickness.setText("" + model.getGridThickness());

      textFieldRowNumber.setHorizontalAlignment(SwingConstants.TRAILING);
      textFieldColumnNumber.setHorizontalAlignment(SwingConstants.TRAILING);
      textFieldCellWidth.setHorizontalAlignment(SwingConstants.TRAILING);
      textFieldCellHeight.setHorizontalAlignment(SwingConstants.TRAILING);
      textFieldThickness.setHorizontalAlignment(SwingConstants.TRAILING);
   }

   public LedLetterFontDisplayModel getModel() {
      return model;
   }
}
