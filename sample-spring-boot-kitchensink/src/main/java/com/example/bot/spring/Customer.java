package com.example.bot.spring;

/**
 * <h1>Customer details</h1>
 * The Customer class store the information
 * of Customers
 *
 */

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

	private boolean showDiscount;

	private int preferenceNum;
	private boolean preferenceFinished;

	/**
	 * This is a Customer class which store
	 * the information of the booking customer
	 * @param id Customer's id
	 * @param name Customer's name
	 * @param age Customer's age
	 * @param tour Customer's tour
	 * @param paid_amount Customer's paid amount
	 */
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
		this.showDiscount = true;
		this.preferenceNum = -1;
		this.preferenceFinished = false;

	}
	
	/**
	 * This methode set the id of 
	 * the customer to the input String
	 * @param id Customer's id
	 */
	public void setId(String id) { this.id = id;}
	/**
	 * This method return the id of
	 * the customer, which is a String
	 * @return Customer's id
	 */
	public String getId() { return this.id;}
	
	
	/**
	 * This methode set the name of 
	 * the customer to the input String
	 * @param name Customer's name
	 */
	public void setName(String name) { this.name = name;}
	/**
	 * This method return the name of
	 * the customer, which is a String
	 * @return Customer's name
	 */
	public String getName() { return this.name;}
	
	
	/**
	 * This methode set the age of 
	 * the customer to the input integer
	 * @param age Customer's age
	 */
	public void setAge(int age) { this.age = age;}
	/**
	 * This method return the age of
	 * the customer, which is a integer
	 * @return Customer's age
	 */
	public int getAge() { return this.age;}
	
	
	/**
	 * This methode set the phone number of 
	 * the customer to the input String
	 * @param phoneNum Customer's phone number
	 */
	public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum;}
	/**
	 * This method return the phone number 
	 * of the customer, which is a String
	 * @return Customer's phone number
	 */
	public String getPhoneNum() { return this.phoneNum;}
	
	
	/**
	 * This methode set the joined tour of 
	 * the customer to the input tour objects
	 * @param tour tour joined
	 */
	public void setTour(Tour tour) { this.tour = tour;}
	/**
	 * This method return the tour the
	 * customer joined, which is a tour class
	 * @return  tour joined
	 */
	public Tour getTour() { return this.tour;}
	
	
	/**
	 * This methode set the total number of 
	 * the customers joined to the input 
	 * CustomerNo object 
	 * @param customerNo no of customer joined
	 */
	public void setCustomerNo(CustomerNo customerNo) { this.customerNo = customerNo;}
	/**
	 * This method return the number of
	 * customers joined, which is a CustomerNo object
	 * @return customerNo
	 */
	public CustomerNo getCustomerNo() { return this.customerNo;}
	
	
	// Working from here to ...
	/**
	 * This methode set the fee needed for 
	 * the customer to the input Fee object
	 * @param fee Fee that need to pad
	 */
	public void setFee(Fee fee) { this.fee = fee;}
	/**
	 * This method return the fee needed for
	 * the customer, which is a Fee object
	 * @return Fee that need to pad
	 */
	public Fee getFee() { return this.fee;}
	
	/**
	 * This method calculate the fee 
	 * the customer have to pay according
	 * to the booking they made and
	 * the number of children and adult
	 * @param selectedBooking the booking selected
	 */
	public void calculateFee(Booking selectedBooking) {
		String dates = this.tour.getDates();
		int price = 0;
		int adult_num = this.customerNo.getAdultNo();
		int children_num = this.customerNo.getChildrenNo();

		int day = selectedBooking.dateToDay();
		switch (day) {
			case 2: case 3: case 4: case 5: case 6: { price = this.tour.getweekDayPrice(); break;}
			case 1: case 7: { price = this.tour.getweekEndPrice(); break;}
		}
		
		double adultPrice = adult_num * price;
		this.fee.setAdultFee(adultPrice);
		
		double childrenPrice = children_num * price * 0.8;
		this.fee.setChildrenFee(childrenPrice);
		
		this.fee.setTotalFee();
	}

	public void calculateDiscountFee(Booking selectedBooking) {
		String dates = this.tour.getDates();
		int price = 0;
		int adult_num = this.customerNo.getAdultNo();
		int children_num = this.customerNo.getChildrenNo();

		int day = selectedBooking.dateToDay();
		switch (day) {
			case 2: case 3: case 4: case 5: case 6: { price = this.tour.getweekDayPrice(); break;}
			case 1: case 7: { price = this.tour.getweekEndPrice(); break;}
		}

		double adultPrice = adult_num * price /2;
		this.fee.setAdultFee(adultPrice);

		double childrenPrice = children_num * price * 0.8 /2;
		this.fee.setChildrenFee(childrenPrice);

		this.fee.setTotalFee();
	}
	
	/**
	 * This method make payment record and 
	 * return the exceeding amount 
	 * in double value. If the booking
	 * are all paid, return 0
	 * @param amount amount paying
	 * @return exceeding amount
	 */
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
	
	/**
	 * This method return the amount
	 * paid by the customer
	 * @return paid amount
	 */
	public double getPayAmount() { return paid_amount;}
	
	/**
	 * This method return a boolean 
	 * value indicate whether the customer
	 * has unpaid amount.
	 * True: unpaid amount exist
	 * False: no unpaid amount
	 * @return haveRemainPayment
	 */
	public boolean haveRemainPayment()  { return (paid_amount < fee.getTotalFee());}
	// here (by Ryan Tang)

	
	/**
	 * This methode set the stage the
	 * customer proceeded to to the input integer
	 * @param stage stage of customer in booking process
	 */
	public void setStage(int stage) { this.stage = stage;}
	
	/**
	 * This method return the stage
	 * the customer has proceeded to,
	 * which is a integer 
	 * @return current customer stage
	 */
	public int getStage() { return this.stage;}
	
	/**
	 * This method proceed the stage of
	 * the customer to the next stage
	 */
	public void stageProceed() { this.stage++;}
	
	/**
	 * This method restore the stage of
	 *  the customer to the previous stage
	 */
	public void stageRestore() { this.stage--;}
	
	/**
	 * This method reset the stage of 
	 * the customer to stage 0
	 */
	public void stageZero() {this.stage = 0; resetAll();}
	
	/**
	 * This method reset all the customer data
	 */
	private void resetAll() {
		this.tour = null;
		this.customerNo = new CustomerNo(-1, -1, -1);
		this.fee = new Fee(0, 0, 0);
		this.paid_amount = -1;
		this.stage = 0;
		this.inputOption = -1;
		this.numInput = 0;
		this.preferenceNum = -1;
		this.preferenceFinished = false;
	}
	
	public int getInputOption() { return this.inputOption;}
	public void setInputOption(int inputOption) { this.inputOption = inputOption; numInput++;}
	public void resetInputOption() { this.inputOption = -1;}
	
	public int getNumInput() { return this.numInput;}
	public void resetNumInput() { this.numInput = 0;}

	public void setShowDiscount(boolean showDiscount) {this.showDiscount = showDiscount;}
	public boolean getShowDiscount(){ return this.showDiscount;}
	
	public int getPreferenceNum() { return this.preferenceNum;}
	public void preferenceNumIncre() { this.preferenceNum++;}
	public void resetPreferenceNum() { this.preferenceNum = -1;}
	
	public boolean isPreferenceFinished() { return this.preferenceFinished;}
	public void setPreferenceFinished(boolean state) { this.preferenceFinished = state;}
	public void resetPreferenceFinished() { this.preferenceFinished = false;}

	public boolean inputFinished() {
		if (id != null && name != null && age >= 0 && phoneNum != null && tour != null && 
				customerNo.inputDone())
			return true;
		return false;
	}

	public void update(){

	}
}

