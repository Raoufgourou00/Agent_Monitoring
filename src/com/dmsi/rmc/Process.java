package com.dmsi.rmc;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.ProcCredName;
import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.ProcState;
import org.hyperic.sigar.ProcTime;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.JSONObject;


public class Process {

	private long pid;
	private String name;
	private String userName;
	private String cpuTime;
	private String memRss;
	private String memShare;
	private String memSize;
	private String memVsize;
	private String startTime;
	private String state;
	private double cpuPercent;
	
	
	public Process() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	@SuppressWarnings("deprecation")
	public static Process initialise(long proc,Sigar sigar) {
		
		Process process = new Process();
		ProcCpu procCpu;
		try {
			procCpu = sigar.getProcCpu(proc);
			process.pid =  proc;
			process.cpuPercent = procCpu.getPercent() * 100D;
			process.startTime =  getDate(procCpu.getStartTime());
			
			ProcCredName procCredName = sigar.getProcCredName(proc);
			process.userName = procCredName.getUser();
			
			ProcMem procMem = sigar.getProcMem(proc);
			process.memRss = Sigar.formatSize(procMem.getRss());
			process.memShare = Sigar.formatSize(procMem.getShare());
			process.memSize = Sigar.formatSize(procMem.getSize());
			process.memVsize = Sigar.formatSize(procMem.getVsize());
		
			ProcState procStat = sigar.getProcState(proc);
			process.name = procStat.getName();
			process.state = getState(procStat.getState());
			
			ProcTime procTime = sigar.getProcTime(proc);
			long time = procTime.getTotal();
			 
			process.cpuTime = msToTime(time);
			return process;
		} catch (SigarException e) {
			//LoggerClass.getLOGGER().warning("Can't Get Process Information Of:"+ "'" + proc + "' : Permission Denied");
			return null;
		}	
	}
	
	
	
	public static String getDate(long time) {
		Date date = new Date(time);
		SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return DateFor.format(date);
		
	}
	
	
    public static String getState(char state) {
    	switch (state) {
        case ProcState.SLEEP:
        	return("Sleeping");
		case ProcState.RUN:
        	return("Running");
        case ProcState.STOP:
        	return("Suspended");  	
        case ProcState.ZOMBIE:
        	return("Zombie");
        case ProcState.IDLE:
        	return("Idle");
        default:
        	return("Unknown");
      }
    }
    
    public static String msToTime(long time) {
    	 long ms = time%100;
		 long s = time/1000%60;
		 long m = time/1000/60;
		 return m + ":" + s + ":" + ms ;
    }

	public long getPid() {
		return pid;
	}

	public String getName() {
		return name;
	}

	public String getUserName() {
		return userName;
	}

	public String getCpuTime() {
		return cpuTime;
	}

	public String getMemRss() {
		return memRss;
	}

	public String getMemShare() {
		return memShare;
	}

	public String getMemSize() {
		return memSize;
	}

	public String getMemVsize() {
		return memVsize;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getState() {
		return state;
	}

	public double getCpuPercent() {
		return cpuPercent;
	}


	@Override
	public String toString() {
		return String.format(
				"Process [pid=%s, name=%s, userName=%s, cpuTime=%s, memRss=%s, memShare=%s, memSize=%s, memVsize=%s, startTime=%s, state=%s, cpuPercent=%s]",
				pid, name, userName, cpuTime, memRss, memShare, memSize, memVsize, startTime, state, cpuPercent);
	}


	public JSONObject toJSONObject() {
		JSONObject jo = new JSONObject();
		jo.put("pid",pid);
		jo.put("name",name);
		jo.put("userName",userName);
		jo.put("cpuTime",cpuTime);
		jo.put("memRss",memRss);
		jo.put("name",name);
		jo.put("memShare",memShare);
		jo.put("memVsize",memVsize);
		jo.put("startTime",startTime);
		jo.put("state",state);
		jo.put("cpuPercent",cpuPercent);
		return jo;
	}
	
}
