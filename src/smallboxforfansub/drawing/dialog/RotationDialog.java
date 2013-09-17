/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RotationDialog.java
 *
 * Created on 1 juin 2011, 09:26:51
 */
package smallboxforfansub.drawing.dialog;

import javax.swing.SpinnerNumberModel;
import drawing.Language;

/**
 *
 * @author The Wingate 2940
 */
public class RotationDialog extends javax.swing.JDialog {
    
    private ButtonPressed bp;
    private SpinnerNumberModel snmAngle;
    private Language localeLanguage = pointsandcurves.PACFrame.getLanguage();
    
    public enum ButtonPressed{
        NONE, OK_BUTTON, CANCEL_BUTTON;
    }

    /** Creates new form RotationDialog */
    public RotationDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        bp = ButtonPressed.NONE;
        snmAngle = new SpinnerNumberModel(0, -360, 360, 1);
        spiAngle.setModel(snmAngle);
        
        jLabel1.setVisible(false);
        
        if(localeLanguage.getValueOf("titleRotation")!=null){setTitle(localeLanguage.getValueOf("titleRotation"));}
        if(localeLanguage.getValueOf("buttonOk")!=null){OK_Button.setText(localeLanguage.getValueOf("buttonOk"));}
        if(localeLanguage.getValueOf("buttonCancel")!=null){Cancel_Button.setText(localeLanguage.getValueOf("buttonCancel"));}
        if(localeLanguage.getValueOf("labelRotation2")!=null){jLabel2.setText(localeLanguage.getValueOf("labelRotation2"));}
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        spiAngle = new javax.swing.JSpinner();
        Cancel_Button = new javax.swing.JButton();
        OK_Button = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setForeground(new java.awt.Color(255, 51, 51));
        jLabel1.setText("<html><i>You have to right-click on the drawing before the opening of this window.");

        jLabel2.setText("Angle of rotation : ");

        Cancel_Button.setText("Cancel");
        Cancel_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cancel_ButtonActionPerformed(evt);
            }
        });

        OK_Button.setText("OK");
        OK_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OK_ButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spiAngle, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(OK_Button, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Cancel_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(spiAngle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Cancel_Button)
                    .addComponent(OK_Button))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OK_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OK_ButtonActionPerformed
        bp = ButtonPressed.OK_BUTTON;
        dispose();
    }//GEN-LAST:event_OK_ButtonActionPerformed

    private void Cancel_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel_ButtonActionPerformed
        bp = ButtonPressed.CANCEL_BUTTON;
        dispose();
    }//GEN-LAST:event_Cancel_ButtonActionPerformed

    public int showDialog(){
        setVisible(true);

        if(bp.equals(ButtonPressed.OK_BUTTON)){
            int angle = snmAngle.getNumber().intValue();
            return angle;
        }else{
            return 0;
        }
    }
    
    public void setLangOKButton(String s){
        OK_Button.setText(s);
    }
    
    public void setLangCancelButton(String s){
        Cancel_Button.setText(s);
    }
    
    public void setLangRedMessage(String s){
        jLabel1.setText(s);
    }
    
    public void setLangMessage(String s){
        jLabel2.setText(s);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                RotationDialog dialog = new RotationDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSpinner spiAngle;
    // End of variables declaration//GEN-END:variables
}
