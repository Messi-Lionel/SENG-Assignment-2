/*Author: (T7) Yiyuan Li, Tian zhiran
 *Student No: C3434681, C3494501
 *Date: 26-03-2024
 */
public class Investment {
    private final double year1Deposit;
    private final double year2Deposit;
    private final double year3Deposit;
    private int coinSelection;
    private static final double BESTCOIN_RATE = 0.18;
    private static final double SIMPLECOIN_RATE = 0.12;
    private static final double FASTCOIN_RATE = 0.15;

    public Investment(double year1Deposit, double year2Deposit, double year3Deposit, int coinSelection) {
        this.year1Deposit = year1Deposit;
        this.year2Deposit = year2Deposit;
        this.year3Deposit = year3Deposit;
        this.coinSelection = coinSelection;
    }

    public static class InvestmentResult {
        public double year1Profit;
        public double year2Profit;
        public double year3Profit;
        public double totalProfit;

        public InvestmentResult(double year1Profit, double year2Profit, double year3Profit) {
            this.year1Profit = year1Profit;
            this.year2Profit = year2Profit;
            this.year3Profit = year3Profit;
            this.totalProfit = year1Profit + year2Profit + year3Profit;
        }
    }

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

    // Getters and setters
    public int getCoinSelection() {
        return this.coinSelection;
    }
    public void setCoinSelection(int coinSelection) {
        this.coinSelection = coinSelection;
    }

}