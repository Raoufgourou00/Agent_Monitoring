package com.dmsi.rmc;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;


public class FrameConnection {
	
	private Listner cls;
	private static MachineState machineState = new MachineState();
	private static boolean running = false;
	private URL url;
	

	//----This Is For Frame--------
	private static JFrame frame;
	private JMenuBar menuBar;
	private JMenu danky;
	private JMenuItem showLog;
	private JMenuItem hide;
	private JMenuItem quit;
	private JMenu help;
	private JMenuItem aboutDanky;
	private JLabel label1;
	private JLabel label2;
	private String[] choices = {"VM","Server"};
	private JLabel label3;
	private static JComboBox<Object> label4;
	private JLabel label5;
	private static JTextField label6;
	private JLabel label7;
	private static JTextField label8;
	private JLabel label9;
	private static JTextField label10;
	private JLabel label11;
	private static JButton on;
	private static JButton off;
	private JFrame logFrame;
	private JTextArea logField;
	private JButton clearLogField;
	private JButton hideLogFrame;
	private JButton refreshLog;
	private SwingWorker<Void, Integer> worker;
	private JFrame aboutFrame;
	//---------------------------
	
	
	

	FrameConnection() {
		
		//-------------This Is Just To Get The Screen Name------------------
		this.pushText("Collecting data", 0);
		machineState.collectData();
		//----------------------------------------------------------------
		
		
		//---------------Initialize Listner-------------------------------
		cls = new Listner();
		cls.start();
		//---------------------------------------------------------------
		
		
		//-------Frame---------- 
		ImageIcon img = new ImageIcon("img/logoDonkey.png");
		frame = new JFrame();
		frame.setIconImage(img.getImage());
		frame.setTitle("Donkey");
		if(machineState.getMachineInfo().isWindows()) {
			frame.setBounds(480, 180, 300, 230);
		}
		else {
			frame.setBounds(480, 180, 300, 240);
		}	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLayout(null);
		frame.getContentPane().setBackground(new Color(235, 230, 230));
		//------------------------------------------------------------------
		
		
		//------------------------Menu Bar----------------------------------
		menuBar = new JMenuBar();
		danky = new JMenu("Donkey");
		help = new JMenu("Help");		
		
		danky.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,11));
		help.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,11));
		
		showLog = new JMenuItem();
		hide = new JMenuItem();
		quit = new JMenuItem();
		aboutDanky = new JMenuItem();
		
		showLog.setBackground(Color.WHITE);
		hide.setBackground(Color.WHITE);
		quit.setBackground(Color.WHITE);
		aboutDanky.setBackground(Color.WHITE);
		
		showLog.setText("Show Log                  ");
		hide.setText("Hide                           ");
		quit.setText("Quit                               ");
		aboutDanky.setText("About Donkey...       ");
		
		showLog.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,11));
		hide.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,11));
		quit.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,11));
		aboutDanky.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,11));
		
		KeyStroke f2 = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
		KeyStroke f4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0);
		KeyStroke Ctrl_Q = KeyStroke.getKeyStroke(KeyEvent.VK_Q, 2);
		KeyStroke Ctrl_H = KeyStroke.getKeyStroke(KeyEvent.VK_H, 2);
		
		showLog.setAccelerator(f2);
		hide.setAccelerator(f4);
		quit.setAccelerator(Ctrl_Q);
		aboutDanky.setAccelerator(Ctrl_H);		
		
		danky.add(showLog);
		danky.add(hide);
		danky.add(new JSeparator());
		danky.add(quit);
		help.add(aboutDanky);	
		
		quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            	
            	frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));	
            }
        });
		
		hide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            	
            	frame.setVisible(false);	
            }
        });
		
		showLog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            	
            	refreshLog();
            	logFrame.setVisible(true);
    	
            }
        });
		
		aboutDanky.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            	
            	aboutFrame.setVisible(true);
            }
        });
		
		menuBar.add(danky);
		menuBar.add(help);	
		frame.setJMenuBar(menuBar);
		//-------------------------------------------------------------------------------------
		
		
		//----------------------------ScreenName Field-----------------------------------------
		label1 = new JLabel();
		label1.setText("Screen Name");
		label1.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
		label1.setForeground(Color.BLACK);
		label1.setBounds(10, 10, 85,20);
		frame.add(label1);
		
		label2 = new JLabel();
		label2.setText(machineState.getLastName());
		label2.setFont(new Font(Font.SERIF,Font.BOLD,12));
		label2.setForeground(Color.BLACK);
		
		frame.add(label2);
		//------------------------------------

		
		//------------MachineType Filed---------
		label3 = new JLabel();
		label3.setText("Machine Type :");
		label3.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
		label3.setForeground(Color.BLACK);
		label3.setBounds(10, 40, 100,20);
		frame.add(label3);
		
		
		label4 = new JComboBox<Object>(choices);
		label4.setSelectedIndex(1);
		label4.setFont(new Font(Font.SERIF,Font.BOLD,12));
		label4.setForeground(Color.BLACK);
		
		frame.add(label4);
		//-------------------------------------
		
		
		//-----------serverIp Field------------
		label5 = new JLabel();
		label5.setText("Host IP :");
		label5.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
		label5.setForeground(Color.BLACK);
		label5.setBounds(10, 65, 100,20);
		frame.add(label5);
		
		label6 = new JTextField();
		label6.setFont(new Font(Font.SERIF,Font.BOLD,12));
		label6.setForeground(Color.BLACK);
		
		frame.add(label6);
		//---------------------------------------
		
			
		//------------ServerPort Filed---------
		label7 = new JLabel();
		label7.setText("Host Port :");
		label7.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
		label7.setForeground(Color.BLACK);
		label7.setBounds(10, 90, 100,20);
		frame.add(label7);
		
		label8 = new JTextField();
		label8.setFont(new Font(Font.SERIF,Font.BOLD,12));
		label8.setForeground(Color.BLACK);
		
		frame.add(label8);
		//-------------------------------------
		
		
		
		//----------------Timer Field-----------------
		label9 = new JLabel();
		label9.setText("Timer (Min) :");
		label9.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
		label9.setForeground(Color.BLACK);
		label9.setBounds(10, 115, 100,20);
		frame.add(label9);
		
		label10 = new JTextField();
		label10.setFont(new Font(Font.SERIF,Font.BOLD,12));
		label10.setForeground(Color.BLACK);
		
		frame.add(label10);
		//-------------------------------------------------
		
		
	
		
		//--------------------Start Button-------------------
		on = new JButton();
		on.setText("Start");
		on.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
		on.setBackground(new Color(237, 235, 235));
		
		on.addActionListener(new ActionListener() {
            
			@Override
            public void actionPerformed(ActionEvent ae) {
            	
				
				if(!ipIsValid(label6.getText())) {
					showErrorMessage("Please entre a valid ip address.");
				}
				else {
					
						if(!portIsValid(label8.getText())) {
							showErrorMessage("Please entre a valid port number.");
						}
						else
						{
										
							if(!timerIsValid(label10.getText())) {
								showErrorMessage("Please Entre An Integer Positif Number For Timer.");
							}
							else {
								
								try {
				            		machineState = new MachineState(label6.getText(),Integer.parseInt(label8.getText()),Integer.parseInt(label10.getText()),choices[label4.getSelectedIndex()],cls.getListnerPort());	
				            		url = new URL(machineState.toUrl());
				            		setRunning(true);
				            		pushText("Starting client",0);
				            		pushText("Set client configurations",0);
				            		pushText("Host IP: '"+machineState.getHost()+"' Host Port: '"+machineState.getPort()+"' Machine Type: '"+machineState.getMachineType()+"' Timer: '"+machineState.getTimer()+" minutes'",0);
				            		pushText("Started client",0);				            		
				            		initializeWorker();
				                    worker.execute();
				            	}
				                catch( Exception e ){ 
				                	pushText("Failed to initialize URL",1);
				            		label11.setText("Donkey Is Not Running.");
				            	}
								
							}
						}
							
								
								
						
	
							
					}
				
				}
        });	
		frame.add(on);
		//-----------------------------------------------------------------------
		
		
		
		
		//---------------------Of Button--------------------------------------
		off = new JButton();
		off.setText("Stop");
		off.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
		off.setBackground(new Color(237, 235, 235));
		
		off.setBorderPainted( false );
		off.setFocusPainted( false );
		off.setEnabled(false);
		off.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            	
            	setRunning(false);
            	pushText("Stoped client",0);
            	worker.cancel(true);  
        		stop();	
        		label11.setText("Donkey is not running.");
        		pushText("Process exited normally",0);	
            }
        });	
		frame.add(off);
		//------------------------------------------------------------------
		
		
		
		if(machineState.getMachineInfo().isWindows()){
			label2.setBounds(120, 10, 170,20);
			label4.setBounds(120, 40, 170,20);
			label6.setBounds(120, 65, 170,20);
			label8.setBounds(120, 90, 170,20);
			label10.setBounds(120, 115, 170,20);
			on.setBounds(223, 150, 65, 20);
			off.setBounds(150, 150, 65, 20);
		}
		else {
			label2.setBounds(120, 10, 175,20);
			label4.setBounds(120, 40, 175,20);
			label6.setBounds(120, 65, 175,20);
			label8.setBounds(120, 90, 175,20);
			label10.setBounds(120, 115, 175,20);
			on.setBounds(223, 150, 70, 20);
			off.setBounds(150, 150, 70, 20);	
		}
		
		
		//-----------------Danky's Message-------------------
		label11 = new JLabel();
		label11.setText("Donkey Is Not Running.");
		label11.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
		label11.setForeground(Color.BLACK);
		label11.setBounds(5, 150, 200,20);
		frame.add(label11);
		//---------------------------------------------------------
		
		
		//-----------Log Frame -----------------------------
		logFrame = new JFrame();
		logFrame.setIconImage(img.getImage());
        logFrame.setTitle("Log-Donkey");
        logFrame.setVisible(false);
        if(machineState.getMachineInfo().isWindows()) {
        	logFrame.setBounds(300, 200, 705, 330);
        }
        else {
        	logFrame.setBounds(300, 200, 700, 340);
        }     
        logFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        logFrame.setResizable(false);
        logFrame.setLayout(null);
        logFrame.getContentPane().setBackground(new Color(235, 230, 230));   
        
        logField = new JTextArea();
        logField.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
        logField.setForeground(Color.BLACK);
        logField.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(logField);
        scrollPane.setBounds(10,10,680,260);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
   
        logFrame.getContentPane().add(scrollPane);
        
        hideLogFrame = new JButton();
        hideLogFrame.setText("Hide");
        hideLogFrame.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
        hideLogFrame.setBackground(new Color(237, 235, 235));
        hideLogFrame.setBounds(588, 275, 100, 20);
        hideLogFrame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            	
            	logFrame.setVisible(false);	
            }
        });
		
        logFrame.add(hideLogFrame );
        
        
        clearLogField = new JButton();
        clearLogField.setText("Clear Log");
        clearLogField.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
        clearLogField.setBackground(new Color(237, 235, 235));
        clearLogField.setBounds(482, 275, 100, 20);
        clearLogField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            	
            		clearLog();
            		refreshLog();
            }
        });
		
        logFrame.add(clearLogField );
        
        
        refreshLog = new JButton();
        refreshLog.setText("Refresh");
        refreshLog.setBounds(10, 275, 100, 20);
        refreshLog.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
        refreshLog.setBackground(new Color(237, 235, 235));
        refreshLog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            	
            	refreshLog();
            		
            }
        });
		
        logFrame.add(refreshLog );
        
        
        ImageIcon image = new ImageIcon("img/Logo.png");
        JLabel l1 = new JLabel();

        l1.setIcon(image);
        l1.setHorizontalAlignment(JLabel.CENTER);
        l1.setVerticalAlignment(JLabel.CENTER);
        
        
        JLabel l2 = new JLabel();
        l2.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
        l2.setText("Donkey monitoring agent is specialized software that help");
        l2.setForeground(Color.GRAY);
       
        
        JLabel l3 = new JLabel();
        l3.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
        l3.setText("keep servers up-to-date via continuous, 24/24 scanning.");
        l3.setForeground(Color.GRAY);
        
        
        JLabel l4 = new JLabel();
        l4.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
        l4.setText("Version: 0.0");
        l4.setForeground(Color.GRAY);
        
        
        JLabel l6 = new JLabel();
        l6.setFont(new Font(Font.SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
        l6.setText("Build Date: May 2022");
        l6.setForeground(Color.GRAY);
        
        
        JLabel l5 = new JLabel();
        l5.setFont(new Font(Font.SERIF,Font.ROMAN_BASELINE,11));
        l5.setText("Copyright Ⓒ 2021-2022 Gourou Med Raouf, Sayoud Abdelhadi");
        l5.setForeground(Color.GRAY);
        
        if(machineState.getMachineInfo().isWindows()) {
        	 l1.setBounds(140,10,100,37);
        	 l2.setBounds(50,50,400,20);
             l3.setBounds(55,70,400,20);
             l4.setBounds(20,110,400,20);
             l6.setBounds(20,130,400,20);
             l5.setBounds(45,160,400,20);
        }
        else {
        	l1.setBounds(140,10,100,37);
            l2.setBounds(15,50,400,20);
            l3.setBounds(20,70,400,20);
            l4.setBounds(15,100,400,20);
            l6.setBounds(15,120,400,20);
            l5.setBounds(20,160,400,20);
        }
        
        
        aboutFrame = new JFrame();
        aboutFrame.setIconImage(img.getImage());
        aboutFrame.setTitle("About Donkey");
        aboutFrame.setLayout(null);
        aboutFrame.add(l1);
        aboutFrame.add(l2);
        aboutFrame.add(l3);
        aboutFrame.add(l4);
        aboutFrame.add(l5);
        aboutFrame.add(l6);
        aboutFrame.pack();
        aboutFrame.setBounds(430, 300, 400, 220);
        aboutFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        aboutFrame.setResizable(false);
        aboutFrame.getContentPane().setBackground(Color.WHITE);   
        
       
        frame.setVisible(true);
		//-----------------------------------------------------------
        
        
		
	}

	 public static JComboBox<Object> getLabel4() {
		return label4;
	}

	public static JTextField getLabel6() {
		return label6;
	}

	public static JTextField getLabel8() {
		return label8;
	}

	public static JTextField getLabel10() {
		return label10;
	}

	

	public static JButton getOn() {
		return on;
	}


	public static JButton getOff() {
		return off;
	}


	private void initializeWorker() {
	        worker = new SwingWorker<Void, Integer>() {

	            @Override
	            protected Void doInBackground() throws Exception {
	            		
	            	int i = 0;
	            	while (!isCancelled()) {
	            			
	            		i++;
	            		//Give This i To "process" Method
	            		publish(i); 
	                    
	            		
	                    try {
							java.util.concurrent.TimeUnit.MINUTES.sleep(Integer.parseInt(label10.getText()));
						} catch (InterruptedException e) {
							pushText("Clock Is Down",1);
							label11.setText("Donkey Is Not Running.");
						}
						
	                    
	                }
					return null;               
	            }
	            
	            @Override
	            protected void process(List<Integer> chunks) {
	                
	            	int i = chunks.get(0);           
	                i = i + 1;
	                
	                pushText("Collecting Data",0);
	                machineState.collectData();
	                
	            	/*
	                if(sendTextData("{\n" +  
	    					"        \"name\": \"djamel\",\n" + 
	    					"        \"dpt\": \"it\",\n" + 
	    					"        \"salary\": 2000\n" + 
	    					"    }",url) ){
                		
	                	label11.setText("Danky Is Sending.");
	                	
	                	
                	}
	                else {
                		label11.setText("Danky Is Not Sending.");
                	}
	                */
	                
	                pushText("Connecting To "+ "'" + machineState.toUrl() + "'",0);
	                boolean b = sendTextData(machineState.toJSONObject().toString(),url);
	                
	            	if(b){ 
	            		
	                	label11.setText("Donkey Is Sending.");
	                	pushText("Cconnection Successfully Established Server: Connection Succeeded",0);
	                	pushText("Data Is Sent",0);
                	}
                	else {
                		label11.setText("Donkey Is Not Sending.");
                		pushText("Failed To Connect To Server: Connection Refused",1);
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
	
	 	
	
	 	
	 	public boolean ipIsValid(final String ip) {
	 	    String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

	 	    return ip.matches(PATTERN)||ip.equals("localhost");
	 	}
	 	
	 	
	 	public static boolean portIsValid(final String port) {
	 		String PATTERN = "^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
	 		return port.matches(PATTERN);
	 	}
	 	
	 
	 	public boolean timerIsValid(final String timer) {
	 		String PATTERN = "^[1-9]\\d*$";
	 		return timer.matches(PATTERN);
	 	}
	 	
	 	public static void showErrorMessage(String errorMessage) {
	 		JOptionPane.showMessageDialog(frame, errorMessage);
	 	}
	 	
	 	
	 	public Boolean sendTextData(String data,URL url) {
			
	 		boolean ok = true;
			try {
				HttpURLConnection con = (HttpURLConnection)url.openConnection();
				con.setRequestMethod("POST");
				con.setConnectTimeout(5000);
				con.setReadTimeout(5000);
				con.setRequestProperty("Content-Type", "application/json; utf-8");
				con.setRequestProperty("Accept", "application/json");
				con.setDoOutput(true);
		
				try(OutputStream os = con.getOutputStream()) {
				    byte[] input = data.getBytes("utf-8");
				    os.write(input, 0, input.length);			
				}
					
				if(con.getResponseCode() != 200)
				{	
					ok = false;		
				}
				
				
				try(BufferedReader br = new BufferedReader(
						    new InputStreamReader(con.getInputStream(), "utf-8"))) {
						    StringBuilder response = new StringBuilder();
						    String responseLine = null;
						    while ((responseLine = br.readLine()) != null) {
						        response.append(responseLine.trim());
						    }
						    //System.out.println(response.toString());
						}
				
				
				
				
			} catch (Exception e) {
				pushText("Connection Failed",1);
				return false;
			}
			
			return ok;
			
			
			
			
		}
	    
	 	 	
	 	public void pushText(String text,int message) {
	 		
	 		switch (message) {
	 		case 0: LoggerClass.getLOGGER().info(text);
	 			break;
	 		case 1: LoggerClass.getLOGGER().warning(text);	
	 			break;
	 		}
	 		
	 	}
	 	
	 	
	 	public void refreshLog() {
	 		File log = new File("log/Events.log");
	 		try {
	 			logField.setText("");
				Scanner scanner = new Scanner(log);
				while(scanner.hasNext()) {
					logField.append(scanner.nextLine()+"\n");
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 		
	 		
	 	}
	 	
	 	public void clearLog() {
	 	    File file = new File("log/Events.log");
	 	    PrintWriter writer;
			try {
				writer = new PrintWriter(file);
				writer. print("");
		 	    writer. close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 	    
	 	}
	 	 	
	 	static void loadLibrary() {
	 		
	 		System.setProperty( "java.library.path", "./lib" );
	 		System.setProperty( "java.library.path", "./log" );
	 		
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
	 	
	 	
	 	public static JFrame getFrame() {
			return frame;
		}

		public static boolean getRunning() {
			return running;
		}

		public static void setRunning(boolean running) {
			FrameConnection.running = running;
		}

		public void setCls(Listner cls) {
			this.cls = cls;
		}

		public static void setMachineState(MachineState machineState) {
			FrameConnection.machineState = machineState;
		}

		public void setUrl(URL url) {
			this.url = url;
		}

		public static void setFrame(JFrame frame) {
			FrameConnection.frame = frame;
		}

		public void setMenuBar(JMenuBar menuBar) {
			this.menuBar = menuBar;
		}

		public void setDanky(JMenu danky) {
			this.danky = danky;
		}

		public void setShowLog(JMenuItem showLog) {
			this.showLog = showLog;
		}

		public void setHide(JMenuItem hide) {
			this.hide = hide;
		}

		public void setQuit(JMenuItem quit) {
			this.quit = quit;
		}

		public void setHelp(JMenu help) {
			this.help = help;
		}

		public void setAboutDanky(JMenuItem aboutDanky) {
			this.aboutDanky = aboutDanky;
		}

		public void setLabel1(JLabel label1) {
			this.label1 = label1;
		}

		public void setLabel2(JLabel label2) {
			this.label2 = label2;
		}

		public void setChoices(String[] choices) {
			this.choices = choices;
		}

		public void setLabel3(JLabel label3) {
			this.label3 = label3;
		}

		public static void setLabel4(JComboBox<Object> label4) {
			FrameConnection.label4 = label4;
		}

		public void setLabel5(JLabel label5) {
			this.label5 = label5;
		}

		public static void setLabel6(JTextField label6) {
			FrameConnection.label6 = label6;
		}

		public void setLabel7(JLabel label7) {
			this.label7 = label7;
		}

		public static void setLabel8(JTextField label8) {
			FrameConnection.label8 = label8;
		}

		public void setLabel9(JLabel label9) {
			this.label9 = label9;
		}

		public static void setLabel10(JTextField label10) {
			FrameConnection.label10 = label10;
		}

		public void setLabel11(JLabel label11) {
			this.label11 = label11;
		}

		public static void setOn(JButton on) {
			FrameConnection.on = on;
		}

		public static void setOff(JButton off) {
			FrameConnection.off = off;
		}

		public void setLogFrame(JFrame logFrame) {
			this.logFrame = logFrame;
		}

		public void setLogField(JTextArea logField) {
			this.logField = logField;
		}

		public void setClearLogField(JButton clearLogField) {
			this.clearLogField = clearLogField;
		}

		public void setHideLogFrame(JButton hideLogFrame) {
			this.hideLogFrame = hideLogFrame;
		}

		public void setRefreshLog(JButton refreshLog) {
			this.refreshLog = refreshLog;
		}

		public void setWorker(SwingWorker<Void, Integer> worker) {
			this.worker = worker;
		}

		public void setAboutFrame(JFrame aboutFrame) {
			this.aboutFrame = aboutFrame;
		}

		public Listner getCls() {
			return cls;
		}

		public static MachineState getMachineState() {
			return machineState;
		}

		public URL getUrl() {
			return url;
		}

		public JMenuBar getMenuBar() {
			return menuBar;
		}

		public JMenu getDanky() {
			return danky;
		}

		public JMenuItem getShowLog() {
			return showLog;
		}

		public JMenuItem getHide() {
			return hide;
		}

		public JMenuItem getQuit() {
			return quit;
		}

		public JMenu getHelp() {
			return help;
		}

		public JMenuItem getAboutDanky() {
			return aboutDanky;
		}

		public JLabel getLabel1() {
			return label1;
		}

		public JLabel getLabel2() {
			return label2;
		}

		public String[] getChoices() {
			return choices;
		}

		public JLabel getLabel3() {
			return label3;
		}

		public JLabel getLabel5() {
			return label5;
		}

		public JLabel getLabel7() {
			return label7;
		}

		public JLabel getLabel9() {
			return label9;
		}

		public JLabel getLabel11() {
			return label11;
		}

		public JFrame getLogFrame() {
			return logFrame;
		}

		public JTextArea getLogField() {
			return logField;
		}

		public JButton getClearLogField() {
			return clearLogField;
		}

		public JButton getHideLogFrame() {
			return hideLogFrame;
		}

		public JButton getRefreshLog() {
			return refreshLog;
		}

		public SwingWorker<Void, Integer> getWorker() {
			return worker;
		}

		public JFrame getAboutFrame() {
			return aboutFrame;
		}

		

	 	 	
}

