package com.dmsi.rmc.machine;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class FrameConnection {
	
	
	private ServerState serverState = new ServerState();
	private URL url;
	
	
	//----This Is For Frame--------
	private JFrame frame;
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
	//---------------------------
	
	
	
	
	FrameConnection() {
		
		serverState.collectData();
		
		//-------Frame---------- 
		frame = new JFrame();
		frame.setTitle("Danky");
		frame.setBounds(480, 180, 300, 240);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLayout(null);
		frame.getContentPane().setBackground(new Color(235, 230, 230));
		//---------------------
		
		//-------Menu Bar-----
		menuBar = new JMenuBar();
		danky = new JMenu("Danky");
		help = new JMenu("Help");	
		menuBar.add(danky);
		menuBar.add(help);	
		frame.setJMenuBar(menuBar);
		//-----------------------
		
		
		//--------ScreenName Field-------------
		label1 = new JLabel();
		label1.setText("Screen Name");
		label1.setFont(new Font(Font.SANS_SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
		label1.setForeground(Color.BLACK);
		label1.setBounds(5, 10, 85,20);
		frame.add(label1);
		
		label2 = new JLabel();
		label2.setText(serverState.getLastName());
		label2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,12));
		label2.setForeground(Color.BLACK);
		label2.setBounds(100, 10, 190,20);
		frame.add(label2);
		//------------------------------------
		
		
		//-----------serverIp Field------------
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
		//---------------------------------------
		
		
		//------------ServerPort Filed---------
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
		//-------------------------------------
		
		
		
		//------------Context Field---------------
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
		//-------------------------------------------
		
		
		
		//----------------Timer Field-----------------
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
		//-------------------------------------------------
		
		
		//--------------------Start Button-------------------
		on = new JButton();
		on.setText("Start");
		on.setFont(new Font(Font.SANS_SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
		on.setBackground(new Color(237, 235, 235));
		on.setBounds(223, 150, 70, 20);
		
		on.addActionListener(new ActionListener() {
            
			@Override
            public void actionPerformed(ActionEvent ae) {
            	
				if(!ipIsValid(label4.getText())) {
					showErrorMessage("Make Sur You Entred A Valid Ip Address.");
				}
				else {
					
						if(!portIsValid(label6.getText())) {
							showErrorMessage("Make Sur You Entred A Valid Port Number.");
						}
						else
						{
							
								if(!timerIsValid(label10.getText())) {
									showErrorMessage("Make Sur You Entred Positif Number For Timer.");
								}
								else {
									
									try {
					            		serverState = new ServerState(label4.getText(),Integer.parseInt(label6.getText()),label8.getText(),Integer.parseInt(label10.getText()));	
					            		url = new URL(serverState.toUrl());
					            		System.out.println("Connection Construction: Ok");
					            		initializeWorker();
					                    worker.execute();
					            	}
					                catch( Exception e ){ 
					                	System.out.println("Connection Construction: Bad");
					            		label11.setText("You're Danky.");
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
		//------------------------------------------------------------------
		
		
		
		//-----------------Danky's Message-------------------
		label11 = new JLabel();
		label11.setText("Danky Is Not Running.");
		label11.setFont(new Font(Font.SANS_SERIF,Font.LAYOUT_LEFT_TO_RIGHT,12));
		label11.setForeground(Color.BLACK);
		label11.setBounds(5, 150, 200,20);
		frame.add(label11);
		//---------------------------------------------------------
		
		
		//-----------------Make Frame Visible-------------------
		frame.setVisible(true);
		//------------------------------------------------------
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
							java.util.concurrent.TimeUnit.MINUTES.sleep(Integer.parseInt(label10.getText()));
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
	                serverState.collectData();
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
	            	if(sendTextData(serverState.toJSONObject().toString(),url)){
                		
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
	
	 	
	
	 	
	 	public boolean ipIsValid(final String ip) {
	 	    String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

	 	    return ip.matches(PATTERN)||ip.equals("localhost");
	 	}
	 	
	 	
	 	public boolean portIsValid(final String port) {
	 		String PATTERN = "^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
	 		return port.matches(PATTERN);
	 	}
	 	
	 
	 	public boolean timerIsValid(final String timer) {
	 		String PATTERN = "^[1-9]\\d*$";
	 		return timer.matches(PATTERN);
	 	}
	 	
	 	public void showErrorMessage(String errorMessage) {
	 		JOptionPane.showMessageDialog(frame, errorMessage);
	 	}
	 	
	 	
	 	
	 	
	 	public Boolean sendTextData(String data,URL url) {
			boolean ok = true;
			try {
				
				HttpURLConnection con = (HttpURLConnection)url.openConnection();
				con.setRequestMethod("POST");
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
					System.out.println("Connection Estableshed");
				}
				
				
				try(BufferedReader br = new BufferedReader(
						  new InputStreamReader(con.getInputStream(), "utf-8"))) {
						    StringBuilder response = new StringBuilder();
						    String responseLine = null;
						    while ((responseLine = br.readLine()) != null) {
						        response.append(responseLine.trim());
						    }
						    System.out.println(response.toString());
						}
				
				
				
				
			} catch (Exception e) {
				System.err.println("Err: Connection Failed");
				return false;
			}
			
			return ok;
			
			
			
			
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

