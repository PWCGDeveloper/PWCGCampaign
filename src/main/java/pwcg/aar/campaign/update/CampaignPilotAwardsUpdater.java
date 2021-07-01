package pwcg.aar.campaign.update;

import java.util.Map;

import pwcg.aar.data.CampaignUpdateData;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.squadmember.HistoricalAce;
import pwcg.campaign.squadmember.PilotSkill;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class CampaignPilotAwardsUpdater 
{
	private Campaign campaign = null;
	
	private CampaignUpdateData campaignUpdateData;
	
	public CampaignPilotAwardsUpdater (Campaign campaign, CampaignUpdateData campaignUpdateData) 
	{
        this.campaign = campaign;
        this.campaignUpdateData = campaignUpdateData;
	}

    
	public void updatesForMissionEvents() throws PWCGException 
	{
        assignMissionsFlown();

		assignVictories();

		assignMedals();

		assignPromotions();
		
        adjustPilotSkill();
	}

    private void assignMissionsFlown() throws PWCGException
    {
        for (Integer serialNumber : campaignUpdateData.getPersonnelAcheivements().getMissionsFlown().keySet())
        {
            SquadronMember pilot = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            Integer missionFlown = campaignUpdateData.getPersonnelAcheivements().getMissionsFlown().get(serialNumber);
            pilot.setMissionFlown(missionFlown);
        }
    }

    private void assignVictories() throws PWCGException
    {
        for (Integer serialNumber : campaignUpdateData.getPersonnelAcheivements().getVictoriesByPilot().keySet())
        {
            for (Victory victory : campaignUpdateData.getPersonnelAcheivements().getVictoriesByPilot().get(serialNumber)) 
            {
                if (victory.getVictim().getAirOrGround() == Victory.AIRCRAFT)
                {
                    addAirVictory(serialNumber, victory);
                }
                else if (victory.getVictim().getAirOrGround() == Victory.VEHICLE)
                {
                    addGroundVictory(serialNumber, victory);
                }
                else
                {
                    PWCGLogger.log(LogLevel.ERROR, "Invalid victory type " + victory.getVictim().getType());
                }
            }
        }
    }

    private void assignMedals() throws PWCGException
    {
        for (Integer serialNumber : campaignUpdateData.getPersonnelAwards().getCampaignMemberMedals().keySet())
        {
            Map<String, Medal> medals = campaignUpdateData.getPersonnelAwards().getCampaignMemberMedals().get(serialNumber);
            for (Medal medal : medals.values())
            {
                SquadronMember pilot = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
                pilot.addMedal(medal);
            }
        }
    }


    private void assignPromotions() throws PWCGException
    {
        for (Integer serialNumber : campaignUpdateData.getPersonnelAwards().getPromotions().keySet())
        {
            String rank = campaignUpdateData.getPersonnelAwards().getPromotions().get(serialNumber);
            SquadronMember pilot = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            pilot.setRank(rank);
        }
    }

    
    private void adjustPilotSkill() throws PWCGException
    {
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAINoWounded(campaign.getPersonnelManager().getActiveCampaignMembers(), campaign.getDate());
        for (SquadronMember pilot : squadronMembers.getSquadronMemberCollection().values())
        {
            if (!pilot.isHistoricalAce())
            {
                PilotSkill pilotSkill = new PilotSkill(campaign);
                pilotSkill.advancePilotSkillForPerformance(pilot);
            }
        }
    }

    private void addAirVictory(Integer serialNumber, Victory victory) throws PWCGException
    {
        if (serialNumber != 0)
        {
            SquadronMember squadronMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            if (squadronMember != null)
            {
                squadronMember.addVictory(victory);
                addToHistoricalAceRecordForPersistence(serialNumber, victory);
            }
        }
    }

    private void addGroundVictory(Integer serialNumber, Victory victory) throws PWCGException
    {
        if (serialNumber != null)
        {
            SquadronMember squadronMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            if (squadronMember != null)
            {
                squadronMember.addGroundVictory(victory);
                addToHistoricalAceRecordForPersistence(serialNumber, victory);
            }
        }
    }
    

    private void addToHistoricalAceRecordForPersistence(Integer serialNumber, Victory victory)
    {
        HistoricalAce historicalAce = PWCGContext.getInstance().getAceManager().getHistoricalAceBySerialNumber(serialNumber);
        if (historicalAce != null)
        {
            historicalAce.addVictory(victory);
        }
    }
 }
