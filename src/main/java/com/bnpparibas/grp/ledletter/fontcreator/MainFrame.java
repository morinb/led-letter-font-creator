package com.bnpparibas.grp.ledletter.fontcreator;

import com.bnpparibas.grp.ledletter.fontcreator.icons.Icons;
import com.bnpparibas.grp.ledletter.fontcreator.status.StatusBar;
import com.bnpparibas.grp.ledletter.fontcreator.status.StatusBarFactory;
import com.bnpparibas.grp.ledletter.fontcreator.utils.HexUtils;
import com.google.common.collect.Maps;
import com.jtattoo.plaf.acryl.AcrylLookAndFeel;
import org.apache.commons.lang.StringUtils;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author morinb.
 */
public class MainFrame extends JFrame {
   private static final String LAST_USED_FOLDER = "LedLetterFontCreator : LAST_USED_FOLDER";

   private static final Pattern FONT_FORMAT_LINES_PATTERN = Pattern.compile(".*?_\\d+\\s*\\(\\s*\"([0-9A-F ]+)\"\\s*,\\s*(\\d+)\\s*\\)\\s*\\/\\s*\\*.*?\\s*\\*\\/,");

   public static final FileFilter LLF_FILE_FILTER = new FileFilter() {
      @Override
      public boolean accept(File f) {
         return f.isDirectory() || f.isHidden() || f.getAbsolutePath().endsWith(".llf");
      }

      @Override
      public String getDescription() {
         return "(*.llf) Led Letter Font";
      }
   };
   public static final String HEADER = "w:%d,h:%d,b:%d\n";
   private JTabbedPane tabbedPane = new JTabbedPane();
   private JMenuItem menuItemSave;

   //region character list 
   int[] characters = {32 /* SPACE */,
         33 /* ! */,
         34 /* " */,
         35 /* # */,
         36 /* $ */,
         37 /* % */,
         38 /* & */,
         40 /* ( */,
         41 /*  */,
         42 /* * */,
         43 /* + */,
         44 /* , */,
         45 /* - */,
         46 /* . */,
         47 /*  / */,
         48 /* 0 */,
         49 /* 1 */,
         50 /* 2 */,
         51 /* 3 */,
         52 /* 4 */,
         53 /* 5 */,
         54 /* 6 */,
         55 /* 7 */,
         56 /* 8 */,
         57 /* 9 */,
         58 /* : */,
         59 /* ; */,
         60 /* < */,
         61 /* = */,
         62 /* > */,
         63 /* ? */,
         64 /* @ */,
         65 /* A */,
         66 /* B */,
         67 /* C */,
         68 /* D */,
         69 /* E */,
         70 /* F */,
         71 /* G */,
         72 /* H */,
         73 /* I */,
         74 /* J */,
         75 /* K */,
         76 /* L */,
         77 /* M */,
         78 /* N */,
         79 /* O */,
         80 /* P */,
         81 /* Q */,
         82 /* R */,
         83 /* S */,
         84 /* T */,
         85 /* U */,
         86 /* V */,
         87 /* W */,
         88 /* X */,
         89 /* Y */,
         90 /* Z */,
         91 /* [ */,
         92 /* \ */,
         93 /* ] */,
         95 /* _ */,
         97 /* a */,
         98 /* b */,
         99 /* c */,
         100 /* d */,
         101 /* e */,
         102 /* f */,
         103 /* g */,
         104 /* h */,
         105 /* i */,
         106 /* j */,
         107 /* k */,
         108 /* l */,
         109 /* m */,
         110 /* n */,
         111 /* o */,
         112 /* p */,
         113 /* q */,
         114 /* r */,
         115 /* s */,
         116 /* t */,
         117 /* u */,
         118 /* v */,
         119 /* w */,
         120 /* x */,
         121 /* y */,
         122 /* z */,
         123 /* { */,
         125 /* } */,
         161 /* &iexcl; */,
         186 /* &ordm; */,
         191 /* &iquest; */,
         209 /* &Ntilde; */,
         241 /* &ntilde; */};
   //endregion

