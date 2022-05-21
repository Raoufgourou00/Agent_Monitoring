package com.dmsi.rmc;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;
import org.json.JSONObject;

public class Memory {

	private long memTotal;
	private long memFree;
	private long memUsed;
	private long memRam;
	private long swapTotal;
    private long swapFree;
    private long swapUsed;
    private double memUsedPct;
    private double swapUsedPct;
	
    public Memory(Sigar sigar) {
		super();
		initialise(sigar);
	}
    
    public void initialise(Sigar sigar) {
    	
    	
    	Mem mem = null;
    	Swap swap = null;
    	
    
			try {
				mem = sigar.getMem();
				this.memTotal = mem.getTotal();
				this.memFree = mem.getFree();
				this.memUsed = mem.getUsed();
				this.memRam = mem.getRam();
				this.memUsedPct = mem.getUsedPercent();
			} catch (SigarException e) {
				LoggerClass.getLOGGER().warning("Can't Get Memory Informations: Permission Denied");
			}
			
			
			
			try {
				swap = sigar.getSwap();
				this.swapTotal = swap.getTotal();
				this.swapFree = swap.getFree();
				this.swapUsed = swap.getUsed();
				
				if(this.swapUsed == 0) {
					this.swapUsedPct = 0.0;
				}
				else
				{
					this.swapUsedPct = new Double(this.swapUsed) / new Double(this.swapTotal) ;
				}
			} catch (SigarException e) {
				LoggerClass.getLOGGER().warning("Can't Get Swap Informations: Permission Denied");
			}
			

    }
    
   
	public long getMemTotal() {
		return memTotal;
	}

	public long getMemFree() {
		return memFree;
	}

	public long getMemUsed() {
		return memUsed;
	}

	public long getMemRam() {
		return memRam;
	}

	public long getSwapTotal() {
		return swapTotal;
	}

	public long getSwapFree() {
		return swapFree;
	}

	public long getSwapUsed() {
		return swapUsed;
	}

	public double getMemUsedPct() {
		return memUsedPct;
	}

	public double getSwapUsedPct() {
		return swapUsedPct;
	}

 

	@Override
	public String toString() {
		return String.format(
				"Memory [memTotal=%s, memFree=%s, memUsed=%s, memRam=%s, swapTotal=%s, swapFree=%s, swapUsed=%s, memUsedPct=%s, swapUsedPct=%s]",
				memTotal, memFree, memUsed, memRam, swapTotal, swapFree, swapUsed, memUsedPct, swapUsedPct);
	}

	public JSONObject toJSONObject() {
		JSONObject jo = new JSONObject();
		jo.put("memTotal",memTotal);
		jo.put("memFree",memFree);
		jo.put("memUsed",memUsed);
		jo.put("memRam",memRam);
		jo.put("memUsedPct",memUsedPct);
		jo.put("swapTotal",swapTotal);
		jo.put("swapFree",swapFree);
		jo.put("swapUsed",swapUsed);
		jo.put("swapUsedPct",swapUsedPct);
		return jo;
	}
}
