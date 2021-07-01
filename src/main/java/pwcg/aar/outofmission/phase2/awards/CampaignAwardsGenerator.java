package pwcg.aar.outofmission.phase2.awards;

import java.util.List;

import pwcg.aar.AARFactory;
import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class CampaignAwardsGenerator
{
    private AARContext aarContext;
    private CampaignMemberAwardsGenerator awardsGenerator;

    public CampaignAwardsGenerator(Campaign campaign, AARContext aarContext)
    {
        this.aarContext = aarContext;
        this.awardsGenerator =  AARFactory.makeCampaignMemberAwardsGenerator(campaign, aarContext);
    }

    public AARPersonnelAwards createCampaignMemberAwards(List<SquadronMember> squadronMembersToEvaluate) throws PWCGException
    {
        AARPersonnelAwards personnelAwards = AARFactory.makeAARPersonnelAwards();

        for (SquadronMember squadronMember : squadronMembersToEvaluate)
        {
            if (!squadronMember.isHistoricalAce())
            {                
                int airVictoriesToday = getAirVictoryCountToday(squadronMember);
                AARPersonnelAwards personnelAwardsForSquadronMember = awardsGenerator.generateAwards(squadronMember, airVictoriesToday);
                personnelAwards.merge(personnelAwardsForSquadronMember);
            }
        }
        
        return personnelAwards;
    }

    private int getAirVictoryCountToday(SquadronMember squadronMember)
    {
        int airVictoriesToday = aarContext.getPersonnelAcheivements().getAirVictoryCountForPilot(squadronMember.getSerialNumber());
        return airVictoriesToday;
    }
}
