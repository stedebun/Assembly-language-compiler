package project;

import java.util.Map;
import java.util.TreeMap;

public class MachineModel {

	private CPU cpu = new CPU();
	private Memory memory = new Memory();
	private HaltCallback callback;
	public final Map<Integer, Instruction> INSTRUCTIONS = new TreeMap<Integer, Instruction>();
	public Code code = new Code();
	Job[] jobs = new Job[2];
	private Job currentJob;

	public MachineModel() {
		this(() -> System.exit(0));
	}

	public MachineModel(HaltCallback callback) {
		this.callback = callback;

		//INSTRUCTION_MAP entry for "NOP"
		INSTRUCTIONS.put(0x0, arg -> {
			cpu.incrementIP();
		});

		//INSTRUCTION_MAP entry for "LODI"
		INSTRUCTIONS.put(0x1, arg -> {
			cpu.setAccumulator(arg);
			cpu.incrementIP();
		});

		//INSTRUCTION_MAP entry for "LOD"
		INSTRUCTIONS.put(0x2, arg -> {
			int arg1 = memory.getData(cpu.getMemoryBase()+arg);
			cpu.setAccumulator(arg1);
			cpu.incrementIP();
		});

		//INSTRUCTION_MAP entry for "LODN"
		INSTRUCTIONS.put(0x3, arg -> {
			int arg1 = memory.getData(cpu.getMemoryBase()+arg);
			int arg2 = memory.getData(cpu.getMemoryBase()+arg1);
			cpu.setAccumulator(arg2);
			cpu.incrementIP();
		});

		//INSTRUCTION_MAP entry for "STO"
		INSTRUCTIONS.put(0x4, arg -> {
			int arg1 = cpu.getAccumulator();
			memory.setData(cpu.getMemoryBase()+arg, arg1); 
			cpu.incrementIP();
		});

		//INSTRUCTION_MAP entry for "STON"
		INSTRUCTIONS.put(0x5, arg -> {
			int arg1 = cpu.getAccumulator();
			int arg2 = memory.getData(cpu.getMemoryBase()+arg);
			memory.setData(cpu.getMemoryBase()+arg2, arg1); 
			cpu.incrementIP();
		});

		//INSTRUCTION_MAP entry for "JMPI"
		INSTRUCTIONS.put(0x6, arg -> {
			cpu.setInstructionPointer(cpu.getInstructionPointer() + arg); 
		});

		//INSTRUCTION_MAP entry for "JUMP"
		INSTRUCTIONS.put(0x7, arg -> {
			int arg1 = memory.getData(cpu.getMemoryBase()+arg);
			cpu.setInstructionPointer(cpu.getInstructionPointer() + arg1); 
		});

		//INSTRUCTION_MAP entry for "JMZI"
		INSTRUCTIONS.put(0x8, arg -> {
			if(cpu.getAccumulator() == 0){
				cpu.setInstructionPointer(cpu.getInstructionPointer() + arg); 
			} 
			else{
				cpu.incrementIP();
			}
		});

		//INSTRUCTION_MAP entry for "JMPZ"
		INSTRUCTIONS.put(0x9, arg -> {
			if(cpu.getAccumulator() == 0){
				int arg1 = memory.getData(cpu.getMemoryBase()+arg);
				cpu.setInstructionPointer(cpu.getInstructionPointer() + arg1); 
			} 
			else{
				cpu.incrementIP();
			}
		});

		//INSTRUCTION_MAP entry for "ADDI"
		INSTRUCTIONS.put(0xA, arg -> {
			cpu.setAccumulator(cpu.getAccumulator() + arg);
			cpu.incrementIP();
		});

		//INSTRUCTION_MAP entry for "ADD"
		INSTRUCTIONS.put(0xB, arg -> {
			int arg1 = memory.getData(cpu.getMemoryBase()+arg);
			cpu.setAccumulator(cpu.getAccumulator() + arg1);
			cpu.incrementIP();
		});

		//INSTRUCTION_MAP entry for "ADDN"
		INSTRUCTIONS.put(0xC, arg -> {
			int arg1 = memory.getData(cpu.getMemoryBase()+arg);
			int arg2 = memory.getData(cpu.getMemoryBase()+arg1);
			cpu.setAccumulator(cpu.getAccumulator() + arg2);
			cpu.incrementIP();
		});

		//INSTRUCTION_MAP entry for "SUBI"
		INSTRUCTIONS.put(0xD, arg -> {
			cpu.setAccumulator(cpu.getAccumulator() - arg);
			cpu.incrementIP();
		});

		//INSTRUCTION_MAP entry for "SUB"
		INSTRUCTIONS.put(0xE, arg -> {
			int arg1 = memory.getData(cpu.getMemoryBase() + arg);
			cpu.setAccumulator(cpu.getAccumulator() - arg1);
			cpu.incrementIP();
		});

		//INSTRUCTION_MAP entry for "SUBN"
		INSTRUCTIONS.put(0xF, arg -> {
			int arg1 = memory.getData(cpu.getMemoryBase() + arg);
			int arg2 = memory.getData(cpu.getMemoryBase() + arg1);
			cpu.setAccumulator(cpu.getAccumulator() - arg2);
			cpu.incrementIP();
		});

		//INSTRUCTION_MAP entry for "MULI"
		INSTRUCTIONS.put(0x10, arg -> {
			cpu.setAccumulator(cpu.getAccumulator() * arg);
			cpu.incrementIP();
		});

		//INSTRUCTION_MAP entry for "MUL"
		INSTRUCTIONS.put(0x11, arg -> {
			int arg1 = memory.getData(cpu.getMemoryBase() + arg);
			cpu.setAccumulator(cpu.getAccumulator() * arg1);
			cpu.incrementIP();
		});

		//INSTRUCTION_MAP entry for "<MULN"
		INSTRUCTIONS.put(0x12, arg -> {
			int arg1 = memory.getData(cpu.getMemoryBase() + arg);
			int arg2 = memory.getData(cpu.getMemoryBase() + arg1);
			cpu.setAccumulator(cpu.getAccumulator() * arg2);
			cpu.incrementIP();
		});

		//INSTRUCTION_MAP entry for "DIVI"
		INSTRUCTIONS.put(0x13, arg -> { 
			if(arg == 0){ 
				throw new DivideByZeroException();
			}
			cpu.setAccumulator(cpu.getAccumulator() / arg);
			cpu.incrementIP();
		});

		//INSTRUCTION_MAP entry for "DIV"
		INSTRUCTIONS.put(0x14, arg -> { 
			int arg1 = memory.getData(cpu.getMemoryBase() + arg);
			if(arg1 == 0){ 
				throw new DivideByZeroException();
			}
			cpu.setAccumulator(cpu.getAccumulator() / arg1);
			cpu.incrementIP();
		});

		//INSTRUCTION_MAP entry for "DIVN"
		INSTRUCTIONS.put(0x15, arg -> {
			int arg1 = memory.getData(cpu.getMemoryBase() + arg);
			int arg2 = memory.getData(cpu.getMemoryBase() + arg1);
			if(arg2 == 0){ 
				throw new DivideByZeroException();
			}
			cpu.setAccumulator(cpu.getAccumulator() / arg2);
			cpu.incrementIP();
		});

		//INSTRUCTION_MAP entry for "ANDI"
		INSTRUCTIONS.put(0x16, arg -> {
			if(cpu.getAccumulator() != 0 && arg != 0){
				cpu.setAccumulator(1);
				cpu.incrementIP();
			}
			else{
				cpu.setAccumulator(0);
				cpu.incrementIP();
			}
		});

		//INSTRUCTION_MAP entry for "AND"
		INSTRUCTIONS.put(0x17, arg -> {
			if(cpu.getAccumulator() != 0 && memory.getData(cpu.getMemoryBase() + arg) != 0){
				cpu.setAccumulator(1);
				cpu.incrementIP();
			}
			else{
				cpu.setAccumulator(0);
				cpu.incrementIP();
			}
		});

		//INSTRUCTION_MAP entry for "NOT"
		INSTRUCTIONS.put(0x18, arg -> {
			if(cpu.getAccumulator() != 0){
				cpu.setAccumulator(0);
				cpu.incrementIP();
			}
			else if(cpu.getAccumulator() == 0){
				cpu.setAccumulator(1);
				cpu.incrementIP();
			}
		});

		//INSTRUCTION_MAP entry for "CMPL"
		INSTRUCTIONS.put(0x19, arg -> {
			if(memory.getData(cpu.getMemoryBase() + arg) < 0){
				cpu.setAccumulator(1);
				cpu.incrementIP();
			}
			else{
				cpu.setAccumulator(0);
				cpu.incrementIP();
			}
		});

		//INSTRUCTION_MAP entry for "CMPZ"
		INSTRUCTIONS.put(0x1A, arg -> {
			if(memory.getData(cpu.getMemoryBase() + arg) == 0){
				cpu.setAccumulator(1);
				cpu.incrementIP();
			}
			else{
				cpu.setAccumulator(0);
				cpu.incrementIP();
			}
		});

		//INSTRUCTION_MAP entry for "JMPN"
		INSTRUCTIONS.put(0x1B, arg -> {
			int target = memory.getData(cpu.getMemoryBase() + arg);
			cpu.setInstructionPointer(currentJob.getStartcodeIndex()+target);
		});

		//INSTRUCTION_MAP entry for "HALT"
		INSTRUCTIONS.put(0x1F, arg -> {
			callback.halt();			
		});

		//Start of jobs 
		jobs[0] = new Job();
		currentJob = jobs[0];

		jobs[0].setStartCodeIndex(0);
		jobs[0].setStartMemoryIndex(0);

		jobs[1] = new Job();
		jobs[1].setStartCodeIndex(Code.CODE_MAX/4);
		jobs[1].setStartMemoryIndex(Memory.DATA_SIZE/2);
	}

