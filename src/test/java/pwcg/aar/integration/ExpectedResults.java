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
    private int otherPilotSerialNumber;
    private List<Integer> lostPilots = new ArrayList<>();
    private int playerAirVictories = 0;
    private int playerGroundVictories = 0;
    private int otherAirVictories = 0;
    private int otherGroundVictories = 0;
    
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

    public int getOtherPilotSerialNumber()
    {
        return otherPilotSerialNumber;
    }

    public void setOtherPilotSerialNumber(int otherPilotSerialNumber)
    {
        this.otherPilotSerialNumber = otherPilotSerialNumber;
    }

    public List<Integer> getLostPilots()
    {
        return lostPilots;
    }

    public void setLostPilots(List<Integer> lostPilots)
    {
        this.lostPilots = lostPilots;
    }

    public int getPlayerAirVictories()
    {
        return playerAirVictories;
    }

    public void addPlayerAirVictories()
    {
        ++playerAirVictories;
    }

    public int getPlayerGroundVictories()
    {
        return playerGroundVictories;
    }

    public void addPlayerGroundVictories()
    {
        ++playerGroundVictories;
    }

    public int getOtherAirVictories()
    {
        return otherAirVictories;
    }

    public void addOtherAirVictories()
    {
        ++otherAirVictories;
    }

    public int getOtherGroundVictories()
    {
        return otherGroundVictories;
    }

    public void addOtherGroundVictories()
    {
        ++otherGroundVictories;
    }


}