   private Map<Integer, LedLetterFontDisplay> displaysMap = Maps.newHashMap();
   private final StatusBar statusBar;
   private int nbCol;
   private int nbRow;

   MainFrame() {
      setTitle("Led Letter Font Creator");
      createMenuBar();
      statusBar = StatusBarFactory.getDefaultStatusBar();

      statusBar.addStatusDirtyChangedListener(isDirty -> menuItemSave.setEnabled(isDirty));

      this.setLayout(new BorderLayout());
      this.add(tabbedPane, BorderLayout.CENTER);

      this.add(statusBar, BorderLayout.SOUTH);
      statusBar.setMessageFor("Application Launched", 3, TimeUnit.SECONDS);
   }

   public static void main(String[] args) {

      try {
         final Properties themeProperties = AcrylLookAndFeel.getThemeProperties("Default");
         themeProperties.put("logoString", "");
         themeProperties.put("tooltipCastShadow", "0");
         themeProperties.put("tooltipBorderSize", "0");
         themeProperties.put("tooltipShadowSize", "0");
         themeProperties.put("centerWindowTitle", "on");
         themeProperties.put("menuOpaque", "on");
         AcrylLookAndFeel.setCurrentTheme(themeProperties);
         UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
      } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
         e.printStackTrace();
      }

