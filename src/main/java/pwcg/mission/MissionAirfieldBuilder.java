package pwcg.mission;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;

public class MissionAirfieldBuilder
{
    private Campaign campaign;
    private Mission mission;
    private CoordinateBox structureBorders;
    private List<Airfield> fieldSet = new ArrayList<>();;

    public MissionAirfieldBuilder (Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
        this.structureBorders = mission.getStructureBorders();
    }
    
    public void buildFieldsForPatrol() throws PWCGException 
    {
        TreeMap<String, Airfield> selectedFields = selectAirfieldsWithinMissionBoundaries();
        fieldSet = new ArrayList<>(selectedFields.values());
    }

    public List<Airfield> getFieldsForPatrol() throws PWCGException 
    {
        return fieldSet;
    }

    private TreeMap<String, Airfield> selectAirfieldsWithinMissionBoundaries() throws PWCGException
    {
        TreeMap<String, Airfield> selectedFields = new TreeMap<>();
        for (Airfield airfield :  PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAllAirfields().values())
        {
            if (PWCGContext.getInstance().getCurrentMap().getAirfieldManager().isAirfieldOccupied(
                    PWCGContext.getInstance().getSquadronManager().getActiveSquadrons(campaign.getDate()), campaign.getDate()))
            {
            	if (structureBorders.isInBox(airfield.getPosition()))
                {
                    selectedFields.put(airfield.getName(), airfield);
                    airfield.addAirfieldObjects(mission);
                }
            }
        }
        
        return selectedFields;
    }
}
