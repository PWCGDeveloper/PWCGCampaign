package pwcg.campaign.personnel;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.core.exception.PWCGException;

public class InitialReplacementStaffer
{
    public static final int NUM_INITIAL_REPLACEMENTS = 20;
    
    private CrewMemberReplacementFactory squadronMemberFactory;
    private CrewMembers squadronMembers;
    
    public InitialReplacementStaffer(Campaign campaign, ArmedService service) 
    {
        squadronMembers = new CrewMembers();
        squadronMemberFactory = new CrewMemberReplacementFactory(campaign, service);
    }

    public CrewMembers staffReplacementsForService() throws PWCGException
    {
        for (int i = 0; i < NUM_INITIAL_REPLACEMENTS; ++i)
        {
            CrewMember replacement = squadronMemberFactory.createAIReplacementCrewMember();
            squadronMembers.addToCrewMemberCollection(replacement);
        }
        
        return squadronMembers;
    }
}
