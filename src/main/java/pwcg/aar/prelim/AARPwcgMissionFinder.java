package pwcg.aar.prelim;

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
        directoryReader.sortilesInDir(campaign.getCampaignPath());
        List<String> pwcgMissionFileNames = directoryReader.getSortedFilesWithFilter(".MissionData.json");

        return loadPwcgMissionDataSets(pwcgMissionFileNames);
    }

    private List<PwcgMissionData> loadPwcgMissionDataSets(List<String> pwcgMissionFileNames) throws PWCGException
    {
    	List<PwcgMissionData> sortedPwcgMissionData = new ArrayList<>();
        for (String pwcgMissionFileName : pwcgMissionFileNames) 
        {
            PwcgMissionData pwcgMissionData = CampaignMissionIOJson.readJson(campaign, pwcgMissionFileName);
            sortedPwcgMissionData.add(pwcgMissionData);
        }
        
        return sortedPwcgMissionData;
    }
}
