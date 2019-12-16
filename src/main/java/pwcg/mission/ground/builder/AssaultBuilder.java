package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.AssaultDefinition;
import pwcg.mission.target.AssaultDefinitionGenerator;
import pwcg.mission.target.TacticalTarget;
import pwcg.mission.target.TargetDefinition;

public class AssaultBuilder
{
    public static IGroundUnitCollection generateAssault(Mission mission, TargetDefinition targetDefinition) throws PWCGException
    {
        AssaultDefinitionGenerator assaultDefinitionGenerator = new AssaultDefinitionGenerator(mission.getCampaign());
        List<AssaultDefinition> assaultDefinitions = assaultDefinitionGenerator.generateAssaultDefinition(targetDefinition);

        
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION, 
                "Battle", 
                TacticalTarget.TARGET_ASSAULT,
                Coalition.getCoalitions());

        IGroundUnitCollection battleUnitCollection = new GroundUnitCollection (groundUnitCollectionData);

        List<IGroundUnit> primaryAssaultSegmentGroundUnits = new ArrayList<>();
        
        for (AssaultDefinition assaultDefinition : assaultDefinitions)
        {
            AssaultSegmentBuilder assaultSegmentBuilder = new AssaultSegmentBuilder(mission.getCampaign(), assaultDefinition);
            IGroundUnitCollection assaultSegmentUnits = assaultSegmentBuilder.generateAssaultSegment();
            primaryAssaultSegmentGroundUnits.add(assaultSegmentUnits.getPrimaryGroundUnit());
            battleUnitCollection.merge(assaultSegmentUnits);
        }

        mission.registerAssault(assaultDefinitions.get(0));
        battleUnitCollection.setPrimaryGroundUnit(primaryAssaultSegmentGroundUnits.get(0));
        battleUnitCollection.finishGroundUnitCollection();
        return battleUnitCollection;
    }
 }
