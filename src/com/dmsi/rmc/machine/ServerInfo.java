package com.dmsi.rmc.machine;

import java.util.ArrayList;
import java.util.Locale;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.NetConnection;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.JSONArray;
import org.json.JSONObject;

public class ServerInfo {
	
	private String host;
	private String userName;
	private String language;
	private String fqdn;
	private int totalCpus;
	
	
	private OS os;
	private Memory memory;
	private ArrayList<Media> mediaList;
	private ArrayList<CPU> cpuList;
	private ArrayList<NetworkInterface> networkInterfaceList;
	private ArrayList<Process> processList;
	private ArrayList<NetworkConnection> networkConnectionList;
	

	
	public ServerInfo(Sigar sigar) {
			
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
			System.err.println("Can't Get Medias List");
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
			System.err.println("Can't Get CPUs List");
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
			System.err.println("Err: Can't Get Network Interface List");
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
				p.initialise(process[i], sigar);
				processList.add(p);
			}
		} catch (SigarException e) {
			System.err.println("Err: Can't Get Process List");
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
			System.err.println("Err: Can't Get Network Connection List");
		}
		
		
		// fqdn
		try {
			this.fqdn = sigar.getFQDN();
		} catch (SigarException e) {
			System.err.println("Err: Can't Get FQDN");
		}
		
		
		// hostName
		org.hyperic.sigar.NetInfo netInfo;
		try {
			netInfo = sigar.getNetInfo();
			this.host = netInfo.getHostName();
		} catch (SigarException e) {
			System.err.println("Err: Can't Get Host Name");
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
				"ServerInfo [host=%s, userName=%s, language=%s, fqdn=%s, totalCpus=%s, os=%s, memory=%s, mediaList=%s, cpuList=%s, networkInterfaceList=%s, processList=%s, networkConnectionList=%s]",
				host, userName, language, fqdn, totalCpus, os, memory, mediaList, cpuList, networkInterfaceList,
				processList, networkConnectionList);
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
