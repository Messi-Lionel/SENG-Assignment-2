package src;
/*Author: (T7) Yiyuan Li, Tian zhiran
 *Student No: C3434681, C3494501
 *Date: 04-04-2024
 */
public class Investment {
    /**
     * The deposit amount for the first year.
     */
    private final double year1Deposit;

    /**
     * The deposit amount for the second year.
     */
    private final double year2Deposit;

    /**
     * The deposit amount for the third year.
     */
    private final double year3Deposit;

    /**
     * The selected cryptocurrency coin. 1 for BestCoin, 2 for SimpleCoin, 3 for FastCoin.
     */
    private int coinSelection;

    /**
     * The interest rate for BestCoin.
     */
    private static final double BESTCOIN_RATE = 0.18;

    /**
     * The interest rate for SimpleCoin.
     */
    private static final double SIMPLECOIN_RATE = 0.12;

    /**
     * The interest rate for FastCoin.
     */
    private static final double FASTCOIN_RATE = 0.15;

    /**
     * Constructor for the Investment class.
     * 
     * Initializes a new Investment object with the given deposit amounts for the first three years and the selected cryptocurrency coin.
     * 
     * @param year1Deposit The deposit amount for the first year.
     * @param year2Deposit The deposit amount for the second year.
     * @param year3Deposit The deposit amount for the third year.
     * @param coinSelection The selected cryptocurrency coin. 1 for BestCoin, 2 for SimpleCoin, 3 for FastCoin.
     */
    public Investment(double year1Deposit, double year2Deposit, double year3Deposit, int coinSelection) {
        this.year1Deposit = year1Deposit;
        this.year2Deposit = year2Deposit;
        this.year3Deposit = year3Deposit;
        this.coinSelection = coinSelection;
    }

    /**
     * This class represents the result of an investment.
     * 
     * It includes the profit for each of the first three years and the total profit.
     */
    public static class InvestmentResult {
        public double year1Profit;
        public double year2Profit;
        public double year3Profit;
        public double totalProfit;

        /**
         * Constructor for the InvestmentResult class.
         * 
         * Initializes a new InvestmentResult object with the given profits for the first three years. The total profit is calculated as the sum of these three profits.
         * 
         * @param year1Profit The profit for the first year.
         * @param year2Profit The profit for the second year.
         * @param year3Profit The profit for the third year.
         */
        public InvestmentResult(double year1Profit, double year2Profit, double year3Profit) {
            this.year1Profit = year1Profit;
            this.year2Profit = year2Profit;
            this.year3Profit = year3Profit;
            this.totalProfit = year1Profit + year2Profit + year3Profit;
        }
    }

    /**
     * This method calculates the profit for each of the first three years and the total profit of the investment.
     * 
     * It first determines the profit rate based on the selected cryptocurrency coin. If the coin selection is invalid, the profit rate is set to 0.
     * 
     * It then calculates the profit for each year. The profit for a year is the sum of the deposits for that year and all previous years, multiplied by the profit rate.
     * 
     * Finally, it returns a new InvestmentResult object with the calculated profits.
     * 
     * @return An InvestmentResult object with the calculated profits for the first three years and the total profit.
     */
    public InvestmentResult calcInvestment() {
        double profitRate = switch (coinSelection) {
            case 1 -> BESTCOIN_RATE;
            case 2 -> SIMPLECOIN_RATE;
            case 3 -> FASTCOIN_RATE;
            default -> 0; // Invalid selection, no profit
        };

        double year1Profit = year1Deposit * profitRate;
        double year2Profit = (year1Deposit + year2Deposit) * profitRate;
        double year3Profit = (year1Deposit + year2Deposit + year3Deposit) * profitRate;

        return new InvestmentResult(year1Profit, year2Profit, year3Profit);
    }

    /**
     * This method returns the selected cryptocurrency coin.
     * 
     * @return The selected cryptocurrency coin. 1 for BestCoin, 2 for SimpleCoin, 3 for FastCoin.
     */
    public int getCoinSelection() {
        return this.coinSelection;
    }

    /**
     * This method returns the deposit amount for the first year.
     * 
     * @return The deposit amount for the first year.
     */
    public double getYear1Deposit() {
        return this.year1Deposit;
    }

    /**
     * This method sets the selected cryptocurrency coin.
     * 
     * @param coinSelection The selected cryptocurrency coin. 1 for BestCoin, 2 for SimpleCoin, 3 for FastCoin.
     */
    public void setCoinSelection(int coinSelection) {
        this.coinSelection = coinSelection;
    }

}