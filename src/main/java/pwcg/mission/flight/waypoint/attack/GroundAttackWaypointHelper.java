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
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class GroundAttackWaypointHelper
{
	private IFlight flight;
	private Coordinate ingressPosition;
	private int attackAltitude;
    private List<McuWaypoint>waypointsBefore = new ArrayList<>();
    private List<McuWaypoint>waypointsAfter = new ArrayList<>();
	
	
	public GroundAttackWaypointHelper(IFlight flight, Coordinate ingressPosition, int attackAltitude)
	{
		this.ingressPosition = ingressPosition;		
        this.flight = flight;       
        this.attackAltitude = attackAltitude;       
	}
	
	public void createTargetWaypoints() throws PWCGException  
	{
		McuWaypoint targetIngressWP = createGroundAttackTargetApproachWaypoint();
		McuWaypoint targetFinalWP = createTargetFinalWaypoint(targetIngressWP);
		createGroundAttackEgressWaypoint(targetIngressWP, targetFinalWP);		
	}

	private McuWaypoint createGroundAttackTargetApproachWaypoint() throws PWCGException  
	{
		Coordinate coord = calculateTargetIngressCoords(ingressPosition);

		McuWaypoint targetApproachWP = WaypointFactory.createTargetApproachWaypointType();		
		targetApproachWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
		targetApproachWP.setSpeed(flight.getFlightCruisingSpeed());
		targetApproachWP.setPosition(coord);	
		targetApproachWP.setTargetWaypoint(false);
		waypointsBefore.add(targetApproachWP);	
		
		return targetApproachWP;
	}

	private Coordinate calculateTargetIngressCoords(Coordinate ingressPosition)
	        throws PWCGException, PWCGException
	{
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int bombApproachDistance = productSpecificConfiguration.getBombApproachDistance();

        double ingressAngle = MathUtils.calcAngle(ingressPosition.copy(), flight.getTargetDefinition().getPosition().copy());
        double angleBackFromTarget = MathUtils.adjustAngle(ingressAngle, 150);
		Coordinate targetIngressCoords = MathUtils.calcNextCoord(flight.getCampaignMap(), flight.getTargetDefinition().getPosition(), angleBackFromTarget, bombApproachDistance);
		targetIngressCoords.setYPos(ingressPosition.getYPos());
		return targetIngressCoords;
	}

    private McuWaypoint createTargetFinalWaypoint(McuWaypoint approachWP) throws PWCGException  
    {
        Coordinate coord = calculateTargetFinalCoords(approachWP);

        McuWaypoint targetFinalWP = WaypointFactory.createTargetFinalWaypointType();
        targetFinalWP.setTriggerArea(McuWaypoint.TARGET_AREA);
        targetFinalWP.setDesc(WaypointType.TARGET_FINAL_WAYPOINT.getName());
        targetFinalWP.setSpeed(flight.getFlightCruisingSpeed());
        targetFinalWP.setPosition(coord);    
        targetFinalWP.setOrientation(approachWP.getOrientation().copy());
        targetFinalWP.setTargetWaypoint(true);

        waypointsBefore.add(targetFinalWP);
        
        return targetFinalWP;
    }

	private Coordinate calculateTargetFinalCoords(McuWaypoint approachWP) throws PWCGException
	{
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int bombFinalApproachDistance = productSpecificConfiguration.getBombFinalApproachDistance() - 1000;

        double angleFromTarget = MathUtils.calcAngle(flight.getTargetDefinition().getPosition(), approachWP.getPosition());
        Coordinate coord = MathUtils.calcNextCoord(flight.getCampaignMap(), flight.getTargetDefinition().getPosition(), angleFromTarget, bombFinalApproachDistance);
        coord.setYPos(attackAltitude);
		return coord;
	}   

	protected void createGroundAttackEgressWaypoint(McuWaypoint targetIngressWP, McuWaypoint targetFinalWP) throws PWCGException  
	{
        Coordinate coord = calculateTargetEgressCoords(targetIngressWP, targetFinalWP);

		McuWaypoint targetEgressWP = WaypointFactory.createTargetEgressWaypointType();
		targetEgressWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
		targetEgressWP.setSpeed(flight.getFlightCruisingSpeed());
		targetEgressWP.setPosition(coord);	
		targetEgressWP.setTargetWaypoint(false);

		waypointsAfter.add(targetEgressWP);	
	}
	
	private Coordinate calculateTargetEgressCoords(McuWaypoint targetIngressWP, McuWaypoint targetFinalWP) throws PWCGException
	{
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int bombTargetEgressDistance = productSpecificConfiguration.getBombFinalApproachDistance() / 2;

        double angleFromTarget = MathUtils.calcAngle(targetFinalWP.getPosition(), flight.getTargetDefinition().getPosition());
        double angleEgressFromTarget = MathUtils.adjustAngle(angleFromTarget, 240);
        Coordinate coord = MathUtils.calcNextCoord(flight.getCampaignMap(), flight.getTargetDefinition().getPosition(), angleEgressFromTarget, bombTargetEgressDistance);
        coord.setYPos(targetIngressWP.getPosition().getYPos());
		return coord;
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
