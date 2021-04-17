package pwcg.mission.ground;

import pwcg.campaign.api.Side;
import pwcg.campaign.battle.AmphibiousAssault;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.ground.builder.AmphibiousAssaultBuilder;
import pwcg.mission.ground.builder.IBattleBuilder;

public class MissionBattleBuilderFactory
{   
    public static IBattleBuilder getBattleBuilder(Mission mission) throws PWCGException
    {
        AmphibiousAssault amphibiousAssault = PWCGContext.getInstance().getCurrentMap().getAmphibiousAssaultManager().getActiveAmphibiousAssault(mission);
        if (amphibiousAssault != null)
        {
            return new AmphibiousAssaultBuilder(mission, amphibiousAssault);
        }
        
        if (hasCargoRoutesForAllPlayerSides(mission))
        {
            return new SeaBattleBuilder(mission);
        }

        if (PWCGContext.getInstance().getCurrentMap().isNoBattlePeriod(mission.getCampaign().getDate()))
        {
            return new NoBattleBuilder();
        }
        
        return new MissionBattleBuilder(mission);
    }

    private static boolean hasCargoRoutesForAllPlayerSides(Mission mission) throws PWCGException
    {
        Squadron squadron =  PWCGContext.getInstance().getSquadronManager().getSquadron(mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0).getSquadronId());
        Coordinate playerSquadronPosition = squadron.determineCurrentAirfieldAnyMap(mission.getCampaign().getDate()).getPosition();
        
        for (Side side : mission.getParticipatingPlayers().getMissionPlayerSides())
        {
            boolean hasCargoRouteForSide = PWCGContext.getInstance().getCurrentMap().getShippingLaneManager().hasNearbyCargoShipRouteBySide(playerSquadronPosition, side);
            if (!hasCargoRouteForSide)
            {
                return false;
            }
        }

        return true;
    }
}
