package com.example.bot.spring;

public class Fee{
	private Customer customer;
	private int total_fee;
	private int adult_fee;
	private int children_fee;
	private int toodler_fee;
	
	public Fee(Customer c) {
		this.customer = c;
		this.total_fee = 0;
		this.adult_fee = 0;
		this.children_fee = 0;
		this.toodler_fee = 0;
	}
	
	public void setTotalFee() {
		this.total_fee = getAdultFee(customer)+getChildrenFee(customer)+getToodlerFee(customer);
	}
	public int getTotalFee() {
		return total_fee;
	}
	
	public void setAdultFee() {
		int fee = 0;		
		this.adult_fee = fee;
	}
	public int getAdultFee() {
		return adult_fee;
	}
	
	public void setChildrenFee() {
		int fee = 0;		
		this.children_fee = fee;
	}
	public int getChildrenFee() {
		return children_fee;
	}
	
	public void setToodlerFee() {
		int fee = 0;
		this toodler_fee = fee;
	}
	public int getToodlerFee() {
		return toodler_fee;
	}
}