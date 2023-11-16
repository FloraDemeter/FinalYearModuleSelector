package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Module;
import model.RunPlan;

public class SelectModulesPane extends GridPane {
	
	private ListView<Module> term1Opt, term2Opt, term1Sel, term2Sel, termYearLong;
	private ObservableList<Module> term1Optlist, term2Optlist, term1Sellist, term2Sellist, termYearLongList;
	private Button btnAdd1, btnAdd2, btnRemove1, btnRemove2, btnReset, btnSubmit;
	private int size;
	private TextArea term1Cred, term2Cred;

	public SelectModulesPane() {
		
		this.setAlignment(Pos.CENTER);
		this.setPadding(new Insets(10, 10, 10, 10));

		ColumnConstraints column0 = new ColumnConstraints();
		column0.setHalignment(HPos.RIGHT);
		
		size = 29;
		
		Label lbltermOpt1 = new Label("Select module for term 1: ");
		Label lbltermSel1 = new Label("Selected module for term 1: ");
		
		Label lbltermOpt2 = new Label("Select module for term 2: ");
		Label lbltermSel2 = new Label("Selected module for term 2: ");
		
		Label lblYearLong = new Label("Mandatory year long module: ");
		
		Label cred1 = new Label("Credits: ");
		Label cred2 = new Label("Credits: ");
		
		term1Cred = new TextArea("0");
		term1Cred.setEditable(false);
		term1Cred.setPrefSize(10,10);
		term1Cred.setMaxHeight(Control.USE_PREF_SIZE);
		
		term2Cred = new TextArea("0");
		term2Cred.setEditable(false);
		term2Cred.setPrefSize(10,10);
		term2Cred.setMaxHeight(Control.USE_PREF_SIZE);
		
		term1Optlist = FXCollections.observableArrayList();
		term1Opt = new ListView<>(term1Optlist);
		term1Opt.setPrefHeight(size);
		
		term2Optlist = FXCollections.observableArrayList();
		term2Opt = new ListView<>(term2Optlist);
		term2Opt.setPrefHeight(size);

		term1Sellist = FXCollections.observableArrayList();
		term1Sel = new ListView<>(term1Sellist);
		term1Sel.setPrefHeight(size);

		term2Sellist = FXCollections.observableArrayList();
		term2Sel = new ListView<>(term2Sellist);
		term2Sel.setPrefHeight(size);
		
		termYearLongList = FXCollections.observableArrayList();
		termYearLong = new ListView<>(termYearLongList);
		termYearLong.setPrefHeight(29);
		
		btnAdd1 = new Button("Add");
		btnAdd2 = new Button("Add");
		btnRemove1 = new Button("Remove");
		btnRemove2 = new Button("Remove");
		btnReset = new Button("Reset");
		btnSubmit = new Button("Submit");
		
		VBox credit1 = new VBox(cred1 ,term1Cred);
		VBox credit2 = new VBox(cred2, term2Cred);
		VBox btnBox1 = new VBox(btnAdd1,btnRemove1, credit1);
		VBox btnBox2 = new VBox(btnAdd2,btnRemove2, credit2);
		VBox header = new VBox(lblYearLong, termYearLong);
		
		HBox btnBox3 = new HBox(btnSubmit,btnReset);
		
		btnBox3.setAlignment(Pos.CENTER);
		
		this.add(header, 0, 0, 3, 1);
		
		this.add(lbltermOpt1, 0, 1);
		this.add(new HBox(), 1, 1);
		this.add(lbltermSel1, 2, 1);
		
		this.add(term1Opt, 0, 2);
		this.add(btnBox1, 1, 2);
		this.add(term1Sel, 2, 2);
		
		this.add(lbltermOpt2, 0, 3);
		this.add(new HBox(), 1, 3);
		this.add(lbltermSel2, 2, 3);
		
		this.add(term2Opt, 0, 4);
		this.add(btnBox2, 1, 4);
		this.add(term2Sel, 2, 4);
		
		this.add(btnBox3, 0, 5, 3, 1);
		
		this.setHgrow(term1Opt, Priority.ALWAYS);	
		this.setHgrow(term1Sel, Priority.ALWAYS);		
	}
	
	public void setListViewHeight(int i) {
		size = size * (i/4);
		term1Opt.setPrefHeight(size);
		term1Sel.setPrefHeight(size);
		term2Opt.setPrefHeight(size);
		term2Sel.setPrefHeight(size);
	}
	
	public void addCourse(Module mod) {
		if (mod.isMandatory()) {
			if (mod.getDelivery() == RunPlan.YEAR_LONG) {
				termYearLongList.add(mod);
				addCredit(mod.getModuleCredits()/2, 1);
				addCredit(mod.getModuleCredits()/2, 2);
			}
			if (mod.getDelivery() == RunPlan.TERM_1) {
				term1Sellist.add(mod);
				addCredit(mod.getModuleCredits(), 1);
			} else if (mod.getDelivery() == RunPlan.TERM_2) {
				term2Sellist.add(mod);
				addCredit(mod.getModuleCredits(), 2);
			}
		} else {
			if (mod.getDelivery() == RunPlan.TERM_1) {
				term1Optlist.add(mod);
			} else if (mod.getDelivery() == RunPlan.TERM_2) {
				term2Optlist.add(mod);
			}
		}
	}
	
