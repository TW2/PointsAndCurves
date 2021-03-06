/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pointsandcurves;

import com.sun.syndication.io.FeedException;
import drawing.Language;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

/**
 *
 * @author The Wingate 2940
 */
public class NewsFrame extends javax.swing.JFrame {
    
    private Language localeLanguage = PACFrame.getLanguage();

    /**
     * Creates new form NewsFrame
     */
    public NewsFrame() {
        initComponents();
        setLocationRelativeTo(null);
        
        String category = Language.getDefaultISO_3166()==Language.ISO_3166.France ? "Feuille French" : "Feuille English";
        
        AtomRSS ar = null;
        try {
            ar = new AtomRSS("http://redaffaire.wordpress.com/feed/", category);
        } catch (MalformedURLException ex) {
            
        } catch (IOException ex) {
            
        } catch (IllegalArgumentException | FeedException ex) {
            
        }
        
        if(ar!=null){
            JPanel pan = new JPanel(new GridLayout(10, 1));
            int x = 0, y = 0;
            
            List<Map<AtomRSS.InfoType, String>> entries = ar.getEntries();
            
            for(Map<AtomRSS.InfoType, String> sub_entries : entries){
                RSSPanel rssp = new RSSPanel(
                        sub_entries.get(AtomRSS.InfoType.TITLE),
                        sub_entries.get(AtomRSS.InfoType.DESCRIPTION),
                        sub_entries.get(AtomRSS.InfoType.LINK),
                        sub_entries.get(AtomRSS.InfoType.AUTHOR),
                        sub_entries.get(AtomRSS.InfoType.DATE));
                
                rssp.setLocation(x, y);
                rssp.setBackground(Color.white);
                rssp.setOpaque(true);
                rssp.setSize(600, 250);
                rssp.setPreferredSize(new Dimension(600, 250));
                
                pan.add(rssp);
                
                y += rssp.getHeight();
            }
            
            pan.setSize(600, y);
            jScrollPane1.setViewportView(pan);
        }
        
        
        ar = null;
        try {
            ar = new AtomRSS("http://code.google.com/feeds/p/feuille/downloads/basic.html", null);
        } catch (MalformedURLException ex) {
            
        } catch (IOException ex) {
            
        } catch (IllegalArgumentException | FeedException ex) {
            
        }
        
        if(ar!=null){
            JPanel pan = new JPanel(new GridLayout(5, 1));
            int x = 0, y = 0;
            
            List<Map<AtomRSS.InfoType, String>> entries = ar.getEntries();
            
            for(Map<AtomRSS.InfoType, String> sub_entries : entries){
                AtomMiniPanel atom = new AtomMiniPanel(
                        sub_entries.get(AtomRSS.InfoType.TITLE),
                        sub_entries.get(AtomRSS.InfoType.LINK));
                
                atom.setLocation(x, y);
                atom.setBackground(Color.white);
                atom.setOpaque(true);
                atom.setSize(600, 70);
                atom.setPreferredSize(new Dimension(600, 70));
                
                pan.add(atom);
                
                y += atom.getHeight();
            }
            
            pan.setSize(600, y);
            jScrollPane2.setViewportView(pan);
        }
        
        if(localeLanguage.getValueOf("labelWelcomeNews")!=null){
            jLabel1.setText(localeLanguage.getValueOf("labelWelcomeNews"));} 
        if(localeLanguage.getValueOf("labelWelcomeDL")!=null){
            jLabel2.setText(localeLanguage.getValueOf("labelWelcomeDL"));}
        if(localeLanguage.getValueOf("buttonContinue")!=null){
            jButton1.setText(localeLanguage.getValueOf("buttonContinue"));}
        
        pack();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Welcome - Points And Curves");
        setAlwaysOnTop(true);
        setResizable(false);

        jLabel1.setText("<html><h2>Nouvelles :");

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jLabel2.setText("<html><h2>Téléchargements :");

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jButton1.setText("Continuer");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        jLabel3.setText("RSS and Atom by ROME and JDom");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
            .addComponent(jScrollPane1)
            .addComponent(jLabel2)
            .addComponent(jScrollPane2)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jLabel3)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(NewsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NewsFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
