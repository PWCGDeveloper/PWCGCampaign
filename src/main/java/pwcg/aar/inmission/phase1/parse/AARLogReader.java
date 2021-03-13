package pwcg.aar.inmission.phase1.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class AARLogReader 
{
    private AARMissionLogFileSet aarLogFileMissionFile;
    private AARLogFileSetFactory aarLogFileSetFactory = new AARLogFileSetFactory();
    private List<String> logLinesFromMission = new ArrayList<>();

    public AARLogReader(AARMissionLogFileSet aarLogFileMissionFile)
    {
        this.aarLogFileMissionFile = aarLogFileMissionFile;
    }

    public List<String> readLogFilesForMission(Campaign campaign) throws PWCGException 
    {
        try
        {
            String selectedFileSet = aarLogFileMissionFile.getLogFileName();
                        
            aarLogFileSetFactory.determineMissionResultsFileForRequestedFileSet(selectedFileSet);
            List<File> aarLogFilesForThisSet = aarLogFileSetFactory.getLogFileSets();
            if (aarLogFilesForThisSet.size() == 0)
            {
                throw new PWCGException("No files found for log set " + selectedFileSet);
            }
            
            for (File filename : aarLogFilesForThisSet) 
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
            return AARLogKeeper.selectLogLinesToKeep(logLinesFromMission);        
        }
    }

    private void readLogFile(File file) throws FileNotFoundException, IOException, PWCGException
    {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;

        while ((line = reader.readLine()) != null) 
        {
            logLinesFromMission.add(line);
        }

        reader.close();
    }
}

