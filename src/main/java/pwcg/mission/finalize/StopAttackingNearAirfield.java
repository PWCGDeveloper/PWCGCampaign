package pwcg.mission.finalize;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.FlightTypeCategory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.StopAttackingNearAirfieldSequence;

public class StopAttackingNearAirfield
{
    private IFlight flight;
    private List<Airfield> airfieldsInMission;
    private List<StopAttackingNearAirfieldSequence>sequencesForFlight = new ArrayList<>();

    public StopAttackingNearAirfield (IFlight flight, List<Airfield> airfieldsInMission)
    {
        this.flight = flight;
        this.airfieldsInMission = airfieldsInMission;
    }
    
    public List<StopAttackingNearAirfieldSequence> stopAttackingAirfields() throws PWCGException
    {
        if (isStopAttackingNearAirfieldSequence())
        {
            makeStopAttackForEachAirfield();
        }
        return sequencesForFlight;
    }

    private void makeStopAttackForEachAirfield() throws PWCGException
    {
        for (Airfield airfield : airfieldsInMission)
        {
            if (isAirfieldForStopAttack(airfield))
            {
                makeStopAttackForEachPlane(airfield);
            }
        }
    }

    private void makeStopAttackForEachPlane(Airfield airfield) throws PWCGException
    {
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            if (!plane.getPilot().isPlayer())
            {
                StopAttackingNearAirfieldSequence sequence = new StopAttackingNearAirfieldSequence();
                McuWaypoint approachWaypoint = flight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_LANDING_APPROACH);
                sequence.makeThePlaneGoAway(plane, approachWaypoint, airfield.getPosition());
                sequencesForFlight.add(sequence);
            }
        }
    }
    
    private boolean isStopAttackingNearAirfieldSequence()
    {
        if (flight.getFlightType() == FlightTypes.OFFENSIVE)
        {
            return false;
        }
        
        if (flight.getFlightType() == FlightTypes.ESCORT)
        {
            return false;
        }
        
        if (!flight.getFlightType().isCategory(FlightTypeCategory.FIGHTER))
        {
            return false;
        }
        
        return true;
    }
    
    private boolean isAirfieldForStopAttack(Airfield airfield) throws PWCGException
    {
        Campaign campaign = flight.getCampaign();
        if (airfield.getCountry(campaign.getCampaignMap(), campaign.getDate()).getSide() == flight.getSquadron().determineSide())
        {
            return false;
        }
        
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getFrontLinesForMap(flight.getCampaign().getDate());
        Coordinate closestEnemyFrontLines = frontLinesForMap.findClosestFrontCoordinateForSide(airfield.getPosition(), flight.getSquadron().determineEnemySide());
        double distanceFromAirfieldToFront = MathUtils.calcDist(airfield.getPosition(), closestEnemyFrontLines);
        
        
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int airfieldGoAwayDistance = productSpecific.getAirfieldGoAwayDistance();
        if (distanceFromAirfieldToFront < airfieldGoAwayDistance)
        {
            return false;
        }
        
        return true;
    }
}
