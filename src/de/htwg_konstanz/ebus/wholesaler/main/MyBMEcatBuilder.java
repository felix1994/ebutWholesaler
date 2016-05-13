package de.htwg_konstanz.ebus.wholesaler.main;

import java.io.File;
import java.math.BigDecimal;
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

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOCountry;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOProduct;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOSalesPrice;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.ProductBOA;

public class MyBMEcatBuilder {

	public void start(String searchstring) {

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
			bmecat.setAttribute("version", "1.2");
			doc.appendChild(bmecat);

			Element header = doc.createElement("HEADER");
			bmecat.appendChild(header);

			Element catalog = doc.createElement("CATALOG");
			header.appendChild(catalog);

			Element language = doc.createElement("LANGUAGE");
			language.appendChild(doc.createTextNode("deu"));
			catalog.appendChild(language);

			// ID generieren? Pflichtfeld!
			Element catalog_id = doc.createElement("CATALOG_ID");
			catalog_id.appendChild(doc.createTextNode("001"));
			catalog.appendChild(catalog_id);

			// Konstanz setzen?! Pflichtfeld!
			Element catalog_version = doc.createElement("CATALOG_VERSION");
			catalog_version.appendChild(doc.createTextNode("1.0"));
			catalog.appendChild(catalog_version);

			Element supplier = doc.createElement("SUPPLIER");
			header.appendChild(supplier);

			// ? Pflichtfeld! Sind wir der Supplier? --> Konstant setzen?
			Element supplier_name = doc.createElement("SUPPLIER_NAME");
			supplier_name.appendChild(doc.createTextNode("Supplier"));
			supplier.appendChild(supplier_name);

			Element t_new_catalog = doc.createElement("T_NEW_CATALOG");
			bmecat.appendChild(t_new_catalog);

			ProductBOA pboa = ProductBOA.getInstance();
			List<BOProduct> articles = pboa.findAll();

			// If the user wants to export some products via entering a
			// searchstring
			String search = "%" + searchstring + "%";
			if (!searchstring.equals("fullcatalog"))
				articles = pboa.findByShortdescriptionLike(search);

			for (BOProduct a : articles) {
				Element article = doc.createElement("ARTICLE");
				t_new_catalog.appendChild(article);

				Element supplier_aid = doc.createElement("SUPPLIER_AID");
				// OrderNumberSupplier muss aufgesplittet werden --> wie?!
				String s_aid = a.getOrderNumberSupplier();
				supplier_aid.appendChild(doc.createTextNode(s_aid));
				article.appendChild(supplier_aid);

				Element article_details = doc.createElement("ARTICLE_DETAILS");
				article.appendChild(article_details);

				Element description_short = doc.createElement("DESCRIPTION_SHORT");
				String descr_short = a.getShortDescription();
				description_short.appendChild(doc.createTextNode(descr_short));
				article_details.appendChild(description_short);

				String descr_long = a.getLongDescription();
				if (descr_long != null) {
					Element description_long = doc.createElement("DESCRIPTION_LONG");
					description_long.appendChild(doc.createTextNode(descr_long));
					article_details.appendChild(description_long);
				}

				Element article_order_details = doc.createElement("ARTICLE_ORDER_DETAILS");
				article.appendChild(article_order_details);

				Element order_unit = doc.createElement("ORDER_UNIT");
				order_unit.appendChild(doc.createTextNode("PK"));
				article_order_details.appendChild(order_unit);

				Element article_price_details = doc.createElement("ARTICLE_PRICE_DETAILS");
				article.appendChild(article_price_details);

				List<BOSalesPrice> prices = a.getSalesPrices();

				for (BOSalesPrice price : prices) {
					Element article_price = doc.createElement("ARTICLE_PRICE");
					String price_type = price.getPricetype();
					article_price.setAttribute("price_type", price_type);
					article_price_details.appendChild(article_price);

					Element price_amount = doc.createElement("PRICE_AMOUNT");
					BigDecimal amount = price.getAmount();
					price_amount.appendChild(doc.createTextNode(amount.toString()));
					article_price.appendChild(price_amount);

					Element tax = doc.createElement("TAX");
					BigDecimal t = price.getTaxrate();
					tax.appendChild(doc.createTextNode(t.toString()));
					article_price.appendChild(tax);

					Element territory = doc.createElement("TERRITORY");
					BOCountry country = price.getCountry();
					territory.appendChild(doc.createTextNode(country.getIsocode()));
					article_price.appendChild(territory);
				}

			}

			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer t = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("/Users/Felix/Desktop/result.xml"));
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
