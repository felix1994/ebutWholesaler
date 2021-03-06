package de.htwg_konstanz.ebus.wholesaler.main;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOCountry;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOProduct;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOSalesPrice;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOSupplier;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.CountryBOA;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.PriceBOA;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.ProductBOA;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa._BaseBOA;

public class MyBMEcatParser {

	private static final double MARGE = 1.2;
	private Document document;
	private ImportInformation importInfo;

	public MyBMEcatParser(Document doc, ImportInformation importInfo) {
		this.document = doc;
		// this.pboa = ProductBOA.getInstance();
		this.importInfo = importInfo;
	}

	private boolean validateDocument() {
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
			return false;
		} catch (IOException e) {
			importInfo.setIoException(true);
			importInfo.setProblemOccured(true);
			return false;
		}
		return true;
	}

	public void start() {
		if (!validateDocument())
			return;
		NodeList articles = XPathMethods.getArticles(document.getDocumentElement());
		if (articles == null || articles.getLength() == 0)
			return;
		int size = articles.getLength();
		importInfo.setArticlesFound(size);
		for (int i = 0; i < size; i++) {
			try {
				BOProduct article = new BOProduct();
				Node articleNode = articles.item(i);
				String orderNumber = XPathMethods.getOrderNumber(articleNode);
				// Ordernumber is concatenation of supplier_aid + suppliername,
				// if no supplier was found no ordernumber could be created ->
				// null
				if (orderNumber == null) {
					importInfo.setProblemOccured(true);
					importInfo.setSupplierFound(false);
					return;
				}
				article.setOrderNumberSupplier(orderNumber);
				article.setShortDescription(XPathMethods.getShortdescription(articleNode));
				article.setOrderNumberCustomer(XPathMethods.getOrderNumber(articleNode));
				article.setOrderNumberSupplier(XPathMethods.getOrderNumberSupplier(articleNode));
				BOSupplier supplier = XPathMethods.getSupplier(document.getDocumentElement());
				if (supplier == null) {
					importInfo.setSupplierFound(false);
					importInfo.setProblemOccured(true);
				} else {
					article.setSupplier(supplier);
					// Check if an identical article is already stored in
					// database
					int materialNumber = checkIfArticleIsAlreadyStored(article, articleNode);
					if (materialNumber != -1) {
						BOProduct product = ProductBOA.getInstance().findByMaterialNumber(materialNumber);

						// when you delete a product, automatically all
						// salesPrices will be deleted -> on delete cascade

						// delete the product and commit it to the db
						ProductBOA.getInstance().delete(product);
						importInfo.addOneToProductsUpdated();
						commit();
					} else {
						importInfo.addOneToArticlesAddedToDatabase();
					}
					// Save the Product to the database
					ProductBOA.getInstance().saveOrUpdate(article);
					commit();
					createPrice(article, articleNode);
				}

			} catch (ConstraintViolationException e) {
				// TODO Database error :(
			}

		}

	}

	private void commit() {
		_BaseBOA.getInstance().commit();

	}

	private void createPrice(BOProduct article, Node articleNode) {
		NodeList articlePrices = XPathMethods.getCountry(articleNode);
		int size = articlePrices.getLength();
		for (int i = 0; i < size; i++) {
			Node articlePrice = articlePrices.item(i);
			NodeList territories = XPathMethods.getTerritories(articlePrice);
			int terrSize = territories.getLength();
			for (int j = 0; j < terrSize; j++) {
				String isoCode = territories.item(j).getFirstChild().getNodeValue();
				BOCountry country = CountryBOA.getInstance().findCountry(isoCode);
				if (country == null) {
					// do something...
					// e.g. leave the procedure or throw an exception...
					break;
				}

				// Create a sales price
				// The following attributes are mandatory to save or update a
				// BOSalesPrice
				// - Product (BOProduct)
				// - Country (BOCountry)
				// - LowerboundScaledprice
				double priceAmount = XPathMethods.getPriceAmount(articlePrice);
				double tax = XPathMethods.getTax(articlePrice);
				BigDecimal priceAmount2 = BigDecimal.valueOf(priceAmount * MARGE);
				BigDecimal tax2 = BigDecimal.valueOf(tax);
				BOSalesPrice salesPrice = new BOSalesPrice(priceAmount2, tax2, "net_list");
				salesPrice.setProduct(article);
				salesPrice.setCountry(country);
				salesPrice.setLowerboundScaledprice(1);
				PriceBOA.getInstance().saveOrUpdateSalesPrice(salesPrice);
				commit();
			}
		}

	}

	/**
	 * 
	 * @param article
	 * @param articleNode
	 * @return the new orderNumberSupplier or "alreadyStored" if the article is
	 *         already stored in the database
	 */
	private Integer checkIfArticleIsAlreadyStored2(BOProduct article, Node articleNode) {
		// Supplier AID + Supplier = orderNumberSupplier
		Map<String, Integer> orderNumberCustomer = new HashMap<String, Integer>();
		List<BOProduct> products = ProductBOA.getInstance().findAll();
		for (BOProduct product : products) {
			orderNumberCustomer.put(product.getOrderNumberCustomer(), product.getMaterialNumber());
		}
		String orderNumber = article.getOrderNumberCustomer();

		if (orderNumberCustomer.containsKey(orderNumber)) {
			return orderNumberCustomer.get(orderNumber);
		} else
			return -1;
	}

	private Integer checkIfArticleIsAlreadyStored(BOProduct article, Node articleNode) {
		String artSupplier = article.getSupplier().getCompanyname();
		String artOrderNumberSupplier = article.getOrderNumberSupplier();

		// key supplier value ordernumber
		Map<String, Map<String, Integer>> databaseValues = new HashMap<String, Map<String, Integer>>();
		List<BOProduct> products = ProductBOA.getInstance().findAll();
		for (BOProduct product : products) {
			if (!databaseValues.containsKey(product.getSupplier().getCompanyname())) {
				databaseValues.put(product.getSupplier().getCompanyname(), new HashMap<String, Integer>());
				databaseValues.get(product.getSupplier().getCompanyname()).put(product.getOrderNumberSupplier(),
						product.getMaterialNumber());
			} else {
				databaseValues.get(product.getSupplier().getCompanyname()).put(product.getOrderNumberSupplier(),
						product.getMaterialNumber());
			}
		}

		if (databaseValues.containsKey(artSupplier)) {
			if (databaseValues.get(artSupplier).get(artOrderNumberSupplier) != null)
				return databaseValues.get(artSupplier).get(artOrderNumberSupplier);
			else
				return -1;
		} else {
			return -1;
		}
	}

}
