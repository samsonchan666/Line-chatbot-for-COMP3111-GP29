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
    private List<String> bookingDate = null;
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
    
    List<String> listBookingDate(String text) throws Exception{
    	this.connection = this.getConnection();
    	if (text == null) throw new Exception("NOT FOUND");
    	bookingDate = new ArrayList<String>();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT *  FROM booking "
            );
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String tourid = rs.getString("tourId").toLowerCase();
                if (!(text.equals(tourid))) continue;
                bookingDate.add(rs.getString("dates"));
            }
            rs.close();
            stmt.close();
        }
        catch (Exception e){
            System.out.println("searchTour()" + e);
        }
        connection.close();
        return bookingDate;
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
//            PreparedStatement stmt = connection.prepareStatement(
//                    "SELECT *  FROM tour where STRPOS( LOWER(?), LOWER(name))>0  or STRPOS( LOWER(?), LOWER(attraction))>0"
//            );
//            stmt.setString(1, text);
//            stmt.setString(2, text);
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
            tourList = new ArrayList<Tour>() ;
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
            tourList = new ArrayList<Tour>() ;
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

    Tour getSelectedTour() { 
    	if (selectedTour == null) return null;
    	return selectedTour;
    }
    
    List<Tour> getTourList() {
    	if (tourList == null) return null;
    	return tourList;
    }
    
    void resetTourList() { tourList = null;}
}