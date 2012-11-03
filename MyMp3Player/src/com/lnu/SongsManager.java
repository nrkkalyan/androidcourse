package com.lnu;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Environment;

public class SongsManager {
	private static final File MUSIC_DIR = Environment
			.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);

	private final static List<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	public static List<HashMap<String, String>> getSongsList() {
		if (MUSIC_DIR == null || !MUSIC_DIR.exists()) {
			throw new RuntimeException("SD card not found.");
		}
		File[] files = MUSIC_DIR.listFiles(new Mp3FileFilter());
		if (files.length == 0) {
			throw new RuntimeException("No Mp3 Songs found.");
		}
		for (File song : files) {
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("name", song.getName());
			item.put("songPath", song.getPath());
		}
		return songsList;
	}

	private static class Mp3FileFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String filename) {
			return filename.toLowerCase().endsWith(".mp3");
		}

	}
}
