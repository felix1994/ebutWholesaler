package de.htwg_konstanz.ebus.wholesaler.main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOSupplier;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.ProductBOA;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.SupplierBOA;

public class MyBMEcatParser {

	private XPathMethods xpathmethods;
	private Document document;
	private boolean valid;
	private SupplierBOA sboa;
	private ProductBOA pboa;
	private XPath xpath;
	private int articlesFound;
	private int articlesAddedToDatabase;

	public MyBMEcatParser(Document doc) {
		this.document = doc;
		this.xpath = XPathFactory.newInstance().newXPath();
		this.sboa = SupplierBOA.getInstance();
		this.pboa = ProductBOA.getInstance();
		this.valid = true;
		this.articlesAddedToDatabase = 0;
		this.articlesFound = 0;
		this.xpathmethods = new XPathMethods();
	}

	private boolean validateDocument() {
		try {
			// Create a SchemaFactory capable of understanding W3C schemas
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory
					.newSchema(new File("//WholesalerWebDemo//files//bmecat_new_catalog_1_2_simple_without_NS.xsd"));
			Validator validator = schema.newValidator();
			validator.validate(new DOMSource(document));
		} catch (SAXException | IOException e) {
			valid = false;
			return false;
		}
		return true;
	}

	public void start() {
		if (!validateDocument())
			return;
		NodeList articles = getArticles();
		if (articles == null || articles.getLength() == 0)
			return;
		int size = articles.getLength();
		// for (int i = 0; i < size; i++) {
		// BOProduct article = new BOProduct();
		// Node node = articles.item(i);

		// NOT NULL
		// article.setMaterialNumber(getMaterialNumber(node));
		// article.setOrderNumberSupplier();
		// article.setShortDescription();
		// article.setOrderNumberCustomer();
		// article.setSupplier(getSupplier());
		//
		// NULL
		//
		// }

		// Node node = articles.item(0);
		// BOProduct article = new BOProduct();
		// article.setMaterialNumber(getMaterialNumber(node));
		// article.setOrderNumberSupplier("atzusdsdf");
		// article.setShortDescription("assdfsd");
		// article.setOrderNumberCustomer("asfsdf2");
		// article.setSupplier(sboa.findSupplierById("10"));
		// pboa.saveOrUpdate(article);

		// Save the Product to de databse
		// pboa.saveOrUpdate(article);

	}

	private Integer getMaterialNumber(Node node) {
		String expression = "//EAN";
		Node materialNumber = null;
		try {
			materialNumber = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		if (materialNumber == null) {
			// TODO ##########################
		}

		int ean = Integer.parseInt(materialNumber.getFirstChild().getNodeValue());
		int x = 4;
		return ean;
	}

	/**
	 * 
	 * @return the BOSupplier if the supplier already exists, otherwise a new
	 *         created one ?!
	 */
	private BOSupplier getSupplier() {
		String expression = "//SUPPLIER_NAME";
		Node node = null;
		try {
			node = (Node) xpath.evaluate(expression, document.getDocumentElement(), XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		if (node == null) {
			// TODO ##########################
		}
		List<BOSupplier> suppliers = sboa.findAll();
		String suppliername = node.getNodeValue();
		boolean supplierExists = false;
		BOSupplier boSupplier = null;
		for (BOSupplier supplier : suppliers) {
			if (suppliername.equals(supplier.getLastname())) {
				supplierExists = true;
				boSupplier = supplier;
				break;
			}
		}
		if (supplierExists)
			return boSupplier;
		else {
			// TODO CREATE NEW SUPPLER ? ########
			return null;
		}
	}

	/**
	 * 
	 * @return a NodeList with all ARTICLEs found, or null
	 */
	private NodeList getArticles() {
		String expression = "//ARTICLE";
		NodeList nodes = null;
		try {
			nodes = (NodeList) xpath.evaluate(expression, document.getDocumentElement(), XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return nodes;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean isValid) {
		this.valid = isValid;
	}

	public int getArticlesFound() {
		return articlesFound;
	}

	public int getArticlesAddedToDatabase() {
		return articlesAddedToDatabase;
	}

}
