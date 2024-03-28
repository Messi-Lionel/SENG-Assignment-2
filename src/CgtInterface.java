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
    private Scanner scanner = new Scanner(System.in);
    private List<User> users = new ArrayList<>();

    public CgtInterface() {
        this.user = new User();
    }

    public void run() {

        // loop
        while (true) {
            // main menu
            System.out.println("==============CGT CALCULATOR==============");
            System.out.println("Please Select option:");
            System.out.println("(1). Add user");
            System.out.println("(2). Delete user");
            System.out.println("(3). Check specific user's details");
            System.out.println("(4). Display all users");
            System.out.println("(5). Exit");
            System.out.println("============ENTER NUMBER BELOW============");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    if (users.size() < 5)
                        addUserAndProcess();
                    else
                        System.out.println("Cannot add more users. Maximum 5 users");
                    break;
                case 2:
                    deleteUser();
                    break;
                case 3:
                    displayUser();
                    break;
                case 4:
                    displayAllUsers();
                    break;
                case 5:
                    System.out.println("Exiting program...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

        }
    }

    public static void main(String[] args){
        CgtInterface cgtInterface = new CgtInterface();
        cgtInterface.run();
    }

    /*
        - Add user
        - Delete user
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
        scanner.nextLine();

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

        // ask for investment
        System.out.println("Do you want to add an investment? (yes/no):");
        if (getYesNoInput()) {
        int investmentCount = getPositiveIntegerInput("Enter number of investments: (1 or 2)");
            for (int i = 0; i < investmentCount; i++) {
                double year1Deposit = getInitialInvestment(scanner);
                double year2Deposit = getYearlyInvestment(scanner, 1);
                double year3Deposit = getYearlyInvestment(scanner, 2);
                int coinSelection = getCryptoCurrencySelection(scanner);
                Investment investment = new Investment(year1Deposit, year2Deposit, year3Deposit, coinSelection);
                boolean added = newUser.addInvestment(investment);
                if (!added) {
                    System.out.println("Faild to add, investment, user can have most two investments.");
                    break;
                }
            }
        }
        users.add(newUser);
        System.out.println("User added successfully!" + "\n");
        writeUsersToFile(); // Update the file with the latest users list
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
        String filename = "users_details.txt";

        try (FileWriter writer = new FileWriter(filename, false)) {
            for (User user : users) {
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
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }



    /*
        all error handling functions
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
    private int getPositiveIntegerInput(String prompt) {
        int input = 0;
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
    private double getSellingPriceInput(String prompt, double buyingPrice) {
        double sellingPrice = 0.0;
        do {
            sellingPrice = getPositiveDoubleInput(prompt);
            if (sellingPrice <= buyingPrice)
                System.out.println("Selling price must be greater than buying price");
        } while (sellingPrice <= buyingPrice);
        return sellingPrice;
    }

    /*
        all the methods to collect input for investment calculation
            - getInitialInvestment
            - getCryptoCurrencySelection
            - getYearlyInvestment
     */
    private double getInitialInvestment(Scanner scanner) {
        double year1Deposit;
        do {
            System.out.println("Enter the amount you want to invest in the first year: ");
            while (!scanner.hasNextDouble()) {
                System.out.println("Invalid input. Please enter a positive number.");
                scanner.next(); // to move scanner cursor to the next line
            }
            year1Deposit = scanner.nextDouble();
            if (year1Deposit <= 0) {
                System.out.println("Invalid input. The amount must be greater than 0.");
            }
        } while (year1Deposit <= 0);
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