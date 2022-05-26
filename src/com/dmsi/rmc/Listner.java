package com.dmsi.rmc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.swing.JOptionPane;
import org.json.JSONException;
import org.json.JSONObject;


public class Listner extends Thread {
	

	private int listnerPort = 8082;
	private boolean end; 
	
	public Listner() {
		super();
	}

	public Listner(int listnerPort) {
		super();
		this.listnerPort = listnerPort;
	}

	public void terminate() {
		end = true;
	}
	
	
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void run() {
		try {
			
			//----------------Create ServerSocket----------------------
			@SuppressWarnings("resource")
			ServerSocket ss = new ServerSocket(listnerPort);
			end = false;
			while(!end) {
				
				//----------------Accept WebService Connection------------------------
				Socket s = ss.accept();
		
				InputStream is = s.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
							
				BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), "UTF8"));
					
				//------------------Read the secret key from connection------------------
				ObjectInputStream ois = new ObjectInputStream(is);
				SecretKey secretKey = (SecretKey) ois.readObject();
				//-----------------------------------------------------------------------
				
				//----------------Read The command from connection----------------------
				StringBuilder response = new StringBuilder();
			    String responseLine = null;
			    while ((responseLine = br.readLine()) != null) {
			        if(responseLine.equals("eof")) {
			        	break;
			        }
			        else {
			        	response.append(responseLine.trim());
			        }		    	
			    }
			    
