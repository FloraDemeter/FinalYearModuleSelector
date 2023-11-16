package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import model.Module;
import model.RunPlan;


public class buildModules {
	
	private static String courseName, xCourseName;

	public buildModules() throws FileNotFoundException {

		List<Module> temps = new ArrayList<>();
		Map<String, List<Module>> courses = new HashMap<>();

		
		Scanner sc = new Scanner(new File("Modules.txt"));
		sc.useDelimiter("[<>\r\n]+");
		
		courseName = sc.next();
		xCourseName = courseName;
		while (!courseName.equals("end")) {
			String moduleCode = sc.next();
			String moduleName = sc.next();
			String moduleCredit = sc.next();
			String moduleMandatory = sc.next();
			String moduleTerm = sc.next();
			RunPlan term = RunPlan.valueOf(moduleTerm);
			
			
			Module temp = new Module(moduleCode, moduleName, Integer.parseInt(moduleCredit),Boolean.parseBoolean(moduleMandatory),term);
			
			if (courseName.equals(xCourseName)) {
				Collections.addAll(temps, temp);
				courses.put(courseName, temps);
			} else {
//				Because of this it doesn't work.. and I don't have enough time to figure out why
//				wanted to use a temporary list for the module list but cannot figure out how to clear the list
//				but still keep the value that already existed without overwriting it...
//				if (!temps.isEmpty()) {
//					temps.clear();
//				}
				Collections.addAll(temps, temp);
				courses.put(courseName, temps);
			}
	
			xCourseName = courseName;
			courseName = sc.next();
			
		}
			
//			for (String key : courses.keySet()) {
//				System.out.println(key);
//				courses.get(key).forEach(m -> System.out.println(m.actualToString()));
//			}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass())
			return false;
		
		buildModules other = (buildModules) obj;
		
		return this.courseName == other.xCourseName;
		
	}
	
}
