package src;
/*Author: (T7) Yiyuan Li, Tian Zhiran
 *Student No: C3434681, C3494501
 *Date: 26-03-2024
 */
import java.util.ArrayList;
import java.util.List;
public class User{

    private String name;
    private Investment investAccount;
    private double annualSalary;
    private double buyingPrice;
    private double sellingPrice;
    private int years;
    private boolean resident;
    private double capitalGainsTax;
    private double actualProfit;
    private double taxRate;
    private List<Investment> investments;

    // CGT calculation method
    public void calcCgt() {
        double profit = sellingPrice - buyingPrice;
        double profitForCgt = profit / years;
        double totalIncome = annualSalary + profitForCgt;

        // Use the calculateTaxRate method to calculate the tax rate
        taxRate = calculateTaxRate(totalIncome, resident);

        capitalGainsTax = taxRate * profitForCgt;
        actualProfit = profitForCgt - capitalGainsTax;
    }

    public User() {
        this.investments = new ArrayList<>();
    }

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

    // investment list
    public boolean addInvestment(Investment investment) {
        if (this.investments.size() < 2) {
            this.investments.add(investment);
            return true;
        } else {
            System.out.println("User can have at most two investments.");
            return false;
        }
    }


    // get all users investment
    public List<Investment> getInvestments() {
        return this.investments;
    }

    public void setInvestAccount(Investment investAccount) {
        this.investAccount = investAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAnnualSalary() {
        return annualSalary;
    }

    public void setAnnualSalary(double annualSalary) {
        this.annualSalary = annualSalary;
    }

    public boolean isResident() {
        return resident;
    }

    public void setResident(boolean resident) {
        this.resident = resident;
    }

    public double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public void setInvestCoinSelection(int coinSelection) {
        if (this.investAccount != null) {
            this.investAccount.setCoinSelection(coinSelection);
        } else {
            // if investAccount is empty，then initialize it
            this.investAccount = new Investment(0, 0, 0, coinSelection);
        }
    }

    public int getInvestCoinSelection() {
        if (this.investAccount != null) {
            return this.investAccount.getCoinSelection();
        }
        return -1;
    }

    // Getters to access the results for display in CgtInterface

    public double getTaxRate() {
        return taxRate;
    }

    public double getCapitalGainsTax() {
        return capitalGainsTax;
    }

    public double getActualProfit() {
        return actualProfit;
    }

    public Investment getInvestAccount() {
        return investAccount;
    }
}