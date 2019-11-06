/*
 * EVView.java
 */

package br.com.evsqp.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.Timer;

import org.jdesktop.application.Action;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.TaskMonitor;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import br.com.evsqp.display.chart.CombinedCategoryPlot;
import br.com.evsqp.display.wavelet.WaveletResultPlot;
import br.com.evsqp.utils.table.ButtonTableModel;
import br.com.evsqp.utils.table.DescriptorButton;
import br.com.evsqp.utils.table.WaveletButton;
import br.com.evsqp.view.DescriptorForm;
import br.com.evsqp.view.WaveletForm;
import cs.jwave.handlers.wavelets.Wavelet;

/**
 * The application's main frame.
 */
public class EVView extends FrameView {
    
    EVApp controller;
	private WaveletForm waveletForm;
	private DescriptorForm descriptorForm;

    public EVView(SingleFrameApplication app) {
        
        super(app);
        controller = (EVApp) app;
        waveletForm = new WaveletForm();
        descriptorForm = new DescriptorForm();

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = controller.getContext().getResourceMap(EVView.class);
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        startButton = new javax.swing.JButton();
        pauseButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        waveletPanel = new javax.swing.JPanel();
        itemScroll = new javax.swing.JScrollPane();
        itemList = new javax.swing.JList();
        viewWaveletButton = new javax.swing.JButton();
        removeAllButton = new javax.swing.JButton();
        removeSelectedButton = new javax.swing.JButton();
        addSelectedButton = new javax.swing.JButton();
        selectAllButton = new javax.swing.JButton();
        selectedItemScroll = new javax.swing.JScrollPane();
        selectedItemList = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        levelsCombo = new javax.swing.JComboBox();
        borderPanel = new javax.swing.JPanel();
        itemScroll1 = new javax.swing.JScrollPane();
        itemList1 = new javax.swing.JList();
        removeAllButton1 = new javax.swing.JButton();
        removeSelectedButton1 = new javax.swing.JButton();
        addSelectedButton1 = new javax.swing.JButton();
        selectAllButton1 = new javax.swing.JButton();
        selectedItemScroll1 = new javax.swing.JScrollPane();
        selectedItemList1 = new javax.swing.JList();
        descriptorPanel = new javax.swing.JPanel();
        itemScroll2 = new javax.swing.JScrollPane();
        itemList2 = new javax.swing.JList();
        removeAllButton2 = new javax.swing.JButton();
        removeSelectedButton2 = new javax.swing.JButton();
        addSelectedButton2 = new javax.swing.JButton();
        selectAllButton2 = new javax.swing.JButton();
        selectedItemScroll2 = new javax.swing.JScrollPane();
        selectedItemList2 = new javax.swing.JList();
        ldaPanel = new javax.swing.JPanel();
        jButton_LDA = new javax.swing.JButton();
        selectedItemScroll3 = new javax.swing.JScrollPane();
        selectedDescriptorLDA = new javax.swing.JList();
        selectedItemScroll4 = new javax.swing.JScrollPane();
        selectedWaveletLDA = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel_combinacoes = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        selectedItemScroll5 = new javax.swing.JScrollPane();
        selectedLevelsLDA = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        loadedClasses = new javax.swing.JList();
        jButton_WeigthsLDA = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        loadedValidation = new javax.swing.JList();
        jButton_TestLDA = new javax.swing.JButton();
        jButton_Distribution = new javax.swing.JButton();
        jButton_ROC = new javax.swing.JButton();
        jText_TestNr = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jButton_CheckValidation = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemList3 = new javax.swing.JList();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        newItemMenu = new javax.swing.JMenuItem();
        loadItemMenu = new javax.swing.JMenuItem();
        exportMenu = new javax.swing.JMenu();
        expDescResultMenuItem = new javax.swing.JMenuItem();
        saveItemMenu = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        waveletResultItemMenu = new javax.swing.JMenuItem();
        descriptorResultItemMenu = new javax.swing.JMenuItem();
        descriptorPlotResultItemMenu = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jToggleButton5 = new javax.swing.JToggleButton();

        mainPanel.setName("mainPanel"); // NOI18N

        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        startButton.setMnemonic('S');
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(br.com.evsqp.main.EVApp.class).getContext().getResourceMap(EVView.class);
        startButton.setText(resourceMap.getString("startButton.text")); // NOI18N
        startButton.setFocusable(false);
        startButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        startButton.setName("startButton"); // NOI18N
        startButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(startButton);

        pauseButton.setMnemonic('P');
        pauseButton.setText(resourceMap.getString("pauseButton.text")); // NOI18N
        pauseButton.setFocusable(false);
        pauseButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pauseButton.setName("pauseButton"); // NOI18N
        pauseButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(pauseButton);

        stopButton.setMnemonic('T');
        stopButton.setText(resourceMap.getString("stopButton.text")); // NOI18N
        stopButton.setFocusable(false);
        stopButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stopButton.setName("stopButton"); // NOI18N
        stopButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(stopButton);

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        waveletPanel.setName("waveletPanel"); // NOI18N

        itemScroll.setName("itemScroll"); // NOI18N

        itemList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        itemList.setName("itemList"); // NOI18N
        itemScroll.setViewportView(itemList);

        viewWaveletButton.setText(resourceMap.getString("viewWaveletButton.text")); // NOI18N
        viewWaveletButton.setName("viewWaveletButton"); // NOI18N

        removeAllButton.setText(resourceMap.getString("removeAllButton.text")); // NOI18N
        removeAllButton.setMaximumSize(new java.awt.Dimension(32, 32));
        removeAllButton.setMinimumSize(new java.awt.Dimension(32, 32));
        removeAllButton.setName("removeAllButton"); // NOI18N
        removeAllButton.setPreferredSize(new java.awt.Dimension(32, 32));

        removeSelectedButton.setText(resourceMap.getString("removeSelectedButton.text")); // NOI18N
        removeSelectedButton.setMaximumSize(new java.awt.Dimension(32, 32));
        removeSelectedButton.setMinimumSize(new java.awt.Dimension(32, 32));
        removeSelectedButton.setName("removeSelectedButton"); // NOI18N
        removeSelectedButton.setPreferredSize(new java.awt.Dimension(32, 32));

        addSelectedButton.setText(resourceMap.getString("addSelectedButton.text")); // NOI18N
        addSelectedButton.setMaximumSize(new java.awt.Dimension(32, 32));
        addSelectedButton.setMinimumSize(new java.awt.Dimension(32, 32));
        addSelectedButton.setName("addSelectedButton"); // NOI18N
        addSelectedButton.setPreferredSize(new java.awt.Dimension(32, 32));

        selectAllButton.setText(resourceMap.getString("selectAllButton.text")); // NOI18N
        selectAllButton.setMaximumSize(new java.awt.Dimension(32, 32));
        selectAllButton.setMinimumSize(new java.awt.Dimension(32, 32));
        selectAllButton.setName("selectAllButton"); // NOI18N
        selectAllButton.setPreferredSize(new java.awt.Dimension(32, 32));

        selectedItemScroll.setName("selectedItemScroll"); // NOI18N

        selectedItemList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        selectedItemList.setName("selectedItemList"); // NOI18N
        selectedItemScroll.setViewportView(selectedItemList);

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        levelsCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        levelsCombo.setName("levelsCombo"); // NOI18N

        javax.swing.GroupLayout waveletPanelLayout = new javax.swing.GroupLayout(waveletPanel);
        waveletPanel.setLayout(waveletPanelLayout);
        waveletPanelLayout.setHorizontalGroup(
            waveletPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(waveletPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(waveletPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(viewWaveletButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(itemScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(waveletPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(removeAllButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeSelectedButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addSelectedButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectAllButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(waveletPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(waveletPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(levelsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(selectedItemScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(92, 92, 92))
        );
        waveletPanelLayout.setVerticalGroup(
            waveletPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(waveletPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(waveletPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(waveletPanelLayout.createSequentialGroup()
                        .addComponent(itemScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(viewWaveletButton))
                    .addGroup(waveletPanelLayout.createSequentialGroup()
                        .addGroup(waveletPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(waveletPanelLayout.createSequentialGroup()
                                .addComponent(selectAllButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addSelectedButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(removeSelectedButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(removeAllButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(selectedItemScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(waveletPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(levelsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(173, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("waveletPanel.TabConstraints.tabTitle"), waveletPanel); // NOI18N

        borderPanel.setName("borderPanel"); // NOI18N

        itemScroll1.setName("itemScroll1"); // NOI18N

        itemList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        itemList1.setName("itemList1"); // NOI18N
        itemScroll1.setViewportView(itemList1);

        removeAllButton1.setText(resourceMap.getString("removeAllButton1.text")); // NOI18N
        removeAllButton1.setMaximumSize(new java.awt.Dimension(32, 32));
        removeAllButton1.setMinimumSize(new java.awt.Dimension(32, 32));
        removeAllButton1.setName("removeAllButton1"); // NOI18N
        removeAllButton1.setPreferredSize(new java.awt.Dimension(32, 32));

        removeSelectedButton1.setText(resourceMap.getString("removeSelectedButton1.text")); // NOI18N
        removeSelectedButton1.setMaximumSize(new java.awt.Dimension(32, 32));
        removeSelectedButton1.setMinimumSize(new java.awt.Dimension(32, 32));
        removeSelectedButton1.setName("removeSelectedButton1"); // NOI18N
        removeSelectedButton1.setPreferredSize(new java.awt.Dimension(32, 32));

        addSelectedButton1.setText(resourceMap.getString("addSelectedButton1.text")); // NOI18N
        addSelectedButton1.setMaximumSize(new java.awt.Dimension(32, 32));
        addSelectedButton1.setMinimumSize(new java.awt.Dimension(32, 32));
        addSelectedButton1.setName("addSelectedButton1"); // NOI18N
        addSelectedButton1.setPreferredSize(new java.awt.Dimension(32, 32));

        selectAllButton1.setText(resourceMap.getString("selectAllButton1.text")); // NOI18N
        selectAllButton1.setMaximumSize(new java.awt.Dimension(32, 32));
        selectAllButton1.setMinimumSize(new java.awt.Dimension(32, 32));
        selectAllButton1.setName("selectAllButton1"); // NOI18N
        selectAllButton1.setPreferredSize(new java.awt.Dimension(32, 32));

        selectedItemScroll1.setName("selectedItemScroll1"); // NOI18N

        selectedItemList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        selectedItemList1.setName("selectedItemList1"); // NOI18N
        selectedItemScroll1.setViewportView(selectedItemList1);

        javax.swing.GroupLayout borderPanelLayout = new javax.swing.GroupLayout(borderPanel);
        borderPanel.setLayout(borderPanelLayout);
        borderPanelLayout.setHorizontalGroup(
            borderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(borderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(itemScroll1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(borderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(removeAllButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeSelectedButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addSelectedButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectAllButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addComponent(selectedItemScroll1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(92, Short.MAX_VALUE))
        );
        borderPanelLayout.setVerticalGroup(
            borderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(borderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(borderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(selectedItemScroll1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemScroll1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(borderPanelLayout.createSequentialGroup()
                        .addComponent(selectAllButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addSelectedButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeSelectedButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeAllButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(205, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("borderPanel.TabConstraints.tabTitle"), borderPanel); // NOI18N

        descriptorPanel.setName("descriptorPanel"); // NOI18N

        itemScroll2.setName("itemScroll2"); // NOI18N

        itemList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        itemList2.setName("itemList2"); // NOI18N
        itemScroll2.setViewportView(itemList2);

        removeAllButton2.setText(resourceMap.getString("removeAllButton2.text")); // NOI18N
        removeAllButton2.setMaximumSize(new java.awt.Dimension(32, 32));
        removeAllButton2.setMinimumSize(new java.awt.Dimension(32, 32));
        removeAllButton2.setName("removeAllButton2"); // NOI18N
        removeAllButton2.setPreferredSize(new java.awt.Dimension(32, 32));

        removeSelectedButton2.setText(resourceMap.getString("removeSelectedButton2.text")); // NOI18N
        removeSelectedButton2.setMaximumSize(new java.awt.Dimension(32, 32));
        removeSelectedButton2.setMinimumSize(new java.awt.Dimension(32, 32));
        removeSelectedButton2.setName("removeSelectedButton2"); // NOI18N
        removeSelectedButton2.setPreferredSize(new java.awt.Dimension(32, 32));

        addSelectedButton2.setText(resourceMap.getString("addSelectedButton2.text")); // NOI18N
        addSelectedButton2.setMaximumSize(new java.awt.Dimension(32, 32));
        addSelectedButton2.setMinimumSize(new java.awt.Dimension(32, 32));
        addSelectedButton2.setName("addSelectedButton2"); // NOI18N
        addSelectedButton2.setPreferredSize(new java.awt.Dimension(32, 32));

        selectAllButton2.setText(resourceMap.getString("selectAllButton2.text")); // NOI18N
        selectAllButton2.setMaximumSize(new java.awt.Dimension(32, 32));
        selectAllButton2.setMinimumSize(new java.awt.Dimension(32, 32));
        selectAllButton2.setName("selectAllButton2"); // NOI18N
        selectAllButton2.setPreferredSize(new java.awt.Dimension(32, 32));

        selectedItemScroll2.setName("selectedItemScroll2"); // NOI18N

        selectedItemList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        selectedItemList2.setName("selectedItemList2"); // NOI18N
        selectedItemScroll2.setViewportView(selectedItemList2);

        javax.swing.GroupLayout descriptorPanelLayout = new javax.swing.GroupLayout(descriptorPanel);
        descriptorPanel.setLayout(descriptorPanelLayout);
        descriptorPanelLayout.setHorizontalGroup(
            descriptorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(descriptorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(itemScroll2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(descriptorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(removeAllButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeSelectedButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addSelectedButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectAllButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addComponent(selectedItemScroll2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(92, Short.MAX_VALUE))
        );
        descriptorPanelLayout.setVerticalGroup(
            descriptorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(descriptorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(descriptorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(selectedItemScroll2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemScroll2, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(descriptorPanelLayout.createSequentialGroup()
                        .addComponent(selectAllButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addSelectedButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeSelectedButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeAllButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(205, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("descriptorPanel.TabConstraints.tabTitle"), descriptorPanel); // NOI18N

        ldaPanel.setName("ldaPanel"); // NOI18N

        jButton_LDA.setText(resourceMap.getString("jButton_LDA.text")); // NOI18N
        jButton_LDA.setName("jButton_LDA"); // NOI18N

        selectedItemScroll3.setName("selectedItemScroll3"); // NOI18N

        selectedDescriptorLDA.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        selectedDescriptorLDA.setToolTipText(resourceMap.getString("selectedDescriptorLDA.toolTipText")); // NOI18N
        selectedDescriptorLDA.setName("selectedDescriptorLDA"); // NOI18N
        selectedItemScroll3.setViewportView(selectedDescriptorLDA);

        selectedItemScroll4.setName("selectedItemScroll4"); // NOI18N

        selectedWaveletLDA.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        selectedWaveletLDA.setToolTipText(resourceMap.getString("selectedWaveletLDA.toolTipText")); // NOI18N
        selectedWaveletLDA.setName("selectedWaveletLDA"); // NOI18N
        selectedItemScroll4.setViewportView(selectedWaveletLDA);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel_combinacoes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_combinacoes.setText(resourceMap.getString("jLabel_combinacoes.text")); // NOI18N
        jLabel_combinacoes.setName("jLabel_combinacoes"); // NOI18N

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        selectedItemScroll5.setName("selectedItemScroll5"); // NOI18N

        selectedLevelsLDA.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        selectedLevelsLDA.setToolTipText(resourceMap.getString("selectedLevelsLDA.toolTipText")); // NOI18N
        selectedLevelsLDA.setName("selectedLevelsLDA"); // NOI18N
        selectedItemScroll5.setViewportView(selectedLevelsLDA);

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        loadedClasses.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        loadedClasses.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        loadedClasses.setToolTipText(resourceMap.getString("loadedClasses.toolTipText")); // NOI18N
        loadedClasses.setName("loadedClasses"); // NOI18N
        jScrollPane2.setViewportView(loadedClasses);

        jButton_WeigthsLDA.setText(resourceMap.getString("jButton_WeigthsLDA.text")); // NOI18N
        jButton_WeigthsLDA.setName("jButton_WeigthsLDA"); // NOI18N

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        loadedValidation.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        loadedValidation.setName("loadedValidation"); // NOI18N
        jScrollPane3.setViewportView(loadedValidation);

        jButton_TestLDA.setText(resourceMap.getString("jButton_TestLDA.text")); // NOI18N
        jButton_TestLDA.setName("jButton_TestLDA"); // NOI18N

        jButton_Distribution.setText(resourceMap.getString("jButton_Distribution.text")); // NOI18N
        jButton_Distribution.setName("jButton_Distribution"); // NOI18N

        jButton_ROC.setText(resourceMap.getString("jButton_ROC.text")); // NOI18N
        jButton_ROC.setName("jButton_ROC"); // NOI18N

        jText_TestNr.setText(resourceMap.getString("jText_TestNr.text")); // NOI18N
        jText_TestNr.setName("jText_TestNr"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jButton_CheckValidation.setText(resourceMap.getString("jButton_CheckValidation.text")); // NOI18N
        jButton_CheckValidation.setName("jButton_CheckValidation"); // NOI18N

        javax.swing.GroupLayout ldaPanelLayout = new javax.swing.GroupLayout(ldaPanel);
        ldaPanel.setLayout(ldaPanelLayout);
        ldaPanelLayout.setHorizontalGroup(
            ldaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ldaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ldaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(ldaPanelLayout.createSequentialGroup()
                        .addGroup(ldaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                            .addComponent(jScrollPane3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ldaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(ldaPanelLayout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(ldaPanelLayout.createSequentialGroup()
                                .addComponent(selectedItemScroll4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(selectedItemScroll5)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ldaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                            .addComponent(selectedItemScroll3)))
                    .addGroup(ldaPanelLayout.createSequentialGroup()
                        .addGroup(ldaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(ldaPanelLayout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jText_TestNr))
                            .addComponent(jButton_TestLDA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton_LDA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ldaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel_combinacoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton_CheckValidation, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ldaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton_Distribution, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                            .addComponent(jButton_ROC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton_WeigthsLDA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ldaPanelLayout.setVerticalGroup(
            ldaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ldaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ldaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ldaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel4)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ldaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(selectedItemScroll5, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(selectedItemScroll4, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ldaPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(selectedItemScroll3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ldaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_LDA)
                    .addComponent(jButton_Distribution)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ldaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ldaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton_TestLDA)
                        .addComponent(jButton_ROC))
                    .addComponent(jLabel_combinacoes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ldaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_WeigthsLDA)
                    .addGroup(ldaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jText_TestNr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addComponent(jButton_CheckValidation)))
                .addContainerGap(60, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("ldaPanel.TabConstraints.tabTitle"), ldaPanel); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        itemList3.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        itemList3.setToolTipText(resourceMap.getString("itemList3.toolTipText")); // NOI18N
        itemList3.setName("itemList3"); // NOI18N
        jScrollPane1.setViewportView(itemList3);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1)))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
            .addComponent(jScrollPane1)
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setMnemonic('F');
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        newItemMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newItemMenu.setMnemonic('N');
        newItemMenu.setText(resourceMap.getString("newItemMenu.text")); // NOI18N
        newItemMenu.setName("newItemMenu"); // NOI18N
        fileMenu.add(newItemMenu);

        loadItemMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        loadItemMenu.setMnemonic('L');
        loadItemMenu.setText(resourceMap.getString("loadItemMenu.text")); // NOI18N
        loadItemMenu.setName("loadItemMenu"); // NOI18N
        fileMenu.add(loadItemMenu);

        exportMenu.setMnemonic('x');
        exportMenu.setText(resourceMap.getString("exportMenu.text")); // NOI18N
        exportMenu.setToolTipText(resourceMap.getString("exportMenu.toolTipText")); // NOI18N
        exportMenu.setName("exportMenu"); // NOI18N
        exportMenu.setOpaque(true);

        expDescResultMenuItem.setMnemonic('r');
        expDescResultMenuItem.setText(resourceMap.getString("expDescResultMenuItem.text")); // NOI18N
        expDescResultMenuItem.setName("expDescResultMenuItem"); // NOI18N
        exportMenu.add(expDescResultMenuItem);

        fileMenu.add(exportMenu);

        saveItemMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveItemMenu.setMnemonic('S');
        saveItemMenu.setText(resourceMap.getString("saveItemMenu.text")); // NOI18N
        saveItemMenu.setName("saveItemMenu"); // NOI18N
        fileMenu.add(saveItemMenu);

        saveAsMenuItem.setMnemonic('A');
        saveAsMenuItem.setText(resourceMap.getString("saveAsMenuItem.text")); // NOI18N
        saveAsMenuItem.setName("saveAsMenuItem"); // NOI18N
        fileMenu.add(saveAsMenuItem);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(br.com.evsqp.main.EVApp.class).getContext().getActionMap(EVView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        viewMenu.setMnemonic('V');
        viewMenu.setText(resourceMap.getString("viewMenu.text")); // NOI18N
        viewMenu.setName("viewMenu"); // NOI18N

        waveletResultItemMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        waveletResultItemMenu.setMnemonic('W');
        waveletResultItemMenu.setText(resourceMap.getString("waveletResultItemMenu.text")); // NOI18N
        waveletResultItemMenu.setName("waveletResultItemMenu"); // NOI18N
        viewMenu.add(waveletResultItemMenu);

        descriptorResultItemMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        descriptorResultItemMenu.setMnemonic('D');
        descriptorResultItemMenu.setText(resourceMap.getString("descriptorResultItemMenu.text")); // NOI18N
        descriptorResultItemMenu.setName("descriptorResultItemMenu"); // NOI18N
        viewMenu.add(descriptorResultItemMenu);

        descriptorPlotResultItemMenu.setText(resourceMap.getString("descriptorPlotResultItemMenu.text")); // NOI18N
        descriptorPlotResultItemMenu.setName("descriptorPlotResultItemMenu"); // NOI18N
        viewMenu.add(descriptorPlotResultItemMenu);

        menuBar.add(viewMenu);

        helpMenu.setMnemonic('H');
        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setMnemonic('A');
        aboutMenuItem.setText(resourceMap.getString("aboutMenuItem.text")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 429, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        jToggleButton5.setText(resourceMap.getString("jToggleButton5.text")); // NOI18N
        jToggleButton5.setName("jToggleButton5"); // NOI18N

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JButton addSelectedButton;
    private javax.swing.JButton addSelectedButton1;
    private javax.swing.JButton addSelectedButton2;
    private javax.swing.JPanel borderPanel;
    private javax.swing.JPanel descriptorPanel;
    private javax.swing.JMenuItem descriptorPlotResultItemMenu;
    private javax.swing.JMenuItem descriptorResultItemMenu;
    private javax.swing.JMenuItem expDescResultMenuItem;
    private javax.swing.JMenu exportMenu;
    private javax.swing.JList itemList;
    private javax.swing.JList itemList1;
    private javax.swing.JList itemList2;
    private javax.swing.JList itemList3;
    private javax.swing.JScrollPane itemScroll;
    private javax.swing.JScrollPane itemScroll1;
    private javax.swing.JScrollPane itemScroll2;
    private javax.swing.JButton jButton_CheckValidation;
    private javax.swing.JButton jButton_Distribution;
    private javax.swing.JButton jButton_LDA;
    private javax.swing.JButton jButton_ROC;
    private javax.swing.JButton jButton_TestLDA;
    private javax.swing.JButton jButton_WeigthsLDA;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel_combinacoes;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jText_TestNr;
    private javax.swing.JToggleButton jToggleButton5;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel ldaPanel;
    private javax.swing.JComboBox levelsCombo;
    private javax.swing.JMenuItem loadItemMenu;
    private javax.swing.JList loadedClasses;
    private javax.swing.JList loadedValidation;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem newItemMenu;
    private javax.swing.JButton pauseButton;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton removeAllButton;
    private javax.swing.JButton removeAllButton1;
    private javax.swing.JButton removeAllButton2;
    private javax.swing.JButton removeSelectedButton;
    private javax.swing.JButton removeSelectedButton1;
    private javax.swing.JButton removeSelectedButton2;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveItemMenu;
    private javax.swing.JButton selectAllButton;
    private javax.swing.JButton selectAllButton1;
    private javax.swing.JButton selectAllButton2;
    private javax.swing.JList selectedDescriptorLDA;
    private javax.swing.JList selectedItemList;
    private javax.swing.JList selectedItemList1;
    private javax.swing.JList selectedItemList2;
    private javax.swing.JScrollPane selectedItemScroll;
    private javax.swing.JScrollPane selectedItemScroll1;
    private javax.swing.JScrollPane selectedItemScroll2;
    private javax.swing.JScrollPane selectedItemScroll3;
    private javax.swing.JScrollPane selectedItemScroll4;
    private javax.swing.JScrollPane selectedItemScroll5;
    private javax.swing.JList selectedLevelsLDA;
    private javax.swing.JList selectedWaveletLDA;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JButton stopButton;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JButton viewWaveletButton;
    private javax.swing.JPanel waveletPanel;
    private javax.swing.JMenuItem waveletResultItemMenu;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;


    public JButton getAddSelectedButton() {
        return addSelectedButton;
    }

    public void setAddSelectedButton(JButton addSelectedButton) {
        this.addSelectedButton = addSelectedButton;
    }

    public JButton getAddSelectedButton1() {
        return addSelectedButton1;
    }

    public void setAddSelectedButton1(JButton addSelectedButton1) {
        this.addSelectedButton1 = addSelectedButton1;
    }

    public JButton getAddSelectedButton2() {
        return addSelectedButton2;
    }

    public void setAddSelectedButton2(JButton addSelectedButton2) {
        this.addSelectedButton2 = addSelectedButton2;
    }

    public JPanel getBorderPanel() {
        return borderPanel;
    }

    public void setBorderPanel(JPanel borderPanel) {
        this.borderPanel = borderPanel;
    }

    public int getBusyIconIndex() {
        return busyIconIndex;
    }

    public void setBusyIconIndex(int busyIconIndex) {
        this.busyIconIndex = busyIconIndex;
    }

    public EVApp getController() {
        return controller;
    }

    public void setController(EVApp controller) {
        this.controller = controller;
    }

    public JPanel getDescriptorPanel() {
        return descriptorPanel;
    }

    public void setDescriptorPanel(JPanel descriptorPanel) {
        this.descriptorPanel = descriptorPanel;
    }

    public JMenuItem getDescriptorResultItemMenu() {
        return descriptorResultItemMenu;
    }
    
    public JMenuItem getDescriptorPlotResult() {
		return descriptorPlotResultItemMenu;
	}
    
    public JLabel getLabelCombinacoes(){
    	return jLabel_combinacoes;
    }

    public void setDescriptorResultItemMenu(JMenuItem descriptorResultItemMenu) {
        this.descriptorResultItemMenu = descriptorResultItemMenu;
    }

    public JList getItemList() {
        return itemList;
    }

    public void setItemList(JList itemList) {
        this.itemList = itemList;
    }

    public JList getItemList1() {
        return itemList1;
    }

    public void setItemList1(JList itemList1) {
        this.itemList1 = itemList1;
    }

    public JList getItemList2() {
        return itemList2;
    }

    public void setItemList2(JList itemList2) {
        this.itemList2 = itemList2;
    }

    public JList getItemList3() {
        return itemList3;
    }

    public void setItemList3(JList itemList3) {
        this.itemList3 = itemList3;
    }

    public JScrollPane getItemScroll() {
        return itemScroll;
    }

    public void setItemScroll(JScrollPane itemScroll) {
        this.itemScroll = itemScroll;
    }

    public JScrollPane getItemScroll1() {
        return itemScroll1;
    }

    public void setItemScroll1(JScrollPane itemScroll1) {
        this.itemScroll1 = itemScroll1;
    }

    public JScrollPane getItemScroll2() {
        return itemScroll2;
    }

    public void setItemScroll2(JScrollPane itemScroll2) {
        this.itemScroll2 = itemScroll2;
    }

    public JLabel getjLabel1() {
        return jLabel1;
    }

    public void setjLabel1(JLabel jLabel1) {
        this.jLabel1 = jLabel1;
    }

    public JScrollPane getjScrollPane1() {
        return jScrollPane1;
    }

    public void setjScrollPane1(JScrollPane jScrollPane1) {
        this.jScrollPane1 = jScrollPane1;
    }

    public JTabbedPane getjTabbedPane1() {
        return jTabbedPane1;
    }

    public void setjTabbedPane1(JTabbedPane jTabbedPane1) {
        this.jTabbedPane1 = jTabbedPane1;
    }

    public JToolBar getjToolBar1() {
        return jToolBar1;
    }

    public void setjToolBar1(JToolBar jToolBar1) {
        this.jToolBar1 = jToolBar1;
    }

    public JComboBox getLevelsCombo() {
        return levelsCombo;
    }

    public void setLevelsCombo(JComboBox levelsCombo) {
        this.levelsCombo = levelsCombo;
    }

    public JMenuItem getLoadItemMenu() {
        return loadItemMenu;
    }

    public void setLoadItemMenu(JMenuItem loadItemMenu) {
        this.loadItemMenu = loadItemMenu;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JMenuItem getNewItemMenu() {
        return newItemMenu;
    }

    public void setNewItemMenu(JMenuItem newItemMenu) {
        this.newItemMenu = newItemMenu;
    }

    public JButton getPauseButton() {
        return pauseButton;
    }

    public void setPauseButton(JButton pauseButton) {
        this.pauseButton = pauseButton;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public JButton getRemoveAllButton() {
        return removeAllButton;
    }

    public void setRemoveAllButton(JButton removeAllButton) {
        this.removeAllButton = removeAllButton;
    }

    public JButton getRemoveAllButton1() {
        return removeAllButton1;
    }

    public void setRemoveAllButton1(JButton removeAllButton1) {
        this.removeAllButton1 = removeAllButton1;
    }

    public JButton getRemoveAllButton2() {
        return removeAllButton2;
    }

    public void setRemoveAllButton2(JButton removeAllButton2) {
        this.removeAllButton2 = removeAllButton2;
    }

    public JButton getRemoveSelectedButton() {
        return removeSelectedButton;
    }

    public void setRemoveSelectedButton(JButton removeSelectedButton) {
        this.removeSelectedButton = removeSelectedButton;
    }

    public JButton getRemoveSelectedButton1() {
        return removeSelectedButton1;
    }

    public void setRemoveSelectedButton1(JButton removeSelectedButton1) {
        this.removeSelectedButton1 = removeSelectedButton1;
    }

    public JButton getRemoveSelectedButton2() {
        return removeSelectedButton2;
    }

    public void setRemoveSelectedButton2(JButton removeSelectedButton2) {
        this.removeSelectedButton2 = removeSelectedButton2;
    }

    public JMenuItem getSaveItemMenu() {
        return saveItemMenu;
    }

    public void setSaveItemMenu(JMenuItem saveItemMenu) {
        this.saveItemMenu = saveItemMenu;
    }

    public JButton getSelectAllButton() {
        return selectAllButton;
    }

    public void setSelectAllButton(JButton selectAllButton) {
        this.selectAllButton = selectAllButton;
    }

    public JButton getSelectAllButton1() {
        return selectAllButton1;
    }

    public void setSelectAllButton1(JButton selectAllButton1) {
        this.selectAllButton1 = selectAllButton1;
    }

    public JButton getSelectAllButton2() {
        return selectAllButton2;
    }

    public void setSelectAllButton2(JButton selectAllButton2) {
        this.selectAllButton2 = selectAllButton2;
    }

    public JList getSelectedItemList() {
        return selectedItemList;
    }

    public void setSelectedItemList(JList selectedItemList) {
        this.selectedItemList = selectedItemList;
    }

    public JList getSelectedItemList1() {
        return selectedItemList1;
    }

    public void setSelectedItemList1(JList selectedItemList1) {
        this.selectedItemList1 = selectedItemList1;
    }

    public JList getSelectedItemList2() {
        return selectedItemList2;
    }

    public void setSelectedItemList2(JList selectedItemList2) {
        this.selectedItemList2 = selectedItemList2;
    }

    public JScrollPane getSelectedItemScroll() {
        return selectedItemScroll;
    }

    public void setSelectedItemScroll(JScrollPane selectedItemScroll) {
        this.selectedItemScroll = selectedItemScroll;
    }

    public JScrollPane getSelectedItemScroll1() {
        return selectedItemScroll1;
    }

    public void setSelectedItemScroll1(JScrollPane selectedItemScroll1) {
        this.selectedItemScroll1 = selectedItemScroll1;
    }

    public JScrollPane getSelectedItemScroll2() {
        return selectedItemScroll2;
    }

    public void setSelectedItemScroll2(JScrollPane selectedItemScroll2) {
        this.selectedItemScroll2 = selectedItemScroll2;
    }

    public JButton getStartButton() {
        return startButton;
    }

    public void setStartButton(JButton startButton) {
        this.startButton = startButton;
    }
    
    public JButton getLDAButton(){
    	return jButton_LDA;
    }
    
    public JList getLoadedClasses(){
    	return loadedClasses;
    }
    
    public JList getLoadedValidation() {
		return loadedValidation;
	}
    
    public JList getSelectedWaveletLDA(){
    	return selectedWaveletLDA;
    }
    
    public JList getSelectedDescriptorLDA(){
    	return selectedDescriptorLDA;
    }
    
    public JList getSelectedLevelsLDA(){
    	return selectedLevelsLDA;
    }
    
    public JLabel getStatusAnimationLabel() {
        return statusAnimationLabel;
    }

    public void setStatusAnimationLabel(JLabel statusAnimationLabel) {
        this.statusAnimationLabel = statusAnimationLabel;
    }

    public JLabel getStatusMessageLabel() {
        return statusMessageLabel;
    }

    public void setStatusMessageLabel(JLabel statusMessageLabel) {
        this.statusMessageLabel = statusMessageLabel;
    }

    public JPanel getStatusPanel() {
        return statusPanel;
    }

    public void setStatusPanel(JPanel statusPanel) {
        this.statusPanel = statusPanel;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public void setStopButton(JButton stopButton) {
        this.stopButton = stopButton;
    }

    public JMenu getViewMenu() {
        return viewMenu;
    }

    public void setViewMenu(JMenu viewMenu) {
        this.viewMenu = viewMenu;
    }

    public JButton getViewWaveletButton() {
        return viewWaveletButton;
    }

    public void setViewWaveletButton(JButton viewWaveletButton) {
        this.viewWaveletButton = viewWaveletButton;
    }

    public JPanel getWaveletPanel() {
        return waveletPanel;
    }

    public void setWaveletPanel(JPanel waveletPanel) {
        this.waveletPanel = waveletPanel;
    }

    public JMenuItem getWaveletResultItemMenu() {
        return waveletResultItemMenu;
    }

    public void setWaveletResultItemMenu(JMenuItem waveletResultItemMenu) {
        this.waveletResultItemMenu = waveletResultItemMenu;
    }

    public JMenuItem getAboutMenuItem() {
        return aboutMenuItem;
    }

    public void setAboutMenuItem(JMenuItem aboutMenuItem) {
        this.aboutMenuItem = aboutMenuItem;
    }

	public void showWaveletResult(WaveletButton wb) {
		WaveletResultPlot wPlot = new WaveletResultPlot();
		wPlot.setTitel(wb.getWavelet()+"("+wb.getSignal()+")"+"_"+Wavelet.bName[wb.getBoundary()]);
		wPlot.setResult(wb.getResult());
		wPlot.setSignal(controller.getModel().getReader(wb.getSignal()).getArray());
		wPlot.setAbsolute(true);
		wPlot.setNormalized(false);
		wPlot.showDetails();		
	}
	
	public void showDescriptorPlotResult() {

		ButtonTableModel dmodel = controller.getModel().getDescriptorTableModel();
        final XYLineAndShapeRenderer lineRenderer = new XYLineAndShapeRenderer();
        lineRenderer.setBaseShapesVisible(false);
        
        XYToolTipGenerator tt1 = new XYToolTipGenerator() {
            public String generateToolTip(XYDataset dataset, int series, int item) {
               //Number x = dataset.getX(series, item);
               //Number y = dataset.getY(series, item);
            	DecimalFormat df = new DecimalFormat("#.##");      
            	return dataset.getSeriesKey(series)+":"+
            			controller.getModel().getWaveletTableModel().getColumnName(item).toString()+":"+
            	df.format(dataset.getYValue(series, item));
            }
        };
         
        lineRenderer.setBaseToolTipGenerator(tt1);
		
		// vamos criar janelas para cada descritor
		for(int i=0; i<dmodel.getRowCount(); i++){

			// cria janela com o nome do descritor
			DescriptorButton db = (DescriptorButton) dmodel.getValueAt(i, 1);
			DefaultCategoryDataset[] r = db.getResult();
			//CombinedCategoryPlot dPlot = new CombinedCategoryPlot(db.getText());
			
			// cria séries conforme a quantidade de waveletsxborda
			// x nr de decomposicoes
			// y nr de waveletsxbordas
			XYSeries[][] series = new XYSeries[r.length][r[0].getColumnCount()];

			// loop por todos os sinais
			for (int j = 0; j < dmodel.getColumnCount(); j++) {
				
				DescriptorButton d = (DescriptorButton) dmodel.getValueAt(i, j+1);
				DefaultCategoryDataset[] result = d.getResult();
				
				for (int x = series.length-1; x > -1 ; x--) {
					for (int y = 0; y < series[0].length; y++) {
						
						if(series[x][y] == null) 
							series[x][y] = new XYSeries(result[x].getColumnKey(y).toString());
						series[x][y].add(series[x][y].getItemCount(), result[x].getValue(0, y));
					}
				}			
			}
			
			// hack para não mostrar as legendas do todos os subplots; Somente a legenda do primeiro plot
			// pois todos são iguais.
	        final CombinedDomainXYPlot plot = new CombinedDomainXYPlot(){
				private static final long serialVersionUID = -4902222675246739929L;
				@Override
	        	public LegendItemCollection getLegendItems() {
	        	    @SuppressWarnings("unchecked")
					java.util.List<XYPlot> subplots = getSubplots();
	        	    if ((subplots != null) && !subplots.isEmpty()) {
	        	        return subplots.get(0).getLegendItems();
	        	    }
	        	    return super.getLegendItems();
	        	}
	        };
	        
	        
	        //double max, min, mean;
	        
	        //System.out.println(db.getText());
	        
	        // cria datasets para cada decomposicao e insere dentro de
	        // cada dataset as séries equivalentes de cada waveletxborda
	        // cada serie de waveletxborda é composta por valores de descritor
	        // de um sinal especifico pela wtxborda
			for (int x = series.length-1; x>-1; x--) {
				XYSeriesCollection collection = new XYSeriesCollection();
				
				//System.out.println("D"+(x+1));
				
				for(int y=0; y<series[0].length; y++){
					collection.addSeries(series[x][y]);
					
					//max = Double.MIN_VALUE;
					//min = Double.MAX_VALUE;
					//mean = 0;

//					for (int j = 0; j < series[x][y].getItemCount(); j++) {
//						double yValue = series[x][y].getDataItem(j).getYValue();
//						if(max<yValue) max = yValue;
//						if(min>yValue) min = yValue;
//						mean += yValue;
//					}					
//					mean /= series[x][y].getItemCount();
//					
//					System.out.println(series[x][y].getKey()+" "+"Max:"+max+"\tMin:"+min+"\tMean:"+mean);
				}	
				
				
	        	final NumberAxis rangeAxis = new NumberAxis("D"+(x+1));
	            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	            rangeAxis.setTickMarksVisible(true);
	            rangeAxis.setTickLabelsVisible(true);
	            rangeAxis.setLabelAngle(Math.toRadians(90));
	            final XYPlot subplot = new XYPlot(collection, null, rangeAxis, lineRenderer);
	            
				plot.add(subplot,1);
			}
			
	        ValueAxis domainAxis = plot.getDomainAxis();
	        domainAxis.setTickLabelsVisible(false);			
			JFrame jFrame = new JFrame(db.getText());
			final JFreeChart chart = new JFreeChart(plot);
			final ChartPanel chartPanel = new ChartPanel(chart);
			jFrame.setContentPane(chartPanel);
			jFrame.pack();
			jFrame.setVisible(true);
		}		
	}

	public void showDescriptorResult(DescriptorButton db) {
		
		CombinedCategoryPlot dPlot = new CombinedCategoryPlot(db.getText());
		DefaultCategoryDataset[] result = db.getResult(); 
		for (int i = result.length-1; i>-1; i--) {
			dPlot.addData(result[i], "D"+(i+1));
		}
		dPlot.setDomainName("Descriptor(Wavelet)");
		dPlot.pack();
        //RefineryUtilities.centerFrameOnScreen(dPlot);
        dPlot.setVisible(true);		
	}

	public void initDescriptorTable() {
		descriptorForm.initTable();		
	}

	public void initWaveletTable() {
		waveletForm.initTable();		
	}

	public JTable getWaveletResultTable() {
		return waveletForm.getResultTable();
	}

	public JTable getDescriptorResultTable() {
		return descriptorForm.getResultTable();
	}

	public JFrame getWaveletForm() {
		return waveletForm;
	}

	public JFrame getDescriptorForm() {
		return descriptorForm;
	}

	public Timer getBusyIconTimer() {
		return busyIconTimer;
	}
	
	public void setIdleIcon(){
		statusAnimationLabel.setIcon(idleIcon);
	}

	public javax.swing.JMenuItem getSaveAsMenuItem() {
		return saveAsMenuItem;
	}

	public void setSaveAsMenuItem(javax.swing.JMenuItem saveAsMenuItem) {
		this.saveAsMenuItem = saveAsMenuItem;
	}

	public javax.swing.JMenuItem getExpDescResultMenuItem() {
		return expDescResultMenuItem;
	}	

    @Action
    public void showLdaHelp() {
    }

	public JButton getValidationButton() {
		return jButton_CheckValidation;
	}

	public JButton getDistributionButton() {
		return jButton_Distribution;
	}

	public JButton getROCButton() {
		return jButton_ROC;
	}

	public JButton getWeightsButton() {
		return jButton_WeigthsLDA;
	}

	public JButton getTestButton() {
		return jButton_TestLDA;
	}

	public JTextField getTestField() {
		return jText_TestNr;
	}
	
	public JLabel getTestFiedl(){
		return jLabel8;
	}
}
