package skeleton;

import java.util.HashMap;

public class Adapter {
	public static final String[] BEVERAGES = new String[] {
			"Caff� Americano", "Caff� Mocha", "Caff� Latte", 
			"Cappuccino", "Caramel Macchiato", "Espresso" }; // You can change these

	/**
	* This function compute the edit distance between a string and every 
	* strings in your selected beverage set. The beverage with the minimum 
	* edit distance <= 3 is returned. The use of Wagner_Fischer algorithm
	* is shown in the main function in WagnerFischer.java
	**/
	public String getBeverage(String s){
		// TODO: find the word with minimum edit distance
		for (int i = 0; i < BEVERAGES.length; i++){
			WagnerFischer wagnerFischer = new WagnerFischer(s, BEVERAGES[i]);
			if (wagnerFischer.getDistance() <= 3) return BEVERAGES[i];
		}
		return null;
	}
}
