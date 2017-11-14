/**
 * Created by Samson on 13/11/2017.
 */
import javax.swing.*;

import java.awt.*;

public class TourPane {
    //Labels to identify the fields
    private JLabel idLabel;
    private JLabel nameLabel;
    private JLabel descripLabel;
    private JLabel durationLabel;
    private JLabel dayLabel;
    private JLabel weekDayCostLabel;
    private JLabel weekEndCostLabel;

    //Strings for the labels
    private static String idString = "Tour ID: ";
    private static String nameString = "Tour Name ";
    private static String descripString = "Tour Short Description: ";
    private static String durationString = "Duration: ";
    private static String dayString = "Days: ";
    private static String weekDayCostString = "Weekday cost: ";
    private static String weekEndCostString = "Weekend cost: ";

    private JTextField idField;
    private JTextField nameField;
    private JTextField descripField;
    private JTextField durationField;
    private JTextField dayField;
    private JTextField weekDayCostField;
    private JTextField weekEndCostField;

    private String id ;
    private String name ;
    private String descrip;
    private String duration;
    private String day ;
    private String weekDayCost;
    private String weekEndCost;

    private JPanel labelPane;
    private JPanel fieldPane;

    DatabaseEngine databaseEngine;

    public TourPane(){
        initLabelField();
        try {
            databaseEngine = new DatabaseEngine();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        labelPane = new JPanel(new GridLayout(0,1));
        labelPane.add(idLabel);
        labelPane.add(nameLabel);
        labelPane.add(descripLabel);
        labelPane.add(durationLabel);
        labelPane.add(dayLabel);
        labelPane.add(weekDayCostLabel);
        labelPane.add(weekEndCostLabel);

        fieldPane = new JPanel(new GridLayout(0,1));
        fieldPane.add(idField);
        fieldPane.add(nameField);
        fieldPane.add(descripField);
        fieldPane.add(durationField);
        fieldPane.add(dayField);
        fieldPane.add(weekDayCostField);
        fieldPane.add(weekEndCostField);
    }

    public JPanel getLabelPane() { return labelPane;}
    public JPanel getFieldPane() { return fieldPane;}

    public String performSubmit(){
        getFields();
        if (isValid()) {
            reset();
            addToDatabase();
            return "Tour inserted successfully ";
        }
        else return errorMsg();
    }

    private void getFields(){
        id = idField.getText();
        name = nameField.getText();
        descrip = descripField.getText();
        duration = durationField.getText();
        day = dayField.getText();
        weekDayCost = weekDayCostField.getText();
        weekEndCost = weekEndCostField.getText();
    }

    private void addToDatabase(){
        try {
            databaseEngine.addTour(id, name, descrip, duration, day, weekDayCost, weekEndCost);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean isValid(){
        return CheckingHandler.inputCheck(id, duration, weekDayCost, weekEndCost);
    }

    private String errorMsg(){
        String msg = "<html>";
        if (!(CheckingHandler.idCheck(id))) msg+="ID Invalid; ";
        if (!(CheckingHandler.durationCheck(duration))) msg+= "Duration Invalid; ";
        if (!(CheckingHandler.costCheck(weekDayCost,weekEndCost))) msg+= "Cost Invalid; ";
        msg+="</html>";
        return msg;
    }

    private void reset(){
        idField.setText(null);
        nameField.setText(null);
        descripField.setText(null);
        durationField.setText(null);
        dayField.setText(null);
        weekDayCostField.setText(null);
        weekEndCostField.setText(null);
    }

    private void initLabelField(){
        idLabel = new JLabel(idString);
        nameLabel = new JLabel(nameString);
        descripLabel = new JLabel(descripString);
        durationLabel = new JLabel(durationString);
        dayLabel = new JLabel(dayString);
        weekDayCostLabel = new JLabel(weekDayCostString);
        weekEndCostLabel = new JLabel(weekEndCostString);

        idField = new JTextField();
        idField.setColumns(30);

        nameField = new JTextField();
        nameField.setColumns(10);

        descripField = new JTextField();
        descripField.setColumns(10);

        durationField = new JTextField();
        durationField.setColumns(10);

        dayField = new JTextField();
        dayField.setColumns(10);

        weekDayCostField = new JTextField();
        weekDayCostField.setColumns(10);

        weekEndCostField = new JTextField();
        weekEndCostField.setColumns(10);

        idLabel.setLabelFor(idField);
        nameLabel.setLabelFor(nameField);
        descripLabel.setLabelFor(descripField);
        durationLabel.setLabelFor(descripField);
        dayLabel.setLabelFor(descripField);
        weekDayCostLabel.setLabelFor(weekDayCostField);
        weekEndCostLabel.setLabelFor(weekEndCostField);
    }

}
