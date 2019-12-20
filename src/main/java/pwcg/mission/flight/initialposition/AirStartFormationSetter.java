package pwcg.mission.flight.initialposition;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.waypoint.WaypointType;

public class AirStartFormationSetter
{
    public static void resetAirStartFormation(Flight flight, Coordinate startCoordinate) throws PWCGException 
    {
        double startOrientation = MathUtils.calcAngle(startCoordinate, flight.getTargetPosition());
        int initialAltitude = WaypointType.getAltitudeForWaypointType(WaypointType.AIR_START_WAYPOINT, flight.getFlightAltitude());

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
            planeCoords.setYPos(initialAltitude + (i * AircraftSpacingVertical));
            
            plane.setPosition(planeCoords);
            plane.setOrientation(new Orientation(startOrientation));
            plane.populateEntity(flight, flightLeader);

            ++i;
        }
    }
}
