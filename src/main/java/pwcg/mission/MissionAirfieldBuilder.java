package pwcg.mission;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;

public class MissionAirfieldBuilder
{
    private Campaign campaign;
    private Mission mission;
    
    public MissionAirfieldBuilder (Campaign campaign, Mission mission)
    {
        this.campaign = campaign;
        this.mission = mission;
    }
    
    public List<IAirfield> getFieldsForPatrol() throws PWCGException 
    {
        TreeMap<String, IAirfield> selectedFields = selectAirfieldsWithinMissionBoundaries();
        List<IAirfield> fieldSet = new ArrayList<>(selectedFields.values());

        return fieldSet;
    }

    private TreeMap<String, IAirfield> selectAirfieldsWithinMissionBoundaries() throws PWCGException
    {
        CoordinateBox missionBorders = createMissionBordersForAirfields();

        TreeMap<String, IAirfield> selectedFields = new TreeMap<>();

        for (IAirfield field :  PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAllAirfields().values())
        {
        	if (missionBorders.isInBox(field.getPosition()))
            {
                field.addAirfieldObjects(mission);        	    
                selectedFields.put(field.getName(), field);
            }
        }
        
        return selectedFields;
    }

	private CoordinateBox createMissionBordersForAirfields() throws PWCGException, PWCGException
	{
		ConfigManager configManager = campaign.getCampaignConfigManager();
        int keepAirfieldSpread = configManager.getIntConfigParam(ConfigItemKeys.KeepAirfieldSpreadKey);
        CoordinateBox keepAirfieldsBorder = mission.getMissionBorders().expandBox(keepAirfieldSpread);
		return keepAirfieldsBorder;
	}
}
