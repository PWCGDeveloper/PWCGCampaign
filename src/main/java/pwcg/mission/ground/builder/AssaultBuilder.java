package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.AssaultDefinition;
import pwcg.mission.target.AssaultDefinitionGenerator;
import pwcg.mission.target.TargetType;

public class AssaultBuilder
{
    public static IGroundUnitCollection generateAssault(Mission mission, Coordinate battleLocation) throws PWCGException
    {
        AssaultDefinitionGenerator assaultDefinitionGenerator = new AssaultDefinitionGenerator(mission.getCampaign());
        List<AssaultDefinition> assaultDefinitions = assaultDefinitionGenerator.generateAssaultDefinition(battleLocation);
        
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION, 
                "Battle", 
                TargetType.TARGET_INFANTRY,
                Coalition.getCoalitions());

        IGroundUnitCollection battleUnitCollection = new GroundUnitCollection ("Assault", groundUnitCollectionData);

        List<IGroundUnit> primaryAssaultSegmentGroundUnits = new ArrayList<>();
        
        for (AssaultDefinition assaultDefinition : assaultDefinitions)
        {
            AssaultSegmentBuilder assaultSegmentBuilder = new AssaultSegmentBuilder(mission, assaultDefinition);
            IGroundUnitCollection assaultSegmentUnits = assaultSegmentBuilder.generateAssaultSegment();
            primaryAssaultSegmentGroundUnits.add(assaultSegmentUnits.getPrimaryGroundUnit());
            battleUnitCollection.merge(assaultSegmentUnits);
            mission.registerAssault(assaultDefinition);
        }

        battleUnitCollection.setPrimaryGroundUnit(primaryAssaultSegmentGroundUnits.get(0));
        battleUnitCollection.finishGroundUnitCollection();
        return battleUnitCollection;
    }
 }
