/*
 * Created by JFormDesigner on Mon Dec 05 14:58:00 CST 2016
 */

package kdde.niagara.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.lang.StringUtils;

import kdde.niagara.gui.filter.*;
import kdde.niagara.mining.Miner;
import kdde.niagara.mining.Param;
import kdde.niagara.mining.SqMiningFactory;


/**
 * @author yan
 */
public class App extends JFrame {
	
	private File file1 = null;
	
	private File file2 = null;
	public App() {
		initComponents();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	
	private void button1ActionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser("D:\\yanli\\development\\workspace\\Paper4");
		fileChooser.addChoosableFileFilter(new DataFileClassFilter("arff"));
		fileChooser.addChoosableFileFilter(new DataFileClassFilter("cvs"));
		fileChooser.addChoosableFileFilter(new DataFileClassFilter("txt"));
		int result = fileChooser.showOpenDialog(filePanel);
		if (result == JFileChooser.APPROVE_OPTION) {
			// 成功读取
			file1 = fileChooser.getSelectedFile();
			String fileName = file1.getName();
			int index = fileName.lastIndexOf('.');
			String extension = "";
			if (index > 0 && index < fileName.length() - 1) {
				extension = fileName.substring(index + 1).toLowerCase();
				if (!"arff,cvs,txt".contains(extension)) {// 判断文件是否为支持的数据类型
					JOptionPane.showMessageDialog(null, "数据文件只支持*.arff,*.cvs,*.txt格式文件！");
					file1=null;
					return;
				}
			}
			fileTextField1.setText(fileName);
		}
	}

	private void button2ActionPerformed(ActionEvent e) {

		JFileChooser fileChooser = new JFileChooser("D:\\yanli\\development\\workspace\\Paper4");
		fileChooser.addChoosableFileFilter(new DataFileClassFilter("arff"));
		fileChooser.addChoosableFileFilter(new DataFileClassFilter("cvs"));
		fileChooser.addChoosableFileFilter(new DataFileClassFilter("txt"));
		int result = fileChooser.showOpenDialog(filePanel);
		if (result == JFileChooser.APPROVE_OPTION) {
			// 成功读取
			file2 = fileChooser.getSelectedFile();
			String fileName = file2.getName();
			int index = fileName.lastIndexOf('.');
			String extension = "";
			if (index > 0 && index < fileName.length() - 1) {
				extension = fileName.substring(index + 1).toLowerCase();
				if (!"arff,cvs,txt".contains(extension)) {// 判断文件是否为支持的数据类型
					JOptionPane.showMessageDialog(null, "数据文件只支持*.arff,*.cvs格式文件！");
					file2=null;
					return;
				}
			}
			fileTextField2.setText(fileName);
		}
	
	}

	private void chooseActionPerformed(ActionEvent e) {

		  JPopupMenu popup = getChooseClassPopupMenu();

		  // show the popup where the source component is
		  if (e.getSource() instanceof Component) {
		    Component comp = (Component) e.getSource();
		    popup.show(comp, comp.getX(), comp.getY());
		    popup.pack();
		  }
		}
	/**
	   * Returns a popup menu that allows the user to change
	   * the class of object.
	   *
	   * @return a JPopupMenu that when shown will let the user choose the class
	   */
	  public JPopupMenu getChooseClassPopupMenu() {


	    // create the tree, and find the path to the current class
		String[] s1 ={"MDSP-CGC"};
		Hashtable hashtable1 = new Hashtable();
		Hashtable hashtable2 = new Hashtable();
		hashtable2.put("Sequence", s1);
		hashtable1.put("Niagara", hashtable2);
	    final JTree tree = new JTree(hashtable1);
	    tree.getSelectionModel().setSelectionMode
	      (TreeSelectionModel.SINGLE_TREE_SELECTION);

	    // create the popup
	    final JPopupMenu popup = new JTreePopupMenu(tree);
	      
	    // respond when the user chooses a class
	    tree.addTreeSelectionListener(new TreeSelectionListener() {
		public void valueChanged(TreeSelectionEvent e) {
		  DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		  
		  if (node == null) 
		    return;
		  
		  if (node.isLeaf()) {
		    /*if (node.m_Capabilities != null && m_CapabilitiesFilter != null) {
		      if (!node.m_Capabilities.supportsMaybe(m_CapabilitiesFilter) && 
		          !node.m_Capabilities.supports(m_CapabilitiesFilter)) {
		        return;
		      }
		    } */
			algorithmTextField.setText(node.toString());
		    popup.setVisible(false);
		  }
		}
	      });
	    
	    return popup;
	  }

