import java.util.*;
import java.util.stream.Collectors;

class FoodItem {
    String name;
    double price;
    String category;
    boolean available;

    public FoodItem(String name, double price, String category, boolean available) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.available = available;
    }

    @Override
    public String toString() {
        return name + " - $" + price + " - " + category + " - " + (available ? "Available" : "Unavailable");
    }
}

class CartItem {
    FoodItem item;
    int quantity;

    public CartItem(FoodItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return item.price * quantity;
    }

    @Override
    public String toString() {
        return item.name + " - Quantity: " + quantity + " - Total: $" + getTotalPrice();
    }
}

class ByteMeCanteenSystem {
    TreeMap<String, FoodItem> menu = new TreeMap<>();
    HashMap<String, String> customerAccounts = new HashMap<>();

    public ByteMeCanteenSystem() {
        addItemToMenu("Burger", 5.00, "Snacks", true);
        addItemToMenu("Fries", 2.50, "Snacks", true);
        addItemToMenu("Soda", 1.50, "Beverages", true);
        addItemToMenu("Pizza", 8.00, "Meals", false);
        addItemToMenu("Coffee", 3.00, "Beverages", true);
    }

    public void addItemToMenu(String name, double price, String category, boolean available) {
        menu.put(name, new FoodItem(name, price, category, available));
    }

    public void displayAllItems() {
        menu.values().forEach(item -> System.out.println(item));
    }

    public List<FoodItem> searchMenu(String keyword) {
        return menu.values().stream().filter(item -> item.name.toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
    }

    public List<FoodItem> filterMenuByCategory(String category) {
        return menu.values().stream().filter(item -> item.category.equalsIgnoreCase(category)).collect(Collectors.toList());
    }

    public List<FoodItem> sortMenuByPrice(boolean ascending) {
        return menu.values().stream().sorted(Comparator.comparingDouble(item -> ascending ? item.price : -item.price)).collect(Collectors.toList());
    }

    public void registerCustomer(String loginId, String password) {
        customerAccounts.put(loginId, password);
        System.out.println("Registration successful. You can now log in.");
    }

    public boolean loginCustomer(String loginId, String password) {
        return customerAccounts.containsKey(loginId) && customerAccounts.get(loginId).equals(password);
    }
}

public class ByteMe {
    private static final String ADMIN_ID = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ByteMeCanteenSystem system = new ByteMeCanteenSystem();
        List<CartItem> cart = new ArrayList<>();

