package pwcg.campaign;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;

public class MissionSpread
{
    private Campaign campaign;
    
    private List<Integer> monthlyMissionMinimum = new ArrayList<Integer>();
    private List<Integer> monthlyMissionSpread = new ArrayList<Integer>();

    public MissionSpread (Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    private void getCampaignMissionSpacing() throws PWCGException 
    {
        ConfigManagerCampaign campaignConfigManager = campaign.getCampaignConfigManager();
                
        monthlyMissionMinimum.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing01MinKey)));
        monthlyMissionMinimum.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing02MinKey)));
        monthlyMissionMinimum.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing03MinKey)));
        monthlyMissionMinimum.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing04MinKey)));
        monthlyMissionMinimum.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing05MinKey)));
        monthlyMissionMinimum.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing06MinKey)));
        monthlyMissionMinimum.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing07MinKey)));
        monthlyMissionMinimum.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing08MinKey)));
        monthlyMissionMinimum.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing09MinKey)));
        monthlyMissionMinimum.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing10MinKey)));
        monthlyMissionMinimum.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing11MinKey)));
        monthlyMissionMinimum.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing12MinKey)));

        monthlyMissionSpread.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing01MaxAdditionalKey)));
        monthlyMissionSpread.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing02MaxAdditionalKey)));
        monthlyMissionSpread.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing03MaxAdditionalKey)));
        monthlyMissionSpread.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing04MaxAdditionalKey)));
        monthlyMissionSpread.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing05MaxAdditionalKey)));
        monthlyMissionSpread.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing06MaxAdditionalKey)));
        monthlyMissionSpread.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing07MaxAdditionalKey)));
        monthlyMissionSpread.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing08MaxAdditionalKey)));
        monthlyMissionSpread.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing09MaxAdditionalKey)));
        monthlyMissionSpread.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing10MaxAdditionalKey)));
        monthlyMissionSpread.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing11MaxAdditionalKey)));
        monthlyMissionSpread.add(Integer.valueOf(campaignConfigManager.getIntConfigParam(ConfigItemKeys.MissionSpacing12MaxAdditionalKey)));
    }

    public int getMissionSpread(int month) throws PWCGException
    {
        getCampaignMissionSpacing();
        return monthlyMissionSpread.get(month).intValue();
    }

    public int getMissionMinimum(int month) throws PWCGException
    {
        getCampaignMissionSpacing();
        return monthlyMissionMinimum.get(month).intValue();
    }

}