	private void runbuttonActionPerformed(ActionEvent e) {
		if (file1 == null) {
			JOptionPane.showMessageDialog(null, "请选择数据集1");
			fileButton1.requestFocus();
			return;
		}
		if (file2 == null) {
			JOptionPane.showMessageDialog(null, "请选择数据集2");
			fileButton2.requestFocus();
			return;
		}
		String alpha = alphaTextField.getText();
		if (StringUtils.isEmpty(alpha)) {
			JOptionPane.showMessageDialog(null, "请输入alpha");
			alphaTextField.requestFocus();
			return;
		}
		Double alphadouble = 0.0;
		try {
			alphadouble = Double.valueOf(alpha);
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(null, "请输入double");
			alphaTextField.requestFocus();
			return;
		}

		String betal = betalTextField.getText();
		if (StringUtils.isEmpty(betal)) {
			JOptionPane.showMessageDialog(null, "请输入betal");
			betalTextField.requestFocus();
			return;
		}
		Double betaldouble = null;
		try {
			betaldouble = Double.valueOf(betal);
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(null, "请输入double");
			betalTextField.requestFocus();
			return;
		}
		String algorithm = algorithmTextField.getText();
		if (algorithm == null) {
			JOptionPane.showMessageDialog(null, "请选择算法");
			choosebutton.requestFocus();
			return;
		}
		Param param = new Param(file1, file2, alphadouble, betaldouble);
		Miner miner = SqMiningFactory.createMiner(algorithm);
		if (miner != null) {
			java.util.List<String> results = miner.mine(param);
			StringBuffer result = new StringBuffer();
			for (String string : results) {
				result.append(string + "\n");
			}
			textPane1.setText(result.toString());
		}
	}
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		menuBar1 = new JMenuBar();
		menu1 = new JMenu();
		menuItem1 = new JMenuItem();
		menu2 = new JMenu();
		menu3 = new JMenu();
		filePanel = new JPanel();
		fileButton1 = new JButton();
		fileButton2 = new JButton();
		fileTextField1 = new JTextField();
		fileTextField2 = new JTextField();
		panel2 = new JPanel();
		choosebutton = new JButton();
		algorithmTextField = new JTextField();
		panel3 = new JPanel();
		label1 = new JLabel();
		label2 = new JLabel();
		alphaTextField = new JTextField();
		betalTextField = new JTextField();
		runButton = new JButton();
		scrollPane1 = new JScrollPane();
		textPane1 = new JTextPane();

		//======== this ========
		setTitle("Niagara");
		Container contentPane = getContentPane();

		//======== menuBar1 ========
		{
			menuBar1.setPreferredSize(new Dimension(20, 18));
			menuBar1.setOpaque(false);
			menuBar1.setAutoscrolls(true);

			//======== menu1 ========
			{
				menu1.setText("\u6587\u4ef6");

				//---- menuItem1 ----
				menuItem1.setText("\u4fdd\u5b58");
				menuItem1.setPreferredSize(new Dimension(50, 24));
				menuItem1.setAlignmentX(0.0F);
				menu1.add(menuItem1);
			}
			menuBar1.add(menu1);

			//======== menu2 ========
			{
				menu2.setText("\u7f16\u8f91");
			}
			menuBar1.add(menu2);

			//======== menu3 ========
			{
				menu3.setText("\u89c6\u56fe");
			}
			menuBar1.add(menu3);
		}
		setJMenuBar(menuBar1);

