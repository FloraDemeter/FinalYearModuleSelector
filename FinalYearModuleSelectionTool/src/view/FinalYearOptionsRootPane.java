package view;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;


public class FinalYearOptionsRootPane extends BorderPane {

	private CreateStudentProfilePane cspp;
	private SelectModulesPane smp;
	private SelectReservedModulesPane srmp;
	private ModulesOverviewPane mop;
	private FinalYearOptionsMenuBar mstmb;
	private TabPane tp;
	
	public FinalYearOptionsRootPane() {
		tp = new TabPane();
		tp.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		
		cspp = new CreateStudentProfilePane();
		smp = new SelectModulesPane();
		srmp = new SelectReservedModulesPane();
		mop = new ModulesOverviewPane();
		
		Tab t1 = new Tab("Create Profile", cspp);
		Tab t2 = new Tab("Module Selection", smp);
		Tab t3 = new Tab("Reserved Modules Selection", srmp);
		Tab t4 = new Tab("Overview of Modules", mop);
		
		tp.getTabs().addAll(t1,t2,t3,t4);
		
		mstmb = new FinalYearOptionsMenuBar();
		
		this.setTop(mstmb);
		this.setCenter(tp);
	}

	public CreateStudentProfilePane getCreateStudentProfilePane() {
		return cspp;
	}
	
	public SelectModulesPane getSelectModulesPane() {
		return smp;
	}
	
	public SelectReservedModulesPane getSelectReservedModulesPane() {
		return srmp;
	}
	
	public ModulesOverviewPane getModulesOverviewPane() {
		return mop;
	}
	
	public FinalYearOptionsMenuBar getModuleSelectionToolMenuBar() {
		return mstmb;
	}
	
	public void changeTab(int index) {
		tp.getSelectionModel().select(index);
	}
}
