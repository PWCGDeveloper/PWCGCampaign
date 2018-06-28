package pwcg.campaign.personnel;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;

public class InitialReplacementStaffer
{
    public static final int NUM_INITIAL_REPLACEMENTS = 20;
    
    private SquadronMemberReplacementFactory squadronMemberFactory;
    private SquadronMembers squadronMembers;
    
    public InitialReplacementStaffer(Campaign campaign, ArmedService service) 
    {
        squadronMembers = new SquadronMembers();
        squadronMemberFactory = new SquadronMemberReplacementFactory(campaign, service);
    }

    public SquadronMembers staffReplacementsForService() throws PWCGException
    {
        for (int i = 0; i < NUM_INITIAL_REPLACEMENTS; ++i)
        {
            SquadronMember replacement = squadronMemberFactory.createAIReplacementPilot();
            squadronMembers.addSquadronMember(replacement);
        }
        
        return squadronMembers;
    }
}
