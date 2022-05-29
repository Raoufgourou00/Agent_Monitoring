package com.dmsi.rmc;

import java.util.ArrayList;
import java.util.Locale;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.NetConnection;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.JSONArray;
import org.json.JSONObject;

public class MachineInfo {
	
	private String host;
	private String userName;
	private String language;
	private String fqdn;
	private int totalCpus;
	private String defaultGatway;
	private String primaryDNS;
	
	
	private OS os;
	private Memory memory;
	private ArrayList<Media> mediaList;
	private ArrayList<CPU> cpuList;
	private ArrayList<NetworkInterface> networkInterfaceList;
	private ArrayList<Process> processList;
	private ArrayList<NetworkConnection> networkConnectionList;
	private String hostName;
	private String domainName;
	private String secondaryDns;
	

	
	public MachineInfo(Sigar sigar) {
			
		super();
		
		// Medias	
		mediaList = new ArrayList<Media>();
		FileSystem[] fileSystemList;
		try {
			fileSystemList = sigar.getFileSystemList();
			for(int i = 0 ;i<fileSystemList.length ;i++) {
				
				FileSystem fileSystem = fileSystemList[i];
				if(fileSystem.getType() == FileSystem.TYPE_LOCAL_DISK) {

					Media media = new Media(fileSystem);
					mediaList.add(media);
				}
				
			}
			
		} catch (SigarException e2) {
			LoggerClass.getLOGGER().warning("Can't Get Medias List: Permission Denied");
		}
		
		
		// CPUs
		cpuList = new ArrayList<CPU>();
		org.hyperic.sigar.CpuInfo[] cpuInfoList;
		try {
			cpuInfoList = sigar.getCpuInfoList();
			CpuPerc[] cpuPercList = sigar.getCpuPercList();
			for(int i = 0 ;i<cpuInfoList.length ;i++) {
				
				org.hyperic.sigar.CpuInfo cpuInfo = cpuInfoList[i];
				CpuPerc cpuPerc = cpuPercList[i];
				CPU cpu = new CPU(cpuInfo,cpuPerc,i);
				cpuList.add(cpu);
			}
			
		} catch (SigarException e1) {
			LoggerClass.getLOGGER().warning("Can't Get CPUs List: Permission Denied");
		}
		
		
		// Memory
		memory = new Memory(sigar);
		
		// Network Interfaces
		networkInterfaceList = new ArrayList<NetworkInterface>();
		String[] netInterfaces;
		try {
			netInterfaces = sigar.getNetInterfaceList();
			for(int i = 0 ; i<netInterfaces.length ; i++) {
				
				NetworkInterface networkInterface = new NetworkInterface();
				 
				if(networkInterface.initialise(netInterfaces[i], sigar)) {
					networkInterfaceList.add(networkInterface);
				}	
			}
		} catch (SigarException e) {
			LoggerClass.getLOGGER().warning("Can't Get Network Interface List: Permission Denied");
		}
		
		// OS
		os = new OS();
		
		// ProcessList
		processList = new ArrayList<Process>();
		long[] process;
		try {
			process = sigar.getProcList();
			for(int i = 0 ; i<process.length ; i++) {
				
				Process p = new Process();
				p = Process.initialise(process[i], sigar);
				if(p != null) {
					processList.add(p);
				}			
			}
		} catch (SigarException e) {
			LoggerClass.getLOGGER().warning("Can't Get Process List: Permission Denied");
		}
		
		
		// NetworkConnections
		networkConnectionList = new ArrayList<NetworkConnection>();
		int flags = NetFlags.CONN_CLIENT | NetFlags.CONN_PROTOCOLS | NetFlags.CONN_RAW | NetFlags.CONN_SERVER | NetFlags.CONN_TCP | NetFlags.CONN_UDP | NetFlags.CONN_UNIX ;
		NetConnection[] ncl;
		try {
			ncl = sigar.getNetConnectionList(flags);
			for(int i = 0 ; i<ncl.length ; i++) {
				NetworkConnection networkConnection = new NetworkConnection();
				networkConnection.initialise(ncl[i]);
				networkConnectionList.add(networkConnection);
			}
		} catch (SigarException e) {
			LoggerClass.getLOGGER().warning("Can't Get Network Connection List: Permission Denied");
		}
		
		
		// fqdn
		try {
			this.fqdn = sigar.getFQDN();
		} catch (SigarException e) {
			LoggerClass.getLOGGER().warning("Can't Get FQDN: Permission Denied");
		}
		
		
		// hostName
		org.hyperic.sigar.NetInfo netInfo;
		try {
			netInfo = sigar.getNetInfo();
			this.host = netInfo.getHostName();
		} catch (SigarException e) {
			LoggerClass.getLOGGER().warning("Can't Get Host Name: Permission Denied");
		}
		
		
		// Language
		Locale locale = Locale.getDefault();
		String lang = locale.getDisplayLanguage();
		String country = locale.getDisplayCountry();
		this.language =  lang + " (" + country + ")"; 
		
		
		// Total CPUs
		this.totalCpus = this.getCpuList().size();
		
		// UserName
		this.userName = System.getProperty("user.name");
		
		
		try {
			
			this.defaultGatway = sigar.getNetInfo().getDefaultGateway();
			this.primaryDNS = sigar.getNetInfo().getPrimaryDns();
			this.secondaryDns = sigar.getNetInfo().getSecondaryDns();
			this.domainName = sigar.getNetInfo().getDomainName();
			this.hostName = sigar.getNetInfo().getHostName();
			
		} catch (SigarException e) {
			LoggerClass.getLOGGER().warning("Can't get the default getway: Permission Denied");
		}
		

	}

	
	
