package pwcg.mission;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;

public class MissionAirfieldBuilder
{
    private Campaign campaign;
    private Mission mission;
    private List<Airfield> fieldSet = new ArrayList<>();;

    public MissionAirfieldBuilder (Campaign campaign, Mission mission)
    {
        this.campaign = campaign;
        this.mission = mission;
    }
    
    public void buildFieldsForPatrol() throws PWCGException 
    {
        CoordinateBox missionBorders = createMissionBordersForAirfields();
        TreeMap<String, Airfield> selectedFields = selectAirfieldsWithinMissionBoundaries(missionBorders);
        fieldSet = new ArrayList<>(selectedFields.values());
    }
    
    public List<Airfield> getFieldsForPatrol() throws PWCGException 
    {
        return fieldSet;
    }

    private TreeMap<String, Airfield> selectAirfieldsWithinMissionBoundaries(CoordinateBox missionBorders) throws PWCGException
    {
        TreeMap<String, Airfield> selectedFields = new TreeMap<>();
        for (Airfield airfield :  PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAllAirfields().values())
        {
            if (PWCGContext.getInstance().getCurrentMap().getAirfieldManager().isAirfieldOccupied(
                    PWCGContext.getInstance().getSquadronManager().getActiveSquadrons(campaign.getDate()), campaign.getDate()))
            {
            	if (missionBorders.isInBox(airfield.getPosition()))
                {
                    selectedFields.put(airfield.getName(), airfield);
                    airfield.addAirfieldObjects(mission);
                }
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
