package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
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
    private List<IAirfield> airfieldsInMission;
    private List<StopAttackingNearAirfieldSequence>sequencesForFlight = new ArrayList<>();

    public StopAttackingNearAirfield (IFlight flight, List<IAirfield> airfieldsInMission)
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
        for (IAirfield airfield : airfieldsInMission)
        {
            if (isAirfieldForStopAttack(airfield))
            {
                makeStopAttackForEachPlane(airfield);
            }
        }
    }

    private void makeStopAttackForEachPlane(IAirfield airfield) throws PWCGException
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
    
    private boolean isAirfieldForStopAttack(IAirfield airfield) throws PWCGException
    {
        if (airfield.getCountry(flight.getCampaign().getDate()).getSide() == flight.getSquadron().determineSide())
        {
            return false;
        }
        
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(flight.getCampaign().getDate());
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
