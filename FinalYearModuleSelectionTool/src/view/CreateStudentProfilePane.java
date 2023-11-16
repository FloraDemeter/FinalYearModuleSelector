package view;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.Course;
import model.Name;

public class CreateStudentProfilePane extends GridPane {

	private ComboBox<Course> cboCourses;
	private DatePicker inputDate;
	private TextField txtFirstName, txtSurname,  txtPnumber, txtEmail;
	private Button btnCreateProfile;

	public CreateStudentProfilePane() {
		this.setVgap(15);
		this.setHgap(20);
		this.setAlignment(Pos.CENTER);

		ColumnConstraints column0 = new ColumnConstraints();
		column0.setHalignment(HPos.RIGHT);

		this.getColumnConstraints().addAll(column0);
		
		Label lblTitle = new Label("Select course: ");
		Label lblPnumber = new Label("Input P number: ");
		Label lblFirstName = new Label("Input first name: ");
		Label lblSurname = new Label("Input surname: ");
		Label lblEmail = new Label("Input email: ");
		Label lblEmailEnd = new Label("@dmu.ac.uk");
		Label lblDate = new Label("Input date: ");
		
		cboCourses = new ComboBox<Course>();
		
		txtFirstName = new TextField();
		txtSurname = new TextField();
		txtPnumber = new TextField();
		txtEmail = new TextField();
		
		inputDate = new DatePicker();
		
		btnCreateProfile = new Button("Create Profile");
		
		HBox email = new HBox(txtEmail, lblEmailEnd);

		this.add(lblTitle, 0, 0);
		this.add(cboCourses, 1, 0);

		this.add(lblPnumber, 0, 1);
		this.add(txtPnumber, 1, 1);
		
		this.add(lblFirstName, 0, 2);
		this.add(txtFirstName, 1, 2);

		this.add(lblSurname, 0, 3);
		this.add(txtSurname, 1, 3);
		
		this.add(lblEmail, 0, 4);
		//this.add(txtEmail, 1, 4);
		this.add(email, 1, 4);
		
		this.add(lblDate, 0, 5);
		this.add(inputDate, 1, 5);
			
		this.add(new HBox(), 0, 6);
		this.add(btnCreateProfile, 1, 6);
	}
	
	public void addCourseDataToComboBox(Course[] courses) {
		cboCourses.getItems().addAll(courses);
		cboCourses.getSelectionModel().select(0);
	}
	
	public Course getSelectedCourse() {
		return cboCourses.getSelectionModel().getSelectedItem();
	}
	
	public String getStudentPnumber() {
		return txtPnumber.getText();
	}
	
	public Name getStudentName() {
		return new Name(txtFirstName.getText(), txtSurname.getText());
	}

	public String getStudentEmail() {
		return txtEmail.getText();
	}
	
	public LocalDate getStudentDate() {
		return inputDate.getValue();
	}
	
	public void addCreateStudentProfileHandler(EventHandler<ActionEvent> handler) {
		btnCreateProfile.setOnAction(handler);
	}

	/**
	 * To clear content when loading in an existing student profile
	 */
	public void clearContent() {
		cboCourses.getSelectionModel().select(0);
		txtFirstName.setText("");
		txtSurname.setText("");
		txtPnumber.setText("");
		txtEmail.setText("");
		inputDate.setValue(null);
		
	}

}
