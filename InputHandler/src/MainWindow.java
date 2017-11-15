import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;


public class MainWindow extends JPanel  {

    private TourPane tourPane;
    private FaqPane faqPane;
    private LowerPane lowerPane;


    public MainWindow()  {
        super(new BorderLayout());

        //Get the tour label and field panel
        tourPane = new TourPane();
        faqPane = new FaqPane();

        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Add Tour", tourPane);
        tabbedPane.addTab("Add FAQ", faqPane);
        add(tabbedPane,BorderLayout.CENTER);

        lowerPane = new LowerPane();
        lowerPane.setTriggerPanel(tourPane);
        add(lowerPane,BorderLayout.PAGE_END);

        tabbedPane.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedComponent() == tourPane){
                    lowerPane.setTriggerPanel(tourPane);
                }
                else if (tabbedPane.getSelectedComponent() == faqPane){
                    lowerPane.setTriggerPanel(faqPane);
                }
            }
        });


    }

}
