
package haniffa.catering.Admin.Classes;

import haniffa.catering.Database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import javax.swing.JSpinner;
import javax.swing.JTextField;





public class Order {
   
    // Method to extract amount from a JLabel
    private static double extractAmountFromLabel(JLabel label) {
        String text = label.getText().replace("$", "");
        return Double.parseDouble(text);
    }

    // Calculate total amount
    public static double calculateTotal(JLabel... labels) {
        double totalAmount = 0.00;
        for (JLabel label : labels) {
            totalAmount += extractAmountFromLabel(label);
        }
        return totalAmount;
    }
    
    // Balance amount
    public static void balanceAmount (double totalAmount, JTextField txtAdvancePayment, String advancePayment, JLabel balanceLabel) {
         advancePayment = txtAdvancePayment.getText();
        try {
            double advanceAmount = Double.parseDouble(advancePayment);
            double balance = totalAmount - advanceAmount;
            balanceLabel.setText("$" + String.format("%.2f", balance));
        }
        catch (NumberFormatException ex) {
            // Handle invalid input
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.");
        }
            
    }
    
    // Save to database
    public static void orderConfirm (String name, int phoneNumber, int chicken, int beef, int mutton
                , int exChicken, int exWattalapam, double total, double advance, double balance) 
    {   
        
        Connection connection = DatabaseConnection.getConnection();
        int batchID = 0;
        try {
            
            
            // Retreive active batch id
            
            String sql = "SELECT * FROM batch WHERE BatchStatus = true";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                 batchID = rs.getInt("BatchID");
                
            }else{
                System.out.println("No Batch is currently active");
                return;
            }
            
            
           
            String insert = "INSERT INTO orders (CustomerName, CustomerContact, Chicken, Beef, Mutton, ExChicken, ExWattalapam, Total, Advance, Balance, BatchID) VALUES "
                    + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement queryPrepareStatement = connection.prepareStatement(insert);
            queryPrepareStatement.setString(1, name);
            queryPrepareStatement.setInt(2, phoneNumber);
            queryPrepareStatement.setInt(3, chicken);
            queryPrepareStatement.setInt(4, beef);
            queryPrepareStatement.setInt(5, mutton);
            queryPrepareStatement.setInt(6, exChicken);
            queryPrepareStatement.setInt(7, exWattalapam);
            queryPrepareStatement.setDouble(8, total);
            queryPrepareStatement.setDouble(9, advance);
            queryPrepareStatement.setDouble(10, balance);  
            queryPrepareStatement.setInt(11, batchID);
                 
            queryPrepareStatement.execute();

            JOptionPane.showMessageDialog(null, "Order Placed");
                     
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Clear the feilds
    public static void clearOrder(JTextField nameField, JTextField phoneField, JSpinner chickenSpinner, JSpinner beefSpinner, JSpinner muttonSpinner, JSpinner extraChickenSpinner, JSpinner extraWattalapamSpinner, JLabel chickenAmount, JLabel beefAmount, JLabel muttonAmount, JLabel extraChickenAmount, JLabel extraWattalapamAmount, JLabel totalAmount, JLabel balanceAmount, JTextField advanceAmount) {
        // Clear text fields
        nameField.setText("");
        phoneField.setText("");

        // Reset spinner values to 0
        chickenSpinner.setValue(0);
        beefSpinner.setValue(0);
        muttonSpinner.setValue(0);
        extraChickenSpinner.setValue(0);
        extraWattalapamSpinner.setValue(0);
        
        // Clear JLabels
        chickenAmount.setText("00.00");
        beefAmount.setText("00.00");
        muttonAmount.setText("00.00");
        extraChickenAmount.setText("00.00");
        extraWattalapamAmount.setText("00.00");
        totalAmount.setText("00.00");
        balanceAmount.setText("00.00");
        
        // Clear JTextField
        advanceAmount.setText("");
    }
    
    // Set amount values to the table
   public static void displayAmount(JLabel chickenLabel, JLabel beefLabel, JLabel muttonLabel, JLabel exChickenLabel, JLabel exWattalapamLabel) {
        ChickenBiriyani chickenItem = new ChickenBiriyani();
        BeefBiriyani beefItem = new BeefBiriyani();
        MuttonBiriyani muttonItem = new MuttonBiriyani();
        ExtraChicken exChickenItem = new ExtraChicken();
        ExtraWattalapam exWattalapamItem = new ExtraWattalapam();

        
        chickenLabel.setText(String.valueOf(chickenItem.getPrice()));
        beefLabel.setText(String.valueOf(beefItem.getPrice()));
        muttonLabel.setText(String.valueOf(muttonItem.getPrice()));
        exChickenLabel.setText(String.valueOf(exChickenItem.getPrice()));
        exWattalapamLabel.setText(String.valueOf(exWattalapamItem.getPrice()));
    }
   
   public static void displayOrderCounts(JLabel TotalChicken, JLabel TotalBeef, JLabel TotalMutton, JLabel TotalExtraChicken, JLabel TotalExtraWattalapam) {
       Connection connection = DatabaseConnection.getConnection();
       try {
          
            String query = "SELECT SUM(Chicken) AS TotalChicken, "
                         + "SUM(Beef) AS TotalBeef, "
                         + "SUM(Mutton) AS TotalMutton, "
                         + "SUM(ExChicken) AS TotalExtraChicken, "
                         + "SUM(ExWattalapam) AS TotalExtraWattalapam "
                         + "FROM orders";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Retrieve the total counts for each type of item
                int totalChicken = resultSet.getInt("TotalChicken");
                int totalBeef = resultSet.getInt("TotalBeef");
                int totalMutton = resultSet.getInt("TotalMutton");
                int totalExtraChicken = resultSet.getInt("TotalExtraChicken");
                int totalExtraWattalapam = resultSet.getInt("TotalExtraWattalapam");

                // Update the JLabels with the retrieved counts
                TotalChicken.setText(String.valueOf(totalChicken));
                TotalBeef.setText(String.valueOf(totalBeef));
                TotalMutton.setText(String.valueOf(totalMutton));
                TotalExtraChicken.setText(String.valueOf(totalExtraChicken));
                TotalExtraWattalapam.setText(String.valueOf(totalExtraWattalapam));
            }
       }
       catch (SQLException e) {
            e.printStackTrace();
        }
   }
}
