package project;

public class Memory {
	public static int DATA_SIZE = 2048;
	private int[] data = new int[DATA_SIZE];
	private int changedIndex = -1;
	
	int[] getData() {
		return data;
	}

	int getData(int index) {
		return data[index];
	}
	
	void setData(int index, int value) {
		data[index] = value;
		changedIndex = index;
	}
	
	int getChangedIndex() {
		return changedIndex;
	}
	
	void clear(int start, int end) {
		for(int i = start; i < end; i++) {
			data[i] = 0;
			changedIndex = -1;
		}
	}
}