	public String getHost() {
		return host;
	}


	public String getUserName() {
		return userName;
	}

	public String getLanguage() {
		return language;
	}



	public String getFqdn() {
		return fqdn;
	}


	public int getTotalCpus() {
		return totalCpus;
	}

	public OS getOs() {
		return os;
	}

	public Memory getMemory() {
		return memory;
	}

	public ArrayList<Media> getMediaList() {
		return mediaList;
	}

	public ArrayList<CPU> getCpuList() {
		return cpuList;
	}

	public ArrayList<NetworkInterface> getNetworkInterfaceList() {
		return networkInterfaceList;
	}

	public ArrayList<Process> getProcessList() {
		return processList;
	}

	public ArrayList<NetworkConnection> getNetworkConnectionList() {
		return networkConnectionList;
	}


	
    @Override
	public String toString() {
		return String.format(
				"MachineInfo [host=%s, userName=%s, language=%s, fqdn=%s, totalCpus=%s, os=%s, memory=%s, mediaList=%s, cpuList=%s, networkInterfaceList=%s, processList=%s, networkConnectionList=%s]",
				host, userName, language, fqdn, totalCpus, os, memory, mediaList, cpuList, networkInterfaceList,
				processList, networkConnectionList);
	}



    public boolean isWindows() {
		return OperatingSystem.IS_WIN32;
	}




	public String getDefaultGatway() {
		return defaultGatway;
	}



	public void setDefaultGatway(String defaultGatway) {
		this.defaultGatway = defaultGatway;
	}



	public String getPrimaryDNS() {
		return primaryDNS;
	}



	public void setPrimaryDNS(String primaryDNS) {
		this.primaryDNS = primaryDNS;
	}



	public String getHostName() {
		return hostName;
	}



	public void setHostName(String hostName) {
		this.hostName = hostName;
	}



	public String getDomainName() {
		return domainName;
	}



	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}



	public String getSecondaryDns() {
		return secondaryDns;
	}



	public void setSecondaryDns(String secondaryDns) {
		this.secondaryDns = secondaryDns;
	}



	public JSONObject toJSONObject() {
    	
    	JSONObject jo = new JSONObject();
    	
    	jo.put("host", this.getHost());
    	jo.put("userName", this.getUserName());
    	jo.put("language", this.getLanguage());
    	jo.put("fqdn", this.getFqdn());
    	jo.put("totalCpus", this.getTotalCpus());
    	
    	jo.put("memory", getMemory().toJSONObject());
    	
    	JSONArray jaCpus = new JSONArray();
		for(CPU cpu : getCpuList()) {
			jaCpus.put(cpu.toJSONObject());
		}
		
		JSONArray jaMedias = new JSONArray();
		for(Media media : getMediaList()) {
			jaMedias.put(media.toJSONObject());
		}
		
	
		JSONArray jaNetConnections = new JSONArray();
		for(NetworkConnection nc : getNetworkConnectionList()) {
			jaNetConnections.put(nc.toJSONObject());
		}
		
		JSONArray jaProcess = new JSONArray();
		for(Process p : getProcessList()) {
			jaProcess.put(p.toJSONObject());
		}
			
		jo.put("cpuList", jaCpus);
		jo.put("mediaList", jaMedias);
		jo.put("networkConnectionList", jaNetConnections);
		jo.put("processList", jaProcess);
    	
    	return jo;
    }
	

	
}
