package com.dmsi.rmc.facade;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
public class FacadeGUI {
	
	JFrame frame;
	
	private JMenuBar menuBar;
	private JMenu danky;
	private JMenu help;
	
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JTextField label4;
	private JLabel label5;
	private JTextField label6;
	private JLabel label7;
	private JTextField label8;
	private JLabel label9;
	private JTextField label10;
	
	private JButton on;
	private JButton off;
	private SwingWorker<Void, Integer> worker;
	private JLabel label11;

	private Connection conn;
	
	private FacadeGUI() {
		
		frame = new JFrame();
		frame.setTitle("Danky");
		frame.setBounds(480, 180, 300, 240);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLayout(null);
		frame.getContentPane().setBackground(new Color(235, 230, 230));
		
		// Menu Bar
		menuBar = new JMenuBar();
		danky = new JMenu("Danky");
		help = new JMenu("Help");
		
		menuBar.add(danky);
		menuBar.add(help);
		
		frame.setJMenuBar(menuBar);
		
		
		label1 = new JLabel();
		label1.setText("Screen Name");
		label1.setFont(new Font(Font.SANS_SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
		label1.setForeground(Color.BLACK);
		label1.setBounds(5, 10, 85,20);
		frame.add(label1);
		
		conn = new Connection();
		conn.collectData();
		
		label2 = new JLabel();
		label2.setText(conn.getLastName());
		label2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,12));
		label2.setForeground(Color.BLACK);
		label2.setBounds(100, 10, 190,20);
		frame.add(label2);
		
		
		label3 = new JLabel();
		label3.setText("Server IP :");
		label3.setFont(new Font(Font.SANS_SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
		label3.setForeground(Color.BLACK);
		label3.setBounds(10, 40, 85,20);
		frame.add(label3);
		
		label4 = new JTextField();
		//label4.setText("192.168.15.1");
		label4.setFont(new Font(Font.SANS_SERIF,Font.BOLD,12));
		label4.setForeground(Color.BLACK);
		label4.setBounds(105, 40, 190,20);
		frame.add(label4);
		
		
		label5 = new JLabel();
		label5.setText("Server Port :");
		label5.setFont(new Font(Font.SANS_SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
		label5.setForeground(Color.BLACK);
		label5.setBounds(10, 65, 85,20);
		frame.add(label5);
		
		label6 = new JTextField();
		//label6.setText("8085");
		label6.setFont(new Font(Font.SANS_SERIF,Font.BOLD,12));
		label6.setForeground(Color.BLACK);
		label6.setBounds(105, 65, 190,20);
		frame.add(label6);
		
		
		label7 = new JLabel();
		label7.setText("Context :");
		label7.setFont(new Font(Font.SANS_SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
		label7.setForeground(Color.BLACK);
		label7.setBounds(10, 90, 85,20);
		frame.add(label7);
		
		label8 = new JTextField();
		//label8.setText("8085");
		label8.setFont(new Font(Font.SANS_SERIF,Font.BOLD,12));
		label8.setForeground(Color.BLACK);
		label8.setBounds(105, 90, 190,20);
		frame.add(label8);
		
		label9 = new JLabel();
		label9.setText("Timer (Min) :");
		label9.setFont(new Font(Font.SANS_SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
		label9.setForeground(Color.BLACK);
		label9.setBounds(10, 115, 85,20);
		frame.add(label9);
		
		label10 = new JTextField();
		//label8.setText("8085");
		label10.setFont(new Font(Font.SANS_SERIF,Font.BOLD,12));
		label10.setForeground(Color.BLACK);
		label10.setBounds(105, 115, 190,20);
		frame.add(label10);
		
		
		on = new JButton();
		on.setText("Start");
		on.setFont(new Font(Font.SANS_SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
		on.setBackground(new Color(237, 235, 235));
		on.setBounds(223, 150, 70, 20);
		on.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            	
            	try {
            		conn = new Connection(label4.getText(),Integer.parseInt(label6.getText()),label8.getText(),Integer.parseInt(label10.getText()));	
            		System.out.println("Connection Construction: Ok");
            		initializeWorker();
                    worker.execute();
            	}
                catch( Exception e ){ 
                	System.out.println("Connection Construction: Bad");
            		label11.setText("You're Danky.");
            	}
            	
            	
            }
        });
		
		frame.add(on);
		
		
		off = new JButton();
		off.setText("Stop");
		off.setFont(new Font(Font.SANS_SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
		off.setBackground(new Color(237, 235, 235));
		off.setBounds(150, 150, 70, 20);
		off.setBorderPainted( false );
		off.setFocusPainted( false );
		off.setEnabled(false);
		off.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            	
            	worker.cancel(true);
            	
                
        		stop();
        		
        		label11.setText("Danky Is Not Running.");
        		
            }
        });
		
		frame.add(off);
		
		label11 = new JLabel();
		label11.setText("Danky Is Not Running.");
		label11.setFont(new Font(Font.SANS_SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
		label11.setForeground(Color.BLACK);
		label11.setBounds(5, 150, 200,20);
		frame.add(label11);
	
		frame.setVisible(true);
	}
	
	
	
	public static void main(String[] args) {
		
		FacadeGUI.loadLibrary();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SwingUtilities.invokeLater(() -> new FacadeGUI());
					//window.setVisible(true);		
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
	}


	 private void initializeWorker() {
	        worker = new SwingWorker<Void, Integer>() {

	            @Override
	            protected Void doInBackground() throws Exception {
	            		
	            	int i = 0;
	            	
	            	while (!isCancelled()) {
	            		
	            		
	            		i++;
	            		//Thread.sleep(Integer.parseInt(label10.getText())*1000);
	                    publish(i); // give this i to "process" method
	                   
	                    try {
							java.util.concurrent.TimeUnit.SECONDS.sleep(Integer.parseInt(label10.getText()));
						} catch (InterruptedException e) {
		
							label11.setText("Danky Is Not Running.");
						}
	                    
	                }
					return null;
				
	               
	            }
	            
	            @Override
	            protected void process(List<Integer> chunks) {
	                int i = chunks.get(0);
	                
	                System.out.println("Iteration number: " + i);
	                i++;
	            	conn.collectData();
	            	/*
	                if(conn.sendTextData("{\n" +  
	    					"        \"name\": \"djamel\",\n" + 
	    					"        \"dpt\": \"it\",\n" + 
	    					"        \"salary\": 2000\n" + 
	    					"    }") ){
                		
	                	label11.setText("Danky Is Sending.");
	                	
	                	
                	}*/
	            	if(conn.sendTextData(conn.toJSONObject().toString())){
                		
	                	label11.setText("Danky Is Sending.");
	                	
	                	
                	}
                	else {
                		label11.setText("Danky Is Not Sending.");
                	}
	                
	             start();  
	                         
	            }
	            
	        };
	        
	    }
	
	 
	 
	 	public void start() {
	 		
	 		off.setBorderPainted(true);
    		off.setFocusPainted(true);
    		off.setEnabled(true);
    		on.setBorderPainted( false );
    		on.setFocusPainted( false );
    		on.setEnabled(false);
    		
	 		
	 		label4.setEnabled(false);
	 		label6.setEnabled(false);
	 		label8.setEnabled(false);
	 		label10.setEnabled(false);
	 	}
	 	
	 	
	 	public void stop() {
	 		
	 		off.setBorderPainted(false);
    		off.setFocusPainted(false);
    		off.setEnabled(false);
            
    		on.setBorderPainted(true);
    		on.setFocusPainted(true);
    		on.setEnabled(true);
	 		
    		label4.setEnabled(true);
	 		label6.setEnabled(true);
	 		label8.setEnabled(true);
	 		label10.setEnabled(true);
    		
	 	}
	
	 	
	
	 	
	 	
	 	
	 	
	 	static void loadLibrary() {
	 		
	 		System.setProperty( "java.library.path", "./lib" );

			java.lang.reflect.Field fieldSysPath;
			try {
				fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
				fieldSysPath.setAccessible( true );
				fieldSysPath.set( null, null );
			} catch (NoSuchFieldException | SecurityException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			catch (IllegalArgumentException | IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	 	}
	 	
	 	
	 	
}

