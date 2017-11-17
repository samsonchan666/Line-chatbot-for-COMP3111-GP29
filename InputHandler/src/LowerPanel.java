/**
 * Created by Samson on 13/11/2017.
 */

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LowerPanel extends JPanel implements ActionListener{
    private JLabel resMsg;
    private Button submitButton;
    private TourPane tourPane;

    public LowerPanel(){
        super(new GridLayout(1,0));
        resMsg = new JLabel("                                                                          ");
        submitButton = new Button("Submit");
        submitButton.addActionListener(this);
        add(resMsg);
        add(submitButton);
    }

    public void setTriggerPanel(TourPane tourPane){
        this.tourPane = tourPane;
    }

    public void actionPerformed(ActionEvent e) {
        String msg = tourPane.performSubmit();
        resMsg.setText(msg);
    }
}
