package com.dmsi.rmc;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.JSONObject;


public class Media {

	private String fileSystemName;
	private String fileSystemType;
	private long fileSystemTotal;
	private long fileSystemFree;
	private long fileSystemUsed;
	private long fileSystemAvail;
	private double usedPct;
	
	


	public Media(FileSystem fileSystem) {
		super();
		initialise(fileSystem);
	}

	public void initialise(FileSystem fileSystem) {
		
		Sigar sigar = new Sigar();
			
		this.fileSystemName = fileSystem.getDirName();
		this.fileSystemType = fileSystem.getSysTypeName();
		
		FileSystemUsage usage;
		try {
			usage = sigar.getFileSystemUsage(fileSystemName);
			this.fileSystemAvail = usage.getAvail();
			this.fileSystemFree = usage.getFree();
			this.fileSystemTotal = usage.getTotal();
			this.fileSystemUsed = usage.getUsed();
			this.usedPct = usage.getUsePercent() * 100D;
		} catch (SigarException e) {
			LoggerClass.getLOGGER().warning("Can't Get File System Usage Of"+ "'" + fileSystemName + "' : Permission Denied");
		}
		

	}
	
	
	public String getFileSystemName() {
		return fileSystemName;
	}


	public String getFileSystemType() {
		return fileSystemType;
	}


	public long getFileSystemTotal() {
		return fileSystemTotal;
	}


	public long getFileSystemFree() {
		return fileSystemFree;
	}


	public long getFileSystemUsed() {
		return fileSystemUsed;
	}


	public long getFileSystemAvail() {
		return fileSystemAvail;
	}


	public double getUsedPct() {
		return usedPct;
	}


	
	
	@Override
	public String toString() {
		return String.format(
				"Media [fileSystemName=%s, fileSystemType=%s, fileSystemTotal=%s, fileSystemFree=%s, fileSystemUsed=%s, fileSystemAvail=%s, usedPct=%s]",
				fileSystemName, fileSystemType, fileSystemTotal, fileSystemFree, fileSystemUsed, fileSystemAvail,
				usedPct);
	}

	public JSONObject toJSONObject() {
		JSONObject jo = new JSONObject();
		jo.put("fileSystemName",fileSystemName);
		jo.put("fileSystemType",fileSystemType);
		jo.put("fileSystemTotal",fileSystemTotal);
		jo.put("fileSystemFree",fileSystemFree);
		jo.put("fileSystemUsed",fileSystemUsed);
		jo.put("fileSystemAvail",fileSystemAvail);
		jo.put("usedPct",usedPct);
		return jo;
	}
	
	
	
	
}