      SwingUtilities.invokeLater(() -> {
         MainFrame mf = new MainFrame();
         mf.setSize(800, 600);
         mf.setLocationRelativeTo(null);
         mf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
         mf.setVisible(true);

      });
   }

   private void createMenuBar() {
      final JMenuBar menuBar = new JMenuBar();
      //region Menu File
      {
         final JMenu file = new JMenu("File");
         file.setMnemonic('F');

         menuBar.add(file);

         //region MenuItem New
         file.add(createMenuItem("New", "Creates a new Led Letter Font file.", 'n', "control N", Icons.NEW, (e) -> {
            if (isDirty()) {
               if (doesUserWantToSave()) {
                  // save
               }

            }

            for (int c : characters) {


               LedLetterFontDisplayModel model = askUserModel();
               LedLetterFontDisplay fontDisplay = new LedLetterFontDisplay(model);
               JScrollPane scrollPaneFontDisplay = new JScrollPane();
               scrollPaneFontDisplay.getViewport().add(fontDisplay);
               model.addLedLetterFontDisplayModelListener(new MyLedLetterFontDisplayModelListener(scrollPaneFontDisplay));
               displaysMap.put(c, fontDisplay);
               tabbedPane.add("" + (char) c, scrollPaneFontDisplay);
            }

            statusBar.setDirty(true);

         }));

         //endregion

         //region MenuItem Open 
         file.add(createMenuItem("Open", "Opens a Led Letter Font", 'o', "control O", Icons.OPEN, (e) -> {
            Preferences preferences = Preferences.userRoot().node(getClass().getName());
            final JFileChooser jfc = new JFileChooser(preferences.get(LAST_USED_FOLDER, new File(".").getAbsolutePath()));
            jfc.setFileFilter(LLF_FILE_FILTER);
            if (JFileChooser.APPROVE_OPTION == jfc.showOpenDialog(MainFrame.this)) {
               preferences.put(LAST_USED_FOLDER, jfc.getSelectedFile().getParent());

               try {
                  openLedLetterFont(jfc.getSelectedFile());
               } catch (IOException e1) {
                  e1.printStackTrace();
                  statusBar.setMessageFor(e1.getMessage(), 10, TimeUnit.SECONDS);
               }
            }
         }));
         //endregion

         //region MenuItem Save
         final JMenuItem menuItemSave = createMenuItem("Save", "Save the current Led Letter Font", 's', "control S", Icons.SAVE, (e) -> {
            Preferences preferences = Preferences.userRoot().node(getClass().getName());
            final JFileChooser jfc = new JFileChooser(preferences.get(LAST_USED_FOLDER, new File(".").getAbsolutePath()));
            jfc.setFileFilter(LLF_FILE_FILTER);
            if (JFileChooser.APPROVE_OPTION == jfc.showSaveDialog(MainFrame.this)) {
               preferences.put(LAST_USED_FOLDER, jfc.getSelectedFile().getParent());
               try {
                  saveLedLetterFont(jfc.getSelectedFile());
                  statusBar.setDirty(false);
               } catch (FileNotFoundException e1) {
                  e1.printStackTrace();
                  statusBar.setMessageFor(e1.getMessage(), 10, TimeUnit.SECONDS);
               }
            }

         });
         menuItemSave.setEnabled(false);
         file.add(menuItemSave);
         //endregion

         //region MenuItem Exit
         file.addSeparator();
         file.add(createMenuItem("Exit", "Exits the application", 'x', "control Q", Icons.EXIT, (e -> {
            if (isDirty()) {
               if (doesUserWantToSave()) {
                  // save
               }
            }
            System.exit(0);

         })));
         //endregion
      }
      //endregion

      //region Menu Help
      {
         final JMenu help = new JMenu("Help");
         help.setMnemonic('H');
         menuBar.add(Box.createHorizontalGlue());
         menuBar.add(help);

         help.add(createMenuItem("About", "Display About window", 'a', "control COMMA", Icons.ABOUT, e -> {
            JOptionPane.showMessageDialog(MainFrame.this, "", "About", JOptionPane.INFORMATION_MESSAGE);
         }));
      }
      //endregion
      setJMenuBar(menuBar);
   }

   private LedLetterFontDisplayModel askUserModel() {
      final LedLetterFontDisplayModel model = new LedLetterFontDisplayModel(new Dimension(10, 10), new Dimension(7, 7));
      this.nbCol = model.getGridDimension().width;
      this.nbRow = model.getGridDimension().height;
      return model;
   }

   private void openLedLetterFont(File selectedFile) throws IOException {
      final List<String> lines = Files.readAllLines(Paths.get(selectedFile.toURI()));
      boolean error = false;
      if (lines.size() > 1) {
         String header = lines.get(0);
         final String[] heads = header.split(",");
         this.nbCol = Integer.valueOf(heads[0].startsWith("w:") ? heads[0].substring(2) : "-1");
         this.nbRow = Integer.valueOf(heads[1].startsWith("h:") ? heads[1].substring(2) : "-1");
         int nbBytes = Integer.valueOf(heads[2].startsWith("b:") ? heads[2].substring(2) : "-1");
         if (nbCol == -1 || nbRow == -1 || nbBytes == -1) {
            error = true;
         } else {
            for (String line : lines) {
               final Matcher matcher = FONT_FORMAT_LINES_PATTERN.matcher(line);
               if (matcher.matches()) {
                  final String encodedData = matcher.group(1);
                  final int charValue = Integer.parseInt(matcher.group(2));

                  boolean[][] v = new boolean[nbRow][nbCol];
                  for (int j = 0; j < nbBytes * nbCol; j += nbBytes) {
                     for (int b = 0; b < nbBytes; b++) {
                        BitSet bs = BitSet.valueOf(HexUtils.toBytes(encodedData.split(" ")[j + b]));
                        for (int k = 8 * b; k < Math.min(8 * (b + 1), nbRow); k++) {
                           final int colIndex = (j / nbBytes);
                           v[k][colIndex] = bs.get(7 - k + 8 * b);
                        }
                     }
                  }

                  LedLetterFontDisplayModel model = new LedLetterFontDisplayModel(new Dimension(nbCol, nbRow));
                  LedLetterFontDisplay fontDisplay = new LedLetterFontDisplay(model);
                  fontDisplay.addStatusDirtyChangedListener(statusBar); // Make status bar listen to the display dirty status.
                  fontDisplay.setValues(v);

                  JScrollPane scrollPaneFontDisplay = new JScrollPane();
                  scrollPaneFontDisplay.getViewport().add(fontDisplay);
                  model.addLedLetterFontDisplayModelListener(new MyLedLetterFontDisplayModelListener(scrollPaneFontDisplay));

                  displaysMap.put(charValue, fontDisplay);
                  tabbedPane.add("" + (char) charValue, scrollPaneFontDisplay);

               }
            }
         }
      } else {
         error = true;
      }

      if (error) {
         final String message = selectedFile + " has not the right format.";
         statusBar.setMessageFor(message, 10, TimeUnit.SECONDS);
         JOptionPane.showMessageDialog(MainFrame.this, message, "Error", JOptionPane.ERROR_MESSAGE);
      }
   }

   private void saveLedLetterFont(File selectedFile) throws FileNotFoundException {
      if (!selectedFile.getName().endsWith(".llf")) {
         selectedFile = new File(selectedFile.getAbsoluteFile() + ".llf");
      }
      statusBar.setMessageFor("Saving under " + selectedFile.getName(), 3, TimeUnit.SECONDS);
      // Writing header
      final LedLetterFontDisplay next = displaysMap.values().iterator().next();
      int nbBytes = nbRow / 8;
      if (nbRow % 8 != 0) {
         nbBytes += 1;
      }
      PrintWriter pw = new PrintWriter(selectedFile);

      pw.write(String.format(HEADER, nbCol, nbRow, nbBytes));
      for (int c : characters) {
         LedLetterFontDisplay fd = displaysMap.get(c);

         final boolean[][] values = fd.getValues();

         pw.write(String.format("_%d(\"", c));
         for (int j = 0; j < nbBytes * nbCol; j += nbBytes) {
            for (int b = 0; b < nbBytes; b++) {
               BitSet bs = new BitSet();
               for (int rowIndex = 8 * b; rowIndex < Math.min(8 * (b + 1), nbRow); rowIndex++) {
                  int colIndex = j / nbBytes;
                  final int bitIndex = rowIndex - 8 * b;
                  bs.set(bitIndex, values[rowIndex][colIndex]);
               }
               pw.write(bitSetToHexa(bs) + " ");
            }
         }
         pw.write(String.format("\", %d) /* %s */,\n", c, c));
      }
      pw.close();
   }

   private String bitSetToHexa(final BitSet bs) {
      final StringBuilder sb = new StringBuilder();

      for (int i = 0; i < 8; i++) {
         sb.append(bs.get(i) ? '1' : '0');
      }
      return StringUtils.leftPad(Integer.toHexString(Integer.parseInt(sb.toString(), 2)), 2, '0').toUpperCase();
   }

   //region menu item creation utility methods.
   private JMenuItem createMenuItem(String name, String howerText, char mnemonic, String accelerator, ActionListener listener) {
      return createMenuItem(name, howerText, mnemonic, accelerator, null, listener);
   }

   private JMenuItem createMenuItem(String name, String howerText, char mnemonic, String accelerator, Icon icon, ActionListener listener) {
      JMenuItem menuItem = new JMenuItem(name);
      menuItem.addActionListener(listener);
      menuItem.setAccelerator(KeyStroke.getKeyStroke(accelerator));
      menuItem.setMnemonic(mnemonic);
      menuItem.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseEntered(MouseEvent e) {
            statusBar.setMessageFor(howerText, 3, TimeUnit.SECONDS);
         }

         @Override
         public void mouseExited(MouseEvent e) {
            statusBar.clearMessage();
         }
      });
      if (icon != null) {
         menuItem.setIcon(icon);
      }

      return menuItem;
   }

   //endregion 

   private boolean doesUserWantToSave() {
      return false;
   }

   private boolean isDirty() {
      return statusBar.isDirty();
   }
   
   private class MyLedLetterFontDisplayModelListener implements LedLetterFontDisplayModelListener {
      JScrollPane container;

      public MyLedLetterFontDisplayModelListener(JScrollPane container) {
         this.container = container;
      }

      @Override
      public void cellDimensionChanged(Dimension oldDimension, Dimension newDimension) {
         container.revalidate();
      }

      @Override
      public void gridThicknessChanged(int oldthickness, int newThickness) {
         container.revalidate();
      }

      @Override
      public void gridDimensionChanged(Dimension oldDimension, Dimension newDimension) {
         container.revalidate();
      }

      @Override
      public void zoomLevelChanged(int oldZoomLevel, int newZoomLevel) {
         container.revalidate();
      }
   }
}
