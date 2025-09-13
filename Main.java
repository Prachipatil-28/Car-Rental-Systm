import java.util.*;

class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;

    public Car(String carId, String brand, String model, double basePricePerDay) {
        this.carId = carId.toUpperCase(); // store in uppercase for consistency
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;
    }
}

class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

class Rental {
    private Car car;
    private Customer customer;
    private int days;
    private double totalPrice;

    public Rental(Car car, Customer customer, int days, double totalPrice) {
        this.car = car;
        this.customer = customer;
        this.days = days;
        this.totalPrice = totalPrice;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}

class CarRentalSystem {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;
    private double totalEarnings = 0.0;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentCar(Car car, Customer customer, int days) {
        if (car.isAvailable()) {
            double totalPrice = car.calculatePrice(days);
            car.rent();
            rentals.add(new Rental(car, customer, days, totalPrice));
            totalEarnings += totalPrice;
        } else {
            System.out.println("Car is not available for rent.");
        }
    }

    public void returnCar(Car car) {
        car.returnCar();
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getCar() == car) {
                rentalToRemove = rental;
                break;
            }
        }
        if (rentalToRemove != null) {
            rentals.remove(rentalToRemove);
        }
    }

    public void showRentalHistory() {
        if (rentals.isEmpty()) {
            System.out.println("\nNo ongoing rentals right now.");
        } else {
            System.out.println("\n== Current Rentals ==");
            for (Rental rental : rentals) {
                System.out.println(rental.getCustomer().getName() + " has rented "
                        + rental.getCar().getBrand() + " " + rental.getCar().getModel()
                        + " for " + rental.getDays() + " days. Total: $" + rental.getTotalPrice());
            }
        }
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Car Rental System =====");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. View Current Rentals");
            System.out.println("4. View Total Earnings");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine();
            if (!input.matches("[1-5]")) {
                System.out.println("Invalid choice. Please enter a number between 1-5.");
                continue;
            }

            int choice = Integer.parseInt(input);

            if (choice == 1) {
                System.out.println("\n== Rent a Car ==\n");
                String customerName;
                do {
                    System.out.print("Enter your name: ");
                    customerName = scanner.nextLine().trim();
                    if (customerName.isEmpty()) {
                        System.out.println("Name cannot be empty. Please enter a valid name.");
                    }
                } while (customerName.isEmpty());

                System.out.println("\nAvailable Cars:");
                boolean hasAvailable = false;
                for (Car car : cars) {
                    if (car.isAvailable()) {
                        hasAvailable = true;
                        System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel());
                    }
                }

                if (!hasAvailable) {
                    System.out.println("No cars available right now.");
                    continue;
                }

                System.out.print("\nEnter the car ID you want to rent: ");
                String carId = scanner.nextLine().toUpperCase();

                System.out.print("Enter the number of days for rental: ");
                int rentalDays;
                try {
                    rentalDays = Integer.parseInt(scanner.nextLine());
                    if (rentalDays <= 0) {
                        System.out.println("Rental days must be greater than 0.");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number of days.");
                    continue;
                }

                Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
                addCustomer(newCustomer);

                Car selectedCar = null;
                for (Car car : cars) {
                    if (car.getCarId().equals(carId) && car.isAvailable()) {
                        selectedCar = car;
                        break;
                    }
                }

                if (selectedCar != null) {
                    double totalPrice = selectedCar.calculatePrice(rentalDays);
                    System.out.println("\n== Rental Information ==\n");
                    System.out.println("Customer ID: " + newCustomer.getCustomerId());
                    System.out.println("Customer Name: " + newCustomer.getName());
                    System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
                    System.out.println("Rental Days: " + rentalDays);
                    System.out.printf("Total Price: $%.2f%n", totalPrice);

                    System.out.print("\nConfirm rental (Y/N): ");
                    String confirm = scanner.nextLine();

                    if (confirm.equalsIgnoreCase("Y")) {
                        rentCar(selectedCar, newCustomer, rentalDays);
                        System.out.println("\nCar rented successfully.");
                    } else {
                        System.out.println("\nRental canceled.");
                    }
                } else {
                    System.out.println("\nInvalid car selection or car not available for rent.");
                }

            } else if (choice == 2) {
                System.out.println("\n== Return a Car ==\n");
                System.out.print("Enter the car ID you want to return: ");
                String carId = scanner.nextLine().toUpperCase();

                Car carToReturn = null;
                for (Car car : cars) {
                    if (car.getCarId().equals(carId) && !car.isAvailable()) {
                        carToReturn = car;
                        break;
                    }
                }

                if (carToReturn != null) {
                    Customer customer = null;
                    for (Rental rental : rentals) {
                        if (rental.getCar() == carToReturn) {
                            customer = rental.getCustomer();
                            break;
                        }
                    }

                    if (customer != null) {
                        returnCar(carToReturn);
                        System.out.println("Car returned successfully by " + customer.getName());
                    } else {
                        System.out.println("Car was not rented or rental information is missing.");
                    }
                } else {
                    System.out.println("Invalid car ID or car is not rented.");
                }

            } else if (choice == 3) {
                showRentalHistory();
            } else if (choice == 4) {
                System.out.printf("\nTotal Earnings: $%.2f%n", totalEarnings);
            } else if (choice == 5) {
                System.out.print("\nAre you sure you want to exit? (Y/N): ");
                String confirmExit = scanner.nextLine();
                if (confirmExit.equalsIgnoreCase("Y")) {
                    System.out.println("\nThank you for using the Car Rental System!");
                    break;
                }
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();

        Car car1 = new Car("C001", "Toyota", "Camry", 60.0);
        Car car2 = new Car("C002", "Honda", "Accord", 70.0);
        Car car3 = new Car("C003", "Mahindra", "Thar", 150.0);

        rentalSystem.addCar(car1);
        rentalSystem.addCar(car2);
        rentalSystem.addCar(car3);

        rentalSystem.menu();
    }
}
