package pwcg.mission.flight;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.plane.PlaneMCU;

public class FlightPositionHelperPlayerStart
{
    private Campaign campaign;
    private Flight flight;
    
    public FlightPositionHelperPlayerStart (Campaign campaign, Flight flight)
    {
        this.campaign = campaign;
        this.flight = flight;
    }
    

    public void createPlayerPlanePosition() throws PWCGException
    {
        if (flight.isAirStart())
        {
            createPlanePositionCloseToFirstWP();
        }
        else
        {
            createPlanePositionRunway();
        }
    }

    private void createPlanePositionRunway() throws PWCGException 
    {
        PlaneMCU flightLeader = flight.getFlightLeader();

        RunwayPlacer runwayPlacer = new RunwayPlacer(campaign);
        List<Coordinate> takeOffPositions = runwayPlacer.getFlightTakeoffPositions(flight);

        for (PlaneMCU plane : flight.getPlanes())
        {
            plane.setPosition(takeOffPositions.get(plane.getNumberInFormation()-1));

            Orientation orient = flight.getAirfield().getTakeoffLocation().getOrientation().copy();
            plane.setOrientation(orient);

            plane.populateEntity(flight, flightLeader);
            
            IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
            int startOnRunwayVal = productSpecificConfiguration.startOnRunway();
            plane.setStartInAir(startOnRunwayVal);
        }
    }

    private void createPlanePositionCloseToFirstWP() throws PWCGException
    {
        Coordinate firstDestinationCoordinate = flight.findFirstWaypointPosition();

        // Calculate plane position about 5 KM from the first destination
        double angleBetweenBaseAndFirstDest = MathUtils.calcAngle(flight.getAirfield().getTakeoffLocation().getPosition().copy(), firstDestinationCoordinate);
        double angleToPlacePlanes = MathUtils.adjustAngle(angleBetweenBaseAndFirstDest, 180);
        
        Coordinate startCoordinate = MathUtils.calcNextCoord(firstDestinationCoordinate, angleToPlacePlanes, 5000);
        startCoordinate.setYPos(firstDestinationCoordinate.getYPos());

        // orientation
        Orientation startOrientation = new Orientation(angleBetweenBaseAndFirstDest);

        FlightPositionHelperAirStart flightPositionHelperAirStart = new FlightPositionHelperAirStart(campaign, flight);
        flightPositionHelperAirStart.createPlanePositionAirStart(startCoordinate, startOrientation);
    }
}
