package pwcg.aar.outofmission.phase2.awards;

import java.util.List;

import pwcg.aar.AARFactory;
import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
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

    public AARPersonnelAwards createCampaignMemberAwards(List<CrewMember> squadronMembersToEvaluate) throws PWCGException
    {
        AARPersonnelAwards personnelAwards = AARFactory.makeAARPersonnelAwards();

        for (CrewMember crewMember : squadronMembersToEvaluate)
        {
            if (!crewMember.isHistoricalAce())
            {                
                int airVictoriesToday = getAirVictoryCountToday(crewMember);
                AARPersonnelAwards personnelAwardsForCrewMember = awardsGenerator.generateAwards(crewMember, airVictoriesToday);
                personnelAwards.merge(personnelAwardsForCrewMember);
            }
        }
        
        return personnelAwards;
    }

    private int getAirVictoryCountToday(CrewMember crewMember)
    {
        int airVictoriesToday = aarContext.getPersonnelAcheivements().getAirVictoryCountForCrewMember(crewMember.getSerialNumber());
        return airVictoriesToday;
    }
}
