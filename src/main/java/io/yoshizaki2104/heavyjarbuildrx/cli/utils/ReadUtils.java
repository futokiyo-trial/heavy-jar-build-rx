package io.yoshizaki2104.heavyjarbuildrx.cli.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ReadUtils {

	public static String readFrom(String path) throws IOException {
		
		StringBuilder sb = new StringBuilder();
		try(FileInputStream fis = new FileInputStream(path);
			InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
				BufferedReader br = new BufferedReader(isr)){
			char[] charArrayData = new char[1024];
			int readCharNum = br.read(charArrayData);
			while(readCharNum!=-1) {
				sb.append(charArrayData, 0, readCharNum);
				readCharNum = br.read(charArrayData);
			}
		}
		return sb.toString();
	}

}
