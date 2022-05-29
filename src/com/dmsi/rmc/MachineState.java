package com.dmsi.rmc;



import java.text.SimpleDateFormat;
import java.util.Date;


import java.util.Properties;

import org.hyperic.sigar.Sigar;

import org.json.JSONArray;
import org.json.JSONObject;

public class MachineState {

	 Sigar sigar = new Sigar();
	 
	 
	 //------------Server Setting------------------
	 private int port;
	 private final String context = "saveMachine";
	 private String host;
	 private int timer;  
	 private int portListner;
	 //----------------------------------------
	 
	 
	 
	 //-------------MachineInfo --------------
	 private MachineInfo machineInfo;
	 private String machineType;
	 //-------------------------------------
	 
	 
	 
	 
	 //-----------------Constructors--------------------------------------------
     public MachineState(){
		super();
	 }
     
    
     public MachineState(String host, int port,int timer,String machineType,int portListner) {
		super();
		this.port = port;
		this.host = host;
		this.timer = timer;
		this.machineType = machineType;
		this.portListner = portListner;
	 }
     //--------------------------------------------------------------------------

     
     
     //------------------Collect Data---------------
	 public void collectData() {
    	 this.machineInfo = new MachineInfo(sigar);
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
    	 return getMachineInfo().getFqdn();
     }
     //--------------------------------------------------------------------------
     
     
     
     
     //---------------------LastMacAddress----------------------------------------
     public String getLastMacAddress() {
    	 for(int i = 0;i<machineInfo.getNetworkInterfaceList().size();i++) {
    		 if(machineInfo.getNetworkInterfaceList().get(i).getPrimaryIpAddress().equals(getLastIpAddress())) {
    			 return machineInfo.getNetworkInterfaceList().get(i).getPrimaryMacAddress();	 
    		 }
    	 }
    	 return null;
     }
     
     
     public String getLastNetworkName() {
    	 for(int i = 0;i<machineInfo.getNetworkInterfaceList().size();i++) {
    		 if(machineInfo.getNetworkInterfaceList().get(i).getPrimaryIpAddress().equals(getLastIpAddress())) {
    			 return machineInfo.getNetworkInterfaceList().get(i).getPrimaryInterface();	 
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
    	 return machineInfo.getHost();
     }
     
     public String getLastSystem()
     {
    	 return machineInfo.getOs().getName();
     }

     public String getMachineId() {
    	 return getLastMacAddress();
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
	 
	 public int getPortListner() {
			return portListner;
		}

	public MachineInfo getMachineInfo() {
		return machineInfo;
	}
	
	
	public String getMachineType() {
		return machineType;
	}


	//-----------------------------------------------
	public void setMachineType(String machineType) {
		this.machineType = machineType;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	

	public void setPortListner(int portListner) {
		this.portListner = portListner;
	}

	public void setMachineInfo(MachineInfo machineInfo) {
		this.machineInfo = machineInfo;
	}

	//-------------------Setters-----------------------
	public void setHost(String host) {
		this.host = host;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}
    //---------------------------------------------------
	
	
	
	//-----------------Print The Data------------------------
	public JSONObject toJSONObject() {
		
		
		JSONObject joMachine = new JSONObject();
		joMachine.put("machineId", getMachineId());
		joMachine.put("machineType", getMachineType());
		joMachine.put("isConnected", true);
		joMachine.put("lastIpAddress", getLastIpAddress());
		joMachine.put("lastMacAddress", getLastMacAddress());
		joMachine.put("lastName", getLastName());
		joMachine.put("lastSystem", getLastSystem());
		
		JSONObject joMachineArchitecture = new JSONObject();
		joMachineArchitecture.put("javaHome", getJavaHome());
		joMachineArchitecture.put("javaVmVendor", getJavaVmVendor());
		joMachineArchitecture.put("javaVmVersion", getJavaVmVersion());
		joMachineArchitecture.put("cpuModel", getMachineInfo().getCpuList().get(0).getModel());
		joMachineArchitecture.put("cpuMhz", getMachineInfo().getCpuList().get(0).getMhz());
		joMachineArchitecture.put("cpuVendor", getMachineInfo().getCpuList().get(0).getVendor());
		joMachineArchitecture.put("osArch",getMachineInfo().getOs().getArch());
		joMachineArchitecture.put("osCpuEndian",getMachineInfo().getOs().getCpuEndian());
		joMachineArchitecture.put("osDataModel",getMachineInfo().getOs().getDataModel());
		joMachineArchitecture.put("osDescription",getMachineInfo().getOs().getDescription());
		joMachineArchitecture.put("osMachine",getMachineInfo().getOs().getMachine());
		joMachineArchitecture.put("osName",getMachineInfo().getOs().getName());
		joMachineArchitecture.put("osPatchLevel",getMachineInfo().getOs().getPatchLevel());
		joMachineArchitecture.put("osVendor",getMachineInfo().getOs().getVendor());
		joMachineArchitecture.put("osVendorCodeName",getMachineInfo().getOs().getVendorCodeName());
		joMachineArchitecture.put("osVendorName",getMachineInfo().getOs().getVendorName());
		joMachineArchitecture.put("osVendorVersion",getMachineInfo().getOs().getVendorVersion());
		joMachineArchitecture.put("osVersion",getMachineInfo().getOs().getVersion());
		
		
		JSONObject joMachineSetting = new JSONObject();
		joMachineSetting.put("host", getHost());
		joMachineSetting.put("port", getPort());
		//joMachineSetting.put("context", getContext());
		joMachineSetting.put("timer", getTimer());
		joMachineSetting.put("portListner", getPortListner());
		
		
		JSONArray jaNetInterfaces = new JSONArray();
		for(NetworkInterface ni : getMachineInfo().getNetworkInterfaceList()) {
			jaNetInterfaces.put(ni.toJSONObject());
		}
		
		joMachine.put("machineArchitecture", joMachineArchitecture);
		joMachine.put("machineSetting", joMachineSetting);
		joMachine.put("networkInterfaceList", jaNetInterfaces);
		
		
		JSONObject joMachineState = new JSONObject();
		joMachineState.put("date", getToday());
		joMachineState.put("machine", joMachine);
		joMachineState.put("machineInfo", getMachineInfo().toJSONObject());
		
		
		return joMachineState;
	}
	//--------------------------------------------------------------------------
	
	
	public String toUrl() {
		return "http://"+host+":"+port+"/"+context;
	}

	
	
	
     
    
     
}


