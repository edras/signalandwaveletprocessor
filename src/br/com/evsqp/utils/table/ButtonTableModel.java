package br.com.evsqp.utils.table;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

public class ButtonTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8666940535942564151L;

	private JButton[][]  table;
	private String [] columns;
	
	public ButtonTableModel(){
		
		columns = new String[]{ "Titel1", "Titel2", "Titel3"};
		table = new JButton[][]{{ new JButton(), new JButton(), new JButton()}};
	}
	
	public ButtonTableModel(int rows, int columns){

		this.columns = new String[columns];
		table = new JButton[rows][columns];
		
		JButton button = new JButton("Calculating...");
		button.setVisible(false);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				table[i][j] = button;
			}			
		}
	}

	public String getColumnName(int column) {
		return columns[column];
	}

	public int getRowCount() {
		return table.length;
	}

	public int getColumnCount() {
		return columns.length;
	}

	public Object getValueAt(int row, int column) {
		return table[row][column-1];
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public Class<?> getColumnClass(int column) {
		return getValueAt(0, column).getClass();
	}

	public void setColumnTitel(int column, String columnName) {
		columns[column] = columnName;
	}
	
	public void setValueAt(int row, int column, JButton button){
		table[row][column] = button;
	}
}
