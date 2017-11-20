package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.net.URISyntaxException;
import java.net.URI;
import java.lang.*;
import java.util.regex.*;
import java.util.Calendar;

/**
 * <h1>A SQLDatabaseEngine search for any booking and tour by their properties</h1>
 */
@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
    private Connection connection;
    String text = null;
    private Tour selectedTour = null;
    private List<Tour> tourList = null;
    private List<String> tourIDList = null;
    private List<String> weekDayOnlyTourIDList = new ArrayList<String>();
    
    private String selectedBookingText = null;
    private Booking selectedBooking = null;
    private List<Booking> bookingList = null;
    private List<String> bookingDateList = null;
    private List<String> preferenceInput = new ArrayList<String>();
    
    /**
     * Returns the return value of searchTour, searchTourByDate, 
     * searchTourByAttraction, and searchFAQ if they returned anything. 
     * (Details in their resprctive documentations)
     * If all returned null, the method throws an exception.
     * 
     * This method returns immediately if searchTour, searchTourByDate, 
     * searchTourByAttraction, or searchFAQ has a return value.
     * 
     * @param text	the text input by the user
     * @return		the answer to the text input
     * @throws Exception throw for postgresql exception
     */
    @Override
    String search(String text) throws Exception {
        //Write your code here
        String result = null;
        this.text = text;
        this.connection = this.getConnection();

        result = searchTour();
        if (result != null){
            connection.close();
            return result;
        }
        result = searchTourByDate();
        if (result != null){
            connection.close();
            return result;
        }

        result = searchFAQ(); // Search FAQ
        if (result != null) {
            connection.close();
            return result;
        }
        result = searchTourByAttraction();
        if (result != null){
            connection.close();
            return result;
        }
        connection.close();
        throw new Exception("NOT FOUND");
    }      
    
    /**
	 * Returns a Connection to the SQL database.
	 * 
	 * This method throws an exception when the URI syntax 
	 * is false or when the SQL database does not exist.
	 * 
	 * @return						Connection to the SQL database
	 * @throws URISyntaxException	if URI syntax isn't as expected
	 * @throws SQLException			if SQL database isn't as expected
	 */
    private Connection getConnection() throws URISyntaxException, SQLException {
        Connection connection;
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

        log.info("Username: {} Password: {}", username, password);
        log.info ("dbUrl: {}", dbUrl);

        connection = DriverManager.getConnection(dbUrl, username, password);

        return connection;
    }  
