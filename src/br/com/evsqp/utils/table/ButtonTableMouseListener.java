package br.com.evsqp.utils.table;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JTable;

public class ButtonTableMouseListener extends MouseAdapter {

	private final JTable table;

	public ButtonTableMouseListener(JTable table) {
		this.table = table;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int column = table.getColumnModel().getColumnIndexAtX(e.getX());
		int row = e.getY() / table.getRowHeight();

		if (row < table.getRowCount() && row >= 0
				&& column < table.getColumnCount() && column >= 0) {
			Object value = table.getValueAt(row, column);
			if (value instanceof JButton) {
				((JButton) value).doClick();
			}
		}
	}
}
