import java.util.*;
import java.util.stream.Collectors;
import java.util.Date;

class Review {
    private String customerName;
    private String reviewText;
    private int rating; // Assume rating is out of 5
    private Date reviewDate;

    public Review(String customerName, String reviewText, int rating) {
        this.customerName = customerName;
        this.reviewText = reviewText;
        this.rating = rating;
        this.reviewDate = new Date(); // Set the current date
    }

    @Override
    public String toString() {
        return "Review by " + customerName + " on " + reviewDate +
                ": " + reviewText + " (Rating: " + rating + "/5)";
    }
}

class Order {
    List<CartItem> items;
    String status;

    public Order(List<CartItem> items) {
        this.items = new ArrayList<>(items);
        this.status = "Pending"; // Set initial status to pending
    }

    public String getStatus() {
        return status;
    }

    public void cancelOrder() {
        this.status = "Canceled";
    }

    public List<CartItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Order{" +
                "items=" + items +
                ", status='" + status + '\'' +
                '}';
    }
}


class FoodItem {
    String name;
    double price;
    String category;
    boolean available;
    List<Review> reviews;

    public FoodItem(String name, double price, String category, boolean available) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.available = available;
        this.reviews = new ArrayList<>();
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public List<Review> getReviews() {
        return reviews;
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
    HashMap<String, List<Order>> orderHistory = new HashMap<>();
    private List<Order> orders;

    public ByteMeCanteenSystem() {
        addItemToMenu("Burger", 5.00, "Snacks", true);
        addItemToMenu("Fries", 2.50, "Snacks", true);
        addItemToMenu("Soda", 1.50, "Beverages", true);
        addItemToMenu("Pizza", 8.00, "Meals", false);
        addItemToMenu("Coffee", 3.00, "Beverages", true);
        this.orders = new ArrayList<>();
    }
    

    public void addMenuItem(FoodItem item) {
        menu.put(item.getName(), item);
    }

    public boolean removeMenuItem(String itemName) {
        if (menu.containsKey(itemName)) {
            menu.remove(itemName);
            return true;
        }
        return false;
    }

    public FoodItem getMenuItem(String itemName) {
        return menu.get(itemName);
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

    public void createOrder(String loginId, List<CartItem> cart) {
        Order order = new Order(cart);
        orderHistory.computeIfAbsent(loginId, k -> new ArrayList<>()).add(order);
    }

    public List<Order> getOrderHistory(String loginId) {
        return orderHistory.getOrDefault(loginId, new ArrayList<>());
    }

    public void leaveReview(Scanner scanner, String itemName) {
        FoodItem item = menu.get(itemName);
        if (item == null) {
            System.out.println("Item not found in menu.");
            return;
        }

        System.out.print("Enter your name: ");
        String customerName = scanner.nextLine();

        System.out.print("Enter your review: ");
        String reviewText = scanner.nextLine();

        System.out.print("Enter your rating (1-5): ");
        int rating = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Review review = new Review(customerName, reviewText, rating);
        item.addReview(review);
        System.out.println("Thank you for your review!");
    }

    // Method for viewing reviews
    public void viewReviews(String itemName) {
        FoodItem item = menu.get(itemName);
        if (item == null) {
            System.out.println("Item not found in menu.");
            return;
        }

        List<Review> reviews = item.getReviews();
        if (reviews.isEmpty()) {
            System.out.println("No reviews yet for this item.");
        } else {
            System.out.println("Reviews for " + item.getName() + ":");
            for (Review review : reviews) {
                System.out.println(review);
            }
        }
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

    public static void menuManagement(Scanner scanner, ByteMeCanteenSystem system) {
        while (true) {
            System.out.println("\nMenu Management Options:");
            System.out.println("1. Add New Item");
            System.out.println("2. Update Existing Item");
            System.out.println("3. Remove Item");
            System.out.println("4. Go Back");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                // Add New Item
                System.out.print("Enter item name: ");
                String name = scanner.nextLine();
                System.out.print("Enter item price: ");
                double price = scanner.nextDouble();
                scanner.nextLine();
                System.out.print("Enter item category (Snacks/Beverages/Meals): ");
                String category = scanner.nextLine();
                System.out.print("Is the item available? (true/false): ");
                boolean available = scanner.nextBoolean();
                scanner.nextLine();

                FoodItem newItem = new FoodItem(name, price, category, available);
                system.addMenuItem(newItem);
                System.out.println(name + " has been added to the menu.");

            } else if (choice == 2) {
                // Update Existing Item
                System.out.print("Enter the name of the item to update: ");
                String itemName = scanner.nextLine();
                FoodItem itemToUpdate = system.getMenuItem(itemName);
                if (itemToUpdate != null) {
                    System.out.print("Enter new price (or press Enter to keep current): ");
                    String priceInput = scanner.nextLine();
                    if (!priceInput.isEmpty()) {
                        double newPrice = Double.parseDouble(priceInput);
                        itemToUpdate.setPrice(newPrice);
                    }

                    System.out.print("Is the item available? (true/false, or press Enter to keep current): ");
                    String availableInput = scanner.nextLine();
                    if (!availableInput.isEmpty()) {
                        boolean newAvailability = Boolean.parseBoolean(availableInput);
                        itemToUpdate.setAvailable(newAvailability);
                    }

                    System.out.println(itemName + " has been updated.");
                } else {
                    System.out.println("Item not found.");
                }

            } else if (choice == 3) {
                // Remove Item
                System.out.print("Enter the name of the item to remove: ");
                String itemName = scanner.nextLine();
                if (system.removeMenuItem(itemName)) {
                    System.out.println(itemName + " has been removed from the menu.");
                } else {
                    System.out.println("Item not found.");
                }

            } else if (choice == 4) {
                System.out.println("Returning to Admin Menu...");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
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
                    loggedInCustomerMenu(scanner, system, cart, loginId);
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
            adminMenu(scanner, system);
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    private static void adminMenu(Scanner scanner, ByteMeCanteenSystem system) {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Menu Management");
            System.out.println("2. Order Management");
            System.out.println("3. Logout");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                menuManagement(scanner, system);
            } else if (choice == 2) {
                orderManagement(scanner, system);
            } else if (choice == 3) {
                System.out.println("Logging out...");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void loggedInCustomerMenu(Scanner scanner, ByteMeCanteenSystem system, List<CartItem> cart, String loginId) {
        while (true) {
            System.out.println("\n1. Browse Menu");
            System.out.println("2. Cart Operations");
            System.out.println("3. Order Tracking");
            System.out.println("4. View Item Reviews");
            System.out.println("5. Leave a Review");
            System.out.println("6. Go Back to Customer Menu");

            int choice = scanner.nextInt();
            scanner.nextLine();
            if (choice == 1) {
                browseMenu(scanner, system);
            } else if (choice == 2) {
                cartOperations(scanner, system, cart, loginId);
            } else if (choice == 3) {
                orderTracking(scanner, system, loginId);
            } else if (choice == 4) {
                System.out.print("Enter item name to view reviews: ");
                String itemName = scanner.nextLine();
                system.viewReviews(itemName);
            } else if (choice == 5) {
                System.out.print("Enter item name to leave a review for: ");
                String itemName = scanner.nextLine();
                system.leaveReview(scanner, itemName);
            } else if (choice == 6) {
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

    public static void cartOperations(Scanner scanner, ByteMeCanteenSystem system, List<CartItem> cart, String loginId) {
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
                checkout(scanner, cart, loginId, system);
                break; // After checkout, go back to cart menu
            } else if (choice == 6) {
                System.out.println("Returning to Customer Menu...");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void orderManagement(Scanner scanner, ByteMeCanteenSystem system) {
        while (true) {
            System.out.println("\nOrder Management Options:");
            System.out.println("1. View Pending Orders");
            System.out.println("2. Update Order Status");
            System.out.println("3. Process Refunds");
            System.out.println("4. Go Back");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                viewPendingOrders(system);
            } else if (choice == 2) {
                updateOrderStatus(scanner, system);
            } else if (choice == 3) {
                processRefunds(scanner, system);
            } else if (choice == 4) {
                System.out.println("Returning to Admin Menu...");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewPendingOrders(ByteMeCanteenSystem system) {
        List<Order> pendingOrders = system.getPendingOrders();
        if (pendingOrders.isEmpty()) {
            System.out.println("No pending orders found.");
        } else {
            System.out.println("Pending Orders:");
            for (Order order : pendingOrders) {
                System.out.println(order);  // Implement toString() in Order class
            }
        }
    }

    private static void updateOrderStatus(Scanner scanner, ByteMeCanteenSystem system) {
        System.out.print("Enter order number to update status: ");
        int orderNumber = scanner.nextInt();
        scanner.nextLine();

        // Logic to fetch order by number and update status
        Order order = system.getOrderByNumber(orderNumber);
        if (order != null) {
            System.out.println("Current Status: " + order.getStatus());
            System.out.println("Update to (1: Preparing, 2: Out for Delivery, 3: Completed): ");
            int statusChoice = scanner.nextInt();
            scanner.nextLine();

            switch (statusChoice) {
                case 1:
                    order.setStatus("Preparing");
                    break;
                case 2:
                    order.setStatus("Out for Delivery");
                    break;
                case 3:
                    order.setStatus("Completed");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
            System.out.println("Order status updated successfully!");
        } else {
            System.out.println("Order not found.");
        }
    }

    private static void processRefunds(Scanner scanner, ByteMeCanteenSystem system) {
        System.out.print("Enter order number to process refund: ");
        int orderNumber = scanner.nextInt();
        scanner.nextLine();

        Order order = system.getOrderByNumber(orderNumber);
        if (order != null && order.isCancellable()) {
            order.processRefund();
            System.out.println("Refund processed successfully!");
        } else {
            System.out.println("Order not found or cannot be refunded.");
        }
    }


    public static void orderTracking(Scanner scanner, ByteMeCanteenSystem system, String loginId) {
        while (true) {
            System.out.println("\nOrder Tracking Options:");
            System.out.println("1. View Order Status");
            System.out.println("2. Cancel Order");
            System.out.println("3. View Order History");
            System.out.println("4. Reorder Previous Order");
            System.out.println("5. Go Back");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                List<Order> orders = system.getOrderHistory(loginId);
                if (orders.isEmpty()) {
                    System.out.println("You have no active orders.");
                } else {
                    // Display all orders
                    for (int i = 0; i < orders.size(); i++) {
                        System.out.println("Order #" + (i + 1) + ": " + orders.get(i).getStatus());
                    }
                }
            } else if (choice == 2) {
                List<Order> orders = system.getOrderHistory(loginId);
                if (orders.isEmpty()) {
                    System.out.println("You have no active orders to cancel.");
                } else {
                    System.out.print("Enter the order number you wish to cancel (e.g., 1, 2): ");
                    int orderNumber = scanner.nextInt();
                    scanner.nextLine();
                    if (orderNumber > 0 && orderNumber <= orders.size()) {
                        orders.get(orderNumber - 1).cancelOrder();
                        System.out.println("Order #" + orderNumber + " has been canceled.");
                    } else {
                        System.out.println("Invalid order number.");
                    }
                }
            } else if (choice == 3) {
                List<Order> orders = system.getOrderHistory(loginId);
                if (orders.isEmpty()) {
                    System.out.println("No order history found.");
                } else {
                    System.out.println("Your Order History:");
                    int orderNumber = 1;
                    for (Order order : orders) {
                        System.out.println("Order #" + orderNumber + ": " + order);
                        orderNumber++;
                    }
                }
            } else if (choice == 4) {
                List<Order> orders = system.getOrderHistory(loginId);
                if (orders.isEmpty()) {
                    System.out.println("No order history found.");
                } else {
                    System.out.print("Enter the order number you wish to reorder (e.g., 1, 2): ");
                    int orderNumber = scanner.nextInt();
                    scanner.nextLine();
                    if (orderNumber > 0 && orderNumber <= orders.size()) {
                        Order orderToReorder = orders.get(orderNumber - 1);
                        system.createOrder(loginId, new ArrayList<>(orderToReorder.getItems()));
                        System.out.println("Order #" + orderNumber + " has been reordered successfully!");
                    } else {
                        System.out.println("Invalid order number.");
                    }
                }
            } else if (choice == 5) {
                System.out.println("Returning to Customer Menu...");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    public void leaveReview(FoodItem item) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String customerName = scanner.nextLine();

        System.out.print("Enter your review: ");
        String reviewText = scanner.nextLine();

        System.out.print("Enter your rating (1-5): ");
        int rating = scanner.nextInt();

        Review review = new Review(customerName, reviewText, rating);
        item.addReview(review);
        System.out.println("Thank you for your review!");
    }

    public void viewReviews(FoodItem item) {
        List<Review> reviews = item.getReviews();
        if (reviews.isEmpty()) {
            System.out.println("No reviews yet for this item.");
        } else {
            System.out.println("Reviews for " + item.getName() + ":");
            for (Review review : reviews) {
                System.out.println(review);
            }
        }
    }

    public static void checkout(Scanner scanner, List<CartItem> cart, String loginId, ByteMeCanteenSystem system) {
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

        boolean paymentSuccessful = false;
        if (paymentMethod == 1) {
            System.out.print("Enter total amount to pay (exact): ");
            double cashPayment = scanner.nextDouble();
            if (cashPayment == totalAmount) {
                paymentSuccessful = true;
            } else {
                System.out.println("Incorrect amount. Payment failed.");
            }
        } else if (paymentMethod == 2) {
            System.out.print("Enter card number: ");
            String cardNumber = scanner.nextLine();
            System.out.print("Enter total amount to pay (exact): ");
            double cardPayment = scanner.nextDouble();
            if (cardPayment == totalAmount) {
                paymentSuccessful = true;
            } else {
                System.out.println("Incorrect amount. Payment failed.");
            }
        } else {
            System.out.println("Invalid payment method selected.");
        }

        if (paymentSuccessful) {
            System.out.println("Payment successful! Thank you for your order.");
            system.createOrder(loginId, cart);
            System.out.println("Order created successfully! You can track your order now.");
            cart.clear();
        }
    }
}
