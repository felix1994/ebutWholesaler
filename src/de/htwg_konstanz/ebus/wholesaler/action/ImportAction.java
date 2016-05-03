package de.htwg_konstanz.ebus.wholesaler.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOProduct;
import de.htwg_konstanz.ebus.wholesaler.demo.IAction;
import de.htwg_konstanz.ebus.wholesaler.demo.util.Constants;

public class ImportAction implements IAction {

	public ImportAction() {
		super();
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, ArrayList<String> errorList) {
		return "import.jsp";
	}

	@Override
	public boolean accepts(String actionName) {
		return actionName.equalsIgnoreCase(Constants.ACTION_IMPORT);
	}

}
