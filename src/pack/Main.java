package pack;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.synth.SynthLookAndFeel;

import de.javasoft.plaf.synthetica.SyntheticaPlainLookAndFeel;

public class Main {
	
	//Mostra o erro em um JOptionPane ao invés do console
	public static void showException(Exception e) {
		e.printStackTrace();
	}
		
	public static void main(String args[]) {
		
		SwingUtilities.invokeLater(new Runnable() {
		     public void run() {
		
		    	 
		JFrame frame = new JFrame("Branco's Android Hit Viewer");
		new Thread(new Runnable() {

			public void run() {
				
				try 
			    {	
					//Muda o Look and Feel pro tema do Synthetica
					SyntheticaPlainLookAndFeel splaf = new SyntheticaPlainLookAndFeel();
					UIManager.setLookAndFeel(splaf);
					
			    } 
			    catch (Exception e) 
			    {
			      showException(e);
			    }

				showDevices();
				createWindow();
				//Depois de inicializar tudo, fecha o Splash
				((SplashPanel)frame.getContentPane()).exit();
			}
		}).start();
		
		//Cria a janela de Splash
		Image icon = Toolkit.getDefaultToolkit().getImage(frame.getClass().getResource("/icon.png"));
		
		frame.setSize(200, 200);
		frame.setUndecorated(true);
		frame.setIconImage(icon);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setBackground(new Color(0, 0, 0, 0));
		frame.setContentPane(new SplashPanel());
		frame.getContentPane().setBackground(new Color(1, 1, 1, 0f));
		
	}
	});
		
	}
	

	//Cria a janela principal
	public static void createWindow() {
		JFrame frame = new JFrame("Branco's Android Hit Viewer");
		frame.setSize(800, 600);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(frame.getClass().getResource("/icon.png")));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		//My Panel define toda a interface
		MyPanel mp = new MyPanel();
		
		
		
		try {
			System.setErr(new DialogOut(mp));
			System.setOut(new DialogOut(mp));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		frame.setContentPane(mp);
		frame.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
			public void windowClosing(WindowEvent e) {
				mp.end();
				
			}
			public void windowClosed(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
		});
		frame.setVisible(true);
	}

	//Roda o adb devices, e lista os dispositivos conectados
	//Caso  não tenha nenhum dispositivo, mostra a mensagem e fecha o programa
	private static void showDevices() {
		try {
			System.out.println("Inicializando ADB");
			Runtime.getRuntime().exec("adb start-server").waitFor();
			Process devices = Runtime.getRuntime().exec("adb devices -l");
			BufferedReader dr = new BufferedReader(new InputStreamReader(devices.getInputStream()));
			String s;
			boolean header = true;
			String dispo = "";
			int cont_dev = 0;
			while((s = dr.readLine()) != null) {
				if(header) {
					header = false;
					continue;
				}
				String aa[] = s.split(":");
				if(aa.length > 2) {
					dispo += s.split(":")[2].split("\\s+")[0];
					cont_dev ++;
				}
			}
			devices.waitFor();
			if(cont_dev == 0) {
				JOptionPane.showMessageDialog(null, "Nenhum dispositivo encontrado, conecte seu dispositivo Android antes de executar o aplicativo");
				dr.close();
				Runtime.getRuntime().exec("adb kill-server").waitFor();
				System.exit(1);
			}
			else {
				JOptionPane.showMessageDialog(null, cont_dev + " dispositivos encontrados:\n" + dispo);
			}
			
			
		}
		catch(Exception e) {
		      showException(e);
		}
	}

}
