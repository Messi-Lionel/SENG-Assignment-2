package src;
/*Author: (T7) Yiyuan Li, Tian Zhiran
 *Student No: C3434681, C3494501
 *Date: 04-04-2024
 */
import java.util.ArrayList;
import java.util.List;
public class User{

    /**
     * The name of the user.
     */
    private String name;

    /**
     * The investment account of the user.
     */
    private Investment investAccount;

    /**
     * The annual salary of the user.
     */
    private double annualSalary;

    /**
     * The buying price of the user's investment.
     */
    private double buyingPrice;

    /**
     * The selling price of the user's investment.
     */
    private double sellingPrice;

    /**
     * The number of years the user has held the investment.
     */
    private int years;

    /**
     * Whether the user is a resident.
     */
    private boolean resident;

    /**
     * The capital gains tax of the user.
     */
    private double capitalGainsTax;

    /**
     * The actual profit of the user.
     */
    private double actualProfit;

    /**
     * The tax rate of the user.
     */
    private double taxRate;

    /**
     * The list of investments of the user.
     */
    private List<Investment> investments;

    /**
     * This method calculates the capital gains tax (CGT) and the actual profit for the user.
     * 
     * It first calculates the total profit from the selling and buying prices, and the profit for CGT by dividing the total profit by the number of years.
     * 
     * It then calculates the total income by adding the annual salary and the profit for CGT.
     * 
     * It uses the calculateTaxRate method to calculate the tax rate based on the total income and the resident status.
     * 
     * Finally, it calculates the CGT by multiplying the tax rate and the profit for CGT, and the actual profit by subtracting the CGT from the profit for CGT.
     */
    public void calcCgt() {
        double profit = sellingPrice - buyingPrice;
        double profitForCgt = profit / years;
        double totalIncome = annualSalary + profitForCgt;

        // Use the calculateTaxRate method to calculate the tax rate
        taxRate = calculateTaxRate(totalIncome, resident);

        capitalGainsTax = taxRate * profitForCgt;
        actualProfit = profitForCgt - capitalGainsTax;
    }

    /**
     * Constructor for the User class.
     * 
     * Initializes a new User object with an empty list of investments.
     */
    public User() {
        this.investments = new ArrayList<>();
    }

    /**
     * This method calculates the tax rate based on the income and the resident status.
     * 
     * If the user is a resident, the tax rate is determined by the following income brackets:
     * - 0% for income up to $18,200
     * - 19% for income over $18,200 up to $45,000
     * - 32.5% for income over $45,000 up to $120,000
     * - 37% for income over $120,000 up to $180,000
     * - 45% for income over $180,000
     * 
     * If the user is not a resident, the tax rate is determined by the following income brackets:
     * - 32.5% for income up to $120,000
     * - 37% for income over $120,000 up to $180,000
     * - 45% for income over $180,000
     * 
     * @param income The income of the user.
     * @param isResident Whether the user is a resident.
     * @return The tax rate.
     */
    private double calculateTaxRate(double income, boolean isResident) {
        if (isResident) {
            // if income <= 18200, then return 0.0
            return income <= 18200 ? 0.0 :
                    income <= 45000 ? 0.19 :
                            income <= 120000 ? 0.325 :
                                    // else if income <= 180000, return 0.37, else return 0,45
                                    income <= 180000 ? 0.37 : 0.45;
        } else {
            return income <= 120000 ? 0.325 :
                    income <= 180000 ? 0.37 : 0.45;
        }
    }

    /**
     * This method adds an investment to the user's list of investments.
     * 
     * If the user already has two investments, it prints a message to the console and returns false.
     * 
     * If the user has less than two investments, it adds the investment to the list and returns true.
     * 
     * @param investment The investment to add.
     * @return True if the investment was added, false otherwise.
     */
    public boolean addInvestment(Investment investment) {
        if (this.investments.size() < 2) {
            this.investments.add(investment);
            return true;
        } else {
            System.out.println("User can have at most two investments.");
            return false;
        }
    }

