package com.bnpparibas.grp.ledletter.fontcreator;

import com.bnpparibas.grp.ledletter.fontcreator.icons.Icons;
import com.bnpparibas.grp.ledletter.fontcreator.status.StatusBar;
import com.bnpparibas.grp.ledletter.fontcreator.status.StatusBarFactory;
import com.jtattoo.plaf.acryl.AcrylLookAndFeel;

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
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

/**
 * @author morinb.
 */
public class MainFrame extends JFrame {
   private static final String LAST_USED_FOLDER = "LedLetterFontCreator : LAST_USED_FOLDER";
   public static final FileFilter LLF_FILE_FILTER = new FileFilter() {
      @Override
      public boolean accept(File f) {
         return (f.isDirectory() || f.isHidden()) && f.getAbsolutePath().endsWith(".llf");
      }

      @Override
      public String getDescription() {
         return "(*.llf) Led Letter Font";
      }
   };
   private LedLetterFontDisplay fontDisplay;
   private JScrollPane scrollPaneFontDisplay = new JScrollPane();
   
   private final StatusBar statusBar;

   MainFrame() {
      setTitle("Led Letter Font Creator");
      createMenuBar();
      statusBar = StatusBarFactory.getDefaultStatusBar();

      this.setLayout(new BorderLayout());
      this.add(scrollPaneFontDisplay, BorderLayout.CENTER);

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
            if(isDirty()) {
               if (doesUserWantToSave()) {
                  // save
               }
               scrollPaneFontDisplay.getViewport().remove(fontDisplay);
            }
            
            LedLetterFontDisplayModel model = askUserModel();
            fontDisplay = new LedLetterFontDisplay(model);
            scrollPaneFontDisplay.getViewport().add(fontDisplay);
            model.addLedLetterFontDisplayModelListener(new LedLetterFontDisplayModelListener() {
               @Override
               public void cellDimensionChanged(Dimension oldDimension, Dimension newDimension) {
                  scrollPaneFontDisplay.revalidate();
               }

               @Override
               public void gridThicknessChanged(int oldthickness, int newThickness) {
                  scrollPaneFontDisplay.revalidate();
               }

               @Override
               public void gridDimensionChanged(Dimension oldDimension, Dimension newDimension) {
                  scrollPaneFontDisplay.revalidate();
               }

               @Override
               public void zoomLevelChanged(int oldZoomLevel, int newZoomLevel) {
                  scrollPaneFontDisplay.revalidate();
               }
            });
         }));
         //endregion

         //region MenuItem Open 
         file.add(createMenuItem("Open", "Opens a Led Letter Font", 'o', "control O", Icons.OPEN, (e) -> {
            Preferences preferences = Preferences.userRoot().node(getClass().getName());
            final JFileChooser jfc = new JFileChooser(preferences.get(LAST_USED_FOLDER, new File(".").getAbsolutePath()));
            jfc.setFileFilter(LLF_FILE_FILTER);
            if (JFileChooser.APPROVE_OPTION == jfc.showOpenDialog(MainFrame.this)) {
               preferences.put(LAST_USED_FOLDER, jfc.getSelectedFile().getParent());

               openLedLetterFont(jfc.getSelectedFile());
            }
         }));
         //endregion

         //region MenuItem Save
         file.add(createMenuItem("Save","Save the current Led Letter Font",  's', "control S", Icons.SAVE, (e) -> {
            Preferences preferences = Preferences.userRoot().node(getClass().getName());
            final JFileChooser jfc = new JFileChooser(preferences.get(LAST_USED_FOLDER, new File(".").getAbsolutePath()));
            jfc.setFileFilter(LLF_FILE_FILTER);
            if (JFileChooser.APPROVE_OPTION == jfc.showSaveDialog(MainFrame.this)) {
               preferences.put(LAST_USED_FOLDER, jfc.getSelectedFile().getParent());
               saveLedLetterFont(jfc.getSelectedFile());
            }

         }));
         //endregion

         //region MenuItem Exit
         file.addSeparator();
         file.add(createMenuItem("Exit","Exits the application", 'x', "control Q", Icons.EXIT, (e -> {
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
      return new LedLetterFontDisplayModel(new Dimension(10, 10), new Dimension(7, 7));
   }

   private void openLedLetterFont(File selectedFile) {
      
   }

   private void saveLedLetterFont(File selectedFile) {
      for (boolean[] bools : fontDisplay.getValues()) {
         for (boolean bool : bools) {
            System.out.print(bool ? "o" : " ");
         }
         System.out.println();
         
      }
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
}
