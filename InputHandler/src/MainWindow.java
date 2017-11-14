import javax.swing.*;

import java.awt.*;

public class MainWindow extends JPanel  {

    private Button addTourButton;
    private Button addFAQButton;

    private TourPane tourPane;
    private LowerPanel lowerPanel;

    public MainWindow()  {
        super(new BorderLayout());

        //Get the tour label and field panel
        tourPane = new TourPane();
        JPanel labelPane = tourPane.getLabelPane();
        JPanel fieldPane = tourPane.getFieldPane();

        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        add(labelPane, BorderLayout.LINE_START);
        add(fieldPane, BorderLayout.LINE_END);

        lowerPanel = new LowerPanel();
        lowerPanel.setTriggerPanel(tourPane);
        add(lowerPanel,BorderLayout.PAGE_END);

        JPanel upperPane = new JPanel(new GridLayout(1,0));
        addTourButton = new Button("Add Tour");
//        addTourButton.addActionListener(this);
        addFAQButton = new Button("Add FAQ");
//        addFAQButton.addActionListener(this);
        upperPane.add(addTourButton);
        upperPane.add(addFAQButton);
        add(upperPane,BorderLayout.PAGE_START);

    }

}
