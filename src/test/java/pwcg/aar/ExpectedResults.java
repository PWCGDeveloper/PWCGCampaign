package pwcg.aar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;

public class ExpectedResults
{
    private Campaign campaign;
    private int squadronMemberCrewMemberSerialNumber;
    private List<Integer> lostCrewMembers = new ArrayList<>();
    private int playerAirVictories = 0;
    private int playerGroundVictories = 0;
    private int squadronMemberAirVictories = 0;
    private int squadronMemberGroundVictories = 0;
    private int enemyAirVictories = 0;

    public ExpectedResults (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public void buildExpectedResultsFromAARContext(AARContext aarContext)
    {
        addLostCrewMembers(aarContext.getPersonnelLosses().getPersonnelKilled());
        addLostCrewMembers(aarContext.getPersonnelLosses().getPersonnelCaptured());
        addLostCrewMembers(aarContext.getPersonnelLosses().getPersonnelMaimed());
    }

    private void addLostCrewMembers(Map<Integer, CrewMember> killedCrewMembers)
    {
        for(CrewMember lostCrewMember : killedCrewMembers.values())
        {
            lostCrewMembers.add(lostCrewMember.getSerialNumber());
        }
    }

    public Campaign getCampaign()
    {
        return campaign;
    }

    public void setCampaign(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public int getCrewMemberCrewMemberSerialNumber()
    {
        return squadronMemberCrewMemberSerialNumber;
    }

    public void setCrewMemberCrewMemberSerialNumber(int squadronMemberCrewMemberSerialNumber)
    {
        this.squadronMemberCrewMemberSerialNumber = squadronMemberCrewMemberSerialNumber;
    }

    public List<Integer> getLostCrewMembers()
    {
        return lostCrewMembers;
    }

    public int getPlayerAirVictories()
    {
        return playerAirVictories;
    }

    public void addPlayerAirVictories()
    {
        ++playerAirVictories;
    }

    public void addEnemyAirVictories()
    {
        ++enemyAirVictories;
    }

    public int getEnemyAirVictories()
    {
        return enemyAirVictories;
    }

    public int getPlayerGroundVictories()
    {
        return playerGroundVictories;
    }

    public void addPlayerGroundVictories()
    {
        ++playerGroundVictories;
    }

    public int getCrewMemberAirVictories()
    {
        return squadronMemberAirVictories;
    }

    public void addCrewMemberAirVictories()
    {
        ++squadronMemberAirVictories;
    }

    public int getCrewMemberGroundVictories()
    {
        return squadronMemberGroundVictories;
    }

    public void addCrewMemberGroundVictories()
    {
        ++squadronMemberGroundVictories;
    }


}
