package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Loader {
	public static String load(MachineModel model, File file,
			int codeOffset, int memoryOffset) {
		int codeSize = 0;
		if(model == null || file == null) return null;
		try (Scanner input = new Scanner(file)) {
			boolean incode = true;			
			while(input.hasNextLine()) {
				String line = input.nextLine();
				if(line.trim().length() == 0) break;
				Scanner parser = new Scanner(line);
				int first = parser.nextInt(16);
				if(incode && first == -1) {
					incode = false;
				} else if(incode) {
					int arg = parser.nextInt(16);
					model.setCode(codeOffset+codeSize, first, arg);
					codeSize++;
				} else {
					int value = parser.nextInt(16);
					model.setData(memoryOffset+first, value);	
				}
				parser.close();
			}
			return "" + codeSize;
		} catch (ArrayIndexOutOfBoundsException e) {
			return("Array Index " + e.getMessage());
		} catch (NoSuchElementException e) {
			return("From Scanner: NoSuchElementException");
		} catch (FileNotFoundException e1) {
			return("File " + file.getName() + " Not Found");
		}
	}
	public static void main(String[] args) {
		MachineModel model = new MachineModel();
		String s = Loader.load(model, new File("qsort.pexe"),100,200);
		for(int i = 100; i < 100+Integer.parseInt(s); i++) {
			System.out.println(model.getCode().getText(i));	
			// use the new getText here instead of getHex	
		}
		for (int i = 200; i < 203; i++)
		System.out.println(i + " " + model.getData(i));
	}
}