	CPU getCpu() {
		return cpu;
	}

	void setCpu(CPU cpu) {
		this.cpu = cpu;
	}

	Memory getMemory() {
		return memory;
	}

	void setMemory(Memory memory) {
		this.memory = memory;
	}

	Instruction get(int hex) {
		return INSTRUCTIONS.get(hex);
	}

	int getData(int index) {
		return memory.getData(index);
	}

	void setData(int i, int j) {
		memory.setData(i, j);
	}

	int getChangedIndex() {
		return memory.getChangedIndex();
	}

	int[] getData() {
		// TODO Auto-generated method stub
		return memory.getData();
	}

	Instruction get(Integer key) {
		return INSTRUCTIONS.get(key);
	}

	int getInstructionPointer() {
		// TODO Auto-generated method stub
		return cpu.getInstructionPointer();
	}

	int getAccumulator() {
		// TODO Auto-generated method stub
		return cpu.getAccumulator();
	}

	int getMemoryBase() {
		return cpu.getMemoryBase();
	}

	void setInstructionPointer(int i) {
		// TODO Auto-generated method stub
		cpu.setInstructionPointer(i);
	}

	void setAccumulator(int i) {
		// TODO Auto-generated method stub
		cpu.setAccumulator(i);
	}

	void setMemoryBase(int memoryBase) {
		cpu.setMemoryBase(memoryBase);
	}

