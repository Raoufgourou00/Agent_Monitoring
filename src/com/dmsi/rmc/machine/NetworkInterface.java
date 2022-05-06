package com.dmsi.rmc.machine;

import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.JSONObject;

public class NetworkInterface {

	private String primaryInterface;
	private String primaryIpAddress;
	private String primaryMacAddress;
	private String primaryNetMask; 
	private long mtu;
	private String description; // full name
	private String type;
	
	/*
	private String defaultGetway;
	private String domainName;
	private String hostName;
	private String primaryDNS;
	private String secondryDNS; 
	*/
	
	public NetworkInterface() {
		super();
	}
	
	public boolean initialise(String netInterface, Sigar sigar) {
		
		NetInterfaceConfig netInterfaceConfig;
		try {
			netInterfaceConfig = sigar.getNetInterfaceConfig(netInterface);
			if (NetFlags.LOOPBACK_ADDRESS.equals(netInterfaceConfig.getAddress()) || (netInterfaceConfig.getFlags() & NetFlags.IFF_LOOPBACK) != 0
	                || NetFlags.NULL_HWADDR.equals(netInterfaceConfig.getHwaddr())) {
				return false;
	        }
			
			this.description = netInterfaceConfig.getDescription();
			this.mtu = netInterfaceConfig.getMtu();
			this.primaryInterface = netInterfaceConfig.getName();
			this.primaryIpAddress = netInterfaceConfig.getAddress();
			this.primaryMacAddress = netInterfaceConfig.getHwaddr();
			this.primaryNetMask = netInterfaceConfig.getNetmask();
			this.type = netInterfaceConfig.getType();
				
		} catch (SigarException e) {
			System.err.println("Err: Can't Get Network Interface Configuration Of"+ "'" + netInterface + "'");
			return false;
		}
		
		return true;
		
		
		/*
		org.hyperic.sigar.NetInfo netInfo = sigar.getNetInfo();
		
		this.defaultGetway = netInfo.getDefaultGateway();
		this.domainName = netInfo.getDomainName();
		this.hostName = netInfo.getHostName();
		this.primaryDNS = netInfo.getPrimaryDns();
		this.secondryDNS = netInfo.getSecondaryDns();
		
		*/	
	}

	public String getPrimaryInterface() {
		return primaryInterface;
	}

	public String getPrimaryIpAddress() {
		return primaryIpAddress;
	}

	public String getPrimaryMacAddress() {
		return primaryMacAddress;
	}

	public String getPrimaryNetMask() {
		return primaryNetMask;
	}
	
	public long getMtu() {
		return mtu;
	}

	public String getDescription() {
		return description;
	}

	public String getType() {
		return type;
	}
	
	/*
	public String getDefaultGetway() {
		return defaultGetway;
	}

	public String getDomainName() {
		return domainName;
	}

	public String getHostName() {
		return hostName;
	}

	public String getPrimaryDNS() {
		return primaryDNS;
	}

	public String getSecondryDNS() {
		return secondryDNS;
	}
	*/
	

	
	public JSONObject toJSONObject() {
		JSONObject jo = new JSONObject();
		jo.put("primaryInterface",primaryInterface);
		jo.put("primaryIpAddress",primaryIpAddress);
		jo.put("primaryMacAddress",primaryMacAddress);
		jo.put("description",description);
		jo.put("primaryNetMask",primaryNetMask);
		jo.put("mtu",mtu);
		jo.put("description",description);
		jo.put("type",type);
		return jo;
	}

	@Override
	public String toString() {
		return String.format(
				"NetworkInterface [primaryInterface=%s, primaryIpAddress=%s, primaryMacAddress=%s, primaryNetMask=%s, mtu=%s, description=%s, type=%s]",
				primaryInterface, primaryIpAddress, primaryMacAddress, primaryNetMask, mtu, description, type);
	}
	
	
	
	
}