        while (true) {
            System.out.println("Welcome to Byte Me! Please select an option:");
            System.out.println("1. Customer");
            System.out.println("2. Admin");
            System.out.println("3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                customerMenu(scanner, system, cart);
            } else if (choice == 2) {
                adminLogin(scanner, system);
            } else if (choice == 3) {
                System.out.println("Exiting the system...");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void customerMenu(Scanner scanner, ByteMeCanteenSystem system, List<CartItem> cart) {
        while (true) {
            System.out.println("\nCustomer Menu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Back to Main Menu");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.print("Enter login ID: ");
                String loginId = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                system.registerCustomer(loginId, password);
            } else if (choice == 2) {
                System.out.print("Enter login ID: ");
                String loginId = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                if (system.loginCustomer(loginId, password)) {
                    System.out.println("\nLogin successful!");
                    loggedInCustomerMenu(scanner, system, cart);
                } else {
                    System.out.println("Invalid login credentials.");
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void adminLogin(Scanner scanner, ByteMeCanteenSystem system) {
        System.out.print("Enter admin ID: ");
        String adminId = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String adminPassword = scanner.nextLine();
        if (adminId.equals(ADMIN_ID) && adminPassword.equals(ADMIN_PASSWORD)) {
            System.out.println("Admin login successful!");
            // Admin functionalities would go here.
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    private static void loggedInCustomerMenu(Scanner scanner, ByteMeCanteenSystem system, List<CartItem> cart) {
        while (true) {
            System.out.println("\n1. Browse Menu");
            System.out.println("2. Cart Operations");
            System.out.println("3. Go Back to Customer Menu");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                browseMenu(scanner, system);
            } else if (choice == 2) {
                cartOperations(scanner, system, cart);
            } else if (choice == 3) {
                System.out.println("Returning to Customer Menu...");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void browseMenu(Scanner scanner, ByteMeCanteenSystem system) {
        while (true) {
            System.out.println("\nBrowse Menu Options:");
            System.out.println("1. View All Items");
            System.out.println("2. Search by Name or Keyword");
            System.out.println("3. Filter by Category");
            System.out.println("4. Sort by Price");
            System.out.println("5. Back to Customer Menu");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.println("\nComplete Menu:");
                system.displayAllItems();
            } else if (choice == 2) {
                System.out.print("Enter item name or keyword to search: ");
                String keyword = scanner.nextLine();
                List<FoodItem> searchResults = system.searchMenu(keyword);
                if (searchResults.isEmpty()) {
                    System.out.println("No items found matching '" + keyword + "'.");
                } else {
                    System.out.println("Search Results:");
                    searchResults.forEach(System.out::println);
                }
            } else if (choice == 3) {
                System.out.println("Select Category to Filter:");
                System.out.println("1. Snacks");
                System.out.println("2. Beverages");
                System.out.println("3. Meals");
                int categoryChoice = scanner.nextInt();
                scanner.nextLine();
                String category = switch (categoryChoice) {
                    case 1 -> "Snacks";
                    case 2 -> "Beverages";
                    case 3 -> "Meals";
                    default -> {
                        System.out.println("Invalid category choice.");
                        yield null;
                    }
                };
                if (category != null) {
                    List<FoodItem> filteredItems = system.filterMenuByCategory(category);
                    if (filteredItems.isEmpty()) {
                        System.out.println("No items found in category '" + category + "'.");
                    } else {
                        System.out.println("Filtered Items:");
                        filteredItems.forEach(System.out::println);
                    }
                }
            } else if (choice == 4) {
                System.out.print("Sort by price (1 for Ascending, 2 for Descending): ");
                int sortChoice = scanner.nextInt();
                scanner.nextLine();
                boolean ascending = (sortChoice == 1);
                List<FoodItem> sortedItems = system.sortMenuByPrice(ascending);
                System.out.println("Sorted Menu:");
                sortedItems.forEach(System.out::println);
            } else if (choice == 5) {
                System.out.println("Returning to Customer Menu...");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void cartOperations(Scanner scanner, ByteMeCanteenSystem system, List<CartItem> cart) {
        while (true) {
            System.out.println("\nCart Operations:");
            System.out.println("1. Add Items");
            System.out.println("2. Modify Item Quantity");
            System.out.println("3. Remove Item");
            System.out.println("4. View Cart");
            System.out.println("5. Checkout");
            System.out.println("6. Go Back");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.print("Enter food item name to add: ");
                String itemName = scanner.nextLine();
                FoodItem item = system.menu.values().stream()
                        .filter(foodItem -> foodItem.name.equalsIgnoreCase(itemName) && foodItem.available)
                        .findFirst()
                        .orElse(null);
                if (item != null && item.available) {
                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();
                    cart.add(new CartItem(item, quantity));
                    System.out.println(itemName + " added to cart.");
                } else {
                    System.out.println("Item not available.");
                }
            } else if (choice == 2) {
                System.out.print("Enter food item name to modify: ");
                String itemName = scanner.nextLine();
                CartItem cartItem = cart.stream().filter(ci -> ci.item.name.equalsIgnoreCase(itemName)).findFirst().orElse(null);
                if (cartItem != null) {
                    System.out.print("Enter new quantity: ");
                    int newQuantity = scanner.nextInt();
                    scanner.nextLine();
                    cartItem.quantity = newQuantity;
                    System.out.println("Quantity updated.");
                } else {
                    System.out.println("Item not found in cart.");
                }
            } else if (choice == 3) {
                System.out.print("Enter food item name to remove: ");
                String itemName = scanner.nextLine();
                CartItem cartItem = cart.stream().filter(ci -> ci.item.name.equalsIgnoreCase(itemName)).findFirst().orElse(null);
                if (cartItem != null) {
                    cart.remove(cartItem);
                    System.out.println(itemName + " removed from cart.");
                } else {
                    System.out.println("Item not found in cart.");
                }
            } else if (choice == 4) {
                if (cart.isEmpty()) {
                    System.out.println("Cart is empty.");
                } else {
                    System.out.println("Your Cart:");
                    cart.forEach(System.out::println);
                }
            } else if (choice == 5) {
                checkout(scanner, cart);
                break; // After checkout, go back to cart menu
            } else if (choice == 6) {
                System.out.println("Returning to Customer Menu...");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void checkout(Scanner scanner, List<CartItem> cart) {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty. Cannot proceed to checkout.");
            return;
        }

        System.out.print("Enter delivery address: ");
        String deliveryAddress = scanner.nextLine();

        double totalAmount = cart.stream().mapToDouble(CartItem::getTotalPrice).sum();
        System.out.println("Total Amount: $" + totalAmount);

        System.out.println("Select Payment Method:");
        System.out.println("1. Pay by Cash");
        System.out.println("2. Pay by Card");
        int paymentMethod = scanner.nextInt();
        scanner.nextLine();

        if (paymentMethod == 1) {
            System.out.print("Enter total amount to pay (exact): ");
            double cashPayment = scanner.nextDouble();
            if (cashPayment == totalAmount) {
                System.out.println("Payment successful! Thank you for your order.");
                cart.clear();
            } else {
                System.out.println("Incorrect amount. Payment failed.");
            }
        } else if (paymentMethod == 2) {
            System.out.print("Enter card number: ");
            String cardNumber = scanner.nextLine();
            System.out.print("Enter total amount to pay (exact): ");
            double cardPayment = scanner.nextDouble();
            if (cardPayment == totalAmount) {
                System.out.println("Payment successful! Thank you for your order.");
                cart.clear();
            } else {
                System.out.println("Incorrect amount. Payment failed.");
            }
        } else {
            System.out.println("Invalid payment method selected.");
        }

    }

}
