package com.bnpparibas.grp.ledletter.fontcreator;

import com.bnpparibas.grp.ledletter.fontcreator.icons.Icons;
import com.bnpparibas.grp.ledletter.fontcreator.status.StatusBar;
import com.bnpparibas.grp.ledletter.fontcreator.status.StatusBarFactory;
import com.bnpparibas.grp.ledletter.fontcreator.utils.HexUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
   private JMenuItem menuItemAddLetter;

   //region character list 
   private static final List<Character> ADDED_LETTER_LIST = Lists.newArrayList();
   private static final List<Character> DEFAULT_CHARACTERS = Lists.newArrayList(
         (char) 32 /* SPACE */,
         (char) 33 /* ! */,
         (char) 34 /* " */,
         (char) 35 /* # */,
         (char) 36 /* $ */,
         (char) 37 /* % */,
         (char) 38 /* & */,
         (char) 40 /* ( */,
         (char) 41 /*  */,
         (char) 42 /* * */,
         (char) 43 /* + */,
         (char) 44 /* , */,
         (char) 45 /* - */,
         (char) 46 /* . */,
         (char) 47 /*  / */,
         (char) 48 /* 0 */,
         (char) 49 /* 1 */,
         (char) 50 /* 2 */,
         (char) 51 /* 3 */,
         (char) 52 /* 4 */,
         (char) 53 /* 5 */,
         (char) 54 /* 6 */,
         (char) 55 /* 7 */,
         (char) 56 /* 8 */,
         (char) 57 /* 9 */,
         (char) 58 /* : */,
         (char) 59 /* ; */,
         (char) 60 /* < */,
         (char) 61 /* = */,
         (char) 62 /* > */,
         (char) 63 /* ? */,
         (char) 64 /* @ */,
         (char) 65 /* A */,
         (char) 66 /* B */,
         (char) 67 /* C */,
         (char) 68 /* D */,
         (char) 69 /* E */,
         (char) 70 /* F */,
         (char) 71 /* G */,
         (char) 72 /* H */,
         (char) 73 /* I */,
         (char) 74 /* J */,
         (char) 75 /* K */,
         (char) 76 /* L */,
         (char) 77 /* M */,
         (char) 78 /* N */,
         (char) 79 /* O */,
         (char) 80 /* P */,
         (char) 81 /* Q */,
         (char) 82 /* R */,
         (char) 83 /* S */,
         (char) 84 /* T */,
         (char) 85 /* U */,
         (char) 86 /* V */,
         (char) 87 /* W */,
         (char) 88 /* X */,
         (char) 89 /* Y */,
         (char) 90 /* Z */,
         (char) 91 /* [ */,
         (char) 92 /* \ */,
         (char) 93 /* ] */,
         (char) 95 /* _ */,
         (char) 97 /* a */,
         (char) 98 /* b */,
         (char) 99 /* c */,
         (char) 100 /* d */,
         (char) 101 /* e */,
         (char) 102 /* f */,
         (char) 103 /* g */,
         (char) 104 /* h */,
         (char) 105 /* i */,
         (char) 106 /* j */,
         (char) 107 /* k */,
         (char) 108 /* l */,
         (char) 109 /* m */,
         (char) 110 /* n */,
         (char) 111 /* o */,
         (char) 112 /* p */,
         (char) 113 /* q */,
         (char) 114 /* r */,
         (char) 115 /* s */,
         (char) 116 /* t */,
         (char) 117 /* u */,
         (char) 118 /* v */,
         (char) 119 /* w */,
         (char) 120 /* x */,
         (char) 121 /* y */,
         (char) 122 /* z */,
         (char) 123 /* { */,
         (char) 125 /* } */,
         (char) 161 /* &iexcl; */,
         (char) 186 /* &ordm; */,
         (char) 191 /* &iquest; */,
         (char) 209 /* &Ntilde; */,
         (char) 241 /* &ntilde; */);
   //endregion

   private LedLetterFontDisplayModel selectedModel;

   private Map<Character, LedLetterFontDisplay> displaysMap = Maps.newHashMap();
   private final StatusBar statusBar;
   private int nbCol;
   private int nbRow;

   MainFrame() {
      setTitle("Led Letter Font Creator");
      createMenuBar();
      statusBar = StatusBarFactory.getDefaultStatusBar();

      statusBar.addStatusDirtyChangedListener(isDirty -> menuItemSave.setEnabled(isDirty));
      tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
      this.setLayout(new BorderLayout());
      this.add(tabbedPane, BorderLayout.CENTER);

      this.add(statusBar, BorderLayout.SOUTH);
      statusBar.setMessageFor("Application Launched", 3, TimeUnit.SECONDS);


      this.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            Boolean res = saveIfDirtyAndCloseAll();
            if(res != null) {
               System.exit(0);
            }

         }
      });
   }

   public static void main(String[] args) {

      try {
         UIManager.setLookAndFeel("net.infonode.gui.laf.InfoNodeLookAndFeel");
      } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
         e.printStackTrace();
      }

      SwingUtilities.invokeLater(() -> {
         MainFrame mf = new MainFrame();
         mf.setIconImage(Icons.FONTS.getImage());
         mf.setSize(800, 600);
         mf.setLocationRelativeTo(null);
         mf.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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
            saveIfDirtyAndCloseAll();
            selectedModel = askUserModel();
            if (selectedModel != null) {
               DEFAULT_CHARACTERS.forEach(this::addLetterTab);
               menuItemAddLetter.setEnabled(true);
               statusBar.setDirty(true);

            }
         }));

         //endregion

         //region MenuItem Open 
         file.add(createMenuItem("Open", "Opens a Led Letter Font", 'o', "control O", Icons.OPEN, (e) -> {
            saveIfDirtyAndCloseAll();

            Preferences preferences = Preferences.userRoot().node(getClass().getName());
            final JFileChooser jfc = new JFileChooser(preferences.get(LAST_USED_FOLDER, new File(".").getAbsolutePath()));
            jfc.setFileFilter(LLF_FILE_FILTER);
            if (JFileChooser.APPROVE_OPTION == jfc.showOpenDialog(MainFrame.this)) {
               preferences.put(LAST_USED_FOLDER, jfc.getSelectedFile().getParent());

               try {
                  openLedLetterFont(jfc.getSelectedFile());
                  menuItemAddLetter.setEnabled(true);
               } catch (IOException e1) {
                  e1.printStackTrace();
                  statusBar.setMessageFor(e1.getMessage(), 10, TimeUnit.SECONDS);
               }
            }
         }));
         //endregion

         //region MenuItem Save
         menuItemSave = createMenuItem("Save", "Save the current Led Letter Font", 's', "control S", Icons.SAVE, (e) -> save());
         menuItemSave.setEnabled(false);
         file.add(menuItemSave);
         //endregion

         //region MenuItem CLose All 
         file.add(createMenuItem("Close All", "Close all opened letters", 'C', "control shift W", Icons.CLOSE, e -> saveIfDirtyAndCloseAll()));
         //endregion

         //region MenuItem Exit
         file.addSeparator();
         file.add(createMenuItem("Exit", "Exits the application", 'x', "control Q", Icons.EXIT, (e -> {
            Boolean res = saveIfDirtyAndCloseAll();
            if(res != null) {
               System.exit(0);
            }

         })));
         //endregion
      }
      //endregion

      //region Menu Edit 
      {
         JMenu edit = new JMenu("Edit");
         edit.setMnemonic('E');

         menuBar.add(edit);

         //region MenuItem Add Letter

         menuItemAddLetter = createMenuItem("Add Letter", "Add a new letter to the list", 'A', "control INSERT", Icons.ADD, e -> {
            Set<Character> allLetters = Sets.newLinkedHashSet();
            allLetters.addAll(DEFAULT_CHARACTERS);
            allLetters.addAll(ADDED_LETTER_LIST);

            AddLetterDialog dialog = new AddLetterDialog(MainFrame.this, allLetters);
            dialog.pack();
            dialog.setLocationRelativeTo(MainFrame.this);
            dialog.setVisible(true);

            char selectedLetter = dialog.getSelectedLetter();
            addLetterTab(selectedLetter, false);
         });
         menuItemAddLetter.setEnabled(false);
         edit.add(menuItemAddLetter);

         //endregion
      }
      //endregion

      //region 
      {
         final JMenu options = new JMenu("Options");
         options.setMnemonic('O');
         menuBar.add(options);

         options.add(createCheckboxMenuItem("Change Tab Layout", "Switch tab layout policy", 'T', "control shift T", Icons.TABS, e -> {
            JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
            final int layout;
            if (item.isSelected()) {
               layout = JTabbedPane.WRAP_TAB_LAYOUT;
            } else {
               layout = JTabbedPane.SCROLL_TAB_LAYOUT;
            }
            tabbedPane.setTabLayoutPolicy(layout);
            tabbedPane.revalidate();
         }));

      }

      //endregion

      //region Menu Help
      {
         final JMenu help = new JMenu("Help");
         help.setMnemonic('H');
         menuBar.add(Box.createHorizontalGlue());
         menuBar.add(help);

         help.add(createMenuItem("About", "Display About window", 'a', "control COMMA", Icons.ABOUT, e -> JOptionPane.showMessageDialog(MainFrame.this, "Allows to create/view/modify Led Letter Fonts", "Led Letter Font Creator", JOptionPane.INFORMATION_MESSAGE)));
      }
      //endregion
      setJMenuBar(menuBar);
   }

   private void save() {
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
   }

   private void addLetterTab(char c) {
      addLetterTab(c, true);
   }

   private void addLetterTab(char c, boolean defaultLetter) {
      LedLetterFontDisplay fontDisplay = new LedLetterFontDisplay(c, selectedModel);
      final TabbedPaneIconUpdater updater = new TabbedPaneIconUpdater(tabbedPane, c, tabbedPane.getTabCount());
      fontDisplay.addLedLetterChangedListener(updater);
      JScrollPane scrollPaneFontDisplay = new JScrollPane();
      scrollPaneFontDisplay.getViewport().add(fontDisplay);
      selectedModel.addLedLetterFontDisplayModelListener(new MyLedLetterFontDisplayModelListener(scrollPaneFontDisplay));
      displaysMap.put(c, fontDisplay);
      tabbedPane.addTab(String.format("%s (%d)", c, (int) c), scrollPaneFontDisplay);
      if (!defaultLetter) {
         ADDED_LETTER_LIST.add(c);
      }

   }

   private Boolean saveIfDirtyAndCloseAll() {
      Boolean save = true;
      if (isDirty()) {
         save = doesUserWantToSave();
         if (save == null) {
            return null; // user cancelled
         }
         if (save) {
            save();
         }
      }
      closeAll();
      return save;
   }

   private LedLetterFontDisplayModel askUserModel() {

      final UserModelDialog dialog = new UserModelDialog(this);
      dialog.pack();
      dialog.setLocationRelativeTo(this);
      dialog.setVisible(true);

      final LedLetterFontDisplayModel model = dialog.getModel();
      if (model != null) {
         this.nbCol = model.getGridDimension().width;
         this.nbRow = model.getGridDimension().height;
      }
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
                  final char charValue = (char) Integer.parseInt(matcher.group(2));

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

                  selectedModel = new LedLetterFontDisplayModel(new Dimension(nbCol, nbRow));
                  LedLetterFontDisplay fontDisplay = new LedLetterFontDisplay(charValue, selectedModel);
                  final TabbedPaneIconUpdater updater = new TabbedPaneIconUpdater(tabbedPane, charValue, tabbedPane.getTabCount());
                  fontDisplay.addLedLetterChangedListener(updater);
                  fontDisplay.addLedLetterChangedListener(statusBar); // Make status bar listen to the display dirty status.
                  fontDisplay.setValues(v);

                  JScrollPane scrollPaneFontDisplay = new JScrollPane();
                  scrollPaneFontDisplay.getViewport().add(fontDisplay);
                  selectedModel.addLedLetterFontDisplayModelListener(new MyLedLetterFontDisplayModelListener(scrollPaneFontDisplay));

                  displaysMap.put(charValue, fontDisplay);
                  tabbedPane.addTab(String.format("%s (%d)", charValue, (int) charValue), scrollPaneFontDisplay);
                  fontDisplay.fireNewlyCreated();

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
      List<Character> allLetters = Lists.newArrayList();
      allLetters.addAll(DEFAULT_CHARACTERS);
      allLetters.addAll(ADDED_LETTER_LIST);
      for (int c : allLetters) {
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
         pw.write(String.format("\", %d) /* %s */,\n", c, (char) c));
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

   private JCheckBoxMenuItem createCheckboxMenuItem(String name, String howerText, char mnemonic, String accelerator, Icon icon, ActionListener listener) {
      JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(name);
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

   private void closeAll() {
      while (tabbedPane.getTabCount() > 0) {
         tabbedPane.removeTabAt(0);
      }
      tabbedPane.revalidate();
      menuItemAddLetter.setEnabled(false);
      statusBar.setDirty(false);
   }

   private Boolean doesUserWantToSave() {
      final int result = JOptionPane.showConfirmDialog(this, "You have pending changes\nDo you want to save ?", "You have pending changes !", JOptionPane.YES_NO_CANCEL_OPTION);
      switch (result) {
         case JOptionPane.YES_OPTION:
            return true;
         case JOptionPane.NO_OPTION:
            return false;
         case JOptionPane.CANCEL_OPTION:
         default:
            return null;
      }

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
