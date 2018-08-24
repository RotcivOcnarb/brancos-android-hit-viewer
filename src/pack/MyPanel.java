package pack;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import layout.CustomTreeCellRenderer;
import layout.CustomTreeNode;
import pack.filters.Filter;

public class MyPanel extends JPanel implements Runnable{

	//Sem ser interface
	DefaultMutableTreeNode root;
	Thread hit_listener_thread;
	HashMap<String, String> param_names;
	HashMap<String, String> compose_names;
	HashMap<String, String> enhanced_names;
	ImageIcon icon_app;
	ImageIcon icon_hit;
	public static ImageIcon icon_parameter;
	public static ImageIcon icon_value;
	ImageIcon icon_broken;
	ArrayList<Process> allrunning;
	
	//Animação
	Thread updateThread;
	volatile Dimension panelCurrent;
	volatile float targetWidth = 240;
	volatile float currentWidth = 240;
	
	volatile Dimension consoleCurrent;
	volatile float targetConsoleHeight = 30;
	volatile float currentConsoleHeight = 30;
	//Interface
	
		/*Topo*/ 
			JButton button_clear;
			
		/*Centro*/ JPanel panel_center;
		
			//Topo
				JScrollPane playload_area_scroll;
				JTextArea playload_area;
			//Centro
				DefaultTableModel params_table_model;
				JTable params_table;
			//Baixo
				JPanel center_bottom_panel;
				JTextArea console_area;
			
		/*Esquerda*/
			DefaultTreeModel hits_model;
			JTree hits_tree;
			JScrollPane hits_tree_scroll;
			
		/*Direita*/volatile JPanel panel_east;
			/*Centro*/JPanel both_filters_panel;
					//Filtro de hit
					JPanel hit_filter_panel;
						JLabel hit_filter_title;
						JButton hit_filter_add;
						JButton hit_filter_remove;
						JList<Filter> hit_filter_list;
						DefaultListModel<Filter> hit_filter_model;
					
					//Filtro de parametro
					JPanel filter_panel;
						JLabel filter_title;
						JTextField filter_txt;
						JButton filter_add;
						JButton filter_remove;
						JList<String> filter_list;
						JScrollPane list_scroll;
						DefaultListModel<String> filter_model;
						JScrollPane hit_list_scroll;
			//Esquerda
					JButton retract_right;
					
	//TODO: Refatoração	
				
