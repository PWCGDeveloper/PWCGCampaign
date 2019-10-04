package pwcg.mission.flight;

import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.plane.PlaneMCU;

public class FlightPositionRunwayStart
{
    private Flight flight;
    
    public FlightPositionRunwayStart (Flight flight)
    {
        this.flight = flight;
    }

    public void createPlanePositionRunway() throws PWCGException 
    {
        PlaneMCU flightLeader = flight.getFlightLeader();

        RunwayPlacer runwayPlacer = new RunwayPlacer();
        List<Coordinate> takeOffPositions = runwayPlacer.getFlightTakeoffPositions(flight);

        for (PlaneMCU plane : flight.getPlanes())
        {
            plane.setPosition(takeOffPositions.get(plane.getNumberInFormation()-1));

            Orientation orient = flight.getFlightInformation().getDepartureAirfield().getTakeoffLocation().getOrientation().copy();
            plane.setOrientation(orient);

            plane.populateEntity(flight, flightLeader);
            
            IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
            int startOnRunwayVal = productSpecificConfiguration.startOnRunway();
            plane.setStartInAir(startOnRunwayVal);
        }
    }
}
