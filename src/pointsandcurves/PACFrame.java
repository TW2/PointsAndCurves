/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pointsandcurves;

import drawing.Configuration;
import drawing.Language;
import drawing.OptionsDialog2;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import org.json.simple.parser.ParseException;
import smallboxforfansub.drawing.DrawingPanel;
import smallboxforfansub.scripting.ScriptPlugin;
import smallboxforfansub.theme.Theme;
import smallboxforfansub.theme.ThemeCollection;



/**
 *
 * @author The Wingate 2940
 */
public class PACFrame extends javax.swing.JFrame {
    
    private static Language localeLanguage = new Language(Locale.getDefault(), "", "");
    private static ScriptPlugin splug;
    private static Frame frame;
    private ThemeCollection themecollection = new ThemeCollection();
    private String theme = "";    
    
    private BufferedImage backgroundimage;
    private String DOCSPATH = "E:\\Dev\\Projets\\Java\\SmallBoxForFansub\\docs\\";
    DrawingPanel dp;
    JInternalFrame dpFile, dpDraw, dpImage, dpShape, dpMode, dpOps, dpScripts,
            dpHistoric, dpLayers, dpOrnament, dpSheet, dpAssComs;

    /**
     * Creates new form PACFrame
     */
    public PACFrame() {
        initComponents();
        init();        
    }
    
