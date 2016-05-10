package de.htwg_konstanz.ebus.wholesaler.main;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.hibernate.exception.ConstraintViolationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOProduct;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOSupplier;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.ProductBOA;

public class MyBMEcatParser {

	private Document document;
	private ProductBOA pboa;
	private ImportInformation importInfo;

	public MyBMEcatParser(Document doc, ImportInformation importInfo) {
		this.document = doc;
		this.pboa = ProductBOA.getInstance();
		this.importInfo = importInfo;
		validateDocument();
	}

	private void validateDocument() {
		try {
			// Create a SchemaFactory capable of understanding W3C schemas
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new File(
					"C://Users//Felix//Documents//Studium//6. Semester//EBUT//Labor//Assignment3//Docs//bmecat_new_catalog_1_2_simple_without_NS.xsd"));
			Validator validator = schema.newValidator();
			validator.validate(new DOMSource(document));
		} catch (SAXException e) {
			importInfo.setValid(false);
			importInfo.setProblemOccured(true);
		} catch (IOException e) {
			importInfo.setIoException(true);
			importInfo.setProblemOccured(true);
		}
	}

	public void start() {
		NodeList articles = XPathMethods.getArticles(document.getDocumentElement());
		if (articles == null || articles.getLength() == 0)
			return;
		int size = articles.getLength();
		importInfo.setArticlesFound(size);
		for (int i = 0; i < size; i++) {
			try {
				BOProduct article = new BOProduct();
				Node articleNode = articles.item(i);
				article.setOrderNumberSupplier(String.valueOf(i));
				article.setShortDescription(XPathMethods.getShortdescription(articleNode));
				article.setOrderNumberCustomer(String.valueOf(i));
				BOSupplier supplier = XPathMethods.getSupplier(document.getDocumentElement());
				// Supplier not found -> null --> error
				if (supplier == null) {
					importInfo.setSupplierFound(false);
					importInfo.setProblemOccured(true);
				} else {
					article.setSupplier(supplier);
					// Check if an identical article is already stored in
					// database
					boolean alreadyStored = checkIfArticleIsAlreadyStored(article);
					if (alreadyStored) {
						// delete product and save the new one
					}
					// Save the Product to the database
					pboa.saveOrUpdate(article);
					importInfo.addOneToArticlesAddedToDatabase();

					// TODO ########## PREIS
				}

			} catch (ConstraintViolationException e) {
				// TODO
			}

		}

	}

	private boolean checkIfArticleIsAlreadyStored(BOProduct article) {
		// TODO Auto-generated method stub
		return false;
	}

}
