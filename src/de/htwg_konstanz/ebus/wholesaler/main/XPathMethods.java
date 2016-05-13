package de.htwg_konstanz.ebus.wholesaler.main;

import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOSupplier;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.SupplierBOA;

public class XPathMethods {
	private static XPath xpath = XPathFactory.newInstance().newXPath();
	private static SupplierBOA sboa = SupplierBOA.getInstance();

	public static Integer getMaterialNumber(Node node) {
		String expression = "ARTICLE_DETAILS/EAN[1]";
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
		return ean;
	}

	/**
	 * 
	 * @return the BOSupplier if the supplier already exists, otherwise a new
	 *         created one ?!
	 */
	public static BOSupplier getSupplier(Node node) {
		String expression = "//SUPPLIER_NAME";
		Node supplierNode = null;
		try {
			supplierNode = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		if (supplierNode == null) {
			// TODO ##########################
		}
		List<BOSupplier> suppliers = sboa.findAll();
		// return suppliers.get(1);

		String suppliername = supplierNode.getFirstChild().getNodeValue();
		boolean supplierExists = false;
		BOSupplier boSupplier = null;
		for (BOSupplier supplier : suppliers) {
			if (suppliername.equals(supplier.getCompanyname())) {
				supplierExists = true;
				boSupplier = supplier;
				break;
			}
		}
		if (supplierExists)
			return boSupplier;
		else {
			return null;
		}
	}

	/**
	 * 
	 * @return a NodeList with all ARTICLEs found, or null
	 */
	public static NodeList getArticles(Node node) {
		String expression = "//ARTICLE";
		NodeList nodes = null;
		try {
			nodes = (NodeList) xpath.evaluate(expression, node, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return nodes;
	}

	/**
	 * 
	 * @return a NodeList with all ARTICLEs found, or null
	 */
	public static String getShortdescription(Node node) {
		String expression = "ARTICLE_DETAILS/DESCRIPTION_SHORT[1]";
		Node shortDescrNode = null;
		try {
			shortDescrNode = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return shortDescrNode.getFirstChild().getNodeValue();
	}

	public static String getSupplierAid(Node node) {
		String expression = "SUPPLIER_AID[1]";
		Node supplierAidNode = null;
		try {
			supplierAidNode = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return supplierAidNode.getFirstChild().getNodeValue();
	}

	public static String getOrderNumber(Node node) {
		String supplier_aid = getSupplierAid(node);
		// To get the Header and the supplier name
		BOSupplier bosupplier = getSupplier(node.getParentNode().getParentNode());
		if (bosupplier == null)
			return null;
		String supplier = bosupplier.getCompanyname();
		String orderNumber = supplier_aid + supplier;
		return orderNumber;
	}

	public static NodeList getCountry(Node node) {
		String expression = "ARTICLE_PRICE_DETAILS/ARTICLE_PRICE";
		NodeList articlePrices = null;
		try {
			articlePrices = (NodeList) xpath.evaluate(expression, node, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return articlePrices;
	}

	public static NodeList getTerritories(Node node) {
		String expression = "TERRITORY";
		NodeList territories = null;
		try {
			territories = (NodeList) xpath.evaluate(expression, node, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return territories;
	}

	public static double getPriceAmount(Node node) {
		String expression = "PRICE_AMOUNT";
		Node priceAmount = null;
		try {
			priceAmount = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return Double.parseDouble(priceAmount.getFirstChild().getNodeValue());
	}

	public static double getTax(Node node) {
		String expression = "TAX";
		Node tax = null;
		try {
			tax = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return Double.parseDouble(tax.getFirstChild().getNodeValue());
	}

}
