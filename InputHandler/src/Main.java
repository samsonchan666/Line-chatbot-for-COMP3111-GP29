/**
 * Created by Samson on 2/11/2017.
 */

import javax.swing.*;

public class Main {

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Input Handler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add contents to the window.
        try{
            frame.add(new MainWindow());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args)  {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

//        DatabaseEngine databaseEngine = new DatabaseEngine();
//        databaseEngine.addTour();
//        databaseEngine.addCustomer();
    }

}