/*
    private String searchRes() throws Exception{
        String result = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT keyword, response FROM restable where STRPOS(LOWER(?),LOWER(keyword))>0"
            );
            stmt.setString(1, text);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getString(2);
            }
            rs.close();
            stmt.close();

        }
        catch (Exception e){
            System.out.println("searchRes()" + e);
        }
        return result;
    }    
 */
    
    /**
     * Returns the information of the tour that matches the name or id specified in text.
     * 
     * Fetches all names and ids from table 'tour' from the database.
     * Checks if the tour names or ids appeared in the text,
     * if yes, 
     * fetch the information of said tour and returns the tour information.
     * Or return null if no tours match.
     * 
     * @return				the information of the tour in a string, or null
     * @throws Exception		throw for postgresql exception
     */
    private String searchTour() throws Exception{
        String result = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT *  FROM tour "
            );
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name").toLowerCase();
                String id = rs.getString("id").toLowerCase();
                if ( !(matchByName(name) || matchByID(id)) ) continue;
                selectedTour = new Tour(rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("attraction"),
                        rs.getInt("duration"),
                        rs.getInt("weekDayPrice"),
                        rs.getInt("weekEndPrice"),
                        rs.getString("dates")
                );

                StringBuilder str = selectedTour.getDetailTourInfo();

                result = str.toString();
            }
            rs.close();
            stmt.close();
        }
        catch (Exception e){
            System.out.println("searchTour()" + e);
        }
        return result;
    }
    
    /**
     * Returns the information of the tours that matches the date specified in text.
     * 
	 * Fetches all dates from table 'tour' from the database.
	 * Checks if the tour dates appeared in the text,
	 * if yes,
	 * add the tour into two arrays, one storing the name and one the id.
	 * Returns the tour information of the tours added to the array.
	 * 
	 * @return				Tour information of tours with the appropiate date
	 * @throws Exception		throw for postgresql exception
	 */
    private String searchTourByDate() throws Exception{
        String result = null;
        StringBuilder str = new StringBuilder();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT *  FROM tour "
            );
            ResultSet rs = stmt.executeQuery();
            tourList = new ArrayList<Tour>();
            tourIDList = new ArrayList<String>();
            boolean hasResult = false;
            while(rs.next()) {
                String dates = rs.getString("dates").toLowerCase();
                if (!(matchByDate(dates))) continue;
                Tour tour = new Tour(rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("attraction"),
                        rs.getInt("duration"),
                        rs.getInt("weekDayPrice"),
                        rs.getInt("weekEndPrice"),
                        rs.getString("dates")
                );
                tourList.add(tour);
                tourIDList.add(tour.getID());
                hasResult = true;
            }
            if (hasResult) {
                if (matchBySort() && matchByPrice()){
                    result = Tour.getBasicTourInfoSortByPrice(tourList, Tour.Keyword.DATE).toString();
                }
                else {
                    result = Tour.getBasicTourInfoByKeyword(tourList, Tour.Keyword.DATE).toString();
                }

            }
            rs.close();
            stmt.close();
        }
        catch (Exception e){
            System.out.println("searchTourByDate()" + e);
        }
        return result;
    }

    /**
     * Returns the answer to the question asked by client via keywords.
     * 
     * Fetches all keywords from table 'faq'.
     * If the keyword matches text, returns the answer.
     * 
     * @return				Answer to the question specified via keywords.
     * @throws Exception		throw for postgresql exception.
     */
    private String searchFAQ() throws Exception{
        String result = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT *  FROM faq "
            );
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String keywords = rs.getString("keywords").toLowerCase();
                if (!(matchByKeywords(keywords))) continue;
                result = rs.getString("respond");
            }
            rs.close();
            stmt.close();
        }
        catch (Exception e){
            System.out.println("searchTour()" + e);
        }
        return result;
    }

    /**
     * Returns the information of the tour that matches the attractions specified in text.
     * 
     * Fetches all attractions from table 'tour' from the database.
	 * Checks if the tour attractions appeared in the text,
	 * if yes,
	 * add the tour into two arrays, one storing the name and one the id.
	 * Returns the tour information of the tours added to the array.
	 * 
	 * @return				tour information of tours with the appropiate attraction.
     * @throws Exception		throw for postgresql exception.
     */
    private String searchTourByAttraction() throws Exception{
        String result = null;
        StringBuilder str = new StringBuilder();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT *  FROM tour "
            );
            ResultSet rs = stmt.executeQuery();
            tourList = new ArrayList<Tour>();
            tourIDList = new ArrayList<String>();
            boolean hasResult = false;
            while(rs.next()) {
                String attraction = rs.getString("attraction").toLowerCase();
                if (!(matchByAttraction(attraction))) continue;
                Tour tour = new Tour(rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("attraction"),
                        rs.getInt("duration"),
                        rs.getInt("weekDayPrice"),
                        rs.getInt("weekEndPrice"),
                        rs.getString("dates")
                );
                tourList.add(tour);
                tourIDList.add(tour.getID());
                hasResult = true;
            }
            if (hasResult) {
                if (matchBySort() && matchByPrice()){
                    result = Tour.getBasicTourInfoSortByPrice(tourList, Tour.Keyword.ATTRACTION).toString();
                }
                else {
                    result = Tour.getBasicTourInfoByKeyword(tourList, Tour.Keyword.ATTRACTION).toString();
                }

            }
            rs.close();
            stmt.close();
        }
        catch (Exception e){
            System.out.println("searchTourByAttraction()" + e);
        }
        return result;
    }

    /**
     * Returns a boolean indicating if attraction appread in text.
     * 
     * @param attraction		intended attraction of customer
     * @return				a boolean indicating if attraction appeared in text
     */
    private boolean matchByAttraction(String attraction){
        Pattern p = Pattern.compile("\\w{3,}");
        Matcher m = p.matcher(attraction);
        while (m.find()){

            if (text.toLowerCase().matches("(.)*" + m.group() + "(.)*")) return true;
        }
        return false;
    }
    
    /**
	 * Returns a boolean indicating if the tour name appeared in the text or not.
	 * Uses Regex.
	 * 
	 * @param name	name of tour
	 * @return		if the tour name appeared in the text or not
	 */
    private boolean matchByName(String name){
        if (text.toLowerCase().matches("(.)*" + name + "(.)*")) return true;
        return false;
    }

    /**
	 * Returns a boolean indicating if the tour id appeared in the text or not.
	 * Uses Regex.
	 * 
	 * @param id		id of tour
	 * @return		if the tour id appeared in the text or not.
	 */
    private boolean matchByID(String id){
        if (text.toLowerCase().matches("(.)*" + id + "(.)*")) return true;
        return false;
    }

    /**
	 * Returns a boolean indicating if the tour date appeared in the text or not.
	 * uses Regex.
	 * 
	 * @param dates	date of tour
	 * @return		if the tour date appeared in the text or not.
	 */
    private boolean matchByDate(String dates){
        Pattern p = Pattern.compile("\\w+");
        Matcher m = p.matcher(dates);
        while (m.find()){
            if (text.toLowerCase().matches("(.)*" + m.group() + "(.)*|(.)*" + m.group() + "(.)*day")) return true;
        }
        return false;
    }
    
    /**
     * Returns a boolean indicating if the tour meets the wanted duration.
     * 
     * @param duration	wanted duration
     * @return			a boolean indicating if the tour meets the wanted duration
     */
    private boolean matchByDuration(int duration) {
    	if (duration <= Integer.parseInt(preferenceInput.get(0))) return true;
    	return false;
    }
    
    /**
     * Returns a boolean indicating if the tour meets the wanted budget.
     * 
     * @param tourID			the ID of the tour testing for budget
     * @param weekDayPrice	budget for weekday
     * @param weekEndPrice	budget for weekend
     * @return				a boolean indicating if the tour meets the wanted budget
     */
    private boolean matchByBudget(String tourID, int weekDayPrice, int weekEndPrice) {
    	int budget = Integer.parseInt(preferenceInput.get(2));
    	if (weekEndPrice <= budget) 
    		return true;
    	else if (weekDayPrice <= budget) {
    		weekDayOnlyTourIDList.add(tourID.toLowerCase());
    		return true;
    	}    		
    	return false;
    }

    /**
     * Returns a boolean indicating if the tour is weekday only and if the day parameter is a weekday too.
     * 
     * @param tourID		the ID of the tour testing for day
     * @param day		the day on a week of the date testing
     * @return			a boolean indicating if the tour is weekday only and if the day parameter is a weekday too
     */
    private boolean matchByWeekDayOnly(String tourID, int day) {
    	if (!weekDayOnlyTourIDList.isEmpty()) {
    		for (int i = 0; i < weekDayOnlyTourIDList.size(); i++)
    			if (tourID.equals(weekDayOnlyTourIDList.get(i)))
    				if (day == 1 || day == 7) //equal weekEND
    					return false;
    	}
    	return true;
    }
    
    /**
     * Returns a boolean checking if the keyword input by client is in the keyword list in the database.
     * @param keywords	input keywords by client
     * @return			a boolean checking if the keyword input by client is in the keyword list in the database
     */
    public boolean matchByKeywords(String keywords){
        Pattern p = Pattern.compile("\\w+");
        Matcher m_keywords = p.matcher(keywords.toLowerCase());
        Matcher m_text = p.matcher(text.toLowerCase());
        List<String> keyword_list = new ArrayList<String>();
        List<String> text_list = new ArrayList<String>();
        while (m_keywords.find()) keyword_list.add(m_keywords.group());
        while (m_text.find()) text_list.add(m_text.group());
        boolean match = false;
        for (String keyword : keyword_list){
            if (text_list.contains(keyword)) continue;
            return false;
        }
        return true;
    }

    /**
	 * Returns a boolean indicating if the String "sort" appeared in the text.
	 * @return		if the String "sort" appeared in the text.
	 */
    private boolean matchBySort(){
        if (text.toLowerCase().matches("(.)*sort(.)*")) return true;
        return false;
    }

    /**
	 * Returns a boolean indicating if the String "price" appeared in the text.
	 * @return		if the String "price" appeared in the text.
	 */
    private boolean matchByPrice(){
        if (text.toLowerCase().matches("(.)*price(.)*")) return true;
        return false;
    }

    /**
     * Sets this.selectedTour to be the parameter tour.
     * @param tour	a tour wanting to be selected
     */
    public void setSelectedTour(Tour tour){
        this.selectedTour = tour;
    }

    /**
     * Returns the selected tour of this object if not null.
     * 
     * @return		the selected tour of this object
     */
    public Tour getSelectedTour() {
    	if (selectedTour == null) return null;
    	return selectedTour;
    }
    
    /**
     * Returns the list of tours inside the array 'tourList' if not null.
     * 
     * @return	the list of tours
     */
    public List<Tour> getTourList() {
    	if (tourList == null) return null;
    	return tourList;
    }

    public void resetTourList() { tourList = null;}
    
    /**
     * Returns the list of IDs of tours in 'tourIDList' if not null.
     * 
     * @return	the list of IDs of tours in 'tourIDList'
     */
    public List<String> getTourIDList() {
    	if (tourIDList == null) return null;
    	return tourIDList;
    }    
   
    /** 
     * Resets tourIDList by setting it to null.
     */
    public void resetTourIDList() { tourIDList = null;}
    
    /**
     * Resets weekDayOnlyTourIDList by pointing it to a new array.
     */
    public void resetWeekDayOnlyTourIDList() { this.weekDayOnlyTourIDList = new ArrayList<String>();}
    
    /**
     * Creates two lists, one containing the booked tours, 
     * another containing the dates of the bookings.
     * 
     * Fetch all tourIDs from the table 'booking' in the datebase.
     * If text input is exactly the same as the tourID, 
     * create a now booking object with the information store in the database.
     * Add the booking object into bookingList, and the date to bookingDateList.
     * 
     * @throws Exception 	throw for postgresql exception
     */
    public void createBookingDateList() throws Exception{
    	if (selectedTour == null) return;
    	String text = selectedTour.getID().toLowerCase();
    	this.connection = this.getConnection();
    	bookingList = new ArrayList<Booking>();
    	bookingDateList = new ArrayList<String>();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT *  FROM booking "
            );
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String tourID = rs.getString("tourId").toLowerCase();
                if (!(text.equals(tourID))) continue;
                Booking booking = new Booking(
                		rs.getString("id"), 
                		selectedTour, 
                		null, 
                		rs.getString("hotel"), 
                		rs.getInt("capacity"), 
                		rs.getInt("miniCustomer"), 
                		rs.getInt("currentCustomer")                		
                );
                booking.setDateString(rs.getString("dates"));
                booking.getTourGuide().setName(rs.getString("tourGuide"));
                booking.getTourGuide().setLineAcc(rs.getString("lineAcc"));
                if (!matchByWeekDayOnly(tourID, booking.dateToDay())) continue;
                bookingList.add(booking);
                bookingDateList.add(rs.getString("dates"));
            }
            rs.close();
            stmt.close();
        }
        catch (Exception e){
            System.out.println("searchTour()" + e);
        }
        connection.close();
    }    
    
    /**
     * Returns the bookingDateList if not empty or null.
     * @return	bookingDateList
     */
    public List<String> getBookingDateList() {
        if (bookingDateList == null || bookingDateList.isEmpty()) return null;
        return bookingDateList;
    }
    
    /**
     * Sets the text input as the selectedBookingText
     * @param text	text input
     */
    public void setSelectedBookingText(String text) { this.selectedBookingText = text;}
    
    /**
     * Initializes selectedBooking by checking with the text input.
     * 
     * If selectedBookingText matches "pick 'certain date'", 
     * set the object selectedBooking from objects from bookingList which satisfies the certain date.
     */
    public void setSelectedBooking() {
    	for (int i = 0; i < bookingList.size(); i++)
    		if (selectedBookingText.toLowerCase().matches("(.)*pick " + bookingList.get(i).dateToString().toLowerCase() + "(.)*"))
    			selectedBooking = bookingList.get(i);
    }

    /**
     * Initializes selectedBooking according to the booking parameter.
     * 
     * Sets selectedBooking to be the booking parameter.
     * 
     * @param booking		intended item to be set as selectedBooking
     */
    public void setSelectedBooking(Booking booking){
        this.selectedBooking = booking;
    }

    /**
     * Returns object 'selectedBooking'.
     * @return	selectedBooking
     */
    public Booking getSelectedBooking() { return this.selectedBooking;}

    /**
     * Adds preferenceInput according to input parameter.
     * @param input		item intended to be set as preferenceInput
     */
    public void addPreferenceInput(String input) { this.preferenceInput.add(input);}
    /**
     * Returns object 'preferenceInput'
     * @return	preperenceInput
     */
    public List<String> getPreferenceInput() { return this.preferenceInput;}
    /**
     * Resets the object 'preferenceInput' by making it a new empty array.
     */
    public void resetPreferenceInput() { this.preferenceInput = new ArrayList<String>();}
    
    /**
     * Returns a tour that matches the input duration, attraction, and budget.
     * 
     * Fetches all items in the columns 'duration', 'attraction', 'id', 'weekDayPrice', and 'weekEndPrice'
     * from table 'tour' of the database.
     * If there is a tour that matches the input duration, attraction and budget, 
     * add it into tourList and its ID into tourIDList
     * and return it.
     * Else return 'not found'
     * 
     * @return				the tour that matches all criteria input
     * @throws Exception		throw for postgresql exception
     */
    public String filterPreference() throws Exception{
        String result = null;
        this.connection = this.getConnection();
        StringBuilder str = new StringBuilder();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT *  FROM tour "
            );
            ResultSet rs = stmt.executeQuery();
            tourList = new ArrayList<Tour>();
            tourIDList = new ArrayList<String>();
            boolean hasResult = false;
            while(rs.next()) {
                int duration = rs.getInt("duration");
                this.text = preferenceInput.get(1);//interest                
                String attraction = rs.getString("attraction").toLowerCase();
                String tourID = rs.getString("id");
                int weekDayPrice = rs.getInt("weekDayPrice");
                int weekEndPrice = rs.getInt("weekEndPrice");
                if (!(matchByDuration(duration) && 
                		matchByAttraction(attraction) && 
                			matchByBudget(tourID, weekDayPrice, weekEndPrice))) continue;
                Tour tour = new Tour(tourID,
                        rs.getString("name"),
                        rs.getString("attraction"),
                        rs.getInt("duration"),
                        rs.getInt("weekDayPrice"),
                        rs.getInt("weekEndPrice"),
                        rs.getString("dates")
                );
                tourList.add(tour);
                tourIDList.add(tour.getID());
                hasResult = true;
            }
            if (hasResult)
            	result = Tour.getBasicTourInfoSortByPrice(tourList, Tour.Keyword.PREFERENCE).toString();
            rs.close();
            stmt.close();
        }
        catch (Exception e){
            System.out.println("PreferenceList()" + e);
        }
        if (result != null) {
            connection.close();
            return result;
        }
        connection.close();
        throw new Exception("NOT FOUND");
    }

    /**
     * Adds a new customer to the database.
     * 
     * Sets the columns of a new entry in the database according to the customer's
     * Name
     * ID
     * Phone number
     * Age
     * ID of booked tours
     * Number of adult-revservations 
     * Number of adult-revservations 
     * Number of toddler-reservations
     * Total fee the customer needs to pay.
     * 
     * Also set the tenth column to be 0.
     * 
     * @param customer		intended customer to be added into database.
     * @throws Exception throw for postgresql exception
     */
    public void saveCustomerToDb(Customer customer) throws Exception{
        this.connection = this.getConnection();
        String sqlInsert = "insert into customer values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setString(1, customer.getName());
        preparedStatement.setString(2, customer.getId());
        preparedStatement.setInt(3, Integer.parseInt(customer.getPhoneNum()));
        preparedStatement.setInt(4, customer.getAge());
        preparedStatement.setString(5, selectedBooking.getID());
        preparedStatement.setInt(6, customer.getCustomerNo().getAdultNo());
        preparedStatement.setInt(7, customer.getCustomerNo().getChildrenNo());
        preparedStatement.setInt(8, customer.getCustomerNo().getToodlerNo());
        preparedStatement.setDouble(9, customer.getFee().getTotalFee());
        preparedStatement.setDouble(10, 0);

        preparedStatement.executeUpdate();
        preparedStatement.close();

        connection.close();
    }

    /**
     * 
     * @param bookingId id for the booking
     * @throws Exception throw for postgresql exception
     */
    public void updateDiscountTour(String bookingId) throws Exception{
        this.connection = this.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "update discount set number = number - 1 where bookingId = (?) "
        );
        stmt.setString(1,bookingId);
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

    /**
     * Search for a discount tour if any and available to customer
     * @param tour a dummy Tour object to save the tour id.
     * @param  booking a dummy Booking object to save the booking id.
     * @return true if a discount tour a available to customer
     * @throws Exception throw for postgresql exception
     */
    public boolean searchDiscountTour(Tour tour, Booking booking) throws Exception{

        Calendar current = Calendar.getInstance();
        this.connection = this.getConnection();

        String strSelect = "select * from discount";
        Statement stmt = connection.createStatement();
        ResultSet rset = stmt.executeQuery(strSelect);
        while(rset.next()) {   // Move the cursor to the next row
            Calendar discountDate = Calendar.getInstance();
            //No more discount, continue
            if(rset.getInt("number") <=0) continue;
            String date = rset.getString("discountDate");
            String time = rset.getString("discountTime");
            String[] splitDate = date.split("/");
            if (time.length() < 4) continue;

            //year, month, day, hour, min
            discountDate.set(Integer.parseInt(splitDate[2]),
                    Integer.parseInt(splitDate[1]) - 1,
                    Integer.parseInt(splitDate[0]),
                    Integer.parseInt(time.substring(0,2))-1,
                    Integer.parseInt(time.substring(2,4))
            );
//           Continue if the discount date and time not reach
            if (discountDate.compareTo(current)>0) continue;
            tour.setID(rset.getString("tourId"));
            booking.setID(rset.getString("bookingId"));
            return true;
        }
        rset.close();
        return false;
    }

    /**
     * Returns the Tour with the specified tourID
     * 
     * Fetch all ids from table 'tour' in database.
     * If id matches with input id, 
     * fetch all other information and make a new Tour object to return.
     * 
     * @param tourId			intended tourID of returned tour
     * @return				tour of matching tourID with tourId
     * @throws Exception		throw for postgresql exception
     */
    public Tour searchTourByID(String tourId) throws Exception{
        Connection con = this.getConnection();
        Tour discount_tour = null;
        try {
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT *  FROM tour "
            );
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (!(tourId.toLowerCase().equals(rs.getString("id").toLowerCase()))) continue;
                discount_tour = new Tour(rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("attraction"),
                        rs.getInt("duration"),
                        rs.getInt("weekDayPrice"),
                        rs.getInt("weekEndPrice"),
                        rs.getString("dates")
                );
            }
            rs.close();
            stmt.close();
        }
        catch (Exception e){
            System.out.println("searchTourByID()" + e);
        }
        return discount_tour;
    }

    /**
     * Returns the tour among all bookings that have the input id.
     * 
     * Fetches all entries in booking with id bookingID.
     * Creates a new Booking object to be returned using information from the database.
     * 
     * @param bookingID		intended bookingID
     * @return				tour in booking with id bookingID
     * @throws Exception		any error
     */
    public Booking searchBookingByID(String bookingID) throws Exception{
        Connection connection = this.getConnection();
        Booking booking = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT *  FROM booking where id = ?"
            );
            stmt.setString(1,bookingID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                booking = new Booking(
                        rs.getString("id"),
                        null,
                        null,
                        rs.getString("hotel"),
                        rs.getInt("capacity"),
                        rs.getInt("miniCustomer"),
                        rs.getInt("currentCustomer")
                );
                booking.setDateString(rs.getString("dates"));
            }
            rs.close();
            stmt.close();
        }
        catch (Exception e){
            System.out.println("searchBookingByID()" + e);
        }
        return booking;
    }
}