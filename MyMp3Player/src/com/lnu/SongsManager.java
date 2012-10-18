package com.lnu;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

public class SongsManager {
	private final File MUSIC_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
	private final List<File> songsList = new ArrayList<File>();

	public List<File> getSongsList() {

		File[] files = MUSIC_DIR.listFiles(new Mp3FileFilter());
		if(files.length ==0){
			throw new RuntimeException("No Mp3 Songs found."); 
		}
		for (File file : files) {
			songsList.add(file);
		}
		return songsList;
	}

	private class Mp3FileFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String filename) {
			return filename.toLowerCase().endsWith(".mp3");
		}

	}
}
