import javax.swing.*;

import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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

        add(tabbedPane, BorderLayout.CENTER);

        lowerPane = new LowerPane();
        lowerPane.setTriggerPanel(tourPane);
        add(lowerPane,BorderLayout.PAGE_END);

        tabbedPane.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JPanel panel = (JPanel) tabbedPane.getSelectedComponent();
                if (panel == tourPane) lowerPane.setTriggerPanel(tourPane);
                else if (panel == faqPane) lowerPane.setTriggerPanel(faqPane);
            }
        });


    }

}
