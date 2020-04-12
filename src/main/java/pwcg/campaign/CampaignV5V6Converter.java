package pwcg.campaign;

import java.io.File;
import java.io.IOException;
import java.util.List;

import pwcg.campaign.io.json.CampaignMissionIOJson;
import pwcg.campaign.io.json.CombatReportIOJson;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DirectoryReader;
import pwcg.core.utils.FileUtils;
import pwcg.core.utils.PWCGLogger;

public class CampaignV5V6Converter
{
    private Campaign campaign;
    
    public CampaignV5V6Converter(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public void convert()
    {
        try
        {
            if (!campaign.isCoop())
            {
                List<SquadronMember> players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
                if (players.size() != 1)
                {
                    return;
                }
                
                Integer pilotSerialNumber = players.get(0).getSerialNumber();
                moveCombatReports(pilotSerialNumber);
                moveMissionData();
            }
        }
        catch(Exception e)
        {
            PWCGLogger.logException(e);
        }
    }
    
    private void moveCombatReports(Integer pilotSerialNumber) throws PWCGException, IOException
    {
        String campaignPath = campaign.getCampaignPath();
        String combatReportPath = CombatReportIOJson.buildCombatReportPath(campaign, pilotSerialNumber);
        makeDirectories(combatReportPath);

        DirectoryReader directoryReader = new DirectoryReader();
        directoryReader.sortilesInDir(campaignPath);
        List<String> combatReportFiles = directoryReader.getSortedFilesWithFilter(CombatReportIOJson.COMBAT_REPORT_SUFFIX);
        
        for (String combatReportFile : combatReportFiles)
        {
            File sourceFile = new File (campaignPath + "\\" + combatReportFile);
            File targetFile = new File (combatReportPath + "\\" + combatReportFile);
            FileUtils.copyFile(sourceFile, targetFile);
            
            FileUtils.deleteFile(campaignPath + "\\" + combatReportFile);
        }

    }
    
    private void moveMissionData() throws PWCGException, IOException
    {
        String campaignPath = campaign.getCampaignPath();
        String missionDataPath = CampaignMissionIOJson.buildMissionDataPath(campaign);
        makeDirectories(missionDataPath);

        DirectoryReader directoryReader = new DirectoryReader();
        directoryReader.sortilesInDir(campaignPath);
        List<String> missionDataFiles = directoryReader.getSortedFilesWithFilter(CampaignMissionIOJson.MISSION_DATA_SUFFIX);
                
        for (String missionDataFile : missionDataFiles)
        {
            File sourceFile = new File (campaignPath + "\\" + missionDataFile);
            File targetFile = new File (missionDataPath + "\\" + missionDataFile);
            FileUtils.copyFile(sourceFile, targetFile);
            
            FileUtils.deleteFile(campaignPath + "\\" + missionDataFile);
        }
    }
    
    private void makeDirectories(String path)
    {
        File combatReportDir = new File(path);
        if (!combatReportDir.exists())
        {
            combatReportDir.mkdirs();
        }
    }


}
