package pwcg.aar.outofmission.phase2.awards;

import java.util.List;
import java.util.Map;

import pwcg.aar.AARFactory;
import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.exception.PWCGException;

public class CampaignAwardsGenerator
{
    private Campaign campaign;
    private AARContext aarContext;
    private CampaignMemberAwardsGenerator awardsGenerator;

    public CampaignAwardsGenerator(
            Campaign campaign, 
            AARContext aarContext)
    {
        this.campaign = campaign;
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
                int victoriesToday = getVictoryCountToday(squadronMember);
                AARPersonnelAwards personnelAwardsForSquadronMember = awardsGenerator.generateAwards(squadronMember, victoriesToday);
                personnelAwards.merge(personnelAwardsForSquadronMember);
            }
        }
        
        return personnelAwards;
    }

    private int getVictoryCountToday(SquadronMember squadronMember)
    {
        int victoriesToday = 0;
        Map<Integer, List<Victory>> victoriesByAllPilots = aarContext.getPersonnelAcheivements().getVictoriesByPilot();
        List<Victory> victoriesByPilot = victoriesByAllPilots.get(squadronMember.getSerialNumber());
        if (victoriesByPilot != null)
        {
            for (Victory victory : victoriesByPilot)
            {
                if (victory.getDate().equals(campaign.getDate()))
                {
                    ++victoriesToday;
                }
            }
        }
        return victoriesToday;
    }
 

}
