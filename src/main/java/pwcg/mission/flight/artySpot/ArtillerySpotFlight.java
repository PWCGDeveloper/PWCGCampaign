package pwcg.mission.flight.artySpot;

import java.util.List;

import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.Unit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.mcu.McuWaypoint;

public class ArtillerySpotFlight extends Flight
{

    public ArtillerySpotFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
    {
        super (flightInformation, missionBeginUnit);
    }

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		ArtillerySpotWaypoints am = new ArtillerySpotWaypoints(this);
		return am.createWaypoints();
	}

	public String getMissionObjective() throws PWCGException 
	{
		String objective = "Correct artillery fire at the specified objective.";
		
		for (Unit linkedUnit : linkedUnits)
		{
            String objectiveLocation =  getMissionObjectiveLocation(flightInformation.getSquadron(), flightInformation.getCampaign().getDate(), linkedUnit);
            
            if (linkedUnit instanceof GroundUnit)
            {
                GroundUnit groundUnit = (GroundUnit)linkedUnit;                             
                TacticalTarget targetType = groundUnit.getPwcgGroundUnitInformation().getTargetType();
                if (targetType == TacticalTarget.TARGET_ASSAULT)
                {
                    objective = "Spot artillery against assaulting enemy troops " + objectiveLocation; 
                    break;
                }
                else if (targetType == TacticalTarget.TARGET_DEFENSE)
                {
                    objective = "Spot artillery against defending enemy troops " + objectiveLocation; 
                    break;
                }
                else if (targetType == TacticalTarget.TARGET_ARTILLERY)
                {
                    objective = "Spot artillery against the artillery battery " + objectiveLocation; 
                    // Artillery might be overridden by something else
                }
                else if (targetType == TacticalTarget.TARGET_TRANSPORT)
                {
                    objective = "Spot artillery against the transport and road facilities " + objectiveLocation; 
                    break;
                }
                else if (targetType == TacticalTarget.TARGET_DRIFTER)
                {
                    objective = "Spot artillery against the light shipping " + objectiveLocation; 
                    break;
                }
                else if (targetType == TacticalTarget.TARGET_TROOP_CONCENTRATION)
                {
                    objective = "Spot artillery against the troop concentrations " + objectiveLocation; 
                    // Troop concentration might be overridden by something else
                }
            }
		}
		
		return objective;
	}

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        this.createSimpleTargetAssociations();
    }
}
