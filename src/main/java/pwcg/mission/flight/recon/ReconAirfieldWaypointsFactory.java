package pwcg.mission.flight.recon;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.mcu.McuWaypoint;

public class ReconAirfieldWaypointsFactory
{
    private IFlight flight;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();

    public ReconAirfieldWaypointsFactory(IFlight flight) throws PWCGException
    {
        this.flight = flight;
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet.addWaypoint(ingressWaypoint);
        
        List<McuWaypoint> waypoints = createTargetWaypoints(ingressWaypoint.getPosition());
        missionPointSet.addWaypoints(waypoints);

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        missionPointSet.addWaypoint(egressWaypoint);

        return missionPointSet;
    }

    private List<McuWaypoint> createTargetWaypoints(Coordinate ingressPosition) throws PWCGException
    {
        List<McuWaypoint> targetWaypoints = new ArrayList<McuWaypoint>();
        
        Side enemySide = flight.getFlightInformation().getCountry().getSide().getOppositeSide();

        // Find airfields to recon
        List <Airfield> enemyAirfields = new ArrayList<Airfield>();
        double maxRadius = 40000.0;
        
        while (enemyAirfields.size() <= 2)
        {
            enemyAirfields =  PWCGContext.getInstance().getMap(flight.getCampaign().getCampaignMap()).getAirfieldManager().getAirfieldFinder().getAirfieldsWithinRadiusBySide(
                    flight.getCampaign().getCampaignMap(), flight.getTargetDefinition().getPosition(), flight.getCampaign().getDate(), maxRadius, enemySide);
                        
            maxRadius += 10000.0;
        }

        int numWaypoints = 2 + RandomNumberGenerator.getRandom(4);
        if (numWaypoints > enemyAirfields.size())
        {
            numWaypoints = enemyAirfields.size();
        }
        
        List <Airfield> remainingAirfields = enemyAirfields;
        Coordinate lastCoord = flight.getTargetDefinition().getPosition().copy();
        for (int i = 0; i < numWaypoints; ++i)
        {
            int index = getNextAirfield(remainingAirfields, lastCoord);
            
            Airfield field = remainingAirfields.get(index);
            McuWaypoint wp = createWP(field.getPosition().copy());
            targetWaypoints.add(wp);

            lastCoord = field.getPosition().copy();
            remainingAirfields.remove(index);
        }
        return targetWaypoints;
    }

    private int getNextAirfield(List <Airfield> remainingAirfields, Coordinate lastCoord) 
    {
        int index = 0;
        double leastDistance = PositionFinder.ABSURDLY_LARGE_DISTANCE;
        
        for (int i = 0; i < remainingAirfields.size(); ++i)
        {
            Airfield field = remainingAirfields.get(i);
            double thisDistance = MathUtils.calcDist(lastCoord, field.getPosition());
            if (thisDistance < leastDistance)
            {
                leastDistance = thisDistance;
                index = i;
            }
        }
        
        return index;
    }

    private McuWaypoint createWP(Coordinate coord) throws PWCGException 
    {
        coord.setYPos(flight.getFlightInformation().getAltitude());

        McuWaypoint wp = WaypointFactory.createReconWaypointType();
        wp.setTriggerArea(McuWaypoint.TARGET_AREA);
        wp.setSpeed(flight.getFlightCruisingSpeed());
        wp.setPosition(coord);

        return wp;
    }
}