    /**
     * This method returns the list of investments of the user.
     * 
     * @return The list of investments of the user.
     */
    public List<Investment> getInvestments() {
        return this.investments;
    }

    /**
     * This method sets the investment account of the user.
     * 
     * @param investAccount The investment account to set.
     */
    public void setInvestAccount(Investment investAccount) {
        this.investAccount = investAccount;
    }

    /**
     * This method returns the name of the user.
     * 
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * This method sets the name of the user.
     * 
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method returns the annual salary of the user.
     * 
     * @return The annual salary of the user.
     */
    public double getAnnualSalary() {
        return annualSalary;
    }

    /**
     * This method sets the annual salary of the user.
     * 
     * @param annualSalary The annual salary to set.
     */
    public void setAnnualSalary(double annualSalary) {
        this.annualSalary = annualSalary;
    }

    /**
     * This method returns whether the user is a resident.
     * 
     * @return True if the user is a resident, false otherwise.
     */
    public boolean isResident() {
        return resident;
    }

    /**
     * This method sets whether the user is a resident.
     * 
     * @param resident True if the user is a resident, false otherwise.
     */
    public void setResident(boolean resident) {
        this.resident = resident;
    }

    /**
     * This method returns the buying price of the user's investment.
     * 
     * @return The buying price of the user's investment.
     */
    public double getBuyingPrice() {
        return buyingPrice;
    }

    /**
     * This method sets the buying price of the user's investment.
     * 
     * @param buyingPrice The buying price to set.
     */
    public void setBuyingPrice(double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    /**
     * This method returns the selling price of the user's investment.
     * 
     * @return The selling price of the user's investment.
     */
    public double getSellingPrice() {
        return sellingPrice;
    }

    /**
     * This method sets the selling price of the user's investment.
     * 
     * @param sellingPrice The selling price to set.
     */
    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    /**
     * This method returns the number of years the user has held the investment.
     * 
     * @return The number of years the user has held the investment.
     */
    public int getYears() {
        return years;
    }

    /**
     * This method sets the number of years the user has held the investment.
     * 
     * @param years The number of years to set.
     */
    public void setYears(int years) {
        this.years = years;
    }

    /**
     * This method sets the coin selection for the user's investment account.
     * 
     * If the user already has an investment account, it sets the coin selection of that account.
     * 
     * If the user does not have an investment account, it initializes a new investment account with the given coin selection and zero deposits for the first three years.
     * 
     * @param coinSelection The coin selection for the investment account. 1 for BestCoin, 2 for SimpleCoin, 3 for FastCoin.
     */
    public void setInvestCoinSelection(int coinSelection) {
        if (this.investAccount != null) {
            this.investAccount.setCoinSelection(coinSelection);
        } else {
            // if investAccount is empty，then initialize it
            this.investAccount = new Investment(0, 0, 0, coinSelection);
        }
    }

    /**
     * This method returns the coin selection for the user's investment account.
     * 
     * If the user has an investment account, it returns the coin selection of that account.
     * 
     * If the user does not have an investment account, it returns -1.
     * 
     * @return The coin selection for the investment account if it exists, -1 otherwise.
     */
    public int getInvestCoinSelection() {
        if (this.investAccount != null) {
            return this.investAccount.getCoinSelection();
        }
        return -1;
    }

    /**
     * This method returns the tax rate of the user.
     * 
     * @return The tax rate of the user.
     */
    public double getTaxRate() {
        return taxRate;
    }

    /**
     * This method returns the capital gains tax of the user.
     * 
     * @return The capital gains tax of the user.
     */
    public double getCapitalGainsTax() {
        return capitalGainsTax;
    }

    /**
     * This method returns the actual profit of the user.
     * 
     * @return The actual profit of the user.
     */
    public double getActualProfit() {
        return actualProfit;
    }

    /**
     * This method returns the investment account of the user.
     * 
     * @return The investment account of the user.
     */
    public Investment getInvestAccount() {
        return investAccount;
    }
}