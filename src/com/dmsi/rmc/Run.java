package com.dmsi.rmc;

import java.awt.EventQueue;
public class Run {

		/*
		public static void main(String[] args) {
				
				
				FrameConnection.loadLibrary();
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
		*/
		public static void main(String[] args) {
			
			FrameConnection.loadLibrary();
			LoggerClass.CreateLogFile();
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						FrameConnection window = new FrameConnection();
						FrameConnection.getFrame().setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	
	
}
