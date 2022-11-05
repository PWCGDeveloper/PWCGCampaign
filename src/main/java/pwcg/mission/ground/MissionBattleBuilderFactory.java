package pwcg.mission.ground;

import pwcg.campaign.battle.AmphibiousAssault;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.builder.AmphibiousAssaultBuilder;
import pwcg.mission.ground.builder.IBattleBuilder;

public class MissionBattleBuilderFactory
{   
    public static IBattleBuilder getBattleBuilder(Mission mission) throws PWCGException
    {
        AmphibiousAssault amphibiousAssault = PWCGContext.getInstance().getMap(mission.getCampaignMap()).getAmphibiousAssaultManager().getActiveAmphibiousAssault(mission);
        if (amphibiousAssault != null)
        {
            return new AmphibiousAssaultBuilder(mission, amphibiousAssault);
        }
        
        if (mission.getSkirmish() != null && mission.getSkirmish().isCargoRouteBattle())
        {
            return new CargoRouteBattleBuilder(mission);
        }
        
        if (mission.getSkirmish() != null && mission.getSkirmish().isShipEncounterZoneBattle())
        {
            return new ShippingEncounterBattleBuilder(mission);
        }

        if (PWCGContext.getInstance().getMap(mission.getCampaignMap()).isNoDynamicBattlePeriod(mission.getCampaign().getDate()))
        {
            return new NoBattleBuilder();
        }
        
        return new MissionBattleBuilder(mission);
    }
}
