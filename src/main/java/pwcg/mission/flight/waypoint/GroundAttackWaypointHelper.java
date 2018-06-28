package pwcg.mission.flight.waypoint;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.mcu.McuWaypoint;

public class GroundAttackWaypointHelper
{
	private int waypointSpeed;
	private int attackAltitude;
	private Coordinate targetCoords;
	private Coordinate ingressPosition;
	private List<McuWaypoint>waypoints = new ArrayList<>();
	
	
	public GroundAttackWaypointHelper(Coordinate ingressPosition, Coordinate targetCoords, int attackAltitude, int waypointSpeed)
	{
		this.ingressPosition = ingressPosition;		
		this.targetCoords = targetCoords;		
		this.waypointSpeed = waypointSpeed;
		this.attackAltitude = attackAltitude;
	}
	
	public List<McuWaypoint> createTargetWaypoints() throws PWCGException  
	{
		McuWaypoint targetIngressWP = createGroundAttackTargetApproachWaypoint();
		McuWaypoint targetFinalWP = createTargetFinalWaypoint(targetIngressWP);
		createGroundAttackEgressWaypoint(targetIngressWP, targetFinalWP);
		
		return waypoints;
	}

	protected McuWaypoint createGroundAttackTargetApproachWaypoint() throws PWCGException  
	{
		Coordinate coord = calculateTargetIngressCoords(ingressPosition);

		McuWaypoint targetApproachWP = WaypointFactory.createTargetApproachWaypointType();		
		targetApproachWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
		targetApproachWP.setSpeed(waypointSpeed);
		targetApproachWP.setPosition(coord);	
		targetApproachWP.setTargetWaypoint(false);
		waypoints.add(targetApproachWP);	
		
		return targetApproachWP;
	}

	private Coordinate calculateTargetIngressCoords(Coordinate ingressPosition)
	        throws PWCGException, PWCGException
	{
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int bombApproachDistance = productSpecificConfiguration.getBombApproachDistance();

        double ingressAngle = MathUtils.calcAngle(ingressPosition.copy(), targetCoords.copy());
        double angleBackFromTarget = MathUtils.adjustAngle(ingressAngle, 150);
		Coordinate targetIngressCoords = MathUtils.calcNextCoord(targetCoords, angleBackFromTarget, bombApproachDistance);		
		targetIngressCoords.setYPos(ingressPosition.getYPos());
		return targetIngressCoords;
	}

    private McuWaypoint createTargetFinalWaypoint(McuWaypoint approachWP) throws PWCGException  
    {
        Coordinate coord = calculateTargetFinalCoords(approachWP);

        McuWaypoint targetFinalWP = WaypointFactory.createTargetFinalWaypointType();
        targetFinalWP.setTriggerArea(McuWaypoint.TARGET_AREA);
        targetFinalWP.setDesc(WaypointType.TARGET_FINAL_WAYPOINT.getName());
        targetFinalWP.setSpeed(waypointSpeed);
        targetFinalWP.setPosition(coord);    
        targetFinalWP.setOrientation(approachWP.getOrientation().copy());
        targetFinalWP.setTargetWaypoint(true);

        waypoints.add(targetFinalWP);
        
        return targetFinalWP;
    }

	private Coordinate calculateTargetFinalCoords(McuWaypoint approachWP) throws PWCGException
	{
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int bombFinalApproachDistance = productSpecificConfiguration.getBombFinalApproachDistance();

        double angleToTarget = MathUtils.calcAngle(approachWP.getPosition(), targetCoords);
        Coordinate coord = MathUtils.calcNextCoord(approachWP.getPosition(), angleToTarget, bombFinalApproachDistance);
        coord.setYPos(attackAltitude);
		return coord;
	}   

	protected void createGroundAttackEgressWaypoint(McuWaypoint targetIngressWP, McuWaypoint targetFinalWP) throws PWCGException  
	{
        Coordinate coord = calculateTargetEgressCoords(targetIngressWP, targetFinalWP);

		McuWaypoint targetEgressWP = WaypointFactory.createTargetEgressWaypointType();
		targetEgressWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
		targetEgressWP.setSpeed(waypointSpeed);
		targetEgressWP.setPosition(coord);	
		targetEgressWP.setTargetWaypoint(false);

		waypoints.add(targetEgressWP);	
	}
	
	private Coordinate calculateTargetEgressCoords(McuWaypoint targetIngressWP, McuWaypoint targetFinalWP) throws PWCGException
	{
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int bombTargetEgressDistance = productSpecificConfiguration.getBombFinalApproachDistance() / 2;

        double angleFromTarget = MathUtils.calcAngle(targetFinalWP.getPosition(), targetCoords);
        double angleEgressFromTarget = MathUtils.adjustAngle(angleFromTarget, 240);
        Coordinate coord = MathUtils.calcNextCoord(targetCoords.copy(), angleEgressFromTarget, bombTargetEgressDistance);
        coord.setYPos(targetIngressWP.getPosition().getYPos());
		return coord;
	}   

}
