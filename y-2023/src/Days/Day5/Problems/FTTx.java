package Days.Day5.Problems;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class for techno-economic analysis.
 *
 * @author Fernando Otero
 * @version 1.0
 * <p>
 * I (Nikola Kolev) wrote the npv() and loadParameters() methods,
 * added the filename, maxRolloutPeriod, and budget vars,
 * and converted everything to static (=> deleted the constructor).
 */
public class FTTx {
    protected static String householdsFilename = "files/fttxFiles/households.csv";
    public static String parametersFilename = "files/fttxFiles/parameters.csv";
    protected static final double budget = 100000;

    // problem parameters
    /**
     * Number of areas.
     */
    public static int noOfAreas;

    /**
     * Length of the study period.
     */
    protected static int period;

    /**
     * Rental charges per household.
     */
    private static double rental;

    /**
     * CAPEX charges. They only occur once per area in
     * the lifetime of the investment.
     */
    private static double capex;

    /**
     * OPEX charges.
     */
    private static double opex;

    /**
     * The interest rate that will be used for the
     * NPV calculations.
     */
    private static double interest;

    /**
     * The last year that a roll-out can occur.
     */
    public static int maxRolloutPeriod;

    /**
     * The deployment plan. Each cell in the array
     * represents an area and each value the roll-out
     * year for that area.
     */
    private static int[] plan;

    /**
     * Population (number of households) per area.
     */
    public static Integer[] households;

    /**
     * Imitator percentages per area.
     */
    public static Double[] imitator;


    /**
     * Default parameters.
     */
    public static void defaultParams() {
        // default parameters values
        noOfAreas = 3;
        period = 10;
        maxRolloutPeriod = period;
        rental = 2;
        capex = 500;
        opex = 200;
        interest = 0.01;

        // default households and imitator values
        households = new Integer[noOfAreas];
        imitator = new Double[noOfAreas];

        households[0] = 100;
        households[1] = 100;
        households[2] = 1000;

        imitator[0] = 0.2;
        imitator[1] = 0.5;
        imitator[2] = 0.2;

        // default plan (individual / solution)
        plan = new int[noOfAreas];

        plan[0] = 0;
        plan[1] = 2;
        plan[2] = 1;
    }

    /**
     * Returns the NPV value.
     *
     * @return the NPV value.
     */
    protected static double npv() {
        return npv(plan);
    }

    public static double npv(int[] plan) {
        Integer[] householdsLeft = households.clone();
        int[] totalCustomers = new int[noOfAreas];
        double budgetLeft = budget;

        double npv = 0;

        for (int year = 1; year <= period; year++) {
            for (int area = 0; area < noOfAreas; area++) {
                if (plan[area] == 0) continue;

                double cashFlow = 0; // annual profit

                if (year == plan[area]) {
                    double annualCost = capex;
                    cashFlow = -annualCost;
                } else if (year > plan[area]) {
                    // calc new customers per area, based on the adoption rates
                    int newCustomers = (int) Math.floor(householdsLeft[area] * imitator[area]);

                    // add new customers to the total (and remove from the left)
                    totalCustomers[area] += newCustomers;
                    householdsLeft[area] -= newCustomers;

                    // calc annual income (total customers * rental)
                    double annualIncome = totalCustomers[area] * rental;

                    // calc annual expenditure (capex or opex)
                    double annualCost = opex;

                    // calc cash flow (annual income - annual expenditure)
                    cashFlow = annualIncome - annualCost;
                }

                // calc present value (cash flow / (1 + interest)^year)
                double presentValue = cashFlow * Math.pow(1 + interest, -year);

                // add present value to the NPV
                npv += presentValue;

                // subtract the cash flow from the budget
                budgetLeft += cashFlow;

                // if the budget is negative, return -1 (invalid solution)
                if (budgetLeft < 0) return -1;
            }
        }

        return npv;
    }

    public static void loadParameters() throws Exception {
        loadParameters(parametersFilename);
    }

    protected static void loadParameters(String filename) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(filename));

        String line;

        while ((line = reader.readLine()) != null) {
            String[] split = line.split(",");

            switch (split[0]) {
                case "Number of areas" -> noOfAreas = Integer.parseInt(split[1]);
                case "Study period" -> period = Integer.parseInt(split[1]);
                case "Annual rental charges" -> rental = Double.parseDouble(split[1]);
                case "CAPEX" -> capex = Double.parseDouble(split[1]);
                case "OPEX" -> opex = Double.parseDouble(split[1]);
                case "Interest rate" -> interest = Double.parseDouble(split[1]);
                case "Maximum rollout period" -> maxRolloutPeriod = Integer.parseInt(split[1]);
                default -> throw new Exception("Error with the parameters file");
            }
        }
    }

    public static void loadHouseholds() throws IOException {
        loadHouseholds(householdsFilename);
    }

    /**
     * Loads a csv file in memory, skipping the first line (column names).
     * <p>
     *
     * @ param filename the file to load.
     */
    protected static void loadHouseholds(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));

        ArrayList<Integer> col1 = new ArrayList<>();
        ArrayList<Double> col2 = new ArrayList<>();

        // skip the first line (column names)
        reader.readLine();
        String line;

        while ((line = reader.readLine()) != null) {
            String[] split = line.split(",");

            col1.add(Integer.valueOf(split[0]));
            col2.add(Double.valueOf(split[1]));
        }

        noOfAreas = col1.size();
        households = col1.toArray(new Integer[noOfAreas]);
        imitator = col2.toArray(new Double[noOfAreas]);
    }
}