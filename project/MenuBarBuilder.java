package project;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MenuBarBuilder implements Observer{
	private JMenuItem assemble = new JMenuItem("Assemble Source...");
	private JMenuItem load = new JMenuItem("Load Program...");
	private JMenuItem exit = new JMenuItem("Exit");
	private JMenuItem go = new JMenuItem("Go");
	private JMenuItem job0 = new JMenuItem("Job 0");
	private JMenuItem job1 = new JMenuItem("Job 1");
	private ViewsOrganizer view;

	public MenuBarBuilder(ViewsOrganizer gui) {
		view = gui;
		gui.addObserver(this);
	}
	
	JMenu createFileMenu() {
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		
		assemble.setMnemonic(KeyEvent.VK_A);
		assemble.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		assemble.addActionListener(e -> view.assembleFile());
		menu.add(assemble);
		
		load.setMnemonic(KeyEvent.VK_L);
		load.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		load.addActionListener(e -> view.loadFile());
		menu.add(load);
		
		menu.addSeparator();
		
		exit.setMnemonic(KeyEvent.VK_E);
		exit.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		exit.addActionListener(e -> view.exit());
		menu.add(exit);
		
		return menu;
	}
	
	JMenu createExecuteMenu() {
		JMenu execute = new JMenu("Execute");
		execute.setMnemonic(KeyEvent.VK_X);
		
		go.setMnemonic(KeyEvent.VK_G);
		go.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_G, ActionEvent.CTRL_MASK));
		go.addActionListener(e -> view.execute());
		execute.add(go);
		
		return execute;
	}
	
	JMenu createJobsMenu() {
		JMenu jobs = new JMenu("Change Jobs");
		jobs.setMnemonic(KeyEvent.VK_J);
		
		job0.setMnemonic(KeyEvent.VK_0);
		job0.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_0, ActionEvent.CTRL_MASK));
		job0.addActionListener(e -> view.setJob(0));
		jobs.add(job0);
		
		job1.setMnemonic(KeyEvent.VK_1);
		job1.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_1, ActionEvent.CTRL_MASK));
		job1.addActionListener(e -> view.setJob(1));
		jobs.add(job1);
		
		return jobs;		
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		assemble.setEnabled(view.getCurrentState().getAssembleFileActive());
		load.setEnabled(view.getCurrentState().getLoadFileActive());
		go.setEnabled((view.getCurrentState().getStepActive()));
		job0.setEnabled(view.getCurrentState().getChangeJobActive());
		job1.setEnabled(view.getCurrentState().getChangeJobActive());
		
	}
}
