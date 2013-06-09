package com.rohan.todo;

public class Item {
    private int id;
	private String itemName;
	private int status;

    /*
     * Constructor that sets up an item of name null and default unchecked state if no program input is given
     */
	public Item()
	{
		this.itemName=null;
		this.status=0;
	}

    /*
     * Constructor that sets up an item of name itemName and status status
     *
     * @param itemName is the name of the item
     * @param status is the status of the item (0 = unchecked, 1 = checked)
     */
	public Item(String itemName, int status) {
		super();
		this.itemName = itemName;
		this.status = status;
	}

    /*
     * Method to get the Id of a specific Item object
     *
     * @return id of the object
     */
	public int getId() {
		return id;
	}

    /*
     * Method to set the Id of a specific Item object
     *
     * @param id is the desired id for the object currently being processed
     */
	public void setId(int id) {
		this.id = id;
	}

    /*
     * Method to get the name of a specific Item object
     *
     * @return the name of the object as a string
     */
	public String getItemName() {
		return itemName;
	}

    /*
     * Method that sets the name of an Item object
     *
     * @param itemName is the desired name of the Item object currently being processed
     */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

    /*
     * Method that returns the status of an Item object
     *
     * @return the status of the object (0 = unchecked, 1 = checked)
     */
	public int getStatus() {
		return status;
	}

    /*
     * Method that sets the status of the Item object
     *
     * @param status sets the status of the object (0 = unchecked, 1 = checked)
     */
	public void setStatus(int status) {
		this.status = status;
	}
	
}
