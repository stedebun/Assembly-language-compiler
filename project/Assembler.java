package project;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Assembler {

	public static String assemble(File input, File output) {
		String retVal = "success";
		ArrayList<String> code = new ArrayList<>();
		ArrayList<String> data = new ArrayList<>();
		ArrayList<String> inText = new ArrayList<>();
		boolean incode = true;
		try (Scanner inp = new Scanner(input)) {
			while(inp.hasNextLine()){
				String line = inp.nextLine();
				inText.add(line);
			}
			int counter = 0;
			int error = inText.size();
			String line = null;
			for(String c : inText) {
				if(c.trim().length() == 0) {
					if(counter + 1 < inText.size()) {
						line = inText.get(counter + 1);
						if(line.trim().length() > 0) {
							error = counter;
							retVal = "Error: line " + (counter + 1) + " is a blank line";
							break;
						}
					}
				}
				counter += 1;
			}

			counter = 0;
			line = null;

			for(String c : inText) {
				if(error == counter) {
					break;
				}
				if(c.trim().length() > 0 && c != null) {
					if(c.charAt(0) == ' ' || c.charAt(0) == '\t') {
						error = counter;
						retVal = "Error: line " + (inText.indexOf(c) + 1) + " starts with a white space";
						break;
					}
				}
				counter += 1;
			}

			counter = 0;
			line = null;
			for(String c : inText) {
				if(error == counter) {
					break;
				}
				if(c.trim().toUpperCase().equals("DATA")) {
					if(!c.trim().equals("DATA")) {
						error = counter;
						retVal = "Error: line " + (inText.indexOf(c) + 1) + " does not have DATA in upper case";
						break;
					}
				}
				counter += 1;
			}

			int i = 0;
			for(String c : inText) {
				if(i >= error) break;
				if(incode && c.trim().equals("DATA")) incode = false;
				else if(incode && c.trim().length() > 0) code.add(c.trim());
				else data.add(c.trim());
				i += 1;
			}

		} catch (FileNotFoundException e) {
			retVal = "Error: the source file " + input + " is missing";
		}

		ArrayList<String> outText = new ArrayList<>();

		for(String s : code) {
			String[] parts = s.split("\\s+");
			int lineNum = inText.indexOf(s) + 1;
			//First part
			if(InstructionMap.sourceCodes.contains(parts[0].toUpperCase()) && !InstructionMap.sourceCodes.contains(parts[0])) {
				System.out.println(s);
				return "Error: line " + lineNum + " does not have the instruction mnemonic in upper case";

			}

			//Second part
			if(InstructionMap.noArgument.contains(parts[0])) {
				if(parts.length != 1) {
					System.out.println(s);
					return "Error: line " + lineNum + " has more than one argument";

				}
			}
			if(!InstructionMap.noArgument.contains(parts[0])) {
				if(parts.length == 1) {
					System.out.println(s);
					return "Error: line " + lineNum + " is missing an argument";

				}
				if(parts.length > 2) {
					System.out.println(s);
					return "Error: line " + lineNum + " has more than one argument";

				}
				if(parts.length == 2) {
					if(parts[1].startsWith("#")) {
						if(!InstructionMap.immediateOK.contains(parts[0])) {
							System.out.println(s);
							return "Error: line " + lineNum + " does not have a correct instruction mnemonic";

						}
						if(InstructionMap.immediateOK.contains(parts[0])) {
							parts[0] = parts[0] + "I";
							parts[1] = parts[1].substring(1);
							
							if(parts[0].equals("JUMPI")) parts[0] = "JMPI"; 
							if(parts[0].equals("JMPZI")) parts[0] = "JMZI"; 

							try {
								Integer.parseInt(parts[1],16); //<<<<< CORRECTION
							} catch (NumberFormatException e) {
								System.out.println(s);
								return "Error: line " + lineNum 
										+ " does not have a numberic argument";
							}
						}
	
					}

					else if(parts[1].startsWith("&")) {
						if(!InstructionMap.indirectOK.contains(parts[0])) {
							System.out.println(s);
							return "Error: line " + lineNum + " does not have a correct instruction mnemonic";

						}
						if(InstructionMap.indirectOK.contains(parts[0])) {
							parts[0] = parts[0] + "N";
							parts[1] = parts[1].substring(1);
							
							if(parts[0].equals("JUMPN")) parts[0] = "JMPN";
							
							try {
								Integer.parseInt(parts[1],16); //<<<<< CORRECTION
							} catch (NumberFormatException e) {
								System.out.println(s);
								return "Error: line " + lineNum 
										+ " does not have a numberic argument";
							}
						}
						 
					}
					
				}
			}
			
			if(!InstructionMap.opcode.containsKey(parts[0])) {
				return "Error: line " + lineNum + " not a valid instruction mnemoic";

			}
			
			if(parts.length == 1) {
				int opcode = InstructionMap.opcode.get(parts[0]);
				outText.add(Integer.toHexString(opcode).toUpperCase() + " 0");
			} else {
				if(InstructionMap.opcode.get(parts[0]) != null) {
					int opcode = InstructionMap.opcode.get(parts[0]);
					outText.add(Integer.toHexString(opcode).toUpperCase() 
							+ " " + parts[1]);
				}
			}
		}
		
		for(String s : data) {
			String[] parts = s.split("\\s+");
			int lineNum = inText.indexOf(s) + 1;

			if(s.trim().length() < 1) {
				break;
			}

			if(parts.length != 2) {
				System.out.println(s);
				return "Error: line " + lineNum + " does not have a size of 2";

			}
			else if(parts.length == 2){
				try {
					Integer.parseInt(parts[0],16); //<<<<< CORRECTION
				} catch (NumberFormatException e) {
					System.out.println(s);
					return "Error: line " + lineNum 
							+ " does not have a numberic argument";
				}

				try {
					Integer.parseInt(parts[1],16); //<<<<< CORRECTION
				} catch (NumberFormatException e) {
					System.out.println(s);
					return "Error: line " + lineNum 
							+ " does not have a numberic argument";
				}
				//				if(InstructionMap.opcode.get(parts[0]) != null) {
				//					int opcode = InstructionMap.opcode.get(parts[0]);
				//					outText.add(Integer.toHexString(opcode).toUpperCase() 
				//							+ " " + parts[1]);
				//				}
			}

		}
		outText.add("-1");
		outText.addAll(data);

		if(retVal.equals("success")) {
			try (PrintWriter outp = new PrintWriter(output)){
				for(String str : outText) {
					outp.println(str);
				}
				outp.close();
			} catch (FileNotFoundException e) {
				retVal = "Error: unable to open " + output; 
			}

		}
		return retVal;

	}

	public static void main(String[] args) {
		System.out.println("Enter the name of the file without extension: ");
		try (Scanner keyboard = new Scanner(System.in)) {
			String filename = keyboard.nextLine();
			String i = assemble(new File(filename + ".pasm"), 
					new File(filename + ".pexe"));
			System.out.println(i );
		}
	}

}