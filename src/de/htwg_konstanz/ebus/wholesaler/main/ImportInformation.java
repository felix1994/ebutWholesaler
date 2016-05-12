package de.htwg_konstanz.ebus.wholesaler.main;

public class ImportInformation {

	private int articlesFound;
	private int articlesAddedToDatabase;
	private boolean valid;
	private boolean wellformed;
	private boolean ioException;
	private boolean supplierFound;
	private boolean problemOccured;
	private int productsUpdated;

	public ImportInformation() {
		this.articlesFound = 0;
		this.articlesAddedToDatabase = 0;
		this.valid = true;
		this.wellformed = true;
		this.ioException = false;
		this.supplierFound = true;
		this.problemOccured = false;
		this.productsUpdated = 0;
	}

	public int getProductsUpdated() {
		return productsUpdated;
	}

	public void addOneToProductsUpdated() {
		productsUpdated++;
	}

	public boolean isProblemOccured() {
		return problemOccured;
	}

	public void setProblemOccured(boolean problemOccured) {
		this.problemOccured = problemOccured;
	}

	public void addOneToArticlesAddedToDatabase() {
		articlesAddedToDatabase++;
	}

	public int getArticlesFound() {
		return articlesFound;
	}

	public void setArticlesFound(int articlesFound) {
		this.articlesFound = articlesFound;
	}

	public int getArticlesAddedToDatabase() {
		return articlesAddedToDatabase;
	}

	public void setArticlesAddedToDatabase(int articlesAddedToDatabase) {
		this.articlesAddedToDatabase = articlesAddedToDatabase;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public boolean isWellformed() {
		return wellformed;
	}

	public void setWellformed(boolean wellformed) {
		this.wellformed = wellformed;
	}

	public boolean isIoException() {
		return ioException;
	}

	public void setIoException(boolean ioException) {
		this.ioException = ioException;
	}

	public boolean isSupplierFound() {
		return supplierFound;
	}

	public void setSupplierFound(boolean supplierFound) {
		this.supplierFound = supplierFound;
	}

}
