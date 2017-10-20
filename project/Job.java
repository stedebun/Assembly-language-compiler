package project;

public class Job {
	private int startCodeIndex;
	private int codeSize;
	private int startmemoryIndex;
	private int currentIP;
	private int currentAcc;
	private States currentState;
	
	public States getCurrentState() {
		return currentState;
	}
	public void setCurrentState(States currentState) {
		this.currentState = currentState;
	}
	public int getStartcodeIndex() {
		return startCodeIndex;
	}
	public void setStartCodeIndex(int startcodeIndex) {
		this.startCodeIndex = startcodeIndex;
	}
	public int getCodeSize() {
		return codeSize;
	}
	public void setCodeSize(int codeSize) {
		this.codeSize = codeSize;
	}
	public int getStartMemoryIndex() {
		return startmemoryIndex;
	}
	public void setStartMemoryIndex(int startmemoryIndex) {
		this.startmemoryIndex = startmemoryIndex;
	}
	public int getCurrentIP() {
		return currentIP;
	}
	public void setCurrentIP(int currentIP) {
		this.currentIP = currentIP;
	}
	public int getCurrentAcc() {
		return currentAcc;
	}
	public void setCurrentAcc(int currentAcc) {
		this.currentAcc = currentAcc;
	}
	
	void reset() {
		codeSize = 0;
		currentAcc = 0;
		currentIP = startCodeIndex;
		currentState = States.NOTHING_LOADED;
	}
}
