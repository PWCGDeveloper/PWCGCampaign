package pwcg.mission.ground.factory;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.AssaultDefinition;
import pwcg.mission.target.AssaultDefinitionGenerator;
import pwcg.mission.target.TargetDefinition;

public class AssaultBuilder
{
    public static IGroundUnitCollection generateAssault(Mission mission, TargetDefinition targetDefinition) throws PWCGException
    {
        AssaultDefinitionGenerator assaultDefinitionGenerator = new AssaultDefinitionGenerator(mission.getCampaign());
        List<AssaultDefinition> assaultDefinitions = assaultDefinitionGenerator.generateAssaultDefinition(targetDefinition);

        IGroundUnitCollection battleUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION, "Battle", Coalition.getCoalitions());
        for (AssaultDefinition assaultDefinition : assaultDefinitions)
        {
            AssaultSegmentBuilder assaultSegmentBuilder = new AssaultSegmentBuilder(mission.getCampaign(), assaultDefinition);
            IGroundUnitCollection assaultSegmentUnits = assaultSegmentBuilder.generateAssaultSegment();
            battleUnitCollection.merge(assaultSegmentUnits);
        }
        mission.registerAssault(assaultDefinitions.get(0));
        return battleUnitCollection;
    }
 }
