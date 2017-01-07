/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package drawing;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.json.simple.parser.ParseException;
import pointsandcurves.PACFrame;
import smallboxforfansub.drawing.preview.ImagePreview;
import smallboxforfansub.filter.ImageFilter;
import smallboxforfansub.theme.Theme;
import smallboxforfansub.theme.ThemeCollection;

/**
 *
 * @author The Wingate 2940
 */
public class OptionsDialog2 extends javax.swing.JDialog {

    private ButtonPressed bp = ButtonPressed.NONE;
    private DefaultComboBoxModel dcbmTheme;
    private java.awt.Frame frame;
    private Language localeLanguage = pointsandcurves.PACFrame.getLanguage();
    private DefaultTableModel dtmFonts;
    private DefaultComboBoxModel dcbmFonts;
    private DefaultTableModel dtmTranslate;
    private Map<String,String> translateMap = new HashMap<>();
    private DefaultComboBoxModel dcbmTranslate;
    private DefaultComboBoxModel dcbmChooseForced;
    private String force_ISO = "---";
    private String optTitle4 = "Existing file";
    private String optMessage6 = "Would you really overwrite the existing file ?";
    private String optMessage7 = "The file has been created or updated.";
    private ThemeCollection themecollection;
    private DefaultComboBoxModel dcbmStartWith;
    private String DOCSPATH = "";
    
    public enum ButtonPressed{
        NONE, OK_BUTTON, CANCEL_BUTTON;
    }
    
    public enum Column{
        FONT(0), CORRECTION(1);

        private int id;

        Column(int id){
            this.id = id;
        }

        public int getId(){
            return id;
        }
    }
    
    public enum ColumnTranslate{
        KEY(0), VALUE(1);

        private int id;

        ColumnTranslate(int id){
            this.id = id;
        }

        public int getId(){
            return id;
        }
    }
    
    public enum SWModule{
        WELCOME("welc","Welcome"),
        KARAOKE("kara","Karaoke"),
        CODEEDITOR("code","Code editor"),
        DRAWEDITOR("draw","Drawing editor"),
        ANALYSIS("anal","Analysis");
        
        private String code;
        private String display;

        SWModule(String code, String display){
            this.code = code;
            this.display = display;
        }
        
        public void setDisplay(String display){
            this.display = display;
        }
        
        public String getDisplay(){
            return display;
        }
        
        public String getCode(){
            return code;
        }
        
        @Override
        public String toString(){
            return display;
        }
    }
    
    /**
     * Creates new form OptoionsDialog2
     */
    public OptionsDialog2(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        frame = parent;
        init();
        setLocationRelativeTo(null);
    }
    
