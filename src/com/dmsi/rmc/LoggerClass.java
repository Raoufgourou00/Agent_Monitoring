package com.dmsi.rmc;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class LoggerClass {

	
	private static Logger LOGGER = null;

	//--------Change Logger Format Configurations-----------------------
	static {
	      System.setProperty("java.util.logging.SimpleFormatter.format",
	              "[%1$tF %1$tT] %4$-7s: %5$s %n");
	      //LOGGER = Logger.getLogger(LoggerClass.class.getName());
	      LOGGER = Logger.getLogger("Log-Danky");  
	  }
	//------------------------------------------------------------------  
	  
	//---------Create The Log File-------------  
	static void CreateLogFile() {
	 		
		    FileHandler fh;  
		    try {  

		        // This block configure the logger with handler and formatter  
		        fh = new FileHandler("log/Events.log");  
		        LOGGER.addHandler(fh);
		        SimpleFormatter formatter = new SimpleFormatter();  
		        fh.setFormatter(formatter);  

		    } catch (SecurityException e) {  
		        e.printStackTrace();  
		    } catch (IOException e) {  
		        e.printStackTrace();  
		    }  
 
		}

	public static Logger getLOGGER() {
		return LOGGER;
	}

	
	

	
}
