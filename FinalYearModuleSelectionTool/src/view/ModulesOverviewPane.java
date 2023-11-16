package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class ModulesOverviewPane extends GridPane {

	private TextArea txtProfile, txtTerm1, txtTerm2 ;
	private Button btnSaveOverview;

	public ModulesOverviewPane() {
		
		this.setAlignment(Pos.CENTER);
		this.setPadding(new Insets(10, 10, 10, 10));

		ColumnConstraints column0 = new ColumnConstraints();
		column0.setHalignment(HPos.RIGHT);
		column0.setHgrow(Priority.ALWAYS);		

		this.getColumnConstraints().addAll(column0);

		Label lblPInfo = new Label("Personal Information: ");
		Label lblTerm1 = new Label("Term 1 Modules: ");
		Label lblTerm2 = new Label("Term 2 Modules: ");
		
		txtProfile = new TextArea();
		txtProfile.setEditable(false);
		txtProfile.setPrefHeight(100);
		
		txtTerm1 = new TextArea();
		txtTerm1.setEditable(false);
		txtTerm1.setPrefHeight(100);
		
		txtTerm2 = new TextArea();
		txtTerm2.setEditable(false);
		txtTerm2.setPrefHeight(100);
		
		btnSaveOverview = new Button("Save Overview");
		
		VBox pInfoBox = new VBox(lblPInfo, txtProfile);
		VBox term1Box = new VBox(lblTerm1, txtTerm1);
		VBox term2Box = new VBox(lblTerm2, txtTerm2);
		
//		doesnt work -- but should allow to vertically grow 
//		VBox.setVgrow(txtTerm1, Priority.ALWAYS);
//		VBox.setVgrow(txtTerm2, Priority.ALWAYS);
//		RowConstraints row0 = new RowConstraints();
//		row0.setVgrow(Priority.ALWAYS);

		
		this.add(pInfoBox, 0, 0);
		this.add(term1Box, 0, 1);
		this.add(term2Box, 0, 2);
		this.add(btnSaveOverview, 0, 3);

	}

	public String getPersonalInfo() {
		return txtProfile.getText();
	}
	
	public String getTerm1Info() {
		return txtTerm1.getText();
	}

	public String getTerm2Info() {
		return txtTerm2.getText();
	}
	
	public void setPersonalInfo(String info) {
		txtProfile.setText(info);
	}
	
	public void setTerm1Info(String info) {
		txtTerm1.setText(info);
	}

	public void setTerm2Info(String info) {
		txtTerm2.setText(info);
	}
	
	public void saveOverviewHandler(EventHandler<ActionEvent> handler) {
		btnSaveOverview.setOnAction(handler);
	}

}
