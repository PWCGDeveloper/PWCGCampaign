package pwcg.aar.campaign.update;

import java.util.Map;

import pwcg.aar.data.AARPersonnelAwards;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.squadmember.HistoricalAce;
import pwcg.campaign.squadmember.PilotSkill;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;

public class CampaignPilotUpdater 
{
	private Campaign campaign = null;
	
	private AARPersonnelAwards personnelAwards;
	
	public CampaignPilotUpdater (Campaign campaign, AARPersonnelAwards personnelAwards) 
	{
        this.campaign = campaign;
        this.personnelAwards = personnelAwards;
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
        for (Integer serialNumber : personnelAwards.getMissionsFlown().keySet())
        {
            SquadronMember pilot = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            Integer missionFlown = personnelAwards.getMissionsFlown().get(serialNumber);
            pilot.setMissionFlown(missionFlown);
        }
    }

    private void assignVictories() throws PWCGException
    {
        for (Integer serialNumber : personnelAwards.getVictoriesByPilot().keySet())
        {
            for (Victory victory : personnelAwards.getVictoriesByPilot().get(serialNumber)) 
            {
                if (victory.getVictim().getAirOrGround() == Victory.AIR_VICTORY)
                {
                    addAirVictory(serialNumber, victory);
                }
                else if (victory.getVictim().getAirOrGround() == Victory.GROUND_VICTORY)
                {
                    addGroundVictory(serialNumber, victory);
                }
                else
                {
                    Logger.log(LogLevel.ERROR, "Invalid victory type " + victory.getVictim().getType());
                }
            }
        }
    }

    private void assignMedals() throws PWCGException
    {
        for (Integer serialNumber : personnelAwards.getCampaignMemberMedals().keySet())
        {
            Map<String, Medal> medals = personnelAwards.getCampaignMemberMedals().get(serialNumber);
            for (Medal medal : medals.values())
            {
                SquadronMember pilot = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
                pilot.addMedal(medal);
            }
        }
    }


    private void assignPromotions() throws PWCGException
    {
        for (Integer serialNumber : personnelAwards.getPromotions().keySet())
        {
            String rank = personnelAwards.getPromotions().get(serialNumber);
            SquadronMember pilot = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            pilot.setRank(rank);
        }
    }

    
    private void adjustPilotSkill() throws PWCGException
    {
        Map<Integer, SquadronMember> squadronMembers = SquadronMemberFilter.filterActiveAI(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());
        for (SquadronMember pilot : squadronMembers.values())
        {
            PilotSkill pilotSkill = new PilotSkill(campaign);
            pilotSkill.advanceSkillofPilot(pilot, pilot.determineSquadron());
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
        HistoricalAce historicalAce = PWCGContextManager.getInstance().getAceManager().getHistoricalAceBySerialNumber(serialNumber);
        if (historicalAce != null)
        {
            historicalAce.addVictory(victory);
        }
    }
 }
