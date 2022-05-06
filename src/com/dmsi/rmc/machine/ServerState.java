package com.dmsi.rmc.machine;



import java.text.SimpleDateFormat;
import java.util.Date;


import java.util.Properties;

import org.hyperic.sigar.Sigar;

import org.json.JSONArray;
import org.json.JSONObject;


import com.dmsi.rmc.machine.NetworkInterface;
import com.dmsi.rmc.machine.ServerInfo;

public class ServerState {

	 Sigar sigar = new Sigar();
	 
	 
	 //------------Server Setting------------------
	 private int port;
	 private String context;
	 private String host;
	 private int timer;  
	 //----------------------------------------
	 
	 
	 
	 //-------------ServerInfo --------------
	 private ServerInfo serverInfo;
	 //-------------------------------------
	 
	 
	 
	 
	 //-----------------Constructors--------------------------------------------
     public ServerState(){
		super();
	 }
      
     public ServerState(String host, int port, String context, int timer) {
		super();
		this.port = port;
		this.context = context;
		this.host = host;
		this.timer = timer;
	 }
     //--------------------------------------------------------------------------

     
     
     //------------------Collect Data---------------
	 public void collectData() {
    	 this.serverInfo = new ServerInfo(sigar);
     }
     //----------------------------------------------
	 
	 
	 
	 //-------------------Java Stuffs--------------------
     public String getJavaHome() {
    	 Properties props = System.getProperties();
 		 return props.getProperty("java.home");
     }
     
     public String getJavaVmVersion() {
    	 Properties props = System.getProperties();
    	 return props.getProperty("java.vm.specification.version");
     }
	
     public String  getJavaVmVendor() {
    	 Properties props = System.getProperties();
    	 return props.getProperty("java.vm.specification.vendor");
     }
     //---------------------------------------------------
    
     
     
     //----------------------LastIpAddress--------------------------------
     public String getLastIpAddress() {
    	 for(int i = 0;i<serverInfo.getNetworkInterfaceList().size();i++) {
    		 if(serverInfo.getNetworkInterfaceList().get(i).getPrimaryIpAddress() != null) {
    			 return serverInfo.getNetworkInterfaceList().get(i).getPrimaryIpAddress();	 
    		 }
    	 }
    	 return null;
     }
     //--------------------------------------------------------------------------
     
     
     
     
     //---------------------LastMacAddress----------------------------------------
     public String getLastMacAddress() {
    	 for(int i = 0;i<serverInfo.getNetworkInterfaceList().size();i++) {
    		 if(serverInfo.getNetworkInterfaceList().get(i).getPrimaryMacAddress() != null) {
    			 return serverInfo.getNetworkInterfaceList().get(i).getPrimaryMacAddress();	 
    		 }
    	 }
    	 return null;
     }
     //---------------------------------------------------------------------------------
     
     
     //--------------Date of the day--------------------
     public String getToday() {
    	 SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    	 return date.format(new Date());
     }
     //-----------------------------------------------
     
     
     
     //-------------------------------Getters----------------------------------
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
	//-----------------------------------------------
	
	
	
	//-------------------Setters-----------------------
	public void setContext(String context) {
		this.context = context;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}
    //---------------------------------------------------
	
	
	
	//-----------------Print The Data------------------------
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
	//--------------------------------------------------------------------------
	
	
	public String toUrl() {
		return "http://"+host+":"+port+"/"+context;
	}

	
	
	
     
    
     
}


