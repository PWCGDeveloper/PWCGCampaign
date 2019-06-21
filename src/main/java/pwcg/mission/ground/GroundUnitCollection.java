package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.Side;
import pwcg.campaign.target.TargetDefinition;
import pwcg.campaign.target.unit.GroundUnitCollectionType;
import pwcg.campaign.target.unit.GroundUnitType;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.unittypes.GroundDirectFireUnit;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.vehicle.IVehicle;

public class GroundUnitCollection
{
    private GroundUnitCollectionType groundUnitCollectionType;
    private Map<GroundUnitType, List<GroundUnit>> alliedUnits = new HashMap<> ();
    private Map<GroundUnitType, List<GroundUnit>> axisUnits = new HashMap<> ();
    private TargetDefinition targetDefinition = null;

    public GroundUnitCollection (GroundUnitCollectionType groundUnitCollectionType)
    {
        this.groundUnitCollectionType = groundUnitCollectionType;
    }
    
    public void addGroundUnit(GroundUnitType grountUnitType, GroundUnit groundUnit) throws PWCGException
    {
        Map<GroundUnitType, List<GroundUnit>> unitsForSide = axisUnits;
        if (groundUnit.getPwcgGroundUnitInformation().getCountry().getSide() == Side.ALLIED)
        {
            unitsForSide = alliedUnits;
        }

        if (!unitsForSide.containsKey(grountUnitType))
        {
            List<GroundUnit> groundUnitsForKey = new ArrayList<GroundUnit>();
            unitsForSide.put(grountUnitType, groundUnitsForKey);
        }

        List<GroundUnit> groundUnitsForKey = unitsForSide.get(grountUnitType);
        groundUnitsForKey.add(groundUnit);
    }

    public void mergeGroundUnitCollection(GroundUnitCollection groundUnitCollection) throws PWCGException
    {
        alliedUnits.putAll(groundUnitCollection.alliedUnits);
        axisUnits.putAll(groundUnitCollection.axisUnits);
    }

    public void setTargetsForAssaultGroundUnits() 
    { 
        setTargetsForAssaultGroundUnitsBySide (getAllAxisGroundUnits(), getAllAlliedGroundUnits());
        setTargetsForAssaultGroundUnitsBySide (getAllAlliedGroundUnits(), getAllAxisGroundUnits());
    }
    
    private void setTargetsForAssaultGroundUnitsBySide(List<GroundUnit> sourceGroundUnits, List<GroundUnit> targetGroundUnits) 
    {
        for (GroundUnit sourceGroundUnit : sourceGroundUnits)
        {
            if (sourceGroundUnit instanceof GroundDirectFireUnit)
            {
                GroundDirectFireUnit directFireUnit = (GroundDirectFireUnit)sourceGroundUnit;
                for (GroundUnit targetGroundUnit : targetGroundUnits)
                {
                    for (IVehicle unit : targetGroundUnit.getVehicles())
                    {
                        directFireUnit.addTarget(unit.getEntity().getIndex());
                    }
                }
            }
        }
    }

    public List<GroundUnit> getAllAlliedGroundUnits()
    {
        List<GroundUnit> alliedGrountUnits = new ArrayList<GroundUnit>();
        for (List<GroundUnit> alliedUnitsByType : alliedUnits.values())
        {
            alliedGrountUnits.addAll(alliedUnitsByType);
        }
        
        return alliedGrountUnits;
    }

    public List<GroundUnit> getAllAxisGroundUnits()
    {
        List<GroundUnit> axisGrountUnits = new ArrayList<GroundUnit>();
        for (List<GroundUnit> axisUnitsByType : axisUnits.values())
        {
            axisGrountUnits.addAll(axisUnitsByType);
        }
        
        return axisGrountUnits;
    }

    public List<GroundUnit> getAllGroundUnits()
    {
        List<GroundUnit> allGrountUnits = getAllAlliedGroundUnits();
        allGrountUnits.addAll( getAllAxisGroundUnits());
        return allGrountUnits;
    }
    
    public Map<GroundUnitType, List<GroundUnit>> getAlliedUnits()
    {
        return alliedUnits;
    }

    public Map<GroundUnitType, List<GroundUnit>> getAxisUnits()
    {
        return axisUnits;
    }

    public GroundUnit getGroundUnitByType(GroundUnitType grountUnitType, Side targetSide)
    {
        Map<GroundUnitType, List<GroundUnit>> unitsForSide = axisUnits;
        if (targetSide == Side.ALLIED)
        {
            unitsForSide = alliedUnits;
        }
        
        if (unitsForSide.containsKey(grountUnitType))
        {
            List<GroundUnit> groundUnitsForKey = unitsForSide.get(grountUnitType);
            if (!groundUnitsForKey.isEmpty())
            {
                return groundUnitsForKey.get(0);
            }
        }
        
        return null;
    }

    public TargetDefinition getTargetDefinition()
    {
        return targetDefinition;
    }

    public void setTargetDefinition(TargetDefinition targetDefinition)
    {
        this.targetDefinition = targetDefinition;
    }

    public Coordinate getTargetCoordinatesFromGroundUnits(Side side) throws PWCGException
    {
        GroundUnitCollectionTargetFinder groundUnitCollectionTargetFinder = new GroundUnitCollectionTargetFinder(this);
        GroundUnit targetUnit = groundUnitCollectionTargetFinder.findTargetUnit(side);
        Coordinate targetCoordinates = targetUnit.getPwcgGroundUnitInformation().getPosition();
        return targetCoordinates;
    }

    public GroundUnitCollectionType getGroundUnitCollectionType()
    {
        return groundUnitCollectionType;
    }
}
