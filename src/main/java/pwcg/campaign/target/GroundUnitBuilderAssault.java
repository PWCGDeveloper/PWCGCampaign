package pwcg.campaign.target;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAssaultGenerator;
import pwcg.campaign.factory.AssaultGeneratorFactory;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionBattle;
import pwcg.mission.ground.AssaultGenerator.BattleSize;
import pwcg.mission.ground.GroundUnitCollection;

public class GroundUnitBuilderAssault
{
    public static GroundUnitCollection createAssault(Campaign campaign, Mission mission, TargetDefinition targetDefinition) throws PWCGException 
    {
        BattleSize battleSize = BattleSize.BATTLE_SIZE_TINY;            
        if (targetDefinition.isPlayerTarget())
        {
            battleSize = BattleSize.BATTLE_SIZE_ASSAULT;            
        }
        
       IAssaultGenerator assaultGenerator = AssaultGeneratorFactory.createAssaultGenerator(campaign, mission, campaign.getDate());
       MissionBattle missionBattle = assaultGenerator.generateAssault(targetDefinition, battleSize);
       
       GroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION);
       groundUnitCollection = missionBattle.getGroundUnitCollection();
       
       List<GroundUnitType> targetUnitTypes = new ArrayList<>();
       targetUnitTypes.add(GroundUnitType.TANK_UNIT);
       targetUnitTypes.add(GroundUnitType.INFANTRY_UNIT);
       targetUnitTypes.add(GroundUnitType.TRANSPORT_UNIT);
       targetUnitTypes.add(GroundUnitType.ARTILLERY_UNIT);
       targetUnitTypes.add(GroundUnitType.MG_UNIT);
       targetUnitTypes.add(GroundUnitType.FLARE_UNIT);
       targetUnitTypes.add(GroundUnitType.STATIC_UNIT);
       targetUnitTypes.add(GroundUnitType.AAA_ARTY_UNIT);
       targetUnitTypes.add(GroundUnitType.AAA_MG_UNIT);
       
       return groundUnitCollection;
    }
}
