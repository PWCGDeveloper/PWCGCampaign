package pwcg.mission.flight;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.plane.PlaneMCU;

public class AirStartFormationSetter
{
    private Flight flight;
    
    public AirStartFormationSetter (Flight flight)
    {
        this.flight = flight;
    }
    
    public void resetAirStartFormation(Coordinate startCoordinate, Orientation orientation) throws PWCGException 
    {
        PlaneMCU flightLeader = flight.getFlightLeader();
        
        int i = 0;
        for (PlaneMCU plane : flight.getPlanes())
        {
            Coordinate planeCoords = new Coordinate();
            
            IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
            int AircraftSpacingHorizontal = productSpecificConfiguration.getAircraftSpacingHorizontal();
            planeCoords.setXPos(startCoordinate.getXPos() - (i * AircraftSpacingHorizontal));
            planeCoords.setZPos(startCoordinate.getZPos() - (i * AircraftSpacingHorizontal));

            int AircraftSpacingVertical = productSpecificConfiguration.getAircraftSpacingVertical();
            planeCoords.setYPos(startCoordinate.getYPos() + (i * AircraftSpacingVertical));
            
            plane.setPosition(planeCoords);

            // Point the planes at the first destination 
            plane.setOrientation(orientation.copy());

            // This must be done last
            plane.populateEntity(flight, flightLeader);

            ++i;
        }
    }
}
