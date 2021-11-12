package model;

import java.io.File;
import java.util.Scanner;

public class Options {

	public static String abc() {
		String onOff = "";
		 try {
		    	File file = new File("C:\\Users\\Admin\\eclipse-workspace\\PBL4\\resource\\Sound.txt");
		        Scanner myReader = new Scanner(file);
		        while(myReader.hasNextLine()) {
		        	String name = myReader.next() + myReader.next() + myReader.next();
		        	String onAndOff = myReader.next();
		        	onOff += onAndOff + " ";
		        }
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
		 return onOff;
	}
	
	public static void main(String[] args) {
		String abc = abc();
		System.out.println(abc);
		String[] onoff = abc.split(" ");
		System.out.println(onoff[0]);
	}

}
