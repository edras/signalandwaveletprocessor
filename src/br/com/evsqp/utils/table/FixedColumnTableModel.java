package br.com.evsqp.utils.table;

import javax.swing.table.AbstractTableModel;

public class FixedColumnTableModel extends AbstractTableModel{
	
	  /**
	 * 
	 */
	private static final long serialVersionUID = -2187199398635917982L;
	
	
	Object[][] data;
	Object[] column;
	int fixedColumnNumber;

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}	
}
