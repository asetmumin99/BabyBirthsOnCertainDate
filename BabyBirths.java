import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
/**
 * Write a description of BabyBirths here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BabyBirths {
    public void printNames(){
        FileResource fr = new FileResource();
        for(CSVRecord rec : fr.getCSVParser(false)){
            int numBorn = Integer.parseInt(rec.get(2));
            if(numBorn <= 100){
                System.out.println("Name " + rec.get(0) + 
                                   ", Gender " + rec.get(1) +
                                   ", Num born " + rec.get(2));
            }
        }
    }
    
    public void totalBirths(FileResource fr){
        int totalBirths = 0, totalBoys = 0, totalGirls = 0;
        int totalGirlsNames = 0, totalBoysNames = 0;
        for(CSVRecord rec : fr.getCSVParser(false)){
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirths += numBorn;
            if(rec.get(1).equals("F")){
                totalGirls += numBorn;
                totalGirlsNames += 1;
            }
            else{
                totalBoys += numBorn;
                totalBoysNames += 1;
            }
        }
        System.out.println("total births = " + totalBirths);
        System.out.println("total Boys = " + totalBoys);
        System.out.println("total Girls = " + totalGirls);
        System.out.println("total Boys Names = " + totalBoysNames);
        System.out.println("total Girls Names = " + totalGirlsNames);
    }
    
    public void testTotalBirths(){
        //FileResource fr = new FileResource("us_babynames_by_year/yob2014.csv");
        FileResource fr = new FileResource("us_babynames_by_year/yob1905.csv");
        totalBirths(fr);
        //System.out.println(getRank(1971, "Frank", "M"));
        //System.out.println(getName(1982, 450, "M"));
        //System.out.println(getName(2014, 6, "M"));
        //whatIsNameInYear("Owen", 1974, 2014, "M");
    }
    
    public int getRank(int year, String name, String gender){
        FileResource fr = new FileResource("us_babynames_by_year/yob" + 
                                            Integer.toString(year) + 
                                            ".csv");
        //FileResource fr = new FileResource("us_babynames_by_year/yob2014.csv");
        int record = 0;
        int rank = -1;
        for(CSVRecord rec : fr.getCSVParser(false)){
            if(rec.get(1).equals(gender)){
                record++;
                if(rec.get(0).equals(name)){
                    //rank = record;
                    return record;
                }
            }
        }
        return rank;
    }
    
    public String getName(int year, int rank, String gender){
        if(rank == -1)
            return "NO NAME";
        else{
            FileResource fr = new FileResource("us_babynames_by_year/yob" + 
                                            Integer.toString(year) + 
                                            ".csv");
            //FileResource fr = new FileResource("us_babynames_by_year/yob2014.csv");
            int record = 0;
            for(CSVRecord rec : fr.getCSVParser(false)){
                if(rec.get(1).equals(gender)){
                    record++;
                    if(rank == record){
                        return rec.get(0);
                    }
                }
            }
            return "NO NAME";
        }
    }
    
    public void whatIsNameInYear(String name, int year, int newYear, String gender){
        int rank = getRank(year, name, gender);
        System.out.println(name + " born in " + year + " would be " +
                           getName(newYear, rank, gender) + " if she was born in " + newYear + ".");
    }
    
    public int yearOfHighestRank (String name, String gender){
        int maxYear = 0, maxRank = Integer.MAX_VALUE;
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            //for(CSVRecord rec : fr.getCSVParser(false)){
                int year = Integer.parseInt(f.getName().substring(3, 7));
                int currentRank = getRank(year, name, gender);
                if(currentRank < maxRank && currentRank > 0){
                    maxRank = currentRank;
                    maxYear = year;
                }
            //}
        }
        
        return maxYear;
    }
    public void testYearOfHighestRank(){
        //System.out.println("yearOfHighestRank(\"Genevieve\", \"F\") = " + yearOfHighestRank("Genevieve", "F"));
        System.out.println("yearOfHighestRank(\"Mich\", \"M\") = " + yearOfHighestRank("Mich", "M"));
    }
    
    public double getAverageRank(String name, String gender){
        double sumRanks = 0.0;
        int numRanks = 0;
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            
            //for(CSVRecord rec : fr.getCSVParser(false)){
                int year = Integer.parseInt(f.getName().substring(3, 7));
                int currentRank = getRank(year, name, gender);
                System.out.println(f.getName() + ", rank = " + currentRank);
                if(currentRank != -1){
                    numRanks++;
                    sumRanks += currentRank;
                }
            //}
        }
        
        if(numRanks == 0)
            return -1;
        else
            return sumRanks/numRanks;
    }
    
    public void testGetAverageRank(){
        System.out.println("getAverageRank(\"Robert\", \"F\") = " + getAverageRank("Robert", "M"));
    }
    
    public int getTotalBirthsRankedHigher(int year, String name, String gender){
        FileResource fr = new FileResource("us_babynames_by_year/yob" + 
                                            Integer.toString(year) + 
                                            ".csv");
            //FileResource fr = new FileResource("us_babynames_by_year/yob2014.csv");
        int sumBirths = 0;
        int rank = getRank(year, name, gender);
        for(CSVRecord rec : fr.getCSVParser(false)){
            int rankgot = getRank(year, rec.get(0), gender);
            System.out.println("rank = " + rank + ", " + 
                                   "getRank(year, rec.get(0), gender) = " + 
                                    rankgot + 
                                   "__" + rec.get(2));
            if(rankgot > 0 && rankgot < rank && !rec.get(0).equals(name) &&  
                 rec.get(1).equals(gender)){
                sumBirths += Integer.parseInt(rec.get(2));
                
            }
        }
        return sumBirths;
    }
    
    public void testGetTotalBirthsRankedHigher(){
        //System.out.println("getTotalBirthsRankedHigher(1990,\"Emily\", \"F\") = " + getTotalBirthsRankedHigher(1990,"Emily", "F"));
        System.out.println("getTotalBirthsRankedHigher(1990,\"Drew\", \"F\") = " + getTotalBirthsRankedHigher(1990,"Drew", "M"));
    }
}


