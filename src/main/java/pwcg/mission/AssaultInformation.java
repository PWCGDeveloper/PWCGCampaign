package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.target.unit.GroundUnitCollectionType;
import pwcg.campaign.target.unit.GroundUnitType;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.unittypes.GroundUnit;

public class AssaultInformation
{
    private ICountry aggressor;
    private ICountry defender;
    private Coordinate assaultPosition = null;
    private Coordinate defensePosition = null;
    private GroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION);
    private CoordinateBox battleBox;

    public ICountry getAggressor()
    {
        return aggressor;
    }

    public void setAggressor(ICountry aggressor)
    {
        this.aggressor = aggressor;
    }

    public ICountry getDefender()
    {
        return defender;
    }

    public void setDefender(ICountry defender)
    {
        this.defender = defender;
    }

    public Coordinate getAssaultPosition()
    {
        return assaultPosition;
    }

    public void setAssaultPosition(Coordinate assaultPosition)
    {
        this.assaultPosition = assaultPosition;
    }

    public Coordinate getDefensePosition()
    {
        return defensePosition;
    }

    public void setDefensePosition(Coordinate defensePosition)
    {
        this.defensePosition = defensePosition;
    }

    public void addGroundUnit(GroundUnitType groundUnitType, GroundUnit groundUnit) throws PWCGException
    {
        groundUnitCollection.addGroundUnit(groundUnitType, groundUnit);
        buildBattleBox();
    }
    
    public boolean isNearBattle(Coordinate coordinate) throws PWCGException
    {
        if (battleBox == null)
        {
            buildBattleBox();
        }
        
        return battleBox.isInBox(coordinate);
    }
    
    public CoordinateBox buildBattleBox() throws PWCGException
    {
        List<Coordinate> coordinates = new ArrayList<>();
        for (GroundUnit groundUnit : groundUnitCollection.getAllGroundUnits())
        {
            coordinates.add(groundUnit.getHomePosition());
        }

        battleBox = CoordinateBox.coordinateBoxFromCoordinateList(coordinates);
        battleBox.expandBox(2000);
        return battleBox;
    }

    public void finalizeBattle()
    {
        groundUnitCollection.setTargetsForAssaultGroundUnits();        
    }

    public GroundUnitCollection getGroundUnitCollection()
    {
        return groundUnitCollection;
    }
    
    
    public Orientation getAssaultOrientation() throws PWCGException
    {
        double angleToDefensePosition = MathUtils.calcAngle(assaultPosition, defensePosition);
        return new Orientation(angleToDefensePosition);
    }
    
    public Orientation getDefenseOrientation() throws PWCGException
    {
        double angleToAssaultPosition = MathUtils.calcAngle(defensePosition, assaultPosition);
        return new Orientation(angleToAssaultPosition);
    }

}
