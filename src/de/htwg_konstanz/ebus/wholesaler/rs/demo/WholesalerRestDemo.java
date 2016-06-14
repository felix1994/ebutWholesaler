package de.htwg_konstanz.ebus.wholesaler.rs.demo;

import java.io.File;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

@Path("/hello")
public class WholesalerRestDemo {

	@GET
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String msg) {

		String output = "Jersey says: " + msg;

		return Response.status(200).entity(output).build();
	}

	@GET
	@Path("/square/{param}")
	public Response getSquare(@PathParam("param") int num) {

		String output = "Square of " + num + " is " + (num * num);

		return Response.status(200).entity(output).build();
	}

	@POST
	@Consumes("text/plain")
	@Path("/squareroot")
	public Response getSquareRoot(final String num) {

		Integer x = Integer.valueOf(num);

		String output = "Squareroot of " + x + " is " + Math.sqrt(x);

		return Response.status(200).entity(output).build();
	}

	@GET
	@Path("/createoutput")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public Response createOutput(@Context UriInfo uriinfo, @QueryParam("outputtype") String outputtype,
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
