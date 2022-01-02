package pwcg.campaign.personnel;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.factory.MedalManagerFactory;
import pwcg.campaign.medals.IMedalManager;
import pwcg.campaign.medals.Medal;
import pwcg.core.exception.PWCGException;

public class CampaignValidatorMedals
{
    private Campaign campaign;

    public CampaignValidatorMedals(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public void assignMissingMedalsForSquadMember(CrewMember crewMember) throws PWCGException 
    {
        if (!(crewMember instanceof TankAce))
        {
            IMedalManager mm = MedalManagerFactory.createMedalManager(crewMember.determineCountry(campaign.getDate()), campaign);
            while (true)
            {
                Medal medal = mm.award(campaign, crewMember, crewMember.determineService(campaign.getDate()),  0);
                if (medal != null)
                {
                    medal.setMedalDate(campaign.getDate());
                    crewMember.addMedal(medal);
                }
                else
                {
                    break;
                }
            }
        }
    }
}
