package pwcg.campaign.squadron;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.SquadronMemberVictories;
import pwcg.core.exception.PWCGException;

public class SquadronSummaryStatistics 
{
    private Campaign campaign = null;
    private int logsForSquadronId = 0;
    
    private int numKilled = 0;
    private int numMaimed = 0;
    private int numCaptured = 0;
    private int numAirToAirVictories = 0;
    private int numTankKills = 0;
    private int numTrainKills = 0;
    private int numGroundUnitKills = 0;

    public SquadronSummaryStatistics (Campaign campaign, int logsForSquadronId)
    {
        this.campaign = campaign;
        this.logsForSquadronId = logsForSquadronId;
    }
    
    public void calculateStatistics() throws PWCGException
    {
        for (SquadronMember squadronMember : campaign.getPersonnelManager().getSquadronPersonnel(logsForSquadronId).getSquadronMembers().getSquadronMemberList())
        {
            if (squadronMember.getPilotActiveStatus() == SquadronMemberStatus.STATUS_KIA)
            {
                ++numKilled;
            }
            else if (squadronMember.getPilotActiveStatus() == SquadronMemberStatus.STATUS_CAPTURED)
            {
                ++numCaptured;
            }
            else if (squadronMember.getPilotActiveStatus() == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
            {
                ++numMaimed;
            }
            
            SquadronMemberVictories squadronMemberVictories = squadronMember.getSquadronMemberVictories();
            numAirToAirVictories += squadronMemberVictories.getAirToAirVictoryCount();
            numTankKills += squadronMemberVictories.getTankVictoryCount();
            numTrainKills += squadronMemberVictories.getTrainVictoryCount();
            numGroundUnitKills += squadronMemberVictories.getGroundVictoryCount();
        }
    }

    public int getNumKilled()
    {
        return numKilled;
    }

    public int getNumMaimed()
    {
        return numMaimed;
    }

    public int getNumCaptured()
    {
        return numCaptured;
    }

    public int getSquadronMembersLostTotal()
    {
        return numKilled + numCaptured + numMaimed;
    }

    public int getNumAirToAirVictories()
    {
        return numAirToAirVictories;
    }

    public int getNumTankKills()
    {
        return numTankKills;
    }

    public int getNumTrainKills()
    {
        return numTrainKills;
    }

    public int getNumGroundUnitKills()
    {
        return numGroundUnitKills;
    }

    public int getGroundKillsTotal()
    {
        return numTankKills + numTrainKills + numGroundUnitKills;
    }
}
