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
    
    private boolean matchByAttraction(String attraction){
        Pattern p = Pattern.compile("\\w{3,}");
        Matcher m = p.matcher(attraction);
        while (m.find()){

            if (text.toLowerCase().matches("(.)*" + m.group() + "(.)*")) return true;
        }
        return false;
    }

    private boolean matchByName(String name){
        if (text.toLowerCase().matches("(.)*" + name + "(.)*")) return true;
        return false;
    }

    private boolean matchByID(String id){
        if (text.toLowerCase().matches("(.)*" + id + "(.)*")) return true;
        return false;
    }

    private boolean matchByDate(String dates){
        Pattern p = Pattern.compile("\\w+");
        Matcher m = p.matcher(dates);
        while (m.find()){
            if (text.toLowerCase().matches("(.)*" + m.group() + "(.)*|(.)*" + m.group() + "(.)*day")) return true;
        }
        return false;
    }
    
    private boolean matchByDuration(int duration) {
    	if (duration <= Integer.parseInt(preferenceInput.get(0))) return true;
    	return false;
    }
    
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
    
    private boolean matchByWeekDayOnly(String tourID, int day) {
    	if (!weekDayOnlyTourIDList.isEmpty()) {
    		for (int i = 0; i < weekDayOnlyTourIDList.size(); i++)
    			if (tourID.equals(weekDayOnlyTourIDList.get(i)))
    				if (day == 1 || day == 7) //equal weekEND
    					return false;
    	}
    	return true;
    }
    
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

    private boolean matchBySort(){
        if (text.toLowerCase().matches("(.)*sort(.)*")) return true;
        return false;
    }

    private boolean matchByPrice(){
        if (text.toLowerCase().matches("(.)*price(.)*")) return true;
        return false;
    }


    public void setSelectedTour(Tour tour){
        this.selectedTour = tour;
    }

    public Tour getSelectedTour() {
    	if (selectedTour == null) return null;
    	return selectedTour;
    }
    
    public List<Tour> getTourList() {
    	if (tourList == null) return null;
    	return tourList;
    }    
    public void resetTourList() { tourList = null;}
    
    public List<String> getTourIDList() {
    	if (tourIDList == null) return null;
    	return tourIDList;
    }    
    public void resetTourIDList() { tourIDList = null;}
    
    public void resetWeekDayOnlyTourIDList() { this.weekDayOnlyTourIDList = new ArrayList<String>();}
    
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
    
    public List<String> getBookingDateList() {
        if (bookingDateList == null || bookingDateList.isEmpty()) return null;
        return bookingDateList;
    }
    
    public void setSelectedBookingText(String text) { this.selectedBookingText = text;}
    public void setSelectedBooking() {
    	for (int i = 0; i < bookingList.size(); i++)
    		if (selectedBookingText.toLowerCase().matches("(.)*pick " + bookingList.get(i).dateToString().toLowerCase() + "(.)*"))
    			selectedBooking = bookingList.get(i);
    }


    public void setSelectedBooking(Booking booking){
        this.selectedBooking = booking;
    }

    public Booking getSelectedBooking() { return this.selectedBooking;}

    
    public void addPreferenceInput(String input) { this.preferenceInput.add(input);}
    public List<String> getPreferenceInput() { return this.preferenceInput;}
    public void resetPreferenceInput() { this.preferenceInput = new ArrayList<String>();}
    
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