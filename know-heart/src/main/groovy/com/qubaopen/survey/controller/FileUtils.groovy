package com.qubaopen.survey.controller;

import org.springframework.stereotype.Service;

@Service
public class FileUtils {

	public static void saveFile(byte[] bytes, String filename) {
		def fos = new FileOutputStream(filename)
		fos.write(bytes)
		fos.close()
	}
}
