package pwcg.coop;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.coop.model.CoopDisplayRecord;
import pwcg.coop.model.CoopPersona;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class CoopPersonaDataBuilder
{
    public List<CoopDisplayRecord> getPlayerSquadronMembersForUser(Campaign campaign) throws PWCGException
    {
        List<CoopDisplayRecord> coopDisplayRecords = new ArrayList<>();

        List<CoopUser> coopUsers = CoopUserManager.getIntance().getAllCoopUsers();
        for (CoopUser coopUser : coopUsers)
        {
            try
            {
                List<SquadronMember> squadronMembersForUserInCampaign = getPlayerSquadronMembersFromCampaign(coopUser, campaign);
                List<CoopDisplayRecord> coopDisplayRecordsForCampaign = formCoopDisplayRecordsForUser(coopUser, campaign, squadronMembersForUserInCampaign);
                coopDisplayRecords.addAll(coopDisplayRecordsForCampaign);
            }
            catch (Exception e)
            {
                PWCGLogger.logException(e);
            }
        }
        return coopDisplayRecords;
    }
    
    private List<SquadronMember> getPlayerSquadronMembersFromCampaign(CoopUser coopUser, Campaign campaign) throws PWCGException
    {
        List<SquadronMember> squadronMembersForUserInCampaign = new ArrayList<>();

        List<CoopPersona> coopPersonas = coopUser.getUserPersonas();
        for (CoopPersona coopPersona : coopPersonas)
        {
            if (coopPersona.getCampaignName().equalsIgnoreCase(campaign.getCampaignData().getName()))
            {
                SquadronMember playerSquadronMember = campaign.getPersonnelManager().getAnyCampaignMember(coopPersona.getSerialNumber());
                if (playerSquadronMember != null)
                {
                    squadronMembersForUserInCampaign.add(playerSquadronMember);
                }
            }
        }
        return squadronMembersForUserInCampaign;
    }
    
    
    private List<CoopDisplayRecord> formCoopDisplayRecordsForUser(CoopUser coopUser, Campaign campaign, List<SquadronMember> squadronMembersForCampaign) throws PWCGException
    {
        List<CoopDisplayRecord> coopDisplayRecords = new ArrayList<>();
        for (SquadronMember squadronMember : squadronMembersForCampaign)
        {
            CoopDisplayRecord coopDisplayRecord = new CoopDisplayRecord();
            coopDisplayRecord.setUsername(coopUser.getUsername());
            coopDisplayRecord.setPilorNameAndRank(squadronMember.getNameAndRank());
            coopDisplayRecord.setCampaignName(campaign.getCampaignData().getName());
            coopDisplayRecord.setSquadronName(squadronMember.determineSquadron().determineDisplayName(campaign.getDate()));
            coopDisplayRecord.setPilotStatus(squadronMember.getPilotActiveStatus());
            coopDisplayRecord.setPilotSerialNumber(squadronMember.getSerialNumber());
            
            coopDisplayRecords.add(coopDisplayRecord);
        }
        return coopDisplayRecords;
    }

}
