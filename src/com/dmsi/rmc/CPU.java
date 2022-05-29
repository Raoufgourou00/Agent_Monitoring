package com.dmsi.rmc;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.json.JSONObject;

public class CPU {


	private int cpuIndex; // ID
	private String vendor;
	private long cacheSize; // KB
	private int coresPerSocket;
	private int mhz;
	private String model;
	private int totalCores;
	private int totalSockets;	
	private double combined; // Usage
	private double idleTime;
	private double irqTime;
	private double niceTime;
	private double softirqTime;
	private double stolenTime;
	private double sysTime;
	private double userTime;
	private double waitTime;
	
	public CPU(CpuInfo cpuInfo,CpuPerc cpuPerc,int index) {
		super();
		initialise(cpuInfo,cpuPerc,index);
		
	} 
	
	public void initialise(CpuInfo cpuInfo,CpuPerc cpuPerc,int index) {
		
		this.cpuIndex = index;
		this.cacheSize = cpuInfo.getCacheSize();
		this.coresPerSocket = cpuInfo.getCoresPerSocket();
		this.mhz = cpuInfo.getMhz();
		this.model = cpuInfo.getModel();
		this.totalCores = cpuInfo.getTotalCores();
		this.totalSockets = cpuInfo.getTotalSockets();
		this.vendor = cpuInfo.getVendor();
		
		this.combined = (Math.round(cpuPerc.getCombined() * 100.0) / 100.0) * 100D;
		this.idleTime = (Math.round(cpuPerc.getIdle() * 100.0) / 100.0) * 100D;
		this.irqTime =  (Math.round(cpuPerc.getIrq() * 100.0) / 100.0) * 100D;
		this.niceTime = (Math.round(cpuPerc.getNice() * 100.0) / 100.0) * 100D;
		this.softirqTime = (Math.round(cpuPerc.getSoftIrq() * 100.0) / 100.0) * 100D;
		this.stolenTime = (Math.round(cpuPerc.getStolen() * 100.0) / 100.0) * 100D;
		this.sysTime = (Math.round(cpuPerc.getSys() * 100.0) / 100.0) * 100D;
		this.userTime = (Math.round(cpuPerc.getUser() * 100.0) / 100.0) * 100D;
		this.waitTime = (Math.round(cpuPerc.getWait() * 100.0) / 100.0) * 100D;
		
		
	}
	


	public String getVendor() {
		return vendor;
	}

	public long getCacheSize() {
		return cacheSize;
	}

	public int getCoresPerSocket() {
		return coresPerSocket;
	}

	public int getMhz() {
		return mhz;
	}

	public String getModel() {
		return model;
	}

	public int getTotalCores() {
		return totalCores;
	}

	public int getTotalSockets() {
		return totalSockets;
	}

	public int getCpuIndex() {
		return cpuIndex;
	}

	public double getCombined() {
		return combined;
	}

	public double getIdleTime() {
		return idleTime;
	}

	public double getIrqTime() {
		return irqTime;
	}

	public double getNiceTime() {
		return niceTime;
	}

	public double getSoftirqTime() {
		return softirqTime;
	}

	public double getStolenTime() {
		return stolenTime;
	}

	public double getSysTime() {
		return sysTime;
	}

	public double getUserTime() {
		return userTime;
	}

	public double getWaitTime() {
		return waitTime;
	}

	

	
	public JSONObject toJSONObject() {
		JSONObject jo = new JSONObject();
		jo.put("cpuIndex",cpuIndex);
		jo.put("vendor",vendor);
		jo.put("cacheSize",cacheSize);
		jo.put("coresPerSocket",coresPerSocket);
		jo.put("mhz",mhz);
		jo.put("model",model);
		jo.put("totalCores",totalCores);
		jo.put("totalSockets",totalSockets);
		jo.put("combined",combined);
		jo.put("idleTime",idleTime);
		jo.put("irqTime",irqTime);
		jo.put("niceTime",niceTime);
		jo.put("softirqTime",softirqTime);
		jo.put("stolenTime",stolenTime);
		jo.put("sysTime",sysTime);
		jo.put("userTime",userTime);
		jo.put("waitTime",waitTime);
		return jo;
	}

}
