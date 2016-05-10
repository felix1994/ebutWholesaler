package de.htwg_konstanz.ebus.wholesaler.main;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOProduct;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.ProductBOA;

public class MyBMEcatBuilder {

	public void start() {
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			
			dbFactory.setValidating(true);
			dbFactory.setExpandEntityReferences(true);
			dbFactory.setIgnoringElementContentWhitespace(true);
			dbFactory.setNamespaceAware(false);
			dbFactory.setIgnoringComments(true);
			
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			Element bmecat = doc.createElement("BMECAT");
			doc.appendChild(bmecat);
			
			Element header = doc.createElement("HEADER");
			bmecat.appendChild(header);
			
			Element catalog = doc.createElement("CATALOG");
			header.appendChild(catalog);
			
			// konstant setzten 
			Element language = doc.createElement("LANGUAGE");
			catalog.appendChild(language);
			
			Element catalog_id = doc.createElement("CATALOG_ID");
			catalog.appendChild(catalog_id);
			
			Element catalog_version = doc.createElement("CATALOG_VERSION");
			catalog.appendChild(catalog_version);
			
			Element catalog_name = doc.createElement("CATALOG_NAME");
			catalog.appendChild(catalog_name);
			
			Element supplier = doc.createElement("SUPPLIER");
			header.appendChild(supplier);
			
			Element supplier_name = doc.createElement("SUPPLIER_NAME");
			supplier.appendChild(supplier_name);
			
			Element t_new_catalog = doc.createElement("T_NEW_CATALOG");
			bmecat.appendChild(t_new_catalog);
			
			ProductBOA pboa = ProductBOA.getInstance();
			List<BOProduct> articles = pboa.findAll();
			
			for (BOProduct a : articles) {
				Element article = doc.createElement("ARTICLE");
				t_new_catalog.appendChild(article);
				
				Element supplier_aid = doc.createElement("SUPPLIER_AID");
				article.appendChild(supplier_aid);
				// manufacturer equals supplier_aid?!?!
				supplier_aid.setNodeValue(a.getManufacturer());
				
				Element article_details = doc.createElement("ARTICLE_DETAILS");
				article.appendChild(article_details);
				
				Element description_short = doc.createElement("DESCRIPTION_SHORT");
				article_details.appendChild(description_short);
				description_short.setNodeValue(a.getShortDescription());
				
				Element description_long = doc.createElement("DESCRIPTION_LONG");
				article_details.appendChild(description_long);
				description_long.setNodeValue(a.getLongDescription());
				
				Element ean = doc.createElement("EAN");
				article_details.appendChild(ean);
				// ean.setNodeValue();
				
			}
			
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer t = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("/Users/ineshamma/Documents/result.xml"));
			t.transform(source, result);
			
			StreamResult consoleResult = new StreamResult(System.out);
			t.transform(source, consoleResult);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
