package pwcg.aar;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.SquadronTestProfile;

public class FCAARResultValidator
{
    Campaign campaign;
    AARTestExpectedResults expectedResults;

    public FCAARResultValidator (AARTestExpectedResults expectedResults)
    {
        this.campaign = expectedResults.getCampaign();
        this.expectedResults = expectedResults;
    }

    public void validateInMission(int playerMissionsFlown, int expectedPlayerVictories) throws PWCGException
    {
        SquadronMember player = campaign.findReferencePlayer();
        assert(campaign.getDate().after(DateUtils.getDateYYYYMMDD(SquadronTestProfile.JASTA_04_PROFILE.getDateString())));
        assert(player.getSquadronMemberVictories().getAirToAirVictoryCount() == expectedResults.getPlayerAirVictories());
        assert(player.getSquadronMemberVictories().getGroundVictoryCount() == expectedResults.getPlayerGroundVictories());
        
        SquadronMember otherPilot = campaign.getPersonnelManager().getAnyCampaignMember(expectedResults.getSquadronMemberPilotSerialNumber());        
        assert(otherPilot.getSquadronMemberVictories().getAirToAirVictoryCount() == expectedResults.getSquadronMemberAirVictories());
        assert(otherPilot.getSquadronMemberVictories().getGroundVictoryCount() == expectedResults.getSquadronMemberGroundVictories());
        for (Integer serialNumber : expectedResults.getLostPilots())
        {
            SquadronMember lostPilot = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            assert(lostPilot.getPilotActiveStatus() <= SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        }
        assert(player.getMissionFlown()  == (playerMissionsFlown+1));
        assert(player.getSquadronMemberVictories().getAirToAirVictoryCount() == expectedPlayerVictories);
    }

    public void validateLeave() throws PWCGException
    {
        SquadronMember player = campaign.findReferencePlayer();
        assert(campaign.getDate().after(DateUtils.getDateYYYYMMDD(SquadronTestProfile.JASTA_04_PROFILE.getDateString())));
        assert(player.getSquadronMemberVictories().getAirToAirVictoryCount() == 0);
        assert(player.getSquadronMemberVictories().getGroundVictoryCount() == 0);
        
        for (Integer serialNumber : expectedResults.getLostPilots())
        {
            SquadronMember lostPilot = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            assert(lostPilot.getPilotActiveStatus() <= SquadronMemberStatus.STATUS_WOUNDED);
        }
    }
}
