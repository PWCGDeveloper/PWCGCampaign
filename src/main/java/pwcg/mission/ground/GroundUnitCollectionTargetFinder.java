package pwcg.mission.ground;

import java.util.List;
import java.util.Map;

import pwcg.campaign.api.Side;
import pwcg.campaign.target.unit.GroundUnitCollectionType;
import pwcg.campaign.target.unit.GroundUnitType;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.unittypes.GroundUnit;

public class GroundUnitCollectionTargetFinder
{
    private GroundUnitCollection groundUnitCollection;
    private Map<GroundUnitType, List<GroundUnit>> groundUnitsForSide;

    public GroundUnitCollectionTargetFinder(GroundUnitCollection groundUnitCollection)
    {
        this.groundUnitCollection = groundUnitCollection;
    }
    
    public GroundUnit findTargetUnit(Side side) throws PWCGException
    {
        groundUnitsForSide = getUnitsForSide(side);
        if (groundUnitCollection.getGroundUnitCollectionType() == GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION)
        {
            return findInfantryTargetUnit();
        }
        else if (groundUnitCollection.getGroundUnitCollectionType() == GroundUnitCollectionType.BALLOON_GROUND_UNIT_COLLECTION)
        {
            return findBalloonTargetUnit();
        }
        else if (groundUnitCollection.getGroundUnitCollectionType() == GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION)
        {
            return findTransportTargetUnit();
        }
        else if (groundUnitCollection.getGroundUnitCollectionType() == GroundUnitCollectionType.STATIC_GROUND_UNIT_COLLECTION)
        {
            return findStaticTargetUnit();
        }
        else 
        {
            throw new PWCGException("No target unit found for unknown ground collection type " + groundUnitCollection.getGroundUnitCollectionType());
        }
    }

    private GroundUnit findInfantryTargetUnit() throws PWCGException
    {
        if (groundUnitsForSide.containsKey(GroundUnitType.TANK_UNIT))
        {
            return groundUnitsForSide.get(GroundUnitType.TANK_UNIT).get(0);
        }
        else if (groundUnitsForSide.containsKey(GroundUnitType.INFANTRY_UNIT))
        {
            return groundUnitsForSide.get(GroundUnitType.INFANTRY_UNIT).get(0);
        }
        else if (groundUnitsForSide.containsKey(GroundUnitType.ARTILLERY_UNIT))
        {
            return groundUnitsForSide.get(GroundUnitType.ARTILLERY_UNIT).get(0);
        }
        else if (groundUnitsForSide.containsKey(GroundUnitType.MG_UNIT))
        {
            return groundUnitsForSide.get(GroundUnitType.MG_UNIT).get(0);
        }
        else if (groundUnitsForSide.containsKey(GroundUnitType.AAA_ARTY_UNIT))
        {
            return groundUnitsForSide.get(GroundUnitType.AAA_ARTY_UNIT).get(0);
        }
        else if (groundUnitsForSide.containsKey(GroundUnitType.AAA_MG_UNIT))
        {
            return groundUnitsForSide.get(GroundUnitType.AAA_MG_UNIT).get(0);
        }
        else if (groundUnitsForSide.containsKey(GroundUnitType.FLARE_UNIT))
        {
            return groundUnitsForSide.get(GroundUnitType.FLARE_UNIT).get(0);
        }
        else 
        {
            throw new PWCGException("No target unit found for infantry ground collection");
        }
    }

    private GroundUnit findBalloonTargetUnit() throws PWCGException
    {
        if (groundUnitsForSide.containsKey(GroundUnitType.BALLOON_UNIT))
        {
            return groundUnitsForSide.get(GroundUnitType.BALLOON_UNIT).get(0);
        }
        else 
        {
            throw new PWCGException("No target unit found for balloon ground collection");
        }
    }

    private GroundUnit findTransportTargetUnit() throws PWCGException
    {
        if (groundUnitsForSide.containsKey(GroundUnitType.TRANSPORT_UNIT))
        {
            return groundUnitsForSide.get(GroundUnitType.TRANSPORT_UNIT).get(0);
        }
        else 
        {
            throw new PWCGException("No target unit found for transport ground collection");
        }
    }

    private GroundUnit findStaticTargetUnit() throws PWCGException
    {
        if (groundUnitsForSide.containsKey(GroundUnitType.STATIC_UNIT))
        {
            return groundUnitsForSide.get(GroundUnitType.STATIC_UNIT).get(0);
        }
        else 
        {
            throw new PWCGException("No target unit found for transport ground collection");
        }
    }

    private Map<GroundUnitType, List<GroundUnit>> getUnitsForSide(Side side)
    {
        if (side == Side.ALLIED)
        {
            return groundUnitCollection.getAlliedUnits();
        }
        else
        {
            return groundUnitCollection.getAxisUnits();
        }
    }
}