    private void init(){        
        TableColumn column;
        String[] fxHead;
        javax.swing.border.TitledBorder tb;
        
        String theme = "Default";
        String backgroundimage = "";
        
        String config_path = getApplicationDirectory() + "\\configuration.json";
        File nf0 = new File(config_path);
        if(nf0.exists()){
            //Le fichier de configuration existe déjà            
            try {
                Configuration conf = new Configuration(config_path);
                conf.load();
                force_ISO = conf.getElement(Configuration.Key.Force_ISO);
                theme = conf.getElement(Configuration.Key.Theme);
                //Laisser ce truc à la fin; risque de plantage
                backgroundimage = conf.getElement(Configuration.Key.Background_Image);
            } catch (IOException | ParseException ex) {
                Logger.getLogger(PACFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        
        
        dcbmTheme = new DefaultComboBoxModel();
        cbTheme.setModel(dcbmTheme);        
        themecollection = new ThemeCollection();
        themecollection.setup();
        for(Theme th : themecollection.getSortedThemes()){
            dcbmTheme.addElement(th);
        }
        dcbmTheme.setSelectedItem(theme);
        
        fxHead = new String[]{"Key", "Value"};        
        dtmTranslate = new DefaultTableModel(null,fxHead){
            Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class};
            boolean[] canEdit = new boolean [] {
                    false, true};
            @Override
            public Class getColumnClass(int columnIndex) {return types [columnIndex];}
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {return canEdit [columnIndex];}
        };        
        tableTranslate.setModel(dtmTranslate);        
        for (int i = 0; i < 2; i++) {
            column = tableTranslate.getColumnModel().getColumn(i);
            switch(i){
                case 0:
                    column.setPreferredWidth(100);
                    column.setIdentifier(ColumnTranslate.KEY.getId());
                    break; //Font
                case 1:
                    column.setPreferredWidth(500);
                    column.setIdentifier(ColumnTranslate.VALUE.getId());
                    break; //Correction
            }
        }        
        dcbmTranslate = new DefaultComboBoxModel();
        cbTranslate.setModel(dcbmTranslate);
        dcbmChooseForced = new DefaultComboBoxModel();
        cbChooseForced.setModel(dcbmChooseForced);
        for(Language.ISO_3166 lg : Language.ISO_3166.values()){
            dcbmTranslate.addElement(lg);
            dcbmChooseForced.addElement(lg);
        }
        dcbmTranslate.setSelectedItem(Language.getDefaultISO_3166());
        setTranslationTable();
        if(force_ISO.equalsIgnoreCase("---")==false){
            cbForceLanguage.setSelected(true);
            cbChooseForced.setEnabled(true);
        }else{
            cbForceLanguage.setSelected(false);
            cbChooseForced.setEnabled(false);
        }
        dcbmChooseForced.setSelectedItem(Language.getFromCode(force_ISO));
        
        tfBGImage.setText(backgroundimage);
        
        if(localeLanguage.getValueOf("titleOPD")!=null){setTitle(localeLanguage.getValueOf("titleOPD"));}
        if(localeLanguage.getValueOf("buttonOk")!=null){OK_Button.setText(localeLanguage.getValueOf("buttonOk"));}
        if(localeLanguage.getValueOf("buttonCancel")!=null){Cancel_Button.setText(localeLanguage.getValueOf("buttonCancel"));}
        if(localeLanguage.getValueOf("buttonChange")!=null){btnChangeBGImage.setText(localeLanguage.getValueOf("buttonChange"));}
        if(localeLanguage.getValueOf("buttonAppTheme")!=null){btnApplyTheme.setText(localeLanguage.getValueOf("buttonAppTheme"));}     
        if(localeLanguage.getValueOf("labelODTheme")!=null){lblTheme.setText(localeLanguage.getValueOf("labelODTheme"));}
        if(localeLanguage.getValueOf("labelODBackImage")!=null){lblBackgroundImage.setText(localeLanguage.getValueOf("labelODBackImage"));}
        if(localeLanguage.getValueOf("labelODInst1")!=null){lblInstruction1.setText(localeLanguage.getValueOf("labelODInst1"));}
        if(localeLanguage.getValueOf("tabODMain")!=null){jTabbedPane1.setTitleAt(0,localeLanguage.getValueOf("tabODMain"));}
        if(localeLanguage.getValueOf("tabODTranslation")!=null){jTabbedPane1.setTitleAt(1,localeLanguage.getValueOf("tabODTranslation"));}
        if(localeLanguage.getValueOf("checkboxForceISO")!=null){cbForceLanguage.setText(localeLanguage.getValueOf("checkboxForceISO"));}
        if(localeLanguage.getValueOf("buttonSave")!=null){btnTranslateSave.setText(localeLanguage.getValueOf("buttonSave"));}
        if(localeLanguage.getValueOf("optpTitle4")!=null){optTitle4 = localeLanguage.getValueOf("optpTitle4");}
        if(localeLanguage.getValueOf("optpMessage6")!=null){optMessage6 = localeLanguage.getValueOf("optpMessage6");}
        if(localeLanguage.getValueOf("optpMessage7")!=null){optMessage7 = localeLanguage.getValueOf("optpMessage7");}
        if(localeLanguage.getValueOf("enumODWelc")!=null){SWModule.WELCOME.setDisplay(localeLanguage.getValueOf("enumODWelc"));}
        if(localeLanguage.getValueOf("enumODKara")!=null){SWModule.KARAOKE.setDisplay(localeLanguage.getValueOf("enumODKara"));}
        if(localeLanguage.getValueOf("enumODCode")!=null){SWModule.CODEEDITOR.setDisplay(localeLanguage.getValueOf("enumODCode"));}
        if(localeLanguage.getValueOf("enumODDraw")!=null){SWModule.DRAWEDITOR.setDisplay(localeLanguage.getValueOf("enumODDraw"));}
        if(localeLanguage.getValueOf("enumODAnal")!=null){SWModule.ANALYSIS.setDisplay(localeLanguage.getValueOf("enumODAnal"));}
        for (int i = 0; i < 2; i++) {
            column = tableTranslate.getColumnModel().getColumn(i);
            switch(i){
                case 0:
                    if(localeLanguage.getValueOf("tableTKey")!=null){
                        column.setHeaderValue(localeLanguage.getValueOf("tableTKey"));
                    }
                    break;
                case 1:
                    if(localeLanguage.getValueOf("tableTValue")!=null){
                        column.setHeaderValue(localeLanguage.getValueOf("tableTValue"));
                    }
                    break;
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fcOptions = new javax.swing.JFileChooser();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        cbForceLanguage = new javax.swing.JCheckBox();
        cbChooseForced = new javax.swing.JComboBox();
        lblTheme = new javax.swing.JLabel();
        cbTheme = new javax.swing.JComboBox();
        btnApplyTheme = new javax.swing.JButton();
        lblBackgroundImage = new javax.swing.JLabel();
        tfBGImage = new javax.swing.JTextField();
        btnChangeBGImage = new javax.swing.JButton();
        lblInstruction1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableTranslate = new javax.swing.JTable();
        cbTranslate = new javax.swing.JComboBox();
        btnTranslateSave = new javax.swing.JButton();
        OK_Button = new javax.swing.JButton();
        Cancel_Button = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        cbForceLanguage.setText("Forcer le langage à :");
        cbForceLanguage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbForceLanguageActionPerformed(evt);
            }
        });

        cbChooseForced.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblTheme.setText("Theme :");

        cbTheme.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnApplyTheme.setText("Appliquer");
        btnApplyTheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyThemeActionPerformed(evt);
            }
        });

        lblBackgroundImage.setText("Image de fond :");

        btnChangeBGImage.setText("Changer...");
        btnChangeBGImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeBGImageActionPerformed(evt);
            }
        });

        lblInstruction1.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        lblInstruction1.setForeground(new java.awt.Color(255, 0, 51));
        lblInstruction1.setText("Pour appliquer la traduction, relancez le logiciel.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTheme, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblBackgroundImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tfBGImage)
                                    .addComponent(cbTheme, 0, 277, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnApplyTheme, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnChangeBGImage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cbForceLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbChooseForced, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 2, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblInstruction1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbForceLanguage)
                    .addComponent(cbChooseForced, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInstruction1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(lblTheme))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbTheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnApplyTheme))))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(lblBackgroundImage))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnChangeBGImage)
                        .addComponent(tfBGImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(160, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Général", jPanel1);

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tableTranslate.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Clé", "Valeur"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableTranslate.setRowHeight(32);
        jScrollPane1.setViewportView(tableTranslate);

        cbTranslate.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnTranslateSave.setText("Sauver");
        btnTranslateSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTranslateSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(cbTranslate, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTranslateSave, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbTranslate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTranslateSave))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Traduction", jPanel6);

        OK_Button.setText("OK");
        OK_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OK_ButtonActionPerformed(evt);
            }
        });

        Cancel_Button.setText("Annuler");
        Cancel_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cancel_ButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(OK_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Cancel_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(OK_Button)
                    .addComponent(Cancel_Button))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OK_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OK_ButtonActionPerformed
        // OK
        bp = ButtonPressed.OK_BUTTON;
        dispose();
    }//GEN-LAST:event_OK_ButtonActionPerformed

    private void Cancel_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel_ButtonActionPerformed
        // Cancel
        bp = ButtonPressed.CANCEL_BUTTON;
        dispose();
    }//GEN-LAST:event_Cancel_ButtonActionPerformed

    private void btnTranslateSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTranslateSaveActionPerformed
        for(int i=0;i<tableTranslate.getRowCount();i++){
            String key = tableTranslate.getValueAt(i, 0).toString();
            String value = tableTranslate.getValueAt(i, 1).toString();
            translateMap.put(key, value);
        }
        Language.ISO_3166 iso = (Language.ISO_3166)cbTranslate.getSelectedItem();
        XmlLangWriter xlw = new XmlLangWriter();
        xlw.setLangMap(translateMap);
        File file = new File(DOCSPATH+iso.getAlpha3()+".lang");
        boolean createFile = true;
        if(file.exists()){
            int a = JOptionPane.showConfirmDialog(this, optMessage6, optTitle4, JOptionPane.YES_NO_OPTION);
            if(a==JOptionPane.NO_OPTION){
                createFile = false;
            }
        }
        if(createFile == true){
            xlw.createLang(file.getAbsolutePath());
            JOptionPane.showMessageDialog(this, optMessage7);
        }
    }//GEN-LAST:event_btnTranslateSaveActionPerformed

    private void btnChangeBGImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeBGImageActionPerformed
        for (FileFilter ff : fcOptions.getChoosableFileFilters()){
            fcOptions.removeChoosableFileFilter(ff);
        }
        fcOptions.addChoosableFileFilter(new ImageFilter());
        fcOptions.setAccessory(new ImagePreview(fcOptions));
        int z = fcOptions.showOpenDialog(this);
        if (z == javax.swing.JFileChooser.APPROVE_OPTION){
            tfBGImage.setText(fcOptions.getSelectedFile().getPath());
        }
    }//GEN-LAST:event_btnChangeBGImageActionPerformed

    private void btnApplyThemeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyThemeActionPerformed
        Theme th = (Theme)dcbmTheme.getSelectedItem();
        pointsandcurves.PACFrame.changeTheme(th);
        javax.swing.SwingUtilities.updateComponentTreeUI(frame);
        javax.swing.SwingUtilities.updateComponentTreeUI(this);
        try {//Force to redraw
            javax.swing.UIManager.setLookAndFeel(new NimbusLookAndFeel());
            javax.swing.SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception exc) {
            System.out.println("Nimbus LookAndFeel not loaded : "+exc);
        }
    }//GEN-LAST:event_btnApplyThemeActionPerformed

    private void cbForceLanguageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbForceLanguageActionPerformed
        if(cbForceLanguage.isSelected()){
            cbChooseForced.setEnabled(true);
        }else{
            cbChooseForced.setEnabled(false);
        }
    }//GEN-LAST:event_cbForceLanguageActionPerformed

    /** <p>Show the dialog.<br />
     * Montre la dialogue.</p> */
    public boolean showDialog(){
        setVisible(true);
        
        if(bp.equals(ButtonPressed.OK_BUTTON)){
            try {
                Configuration conf = new Configuration(getApplicationDirectory() + "\\configuration.json");
                force_ISO = cbForceLanguage.isSelected() ? ((Language.ISO_3166)dcbmChooseForced.getSelectedItem()).getAlpha3() : "---";
                conf.addElement(Configuration.Key.Force_ISO, force_ISO);
                conf.addElement(Configuration.Key.Theme, dcbmTheme.getSelectedItem().toString());
                conf.addElement(Configuration.Key.Background_Image, tfBGImage.getText());
                conf.save();
            } catch (IOException ex) {
                Logger.getLogger(PACFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }else{
            return false;
        }
    }
    
    /** <p>Set the selected theme.<br />
     * Définit le thème sélectionné.</p> */
    public void setSelectedTheme(Theme th){
        dcbmTheme.setSelectedItem(th);
    }
    
    public Theme getSelectedTheme(){
        return (Theme)dcbmTheme.getSelectedItem();
    }
    
    public void setForceLanguage(String language){
        force_ISO = language;
        if(force_ISO.equalsIgnoreCase("---")==false){
            cbForceLanguage.setSelected(true);
            cbChooseForced.setEnabled(true);
        }else{
            cbForceLanguage.setSelected(false);
            cbChooseForced.setEnabled(false);
        }
        dcbmChooseForced.setSelectedItem(Language.getFromCode(force_ISO));
    }
    
    public String getForceLanguage(){
        if(cbForceLanguage.isSelected()==true){
            return ((Language.ISO_3166)cbChooseForced.getSelectedItem()).getAlpha3();
        }else{
            return "---";
        }
    }
    
    public void setBGImage(String BGImage){
        tfBGImage.setText(BGImage);
    }
    
    public String getBGImage(){
        return tfBGImage.getText();
    }
    
    public void setStartWith(String code){
        if(code.equalsIgnoreCase("kara")){
            dcbmStartWith.setSelectedItem(SWModule.KARAOKE);
        }else if(code.equalsIgnoreCase("code")){
            dcbmStartWith.setSelectedItem(SWModule.CODEEDITOR);
        }else if(code.equalsIgnoreCase("draw")){
            dcbmStartWith.setSelectedItem(SWModule.DRAWEDITOR);
        }else if(code.equalsIgnoreCase("anal")){
            dcbmStartWith.setSelectedItem(SWModule.ANALYSIS);
        }else{
            dcbmStartWith.setSelectedItem(SWModule.WELCOME);
        }
    }
    
    public String getStartWith(){
        SWModule swm = (SWModule)dcbmStartWith.getSelectedItem();
        return swm.getCode();
    }
    
    public void setDocsPath(String path){
        DOCSPATH = path;
    }
    
    private Map sortByComparator(Map map) {
        
        List list = new LinkedList(map.entrySet());
 
        //sort list based on comparator
        Collections.sort(list, new Comparator() {
            @Override
             public int compare(Object o1, Object o2) {
	           return ((Comparable) ((Map.Entry) (o1)).getKey())
	           .compareTo(((Map.Entry) (o2)).getKey());
             }
	});
 
        //put sorted list into map again
	Map sortedMap = new LinkedHashMap();
	for (Iterator it = list.iterator(); it.hasNext();) {
	     Map.Entry entry = (Map.Entry)it.next();
	     sortedMap.put(entry.getKey(), entry.getValue());
	}
	return sortedMap;
        
   }
    
    public final void setTranslationTable(){
        translateMap = localeLanguage.getLocaleMap();
        translateMap = sortByComparator(translateMap);
        tableTranslate.removeAll();
        for (String s : translateMap.keySet()){            
            dtmTranslate.addRow(new Object[]{s,translateMap.get(s)});
        }
    }
    
    public void setTranslationTable(Map<String, String> map){
        map = sortByComparator(map);
        tableTranslate.removeAll();
        for (String s : map.keySet()){            
            dtmTranslate.addRow(new Object[]{s,map.get(s)});
        }
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
            java.util.logging.Logger.getLogger(OptionsDialog2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                OptionsDialog2 dialog = new OptionsDialog2(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancel_Button;
    private javax.swing.JButton OK_Button;
    private javax.swing.JButton btnApplyTheme;
    private javax.swing.JButton btnChangeBGImage;
    private javax.swing.JButton btnTranslateSave;
    private javax.swing.JComboBox cbChooseForced;
    private javax.swing.JCheckBox cbForceLanguage;
    private javax.swing.JComboBox cbTheme;
    private javax.swing.JComboBox cbTranslate;
    private javax.swing.JFileChooser fcOptions;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblBackgroundImage;
    private javax.swing.JLabel lblInstruction1;
    private javax.swing.JLabel lblTheme;
    private javax.swing.JTable tableTranslate;
    private javax.swing.JTextField tfBGImage;
    // End of variables declaration//GEN-END:variables
}
