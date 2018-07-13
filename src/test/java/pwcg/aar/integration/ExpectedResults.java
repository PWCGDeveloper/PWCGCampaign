package pwcg.aar.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;

public class ExpectedResults
{
    private Campaign campaign;
    private int squadronMemberPilotSerialNumber;
    private List<Integer> lostPilots = new ArrayList<>();
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
        addLostPilots(aarContext.getCumulativeMissionData().getPersonnelLossesOutOfMission().getPersonnelKilled());
        addLostPilots(aarContext.getCumulativeMissionData().getPersonnelLossesOutOfMission().getPersonnelCaptured());
        addLostPilots(aarContext.getCumulativeMissionData().getPersonnelLossesOutOfMission().getPersonnelMaimed());
    }

    private void addLostPilots(Map<Integer, SquadronMember> killedPilots)
    {
        for(SquadronMember lostPilot : killedPilots.values())
        {
            lostPilots.add(lostPilot.getSerialNumber());
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

    public int getSquadronMemberPilotSerialNumber()
    {
        return squadronMemberPilotSerialNumber;
    }

    public void setSquadronMemberPilotSerialNumber(int squadronMemberPilotSerialNumber)
    {
        this.squadronMemberPilotSerialNumber = squadronMemberPilotSerialNumber;
    }

    public List<Integer> getLostPilots()
    {
        return lostPilots;
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

    public int getSquadronMemberAirVictories()
    {
        return squadronMemberAirVictories;
    }

    public void addSquadronMemberAirVictories()
    {
        ++squadronMemberAirVictories;
    }

    public int getSquadronMemberGroundVictories()
    {
        return squadronMemberGroundVictories;
    }

    public void addSquadronMemberGroundVictories()
    {
        ++squadronMemberGroundVictories;
    }


}
