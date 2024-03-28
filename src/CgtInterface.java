package src;
/*Author: (T7) Yiyuan Li, Tian zhiran
 *Student No: C3434681, C3494501
 *Date: 26-03-2024
 */
import java.util.List;
import java.util.InputMismatchException;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.ArrayList;


public class CgtInterface {

    private final User user;
    private Scanner scanner = new Scanner(System.in);
    private List<User> users = new ArrayList<>();

    public CgtInterface() {
        this.user = new User();
    }

    public void run() {

        while (true) {
            System.out.println("Select option:");
            System.out.println("1. Add user and process");
            System.out.println("2. Delete user");
            System.out.println("3. Display user");
            System.out.println("4. Display all users");
            System.out.println("5. Exit");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    if (users.size() < 5)
                        addUserAndProcess();
                    else
                        System.out.println("Cannot add more users. Maximum 5 users");
                    break;
                case 2:
                    // TODO: deleting user
                    break;
                case 3:
                    // TODO: displaying a specific user
                    break;
                case 4:
                    // TODO: displaying all user
                case 5:
                    System.out.println("Exiting program...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

        }
    }

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
            System.out.println("How many investments do you want to add? (1 or 2)");
            int numInvestment = getPositiveIntegerInput("Enter number of investments: (1 or 2)");
            for (int i = 0; i < numInvestment; i++) {
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
        System.out.println("User added successfully.");

    }

    // error handling - PositiveDouble
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

    // error handling - boolean
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

    // error handling - PositiveInteger
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

    // error handling - make sure selling price is greater than buying price
    private double getSellingPriceInput(String prompt, double buyingPrice) {
        double sellingPrice = 0.0;
        do {
            sellingPrice = getPositiveDoubleInput(prompt);
            if (sellingPrice <= buyingPrice)
                System.out.println("Selling price must be greater than buying price");
        } while (sellingPrice <= buyingPrice);
        return sellingPrice;
    }

    // output Details, CGT, CRYPTOCURRENCY
    private void outputDetails(String name, boolean resident, double annualSalary
            , double buyingPrice, double sellingPrice, int years){

        // format boolean
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        String residentStatus;
        if (resident){
            residentStatus = "YES";
        } else {
            residentStatus = "NO";
        }

        System.out.println("==========USER DETAILS==========");
        System.out.println("NAME: " + name);
        System.out.println("ANNUAL SALARY: $" + annualSalary);
        System.out.println("RESIDENT: " + residentStatus);
        System.out.println();
        System.out.println("=========CRYPTOCURRENCY=========");
        System.out.println("BUYING PRICE: $" + buyingPrice);
        System.out.println("SELLING PRICE: $" + sellingPrice);
        System.out.println("YEARS: " + years);
        System.out.println();
        System.out.println("=====CAPITAL GAINS TAX(CGT)=====");
        System.out.println("TAX RATE: " + user.getTaxRate());
        System.out.println("CGT: $" + df.format(user.getCapitalGainsTax()));
        System.out.println("PROFIT: $" + user.getActualProfit());
    }

    // Methods to collect input for predicted profit calculation
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

    public void displayInvestmentResults() {
        Investment.InvestmentResult result = user.getInvestAccount().calcInvestment();

        System.out.println();
        System.out.println("====== INVESTMENT PROFIT =======");
        System.out.println("Year\tYearly Profit\t\tTotal Profit");
        System.out.printf("1\t\t$%.2f\t\t\t$%.2f\n", result.year1Profit, result.year1Profit);
        double totalProfitAfterYear2 = result.year1Profit + result.year2Profit;
        System.out.printf("2\t\t$%.2f\t\t\t$%.2f\n", result.year2Profit, totalProfitAfterYear2);
        double totalProfitAfterYear3 = totalProfitAfterYear2 + result.year3Profit;
        System.out.printf("3\t\t$%.2f\t\t\t$%.2f\n", result.year3Profit, totalProfitAfterYear3);
    }

    // main
    public static void main(String[] args){
        CgtInterface cgtInterface = new CgtInterface();
        cgtInterface.run();
    }


}