    private void init(){
        frame = this;
        
        //Ouverture d'un fichier de configuration (s'il existe) sinon création
        String config_path = getApplicationDirectory() + "\\configuration.json";
        File nf0 = new File(config_path);
        if(nf0.exists()){
            //Le fichier de configuration existe déjà            
            try {
                Configuration conf = new Configuration(config_path);
                conf.load();
                localeLanguage = new Language(Locale.getDefault(), conf.getElement(Configuration.Key.Force_ISO), "");
                theme = conf.getElement(Configuration.Key.Theme);
                try{
                    backgroundimage = ImageIO.read(new File(Configuration.Key.Background_Image.toString()));
                }catch(Exception e){
                }                
            } catch (IOException | ParseException ex) {
                Logger.getLogger(PACFrame.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }else{
            //Le fichier de configuration n'existe pas, on le crée
            try {
                Configuration conf = new Configuration(config_path);
                conf.addElement(Configuration.Key.Force_ISO, "---");
                conf.addElement(Configuration.Key.Theme, "Default");
                conf.addElement(Configuration.Key.Background_Image, "");
                conf.save();                
            } catch (IOException ex) {
                Logger.getLogger(PACFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // /!\ Using the following method is deprecated
        try {
            javax.swing.UIManager.setLookAndFeel(new NimbusLookAndFeel());
            javax.swing.SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception exc) {
            System.out.println("Nimbus LookAndFeel not loaded : "+exc);
        }
        
        themecollection.setup();
        changeTheme(themecollection.getTheme(theme));
        
        NewsFrame nf = new NewsFrame();
        nf.setVisible(true);
        SplashFrame sf = new SplashFrame();
        sf.setVisible(true);
        
//        UIManager.put("control", Color.blue.brighter());
//        UIManager.put("info", Color.yellow);
//        UIManager.put("nimbusAlertYellow", Color.yellow);
//        UIManager.put("nimbusBase",Color.blue.darker());
//        UIManager.put("nimbusDisabledText", Color.darkGray);
//        UIManager.put("nimbusFocus", Color.lightGray);
//        UIManager.put("nimbusGreen", Color.blue);
//        UIManager.put("nimbusInfoBlue", Color.blue);
//        UIManager.put("nimbusLightBackground", Color.lightGray);
//        UIManager.put("nimbusOrange", Color.orange);
//        UIManager.put("nimbusRed", Color.red);
//        UIManager.put("nimbusSelectedText", Color.lightGray);
//        UIManager.put("nimbusSelectionBackground", Color.darkGray);
//        UIManager.put("text", Color.black);
//        UIManager.put("activeCaption", Color.darkGray);
//        UIManager.put("background", Color.yellow);
//        UIManager.put("controlDkShadow", Color.darkGray);
//        UIManager.put("controlHighlight", Color.cyan);
//        UIManager.put("controlLHighlight", Color.white);
//        UIManager.put("controlShadow", Color.darkGray);
//        UIManager.put("controlText", Color.black);
//        UIManager.put("desktop", Color.blue);
//        UIManager.put("inactiveCaption", Color.blue.darker());
//        UIManager.put("infoText", Color.black);
//        UIManager.put("menu", Color.magenta);
//        UIManager.put("menuText", Color.black);
//        UIManager.put("nimbusBlueGrey", Color.gray);
//        UIManager.put("nimbusBorder", Color.blue.darker());
//        UIManager.put("nimbusSelection", Color.orange);
//        UIManager.put("scrollbar", Color.blue);
//        UIManager.put("textBackground", Color.white);
//        UIManager.put("textForeground", Color.black);
//        UIManager.put("textHighlight", Color.white);
//        UIManager.put("textHighlightText", Color.cyan);
//        UIManager.put("textInactiveText", Color.cyan.darker());
//        SwingUtilities.updateComponentTreeUI(this);
        
        desk = new JDesktopPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if(backgroundimage!=null){
                    g.drawImage(backgroundimage, 0, 0, null);
                }
            }
        };
        mainPanel.add(desk, BorderLayout.CENTER);
        
        //-- dim >> Obtient la taille de l'écran
        //-- gconf >> Obtient la configuration de l'écran
        //-- insets >> Obtient les 'marges' de l'écran
        java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
        java.awt.Dimension dim = toolkit.getScreenSize();
        java.awt.GraphicsConfiguration gconf = java.awt.GraphicsEnvironment
                .getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();
        java.awt.Insets insets = toolkit.getScreenInsets(gconf);
        setSize(dim.width - insets.left - insets.right,
                dim.height - insets.top - insets.bottom);
        
        DOCSPATH = getApplicationDirectory()+"\\docs\\"; //Défaut
        dp = new DrawingPanel(DOCSPATH, this, localeLanguage);
        
        splug = new ScriptPlugin(this);
        splug.setPaths(DOCSPATH, DOCSPATH, DOCSPATH);
        splug.searchForScript(DOCSPATH);
        
        dp.setScriptPlugin(splug);
        dpFile = dp.getIfrFile();
        dpDraw = dp.getIfrDraw();
        dpImage = dp.getIfrImage();
        dpShape = dp.getIfrShape();
        dpMode = dp.getIfrMode();
        dpOps = dp.getIfrOperations();
        dpScripts = dp.getIfrScripts();
        dpHistoric = dp.getIfrHistoric();
        dpLayers = dp.getIfrLayers();
        dpOrnament = dp.getIfrOrnament();
        dpSheet = dp.getIfrSketchpad();
        dpAssComs = dp.getIfrAssCommands();
//        dpHistoric.setLocation(desk.getWidth()-190, 340);
        dpLayers.setLocation(desk.getWidth()-190, 700);
//        dpOrnament.setLocation(desk.getWidth()-190, 90);
        dpHistoric.setLocation(desk.getWidth()-190, 90);
        dpSheet.setSize(desk.getWidth()-400, desk.getHeight()-100);
        dpAssComs.setSize(desk.getWidth()-210, dpAssComs.getHeight());
        desk.add(dpFile);
        desk.add(dpDraw);
        desk.add(dpImage);
        desk.add(dpShape);
//        desk.add(dpMode);
        desk.add(dpOps);
        desk.add(dpScripts);
        desk.add(dpHistoric);
        desk.add(dpLayers);
//        desk.add(dpOrnament);
        desk.add(dpSheet);
        desk.add(dpAssComs);
    }
    
    public String getApplicationDirectory(){
        if(System.getProperty("os.name").equalsIgnoreCase("Mac OS X")){
            java.io.File file = new java.io.File("");
            return file.getAbsolutePath();
        }
        String path = System.getProperty("user.dir");
        if(path.toLowerCase().contains("jre")){
            File f = new File(getClass().getProtectionDomain()
                    .getCodeSource().getLocation().toString()
                    .substring(6));
            path = f.getParent();
        }
        return path;
    }
    
    /** <p>Change the theme.<br />Change le thème.</p> */
    public static void changeTheme(Theme th){
        th.applyTheme(frame);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        desk = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnuConfiguration = new javax.swing.JMenuItem();
        mnuExit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Points And Curves");

        mainPanel.setLayout(new java.awt.BorderLayout());
        mainPanel.add(desk, java.awt.BorderLayout.CENTER);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        jMenu1.setText("File");

        mnuConfiguration.setText("Configuration");
        mnuConfiguration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuConfigurationActionPerformed(evt);
            }
        });
        jMenu1.add(mnuConfiguration);

        mnuExit.setText("Exit");
        mnuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuExitActionPerformed(evt);
            }
        });
        jMenu1.add(mnuExit);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_mnuExitActionPerformed

    private void mnuConfigurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuConfigurationActionPerformed
        OptionsDialog2 od = new OptionsDialog2(frame, true);
        od.showDialog();
    }//GEN-LAST:event_mnuConfigurationActionPerformed

    public static Language getLanguage(){
        return localeLanguage;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PACFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PACFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desk;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuItem mnuConfiguration;
    private javax.swing.JMenuItem mnuExit;
    // End of variables declaration//GEN-END:variables
}