/**
 * This is a CustomerNo class
 * which store the number of customer of
 * different categories joining the tour
 * Categories include adult, 
 *children and toodler
 */
class CustomerNo{
	private int adultNo;
	private int childrenNo;
	private int toodlerNo;
	
	/**
	 * This is the constructor of the 
	 * CustomerNo class with all the 
	 * numbers initialised when constructed
	 * @param adultNo no of adult
	 * @param childrenNo no of children
	 * @param toodlerNo no of toodler
	 */
	public CustomerNo(
			int adultNo,
			int childrenNo,
			int toodlerNo ) {
		this.adultNo = adultNo;
		this.childrenNo = childrenNo;
		this.toodlerNo = toodlerNo;
	}
	
	/**
	 * This methode set the number of
	 * adults to the input integer
	 * @param adultNo no of adult
	 */
	public void setAdultNo(int adultNo) { this.adultNo = adultNo;}
	
	/**
	 * This method return the total 
	 * number of adult as integer
	 * @return number of adult
	 */
	public int getAdultNo() { return this.adultNo;}
	
	
	/**
	 * This methode set the number of
	 * children to the input integer
	 * @param childrenNo number of children
	 */
	public void setChildrenNo(int childrenNo) { this.childrenNo = childrenNo;}
	
	/**
	 * This method return the total 
	 * number of children as integer
	 * @return number of children
	 */
	public int getChildrenNo() { return this.childrenNo;}
	
	
	/**
	 * This methode set the number of
	 * toodler to the input integer
	 * @param toodlerNo number of toodler
	 */
	public void setToodlerNo(int toodlerNo) { this.toodlerNo = toodlerNo;}
	