	Code getCode() {
		return code;
	}

	void setCode(int i, int op, int arg) {
		code.setCode(i, op, arg);
	}

	Job getCurrentJob() {
		return currentJob;
	}

	void setJob(int i) {
		if(i != 0 && i != 1) {
			throw new IllegalArgumentException();
		}
		else {
			currentJob.setCurrentAcc(cpu.getAccumulator());
			currentJob.setCurrentIP(cpu.getInstructionPointer());

			currentJob = jobs[i];

			cpu.setAccumulator(currentJob.getCurrentAcc());
			cpu.setInstructionPointer(currentJob.getCurrentIP());
			cpu.setMemoryBase(currentJob.getStartMemoryIndex());
		}
	}

	States getCurrentState() {
		return currentJob.getCurrentState();
	}

	void setCurrentState(States currentState) {
		currentJob.setCurrentState(currentState);
	}

	void step() {
		try{
			int ip = cpu.getInstructionPointer();
			if(ip < currentJob.getStartcodeIndex() || ip >= currentJob.getStartcodeIndex() + currentJob.getCodeSize()) {
				throw new CodeAccessException();
			}

			int opcode = code.getOp(ip);
			int arg = code.getArg(ip);

			get(opcode).execute(arg);

		}
		catch(Exception e) {
			callback.halt();
			throw e;
		}
	}

	void clearJob() {
		memory.clear(currentJob.getStartMemoryIndex(), currentJob.getStartMemoryIndex() + Memory.DATA_SIZE/2);
		code.clear(currentJob.getStartcodeIndex(), currentJob.getStartcodeIndex() + currentJob.getCodeSize());
		cpu.setAccumulator(0);
		cpu.setInstructionPointer(currentJob.getStartcodeIndex());
		currentJob.reset();
	}

}