		//======== filePanel ========
		{
			filePanel.setBorder(new TitledBorder("\u6570\u636e\u96c6\u9009\u62e9"));

			//---- fileButton1 ----
			fileButton1.setText("\u6570\u636e\u96c61");
			fileButton1.setMargin(new Insets(2, 2, 2, 2));
			fileButton1.addActionListener(e -> button1ActionPerformed(e));

			//---- fileButton2 ----
			fileButton2.setText("\u6570\u636e\u96c62");
			fileButton2.setMargin(new Insets(2, 2, 2, 2));
			fileButton2.addActionListener(e -> button2ActionPerformed(e));

			//---- fileTextField1 ----
			fileTextField1.setEditable(false);

			//---- fileTextField2 ----
			fileTextField2.setEditable(false);

			GroupLayout filePanelLayout = new GroupLayout(filePanel);
			filePanel.setLayout(filePanelLayout);
			filePanelLayout.setHorizontalGroup(
				filePanelLayout.createParallelGroup()
					.addGroup(filePanelLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(filePanelLayout.createParallelGroup()
							.addGroup(filePanelLayout.createSequentialGroup()
								.addComponent(fileButton1)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(fileTextField1))
							.addGroup(filePanelLayout.createSequentialGroup()
								.addComponent(fileButton2)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(fileTextField2, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE)
								.addGap(0, 0, Short.MAX_VALUE)))
						.addContainerGap())
			);
			filePanelLayout.setVerticalGroup(
				filePanelLayout.createParallelGroup()
					.addGroup(filePanelLayout.createSequentialGroup()
						.addGroup(filePanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(fileButton1)
							.addComponent(fileTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(filePanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(fileButton2)
							.addComponent(fileTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
			);
		}

		//======== panel2 ========
		{
			panel2.setBorder(new TitledBorder("\u7b97\u6cd5"));

			//---- choosebutton ----
			choosebutton.setText("\u9009\u62e9");
			choosebutton.setMargin(new Insets(2, 2, 2, 2));
			choosebutton.addActionListener(e -> chooseActionPerformed(e));

			//---- algorithmTextField ----
			algorithmTextField.setEditable(false);

			GroupLayout panel2Layout = new GroupLayout(panel2);
			panel2.setLayout(panel2Layout);
			panel2Layout.setHorizontalGroup(
				panel2Layout.createParallelGroup()
					.addGroup(panel2Layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(choosebutton)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(algorithmTextField, GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
						.addContainerGap())
			);
			panel2Layout.setVerticalGroup(
				panel2Layout.createParallelGroup()
					.addGroup(GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
						.addGap(0, 0, Short.MAX_VALUE)
						.addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(choosebutton)
							.addComponent(algorithmTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
			);
		}

		//======== panel3 ========
		{

			//---- label1 ----
			label1.setText("alpha");

			//---- label2 ----
			label2.setText("betal");

			GroupLayout panel3Layout = new GroupLayout(panel3);
			panel3.setLayout(panel3Layout);
			panel3Layout.setHorizontalGroup(
				panel3Layout.createParallelGroup()
					.addGroup(panel3Layout.createSequentialGroup()
						.addComponent(label1, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(alphaTextField, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addComponent(label2, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(betalTextField, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
						.addGap(0, 210, Short.MAX_VALUE))
			);
			panel3Layout.setVerticalGroup(
				panel3Layout.createParallelGroup()
					.addGroup(GroupLayout.Alignment.TRAILING, panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(label1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(label2, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
						.addComponent(betalTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(alphaTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
			);
		}

		//---- runButton ----
		runButton.setText("\u8fd0\u884c");
		runButton.addActionListener(e -> runbuttonActionPerformed(e));

		//======== scrollPane1 ========
		{
			scrollPane1.setViewportView(textPane1);
		}

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
							.addGroup(contentPaneLayout.createParallelGroup()
								.addComponent(panel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(panel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(filePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 951, Short.MAX_VALUE))
					.addContainerGap())
				.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
					.addContainerGap(42, Short.MAX_VALUE)
					.addComponent(runButton, GroupLayout.PREFERRED_SIZE, 901, GroupLayout.PREFERRED_SIZE)
					.addGap(36, 36, 36))
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addComponent(filePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addComponent(panel2, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(panel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(18, 18, 18)
					.addComponent(runButton)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
					.addContainerGap())
		);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	
	
	
	/**
	   * Creates a popup menu containing a tree that is aware
	   * of the screen dimensions.
	   */
	  public class JTreePopupMenu 
	    extends JPopupMenu {
	    
	    /** for serialization. */
	    static final long serialVersionUID = -3404546329655057387L;

	    /** the popup itself. */
	    private JPopupMenu m_Self;
	    
	    /** The tree. */
	    private JTree m_tree;

	    /** The scroller. */
	    private JScrollPane m_scroller;

	    /** The button for closing the popup again. */
	    private JButton m_CloseButton = new JButton("Close");
	    
	    /**
	     * Constructs a new popup menu.
	     *
	     * @param tree the tree to put in the menu
	     */
	    public JTreePopupMenu(JTree tree) {

	      m_Self = this;
	      
	      setLayout(new BorderLayout());
	      JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	      add(panel, BorderLayout.SOUTH);


	      // close
	      m_CloseButton.setMnemonic('C');
	      m_CloseButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	          if (e.getSource() == m_CloseButton) {
	            m_Self.setVisible(false);
	          }
	        }
	      });
	      panel.add(m_CloseButton);
	      
	      m_tree = tree;
	      
	      JPanel treeView = new JPanel();
	      treeView.setLayout(new BorderLayout());
	      treeView.add(m_tree, BorderLayout.NORTH);
	      
	      // make backgrounds look the same
	      treeView.setBackground(m_tree.getBackground());

	      m_scroller = new JScrollPane(treeView);
	      
	      m_scroller.setPreferredSize(new Dimension(300, 400));
	      m_scroller.getVerticalScrollBar().setUnitIncrement(20);

	      add(m_scroller);
	    }

	    /**
	     * Displays the menu, making sure it will fit on the screen.
	     *
	     * @param invoker the component thast invoked the menu
	     * @param x the x location of the popup
	     * @param y the y location of the popup
	     */
	    public void show(Component invoker, int x, int y) {

	      super.show(invoker, x, y);

	      // calculate available screen area for popup
	      java.awt.Point location = getLocationOnScreen();
	      java.awt.Dimension screenSize = getToolkit().getScreenSize();
	      int maxWidth = (int) (screenSize.getWidth() - location.getX());
	      int maxHeight = (int) (screenSize.getHeight() - location.getY());

	      // if the part of the popup goes off the screen then resize it
	      Dimension scrollerSize = m_scroller.getPreferredSize();
	      int height = (int) scrollerSize.getHeight();
	      int width = (int) scrollerSize.getWidth();
	      if (width > maxWidth) width = maxWidth;
	      if (height > maxHeight) height = maxHeight;
	      
	      // commit any size changes
	      m_scroller.setPreferredSize(new Dimension(width, height));
	      revalidate();
	      pack();
	    }
	  }
	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JMenuBar menuBar1;
	private JMenu menu1;
	private JMenuItem menuItem1;
	private JMenu menu2;
	private JMenu menu3;
	private JPanel filePanel;
	private JButton fileButton1;
	private JButton fileButton2;
	private JTextField fileTextField1;
	private JTextField fileTextField2;
	private JPanel panel2;
	private JButton choosebutton;
	private JTextField algorithmTextField;
	private JPanel panel3;
	private JLabel label1;
	private JLabel label2;
	private JTextField alphaTextField;
	private JTextField betalTextField;
	private JButton runButton;
	private JScrollPane scrollPane1;
	private JTextPane textPane1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	public static void main(String[] args) {
		try {
			// System.out.println(UIManager.getSystemLookAndFeelClassName());
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new App();
	}
}
