package pwcg.core.logfiles;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class LogReader 
{
    private LogFileSetFactory aarLogFileSetFactory = new LogFileSetFactory();
    private List<String> logLinesFromMission = new ArrayList<>();

    public List<String> readLogFilesForMission(Campaign campaign, String selectedFileSet) throws PWCGException 
    {
        try
        {                        
            aarLogFileSetFactory.determineMissionResultsFileForRequestedFileSet(selectedFileSet);
            List<String> aarLogFilesForThisSet = aarLogFileSetFactory.getLogFileSets();
            if (aarLogFilesForThisSet.size() == 0)
            {
                throw new PWCGException("No files found for log set " + selectedFileSet);
            }
            
            for (String filename : aarLogFilesForThisSet) 
            {
                readLogFile(filename);
            }
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
        
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_COOP)
        {
            return logLinesFromMission;
        }
        else
        {
            return LogKeeper.selectLogLinesToKeep(logLinesFromMission);        
        }
    }

    private void readLogFile(String filename) throws FileNotFoundException, IOException, PWCGException
    {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = reader.readLine()) != null) 
        {
            logLinesFromMission.add(line);
        }

        reader.close();
    }
}

