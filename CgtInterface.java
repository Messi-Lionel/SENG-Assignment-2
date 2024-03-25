/*Author: (T7) Yiyuan Li, Tian Zhiran
 *Student No: C3434681, C3494501
 *Date: 26-01-2024
 *Description: [Yiyuan] this structure makes the CgtInterface class more modular,
 *                      with each individual function encapsulated in its own method
 *                      which helps with code maintenance and understanding.
 */

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Scanner;


public class CgtInterface {

    private final User user;

    public CgtInterface() {
        this.user = new User();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        // calling method, get users name
        String name = getName(scanner);
        user.setName(name);
        // calling method, get users annualSalary
        double salary = getAnnualSalary(scanner);
        user.setAnnualSalary(salary);

        // calling method, ask user is resident or not
        boolean isResident = getResident(scanner);
        user.setResident(isResident);

        // calling method, ask user for buying price
        double buyingPrice = getBuyingPrice(scanner);
        user.setBuyingPrice(buyingPrice);

        // calling method, ask user for selling price
        // pass buyingPrice to method, check if sellingPrice is greater than buying price
        double sellingPrice = getSellingPrice(scanner, buyingPrice);
        user.setSellingPrice(sellingPrice);

        // get years
        int years = getYears(scanner);
        user.setYears(years);

        user.calcCgt(); // Calculate CGT

        // Collect investment details after setting selling price
        double year1Deposit = getInitialInvestment(scanner);
        double year2Deposit = getYearlyInvestment(scanner, 1);
        double year3Deposit = getYearlyInvestment(scanner, 2);
        int coinSelection = getCryptoCurrencySelection(scanner);
        user.setInvestCoinSelection(coinSelection);

        // Initialize the investment account
        Investment investment = new Investment(year1Deposit, year2Deposit, year3Deposit, user.getInvestCoinSelection());
        user.setInvestAccount(investment);

        // Perform calculations
        Investment.InvestmentResult result = investment.calcInvestment(); // Calculate investment results

        // Display results
        outputDetails(name, isResident, salary, buyingPrice, sellingPrice, years);
        displayInvestmentResults();

        scanner.close();

    }

    private static String getName(Scanner scanner){
        System.out.println("Enter your name: ");
        return scanner.nextLine();
    }

    // method to check user is resident or not, return a boolean value
    private static boolean getResident(Scanner scanner){
        while (true) {
            System.out.println("Are you a resident? (y/n)");
            String input = scanner.next().trim().toLowerCase(); // turn user input to lowercase for checking
            // check is y or n
            if (input.length() == 1 && input.equals("y")) {
                return true;
            } else if (input.length() == 1 && input.equals("n")) {
                return false;
            } else {
                System.out.println("Invalid input. Please answer 'y' or 'n'.");
            }
        }
    }

    // method to get the user's annual salary, returning double
    private double getAnnualSalary(Scanner scanner){
        double salary = 0;
        boolean validInput = false;
        while (!validInput) {
            System.out.println("Enter your annual salary (as a positive number):");
            if (scanner.hasNextDouble()) {
                salary = scanner.nextDouble();
                if (salary > 0) {
                    validInput = true;
                } else {
                    System.out.println("Invalid input. Please enter a correct number.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // clear input
            }
        }
        return salary;
    }

    // method to get crypto buying price
    private double getBuyingPrice(Scanner scanner){
        double buyingPrice = 0;
        boolean validInput = false;
        while (!validInput) {
            System.out.println("Enter your buying price of the cryptocurrency:");
            if (scanner.hasNextDouble()){
                buyingPrice = scanner.nextDouble();
                if (buyingPrice > 0) {
                    validInput = true;
                } else {
                    System.out.println("Invalid input. Please enter a correct number.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // clear input
            }
        }
        return buyingPrice;
    }

    // method to get crypto selling price & check if sellingPrice is greater than buying price
    private double getSellingPrice(Scanner scanner, double buyingPrice){
        double sellingPrice = 0;
        boolean validInput = false;
        while (!validInput){
            System.out.println("Enter the selling price of th cryptocurrency");
            System.out.println("It must greater than the buying price:");
            if (scanner.hasNextDouble()){
                sellingPrice = scanner.nextDouble();
                if (sellingPrice > buyingPrice) {
                    validInput = true;
                } else {
                    System.out.println("Invalid input. The selling price must be greater than the buying price.");
                }
            } else {
                System.out.println("Invalid input. Please a enter number");
                scanner.next(); // clear input
            }
        }
        return sellingPrice;
    }

    // method to get holding years
    private int getYears(Scanner scanner){
        int years = 0;
        boolean validInput = false;
        while (!validInput){
            System.out.println("How many years have you held these cryptocurrencies? (Enter a Integer number)");
            if (scanner.hasNextInt()){
                years = scanner.nextInt();
                if (years > 0){
                    validInput = true;
                } else {
                    System.out.println("Invalid input. The number must be greater than 0");
                }
            } else {
                System.out.println("Invalid input. Please enter a number");
                scanner.next(); // clear input
            }
        }
        return years;
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
            System.out.println("Enter the amount you want to invest in the first year (cannot more than $" + user.getActualProfit() + "): ");
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

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
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