	/**
	 * This method return the total 
	 * number of toodler as integer
	 * @return number of toodler
	 */
	public int getToodlerNo() { return this.toodlerNo;}

	/**
	 * This method return the total number
	 * of customers of all categories
	 * joining the tour
	 * @return total number of customers
	 */
	public int getTotalNo() { return this.adultNo + this.childrenNo + this.toodlerNo;}
	
	/**
	 * This method return a boolean value
	 * which indicate whether the input
	 * of number of customers has been done
	 * True: input done
	 * False: input not done
	 * @return boolean of whether input is done
	 */
	public boolean inputDone() {
		if (adultNo >= 0 && childrenNo >= 0 && toodlerNo >= 0)
			return true;
		return false;
	}
}

// Working form here to ...
/**
 * This is a Fee class which stores
 * the calculated fee of children, of adult
 * and the total fee of a customer
 */
class Fee{
	private double total_fee;
	private double adult_fee;
	private double children_fee;
	
	/**
	 * This is constructor of the Fee
	 * class with all the value initialised
	 * when constructed
	 * @param total_fee total fee
	 * @param adult_fee adult fee
	 * @param children_fee children fee
	 */
	public Fee(
			double total_fee,
			double adult_fee,
			double children_fee) {
		this.total_fee = total_fee;
		this.adult_fee = adult_fee;
		this.children_fee = children_fee;
	}
	
	/**
	 * This is the method which calculate
	 * the total fee a customer has to pay
	 */
	public void setTotalFee() { this.total_fee = getAdultFee()+getChildrenFee();}
	
	/**
	 * This is the method that return the 
	 * total fee a customer has to pay
	 * @return total fee a customer has to pay
	 */
	public double getTotalFee()  {return total_fee;}
	
	/**
	 * This methode set the fee of 
	 * the adults to the input double value
	 * @param fee adult fee
	 */
	public void setAdultFee(double fee) { this.adult_fee = fee;} // adult has no discount
	
	/**
	 * This method return the fee
	 * of the adults, which is a 
	 * double value
	 * @return adult_fee
	 */
	public double getAdultFee() { return adult_fee;}
	
	
	/**
	 * This method set the fee of
	 * the children to the input double value
	 * @param fee  fee of  the children
	 */
	public void setChildrenFee(double fee) { this.children_fee = fee;} // children has 20% discount, toodler is free
	
	/**
	 * This method return the fee
	 * of the children, which is a 
	 * double value
	 * @return  the fee of the children
	 */
	public double getChildrenFee() { return children_fee;}
} 
// here (by Ryan Tang)
