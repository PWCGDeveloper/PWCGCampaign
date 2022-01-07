package pwcg.mission.ground.org;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.Mission;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.group.AirGroundAttackTargetMcuSequence;
import pwcg.mission.mcu.group.MissionBeginCheckZoneBase;
import pwcg.mission.mcu.group.MissionBeginSelfDeactivatingCheckZone;
import pwcg.mission.target.TargetType;

public class GroundUnitCollection
{
    private Campaign campaign;
    private GroundUnitCollectionData groundUnitCollectionData;
    private int index = IndexGenerator.getInstance().getNextIndex();
    private MissionBeginCheckZoneBase missionBeginUnit;
    private IGroundUnit primaryGroundUnit;
    private List<IGroundUnit> groundUnits = new ArrayList<>();
    private String groundUnitName;
    protected List<AirGroundAttackTargetMcuSequence> attackVehicleMcuGroups = new ArrayList<>();

    public GroundUnitCollection(Campaign campaign, String groundUnitName, GroundUnitCollectionData groundUnitCollectionData)
    {
        this.campaign = campaign;
        this.groundUnitName = groundUnitName;
        this.groundUnitCollectionData = groundUnitCollectionData;
    }

    public void merge(GroundUnitCollection relatedGroundCollection) throws PWCGException
    {
        groundUnits.addAll(relatedGroundCollection.getGroundUnits());
    }

    public void finishGroundUnitCollection() throws PWCGException
    {
        createCheckZone();
        createTargetAssociations();
    }

    public void setCheckZoneTriggerDistance(int zoneMeters)
    {
        missionBeginUnit.setCheckZoneTriggerDistance(zoneMeters);
    }

    public void setCheckZoneTriggerUnit(int unitIndex)
    {
        missionBeginUnit.setCheckZoneTriggerObject(unitIndex);
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

    public List<IGroundUnit> getInterestingGroundUnitsForSide(Side side) throws PWCGException
    {
        List<IGroundUnit> groundUnitsForSide = new ArrayList<>();
        for (IGroundUnit groundUnit : groundUnits)
        {
            if (groundUnit.getCountry().getSide() == side)
            {
                if (groundUnit.getGroundUnitType() != GroundUnitType.AAA_UNIT && groundUnit.getGroundUnitType() != GroundUnitType.STATIC_UNIT)
                {
                    groundUnitsForSide.add(groundUnit);
                }
            }
        }
        
        if (groundUnitsForSide.size() == 0)
        {
            groundUnitsForSide = getGroundUnitsForSide(side);
        }
        
        return groundUnitsForSide;
    }

    public int getUnitCount()
    {
        int unitCount = 0;
        for (IGroundUnit groundUnit : groundUnits)
        {
            unitCount += groundUnit.getUnitCount();
        }
        return unitCount;
    }

    private void createCheckZone() throws PWCGException
    {
        
        int groundUnitSpawnDistance = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.GroundUnitSpawnDistanceKey);

        missionBeginUnit = new MissionBeginSelfDeactivatingCheckZone("Check Zone " + groundUnitName, getPosition(), groundUnitSpawnDistance);
        missionBeginUnit.setCheckZoneCoalitions(groundUnitCollectionData.getTriggerCoalitions());
    }

    private void createTargetAssociations()
    {
        for (IGroundUnit groundUnit : groundUnits)
        {
            missionBeginUnit.linkCheckZoneTarget(groundUnit.getEntryPoint());
        }
    }

    public List<IGroundUnit> getGroundUnits()
    {
        return groundUnits;
    }

    public List<McuSpawn> getSpawns()
    {
        List<McuSpawn> groundUnitCollectionVehicleSpawns = new ArrayList<>();
        for (IGroundUnit groundUnit : groundUnits)
        {
            List<McuSpawn> groundUnitVehicleSpawns = groundUnit.getSpawns();
            groundUnitCollectionVehicleSpawns.addAll(groundUnitVehicleSpawns);
        }
        return groundUnitCollectionVehicleSpawns;
    }

    public Coordinate getPosition() throws PWCGException
    {
        GroundUnitCollectionTargetFinder groundUnitCollectionTargetFinder = new GroundUnitCollectionTargetFinder(this);
        IGroundUnit targetUnit = groundUnitCollectionTargetFinder.findTargetUnit();
        Coordinate targetCoordinates = targetUnit.getPosition();
        return targetCoordinates;
    }

    public TargetType getTargetType()
    {
        return groundUnitCollectionData.getTargetType();
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        if (groundUnits.isEmpty())
        {
            return;
        }
        
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

            writeAttackTargetEntries(writer);

            writer.write("}");
            writer.newLine();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }

    private void writeAttackTargetEntries(BufferedWriter writer) throws PWCGException
    {
        for (AirGroundAttackTargetMcuSequence attackVehicleMcuGroup : attackVehicleMcuGroups)
        {
            attackVehicleMcuGroup.write(writer);
        }
    }

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

    public GroundUnitCollectionType getGroundUnitCollectionType()
    {
        return groundUnitCollectionData.getGroundUnitCollectionType();
    }

    public void addGroundUnit(IGroundUnit groundUnit) throws PWCGException
    {
        groundUnits.add(groundUnit);
    }

    public IGroundUnit getPrimaryGroundUnit()
    {
        if (primaryGroundUnit == null)
        {
            return groundUnits.get(0);
        }
        else
        {
            return primaryGroundUnit;
        }
    }

    public void setPrimaryGroundUnit(IGroundUnit groundUnit)
    {
        primaryGroundUnit = groundUnit;
    }

    public void triggerGroundUnitCollection(Mission mission) throws PWCGException
    {
        missionBeginUnit.triggerMissionBeginCheckZone(mission);
    }

    public int getIndex()
    {
        return index;
    }

    public String getName()
    {
        return groundUnitName;
    }

    public void removeExtraUnits(Coordinate coordinate, int keepRadius) throws PWCGException
    {
        List<IGroundUnit> groundUnitsToKeep = new ArrayList<>();
        for (IGroundUnit groundUnit : groundUnits)
        {
            double distance = MathUtils.calcDist(coordinate, groundUnit.getPosition());
            if (distance < keepRadius)
            {
                groundUnitsToKeep.add(groundUnit);
            }
        }
        groundUnits = groundUnitsToKeep;
    }
}
