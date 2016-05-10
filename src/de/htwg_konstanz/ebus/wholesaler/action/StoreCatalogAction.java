package de.htwg_konstanz.ebus.wholesaler.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.htwg_konstanz.ebus.wholesaler.demo.IAction;
import de.htwg_konstanz.ebus.wholesaler.demo.util.Constants;
import de.htwg_konstanz.ebus.wholesaler.main.ImportInformation;
import de.htwg_konstanz.ebus.wholesaler.main.MyBMEcatParser;

public class StoreCatalogAction implements IAction {

	private static File file;
	private static ImportInformation importInfo;

	public StoreCatalogAction() {
		super();
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, ArrayList<String> errorList) {

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

		ServletFileUpload upload = new ServletFileUpload(factory);
		InputStream stream = null;
		try {
			// parses the request's content to extract file data
			@SuppressWarnings("unchecked")
			List<FileItem> formItems = upload.parseRequest(request);
			FileItem fileItem = formItems.get(0);
			stream = fileItem.getInputStream();
		} catch (Exception ex) {
			request.setAttribute("message", "There was an error: " + ex.getMessage());
		}

		// Initialize ImportInfo class
		importInfo = new ImportInformation();

		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;
		try {
			builder = dbfactory.newDocumentBuilder();
			document = builder.parse(stream);
			stream.close();
		} catch (ParserConfigurationException | IOException e) {
			importInfo.setIoException(true);
			importInfo.setProblemOccured(true);
		} catch (SAXException f) {
			importInfo.setWellformed(false);
			importInfo.setProblemOccured(true);
		}
		// Start parsing the file and search for articles to save in the databse
		MyBMEcatParser parser = new MyBMEcatParser(document, importInfo);
		parser.start();

		// Set a session attribute with key=store_catalog and value the
		// MyBMEcatParser instance to be able to use its variables via jsp
		request.getSession(true).setAttribute(Constants.ACTION_STORE_CATALOG, importInfo);

		return "importCatalogResult.jsp";
	}

	@Override
	public boolean accepts(String actionName) {
		return actionName.equalsIgnoreCase(Constants.ACTION_STORE_CATALOG);
	}

}
