
package haniffa.catering.Admin.Classes;

import haniffa.catering.Database.DatabaseConnection;
import java.awt.Color;
import java.sql.Connection;
import javax.swing.JComboBox;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Batch {
    public String batchName;
    private boolean batchStatus = false;

    
    public static void createBatch (JComboBox<String> batchComboBox, boolean batchStatus, JButton Start) {
          
        // connect database  
        Connection connection = DatabaseConnection.getConnection();
        
        try {
            // Save batch information
            String SelectedEvent = (String) batchComboBox.getSelectedItem();
            batchStatus = true;

            String insertQuery = "INSERT INTO batch (BatchName, BatchStatus) VALUES(?, ?)";
            PreparedStatement queryPrepareStatement = connection.prepareStatement(insertQuery);
            queryPrepareStatement.setString(1, SelectedEvent);
            queryPrepareStatement.setBoolean(2, batchStatus);

            queryPrepareStatement.execute();

            JOptionPane.showMessageDialog(null, "Batch Created");        
        
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
      
    }
    
    public static void restrictBatch (JButton Start, JLabel message) {
         // connect database  
        Connection connection = DatabaseConnection.getConnection();
        
        try {
            
             String query = "SELECT batchStatus FrOM batch WHERE batchStatus = true";
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             
             ResultSet resultSet = preparedStatement.executeQuery();
             
             if(resultSet.next()) {
                 Start.setEnabled(false);
                 message.setText("Please end the current batch to start a new batch");
                 message.setForeground(Color.red);
                 return;
                 
             } else {
                  Start.setEnabled(true);
                  message.setText("No batch Active");
                  message.setForeground(Color.GREEN);
             }
        }
         catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
    public static void EndBatch() {
    Connection connection = DatabaseConnection.getConnection();
    boolean batchStatus;
    try {
        String query = "SELECT batchStatus FROM batch WHERE batchStatus = true";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        
            if(resultSet.next()) {
                batchStatus = resultSet.getBoolean(1);
                if (batchStatus) {
                    String updateQuery = "UPDATE batch SET batchStatus = false WHERE batchStatus = true";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    int rowsUpdated = updateStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(null, "Batch status updated successfully.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to update batch status.");
                    }
                } 
                else {
                    JOptionPane.showMessageDialog(null, "Batch is already inactive.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No active batch found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
