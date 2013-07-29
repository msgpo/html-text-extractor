package com.rekkit.wordextractor;

import java.awt.EventQueue;

public class WordExtractorMain {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WordExtractorGUI frame = new WordExtractorGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
