package de.htwg_konstanz.ebus.wholesaler.rs.demo;

import java.io.File;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.transform.dom.DOMSource;

import de.htwg_konstanz.ebus.wholesaler.demo.util.Constants;
import de.htwg_konstanz.ebus.wholesaler.main.MyBMEcatBuilder;

@Path("webservices")
public class OurWebServices {

	@POST
	@Path("/import")
	@Consumes(MediaType.APPLICATION_XML)
	public Response importCatalog() {
		/*
		 * // Set a session attribute with key=store_catalog and value the //
		 * ImportInformation instance to be able to use its variables via jsp //
		 * Initialize ImportInfo class ImportInformation importInfo = new
		 * ImportInformation();
		 * request.getSession(true).setAttribute(Constants.ACTION_STORE_CATALOG,
		 * importInfo);
		 * 
		 * DiskFileItemFactory factory = new DiskFileItemFactory();
		 * factory.setRepository(new
		 * File(System.getProperty("java.io.tmpdir")));
		 * 
		 * ServletFileUpload upload = new ServletFileUpload(factory);
		 * InputStream stream = null; try { // parses the request's content to
		 * extract file data
		 * 
		 * @SuppressWarnings("unchecked") List<FileItem> formItems =
		 * upload.parseRequest(request); FileItem fileItem = formItems.get(0);
		 * stream = fileItem.getInputStream(); if (stream.available() == 0) {
		 * importInfo.setFileChosen(false); } } catch (IndexOutOfBoundsException
		 * e) { importInfo.setFileChosen(false); } catch (Exception ex) {
		 * request.setAttribute("message", "There was an error: " +
		 * ex.getMessage()); }
		 * 
		 * DocumentBuilderFactory dbfactory =
		 * DocumentBuilderFactory.newInstance(); DocumentBuilder builder = null;
		 * Document document = null; try { builder =
		 * dbfactory.newDocumentBuilder(); document = builder.parse(stream);
		 * stream.close(); } catch (ParserConfigurationException | IOException
		 * e) { importInfo.setIoException(true);
		 * importInfo.setProblemOccured(true); } catch (SAXException f) {
		 * importInfo.setWellformed(false); importInfo.setProblemOccured(true);
		 * } // Start parsing the file and search for articles to save in the
		 * databse MyBMEcatParser parser = new MyBMEcatParser(document,
		 * importInfo); parser.start(); if (importInfo.isProblemOccured()) { if
		 * (!importInfo.isValid()) { return Response.status(200).entity(
		 * "File is not valid").build(); } if (!importInfo.isWellformed()) {
		 * return Response.status(200).entity("File is not wellformed").build();
		 * } if (!importInfo.isSupplierFound()) { return
		 * Response.status(200).entity("Supplier not found in DB").build(); } }
		 * String output = ""; output += "Articles added to db: " +
		 * importInfo.getArticlesAddedToDatabase() + "\n"; output +=
		 * "Articles updated: " + importInfo.getProductsUpdated(); return
		 * Response.status(200).entity(output).build();
		 */
		return Response.status(200).entity("lol").build();
	}

	@GET
	@Path("/export")
	@Produces({ MediaType.APPLICATION_XHTML_XML })
	public Response exportCatalog(@Context UriInfo uriinfo, @QueryParam("outputtype") String outputtype,
			@QueryParam("searchstring") String searchstring) {
		if (searchstring.length() == 0 || searchstring == null)
			searchstring = Constants.FULLCATALOG;
		if (Constants.XML.equals(outputtype)) {
			return createXML(searchstring);
		} else {
			return createXHTML(searchstring);
		}
	}

	private Response createXHTML(String searchstring) {
		MyBMEcatBuilder builder = new MyBMEcatBuilder();
		// ResponseBuilder response = Response.ok((File)
		// builder.start(searchstring, "xhtml"));
		// response.header("Content-Disposition", "attachment;
		// filename=\"exported_products.html\"");

		return Response.ok((File) builder.start(searchstring, Constants.XHTML), MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition", "attachment; filename=\"exported_products.html\"") // optional
				.build();
		// return response.build();
	}

	public Response createXML(String searchstring) {
		MyBMEcatBuilder builder = new MyBMEcatBuilder();
		ResponseBuilder response = Response.ok((DOMSource) builder.start(searchstring, Constants.XML));
		response.header("Content-Disposition", "attachment; filename=\"exported_products.xml\"");
		return response.build();
	}

}
