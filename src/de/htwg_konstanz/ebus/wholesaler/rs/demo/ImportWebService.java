package de.htwg_konstanz.ebus.wholesaler.rs.demo;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.htwg_konstanz.ebus.wholesaler.main.ImportInformation;
import de.htwg_konstanz.ebus.wholesaler.main.MyBMEcatParser;

@Path("/import")
public class ImportWebService {

	@Path("/file")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public Response importFile(InputStream stream) {
		ImportInformation importInfo = new ImportInformation();
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
		if (!importInfo.isProblemOccured()) {
			MyBMEcatParser parser = new MyBMEcatParser(document, importInfo);
			parser.start();
		}

		String result = "";
		if (importInfo.isProblemOccured()) {
			if (!importInfo.isWellformed())
				result += "NOT WELLFORMED";
			if (!importInfo.isValid())
				result += "NOT VALID";
			if (!importInfo.isSupplierFound())
				result += "SUPPLIER NOT FOUND IN DB";
		} else {
			result += "Articles in your file: " + importInfo.getArticlesFound() + "\n";
			result += "Articles added to db: " + importInfo.getArticlesAddedToDatabase() + "\n";
			result += "Articles updated: " + importInfo.getProductsUpdated() + "\n";
		}
		return Response.ok().entity(result).build();
	}

}
