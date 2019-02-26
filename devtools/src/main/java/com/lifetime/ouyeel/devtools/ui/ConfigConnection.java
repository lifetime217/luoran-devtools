package com.lifetime.ouyeel.devtools.ui;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * @author lifetime
 *
 */
public class ConfigConnection extends JDialog {
	private JTextField ip;
	private JTextField port;
	private JTextField dbName;
	private JTextField user;
	private JTextField pwd;
	private MainFrame mainFrame;
	public static final String ConUrl = "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=UTF-8";
	private static final String tmpFile = System.getProperty("java.io.tmpdir") + File.separator + "luoran-devtools.properties";
	private Properties prop;

	public ConfigConnection(JFrame fra) {
		super(fra,"连接配置");
		try {
			setIconImage(ImageIO.read(this.getClass().getResourceAsStream("logo.png")));
		} catch (IOException e1) {
		}
		File f = new File(tmpFile);
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(f);
			prop.load(fis);
			fis.close();
		} catch (Exception e) {
		}
		mainFrame = (MainFrame) fra;
		setPreferredSize(new Dimension(350, 300));
		setSize(350, 300);
		setModal(true);
		setLayout(null);
		
		JButton btn = new JButton("确定并连接");
		btn.setBounds(190, 200, 100, 35);
		btn.addActionListener((e) -> {
			FileOutputStream out;
			try {
				prop.setProperty("ip", ip.getText());
				prop.setProperty("port", port.getText());
				prop.setProperty("dbName", dbName.getText());
				prop.setProperty("user", user.getText());
				prop.setProperty("pwd", pwd.getText());
				out = new FileOutputStream(f);
				prop.store(out, "");
				out.close();
			} catch (Exception e1) {
			}
			mainFrame.refresh(String.format(ConUrl, ip.getText(), port.getText(), dbName.getText()), dbName.getText(),user.getText(),pwd.getText());
			ConfigConnection.this.setVisible(false);
		});

		JButton btn2 = new JButton("取消");
		btn2.setBounds(70, 200, 100, 35);
		btn2.addActionListener((e) -> {
			ConfigConnection.this.setVisible(false);
		});
		
		
		JLabel lbl = new JLabel("IP：");
		lbl.setBounds(20, 20, 80, 25);
		ip = new JTextField(prop.getProperty("ip", "127.0.0.1"));
		ip.setBounds(100, 20, 200, 25);
		add(lbl);
		add(ip);

		lbl = new JLabel("Port：");
		lbl.setBounds(20, 50, 80, 25);
		port = new JTextField(prop.getProperty("port", "3306"));
		port.setBounds(100, 50, 200, 25);
		add(lbl);
		add(port);

		lbl = new JLabel("dbName：");
		lbl.setBounds(20, 80, 80, 25);
		dbName = new JTextField(prop.getProperty("dbName", ""));
		dbName.setBounds(100, 80, 200, 25);
		add(lbl);
		add(dbName);
		
		lbl = new JLabel("userName：");
		lbl.setBounds(20, 110, 80, 25);
		user = new JTextField(prop.getProperty("user", "root"));
		user.setBounds(100, 110, 200, 25);
		add(lbl);
		add(user);

		lbl = new JLabel("pwd：");
		lbl.setBounds(20, 140, 80, 25);
		pwd = new JTextField(prop.getProperty("pwd", "test123!@#"));
		pwd.setBounds(100, 140, 200, 25);
		add(lbl);
		add(pwd);
		
		
		add(btn);
		add(btn2);

	}
}
