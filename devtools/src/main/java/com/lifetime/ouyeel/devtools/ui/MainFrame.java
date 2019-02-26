package com.lifetime.ouyeel.devtools.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.lifetime.ouyeel.devtools.model.TableInfo;
import com.lifetime.ouyeel.devtools.pojo.PojoGenFactory;
import com.lifetime.ouyeel.devtools.util.DbUtil;

/**
 * @author lifetime
 *
 */
public class MainFrame extends JFrame {
	private JTree tableTree;
	private DefaultTreeModel treeModel;
	private JPopupMenu treePopMenu;
	private JTabbedPane jtab;
	private ConfigConnection configConnection;
	private Connection conn;
	private String urlTemp;
	private String dbName;
	private JLabel projectPkgDir;
	private JLabel pkgName;

	public MainFrame(String title) {
		super(title);
		init();

	}

	public void refresh(String url, String dbName, String user, String pwd) {
		this.dbName = dbName;
		if (urlTemp == null || !urlTemp.equals(url)) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(url, user, pwd);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "连接失败." + e.getMessage());
				return;
			}
		}
		List<TableInfo> list = DbUtil.initAllTables(conn, dbName);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(dbName);
		list.forEach(tableInfo -> {
			DefaultMutableTreeNode cnode = new DefaultMutableTreeNode(tableInfo);
			tableInfo.getFields().forEach(finfo -> {
				DefaultMutableTreeNode fnode = new DefaultMutableTreeNode(finfo);
				cnode.add(fnode);
			});
			root.add(cnode);
		});
		treeModel.setRoot(root);
		treeModel.nodeStructureChanged(root);
	}

	public void refresh() {
		if (conn != null && dbName != null) {
			List<TableInfo> list = DbUtil.initAllTables(conn, dbName);
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(dbName);
			list.forEach(tableInfo -> {
				DefaultMutableTreeNode cnode = new DefaultMutableTreeNode(tableInfo);
				tableInfo.getFields().forEach(finfo -> {
					DefaultMutableTreeNode fnode = new DefaultMutableTreeNode(finfo);
					cnode.add(fnode);
				});
				root.add(cnode);
			});
			treeModel.setRoot(root);
			treeModel.nodeStructureChanged(root);
		}
	}

	private void init() {
		setSize(1300, 850);
		setLayout(new BorderLayout());
		try {
			setIconImage(ImageIO.read(this.getClass().getResourceAsStream("logo.png")));
		} catch (IOException e1) {
		}
		JPanel setting = new JPanel(new FlowLayout(FlowLayout.LEFT));
		setting.setPreferredSize(new Dimension(400, 35));
		add(setting, BorderLayout.NORTH);

		JButton btn = new JButton("配置连接");
		btn.addActionListener(e -> {
			if (configConnection == null) {
				configConnection = new ConfigConnection(MainFrame.this);
			}
			configConnection.setLocationRelativeTo(MainFrame.this);
			configConnection.setVisible(true);
		});
		setting.add(btn);

		JButton btn2 = new JButton("刷新表结构");
		btn2.addActionListener(e -> {
			refresh();
		});
		setting.add(btn2);

		JButton btn3 = new JButton("生成一套文件");
		btn3.addActionListener(e -> {
			genFrameworkFile();
		});
		setting.add(btn3);
		
		
		JButton btn4 = new JButton("设置项目目录与包目录");
		btn4.addActionListener(e -> {
			settingProjectDir();
		});
		setting.add(btn4);

		projectPkgDir = new JLabel();
		setting.add(projectPkgDir);

		pkgName = new JLabel();
		setting.add(pkgName);

		JSplitPane splPane = new JSplitPane();
		splPane.setDividerLocation(0.4D);
		splPane.setLastDividerLocation(400);
		add(splPane, BorderLayout.CENTER);

		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.setPreferredSize(new Dimension(400, 400));
		splPane.setLeftComponent(leftPanel);
		treeModel = new DefaultTreeModel(new DefaultMutableTreeNode("未连接"));
		tableTree = new JTree(treeModel);
		JScrollPane jsp = new JScrollPane(tableTree);
		leftPanel.add(jsp, BorderLayout.CENTER);

		treePopMenu = new JPopupMenu();
		JMenuItem genPojoCode = new JMenuItem("生成 Pojo");
		genPojoCode.addActionListener(e -> {
			List<TableInfo> tables = getSelected();
			if (tables != null && !tables.isEmpty()) {
				removeTabs();
				tables.forEach(table -> {
					JPanel tp = createCodePanel(PojoGenFactory.getGen().gen(table));
					jtab.addTab(table.getName(), tp);
				});
			} else {
				JOptionPane.showMessageDialog(MainFrame.this, "请先选择表！");
			}
		});
		treePopMenu.add(genPojoCode);

		tableTree.setComponentPopupMenu(treePopMenu);

		jtab = new JTabbedPane();
		jtab.add("代码区域", new JPanel());
		splPane.setRightComponent(jtab);

	}

	private void settingProjectDir() {
		String dir = JOptionPane.showInputDialog(this, "请输入项目action包的上级目录的全路径", projectPkgDir.getText());
		if (!new File(dir).exists()) {
			JOptionPane.showMessageDialog(this, "该路径不存在 : " + dir);
			return;
		}
		if (!new File(dir).isDirectory()) {
			JOptionPane.showMessageDialog(this, "该路径必须是文件夹 : " + dir);
			return;
		}

		projectPkgDir.setText(dir);
		
		String pkg = JOptionPane.showInputDialog(this, "请输入项目action包的上级包路径", pkgName.getText());
		pkgName.setText(pkg);
	}

	/**
	 * 生成一套框架文件（实体，service，dao，action）
	 */
	protected void genFrameworkFile() {
		if (projectPkgDir.getText() == null || projectPkgDir.getText().trim().length() == 0) {
			String dir = JOptionPane.showInputDialog(this, "请输入项目action包的上级目录的全路径", "");
			if (!new File(dir).exists()) {
				JOptionPane.showMessageDialog(this, "该路径不存在 : " + dir);
				return;
			}
			if (!new File(dir).isDirectory()) {
				JOptionPane.showMessageDialog(this, "该路径必须是文件夹 : " + dir);
				return;
			}

			projectPkgDir.setText(dir);

		}

		if (pkgName.getText() == null || pkgName.getText().trim().length() == 0) {
			String pkg = JOptionPane.showInputDialog(this, "请输入项目action包的上级包路径", "");
			pkgName.setText(pkg);
		}

		List<TableInfo> tables = getSelected();
		if (tables != null && !tables.isEmpty()) {
			StringBuffer successFiles = new StringBuffer();
			for (TableInfo table : tables) {
				try {
					successFiles.append(
							PojoGenFactory.getGen().genAllFile(projectPkgDir.getText(), pkgName.getText(), table));
					successFiles.append("\n");
				} catch (Exception e) {
					e.printStackTrace();
					if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(MainFrame.this,
							"出现错误，是否继续？\n" + e.getMessage())) {
						continue;
					} else {
						break;
					}
				}
			}
			JOptionPane.showMessageDialog(MainFrame.this, "生成完毕！\n" + successFiles);
		} else {
			JOptionPane.showMessageDialog(MainFrame.this, "请先选择表！");
		}
	}

	protected void genEntity(String projectDir2) {

	}

	public void removeTabs() {
		int count = jtab.getTabCount();
		for (int i = count - 1; i >= 0; i--) {
			jtab.removeTabAt(i);
		}
	}

	public JPanel createCodePanel(String code) {
		JPanel codePanel = new JPanel(new BorderLayout());
		JTextPane codeTextPane = new JTextPane();
		codeTextPane.setEditable(false);
		codeTextPane.setText(code);
		JScrollPane jsp = new JScrollPane(codeTextPane);
		codePanel.add(jsp, BorderLayout.CENTER);
		return codePanel;
	}

	protected List<TableInfo> getSelected() {
		if (tableTree.getSelectionPaths() == null) {
			return null;
		}
		List<TableInfo> list = new ArrayList<>();
		Arrays.asList(tableTree.getSelectionPaths()).forEach(item -> {
			if (item.getLastPathComponent() != null && item.getLastPathComponent() instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) item.getLastPathComponent();
				if (node.getUserObject() instanceof TableInfo) {
					list.add((TableInfo) node.getUserObject());
				}
			}
		});
		return list;
	}

}
