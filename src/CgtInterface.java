package src;
/*Author: (T7) Yiyuan Li, Tian zhiran
 *Student No: C3434681, C3494501
 *Date: 26-03-2024
 */
import java.util.List;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;


public class CgtInterface {

    private final User user;
    // make Scanner private, cuz core principles of object-oriented programming is encapsulation
    // also for Security and Integrity
    private final Scanner scanner = new Scanner(System.in);
    // create a new array list for user
    private final List<User> users = new ArrayList<>();

    public void run() {
        // loop
        while (true) {
            // main menu
            System.out.println("Please Select option:");
            System.out.println("(1). Add user");
            System.out.println("(2). Add user's investment");
            System.out.println("(3). Delete user");
            System.out.println("(4). Delete user's investment");
            System.out.println("(5). Check specific user's details");
            System.out.println("(6). Display all users");
            System.out.println("(7). Exit");
            int choice = getPositiveIntegerInput("Select a number: ");

            // user selection
            switch (choice) {
                case 1:
                // add user
                    if (users.size() < 5)
                        addUserAndProcess();
                    else
                        System.out.println("Cannot add more users. Maximum 5 users");
                    System.out.println("Press Enter to return to the main menu...");
                    scanner.nextLine(); // Consume the newline character
                    scanner.nextLine(); // Wait for the user to press Enter
                    break;
                case 2:
                // Add investment to a user
                    if (!users.isEmpty()) {
                        System.out.println("Enter the name of the user to add investment to:");
                        String userName = scanner.next().trim();
                        User userToAddInvestment = findUserByName(userName);
                        if (userToAddInvestment != null) {
                            addInvestmentToUser(userToAddInvestment);
                        } else {
                            System.out.println("User not found.");
                        }
                    } else {
                        System.out.println("There are no users to add investments to.");
                    }
                    System.out.println("Press Enter to return to the main menu...");
                    scanner.nextLine(); // Consume the newline character
                    scanner.nextLine(); // Wait for the user to press Enter
                    break;
                case 3:
                // delete user
                    deleteUser();
                    System.out.println("Press Enter to return to the main menu...");
                    scanner.nextLine(); // Consume the newline character
                    scanner.nextLine(); // Wait for the user to press Enter
                    break;
                case 4:
                // delete investment
                    deleteInvestment();
                    System.out.println("Press Enter to return to the main menu...");
                    scanner.nextLine(); // Consume the newline character
                    scanner.nextLine(); // Wait for the user to press Enter
                    break;
                case 5:
                // display user
                    displayUser();
                    System.out.println("Press Enter to return to the main menu...");
                    scanner.nextLine(); // Consume the newline character
                    scanner.nextLine(); // Wait for the user to press Enter
                    break;
                case 6:
                // display all users
                    displayAllUsers();
                    System.out.println("Press Enter to return to the main menu...");
                    scanner.nextLine(); // Consume the newline character
                    scanner.nextLine(); // Wait for the user to press Enter
                    break;
                case 7:
                // exit
                    System.out.println("Exiting program...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

        }
    }

    public CgtInterface() {
        this.user = new User();
    }
    
    // main function
    public static void main(String[] args){
        CgtInterface cgtInterface = new CgtInterface();
        cgtInterface.run();
    }


    /*
        - Add user
        - Delete user
        - Add user's investment
        - Delete user's investment
        - Check specific user's details
        - Display all users
     */
    private void addUserAndProcess() {
        User newUser = new User();

        /* here we have a bug (by executing get name and salary at the same time)
        * a short delay to ensure that the output gives the user time to react to the output
        * I know this delay is not the correct way to solve the problem, but it works, so .....
        */
        System.out.println("Preparing to add a new user...");
        try {
            Thread.sleep(500); // 500ms
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        

        // ask user basic information
        System.out.println("Enter user's name: ");
        newUser.setName(scanner.nextLine().trim());
        newUser.setAnnualSalary(getPositiveDoubleInput("Enter your annual salary: "));
        System.out.println("Are you a resident? (yes/no)");
        newUser.setResident(getYesNoInput());
        newUser.setBuyingPrice(getPositiveDoubleInput("Enter buying price of the cryptocurrency:"));
        newUser.setSellingPrice(getSellingPriceInput("Enter selling price of the cryptocurrency:", newUser.getBuyingPrice()));
        newUser.setYears(getPositiveIntegerInput("Enter the number of years cryptocurrency is held:"));
        newUser.calcCgt();

        users.add(newUser);
        System.out.println("User added successfully!" + "\n");
        writeUsersToFile(); // Update the file with the latest users list
    }

    // add user's investment
    private void addInvestmentToUser(User user) {
        // Check if the user already has two investments
        if (user.getInvestments().size() >= 2) {
            System.out.println("This user already has the maximum of two investments.");
            return; // Exit the method since no more investments can be added
        }

        double maxInvestable = calculateMaxInvestable(user);
        System.out.println("Maximum amount avaliable to invest: $" + String.format("%.2f", maxInvestable));
        if (maxInvestable <= 0) {
            System.out.println("No funds available for investment.");
            return;
        }

        System.out.println("Do you want to invest? (yes/no)");
        scanner.nextLine(); // Consume the newline character
        boolean wantsToInvest = getYesNoInput();
        if (!wantsToInvest) {
            System.out.println("Investment process cancelled.");
            return; // If the user does not want to invest, exit the method
        }

        double year1Deposit = getInitialInvestment(scanner, maxInvestable);
        double year2Deposit = getYearlyInvestment(scanner, 1);
        double year3Deposit = getYearlyInvestment(scanner, 2);
        int coinSelection = getCryptoCurrencySelection(scanner);
        
        Investment investment = new Investment(year1Deposit, year2Deposit, year3Deposit, coinSelection);
        boolean added = user.addInvestment(investment);
        if (!added) {
            System.out.println("Failed to add investment, user can have at most two investments.");
        } else {
            System.out.println("Investment added successfully!");
            writeUsersToFile(); // Update the file with the latest users list
        }
    }

    // delete user
    private void deleteUser() {
        System.out.println("Enter the name of the user to delete:");
        String name = scanner.next(); // read the user's name.

        boolean isDeleted = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getName().equalsIgnoreCase(name)) {
            /* comparing two strings to check if they are equal
               users.get(i) - accesses the i th element of the users list
               .equalsIgnoreCase(name) - compares the name obtained from the User object
               with the name string provided by the user input
            */
                users.remove(i); // remove user from the list
                isDeleted = true;
                break;
            }
        }

        if (isDeleted)
            System.out.println("The user has been deleted successfully");
        else
            System.out.println("User not found");
    }

    // display specific user's info
    private void displayUser() {
        System.out.println("Enter the name of the user to display: ");
        String name = scanner.next();

        for (User user : users) {
            if (user.getName().equalsIgnoreCase(name)) {
                System.out.println("============USER DETAILS============");
                System.out.println("NAME: " + user.getName());
                System.out.println("ANNUAL SALARY: $" + user.getAnnualSalary());
                System.out.println("RESIDENT: " + (user.isResident() ? "YES" : "NO") + "\n");
                System.out.println("- CRYPTOCURRENCY: ");
                System.out.println("BUYING PRICE: $" + String.format("%.2f", user.getBuyingPrice()));
                System.out.println("SELLING PRICE: $" + String.format("%.2f", user.getSellingPrice()));
                System.out.println("YEARS: " + user.getYears() + "\n");
                System.out.println("- Capital Gains Tax: ");
                System.out.println("TAX RATE: " + user.getTaxRate());
                System.out.println("CGT: $" + String.format("%.2f", user.getCapitalGainsTax()));
                System.out.println("PROFIT: $" + String.format("%.2f", user.getActualProfit()) + "\n");

                // Check if the user has investments and display investment results
                List<Investment> investments = user.getInvestments();
                if (!investments.isEmpty()) {
                    int investmentCounter = 1;
                    for (Investment investment : investments) {
                        Investment.InvestmentResult result = investment.calcInvestment();
                        System.out.println("- INVESTMENT " + investmentCounter + " PROFIT");
                        System.out.println("Year\tYearly Profit\tTotal Profit");
                        System.out.printf("1\t\t$%.2f\t\t\t$%.2f\n", result.year1Profit, result.year1Profit);
                        double totalProfitAfterYear2 = result.year1Profit + result.year2Profit;
                        System.out.printf("2\t\t$%.2f\t\t\t$%.2f\n", result.year2Profit, totalProfitAfterYear2);
                        double totalProfitAfterYear3 = totalProfitAfterYear2 + result.year3Profit;
                        System.out.printf("3\t\t$%.2f\t\t\t$%.2f\n", result.year3Profit, totalProfitAfterYear3);
                        System.out.println("Total Investment Profit: $" + String.format("%.2f", result.totalProfit) + "\n");
                        investmentCounter++;
                    }
                } else {
                    System.out.println("No investments.");
                }
                System.out.println("====================================\n\n");
                return; // Exit the method after displaying the user info
            }
        }
        // If we reach this point, the user was not found
        System.out.println("User not found.");
    }
    
    // display all the users we have
    private void displayAllUsers() {
        if (users.isEmpty()) {
            System.out.println("No users.");
        } else {
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                System.out.println("==============ALL USERS==============");
                System.out.println("User " + (i + 1) + ":");
                System.out.println("\tName: " + user.getName());
                System.out.println("\tResident: " + (user.isResident() ? "Yes" : "No"));
                System.out.println("\tAnnual Salary: $" + String.format("%.2f", user.getAnnualSalary()));
                System.out.println("\tActual Profit: $" + String.format("%.2f", user.getActualProfit()));

                // Display the number of investments
                int numberOfInvestments = user.getInvestments().size();
                if (numberOfInvestments > 0) {
                    System.out.println("\tNumber of investments: " + numberOfInvestments);
                } else {
                    System.out.println("\tNo investment");
                }
                System.out.println(); // Add an empty line for better readability
            }
        }
    }

