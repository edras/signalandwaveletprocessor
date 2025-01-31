/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DescriptorForm.java
 *
 * Created on 02.08.2011, 12:34:34
 */
package br.com.evsqp.view;

import javax.swing.JFrame;
import javax.swing.JTable;

import br.com.evsqp.utils.table.ButtonTableMouseListener;

/**
 *
 * @author edras
 */
public class DescriptorForm extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1505353692937703804L;
	
	public DescriptorForm(){
		super("Descriptors");
		initComponents();
	}
	
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tableScroll = new javax.swing.JScrollPane();
        resultTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setName("Form"); // NOI18N

        tableScroll.setName("tableScroll"); // NOI18N

        resultTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        resultTable.setName("resultTable"); // NOI18N
        tableScroll.setViewportView(resultTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
            .addComponent(tableScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
            .addComponent(tableScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new DescriptorForm().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable resultTable;
    private javax.swing.JScrollPane tableScroll;
    // End of variables declaration//GEN-END:variables
    
    public void initTable(){
        resultTable.addMouseListener(new ButtonTableMouseListener(resultTable));
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);	
        resultTable.setAutoCreateColumnsFromModel(false);
    }

	public JTable getResultTable() {
		return resultTable;
	}
}
