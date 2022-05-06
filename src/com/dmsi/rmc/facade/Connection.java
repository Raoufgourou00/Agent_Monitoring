package com.dmsi.rmc.facade;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;

import java.net.URL;


import java.text.SimpleDateFormat;
import java.util.Date;


import java.util.Properties;

import org.hyperic.sigar.Sigar;

import org.json.JSONArray;
import org.json.JSONObject;


import com.dmsi.rmc.machine.NetworkInterface;
import com.dmsi.rmc.machine.ServerInfo;

public class Connection {

	 Sigar sigar = new Sigar();
	 
	 
	 // Note: Facade is the ServerState Class in the WebApp
	// Server Setting CLass
	 private int port;
	 private String context;
	 private String host;
	 private int timer;  // in minutes
	 //---------------------------
	 
			 // ServerInfo Class
	 private ServerInfo serverInfo;
	 
	 
	 
	 
	 
     public Connection(){
		super();
	}
     
     
     
     public Connection(String host, int port, String context, int timer) {
		super();
		this.port = port;
		this.context = context;
		this.host = host;
		this.timer = timer;
	}



	public void collectData() {
    	 this.serverInfo = new ServerInfo(sigar);
     }
     
	// JavaHome
     public String getJavaHome() {
    	 Properties props = System.getProperties();
 		 return props.getProperty("java.home");
     }
     
     // JavaVmVersion
     public String getJavaVmVersion() {
    	 Properties props = System.getProperties();
    	 return props.getProperty("java.vm.specification.version");
     }
	
     // JavaVmVendor
     public String  getJavaVmVendor() {
    	 Properties props = System.getProperties();
    	 return props.getProperty("java.vm.specification.vendor");
     }
     
     // LastIpAddress
     public String getLastIpAddress() {
    	 for(int i = 0;i<serverInfo.getNetworkInterfaceList().size();i++) {
    		 if(serverInfo.getNetworkInterfaceList().get(i).getPrimaryIpAddress() != null) {
    			 return serverInfo.getNetworkInterfaceList().get(i).getPrimaryIpAddress();	 
    		 }
    	 }
    	 return null;
     }
     
     public String getLastMacAddress() {
    	 for(int i = 0;i<serverInfo.getNetworkInterfaceList().size();i++) {
    		 if(serverInfo.getNetworkInterfaceList().get(i).getPrimaryMacAddress() != null) {
    			 return serverInfo.getNetworkInterfaceList().get(i).getPrimaryMacAddress();	 
    		 }
    	 }
    	 return null;
     }
     
     public String getLastName() {
    	 return serverInfo.getHost();
     }
     
     public String getLastSystem()
     {
    	 return serverInfo.getOs().getName();
     }

     public String getServerId() {
    	 return getLastMacAddress();
     }
     
     public String getToday() {
    	 SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    	 return date.format(new Date());
     }

	public ServerInfo getServerInfo() {
		return serverInfo;
	}

	public int getPort() {
		return port;
	}

	public String getContext() {
		return context;
	}

	public String getHost() {
		return host;
	}


	public int getTimer() {
		return timer;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public void setHost(String host) {
		this.host = host;
	}


	public void setTimer(int timer) {
		this.timer = timer;
	}
     
	public JSONObject toJSONObject() {
		
		
		JSONObject joServer = new JSONObject();
		joServer.put("serverId", getServerId());
		joServer.put("isConnected", true);
		joServer.put("lastIpAddress", getLastIpAddress());
		joServer.put("lastMacAddress", getLastMacAddress());
		joServer.put("lastName", getLastName());
		joServer.put("lastSystem", getLastSystem());
		
		JSONObject joServerArchitecture = new JSONObject();
		joServerArchitecture.put("javaHome", getJavaHome());
		joServerArchitecture.put("javaVmVendor", getJavaVmVendor());
		joServerArchitecture.put("javaVmVersion", getJavaVmVersion());
		joServerArchitecture.put("cpuModel", getServerInfo().getCpuList().get(0).getModel());
		joServerArchitecture.put("cpuMhz", getServerInfo().getCpuList().get(0).getMhz());
		joServerArchitecture.put("cpuVendor", getServerInfo().getCpuList().get(0).getVendor());
		joServerArchitecture.put("osArch",getServerInfo().getOs().getArch());
		joServerArchitecture.put("osCpuEndian",getServerInfo().getOs().getCpuEndian());
		joServerArchitecture.put("osDataModel",getServerInfo().getOs().getDataModel());
		joServerArchitecture.put("osDescription",getServerInfo().getOs().getDescription());
		joServerArchitecture.put("osMachine",getServerInfo().getOs().getMachine());
		joServerArchitecture.put("osName",getServerInfo().getOs().getName());
		joServerArchitecture.put("osPatchLevel",getServerInfo().getOs().getPatchLevel());
		joServerArchitecture.put("osVendor",getServerInfo().getOs().getVendor());
		joServerArchitecture.put("osVendorCodeName",getServerInfo().getOs().getVendorCodeName());
		joServerArchitecture.put("osVendorName",getServerInfo().getOs().getVendorName());
		joServerArchitecture.put("osVendorVersion",getServerInfo().getOs().getVendorVersion());
		joServerArchitecture.put("osVersion",getServerInfo().getOs().getVersion());
		
		
		JSONObject joServerSetting = new JSONObject();
		joServerSetting.put("host", getHost());
		joServerSetting.put("port", getPort());
		joServerSetting.put("context", getContext());
		joServerSetting.put("timer", getTimer());
		
		
		JSONArray jaNetInterfaces = new JSONArray();
		for(NetworkInterface ni : getServerInfo().getNetworkInterfaceList()) {
			jaNetInterfaces.put(ni.toJSONObject());
		}
		
		joServer.put("serverArchitecture", joServerArchitecture);
		joServer.put("serverSetting", joServerSetting);
		joServer.put("networkInterfaceList", jaNetInterfaces);
		
		
		JSONObject joServerState = new JSONObject();
		joServerState.put("date", getToday());
		joServerState.put("server", joServer);
		joServerState.put("serverInfo", getServerInfo().toJSONObject());
		
		
		return joServerState;
	}

	public Boolean sendTextData(String data) {
		boolean ok = true;
		try {
			URL url = new URL ("http",this.host,this.port,this.context);
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
    
     
    
     
}


