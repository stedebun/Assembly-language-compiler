package project;
import javax.swing.Timer;

public class Animator {
	private static final int TICK = 500;
	boolean autoStepOn = false;
	Timer timer;
	ViewsOrganizer view;
	
	public Animator(ViewsOrganizer aView) {
		view = aView;
	}
	
	boolean isAutoStepOn() {
		return autoStepOn;
	}
	
	void toggleAutoStep() {
		if(autoStepOn == true) {
			autoStepOn = false;
		}
		else if(autoStepOn == false) {
			autoStepOn = true;
		}
	}
	
	void setPeriod(int period) {
		timer.setDelay(period);
	}
	
	public void start() {
		timer = new Timer(TICK, e -> {if(autoStepOn) view.step();});
		timer.start();
	}

	void setAutoStepOn(boolean val) {
		// TODO Auto-generated method stub
		autoStepOn = val;
	}
}
