package com.dmsi.rmc.machine;

import java.awt.EventQueue;

import javax.swing.SwingUtilities;

public class Main {

	
		public static void main(String[] args) {
				
				FrameConnection.loadLibrary();
				
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							SwingUtilities.invokeLater(() -> new FrameConnection());
							//window.setVisible(true);		
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				
		}
	
	
}
