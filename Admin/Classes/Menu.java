
package haniffa.catering.Admin.Classes;

import java.awt.List;
import java.awt.MenuItem;
import java.util.ArrayList;
import javax.swing.JSpinner;


public class Menu {
    private String name;
    private double price;
    
    Menu (String name, double price) {
        this.name = name;
        this.price = price;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }
    
    
     
}

