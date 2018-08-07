package pwcg.aar.integration;

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
        assert(campaign.getDate().after(DateUtils.getDateYYYYMMDD("19411101")));
        assert(campaign.getPlayers().get(0).getVictories().size() == expectedResults.getPlayerAirVictories());
        assert(campaign.getPlayers().get(0).getGroundVictories().size() == expectedResults.getPlayerGroundVictories());
        
        SquadronMember otherPilot = campaign.getPersonnelManager().getAnyCampaignMember(expectedResults.getSquadronMemberPilotSerialNumber());        
        assert(otherPilot.getVictories().size() == expectedResults.getSquadronMemberAirVictories());
        assert(otherPilot.getGroundVictories().size() == expectedResults.getSquadronMemberGroundVictories());
        for (Integer serialNumber : expectedResults.getLostPilots())
        {
            SquadronMember lostPilot = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            assert(lostPilot.getPilotActiveStatus() <= SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        }
        assert(campaign.getPlayers().get(0).getMissionFlown()  == (playerMissionsFlown+1));
        assert(campaign.getPlayers().get(0).getVictories().size() == expectedPlayerVictories);
    }

    public void validateLeave() throws PWCGException
    {
        assert(campaign.getDate().after(DateUtils.getDateYYYYMMDD("19411101")));
        assert(campaign.getPlayers().get(0).getVictories().size() == 0);
        assert(campaign.getPlayers().get(0).getGroundVictories().size() == 0);
        
        for (Integer serialNumber : expectedResults.getLostPilots())
        {
            SquadronMember lostPilot = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            assert(lostPilot.getPilotActiveStatus() <= SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        }
    }
}
