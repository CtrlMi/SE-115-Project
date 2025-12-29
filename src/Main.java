// Main.java — Students version
import java.io.*;
import java.util.*;
import java.nio.file.Paths;

public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January","February","March","April","May","June",
                              "July","August","September","October","November","December"};

    static int[][][] profits = new int[MONTHS][DAYS][COMMS];

    // ======== REQUIRED METHOD LOAD DATA (Students fill this) ========
    public static void loadData() {
        for(int month = 0; month < MONTHS ; month++){
            Scanner reader = null;
            try{
                String fileName = "Data_Files/" + months[month] + ".txt";
                reader = new Scanner(Paths.get(fileName));
                reader.nextLine();
                while(reader.hasNextLine()){
                    String line = reader.nextLine();
                    String[] entries = line.split(",");

                    int day = Integer.parseInt(entries[0]);
                    String commodity = entries[1];
                    int profit = Integer.parseInt(entries[2]);

                    for(int i = 0; i < COMMS; i++){
                        if(commodity.equals(commodities[i])){
                            profits[month][day - 1][i] = profit;
                        }
                    }
                }
            } catch (Exception e) {
                continue;
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }

    // ======== 10 REQUIRED METHODS (Students fill these) ========

    public static String mostProfitableCommodityInMonth(int month) {
        if(month < 0 || month > 11) {
            return "INVALID_MONTH";
        }

        int mostProfitable = -100000000;
        int commIndex = 0;

        for (int i = 0; i < COMMS; i++) {
            int total = 0;

            for (int j = 0; j < DAYS; j++) {
                total +=  profits[month][j][i];
            }

            if(total > mostProfitable){
                mostProfitable = total;
                commIndex = i;
            }
        }

        return commodities[commIndex] + " " + mostProfitable;
    }

    public static int totalProfitOnDay(int month, int day) {
        if((day > 28 || day < 1) || month > 11 || month < 0) return -99999;

        int totalProfit = 0;

        for (int i = 0; i < COMMS; i++) {
            totalProfit += profits[month][day-1][i];
        }

        return totalProfit;
    }

    public static int commodityProfitInRange(String commodity, int from, int to) {
        if (from < 1 || to < 1 || to > 28 || from >= to) return -99999;

        int comm_index = -1; // out of bounds

        for (int i = 0; i < COMMS; i++) {
            if (commodity.equals(commodities[i])){
                comm_index = i;
                break;
            }
        }

        if(comm_index == -1) return -99999;

        int total = 0;

        for (int month = 0; month < MONTHS; month++) {
            for (int i = from - 1; i <= to - 1; i++) {
                total += profits[month][i][comm_index];
            }
        }

        return total;
    }

    public static int bestDayOfMonth(int month) {
        if(month < 0 || month > 11) {
            return -1;
        }

        int maxDayProfit = -100000000;
        int total;
        int theDay = 0;

        for (int day = 0; day < DAYS; day++) {
            total = totalProfitOnDay(month, day + 1);

            if(total > maxDayProfit){
                maxDayProfit = total;
                theDay = day;
            }
        }

        return theDay + 1;
    }

    public static String bestMonthForCommodity(String comm) {
        int comm_index = -1;

        for (int i = 0; i < COMMS; i++) {
            if (comm.equals(commodities[i])){
                comm_index = i;
                break;
            }
        }
        if(comm_index == -1) return "INVALID_COMMODITY";

        int maxCommProfit = -100000000;
        int total = 0;
        int monthIndex = 0;

        for (int month = 0; month < MONTHS; month++) {
            total = 0;
            for (int day = 0; day < DAYS; day++) {
                total += profits[month][day][comm_index];
            }
            if(total > maxCommProfit){
                maxCommProfit = total;
                monthIndex = month;
            }
        }

        return months[monthIndex];
    }

    public static int consecutiveLossDays(String comm) {
        int comm_index = -1;

        for (int i = 0; i < COMMS; i++) {
            if (comm.equals(commodities[i])){
                comm_index = i;
                break;
            }
        }

        if(comm_index == -1) return -1;

        int longestStreakMonth = 0;
        int streak  = 0;

        for (int month = 0; month < MONTHS; month++) {
            for (int day = 0; day < DAYS; day++) {
                if(profits[month][day][comm_index] < 0){
                    streak ++;
                    if(streak > longestStreakMonth){
                        longestStreakMonth = streak;
                    }
                }else{
                    streak = 0;
                }
            }
        }

        return longestStreakMonth;
    }

    public static int daysAboveThreshold(String comm, int threshold) {
        int comm_index = -1;

        for (int i = 0; i < COMMS; i++) {
            if (comm.equals(commodities[i])){
                comm_index = i;
                break;
            }
        }

        if(comm_index == -1) return -1;

        int dayCount = 0;

        for (int month = 0; month < MONTHS; month++) {
            for (int day = 0; day < DAYS; day++) {
                if(profits[month][day][comm_index] > threshold){
                    dayCount++;
                }
            }
        }

        return dayCount;
    }

    public static int biggestDailySwing(int month) {
        if(month < 0 || month > 11) return -99999;
        int diff = 0;
        int total;

        for (int day = 1; day < DAYS; day++) {
            total = 0;
            for (int comm = 0; comm < COMMS; comm++) {
                total += Math.abs(profits[month][day][comm] - profits[month][day - 1][comm]);
            }
            if (total > diff){
                diff = total;
            }
        }
        return diff;
    }

    public static String compareTwoCommodities(String c1, String c2) {
        int comm1_index = -1;
        int comm2_index = -1;

        for (int comm = 0; comm < COMMS; comm++) {
            if(c1.equals(commodities[comm])){
                comm1_index = comm;
            }
            if(c2.equals(commodities[comm])){
                comm2_index = comm;
            }
        }

        if (comm1_index == -1 || comm2_index == -1) return "INVALID_COMMODITY";

        int c1_total = 0;
        int c2_total = 0;

        for (int month = 0; month < MONTHS; month++) {
            for (int day = 0; day < DAYS; day++) {
                c1_total += profits[month][day][comm1_index];
                c2_total += profits[month][day][comm2_index];
            }
        }

        int diff = Math.abs(c1_total - c2_total);

        if (c1_total > c2_total) return "C1 is better by " + diff;
        else if (c2_total > c1_total) return  "C2 is better by " + diff;
        else return "Equal";
    }

    public static String bestWeekOfMonth(int month) {
        if(month < 0 || month > 11) return "INVALID_MONTH";
        int total;
        int best = -100000000;
        int bestWeek = 0;

        for (int week = 1; week <= 4; week++) {
            total = 0;
            for (int day = (week -1) * 7; day < week * 7; day++) {
                for (int comm = 0; comm < COMMS; comm++) {
                    total += profits[month][day][comm];
                }
            }

            if (total > best){
                bestWeek = week;
                best = total;
            }
        }

        return "Week" + " " +bestWeek;
    }

    public static void main(String[] args) {
        loadData();
        System.out.println("Data loaded – ready for queries");
    }
}
