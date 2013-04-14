package com.example.shoppersstop;

public class ItemsMap {

	private String name;
	private String Catagory;
	private int X;
	private int Y;

	public ItemsMap(){
		
	}
	
	//Used by compareFunction in PathFinder
	public ItemsMap(int x, int y){
		this.X = x;
		this.Y = y;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getCatagory() {
		return Catagory;
	}

	public void setCatagory(String catagory) {
		Catagory = catagory;
	}
}
