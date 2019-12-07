package pwcg.mission.ground.org;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;
import pwcg.mission.IUnit;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.group.SelfDeactivatingCheckZone;
import pwcg.mission.target.TacticalTarget;

public class GroundUnitCollection implements IGroundUnitCollection
{
    private final static int GROUND_UNIT_SPAWN_DISTANCE = 20000;
    
    protected GroundUnitCollectionType groundUnitCollectionType;
    protected String groundUnitCollectionName;
    protected GroundUnitInformation pwcgGroundUnitInformation;

    private int index = IndexGenerator.getInstance().getNextIndex();  

    private List<IGroundUnit> groundUnits = new ArrayList<> ();
    private SelfDeactivatingCheckZone selfDeactivatingCheckZone;
    private List<Coalition> triggerCoalitions;

    public GroundUnitCollection (GroundUnitCollectionType groundUnitCollectionType, String groundUnitCollectionName, List<Coalition> triggerCoalitions)
    {
        this.groundUnitCollectionType = groundUnitCollectionType;
        this.groundUnitCollectionName = groundUnitCollectionName;
        this.triggerCoalitions = triggerCoalitions;
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
    
    public void finishGroundUnitCollection()
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


    private void createCheckZone()
    {
        selfDeactivatingCheckZone = new SelfDeactivatingCheckZone(pwcgGroundUnitInformation.getPosition(), GROUND_UNIT_SPAWN_DISTANCE);
        selfDeactivatingCheckZone.setCheckZoneCoalitions(triggerCoalitions);
    }

    private void createTargetAssociations()
    {
        for (IGroundUnit groundUnit : groundUnits)
        {
            selfDeactivatingCheckZone.setCheckZoneTarget(groundUnit. getEntryPoint());
        }
    }

    @Override
    public List<IGroundUnit> getGroundUnits()
    {
        return groundUnits;
    }

    @Override
    public Coordinate getPosition()
    {
        return pwcgGroundUnitInformation.getPosition();
    }

    @Override
    public TacticalTarget getTargetType()
    {
        return pwcgGroundUnitInformation.getTargetType();
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
            
            writer.write("  Name = \"" + groundUnitCollectionName + "\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"" + groundUnitCollectionName + "\";");
            writer.newLine();

            selfDeactivatingCheckZone.write(writer);
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
    public int getIndex()
    {
        return index;
    }

    @Override
    public ICountry getCountry() throws PWCGException
    {
        return pwcgGroundUnitInformation.getCountry();
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
        return groundUnitCollectionType;
    }

    @Override
    public void addGroundUnit(IGroundUnit groundUnit)
    {
        groundUnits.add(groundUnit);
    }
}
