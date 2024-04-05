package src;
/*Author: (T7) Yiyuan Li, Tian zhiran
 *Student No: C3434681, C3494501
 *Date: 04-04-2024
 */
import java.util.List;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;


public class CgtInterface {
    /**
     * The user currently interacting with the interface.
     */
    private final User user;
    /**
     * Scanner object used to read input from the user.
     * Made private to adhere to the principles of encapsulation, and to maintain security and integrity.
     */
    private final Scanner scanner = new Scanner(System.in);
    /**
     * List of all users interacting with the interface.
     */
    private final List<User> users = new ArrayList<>();

    public void run() {
        /**
         * This method runs the main loop of the application.
         * It displays a menu to the user with the following options:
         * 1. Add user
         * 2. Add user's investment
         * 3. Delete user
         * 4. Delete user's investment
         * 5. Check specific user's details
         * 6. Display all users
         * 7. Exit
         *
         * The user is prompted to select an option, and the corresponding action is performed.
         * If the user selects an invalid option, they are informed and prompted again.
         * The loop continues until the user chooses to exit.
         */
        while (true) {
            // main menu
            System.out.println("Please Select option:");
            System.out.println("(1). Add user");
            System.out.println("(2). Add user's investment");
            System.out.println("(3). Delete user");
            System.out.println("(4). Delete user's investment");
            System.out.println("(5). Check specific user's details");
            System.out.println("(6). Display all users");
            System.out.println("(7). Save to file");
            System.out.println("(8). Exit");
            int choice = getPositiveIntegerInput("Select a number: ");

            // user selection
            switch (choice) {
                case 1:
                // add user
                    if (users.size() < 5)
                        addUserAndProcess();
                    else
                        System.out.println("Cannot add more users. Maximum 5 users");
                    System.out.printf("Press Enter to return to the main menu...");
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
                    scanner.nextLine(); // Wait for the user to press Enter
                    break;
                case 3:
                // delete user
                    deleteUser();
                    System.out.println("Press Enter to return to the main menu...");
                    scanner.nextLine(); // Wait for the user to press Enter
                    break;
                case 4:
                // delete investment
                    deleteInvestment();
                    System.out.println("Press Enter to return to the main menu...");
                    scanner.nextLine(); // Wait for the user to press Enter
                    break;
                case 5:
                // display user
                    displayUser();
                    System.out.println("Press Enter to return to the main menu...");
                    scanner.nextLine(); // Wait for the user to press Enter
                    break;
                case 6:
                // display all users
                    displayAllUsers();
                    System.out.println("Press Enter to return to the main menu...");
                    scanner.nextLine(); // Wait for the user to press Enter
                    break;
                case 7:
                // save to file
                    writeUsersToFile();
                    System.out.println("Save file successfully!");
                    System.out.println("Press Enter to return to the main menu...");
                    scanner.nextLine(); // Wait for the user to press Enter
                    break;
                case 8:
                // exit
                    System.out.println("Exiting program...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

        }
    }

    /**
     * Constructor for the CgtInterface class.
     * 
     * Initializes a new User object and assigns it to the user field.
     */
    public CgtInterface() {
        this.user = new User();
    }
    
    // main function
    public static void main(String[] args){
        CgtInterface cgtInterface = new CgtInterface();
        cgtInterface.run();
    }

    /**
     * This method adds a new user and processes their information.
     * 
     * It creates a new User object and prompts the user to enter their details: name, annual salary, residency status, buying price of the cryptocurrency, selling price of the cryptocurrency, and the number of years the cryptocurrency is held.
     * 
     * After the user enters their details, the method calculates the capital gains tax for the user.
     * 
     * The new User object is then added to the users list, the user is informed that they have been added successfully, and the users list is updated in the file.
     */
    private void addUserAndProcess() {
        User newUser = new User();
        /* 
        * a short delay to ensure that the output gives the user time to react to the output
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

    }

    /**
     * This method adds an investment to a given user.
     * 
     * It first checks if the user already has the maximum of two investments. If so, it informs the user and exits.
     * Then, it calculates the maximum amount the user can invest and displays it. If the maximum investable amount is less than or equal to zero, it informs the user and exits.
     * 
     * The user is then asked if they want to invest. If they choose not to, the method exits.
     * 
     * If the user chooses to invest, they are prompted to enter the details of the investment: the initial investment, the yearly investments for the next two years, and the cryptocurrency selection.
     * 
     * An Investment object is created with these details and added to the user's investments. If the addition is successful, the user is informed and the users list is updated in the file. If the addition is not successful, the user is informed.
     * 
     * @param user The user to whom the investment is to be added.
     */
    private void addInvestmentToUser(User user) {
        // Check if the user already has two investments
        if (user.getInvestments().size() >= 2) {
            System.out.println("This user already has the maximum of two investments.");
            return; // Exit the method since no more investments can be added
        }

        double maxInvestable = calculateMaxInvestable(user);
        System.out.println("Maximum amount avaliable to invest: $" + String.format("%.2f", maxInvestable)); // Display the maximum amount available to invest
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

        // Get the investment details
        double year1Deposit = getInitialInvestment(scanner, maxInvestable);
        double year2Deposit = getYearlyInvestment(scanner, 1);
        double year3Deposit = getYearlyInvestment(scanner, 2);
        int coinSelection = getCryptoCurrencySelection(scanner);
        
        Investment investment = new Investment(year1Deposit, year2Deposit, year3Deposit, coinSelection);
        boolean added = user.addInvestment(investment);
        if (!added) {
            System.out.println("Failed to add investment, user can have at most two investments."); // If the investment was not added
        } else {
            System.out.println("Investment added successfully!");
        }
    }

    /**
     * This method deletes a user from the list of users.
     * 
     * It prompts the user to enter the name of the user to delete. It then iterates over the list of users, and if it finds a user with the entered name, it removes that user from the list.
     * 
     * After the iteration, it checks if a user has been deleted. If a user has been deleted, it informs the user of the successful deletion. If no user has been deleted, it informs the user that the user was not found.
     */
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

        // check if the user has been deleted
        if (isDeleted)
            System.out.println("The user has been deleted successfully");
        else
            System.out.println("User not found");
    }

    /**
     * This method displays the details of a specific user and their investments.
     * 
     * It prompts the user to enter the name of the user to display. It then loops through the users list to find the user with the entered name.
     * 
     * If the user is found, it prints the user's details to the console. The details include the user's name, annual salary, resident status, buying price, selling price, years, tax rate, capital gains tax, actual profit, and the details of each of their investments.
     * 
     * If the user has investments, it calculates the investment result for each investment and prints the yearly profit and total profit for each year, and the total investment profit.
     * 
     * If the user has no investments, it prints a message to the console.
     * 
     * If the user is not found, it prints a message to the console.
     */
    private void displayUser() {
        System.out.println("Enter the name of the user to display: ");
        String name = scanner.next();

        // loop through the users list to find the user
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
    
    /**
     * This method displays the details of all users and their investments.
     * 
     * If there are no users, it prints a message to the console.
     * 
     * If there are users, it iterates over each user and prints their details to the console. The details include the user's name, resident status, annual salary, actual profit, and the number of investments.
     * 
     * For each user, if they have investments, it prints the number of investments. If they have no investments, it prints a message to the console.
     */
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

    /**
     * This method writes the details of all users and their investments to a file.
     * 
     * It creates a new file named "users_details.txt" and writes the details of each user and their investments to this file.
     * 
     * For each user, it writes the user's name, resident status, annual salary, actual profit, and the number of investments.
     * 
     * For each investment of the user, it calculates the investment result and writes the yearly profit and total profit for each year, and the total investment profit.
     * 
     * If an IOException occurs while writing to the file, it prints an error message to the console.
     */
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

    /**
     * This method deletes an investment for a specific user.
     * 
     * It first prompts the user to enter the name of the user for whom the investment is to be deleted.
     * 
     * If the user is not found or the user has no investments, a message is printed to the console and the method returns.
     * 
     * If the user is found and has investments, it prompts the user to enter the number of the investment to delete.
     * 
     * If the entered investment number is valid, the investment is removed from the user's investments, a success message is printed to the console, and the users list is updated in the file.
     * 
     * If the entered investment number is not valid, a message is printed to the console.
     */
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
        
        // display the user's investments
        int investmentCount = getInvestmentNumber(); // get the investment number to delete
        if (investmentCount < 1 || investmentCount > user.getInvestments().size()) {
            System.out.println("The investment does not exist."); // check investment number is valid
        }

        // remove the investment
        user.getInvestments().remove(investmentCount - 1); // remove the investment from the list
        System.out.println("Investment deleted successfully.");
    }


    /**
     * This method prompts the user to enter the number of the investment they wish to delete and validates the input.
     * 
     * It repeatedly prompts the user until a valid input is provided. A valid input is either 1 or 2.
     * 
     * If the user enters an invalid input (a number other than 1 or 2), they are informed and prompted again.
     * 
     * @return The valid investment number entered by the user.
     */
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

    /**
     * This method prompts the user to enter a positive double value and validates the input.
     * 
     * It repeatedly prompts the user until a valid input is provided. A valid input is a positive double value.
     * 
     * If the user enters an invalid input (a non-number or a non-positive number), they are informed and prompted again.
     * 
     * @param prompt The prompt to display to the user.
     * @return The valid positive double value entered by the user.
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

    /**
     * This method prompts the user to enter a yes/no response and validates the input.
     * 
     * It repeatedly prompts the user until a valid input is provided. A valid input is 'yes', 'y', 'no', or 'n', case insensitive.
     * 
     * If the user enters an invalid input, they are informed and prompted again.
     * 
     * @return true if the user enters 'yes' or 'y', false if the user enters 'no' or 'n'.
     */
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

    /**
     * This method prompts the user to enter a positive integer and validates the input.
     * 
     * It repeatedly prompts the user until a valid input is provided. A valid input is a positive integer.
     * 
     * If the user enters an invalid input (a non-integer or a non-positive integer), they are informed and prompted again.
     * 
     * @param prompt The prompt to display to the user.
     * @return The valid positive integer entered by the user.
     * @throws InputMismatchException if the user enters a non-integer.
     */
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

    /**
     * This method prompts the user to enter the selling price of a cryptocurrency and validates the input.
     * 
     * It repeatedly prompts the user until a valid input is provided. A valid input is a positive number that is greater than the buying price of the cryptocurrency.
     * 
     * If the user enters an invalid input (a non-number, a negative number, or a number that is less than or equal to the buying price), they are informed and prompted again.
     * 
     * @param prompt The prompt to display to the user.
     * @param buyingPrice The buying price of the cryptocurrency.
     * @return The valid selling price entered by the user.
     */
    private double getSellingPriceInput(String prompt, double buyingPrice) {
        double sellingPrice;
        do {
            sellingPrice = getPositiveDoubleInput(prompt);
            if (sellingPrice <= buyingPrice)
                System.out.println("Selling price must be greater than buying price");
        } while (sellingPrice <= buyingPrice);
        return sellingPrice;
    }

    /**
     * This method prompts the user to enter the initial investment amount and validates the input.
     * 
     * It repeatedly prompts the user until a valid input is provided. A valid input is a positive number that does not exceed the maximum investable amount.
     * 
     * If the user enters an invalid input (a non-number, a negative number, or a number that exceeds the maximum investable amount), they are informed and prompted again.
     * 
     * @param scanner The Scanner object to read the user's input.
     * @param maxInvestable The maximum amount that the user can invest.
     * @return The valid initial investment amount entered by the user.
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
    
    /**
     * This method prompts the user to select a cryptocurrency and validates the input.
     * 
     * It repeatedly prompts the user until a valid input is provided. A valid input is an integer between 1 and 3, inclusive.
     * 
     * If the user enters an invalid input (a non-number or a number outside the range), they are informed and prompted again.
     * 
     * @param scanner The Scanner object to read the user's input.
     * @return The valid cryptocurrency selection entered by the user.
     */
    private int getCryptoCurrencySelection(Scanner scanner) {
        int selection;
        do {
            System.out.println("Select cryptocurrency:");
            System.out.println("1. Bestcoin\n2. Simplecoin\n3. Fastcoin");
            selection = scanner.nextInt(); // This is necessary because nextInt() does not consume the newline character
            if (selection < 1 || selection > 3) {
                System.out.println("Invalid selection. Please choose 1, 2, or 3.");
            }
        } while (selection < 1 || selection > 3);
        return selection;
    }
    
    /**
     * This method prompts the user to enter the additional investment amount for a specific year and validates the input.
     * 
     * It repeatedly prompts the user until a valid input is provided. A valid input is a positive number or zero.
     * 
     * If the user enters an invalid input (a non-number or a negative number), they are informed and prompted again.
     * 
     * @param scanner The Scanner object to read the user's input.
     * @param year The year for which the investment amount is to be entered.
     * @return The valid investment amount entered by the user.
     */
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

    /**
     * This method finds and returns a User object from the users list based on the provided name.
     * 
     * It iterates over the list of users, and if it finds a user with a name that matches (ignoring case) the provided name, it returns that user.
     * 
     * If no user is found with the provided name, it returns null.
     * 
     * @param name The name of the user to find.
     * @return The User object with the provided name, or null if no such user is found.
     */
    private User findUserByName(String name) {
        for (User user : users) {
            if (user.getName().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }

    /**
     * This method calculates and returns the maximum amount that the given user can invest.
     * 
     * It first calculates the total amount that the user has invested in the first year across all their investments.
     * 
     * It then subtracts this total from the user's actual profit to calculate the maximum amount that the user can invest, and returns this value.
     * 
     * @param user The user for whom to calculate the maximum investable amount.
     * @return The maximum amount that the user can invest.
     */
    private double calculateMaxInvestable(User user) {
        // Calculate the total amount invested in the first year
        double totalInvestedInFirstYear = user.getInvestments().stream() // Get a stream of the user's investments
                                               .mapToDouble(Investment::getYear1Deposit) // Get the year1Deposit value for each investment
                                               .sum(); // Sum all the year1Deposit values
        return user.getActualProfit() - totalInvestedInFirstYear;
    }
}