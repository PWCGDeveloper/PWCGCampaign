package pwcg.aar.prelim;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.io.json.CampaignMissionIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DirectoryReader;

public class AARPwcgMissionFinder
{    
    private Campaign campaign;
    private DirectoryReader directoryReader;

    public AARPwcgMissionFinder(Campaign campaign)
    {
    	this.campaign = campaign;
        directoryReader = new DirectoryReader();
    }
    
    public List<PwcgMissionData> getSortedPwcgMissionsForCampaign() throws PWCGException
    {
        directoryReader.sortFilesInDir(CampaignMissionIOJson.buildMissionDataPath(campaign));
        List<File> pwcgMissionFileNames = directoryReader.getSortedFilesWithFilter(CampaignMissionIOJson.MISSION_DATA_SUFFIX);

        return loadPwcgMissionDataSets(pwcgMissionFileNames);
    }

    private List<PwcgMissionData> loadPwcgMissionDataSets(List<File> pwcgMissionFiles) throws PWCGException
    {
    	List<PwcgMissionData> sortedPwcgMissionData = new ArrayList<>();
        for (File pwcgMissionFile : pwcgMissionFiles) 
        {
            PwcgMissionData pwcgMissionData = CampaignMissionIOJson.readJson(campaign, pwcgMissionFile.getName());
            sortedPwcgMissionData.add(pwcgMissionData);
        }
        
        return sortedPwcgMissionData;
    }
}
