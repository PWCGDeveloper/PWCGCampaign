package pwcg.mission.flight.waypoint.begin;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuWaypoint;

public class IngressWaypointNearField implements IIngressWaypoint
{
    private IFlight flight;

    public IngressWaypointNearField(IFlight flight) throws PWCGException 
    {
        this.flight = flight;
    }

    public McuWaypoint createIngressWaypoint() throws PWCGException  
    {
        double runwayDogLegOrientation = getIngressOrientationNearField();
        Coordinate ingressCoords = getIngressPositionNearField(runwayDogLegOrientation);

        McuWaypoint ingressWP = WaypointFactory.createIngressWaypointType();
        ingressWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        ingressWP.setSpeed(flight.getFlightCruisingSpeed());
        ingressWP.setPosition(ingressCoords);   
        ingressWP.setOrientation(new Orientation(runwayDogLegOrientation));   
        ingressWP.setTargetWaypoint(false);
        
        return ingressWP;
    }

    private Coordinate getIngressPositionNearField(double runwayOrientation) throws PWCGException 
    {
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        double distanceToIngress = productSpecificConfiguration.getInterceptInnerLoopDistance();

        Coordinate ingressCoordinate = MathUtils.calcNextCoord(flight.getFlightHomePosition(), runwayOrientation, distanceToIngress);
        
        int takeoffWaypointAltitude = flight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.TakeoffWaypointAltitudeKey);
        ingressCoordinate.setYPos(takeoffWaypointAltitude + 200);

        return ingressCoordinate;
    }

    private double getIngressOrientationNearField() throws PWCGException 
    {
        Airfield airfield = flight.getSquadron().determineCurrentAirfieldCurrentMap(flight.getCampaign().getDate());
        double runwayOrientation = airfield.getTakeoffLocation(flight.getMission()).getOrientation().getyOri();
        return runwayOrientation;
    }
}
