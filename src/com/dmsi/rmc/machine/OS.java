package com.dmsi.rmc.machine;

import org.hyperic.sigar.OperatingSystem;
import org.json.JSONObject;

public class OS {
	
	private String arch;
	private String cpuEndian;
	private String dataModel;
	private String description;
	private String machine;
	private String name;
	private String patchLevel;
	private String vendor;
	private String vendorCodeName;
	private String vendorName;
	private String vendorVersion;
	private String version;
	
	public OS() {
		super();
		initialise();
	}
	
	public void initialise() {
		OperatingSystem OS = OperatingSystem.getInstance();
		this.arch = OS.getArch();
		this.cpuEndian = OS.getCpuEndian();
		this.dataModel = OS.getDataModel();
		this.description = OS.getDescription();
		this.machine = OS.getMachine();
		this.name = OS.getName();
		this.patchLevel = OS.getPatchLevel();
		this.vendor = OS.getVendor();
		this.vendorCodeName = OS.getVendorCodeName();
		this.vendorName = OS.getVendorName();
		this.vendorVersion = OS.getVendorVersion();
		this.version = OS.getVersion();
	
	}

	public String getArch() {
		return arch;
	}

	public String getCpuEndian() {
		return cpuEndian;
	}

	public String getDataModel() {
		return dataModel;
	}

	public String getDescription() {
		return description;
	}

	public String getMachine() {
		return machine;
	}

	public String getName() {
		return name;
	}

	public String getPatchLevel() {
		return patchLevel;
	}

	public String getVendor() {
		return vendor;
	}

	public String getVendorCodeName() {
		return vendorCodeName;
	}

	public String getVendorName() {
		return vendorName;
	}

	public String getVendorVersion() {
		return vendorVersion;
	}

	public String getVersion() {
		return version;
	}


	
	
	@Override
	public String toString() {
		return String.format(
				"OS [arch=%s, cpuEndian=%s, dataModel=%s, description=%s, machine=%s, name=%s, patchLevel=%s, vendor=%s, vendorCodeName=%s, vendorName=%s, vendorVersion=%s, version=%s]",
				arch, cpuEndian, dataModel, description, machine, name, patchLevel, vendor, vendorCodeName, vendorName,
				vendorVersion, version);
	}

	public JSONObject toJSONObject() {
		JSONObject jo = new JSONObject();
		jo.put("arch",arch);
		jo.put("cpuEndian",cpuEndian);
		jo.put("dataModel",dataModel);
		jo.put("description",description);
		jo.put("machine",machine);
		jo.put("name",name);
		jo.put("version",version);
		jo.put("patchLevel",patchLevel);
		jo.put("vendor",vendor);
		jo.put("vendorName",vendorName);
		jo.put("vendorVersion",vendorVersion);
		jo.put("vendorCodeName",vendorCodeName);
		return jo;
	}
	
	
	
}
