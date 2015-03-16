package com.qubaopen.doctor;

import org.springframework.stereotype.Service;

@Service
public class FileUtils {

	public void saveFile(byte[] bytes, String filename) {
		def fos = new FileOutputStream(filename)
		fos.write(bytes)
		fos.close()
	}
}
