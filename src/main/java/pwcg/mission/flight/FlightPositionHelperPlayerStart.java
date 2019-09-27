package pwcg.mission.flight;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
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
        else if (flight.isParkedStart())
        {
            createPlanePositionParked();
        }
        else
        {
            createPlanePositionRunway();
        }
    }

    private void createPlanePositionRunway() throws PWCGException 
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

    private void createPlanePositionParked() throws PWCGException
    {
        FlightPositionHelperParkedStart flightPositionHelperParkedStart = new FlightPositionHelperParkedStart(campaign, flight);
        flightPositionHelperParkedStart.createPlanePositionParkedStart();
    }

    private void createPlanePositionCloseToFirstWP() throws PWCGException
    {
        FlightPositionHelperAirStart.createPlayerPlanePositionCloseToFirstWP(flight);
    }
}
