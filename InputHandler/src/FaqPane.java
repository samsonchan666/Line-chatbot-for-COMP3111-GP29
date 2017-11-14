/**
 * Created by Samson on 14/11/2017.
 */
import javax.swing.*;

import java.awt.*;

public class FaqPane extends JPanel implements CenterPane{
    private JLabel keywordLabel;
    private JLabel respondLabel;

    private static String keywordString = "Keywords: ";
    private static String respondString = "Respond: ";

    private JTextField keywordField;
    private JTextArea respondField;

    private JPanel labelPane;
    private JPanel fieldPane;

    private String keyword;
    private String respond;

    DatabaseEngine databaseEngine;

    public FaqPane(){
        super(new BorderLayout());
        initFaqLabelField();
        initFaqPanel();
        try {
            databaseEngine = new DatabaseEngine();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        add(labelPane,BorderLayout.LINE_START);
        add(fieldPane,BorderLayout.LINE_END);
    }

    public String performSubmit(){
        getFields();
        if (inputIsValid()) {
            reset();
            addToDatabase();
            return "Tour inserted successfully ";
        }
        else return errorMsg();
    }

    private void getFields(){
        keyword = keywordField.getText();
        respond = respondField.getText();
    }

    private void addToDatabase(){
        try {
            databaseEngine.addFaq(keyword, respond);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean inputIsValid(){
        return CheckingHandler.inputCheck(keyword);
    }

    private String errorMsg(){
        String msg = "<html>";
        msg+= "keywords should be separated by comma, no space";
        return msg;
    }

    private void reset(){
        keywordField.setText(null);
        respondField.setText(null);
    }

    private void initFaqLabelField(){
        keywordLabel = new JLabel(keywordString);
        respondLabel = new JLabel(respondString);

        keywordField = new JTextField();
        keywordField.setColumns(30);

        respondField = new JTextArea();
        respondField.setLineWrap(true);
        respondField.setColumns(10);

        keywordLabel.setLabelFor(keywordField);
        respondLabel.setLabelFor(respondField);
    }

    private void initFaqPanel(){
        labelPane = new JPanel(new GridLayout(0,1));
        labelPane.add(keywordLabel);
        labelPane.add(respondLabel);

        fieldPane = new JPanel(new GridLayout(0,1));
        fieldPane.add(keywordField);
        fieldPane.add(respondField);

    }
}
