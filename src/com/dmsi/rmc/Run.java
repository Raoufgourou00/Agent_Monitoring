package com.dmsi.rmc;

import java.awt.EventQueue;

import javax.swing.SwingUtilities;
public class Run {

		
	public static void main(String[] args) {
		
		System.setProperty( "java.library.path", "./lib" );
		
		java.lang.reflect.Field fieldSysPath;
		try {
			fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
			fieldSysPath.setAccessible( true );
			fieldSysPath.set( null, null );
		} catch (NoSuchFieldException | SecurityException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		catch (IllegalArgumentException | IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//FrameConnection.loadLibrary();
		LoggerClass.CreateLogFile();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SwingUtilities.invokeLater(() -> new FrameConnection());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		}

		/*
		public static void main(String[] args) {
			
			loadLibrary();
			LoggerClass.CreateLogFile();
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						@SuppressWarnings("unused")
						FrameConnection window = new FrameConnection();
						FrameConnection.getFrame().setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		*/
		
	 	
}
