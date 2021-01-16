package pwcg.campaign.squadmember;

import java.util.List;

public class SquadronMemberVictories
{
    private int airToAirVictories = 0;
    private int tankVictories = 0;
    private int groundVictories = 0;
    
    public SquadronMemberVictories (List<Victory> victories)
    {
        for (Victory victory : victories)
        {
            if (victory.getVictim().getAirOrGround() == Victory.AIR_VICTORY || victory.getVictim().getAirOrGround() == Victory.UNSPECIFIED_VICTORY)
            {
                ++airToAirVictories;
            }
            
            if (victory.getVictim().getAirOrGround() == Victory.GROUND_VICTORY)
            {
                ++groundVictories;
            }
        }
    }

    public int getAirToAirVictories()
    {
        return airToAirVictories;
    }

    public int getTankVictories()
    {
        return tankVictories;
    }

    public int getGroundVictories()
    {
        return groundVictories;
    }
}