    // function that can write all user details in a txt file
    private void writeUsersToFile() {
        // create a new txt file
        String filename = "users_details.txt";

        try (FileWriter writer = new FileWriter(filename, false)) {
            for (User user : users) {
            /*
                for (User user : users) :
                next element in the users collection is assigned to the variable user, which is of type User
                this means that inside the loop, user represents the current User object being processed.
             */
                writer.write("Name: " + user.getName() + "\n");
                writer.write("Resident: " + (user.isResident() ? "Yes" : "No") + "\n");
                writer.write("Annual Salary: $" + String.format("%.2f", user.getAnnualSalary()) + "\n");
                writer.write("Actual Profit: $" + String.format("%.2f", user.getActualProfit()) + "\n");

                List<Investment> investments = user.getInvestments();
                writer.write("Number of investments: " + investments.size() + "\n");
                for (Investment investment : investments) {
                    Investment.InvestmentResult result = investment.calcInvestment();
                    writer.write("\t====== INVESTMENT PROFIT ======\n");
                    writer.write("\tYear\tYearly Profit\tTotal Profit\n");
                    double totalProfitAfterYear2 = result.year1Profit + result.year2Profit;
                    double totalProfitAfterYear3 = totalProfitAfterYear2 + result.year3Profit;
                    writer.write(String.format("\t1\t$%.2f\t\t$%.2f\n", result.year1Profit, result.year1Profit));
                    writer.write(String.format("\t2\t$%.2f\t\t$%.2f\n", result.year2Profit, totalProfitAfterYear2));
                    writer.write(String.format("\t3\t$%.2f\t\t$%.2f\n", result.year3Profit, totalProfitAfterYear3));
                    writer.write(String.format("\tTotal Investment Profit: $%.2f\n", result.totalProfit));
                }
                writer.write("\n"); // Add an empty line for readability
            }
        } catch (IOException e) {
            /*
                (IOException e) means that the catch block is designed to handle exceptions of type IOException
                e: is the exception object
             */
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    // delete user's investment
    private void deleteInvestment() {
        System.out.println("Enter the name of the user to delete investment:");
        String name = scanner.next(); // read the user's name. using next() instead of nextLine() to avoid reading the newline character
        User user = findUserByName(name); // call the findUserByName method to find the user
        if (user == null) {
            System.out.println("User not found."); // if user not found, show this message
            return;
        }

        // check if the user has investments
        if (user.getInvestments().isEmpty()) {
            System.out.println("No investments to delete.");
            return;
        }

        int investmentCount = getInvestmentNumber(); // get the investment number to delete
        if (investmentCount < 1 || investmentCount > user.getInvestments().size()) {
            System.out.println("The investment does not exist."); // check investment number is valid
        }

        // remove the investment
        user.getInvestments().remove(investmentCount - 1); // remove the investment from the list
        System.out.println("Investment deleted successfully.");
        writeUsersToFile(); // update the file with the latest users list
    }

    private int getInvestmentNumber() {
        while (true) {
            System.out.println("Enter the number of the investment to delete (1 or 2):");
            int number = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
            if (number == 1 || number == 2) {
                return number;
            } else {
                System.out.println("Invalid input. Please enter 1 or 2.");
            }
        }
    }

    /*
        all error handling methods
            - PositiveDouble value
            - Boolean
            - PositiveInteger
            - make sure selling price is greater than buying price
     */
    private double getPositiveDoubleInput(String prompt) {
        double input = 0.0;
        boolean valid = false;
        while (!valid) {
            System.out.println(prompt);
            if (scanner.hasNextDouble()) {
                input = scanner.nextDouble();
                if (input > 0) {
                    valid = true;
                } else {
                    System.out.println("Input must be a positive number. Please try again.");
                    scanner.nextLine();
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
        // clear newline character
        scanner.nextLine();
        return input;
    }

    // getYes or no
    private boolean getYesNoInput() {
        String input;
        while (true) {
            input = scanner.nextLine().trim().toLowerCase();
            if ("yes".equals(input) || "y".equals(input))
                return true;
            else if ("no".equals(input) || "n".equals(input))
                return false;
            else
                System.out.println("Invalid input. Please enter 'yes' 'y' or 'no' 'n'");
        }
    }

    // PositiveInteger
    private int getPositiveIntegerInput(String prompt) {
        int input;
        while (true) {
            System.out.println(prompt);
            try {
                input = scanner.nextInt();
                if (input > 0) {
                    break;
                } else {
                    System.out.println("Input must be a positive integer.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next(); // Consume the invalid input
            }
        }
        scanner.nextLine(); // Consume newline
        return input;
    }

    // make sure selling price is greater than buying price
    private double getSellingPriceInput(String prompt, double buyingPrice) {
        double sellingPrice;
        do {
            sellingPrice = getPositiveDoubleInput(prompt);
            if (sellingPrice <= buyingPrice)
                System.out.println("Selling price must be greater than buying price");
        } while (sellingPrice <= buyingPrice);
        return sellingPrice;
    }

    // methods to find user by name
    private User findUserByName(String name) {
        for (User user : users) {
            if (user.getName().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }

    // max investable amount
    private double calculateMaxInvestable(User user) {
        double totalInvestedInFirstYear = user.getInvestments().stream()
                                               .mapToDouble(Investment::getYear1Deposit)
                                               .sum();
        return user.getActualProfit() - totalInvestedInFirstYear;
    }
    
    /*
        all the methods to collect input for investment calculation
            - getInitialInvestment
            - getCryptoCurrencySelection
            - getYearlyInvestment
     */
    private double getInitialInvestment(Scanner scanner, double maxInvestable) {
        double year1Deposit;
        do {
            System.out.println("Enter the amount you want to invest in the first year: ");
            while (!scanner.hasNextDouble()) {
                System.out.println("Invalid input. Please enter a positive number.");
                scanner.next(); // to move scanner cursor to the next line
            }
            year1Deposit = scanner.nextDouble();
            scanner.nextLine(); // Consume the newline character after the number
            if (year1Deposit <= 0) {
                System.out.println("Invalid input. The amount must be a positive number.");
            } else if (year1Deposit > maxInvestable) {
                System.out.println("Invalid input. The amount cannot exceed $" + String.format("%.2f", maxInvestable) + ".");
            }
        } while (year1Deposit <= 0 || year1Deposit > maxInvestable);
        return year1Deposit;
    }
    

    private int getCryptoCurrencySelection(Scanner scanner) {
        int selection;
        do {
            System.out.println("Select cryptocurrency:");
            System.out.println("1. Bestcoin\n2. Simplecoin\n3. Fastcoin");
            selection = scanner.nextInt();
            if (selection < 1 || selection > 3) {
                System.out.println("Invalid selection. Please choose 1, 2, or 3.");
            }
        } while (selection < 1 || selection > 3);
        return selection;
    }
    
    // get yearly investment
    private double getYearlyInvestment(Scanner scanner, int year) {
        double yearlyInvestment;
        do {
            System.out.printf("Enter the additional investment amount for year %d (positive number or zero):%n", year + 1); // Adjusted year in prompt
            while (!scanner.hasNextDouble()) {
                System.out.println("Invalid input. Please enter a positive number or zero.");
                scanner.next(); // to move scanner cursor to the next line
            }
            yearlyInvestment = scanner.nextDouble();
            if (yearlyInvestment < 0) {
                System.out.println("Invalid input. Please enter a positive number or zero.");
            }
        } while (yearlyInvestment < 0);
        return yearlyInvestment;
    }
}