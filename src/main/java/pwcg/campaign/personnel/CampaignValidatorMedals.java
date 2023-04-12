package pwcg.campaign.personnel;

import pwcg.campaign.Campaign;
import pwcg.campaign.factory.MedalManagerFactory;
import pwcg.campaign.medals.IMedalManager;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class CampaignValidatorMedals
{
    private Campaign campaign;

    public CampaignValidatorMedals(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public void assignMissingMedalsForSquadMember(SquadronMember squadronMember) throws PWCGException 
    {
        if (!(squadronMember instanceof Ace))
        {
            IMedalManager mm = MedalManagerFactory.createMedalManager(squadronMember.determineCountry(), campaign);
            while (true)
            {
                Medal medal = mm.award(campaign, squadronMember, squadronMember.determineService(campaign.getDate()),  0);
                if (medal != null)
                {
                    medal.setMedalDate(campaign.getDate());
                    squadronMember.addMedal(medal);
                }
                else
                {
                    break;
                }
            }
        }
    }
}
