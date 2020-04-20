package pwcg.dev.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.io.json.HistoricalAceIOJson;
import pwcg.campaign.squadmember.HistoricalAce;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.utils.DateUtils;

public class AceCsvReader
{
    private List<String> aceVictoryEntries = new ArrayList<>();
    private String aceName ="";
    private int aceSerialNumber = 20112054;

    public static void main (String[] args)
    {
        try
        {
            AceCsvReader aceCsvReader = new AceCsvReader();
            aceCsvReader.reaceAceFile("D:\\PWCG\\Aces\\Erich Hartmann Victories.csv");
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
            victory.setDate(DateUtils.getDateMMDDYY(entrySplit[1]));
            
            historicalAce.addVictory(victory);
        }
        
        HistoricalAceIOJson.writeJson(historicalAce);
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
