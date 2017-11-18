package com.example.bot.spring;

public class Customer implements Observer{
	private String id;
	private String name;
	private int age;
	private String phoneNum;
	private Tour tour;
	private CustomerNo customerNo;
	private Fee fee;						// Ryan Tang
	private double paid_amount;				// Ryan Tang
	private int stage;
	private int inputOption;
	private int numInput;
	
	public Customer(
			String id,
			String name,
			int age,
			Tour tour,
			double paid_amount) {			// Ryan Tang
		this.id = id;
		this.name = name;
		this.age = age;
		this.phoneNum = null;
		this.tour = tour;
		this.customerNo = new CustomerNo(-1, -1, -1);
		this.fee = new Fee(0, 0, 0);		// Ryan Tang
		this.paid_amount = paid_amount;		// Ryan Tang
		this.stage = 0;
		this.inputOption = -1;
		this.numInput = 0;
	}
	public void setId(String id) { this.id = id;}
	public String getId() { return this.id;}
	
	public void setName(String name) { this.name = name;}
	public String getName() { return this.name;}
	
	public void setAge(int age) { this.age = age;}
	public int getAge() { return this.age;}
	
	public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum;}
	public int getPhoneNum() { return this.phoneNum;}
	
	public void setTour(Tour tour) { this.tour = tour;}
	public Tour getTour() { return this.tour;}
	
	public void setCustomerNo(CustomerNo customerNo) { this.customerNo = customerNo;}
	public CustomerNo getCustomerNo() { return this.customerNo;}
	
	// Working from here to ...
	public void setFee(Fee fee) { this.fee = fee;}
	public Fee getFee() { return this.fee;}
	
	public void calculateFee() {
		String dates = this.tour.getDates();
		int price;
		int adult_num = this.customerNo.getAdultNo();
		int children_num = this.customerNo.getChildrenNo();

		int day = selectedBooking.dateToDay();
		switch (day) {
			case 1: case 2: case 3: case 4: case 5: { price = this.tour.getweekDayPrice(); break;}
			case 6: case 7: { price = this.tour.getweekEndPrice(); break;}
		}
		
		double adultPrice = adult_num * price;
		this.fee.setAdultFee(adultPrice);
		
		double childrenPrice = children_num * price;
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
	
	public int getStage() { return this.stage;}
	public void stageProceed() { this.stage++;}
	public void stageRestore() { this.stage--;}
	public void stageZero() {this.stage = 0; resetAll();}
	private void resetAll() {
		this.tour = null;
		this.customerNo = new CustomerNo(-1, -1, -1);
		this.fee = new Fee(0, 0, 0);		// Ryan Tang
		this.paid_amount = -1;		// Ryan Tang
		this.stage = 0;
		this.inputOption = -1;
		this.numInput = 0;
	}
	
	public int getInputOption() { return this.inputOption;}
	public void setInputOption(int inputOption) { this.inputOption = inputOption; numInput++;}
	public void resetInputOption() { this.inputOption = -1;}
	
	public int getNumInput() { return this.numInput;}
	public void resetNumInput() { this.numInput = 0;}
	
	public boolean inputFinished() {
		if (id != null && name != null && age != -1 && tour != null && 
				customerNo.inputDone())
			return true;
		return false;
	}

	public void update(){

	}
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
	
	public boolean inputDone() {
		if (adultNo != -1 && childrenNo != -1 && toodlerNo != -1)
			return true;
		return false;
	}
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
	
	public void setChildrenFee(double fee) { this.children_fee += fee*0.8;} // children has 20% discount, toodler is free
	public double getChildrenFee() { return children_fee;}
} 
// here (by Ryan Tang)