			    String param = decrypt(response.toString(),secretKey);
				LoggerClass.getLOGGER().info("Remote client configurations: Web service " + s.getInetAddress());
			    JSONObject jo = new JSONObject(param.toString());
			   
			    
			    switch(jo.getInt("configType")) {
			    
			    case 1: //Change User Password
			    	
			    	LoggerClass.getLOGGER().info("Changing machine password... ");
			    	if(FrameConnection.getMachineState().getMachineInfo().isWindows()) {
			    		
			    		
			    		InputStream is1 = null;
			    		InputStream is2 = null;
			    		wr.write(changeWindowsPassword(jo.getString("newPassword"),is1,is2) + "\r\n");
						wr.write("eof\r\n");
						wr.flush();  
			    			
						
			    	}
			    	else {
			    		
			    		InputStream is1 = null;
			    		InputStream is2 = null;
			    		wr.write(changeLinuxPassword(jo.getString("newPassword"),is1,is2) + "\r\n");
						wr.write("eof\r\n");
						wr.flush();  
			    	}
			    	
			    	break;
			    	
			    case 2: //ChangeIp
			    	
			    	LoggerClass.getLOGGER().info("Changing network configuration... ");
			    	if(FrameConnection.getMachineState().getMachineInfo().isWindows()) {
			    		
			    		InputStream is1 = null;
			    		InputStream is2 = null;
			    		wr.write(changeWindowsIp(jo.getString("network"),jo.getString("ip"),jo.getString("netmask"),is1,is2) + "\r\n");
			    		wr.write("eof\r\n");
			    		wr.flush();	
			    		
			    	}
			    	else {
			    		
			    		InputStream is1 = null;
			    		InputStream is2 = null;
			    		wr.write(changeLinuxIp(jo.getString("network"),jo.getString("ip"),jo.getString("netmask"),is1,is2) + "\r\n");
			    		wr.write("eof\r\n");
			    		wr.flush();	
			    		
			    	}
			    	break;
			    	
			    case 3: //Change Donkey Configurations
			    	
			    	
			    	LoggerClass.getLOGGER().info("Changing agent configuration... ");
			    	
				    if(jo.optBoolean("on")) {
			    	
				    	if(FrameConnection.getRunning()) {
				    		FrameConnection.getOff().doClick();
				    	}
				    	
				    	FrameConnection.getLabel6().setText(jo.optString("host"));
					    FrameConnection.getLabel8().setText(jo.optString("port"));
					    FrameConnection.getLabel10().setText(jo.optString("timer"));	    	
					    FrameConnection.getOn().doClick();
				    		    	
				    }
				    else {
				    	
				    	if(FrameConnection.getRunning()) {
				    		FrameConnection.getOff().doClick();
				    	}
				    	
				    }
				    
				    JSONObject res = new JSONObject();
				    res.put("on", jo.optBoolean("on"));		
				    res.put("host", FrameConnection.getMachineState().getHost());
				    res.put("port", FrameConnection.getMachineState().getPort());
				    res.put("timer", FrameConnection.getMachineState().getTimer());
				    
				    wr.write(res.toString() + "\r\n");
					wr.write("eof\r\n");
					wr.flush();
			    	
			    	break;
			    	
			    default: 
			    	break;   	
			    }
			    
			    
				wr.close();
				br.close();
				s.close();
			}
			

			
		} catch (IOException e) {
			
			LoggerClass.getLOGGER().warning("Listner port " + listnerPort + " is in use.");
			String str = JOptionPane.showInputDialog("Listner port " + getListnerPort() + " is in use: Please change it.");
			while(!FrameConnection.portIsValid(str)) {
				str = JOptionPane.showInputDialog("Please entre a valid port number.");
			}
			if(!str.equals(getListnerPort())) {
				LoggerClass.getLOGGER().info("Listner port has been changed to " + getListnerPort());
			}
			setListnerPort(Integer.parseInt(str));
			this.run();
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {		
			e.printStackTrace();
		} catch (Exception e) {			
			e.printStackTrace();
		}
	
		
	}

	

	@SuppressWarnings("finally")
	public String changeLinuxPassword(String newPassword,InputStream isErr,InputStream is) {
		
		String command = "(echo \""+ newPassword + "\"; echo \"" + newPassword+ "\") | passwd ";
		ProcessBuilder pb = new ProcessBuilder();
		pb.command("/bin/sh", "-c", command);
	
		
		try {
			
			java.lang.Process p = pb.start();
			OutputStream out = p.getOutputStream();
			isErr = p.getErrorStream();
			is = p.getInputStream();
			
						
			out.write((newPassword + "\n").getBytes());
			out.flush();
			Thread.sleep(1000);
			
			out.write((newPassword + "\n").getBytes());
			out.flush();
			Thread.sleep(5000);
			
		
			
		} catch (Exception e) {	
			e.printStackTrace();
		} finally {
			
			return printResults(is) + " " + printResults(isErr);
		}
	    
		
	}
	
	

	@SuppressWarnings("finally")
	public String changeWindowsPassword(String newPassword,InputStream is,InputStream isErr) {
		
		
		FrameConnection.getMachineState().collectData();
		String user = FrameConnection.getMachineState().getMachineInfo().getUserName();
		user = user.replaceAll(" ","");
		System.out.println(user);
		String command = "net user " + user + " " + newPassword;
		ProcessBuilder builder = new ProcessBuilder();
		builder.command("cmd.exe", "/c", command);
        
		try {
			
			java.lang.Process p = builder.start();
			isErr = p.getErrorStream();
			is = p.getInputStream();
	        
	        
	        
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			return printResults(is) + " " + printErrResults(isErr);
		}
        
       
	}
	
	
	@SuppressWarnings("finally")
	public String changeLinuxIp(String network, String ip, String netmask,InputStream is,InputStream isErr) {
 		
		
		String[] cmd = {"/bin/bash","-c","ifconfig " + network + " " + ip + " netmask " + netmask};
		try {

			java.lang.Process pb = Runtime.getRuntime().exec(cmd);
			isErr = pb.getErrorStream();
			is = pb.getInputStream();
	               
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			return printResults(is) + " " + printErrResults(isErr);
		}
		
	}
	

	@SuppressWarnings("finally")
	public String changeWindowsIp(String network, String ip, String netmask,InputStream is, InputStream isErr) {
			
		String command = "netsh interface ip set address name=\"" + network + "\" static " + ip + " " + netmask;
		ProcessBuilder builder = new ProcessBuilder();
		builder.command("cmd.exe", "/c", command);	
			
		try {

			java.lang.Process p = builder.start();
			isErr = p.getErrorStream();
			is = p.getInputStream();
	        
	       			        
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			return printResults(is) + " " + printErrResults(isErr);
		}
	}
	
	
	public String printResults(InputStream is) {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    String line = "";	
	    String output = "";
	    try {
			while ((line = reader.readLine()) != null) {
				if(!line.isEmpty())
				LoggerClass.getLOGGER().info(line);
				output += line;
			}
			return output;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String printErrResults(InputStream is) {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    String line = "";	
	    String output = "";
	    try {
			while ((line = reader.readLine()) != null) {
				if(!line.isEmpty())
				LoggerClass.getLOGGER().info(line);
				output += line;
			}
			return output;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String decrypt(String encryptedText, SecretKey secretKey)
            throws Exception {
        
		Cipher cipher = Cipher.getInstance("AES");
		Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        String decryptedText = new String(decryptedByte);
        return decryptedText;
    }


	public int getListnerPort() {
		return listnerPort;
	}


	public boolean isEnd() {
		return end;
	}


	public void setListnerPort(int listnerPort) {
		this.listnerPort = listnerPort;
	}


	public void setEnd(boolean end) {
		this.end = end;
	}

		

}