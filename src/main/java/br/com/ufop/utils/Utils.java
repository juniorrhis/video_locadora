package br.com.ufop.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {
	public static void writeFile(String filePath, String fileContent, boolean append) throws IOException{
        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, append));
        
        bw.write(fileContent);
        
        bw.close();
    }
	
	public static String readFile(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        StringBuilder file = new StringBuilder();
        
        boolean isFirst = true;
        
        String line = "";
        while ((line = br.readLine()) != null) {            
            if (isFirst) {
                file.append(line);
            } else {
                file.append("\n").append(line);
            }
            
            isFirst = false;
        }
        
        br.close();
        
        return file.toString();
	}
	
	public static void log(Class<?> c, String message) {
		System.out.println("[" + c.getName() + "] - " + message);
	}
}
