package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Module;
import model.RunPlan;

public class SelectReservedModulesPane extends BorderPane {

	private ListView<Module> term1Opt, term2Opt, term1Sel, Term2Sel;
	private ObservableList<Module> term1Optlist, term2Optlist, term1Sellist, term2Sellist;
	private Button btnAdd1, btnAdd2, btnRemove1, btnRemove2, btnConfirm1, btnConfirm2;
	private Label lblCred;
	private int size;
	private Accordion ReservedModuleSelection;
	private TitledPane term1, term2;

	public SelectReservedModulesPane() {
		
		Label lblMaxCred = new Label("/60");
		Label lblCredTitle = new Label("Credits for year: ");
		lblCred = new Label("0");
		
		size = 29;
	
		term1Optlist = FXCollections.observableArrayList();
		term1Opt = new ListView<>(term1Optlist);
		term1Opt.setPrefHeight(size);
		term1Opt.setMaxHeight(Control.USE_PREF_SIZE);
		
		term2Optlist = FXCollections.observableArrayList();
		term2Opt = new ListView<>(term2Optlist);
		term2Opt.setPrefHeight(size);
		term2Opt.setMaxHeight(Control.USE_PREF_SIZE);

		term1Sellist = FXCollections.observableArrayList();
		term1Sel = new ListView<>(term1Sellist);
		term1Sel.setPrefHeight(size);
		term1Sel.setMaxHeight(Control.USE_PREF_SIZE);

		term2Sellist = FXCollections.observableArrayList();
		Term2Sel = new ListView<>(term2Sellist);
		Term2Sel.setPrefHeight(size);
		Term2Sel.setMaxHeight(Control.USE_PREF_SIZE);
		
		btnAdd1 = new Button("Add");
		btnAdd2 = new Button("Add");
		btnRemove1 = new Button("Remove");
		btnRemove2 = new Button("Remove");
		btnConfirm1 = new Button("Confirm");
		btnConfirm2 = new Button("Confirm");
		
		VBox btnBox1 = new VBox(btnAdd1,btnRemove1, btnConfirm1);
		VBox btnBox2 = new VBox(btnAdd2,btnRemove2, btnConfirm2);
		
		HBox term1modules = new HBox(term1Opt, btnBox1, term1Sel);
		HBox term2modules = new HBox(term2Opt, btnBox2, Term2Sel);
		HBox footer = new HBox(lblCredTitle, lblCred, lblMaxCred);
		
		HBox.setHgrow(term1Opt, Priority.ALWAYS);
		HBox.setHgrow(term1Sel, Priority.ALWAYS);
		HBox.setHgrow(term2Opt, Priority.ALWAYS);
		HBox.setHgrow(Term2Sel, Priority.ALWAYS);
		
		term1 = new TitledPane("Select Reserved Modules for term 1", term1modules);
		term2 = new TitledPane("Select Reserved Modules for term 2", term2modules);
		
		ReservedModuleSelection = new Accordion(term1, term2);
		ReservedModuleSelection.setExpandedPane(term1);
		
		this.setCenter(ReservedModuleSelection);
		this.setBottom(footer);
	}
	
	public void setListViewHeight(int i) {
		size = size * (i/2);
		term1Opt.setPrefHeight(size);
		term1Sel.setPrefHeight(size);
		term2Opt.setPrefHeight(size);
		Term2Sel.setPrefHeight(size);
	}
	
	public void addModules(ObservableList<Module> list) {
		setListViewHeight(list.size());
		
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getDelivery() == RunPlan.TERM_1) {
				term1Optlist.add(list.get(i));
				term1Sellist.remove(list.get(i));
			} else if (list.get(i).getDelivery() == RunPlan.TERM_2) {
				term2Optlist.add(list.get(i));
				term2Sellist.remove(list.get(i));
			}
		}
	}
	
	public void addModule(Module mod) {
		if (mod.getDelivery() == RunPlan.TERM_1) {
			term1Sellist.add(mod);
			term1Optlist.remove(mod);
		} else if (mod.getDelivery() == RunPlan.TERM_2) {
			term2Sellist.add(mod);
			term2Optlist.remove(mod);
		}
		addCredit(mod.getModuleCredits());
	}
	
	public void removeModule(Module mod) {
		if (mod.getDelivery() == RunPlan.TERM_1) {
			term1Optlist.add(mod);
			term1Sellist.remove(mod);
		} else if (mod.getDelivery() == RunPlan.TERM_2) {
			term2Optlist.add(mod);
			term2Sellist.remove(mod);
		}
		removeCredit(mod.getModuleCredits());
	}
	
	public Module getSelectedItem1(boolean sel) {
		Module selected;
		if (sel) {
			selected = term1Sel.getSelectionModel().getSelectedItem();
		} else {
			selected = term1Opt.getSelectionModel().getSelectedItem();
		} return selected;
	} 
	
	public Module getSelectedItem2(boolean sel) {
		Module selected;
		if (sel) {
			selected = Term2Sel.getSelectionModel().getSelectedItem();
		} else {
			selected = term2Opt.getSelectionModel().getSelectedItem();
		} return selected;
	} 
	
	public ObservableList<Module> getSelectedModulesTerm1() {
		return term1Sellist;
	}
	
	public ObservableList<Module> getSelectedModulesTerm2() {
		return term2Sellist;
	}
	
	public ObservableList<Module> getSelectedResModules() {
		ObservableList<Module> list = term1Sellist;
		list.addAll(term2Sellist);
		return list;
	}
	
	public void changePane() {
		ReservedModuleSelection.setExpandedPane(term2);
	}
	
	public Integer getCurrentPane() {
		int term;
		if (ReservedModuleSelection.getExpandedPane() == term1) {
			term = 1; 
		} else { 
			term = 2; 
		}
		return term;
	}
	
	public void addCredit(int credit) {
		int current = Integer.parseInt(lblCred.getText());
		current += credit;
		lblCred.setText(String.valueOf(current));
	}
	
	public void removeCredit(int credit) {
		int current = Integer.parseInt(lblCred.getText());
		current -= credit;
		lblCred.setText(String.valueOf(current));
	}

	public String getSelectedModulesOverview(int term, String info) {
		String selected = info;
		selected += "Reserved modules selected for this term:\n================\n";
		if (term == 1) {
			for (int i = 0; i < term1Sellist.size(); i++) {
				selected += term1Sellist.get(i).modulePrint() + "\n";
			}
		} else if (term == 2) {
			for (int i = 0; i < term2Sellist.size(); i++) {
				selected += term2Sellist.get(i).modulePrint() + "\n";
			}
		}
		return selected;
	}
	
	public void addResTerm1ModHandler(EventHandler<ActionEvent> handler) {
		btnAdd1.setOnAction(handler);
	}

	public void addResTerm2ModHandler(EventHandler<ActionEvent> handler) {
		btnAdd2.setOnAction(handler);
	}

	public void removeResTerm1ModHandler(EventHandler<ActionEvent> handler) {
		btnRemove1.setOnAction(handler);
	}

	public void removeResTerm2ModHandler(EventHandler<ActionEvent> handler) {
		btnRemove2.setOnAction(handler);
	}

	public void confTerm1ResModSelHandler(EventHandler<ActionEvent> handler) {
		btnConfirm1.setOnAction(handler);
	}
	public void confTerm2ResModSelHandler(EventHandler<ActionEvent> handler) {
		btnConfirm2.setOnAction(handler);
	}
}
