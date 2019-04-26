package pwcg.aar;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class AARResultValidator
{
    Campaign campaign;
    ExpectedResults expectedResults;

    public AARResultValidator (ExpectedResults expectedResults)
    {
        this.campaign = expectedResults.getCampaign();
        this.expectedResults = expectedResults;
    }

    public void validateInMission(int playerMissionsFlown, int expectedPlayerVictories) throws PWCGException
    {
        SquadronMember player = campaign.getPersonnelManager().getAllPlayers().getSquadronMemberList().get(0);
        assert(campaign.getDate().after(DateUtils.getDateYYYYMMDD("19411101")));
        assert(player.getVictories().size() == expectedResults.getPlayerAirVictories());
        assert(player.getGroundVictories().size() == expectedResults.getPlayerGroundVictories());
        
        SquadronMember otherPilot = campaign.getPersonnelManager().getAnyCampaignMember(expectedResults.getSquadronMemberPilotSerialNumber());        
        assert(otherPilot.getVictories().size() == expectedResults.getSquadronMemberAirVictories());
        assert(otherPilot.getGroundVictories().size() == expectedResults.getSquadronMemberGroundVictories());
        for (Integer serialNumber : expectedResults.getLostPilots())
        {
            SquadronMember lostPilot = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            assert(lostPilot.getPilotActiveStatus() <= SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        }
        assert(player.getMissionFlown()  == (playerMissionsFlown+1));
        assert(player.getVictories().size() == expectedPlayerVictories);
    }

    public void validateLeave() throws PWCGException
    {
        SquadronMember player = campaign.getPersonnelManager().getAllPlayers().getSquadronMemberList().get(0);
        assert(campaign.getDate().after(DateUtils.getDateYYYYMMDD("19411101")));
        assert(player.getVictories().size() == 0);
        assert(player.getGroundVictories().size() == 0);
        
        for (Integer serialNumber : expectedResults.getLostPilots())
        {
            SquadronMember lostPilot = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            assert(lostPilot.getPilotActiveStatus() <= SquadronMemberStatus.STATUS_WOUNDED);
        }
    }
}
