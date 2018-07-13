package pwcg.mission.flight;

import pwcg.campaign.Campaign;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.plane.PlaneMCU;

public class FlightPositionHelperAirStart
{
    private Campaign campaign;
    private Flight flight;
    
    public FlightPositionHelperAirStart (Campaign campaign, Flight flight)
    {
        this.campaign = campaign;
        this.flight = flight;
    }
    
    public void createPlanePositionAirStart(Coordinate startCoordinate, Orientation orientation) throws PWCGException 
    {
        PlaneMCU flightLeader = flight.getFlightLeader();
        
        // Initial position has already been set for ground starts
        int i = 0;
        for (PlaneMCU plane : flight.getPlanes())
        {
            Coordinate planeCoords = new Coordinate();

            // Air start for other flights
            // Since we always face east, subtract from z to get your mates
            // behind you
            ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
            int AircraftSpacingHorizontal = configManager.getIntConfigParam(ConfigItemKeys.AircraftSpacingHorizontalKey);
            planeCoords.setXPos(startCoordinate.getXPos() - (i * AircraftSpacingHorizontal));
            planeCoords.setZPos(startCoordinate.getZPos() - (i * AircraftSpacingHorizontal));

            int AircraftSpacingVertical = configManager.getIntConfigParam(ConfigItemKeys.AircraftSpacingVerticalKey);
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
