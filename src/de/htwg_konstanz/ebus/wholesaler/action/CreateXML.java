package de.htwg_konstanz.ebus.wholesaler.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.htwg_konstanz.ebus.wholesaler.demo.IAction;
import de.htwg_konstanz.ebus.wholesaler.demo.util.Constants;
import de.htwg_konstanz.ebus.wholesaler.main.MyBMEcatBuilder;

public class CreateXML implements IAction{

		public CreateXML() {
			super();
		}

		@Override
		public String execute(HttpServletRequest request, HttpServletResponse response, ArrayList<String> errorList) {
			MyBMEcatBuilder builder = new MyBMEcatBuilder();
			builder.start();
			
			return "createXML.jsp";
		}

		@Override
		public boolean accepts(String actionName) {
			return actionName.equalsIgnoreCase(Constants.ACTION_CREATE_XML);
		}
}