	public void addModule(Module mod) {
		if (mod.getDelivery() == RunPlan.TERM_1) {
			term1Sellist.add(mod);
			term1Optlist.remove(mod);
			addCredit(mod.getModuleCredits(),1);
		} else if (mod.getDelivery() == RunPlan.TERM_2) {
			term2Sellist.add(mod);
			term2Optlist.remove(mod);
			addCredit(mod.getModuleCredits(),2);
		}
	}
	
	public void removeModule(Module mod) {
		if (!mod.isMandatory()) {
			if (mod.getDelivery() == RunPlan.TERM_1) {
				term1Sellist.remove(mod);
				term1Optlist.add(mod);
				removeCredit(mod.getModuleCredits(), 1);
			} else if (mod.getDelivery() == RunPlan.TERM_2) {
				term2Sellist.remove(mod);
				term2Optlist.add(mod);
				removeCredit(mod.getModuleCredits(), 2);
			}
		}
	}
	
	public void clearContent() {
		setCredit(0);
		
		ObservableList<Module> mandatory = FXCollections.observableArrayList();
		ObservableList<Module> optional = FXCollections.observableArrayList();
		
		term1Sellist.forEach(m -> {if (m.isMandatory() ) {
			mandatory.add(m); }});
		term2Sellist.forEach(m -> {if (m.isMandatory() ) {
			mandatory.add(m); }});
		
		term1Sellist.forEach(m -> {if (!m.isMandatory() ) {
			optional.add(m); }});
		term2Sellist.forEach(m -> {if (!m.isMandatory() ) {
			optional.add(m); }});
		
		term1Sellist.clear();
		term2Sellist.clear();
		
		mandatory.forEach(m -> {
			if (m.getDelivery() == RunPlan.TERM_1 ) {
				term1Sellist.add(m);
				addCredit(m.getModuleCredits(),1);
			} else if (m.getDelivery() == RunPlan.TERM_2 ) {
				term2Sellist.add(m);
				addCredit(m.getModuleCredits(),2);
			}});
		
		optional.forEach(m -> {
			if (m.getDelivery() == RunPlan.TERM_1 ) {
				term1Optlist.add(m);
			} else if (m.getDelivery() == RunPlan.TERM_2 ) {
				term2Optlist.add(m);
			}});
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
			selected = term2Sel.getSelectionModel().getSelectedItem();
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
	
	public ObservableList<Module> getRemainingModules() {
		ObservableList<Module> list = FXCollections.observableArrayList();
		list = term1Optlist;
		list.addAll(term2Optlist);
		return list;
	}
	
	public void addCredit(int credit, int i) {
		int current;
		if (i == 1) {
			current = Integer.parseInt(term1Cred.getText());
			current += credit;
			term1Cred.setText(String.valueOf(current));
		}
		if (i == 2) {
			current = Integer.parseInt(term2Cred.getText());
			current += credit;
			term2Cred.setText(String.valueOf(current));
		}
	}
	
	public void removeCredit(int credit, int i) {
		int current;
		if (i == 1) {
			current = Integer.parseInt(term1Cred.getText());
			current -= credit;
			term1Cred.setText(String.valueOf(current));
		}
		if (i == 2) {
			current = Integer.parseInt(term2Cred.getText());
			current -= credit;
			term2Cred.setText(String.valueOf(current));
		}
	}
	
	public void setCredit(int cred) {
		term1Cred.setText(String.valueOf(cred));
		term2Cred.setText(String.valueOf(cred));
	}
	
	public String getSelectedModulesOverview(int term) {
		String selected = "Modules selected for this term:\n================\n";
		if (term == 1) {
			for (int i = 0; i < term1Sellist.size(); i++) {
				selected += term1Sellist.get(i).modulePrint() + "\n";
			}
		} else if (term == 2) {
			for (int i = 0; i < term2Sellist.size(); i++) {
				selected += term2Sellist.get(i).modulePrint() + "\n";
			}
		}
		selected += "================\n";
		return selected;
	}
	
	public void addTerm1ModHandler(EventHandler<ActionEvent> handler) {
		btnAdd1.setOnAction(handler);
	}

	public void addTerm2ModHandler(EventHandler<ActionEvent> handler) {
		btnAdd2.setOnAction(handler);
	}

	public void removeTerm1ModHandler(EventHandler<ActionEvent> handler) {
		btnRemove1.setOnAction(handler);
	}

	public void removeTerm2ModHandler(EventHandler<ActionEvent> handler) {
		btnRemove2.setOnAction(handler);
	}

	public void resetModSelHandler(EventHandler<ActionEvent> handler) {
		btnReset.setOnAction(handler);
	}
	
	public void submitModSelHandler(EventHandler<ActionEvent> handler) {
		btnSubmit.setOnAction(handler);
	}

}
