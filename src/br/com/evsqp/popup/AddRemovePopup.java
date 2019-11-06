package br.com.evsqp.popup;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.jdesktop.application.ResourceMap;

import br.com.evsqp.main.EVApp;

public class AddRemovePopup extends MouseAdapter{

	EVApp controller;
	JPopupMenu menu;
	JMenuItem addMenuItem;
	JMenuItem removeSelectedMenuItem;
	JMenuItem removeAllMenuItem;
	JMenuItem viewMenuItem;	
	JMenuItem exportMenuItem;
	JMenuItem loadLDAMenuItem;
	JList list;
	
	public AddRemovePopup(EVApp evApp) {
		controller = evApp;
		list = controller.getView().getItemList3();
		initComponents();
	}

	private void initComponents() {
		
		ResourceMap resourceMap = controller.getContext().getResourceMap(AddRemovePopup.class);
		addMenuItem = new JMenuItem(resourceMap.getString("addMenuItem.text"));
		removeSelectedMenuItem = new JMenuItem(resourceMap.getString("removeSelectedMenuItem.text"));
		removeAllMenuItem = new JMenuItem(resourceMap.getString("removeAllMenuItem.text"));
		viewMenuItem = new JMenuItem(resourceMap.getString("viewMenuItem.text"));
		exportMenuItem = new JMenuItem(resourceMap.getString("exportMenuItem.text"));
		loadLDAMenuItem = new JMenuItem("Load LDA dir...");
		loadLDAMenuItem.setVisible(false);
		
		menu = new JPopupMenu();
		menu.add(addMenuItem);
		menu.add(removeSelectedMenuItem);
		menu.add(removeAllMenuItem);
		menu.addSeparator();
		menu.add(viewMenuItem);
		menu.addSeparator();
		menu.add(exportMenuItem);
		menu.add(loadLDAMenuItem);
		
	}

	public void mouseReleased(MouseEvent e) {
		if ( e.isPopupTrigger() && e.getClickCount() == 1 )
			showPopup(e);
	}
	
	private void showPopup(MouseEvent e) {
		
		if(list.getModel().getSize()>0) {
			removeAllMenuItem.setEnabled(true);
			exportMenuItem.setEnabled(true);
		} else {
			removeAllMenuItem.setEnabled(false);
			exportMenuItem.setEnabled(false);
		}
		if(list.getSelectedIndices().length>0){
			removeSelectedMenuItem.setEnabled(true);
			viewMenuItem.setEnabled(true);
		} else {
			removeSelectedMenuItem.setEnabled(false);
			viewMenuItem.setEnabled(false);
		}
		
		menu.show(e.getComponent(), e.getX(), e.getY());		
	}

	public void mousePressed(MouseEvent e) {
		if ( e.isPopupTrigger() && e.getClickCount() == 1 )
			showPopup(e);
	}
	
	public void setList(JList list){
		this.list = list;
	}

	public JMenuItem getAddMenuItem() {
		return addMenuItem;
	}

	public void setAddMenuItem(JMenuItem addMenuItem) {
		this.addMenuItem = addMenuItem;
	}

	public JMenuItem getRemoveSelectedMenuItem() {
		return removeSelectedMenuItem;
	}

	public void setRemoveSelectedMenuItem(JMenuItem removeSelectedMenuItem) {
		this.removeSelectedMenuItem = removeSelectedMenuItem;
	}

	public JMenuItem getRemoveAllMenuItem() {
		return removeAllMenuItem;
	}

	public void setRemoveAllMenuItem(JMenuItem removeAllMenuItem) {
		this.removeAllMenuItem = removeAllMenuItem;
	}

	public JMenuItem getViewMenuItem() {
		return viewMenuItem;
	}

	public void setViewMenuItem(JMenuItem viewMenuItem) {
		this.viewMenuItem = viewMenuItem;
	}

	public JMenuItem getExportMenuItem() {
		return exportMenuItem;
	}
	
	public JMenuItem getLoadLDAMenuItem(){
		return loadLDAMenuItem;
	}
	
	
}
