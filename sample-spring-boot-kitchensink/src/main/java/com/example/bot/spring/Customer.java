package com.example.bot.spring;

public class Customer {
	private String id;
	private String name;
	private int age;
	private Tour tour;
	private CustomerNo customerNo;
	private Fee fee;						// Ryan Tang
	private double paid_amount;				// Ryan Tang
	
	public Customer(
			String id,
			String name,
			int age,
			Tour tour, 
			CustomerNo customerNo,
			Fee fee,						// Ryan Tang
			double paid_amount) {			// Ryan Tang
		this.id = id;
		this.name = name;
		this.age = age;
		this.tour = tour;
		this.customerNo = customerNo;
		this.fee = fee;						// Ryan Tang
		this.paid_amount = paid_amount;		// Ryan Tang
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
	
	// Working from here to ...
	public void setFee(Fee fee) { this.fee = fee;}
	public Fee getFee() { return this.fee;}
	
	public void calculateFee() {
		String dates = this.tour.getDates();
		int weekdayPrice = this.tour.getweekDayPrice();
		int weekendPrice = this.tour.getweekEndPrice();
		
		int adult_num = this.customerNo.getAdultNo();
		int children_num = this.customerNo.getChildrenNo();
		
		int weekday_num = 0;
		int weekend_num = 0;
		
		// weekdays
		if(dates.toLowerCase().contains("mon")) {
			weekday_num += 1;
		}
		if(dates.toLowerCase().contains("tue")) {
			weekday_num += 1;
		}
		if(dates.toLowerCase().contains("wed")) {
			weekday_num += 1;
		}
		if(dates.toLowerCase().contains("thu")) {
			weekday_num += 1;
		}
		if(dates.toLowerCase().contains("fri")) {
			weekday_num += 1;
		}
		// weekends
		if (dates.toLowerCase().contains("sat")) {
			weekend_num += 1;
		}
		if (dates.toLowerCase().contains("sun")) {
			weekend_num += 1;
		}
		
		double adultPrice = adult_num * (weekdayPrice * weekday_num + weekendPrice * weekend_num);
		this.fee.setAdultFee(adultPrice);
		
		double childrenPrice = children_num * (weekdayPrice * weekday_num + weekendPrice * weekend_num);
		this.fee.setChildrenFee(childrenPrice);
		
		this.fee.setTotalFee();
	}
	
	public double pay(double amount) { 
		if (haveRemainPayment()) {
			this.paid_amount += amount;
			if (this.paid_amount > fee.getTotalFee()) {
				amount = this.paid_amount - fee.getTotalFee();
				this.paid_amount = fee.getTotalFee();
				return amount;
			}
			return 0;
		}
		else
			return amount;
	}
	public double getPayAmount() { return paid_amount;}
	public boolean haveRemainPayment()  { return (paid_amount >= fee.getTotalFee());}
	// here (by Ryan Tang)
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

// Working form here to ...
class Fee{
	private double total_fee;
	private double adult_fee;
	private double children_fee;
	
	public Fee(
			double total_fee,
			double adult_fee,
			double children_fee) {
		this.total_fee = total_fee;
		this.adult_fee = adult_fee;
		this.children_fee = children_fee;
	}
	
	public void setTotalFee() { this.total_fee = getAdultFee()+getChildrenFee();}
	public double getTotalFee()  {return total_fee;}
	
	public void setAdultFee(double fee) { this.adult_fee += fee;} // adult has no discount
	public double getAdultFee() { return adult_fee;}
	
	public void setChildrenFee(double fee) { this.children_fee += fee*0.2;} // children has 20% discount, toodler is free
	public double getChildrenFee() { return children_fee;}
} 
// here (by Ryan Tang)
