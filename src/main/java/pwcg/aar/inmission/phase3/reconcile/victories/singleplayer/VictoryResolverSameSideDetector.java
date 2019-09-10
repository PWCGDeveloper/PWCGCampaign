package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.squadmember.SquadronMember;

public class VictoryResolverSameSideDetector
{
    
    public static boolean isSameSide(SquadronMember player, LogVictory resultVictory)
    {
        ICountry playerCountry = CountryFactory.makeCountryByCountry(player.getCountry());
        ICountry victimCountry = resultVictory.getVictim().getCountry();
        if (playerCountry.getCountry() == null || victimCountry == null)
        {
            return true;
        }
        
        if (victimCountry.getSide() ==  playerCountry.getSide())           
        {
            return true;
        }

        return false;
    }

}
