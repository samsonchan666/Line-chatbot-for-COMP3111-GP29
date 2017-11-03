package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.net.URISyntaxException;
import java.net.URI;
import java.lang.*;
import java.util.regex.*;


@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
    private Connection connection;
    String text = null;

    @Override
    String search(String text) throws Exception {
        //Write your code here
        String result = null;
        this.text = text;
        this.connection = this.getConnection();

        result = searchRes();
        if (result != null)
            return result;

        result = searchTour();
        if (result != null)
            return result;

        result = searchTourByDate();
        if (result != null)
            return result;

        result = searchTourByAttraction();
        if (result != null)
            return result;

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
                if (!(matchByName(name))) continue;
                Tour tour = new Tour(rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("attraction"),
                        rs.getInt("duration"),
                        rs.getInt("weekDayPrice"),
                        rs.getInt("weekEndPrice"),
                        rs.getString("dates")
                );
                StringBuilder str = tour.getAllTourInfo();
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
            List<Tour> tourList = new ArrayList<Tour>() ;
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
                    result = Tour.getBasicTourInfoSortByPrice(tourList, text).toString();
                }
                else result = Tour.getBasicTourInfoByDate(tourList, text).toString();
            }
            rs.close();
            stmt.close();
        }
        catch (Exception e){
            System.out.println("searchTourByDate()" + e);
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
            List<Tour> tourList = new ArrayList<Tour>() ;
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
                    result = Tour.getBasicTourInfoSortByPrice(tourList, text).toString();
                }
                else result = Tour.getBasicTourInfoByDate(tourList, text).toString();
            }
            rs.close();
            stmt.close();
        }
        catch (Exception e){
            System.out.println("searchTourByDate()" + e);
        }
        return result;
    }
    private boolean matchByName(String name){
        if (text.toLowerCase().matches("(.)*" + name + "(.)*")) return true;
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

    private boolean matchBySort(){
        if (text.toLowerCase().matches("(.)*sort(.)*")) return true;
        return false;
    }
    private boolean matchByPrice(){
        if (text.toLowerCase().matches("(.)*price(.)*")) return true;
        return false;
    }

    List<String> getTourList() throws Exception {
        //Write your code here
        List<String> tourList = new ArrayList<String>();
        try {
            Connection connection = this.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT name FROM tour");
            ResultSet placeList = stmt.executeQuery();
            while (placeList.next()) {
                tourList.add(placeList.getString(1));
            }
            placeList.close();
            stmt.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Failed to get from database" + e);
        }
        if (tourList != null)
            return tourList;
        throw new Exception("NOT FOUND");
    }
}