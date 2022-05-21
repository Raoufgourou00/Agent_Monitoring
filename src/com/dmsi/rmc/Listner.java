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
			ServerSocket ss = new ServerSocket(listnerPort);
			end = false;
			while(!end) {
				
				Socket s = ss.accept();
				
				InputStream is = s.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
					
				
	
				BufferedWriter wr = new BufferedWriter(
						new OutputStreamWriter(s.getOutputStream(), "UTF8")
						);
				
				ObjectInputStream ois = new ObjectInputStream(is);
				SecretKey secretKey = (SecretKey) ois.readObject();
				//System.out.println(secretKey);
				
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
			    	
			    	if(jo.getString("os").equals("Linux")) {
			    		
			    		//changeLinuxPassword(decrypt(jo.getString("password")),decrypt(jo.getString("newPassword")));
			    		InputStream is1 = null;
			    		wr.write(changeLinuxPassword(jo.getString("password"),jo.getString("newPassword"),is1) + "\r\n");
						wr.write("eof\r\n");
						wr.flush();  	
						
			    	}
			    	else {
			    		// changeWindowsPassword
			    	}
			    	
			    	break;
			    	
			    case 2: //ChangeIp
			    	
			    	if(jo.getString("os").equals("Linux")) {
			    		
			    		JSONObject res = new JSONObject();
			    		res.put("status", changeLinuxIp(jo.getString("ip"),jo.getString("netmask"),jo.getString("network"),jo.getString("password")));
			    		wr.write(res.toString() + "\r\n");
			    		wr.write("eof\r\n");
			    		wr.flush();	
			    	}
			    	else {
			    		//ChangeWindowsIp
			    	}
			    	break;
			    	
			    case 3: //Change Donkey Configurations
			    	
				    boolean okey = true;			       
				    if(jo.optBoolean("on")) {
			    	
				    	if(FrameConnection.getRunning()) {
				    		FrameConnection.getOff().doClick();
				    	}
				    	
				    	FrameConnection.getLabel6().setText(jo.optString("host"));
					    FrameConnection.getLabel8().setText(new Integer(jo.optInt("port")).toString());
					    FrameConnection.getLabel10().setText(new Integer(jo.optInt("timer")).toString());	    	
					    FrameConnection.getOn().doClick();
				    	
				    	if(!FrameConnection.getMachineState().getHost().equals(jo.optString("host"))) {
				    		okey = false;
				    	}
				    	else {
				    		
				    		if(!(FrameConnection.getMachineState().getPort() == jo.optInt("port"))) {
				    			okey = false;
				    		}
				    		else {
		
			    				if(!(FrameConnection.getMachineState().getTimer() == jo.optInt("timer"))) {
					    			okey = false;
					    		}
				    		}
				    	}	    	
				    }
				    else {
				    	FrameConnection.getOff().doClick();
				    }
				    
				    JSONObject res = new JSONObject();
				    res.put("status", okey);			    
				    
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
				LoggerClass.getLOGGER().warning("Listner port has been changed to " + getListnerPort());
			}
			setListnerPort(Integer.parseInt(str));
			this.run();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
	}

	

	@SuppressWarnings("finally")
	public String changeLinuxPassword(String password,String newPassword,InputStream isErr) {
		
		String command = "(echo \"" + password + "\"; echo \""+ newPassword + "\"; echo \"" + newPassword+ "\") | passwd ";
		ProcessBuilder pb = new ProcessBuilder();
		pb.command("/bin/sh", "-c", command);
		java.lang.Process p;
		
		try {
			p = pb.start();
			OutputStream out = p.getOutputStream();
			isErr = p.getErrorStream();
			
			out.write((password + "\n").getBytes());
			out.flush();
			Thread.sleep(1000);
			
			
			out.write((newPassword + "\n").getBytes());
			out.flush();
			Thread.sleep(1000);
	
			
			out.write((newPassword + "\n").getBytes());
			out.flush();
			Thread.sleep(5000);
			
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} finally {
			
			return printResults(isErr);
		}
	    
		
	}
	
	
	public boolean changeLinuxIp(String ip, String netmask,String network,String password) throws IOException {
 		
		String[] cmd = {"/bin/bash","-c","echo " + password + "| sudo -S ifconfig " + network + " " + ip + " netmask " + netmask};
		java.lang.Process pb = Runtime.getRuntime().exec(cmd);
		 
		FrameConnection.getMachineState().collectData();
		if(FrameConnection.getMachineState().getLastIpAddress().equals(ip)) {
			return true;
		}
		return false;
	}
	
	
	public String printResults(InputStream is) {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    String line = "";	
	    String output = null;
	    try {
			while ((line = reader.readLine()) != null) {
				output = line;
			}
			return output;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
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


}
