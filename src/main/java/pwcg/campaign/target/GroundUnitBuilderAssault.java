package pwcg.campaign.target;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAssaultGenerator;
import pwcg.campaign.factory.AssaultGeneratorFactory;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.AssaultInformation;
import pwcg.mission.Mission;
import pwcg.mission.ground.BattleSize;
import pwcg.mission.ground.GroundUnitCollection;

public class GroundUnitBuilderAssault
{
    public static GroundUnitCollection createAssault(Campaign campaign, Mission mission, TargetDefinition targetDefinition) throws PWCGException 
    {
        BattleSize battleSize = BattleSize.BATTLE_SIZE_TINY;      
        if (targetDefinition.isPlayerTarget())
        {
            ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
            String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
            if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
            {
                battleSize = BattleSize.BATTLE_SIZE_SKIRMISH;      
            }
            else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
            {
                int roll = RandomNumberGenerator.getRandom(100);
                if (roll < 50)
                {
                    battleSize = BattleSize.BATTLE_SIZE_SKIRMISH;      
                }
                else
                {
                    battleSize = BattleSize.BATTLE_SIZE_ASSAULT;
                }
            }
            else
            {
                int roll = RandomNumberGenerator.getRandom(100);
                if (roll < 30)
                {
                    battleSize = BattleSize.BATTLE_SIZE_SKIRMISH;      
                }
                else if (roll < 80)
                {
                    battleSize = BattleSize.BATTLE_SIZE_ASSAULT;
                }
                else
                {
                    battleSize = BattleSize.BATTLE_SIZE_OFFENSIVE;
                }
            }
        }
        
       IAssaultGenerator assaultGenerator = AssaultGeneratorFactory.createAssaultGenerator(campaign, mission, campaign.getDate());
       AssaultInformation missionBattle = assaultGenerator.generateAssault(targetDefinition, battleSize);
       
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
