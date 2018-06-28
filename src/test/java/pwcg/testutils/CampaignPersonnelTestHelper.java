package pwcg.testutils;

import java.util.Date;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadmember.VictoryEntity;
import pwcg.core.exception.PWCGException;

public class CampaignPersonnelTestHelper
{

    public static void addVictories (SquadronMember squadronMember, Date date, int numVictories)
    {
        for (int i = 0; i < numVictories; ++i)
        {
            VictoryEntity victim = new VictoryEntity();
            VictoryEntity victor = new VictoryEntity();
            
            Victory victory = new Victory();
            victory.setCrashedInSight(true);
            victory.setDate(date);
            victory.setLocation("Somewhere");
            victory.setVictim(victim);
            victory.setVictor(victor);
            
            squadronMember.getVictories().add(victory);
        }
    }
    
    public static SquadronMember getSquadronMemberByRank(Campaign campaign, String rank) throws PWCGException
    {
        SquadronMember selectedAiSquadMember = null;
        
        Map<Integer, SquadronMember> squadronMembers = campaign.getPersonnelManager().getAllNonAceCampaignMembers();
        for (SquadronMember aiSquadMember : squadronMembers.values())
        {
            if (aiSquadMember.getRank().equals(rank))
            {
                selectedAiSquadMember = aiSquadMember;
            }
        }

        return selectedAiSquadMember;
    }

}
