package pwcg.mission.target.locator;

import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.DrifterManager;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.shipping.ShippingLane;
import pwcg.campaign.shipping.ShippingLaneManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.PositionFinder;
import pwcg.mission.target.TacticalTarget;
import pwcg.mission.target.TargetDefinition;

public class TargetLocatorAttack
{
    private TargetDefinition targetDefinition;
    private Coordinate missionCenter;
    private Coordinate targetLocation;
    private Orientation targetOrientation = new Orientation();
    private int currentSearchRadius = 0;
    
    public TargetLocatorAttack(TargetDefinition targetDefinition, Coordinate missionCenter)
    {
        this.targetDefinition = targetDefinition;
        this.currentSearchRadius = targetDefinition.getPreferredRadius();
        this.missionCenter = missionCenter;
    }
    
    public void locateTarget() throws PWCGException
    {
        TacticalTarget targetType = targetDefinition.getTargetType();
     
        while (targetLocation == null)
        {
            locateSelectedTarget(targetType);
            
            if (targetLocation == null)
            {
                currentSearchRadius += 15000;
            }

            if (currentSearchRadius > 400000)
            {
                throw new PWCGException("Failed to locate a target within a reasonable range");
            }
        }
    }

    private void locateSelectedTarget(TacticalTarget targetType) throws PWCGException
    {
        if (targetType == TacticalTarget.TARGET_ASSAULT)
        {
            getTargetLocationEnemyFrontLines();
        }
        else if (targetType == TacticalTarget.TARGET_DEFENSE)
        {
            getTargetLocationFriendlyFrontLines();
        }
        else if (targetType == TacticalTarget.TARGET_TRAIN)
        {
            getTargetLocationTrainStation();
        }
        else if (targetType == TacticalTarget.TARGET_TRANSPORT)
        {
            getTargetLocationBridge();
        }
        else if (targetType == TacticalTarget.TARGET_DRIFTER)
        {
            getTargetLocationRiver();
        }
        else if (targetType == TacticalTarget.TARGET_AIRFIELD)
        {
            getTargetLocationAirfield();
        }
        else if (targetType == TacticalTarget.TARGET_SHIPPING)
        {
            getTargetLocationShippingLanes();
        }
        else
        {
            getTargetLocationBehindEnemyLines();
        }
    }

    private void getTargetLocationEnemyFrontLines() throws PWCGException
    {
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(targetDefinition.getDate());
        FrontLinePoint frontLinePoint = frontLinesForMap.findCloseFrontPositionForSide(
                missionCenter, 
                currentSearchRadius, 
                targetDefinition.getAttackingCountry().getSide());
        
        if (frontLinePoint != null)
        {
            targetLocation = frontLinePoint.getPosition();
            targetOrientation = new Orientation(frontLinePoint.getOrientation(targetDefinition.getDate()));
        }
    }

    private void getTargetLocationFriendlyFrontLines() throws PWCGException
    {
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(targetDefinition.getDate());
        FrontLinePoint frontLinePoint = frontLinesForMap.findCloseFrontPositionForSide(
                missionCenter, 
                currentSearchRadius, 
                targetDefinition.getTargetCountry().getSide());

        if (frontLinePoint != null)
        {
            targetLocation = frontLinePoint.getPosition();
            targetOrientation = new Orientation(frontLinePoint.getOrientation(targetDefinition.getDate()));
        }
    }

    private void getTargetLocationTrainStation() throws PWCGException
    {
        GroupManager groupData =  PWCGContext.getInstance().getCurrentMap().getGroupManager();
        Block station = groupData.getRailroadStationFinder().getNearbyTrainPosition(
                targetDefinition.getTargetCountry().getSide(), 
                targetDefinition.getDate(), 
                missionCenter,
                currentSearchRadius);
        
        if (station != null)
        {
            targetLocation = station.getPosition();
            targetOrientation = station.getOrientation();
        }
    }

    private void getTargetLocationBridge() throws PWCGException
    {
        GroupManager groupManager = PWCGContext.getInstance().getCurrentMap().getGroupManager();    
        Bridge bridge = groupManager.getBridgeFinder().findBridgeForSideWithinRadius(
                targetDefinition.getTargetCountry().getSide(), 
                targetDefinition.getDate(), 
                missionCenter,
                currentSearchRadius);
        
        if (bridge != null)
        {
            targetLocation = bridge.getPosition();
            targetOrientation = bridge.getOrientation();
        }
    }

    private void getTargetLocationRiver() throws PWCGException
    {
        DrifterManager drifterManager = PWCGContext.getInstance().getCurrentMap().getDrifterManager();
        PWCGLocation selectedBargePosition = drifterManager.getBargePositions().getSelectedLocationWithinRadiusBySide(
                targetDefinition.getTargetCountry().getSide(), 
                targetDefinition.getDate(), 
                missionCenter,
                currentSearchRadius);

        if (selectedBargePosition != null)
        {
            targetLocation = selectedBargePosition.getPosition();
            targetOrientation = selectedBargePosition.getOrientation();
        }
    }

    private void getTargetLocationAirfield() throws PWCGException
    {
        AirfieldManager airfieldManager = PWCGContext.getInstance().getCurrentMap().getAirfieldManager();
        PositionFinder<IAirfield> positionFinder = new PositionFinder<IAirfield>();
        List<IAirfield> potentialTargetAirfields = airfieldManager.getAirFieldsForSide(targetDefinition.getDate(), targetDefinition.getTargetCountry().getSide());
        IAirfield airfield = positionFinder.selectPositionWithinExpandingRadius(
                potentialTargetAirfields, 
                missionCenter, 
                currentSearchRadius, 
                targetDefinition.getMaximumRadius());

        if (airfield != null)
        {
            targetLocation = airfield.getPosition();
            targetOrientation = airfield.getOrientation();
        }
    }

    private void getTargetLocationShippingLanes() throws PWCGException
    {
        ShippingLaneManager shippingLaneManager = PWCGContext.getInstance().getCurrentMap().getShippingLaneManager();        
        ShippingLane shippingLane = shippingLaneManager.getClosestShippingLane(missionCenter);
        if (shippingLane != null)
        {
            targetLocation = shippingLane.getShippingLaneBox().getCoordinateInBox();
            targetOrientation = Orientation.createRandomOrientation();
        }
    }

    private void getTargetLocationBehindEnemyLines() throws PWCGException
    {
        int minDistanceBehindLines = 2000;
        int maxDistanceBehindLines = 6000;

        targetLocation = getBehindLinesTargetPosition(minDistanceBehindLines, maxDistanceBehindLines);
        targetOrientation = Orientation.createRandomOrientation();
    }

    public Coordinate getBehindLinesTargetPosition(int minDistanceBehindLines, int maxDistanceBehindLines) throws PWCGException 
    {
        FrontLinesForMap frontLinesForMap =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(targetDefinition.getDate());
        Coordinate behindLinesPosition = frontLinesForMap.findPositionBehindLinesForSide(
                missionCenter, 
                targetDefinition.getPreferredRadius(), 
                minDistanceBehindLines, 
                maxDistanceBehindLines, 
                targetDefinition.getTargetCountry().getSide());

        return behindLinesPosition;
    }

    public Coordinate getTargetLocation()
    {
        return targetLocation;
    }

    public Orientation getTargetOrientation()
    {
        return targetOrientation;
    }
}
