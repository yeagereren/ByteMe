import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class ByteMeTest {
    Scanner scanner;
    FoodItem item4 ;
    List<CartItem> cart;
    HashMap<String, String> customerAccounts;
    private int quantity;
    private ByteMeCanteenSystem system;
    private CartItem item ;
    private static final String loginId = "hi4";
    @BeforeEach
    void setup(){
        scanner = new Scanner(System.in);
        system = new ByteMeCanteenSystem();
        item = new CartItem(item4,quantity);
        cart = new ArrayList<>();
        customerAccounts = new HashMap<>();
        system.customerAccounts.put("a","a");
        system.customerAccounts.put("hi4","pass");
    }
    @Test
    void testaddunavailableitems(){
        scanner = new Scanner("1\nPizza\n2");
        String result = ByteMe.cartOperations(scanner, system, cart, loginId);
        assertEquals("item not available", result);

    }
    @Test
    void testaddunavailableitems2(){
        scanner = new Scanner("1\nVadaPav\n2");
        String result = ByteMe.cartOperations(scanner, system, cart, loginId);
        assertEquals("item not available", result);

    }
    @Test
    void testAddAvailableItem() {
        scanner = new Scanner("1\nBurger\n2\n3");
        String result = ByteMe.cartOperations(scanner, system, cart, loginId);
        assertEquals("item added", result);

    }
    @Test
    void testAddAvailableItem2() {
        scanner = new Scanner("1\nSoda\n2\n3");
        String result = ByteMe.cartOperations(scanner, system, cart, loginId);
        assertEquals("item added", result);

    }
    @Test
    void testInvalidLogin(){
        scanner = new Scanner("2\naa\na");
        String result = ByteMe.customerMenu(scanner, system, cart);
        assertEquals("Invalid login credentials.", result);
    }
    @Test
    void testInvalidLogin2(){
        scanner = new Scanner("2\nhi\npass");
        String result = ByteMe.customerMenu(scanner, system, cart);
        assertEquals("Invalid login credentials.", result);
    }
    @Test
    void testValidLogin(){
        scanner = new Scanner("2\na\na\n5");
        String result = ByteMe.customerMenu(scanner, system, cart);
        assertEquals("\nLogin successful!", result);
    }
    @Test
    void testValidLogin2(){
        scanner = new Scanner("2\nhi4\npass\n5");
        String result = ByteMe.customerMenu(scanner, system, cart);
        assertEquals("\nLogin successful!", result);
    }
    @AfterEach
    void endUp(){
        system.customerAccounts.remove("a");
        system.customerAccounts.remove("hi4");
    }

}