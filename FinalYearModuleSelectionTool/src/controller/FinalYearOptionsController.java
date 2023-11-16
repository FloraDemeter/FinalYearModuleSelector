package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import model.Course;
import model.RunPlan;
import model.Module;
import model.StudentProfile;
import view.FinalYearOptionsRootPane;
import view.ModulesOverviewPane;
import view.SelectModulesPane;
import view.SelectReservedModulesPane;
import view.CreateStudentProfilePane;
import view.FinalYearOptionsMenuBar;


public class FinalYearOptionsController {

	private FinalYearOptionsRootPane view;
	private StudentProfile model;
	private CreateStudentProfilePane cspp;
	private SelectModulesPane smp;
	private SelectReservedModulesPane srmp;
	private ModulesOverviewPane mop;
	private FinalYearOptionsMenuBar mstmb;
	private buildModules bModules;


	public FinalYearOptionsController(StudentProfile model, FinalYearOptionsRootPane view) {
		this.view = view;
		this.model = model;
		
		cspp = view.getCreateStudentProfilePane();
		mstmb = view.getModuleSelectionToolMenuBar();
		smp = view.getSelectModulesPane();
		srmp = view.getSelectReservedModulesPane();
		mop = view.getModulesOverviewPane();
		
		cspp.addCourseDataToComboBox(buildModulesAndCourses());
		
		//Method to load in modules from textfile
		//cspp.addCourseDataToComboBox(bModules.);

		this.attachEventHandlers();
	}

	
	private void attachEventHandlers() {
		cspp.addCreateStudentProfileHandler(new CreateStudentProfileHandler());
		
		smp.addTerm1ModHandler(e -> smp.addModule(smp.getSelectedItem1(false)));
		smp.addTerm2ModHandler(e -> smp.addModule(smp.getSelectedItem2(false)));
		smp.removeTerm1ModHandler(e -> smp.removeModule(smp.getSelectedItem1(true)));
		smp.removeTerm2ModHandler(e -> smp.removeModule(smp.getSelectedItem2(true)));
		smp.submitModSelHandler(new SubmitHandler());
		smp.resetModSelHandler(e -> smp.clearContent());
		
		srmp.addResTerm1ModHandler(e -> srmp.addModule(srmp.getSelectedItem1(false)));
		srmp.addResTerm2ModHandler(e -> srmp.addModule(srmp.getSelectedItem2(false)));
		srmp.removeResTerm1ModHandler(e -> srmp.removeModule(srmp.getSelectedItem1(true)));
		srmp.removeResTerm2ModHandler(e -> srmp.removeModule(srmp.getSelectedItem2(true)));
		srmp.confTerm1ResModSelHandler(new confirmHandler());
		srmp.confTerm2ResModSelHandler(new confirmHandler());
		
		mop.saveOverviewHandler(new SaveOverviewHandler());
	
		mstmb.addLoadHandler(new LoadHandler());
		mstmb.addSaveHandler(new SaveHandler());
		mstmb.addExitHandler(e -> System.exit(0));
		mstmb.addAboutHandler(e -> this.alertDialogBuilder(AlertType.INFORMATION, "Information", null, "Final Year Module Selection Tool v1.0"));
	}
	
