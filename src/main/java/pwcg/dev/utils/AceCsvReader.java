package pwcg.dev.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.Country;
import pwcg.campaign.crewmember.HistoricalAce;
import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.io.json.HistoricalAceIOJson;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class AceCsvReader
{
    private List<String> aceVictoryEntries = new ArrayList<>();
    private String aceName ="Hermann Graf";
    // private int aceSerialNumber = 101006;
    //private int aceSerialNumber = 102003;
    //private int aceSerialNumber = 103001;
    private int aceSerialNumber = 201001;
    private Country country = Country.GERMANY;
    

    public static void main (String[] args)
    {
        try
        {
            AceCsvReader aceCsvReader = new AceCsvReader();
            aceCsvReader.reaceAceFile("D:\\PWCG\\Aces\\Hermann Graf.txt");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void reaceAceFile(String aceFileName) throws Exception
    {
        HistoricalAce historicalAce = new HistoricalAce();
        historicalAce.setName(aceName);
        historicalAce.setSerialNumber(aceSerialNumber);
        historicalAce.setCountry(country);
        
        readLogFile(aceFileName);
        for (String aceVictoryEntry : aceVictoryEntries)
        {
            String[] entrySplit = aceVictoryEntry.split(",");
            PWCGLogger.log(LogLevel.DEBUG, entrySplit[0]);
            PWCGLogger.log(LogLevel.DEBUG, entrySplit[1]);
            PWCGLogger.log(LogLevel.DEBUG, entrySplit[2]);
            
            Victory victory = new Victory();
            victory.getVictim().setType(entrySplit[2]);
            victory.getVictim().setAirOrGround(Victory.AIRCRAFT);
            victory.setDate(DateUtils.getDateDDMMYYYY(entrySplit[1]));
            
            historicalAce.addVictory(victory);
        }
        
        HistoricalAceIOJson.writeJson(historicalAce);
        
        PWCGLogger.log(LogLevel.DEBUG, aceName + " completed");
    }
    
    private void readLogFile(String filename) throws Exception
    {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = reader.readLine()) != null) 
        {
            aceVictoryEntries.add(line);
        }

        reader.close();
    }
}
