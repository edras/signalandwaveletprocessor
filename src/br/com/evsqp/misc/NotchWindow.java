/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.evsqp.misc;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

/**
 *
 * @author edras
 */
public class NotchWindow extends javax.swing.JFrame {

    /**
     * Creates new form NotchWindow
     */
    public NotchWindow() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFreqField = new javax.swing.JTextField();
        jAddFreqButton = new javax.swing.JToggleButton();
        jRemoveFreqButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jNotchList = new javax.swing.JList<Integer>();
        jApplyButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setResizable(false);

        jAddFreqButton.setText(">");
        jAddFreqButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAddFreqButtonActionPerformed(evt);
            }
        });

        jRemoveFreqButton.setText("<");
        jRemoveFreqButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRemoveFreqButtonActionPerformed(evt);
            }
        });

        DefaultListModel<Integer> model = new DefaultListModel<Integer>();
        model.add(0, 20);
        model.add(1, 60);
        jNotchList.setModel(model);
        jNotchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jNotchList);

        jApplyButton.setText("Apply");
        jLabel1.setText("Frequência");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFreqField, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jAddFreqButton, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                            .addComponent(jRemoveFreqButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jApplyButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jFreqField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jAddFreqButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
                                .addComponent(jApplyButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jRemoveFreqButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jAddFreqButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAddFreqButtonActionPerformed
        if(jFreqField.getText().isEmpty()) return;
        try{
        	addFrequency(Integer.parseInt(jFreqField.getText()));
        } catch (NumberFormatException ex){
        	return;
        }
        		
    }//GEN-LAST:event_jAddFreqButtonActionPerformed

    private void addFrequency(Integer frequency) {
    	boolean found = false;
    	ArrayList<Integer> list = new ArrayList<Integer>();
    	DefaultListModel<Integer> model = (DefaultListModel<Integer>) jNotchList.getModel();
		for (int i = 0; i < model.getSize(); i++) {
			list.add(model.getElementAt(i));
			if(frequency.equals(model.getElementAt(i)))
				found = true;
		}
		if(!found){
			list.add(frequency);
			Collections.sort(list);
			model.removeAllElements();
			for (int i = 0; i < list.size(); i++)
				model.add(i, list.get(i));
		}
	}

	private void jRemoveFreqButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRemoveFreqButtonActionPerformed
		if(jNotchList.isSelectionEmpty()) return;
		int selectedIndex = jNotchList.getSelectedIndex();
		DefaultListModel<Integer> model = (DefaultListModel<Integer>) jNotchList.getModel();
		model.remove(selectedIndex);		
    }//GEN-LAST:event_jRemoveFreqButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NotchWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NotchWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NotchWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NotchWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new NotchWindow().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton jAddFreqButton;
    private javax.swing.JButton jApplyButton;
    private javax.swing.JTextField jFreqField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<Integer> jNotchList;
    private javax.swing.JButton jRemoveFreqButton;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    
    
	public double[] getFrequencies() {
		int size = jNotchList.getModel().getSize();
		double[] frequencies = new double[size];
		for (int i = 0; i < frequencies.length; i++) {
			frequencies[i] = (double)jNotchList.getModel().getElementAt(i);
		}
		return frequencies;
	}

	public JButton getApplyButton() {
		return jApplyButton;		
	}
}