	public MyPanel() {
		
		
		
		new Timer(10, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(panel_east != null &&  center_bottom_panel != null) {
					
					currentWidth += (targetWidth - currentWidth) / 5.0f;
					panelCurrent.setSize(currentWidth, panelCurrent.getHeight());
					panel_east.setPreferredSize(panelCurrent);
					panel_east.setSize(panelCurrent);
					
					currentConsoleHeight += (targetConsoleHeight - currentConsoleHeight) / 5.0f;
					consoleCurrent.setSize(consoleCurrent.getWidth(), currentConsoleHeight);
					center_bottom_panel.setPreferredSize(consoleCurrent);
					center_bottom_panel.setSize(consoleCurrent);
					
					revalidate();
					repaint();
				}
			}
		}).start();
		
		panelCurrent = new Dimension(200, 1000);
		consoleCurrent = new Dimension(200, 200);
		
		this.addComponentListener(new ComponentListener() {

			public void componentShown(ComponentEvent e) {}
			public void componentResized(ComponentEvent e) {
				list_scroll.setPreferredSize(new Dimension(200, (int) filter_panel.getSize().getHeight() - 150));
				hit_list_scroll.setPreferredSize(new Dimension(200, (int) hit_filter_panel.getSize().getHeight() - 120));
				repaint();
			}
			public void componentMoved(ComponentEvent e) {}
			public void componentHidden(ComponentEvent e) {}
			
		});
		
		allrunning = new ArrayList<Process>();
		
		filter_model = new DefaultListModel<String>();
		hit_filter_model = new DefaultListModel<Filter>();
		loadParamNames();

		hit_listener_thread = new Thread(this);
		hit_listener_thread.start();
		
		setLayout(new BorderLayout());
		
		both_filters_panel = new JPanel();
		both_filters_panel.setLayout(new GridLayout(2, 1));
		
		panel_east = new JPanel();
		panel_east.setLayout(new BorderLayout());
		
		//Filtro parametro
			filter_panel = new JPanel();
			filter_panel.setPreferredSize(new Dimension(200, 1000));
			filter_panel.setLayout(new FlowLayout());
			
			filter_title = new JLabel("Hide parameters", JLabel.CENTER);
			filter_title.setFont(new Font("Arial", Font.BOLD, 14));
			filter_title.setPreferredSize(new Dimension(200, 30));
			filter_panel.add(filter_title);
			
			filter_txt = new JTextField();
			filter_txt.setPreferredSize(new Dimension(200, 30));
			filter_panel.add(filter_txt);
			
			filter_add = new JButton("Add Filter");
			filter_add.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!filter_txt.getText().equals("")) {
						filter_model.addElement(filter_txt.getText());
						filter_txt.setText("");
					}
				}
			});
			filter_add.setPreferredSize(new Dimension(200, 30));
			filter_panel.add(filter_add);
			
			filter_remove = new JButton("Remove Filter");
			filter_remove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(filter_list.getSelectedIndex() != -1) {
						filter_model.remove(filter_list.getSelectedIndex());
					}
				}
			});
			filter_remove.setPreferredSize(new Dimension(200, 30));
			filter_panel.add(filter_remove);
			
			filter_list = new JList<String>(filter_model);
			
			list_scroll = new JScrollPane(filter_list);
	
			filter_panel.add(list_scroll);
	
			both_filters_panel.add(filter_panel);
		//Filtro hits
			
			hit_filter_panel = new JPanel();
			hit_filter_panel.setPreferredSize(new Dimension(200, 1000));
			hit_filter_panel.setLayout(new FlowLayout());
			
			hit_filter_title = new JLabel("Exclude hits", JLabel.CENTER);
			hit_filter_title.setFont(new Font("Arial", Font.BOLD, 14));
			hit_filter_title.setPreferredSize(new Dimension(200, 30));
			hit_filter_panel.add(hit_filter_title);
			
			hit_filter_add = new JButton("Add Filter");
			hit_filter_add.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					FilterCreationPanel fcp = new FilterCreationPanel();
					int resp = JOptionPane.showConfirmDialog(null, fcp, "Filter add", JOptionPane.OK_CANCEL_OPTION);
					if(resp == 0) {
						hit_filter_model.addElement(fcp.createFilter());
					}
				}
			});
			hit_filter_add.setPreferredSize(new Dimension(200, 30));
			hit_filter_panel.add(hit_filter_add);
			
			hit_filter_remove = new JButton("Remove Filter");
			hit_filter_remove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(hit_filter_list.getSelectedIndex() != -1) {
						hit_filter_model.remove(hit_filter_list.getSelectedIndex());
					}
				}
			});
			hit_filter_remove.setPreferredSize(new Dimension(200, 30));
			hit_filter_panel.add(hit_filter_remove);
			
			
			hit_filter_list = new JList<Filter>(hit_filter_model);
			
			hit_list_scroll = new JScrollPane(hit_filter_list);
			hit_list_scroll.setPreferredSize(new Dimension(200, 500));
	
			hit_filter_panel.add(hit_list_scroll);
	
			both_filters_panel.add(hit_filter_panel);
			panel_east.add(both_filters_panel, BorderLayout.CENTER);
			
			retract_right = new JButton(">");
			retract_right.setPreferredSize(new Dimension(40, 20));
			retract_right.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(retract_right.getText().equals(">")) {
						retract_right.setText("<");
						targetWidth = 40;
					}
					else if(retract_right.getText().equals("<")) {
						retract_right.setText(">");
						targetWidth = 240;
					}
				}
			});
			
			panel_east.add(retract_right, BorderLayout.WEST);
		add(panel_east, BorderLayout.EAST);
		try {
			icon_app = new ImageIcon(getScaledImage(ImageIO.read(getClass().getResourceAsStream("/smartphone.png")), 16, 16));
			icon_hit = new ImageIcon(getScaledImage(ImageIO.read(getClass().getResourceAsStream("/rocket-hitting-target.png")), 16, 16));
			icon_parameter = new ImageIcon(getScaledImage(ImageIO.read(getClass().getResourceAsStream("/tools.png")), 16, 16));
			icon_value = new ImageIcon(getScaledImage(ImageIO.read(getClass().getResourceAsStream("/value.png")), 16, 16));
			icon_broken = new ImageIcon(getScaledImage(ImageIO.read(getClass().getResourceAsStream("/broken-link.png")), 16, 16));
		}
		catch(Exception e) {
			Main.showException(e);
		}
		
		root = new DefaultMutableTreeNode(new CustomTreeNode("Aplicação", icon_app, ""));
		
		hits_model = new DefaultTreeModel(root);
		
		hits_tree = new JTree(hits_model);
		hits_tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) hits_tree.getLastSelectedPathComponent();
				if(node == null) return;
				try{
					playload_area.setText(((CustomTreeNode)node.getUserObject()).getPayload());
				}catch(Exception e1) {
					e1.printStackTrace();
				}
				for(int i = params_table_model.getRowCount() - 1; i >= 0; i --) {
					params_table_model.removeRow(i);
				}
						
				if(node != null && node.getChildCount() > 0) {
					for(int i = 0; i < node.getChildCount(); i ++) {
						DefaultMutableTreeNode ch = (DefaultMutableTreeNode) node.getChildAt(i);
						
						if(ch.getChildCount() > 0) {
							DefaultMutableTreeNode val = (DefaultMutableTreeNode) ch.getChildAt(0);
							params_table_model.addRow(new Object[]{ch.getUserObject(), val.getUserObject()});
						}
						else {
							params_table_model.addRow(new Object[]{node.getUserObject(), ch.getUserObject()});
						}
					}
				}
			}
		});
		
		try {
			hits_tree.setCellRenderer(new CustomTreeCellRenderer());
		}
		catch(Exception e) {
			Main.showException(e);
		}
		
		hits_tree_scroll = new JScrollPane(hits_tree);
		hits_tree_scroll.setPreferredSize(new Dimension(200, 1000));
		add(hits_tree_scroll, BorderLayout.WEST);
		
		JPanel upper_panel = new JPanel();
		upper_panel.setLayout(new BorderLayout());
		
		button_clear = new JButton("Clear");
		button_clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				root.removeAllChildren();
				hits_model.setRoot(root);
			}
		});
		
		upper_panel.add(button_clear, BorderLayout.CENTER);
		
		add(upper_panel, BorderLayout.NORTH);

		
		//Centro
		panel_center = new JPanel();
		panel_center.setLayout(new BorderLayout());
		
		playload_area = new JTextArea();
		playload_area.setLineWrap(true);
		playload_area.setPreferredSize(new Dimension(100, 100));
		playload_area.setEditable(false);
		
		JScrollPane payload_scroll = new JScrollPane(playload_area);
		payload_scroll.setPreferredSize(new Dimension(100, 50));
		panel_center.add(payload_scroll, BorderLayout.NORTH);
		
		//JTable
		params_table_model = new DefaultTableModel();
		params_table_model.addColumn("Name");
		params_table_model.addColumn("Value");

		params_table = new JTable(params_table_model);

		playload_area_scroll = new JScrollPane(params_table);
		panel_center.add(playload_area_scroll, BorderLayout.CENTER);
		
		center_bottom_panel = new JPanel();
		center_bottom_panel.setLayout(new BorderLayout());
		
		console_area = new JTextArea();
		console_area.setLineWrap(true);
		console_area.setEditable(false);
		
		JScrollPane console_scroll = new JScrollPane(console_area);
		console_scroll.setPreferredSize(new Dimension(100, 100));
		
		center_bottom_panel.add(console_scroll, BorderLayout.CENTER);
		
		JButton retract_console = new JButton("^");
		retract_console.setPreferredSize(new Dimension(40, 30));
		retract_console.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(retract_console.getText().equals("v")) {
					retract_console.setText("^");
					targetConsoleHeight = 30;
				}
				else if(retract_console.getText().equals("^")) {
					retract_console.setText("v");
					targetConsoleHeight = 200;
				}
			}
		});
		center_bottom_panel.add(retract_console, BorderLayout.NORTH);
		panel_center.add(center_bottom_panel, BorderLayout.SOUTH);
		add(panel_center, BorderLayout.CENTER);
	}
	
	public String[] removeEmpties(String[] arr) {
		int cont = 0;
		for(String s : arr) {
			if(!s.equals("")) {
				cont++;
			}
		}
		
		String[] result = new String[cont];
		
		int i = 0;
		for(String s : arr) {
			if(!s.equals("")) {
				result[i] = s;
				i++;
			}
		}
		
		return result;
	}

	public String getName(String code, boolean debug) {
		
		if(param_names.get(code) == null) {
			if(debug) System.out.println("compose");
			if(!code.split("\\d+")[0].equals(code)) {
				
				
				
				String result = "(" + code + ") ";
				
				String[] ltr = code.split("\\d+");
				String[] nms = removeEmpties(code.split("[a-z]+"));
				
				
				
				int nums = 0;
				for(String s : ltr) {
					if(nums < nms.length)
						result += compose_names.get(s) + " " + nms[nums] + " ";
					else
						result += compose_names.get(s);
					
					nums++;
				}
				
//				boolean let = true;
//
//				int cont = 0;
//				while(cont < Math.max(letters.length, numbers.length)) {
//					if(let) {
//						result += compose_names.get(letters[cont]) + " ";
//						if(debug) System.out.println("Construindo resultado: " + result);
//					}else {
//						if(cont < numbers.length) {
//							result += numbers[cont] + " ";
//							cont++;
//							if(debug) System.out.println("Construindo resultado: " + result);
//						}
//					}
//					let = !let;
//				}
				return result;
			}
			else {
				return code;
			}
		}
		else {
			return "(" + code + ") " + param_names.get(code);
		}
	}
	

	
	private void loadParamNames() {		
		param_names = new HashMap<String, String>();
		
		param_names.put("tid", "Tracking ID");
		param_names.put("aip", "Anonymous IP");
		param_names.put("ds", "Data Source");
		param_names.put("qt", "Queue Time");
		param_names.put("z", "Cache");
		param_names.put("cid", "Client ID");
		param_names.put("uid", "User ID");
		param_names.put("sc", "Session Control");
		param_names.put("uip", "User IP");
		param_names.put("ua", "User Agent");
		param_names.put("geoid", "Geographic ID");
		param_names.put("dr", "Document Referer");
		param_names.put("cn", "Campaign Name");
		param_names.put("cs", "Campaign Source");
		param_names.put("cm", "Campaign Medium");
		param_names.put("ck", "Campaign Keyword");
		param_names.put("cc", "Campaign Content");
		param_names.put("ci", "Campaign ID");
		param_names.put("gclid", "Adwords ID");
		param_names.put("dclid", "Announce ID");
		param_names.put("sr", "Screen Resolution");
		param_names.put("vp", "Viewport");
		param_names.put("de", "Document Encoding");
		param_names.put("sd", "Screen Depth");
		param_names.put("ul", "User Language");
		param_names.put("je", "Java Enabled");
		param_names.put("fl", "Flash Version");
		param_names.put("t", "Hit type");
		param_names.put("ni", "Non interactive Hit");
		param_names.put("dl", "Document Location");
		param_names.put("dh", "Document Host");
		param_names.put("dp", "Document Path");
		param_names.put("dt", "Document Title");
		param_names.put("cd", "Screen Name");
		param_names.put("linkid", "Link ID");
		param_names.put("an", "App Name");
		param_names.put("aid", "App ID");
		param_names.put("aiid", "Installer ID");
		param_names.put("av", "App Version");
		param_names.put("ec", "Event Category");
		param_names.put("ea", "Event Action");
		param_names.put("el", "Event Label");
		param_names.put("ev", "Event Value");
		param_names.put("ti", "Transaction ID");
		param_names.put("ta", "Transaction Affiliate");
		param_names.put("tr", "Transaction Recipe");
		param_names.put("ts", "Transaction Sent");
		param_names.put("tt", "Transaction Tributes");
		param_names.put("in", "Item Name");
		param_names.put("il", "Item List");
		param_names.put("ip", "Item Price");
		param_names.put("iq", "Item Quantity");
		param_names.put("ic", "Item Code");
		param_names.put("iv", "Item Category");
		param_names.put("pa", "Product Action");
		param_names.put("tcc", "Coupon Code");
		param_names.put("pal", "Product Action List");
		param_names.put("cos", "Checkout Step");
		param_names.put("col", "Checkout Step Option");
		param_names.put("promoa", "Promotion Action");
		param_names.put("cu", "Currency Code");
		param_names.put("sn", "Social Network");
		param_names.put("sa", "Social Action");
		param_names.put("st", "Social Action Target");
		param_names.put("utc", "User Timing Category");
		param_names.put("utv", "User Timing Variable");
		param_names.put("utt", "User Timing Time");
		param_names.put("utl", "User Timing Label");
		param_names.put("plt", "Page Load Time");
		param_names.put("dns", "Time to DNS Lookup");
		param_names.put("pdt", "Page Download Time");
		param_names.put("rrt", "Redirect Response Time");
		param_names.put("tcp", "TCP Connect Time");
		param_names.put("srt", "Server Response Time");
		param_names.put("dit", "DOM Interactive Time");
		param_names.put("clt", "Content Load Time");
		param_names.put("exd", "Exception Description");
		param_names.put("exf", "Exception Fail");
		param_names.put("xid", "Experiment ID");
		param_names.put("xvar", "Experiment Variant");
		
		//Compose
		compose_names = new HashMap<String, String>();
		compose_names.put("pr", "Price");
		compose_names.put("cd", "Custom Dimension");
		compose_names.put("cm", "Custom Metric");
		compose_names.put("id", "ID");
		compose_names.put("nm", "Name");
		compose_names.put("br", "Brand");
		compose_names.put("ca", "Category");
		compose_names.put("va", "Variant");
		compose_names.put("ps", "Position");
		compose_names.put("gc", "Content Group");
		compose_names.put("pi", "Product ID");
		
		//Enhanced Ecommerce
		enhanced_names = new HashMap<String, String>();
		enhanced_names.put("pr", "Product");
		enhanced_names.put("il", "Item List");
		enhanced_names.put("promo", "Promotion");
		
		HitNameInfo.compose_names = compose_names;
		HitNameInfo.param_names = param_names;
		HitNameInfo.enhanced_names = enhanced_names;
		
		try {
			File f = new File("filters.conf");
			
			if(!f.exists()) f.createNewFile();
			
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			
			String ln;
			
			while((ln = br.readLine()) != null) {
				filter_model.addElement(ln);
			}
			
			br.close();
		}
		catch(Exception e) {
			Main.showException(e);
		}
	}

	@Override
	public void paint(Graphics g) {
		if(list_scroll != null) {
			list_scroll.setPreferredSize(new Dimension(200, (int) filter_panel.getSize().getHeight() - 150));
			hit_list_scroll.setPreferredSize(new Dimension(200, (int) hit_filter_panel.getSize().getHeight() - 120));
		}
		super.paint(g);
	}
	
	Process log;
	BufferedReader br;
	public void run() {
		
		try {
			Process p = Runtime.getRuntime().exec("adb shell setprop log.tag.GAv4 DEBUG");
			allrunning.add(p);
			p.waitFor();
			p = Runtime.getRuntime().exec("adb logcat -c");
			allrunning.add(p);
			p.waitFor();
			System.out.println("Escutando disparos");
			log = Runtime.getRuntime().exec("adb logcat -s GAv4");
			allrunning.add(log);
			String s;
			br = new BufferedReader(new InputStreamReader(log.getInputStream()));
			while((s = br.readLine()) != null && !requestInterruption) {
				String[] arr = s.split(":");
				if(arr.length >= 5) {
					String title = "ERR";
					
					String hit_s = "";
					
					for(int i = 4; i < arr.length; i ++) {
						hit_s += arr[i] + ":";
					}
					hit_s = hit_s.substring(0, hit_s.length() - 1);
					
					try{
						if(hit_s.replaceAll("\\s", "").contains(",t=")) {
							title = hit_s.replaceAll("\\s", "").split(",t=")[1].split(",")[0];
						}
						else {
							title = "undetected";
						}
					}
					catch(Exception e) {
						Main.showException(e);
						System.out.println("ERRO: " + hit_s.replaceAll("\\s", ""));
						continue;
					}
					if(!hit_s.contains("=")) continue;
					DefaultMutableTreeNode hit = new DefaultMutableTreeNode(new CustomTreeNode(title, icon_hit, s));
					
					String[] pl = hit_s.replaceAll("\\s", "").split(",");
					boolean out = true;

					for(int i = 0; i < hit_filter_model.size(); i ++) {
						Filter f = hit_filter_model.get(i);
						if(f.evaluate(pl)) {
							out = false;
							break;
						}
					}
					
					if(out) {						
						EnhancedGroup group;
						
						group = new EnhancedGroup();
						//TODO: agrupamento
						
						for(String k : pl) {
							
							if(!filter_model.contains(k.split("=")[0])) {
								
								if(!k.contains("=")) {
									continue;
								}
								
								HitNameInfo hitName = new HitNameInfo(k);
								if(hitName.fullName.equals("erro")) {
									continue;
								}
								//Ve o tipo de enhance
								if(hitName.getTypeOfEnhance() != "") {
									//caso não exista, cria o grupo local do hit
									if(!group.containsChild(hitName.getTypeOfEnhance())) {
										group.put(hitName.getTypeOfEnhance(), new EnhancedGroup());
									}
									
									EnhancedGroup iteration_group = group.getChild(hitName.getTypeOfEnhance());
									for(int id : hitName.getIds()) {
										if(!iteration_group.containsChild(id + "")) {
											iteration_group.put(id+"", new EnhancedGroup());
										}
										iteration_group = iteration_group.getChild(id+"");
									}
									
									iteration_group.put(hitName.getFullName(), hitName);
									
									
								}
								
								if(!hitName.isEnhancedEcommerce()) {
									if(k.split("=").length == 1) {
										if(k.endsWith("=")) {
											DefaultMutableTreeNode param = new DefaultMutableTreeNode(new CustomTreeNode(hitName.getFullName(), icon_parameter, k));
											param.add(new DefaultMutableTreeNode(new CustomTreeNode("", icon_value, k)));
											hit.add(param);
										}
									}
									else {
										DefaultMutableTreeNode param = new DefaultMutableTreeNode(new CustomTreeNode(hitName.getFullName(), icon_parameter, k));
										param.add(new DefaultMutableTreeNode(new CustomTreeNode(k.split("=")[1], icon_value, k)));
										hit.add(param);
									}
								}
							}
						}
						if(!group.isEmpty()) {
							hit.add(group.getNode("enhanced"));
						}
						
						root.add(hit);
						hits_model.setRoot(root);
					}
					else {
					}
				}
			}
			br.close();
			
		}
		catch (IOException e) {
			ImageIcon icon = null;
			try {
				icon = new ImageIcon(getScaledImage(ImageIO.read(getClass().getResourceAsStream("/sad_branco.png")), 128, 128));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog(
					null,
					"O Branco está muito triste, porque ele não consegue encontrar o processo do adb :(\n"
					+ "Você tem certeza de que colocou o caminho do ADB nas variáveis de ambiente?\n"
					+ "(ele confia no seu potencial, e sabe que você vai conseguir!)",
					"ADB não encontrado", JOptionPane.ERROR_MESSAGE,
					icon);
			System.exit(1);
		} catch (InterruptedException e1) {
			Main.showException(e1);
		}
	}
	
	public static Image getScaledImage(Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}

	boolean requestInterruption = false;
	
	public void end() {
		requestInterruption = true;
		
		try {
			Runtime.getRuntime().exec("adb kill-server").waitFor();
		} catch (InterruptedException e1) {
			Main.showException(e1);
		} catch (IOException e1) {
			Main.showException(e1);
		}
			for(Process p : allrunning) {
				try {
					p.destroy();
				}
				catch(Exception e) {
					System.out.println("Não consegui matar o processo");
					Main.showException(e);
				}
			}
		
	}
	
}
