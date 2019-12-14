package pwcg.mission.ground.org;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;
import pwcg.mission.IUnit;
import pwcg.mission.mcu.group.MissionBeginSelfDeactivatingCheckZone;
import pwcg.mission.target.TacticalTarget;

public class GroundUnitCollection implements IGroundUnitCollection
{
    private final static int GROUND_UNIT_SPAWN_DISTANCE = 20000;
    
    private GroundUnitCollectionData groundUnitCollectionData;

    private int index = IndexGenerator.getInstance().getNextIndex();  

    private List<IGroundUnit> groundUnits = new ArrayList<> ();
    private MissionBeginSelfDeactivatingCheckZone missionBeginUnit;

    public GroundUnitCollection (GroundUnitCollectionData groundUnitCollectionData)
    {
        this.groundUnitCollectionData = groundUnitCollectionData;
    }

    public Coordinate getTargetCoordinatesFromGroundUnits(Side side) throws PWCGException
    {
        GroundUnitCollectionTargetFinder groundUnitCollectionTargetFinder = new GroundUnitCollectionTargetFinder(this);
        IGroundUnit targetUnit = groundUnitCollectionTargetFinder.findTargetUnit(side);
        Coordinate targetCoordinates = targetUnit.getPosition();
        return targetCoordinates;
    }
    
    public void merge(IGroundUnitCollection relatedGroundCollection)
    {
        groundUnits.addAll(relatedGroundCollection.getGroundUnits());
    }
    
    public void finishGroundUnitCollection() throws PWCGException
    {
        createCheckZone();
        createTargetAssociations();
    }

    public List<IGroundUnit> getGroundUnitsForSide(Side side) throws PWCGException
    {
        List<IGroundUnit> groundUnitsForSide = new ArrayList<>();
        for (IGroundUnit groundUnit : groundUnits)
        {
            if (groundUnit.getCountry().getSide() == side)
            {
                groundUnitsForSide.add(groundUnit);
            }
         }
        return groundUnitsForSide;
    }


    private void createCheckZone() throws PWCGException
    {
        missionBeginUnit = new MissionBeginSelfDeactivatingCheckZone(getPosition(), GROUND_UNIT_SPAWN_DISTANCE);
        missionBeginUnit.setCheckZoneCoalitions(groundUnitCollectionData.getTriggerCoalitions());
    }

    private void createTargetAssociations()
    {
        for (IGroundUnit groundUnit : groundUnits)
        {
            missionBeginUnit.linkCheckZoneTarget(groundUnit. getEntryPoint());
        }
    }

    @Override
    public List<IGroundUnit> getGroundUnits()
    {
        return groundUnits;
    }

    @Override
    public Coordinate getPosition() throws PWCGException
    {
        GroundUnitCollectionTargetFinder groundUnitCollectionTargetFinder = new GroundUnitCollectionTargetFinder(this);
        IGroundUnit targetUnit = groundUnitCollectionTargetFinder.findTargetUnit();
        Coordinate targetCoordinates = targetUnit.getPosition();
        return targetCoordinates;
    }

    @Override
    public Coordinate getPosition(Side side) throws PWCGException
    {
        GroundUnitCollectionTargetFinder groundUnitCollectionTargetFinder = new GroundUnitCollectionTargetFinder(this);
        IGroundUnit targetUnit = groundUnitCollectionTargetFinder.findTargetUnit(side);
        Coordinate targetCoordinates = targetUnit.getPosition();
        return targetCoordinates;
    }

    @Override
    public TacticalTarget getTargetType()
    {
        return groundUnitCollectionData.getTargetType();
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            writer.write("  Name = \"" + groundUnitCollectionData.getName() + "\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"" + groundUnitCollectionData.getName() + "\";");
            writer.newLine();

            missionBeginUnit.write(writer);
            for (IGroundUnit groundUnit : groundUnits)
            {
                groundUnit.write(writer);
            }
            
            writer.write("}");
            writer.newLine();
        }
        catch (Exception e)
        {
            Logger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }

    
    @Override
    public void validate() throws PWCGException
    {
        missionBeginUnit.validate();
        for (IGroundUnit groundUnit : groundUnits)
        {
            int entryPoint = groundUnit.getEntryPoint();
            missionBeginUnit.validateTarget(entryPoint);
            groundUnit.validate();
        }
    }
    
    @Override
    public int getIndex()
    {
        return index;
    }

    @Override
    public List<IUnit> getLinkedUnits()
    {
        return new ArrayList<>();
    }

    @Override
    public void addLinkedUnit(IUnit unit) throws PWCGException
    {        
    }

    @Override
    public GroundUnitCollectionType getGroundUnitCollectionType()
    {
        return groundUnitCollectionData.getGroundUnitCollectionType();
    }

    @Override
    public void addGroundUnit(IGroundUnit groundUnit)
    {
        groundUnits.add(groundUnit);
    }
}
