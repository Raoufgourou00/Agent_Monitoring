package com.dmsi.rmc.machine;

import org.hyperic.sigar.NetConnection;
import org.json.JSONObject;


public class NetworkConnection {
	
	private String foreignAddress;
	private long foreignPort;
	private String localAddress;
	private long localPort;
	private String protocol;
	private int protocolNum;
	private String state;
	private int stateNum;
	
	
	public NetworkConnection() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void initialise(NetConnection netConnection) {
		
		this.foreignAddress = netConnection.getRemoteAddress();
		this.foreignPort = netConnection.getRemotePort();
		this.localAddress = netConnection.getLocalAddress();
		this.localPort = netConnection.getLocalPort();
		this.protocol = netConnection.getTypeString();
		this.protocolNum = netConnection.getType();
		this.state = netConnection.getStateString();
		this.stateNum = netConnection.getState();
		
	}

	public String getForeignAddress() {
		return foreignAddress;
	}

	public long getForeignPort() {
		return foreignPort;
	}

	public String getLocalAddress() {
		return localAddress;
	}

	public long getLocalPort() {
		return localPort;
	}

	public String getProtocol() {
		return protocol;
	}

	public int getProtocolNum() {
		return protocolNum;
	}

	public String getState() {
		return state;
	}

	public int getStateNum() {
		return stateNum;
	}


	
	@Override
	public String toString() {
		return String.format(
				"NetworkConnection [foreignAddress=%s, foreignPort=%s, localAddress=%s, localPort=%s, protocol=%s, protocolNum=%s, state=%s, stateNum=%s]",
				foreignAddress, foreignPort, localAddress, localPort, protocol, protocolNum, state, stateNum);
	}

	public JSONObject toJSONObject() {
		JSONObject jo = new JSONObject();
		jo.put("foreignAddress",foreignAddress);
		jo.put("foreignPort",foreignPort);
		jo.put("localAddress",localAddress);
		jo.put("localPort",localPort);
		jo.put("protocol",protocol);
		jo.put("protocolNum",protocolNum);
		jo.put("state",state);
		jo.put("stateNum",stateNum);
		return jo;
	}
	
}
