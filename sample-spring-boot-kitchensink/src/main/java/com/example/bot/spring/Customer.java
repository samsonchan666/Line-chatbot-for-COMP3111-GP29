package com.example.bot.spring;

public class Customer {
	private String id;
	private String name;
	private int age;
	private Tour tour;
	private CustomerNo customerNo;
	
	public Customer(
			String id,
			String name,
			int age,
			Tour tour, 
			CustomerNo customerNo) {			
		this.id = id;
		this.name = name;
		this.age = age;
		this.tour = tour;
		this.customerNo = customerNo;
	}
	public void setId(String id) { this.id = id;}
	public String getId() { return this.id;}
	
	public void setName(String name) { this.name = name;}
	public String getName() { return this.name;}
	
	public void setAge(int age) { this.age = age;}
	public int getAge() { return this.age;}
	
	public void setTour(Tour tour) { this.tour = tour;}
	public Tour getTour() { return tour;}
	
	public void setCustomerNo(CustomerNo customerNo) { this.customerNo = customerNo;}
	public CustomerNo getCustomerNo() { return this.customerNo;}
	
}

class CustomerNo{
	private int adultNo;
	private int childrenNo;
	private int toodlerNo;
	
	public CustomerNo(
			int adultNo,
			int childrenNo,
			int toodlerNo ) {
		this.adultNo = adultNo;
		this.childrenNo = childrenNo;
		this.toodlerNo = toodlerNo;
	}
	
	public void setAdultNo(int adultNo) { this.adultNo = adultNo;}
	public int getAdultNo() { return this.adultNo;}
	
	public void setChildrenNo(int childrenNo) { this.childrenNo = childrenNo;}
	public int getChildrenNo() { return this.childrenNo;}
	
	public void setToodlerNo(int toodlerNo) { this.toodlerNo = toodlerNo;}
	public int getToodlerNo() { return this.toodlerNo;}
	
	
}
