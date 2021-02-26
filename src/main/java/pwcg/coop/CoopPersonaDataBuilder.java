package pwcg.coop;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.coop.model.CoopDisplayRecord;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class CoopPersonaDataBuilder
{
    public List<CoopDisplayRecord> getPlayerSquadronMembersForUser(Campaign campaign) throws PWCGException
    {
        List<CoopDisplayRecord> coopDisplayRecords = new ArrayList<>();

        for (SquadronMember squadronMember : campaign.getPersonnelManager().getAllPlayers().getSquadronMemberList())
        {
            try
            {
                CoopUser coopUser = CoopUserManager.getIntance().getCoopUserForSquadronMember(campaign.getName(), squadronMember.getSerialNumber());
                CoopDisplayRecord coopDisplayRecord = formCoopDisplayRecordsForUser(campaign, coopUser, squadronMember);
                coopDisplayRecords.add(coopDisplayRecord);
            }
            catch (Exception e)
            {
                PWCGLogger.logException(e);
            }
        }
        return coopDisplayRecords;
    }
    
    
    private CoopDisplayRecord formCoopDisplayRecordsForUser(Campaign campaign, CoopUser coopUser, SquadronMember squadronMember) throws PWCGException
    {
        String coopUsername = "Not Assigned";
        if (coopUser != null)
        {
            coopUsername = coopUser.getUsername();
        }
        
        CoopDisplayRecord coopDisplayRecord = new CoopDisplayRecord();
        coopDisplayRecord.setUsername(coopUsername);
        coopDisplayRecord.setPilorNameAndRank(squadronMember.getNameAndRank());
        coopDisplayRecord.setCampaignName(campaign.getCampaignData().getName());
        coopDisplayRecord.setSquadronName(squadronMember.determineSquadron().determineDisplayName(campaign.getDate()));
        coopDisplayRecord.setPilotStatus(squadronMember.getPilotActiveStatus());
        coopDisplayRecord.setPilotSerialNumber(squadronMember.getSerialNumber());

        return coopDisplayRecord;
    }

}