	private class CreateStudentProfileHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			
			String blank = "Please fill in all fields";
			boolean correct = true;
			
			
			if (cspp.getStudentPnumber().isBlank()) { 
				alertDialogBuilder(AlertType.ERROR, "Blank Info", null, blank);
				correct = false;
			}
			if (cspp.getStudentPnumber().length() != 8) {
				alertDialogBuilder(AlertType.ERROR, "Wrong Format", null, "Please ensure that P-number is in correct format");
				correct = false;
			}
			if (cspp.getStudentName().getFamilyName().isBlank() && cspp.getStudentName().getFirstName().isBlank()) { 
				alertDialogBuilder(AlertType.ERROR, "Blank Info", null, blank); 
				correct = false;
			}
			if (cspp.getStudentEmail().isBlank()) { 
				alertDialogBuilder(AlertType.ERROR, "Blank Info", null, blank); 
				correct = false;
			}
			
			
			if (correct) {
				model.setStudentCourse(cspp.getSelectedCourse());
				model.setStudentPnumber(cspp.getStudentPnumber());
				model.setStudentName(cspp.getStudentName());
				model.setStudentEmail(cspp.getStudentEmail() + "@dmu.ac.uk");
				model.setSubmissionDate(cspp.getStudentDate());
				
				mop.setPersonalInfo(model.printInfo());
				
				smp.setListViewHeight(cspp.getSelectedCourse().getAllModulesOnCourse().size());
				cspp.getSelectedCourse().getAllModulesOnCourse().forEach(m -> smp.addCourse(m));
				view.changeTab(1);
			}
		}
	}
	
	private class SubmitHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			if (smp.getSelectedModulesTerm1().size() == 3 && smp.getSelectedModulesTerm2().size() == 3 ) {
				mop.setTerm1Info(smp.getSelectedModulesOverview(1));
				mop.setTerm2Info(smp.getSelectedModulesOverview(2));
				
				srmp.addModules(smp.getRemainingModules());
				
				ObservableList<Module> list = smp.getSelectedModulesTerm1();
				list.addAll(smp.getSelectedModulesTerm2());
				
				for (int i = 0; i < list.size(); i++) {
					model.addSelectedModule(list.get(i));
				}
				view.changeTab(2);
			} else {
				alertDialogBuilder(AlertType.ERROR, "Error Submitting", null, "Please ensure 60 credits per term is selected.\nWith mandatory modules worth 15 credits.\nYear-long module worth 30 credits.");
			}
		}
	}
	
	private class confirmHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			if (srmp.getCurrentPane() == 1) {
				if (srmp.getSelectedModulesTerm1().size() == 2) {
					srmp.changePane();
				} else {
					alertDialogBuilder(AlertType.ERROR, "Error Submitting", null, "Please select 2 modules per term.");
				}
			} else if (srmp.getCurrentPane() == 2) {
				if (srmp.getSelectedModulesTerm2().size() == 2) {
					mop.setTerm1Info(srmp.getSelectedModulesOverview(1, mop.getTerm1Info()));
					mop.setTerm2Info(srmp.getSelectedModulesOverview(2, mop.getTerm2Info()));
					
					ObservableList<Module> list = srmp.getSelectedResModules();
					for (int i = 0; i < list.size(); i++) {
						model.addReservedModule(list.get(i));
					}
					view.changeTab(3);
				} else {
					alertDialogBuilder(AlertType.ERROR, "Error Submitting", null, "Please select 2 modules per term.");
				}
			}
		}
	}
	
	private class SaveOverviewHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			try {
			      FileWriter myWriter = new FileWriter("overview.txt");
			      myWriter.write("Student Information: \n////////////////////\n");
			      myWriter.write(model.printInfo());
			      myWriter.append("\nTerm 1 Selected Modules (including reserved): \n////////////////////\\n");
			      myWriter.append(mop.getTerm1Info());
			      myWriter.append("\nTerm 2 Selected Modules (including reserved): \n////////////////////\\n");
			      myWriter.append(mop.getTerm2Info());
			      myWriter.close();
			    } catch (IOException ioE) {
			    	alertDialogBuilder(AlertType.ERROR, "Error Saving", null, "Error saving selection overview.");
			    }
		}
	}
	
	//doesn't work - but should read in the binary object and fill in overview
	private class LoadHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			cspp.clearContent();
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("studentProfileObj.dat"));) {

				model = (StudentProfile) ois.readObject();
			
				alertDialogBuilder(AlertType.INFORMATION, "Information Dialog", "Load success", "Student Profile loaded from studentProfileObj.dat");
			}
			catch (IOException ioExcep){
				alertDialogBuilder(AlertType.ERROR, "Error Dialog", null, "Error Loading.");
			}
			catch (ClassNotFoundException c) {
				alertDialogBuilder(AlertType.ERROR, "Error Dialog", null, "Class Not Found.");
			}
			
			
		}
	}
	
	private class SaveHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("studentProfileObj.dat"));) {

				oos.writeObject(model.printInfo());
				oos.flush();

				alertDialogBuilder(AlertType.INFORMATION, "Information Dialog", "Save success", "Student Profile saved to studentProfileObj.dat");
			}
			catch (IOException ioExcep){
				alertDialogBuilder(AlertType.ERROR, "Error Dialog", null, "Error Saving.");
			}
		}
	}

	private void alertDialogBuilder(AlertType aType, String title, String header, String content) {
		Alert alert = new Alert(aType);
    	alert.setTitle(title);
    	alert.setHeaderText(header);
    	alert.setContentText(content);
    	alert.showAndWait();
	}

	//if can replace with scanner to read in modules instead of hardcoding.
	private Course[] buildModulesAndCourses() {
		Module imat3423 = new Module("IMAT3423", "Systems Building: Methods", 15, true, RunPlan.TERM_1);
		Module ctec3451 = new Module("CTEC3451", "Development Project", 30, true, RunPlan.YEAR_LONG);
		Module ctec3902_SoftEng = new Module("CTEC3902", "Rigorous Systems", 15, true, RunPlan.TERM_2);	
		Module ctec3902_CompSci = new Module("CTEC3902", "Rigorous Systems", 15, false, RunPlan.TERM_2);
		Module ctec3110 = new Module("CTEC3110", "Secure Web Application Development", 15, false, RunPlan.TERM_1);
		Module ctec3605 = new Module("CTEC3605", "Multi-service Networks 1", 15, false, RunPlan.TERM_1);	
		Module ctec3606 = new Module("CTEC3606", "Multi-service Networks 2", 15, false, RunPlan.TERM_2);	
		Module ctec3410 = new Module("CTEC3410", "Web Application Penetration Testing", 15, false, RunPlan.TERM_2);
		Module ctec3904 = new Module("CTEC3904", "Functional Software Development", 15, false, RunPlan.TERM_2);
		Module ctec3905 = new Module("CTEC3905", "Front-End Web Development", 15, false, RunPlan.TERM_2);
		Module ctec3906 = new Module("CTEC3906", "Interaction Design", 15, false, RunPlan.TERM_1);
		Module ctec3911 = new Module("CTEC3911", "Mobile Application Development", 15, false, RunPlan.TERM_1);
		Module imat3410 = new Module("IMAT3104", "Database Management and Programming", 15, false, RunPlan.TERM_2);
		Module imat3406 = new Module("IMAT3406", "Fuzzy Logic and Knowledge Based Systems", 15, false, RunPlan.TERM_1);
		Module imat3611 = new Module("IMAT3611", "Computer Ethics and Privacy", 15, false, RunPlan.TERM_1);
		Module imat3613 = new Module("IMAT3613", "Data Mining", 15, false, RunPlan.TERM_1);
		Module imat3614 = new Module("IMAT3614", "Big Data and Business Models", 15, false, RunPlan.TERM_2);
		Module imat3428_CompSci = new Module("IMAT3428", "Information Technology Services Practice", 15, false, RunPlan.TERM_2);


		Course compSci = new Course("Computer Science");
		compSci.addModuleToCourse(imat3423);
		compSci.addModuleToCourse(ctec3451);
		compSci.addModuleToCourse(ctec3902_CompSci);
		compSci.addModuleToCourse(ctec3110);
		compSci.addModuleToCourse(ctec3605);
		compSci.addModuleToCourse(ctec3606);
		compSci.addModuleToCourse(ctec3410);
		compSci.addModuleToCourse(ctec3904);
		compSci.addModuleToCourse(ctec3905);
		compSci.addModuleToCourse(ctec3906);
		compSci.addModuleToCourse(ctec3911);
		compSci.addModuleToCourse(imat3410);
		compSci.addModuleToCourse(imat3406);
		compSci.addModuleToCourse(imat3611);
		compSci.addModuleToCourse(imat3613);
		compSci.addModuleToCourse(imat3614);
		compSci.addModuleToCourse(imat3428_CompSci);

		Course softEng = new Course("Software Engineering");
		softEng.addModuleToCourse(imat3423);
		softEng.addModuleToCourse(ctec3451);
		softEng.addModuleToCourse(ctec3902_SoftEng);
		softEng.addModuleToCourse(ctec3110);
		softEng.addModuleToCourse(ctec3605);
		softEng.addModuleToCourse(ctec3606);
		softEng.addModuleToCourse(ctec3410);
		softEng.addModuleToCourse(ctec3904);
		softEng.addModuleToCourse(ctec3905);
		softEng.addModuleToCourse(ctec3906);
		softEng.addModuleToCourse(ctec3911);
		softEng.addModuleToCourse(imat3410);
		softEng.addModuleToCourse(imat3406);
		softEng.addModuleToCourse(imat3611);
		softEng.addModuleToCourse(imat3613);
		softEng.addModuleToCourse(imat3614);

		Course[] courses = new Course[2];
		courses[0] = compSci;
		courses[1] = softEng;

		return courses;
	}
}
