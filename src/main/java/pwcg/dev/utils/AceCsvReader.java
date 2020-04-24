package pwcg.dev.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.Country;
import pwcg.campaign.io.json.HistoricalAceIOJson;
import pwcg.campaign.squadmember.HistoricalAce;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.utils.DateUtils;

public class AceCsvReader
{
    private List<String> aceVictoryEntries = new ArrayList<>();
    private String aceName ="Gunther Rall";
    private int aceSerialNumber = 201006;
   // private int aceSerialNumber = 101006;
    private Country country = Country.GERMANY;
    

    public static void main (String[] args)
    {
        try
        {
            AceCsvReader aceCsvReader = new AceCsvReader();
            aceCsvReader.reaceAceFile("D:\\PWCG\\Aces\\Gunther Rall.txt");
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
            System.out.println(entrySplit[0]);
            System.out.println(entrySplit[1]);
            System.out.println(entrySplit[2]);
            
            Victory victory = new Victory();
            victory.getVictim().setType(entrySplit[2]);
            victory.getVictim().setAirOrGround(Victory.AIR_VICTORY);
            victory.setDate(DateUtils.getDateDDMMYYYY(entrySplit[1]));
            
            historicalAce.addVictory(victory);
        }
        
        HistoricalAceIOJson.writeJson(historicalAce);
        
        System.out.println(aceName + " completed");
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
