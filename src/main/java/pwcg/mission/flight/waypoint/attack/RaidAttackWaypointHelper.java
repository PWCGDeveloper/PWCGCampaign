package pwcg.mission.flight.waypoint.attack;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuWaypoint;

public class RaidAttackWaypointHelper
{
	private IFlight flight;
	private Coordinate ingressPosition;
	private int attackAltitude;
    private List<McuWaypoint>waypointsBefore = new ArrayList<>();
    private List<McuWaypoint>waypointsAfter = new ArrayList<>();
	
	
	public RaidAttackWaypointHelper(IFlight flight, Coordinate ingressPosition, int attackAltitude)
	{
		this.ingressPosition = ingressPosition;		
        this.flight = flight;       
        this.attackAltitude = attackAltitude;       
	}
	
	public void createTargetWaypoints() throws PWCGException  
	{
		McuWaypoint targetPopupWP = createRaidTargetApproachWaypoint();
		createRaidEgressWaypoint(targetPopupWP);		
	}

	private McuWaypoint createRaidTargetApproachWaypoint() throws PWCGException  
	{
		Coordinate coord = calculateTargetPopupCoords();

		McuWaypoint targetPopupWP = WaypointFactory.createTargetFinalWaypointType();		
		targetPopupWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
		targetPopupWP.setSpeed(flight.getFlightCruisingSpeed());
		targetPopupWP.setPosition(coord);	
		targetPopupWP.setTargetWaypoint(true);
		waypointsBefore.add(targetPopupWP);	
		
		return targetPopupWP;
	}

	private Coordinate calculateTargetPopupCoords() throws PWCGException
	{
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int popupBombFinalApproachDistance = productSpecificConfiguration.getBombFinalApproachDistance() - 1000;

        double angleFromTargetToIngress = MathUtils.calcAngle(flight.getTargetDefinition().getPosition().copy(), ingressPosition.copy());
		Coordinate targetPopupCoordinates = MathUtils.calcNextCoord(flight.getTargetDefinition().getPosition(), angleFromTargetToIngress, popupBombFinalApproachDistance);
		targetPopupCoordinates.setYPos(attackAltitude);
		return targetPopupCoordinates;
	}


	private void createRaidEgressWaypoint(McuWaypoint targetPopupWP) throws PWCGException  
	{
        Coordinate coord = calculateTargetEgressCoords(targetPopupWP);

		McuWaypoint targetEgressWP = WaypointFactory.createTargetEgressWaypointType();
		targetEgressWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
		targetEgressWP.setSpeed(flight.getFlightCruisingSpeed());
		targetEgressWP.setPosition(coord);	
		targetEgressWP.setTargetWaypoint(false);

		waypointsAfter.add(targetEgressWP);	
	}
	
	private Coordinate calculateTargetEgressCoords(McuWaypoint targetPopupWP) throws PWCGException
	{
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int bombTargetEgressDistance = productSpecificConfiguration.getBombFinalApproachDistance() / 2;

        double angleFromTarget = MathUtils.calcAngle(targetPopupWP.getPosition(), flight.getTargetDefinition().getPosition());
        double angleEgressFromTarget = MathUtils.adjustAngle(angleFromTarget, 240);
        Coordinate egressCoordinate = MathUtils.calcNextCoord(flight.getTargetDefinition().getPosition(), angleEgressFromTarget, bombTargetEgressDistance);
        egressCoordinate.setYPos(ingressPosition.getYPos());
		return egressCoordinate;
	}

    public List<McuWaypoint> getWaypointsBefore()
    {
        return waypointsBefore;
    }

    public List<McuWaypoint> getWaypointsAfter()
    {
        return waypointsAfter;
    }   
}
