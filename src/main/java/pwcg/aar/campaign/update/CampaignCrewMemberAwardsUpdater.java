package pwcg.aar.campaign.update;

import java.util.Map;

import pwcg.aar.data.CampaignUpdateData;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberSkill;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.HistoricalAce;
import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class CampaignCrewMemberAwardsUpdater 
{
	private Campaign campaign = null;
	
	private CampaignUpdateData campaignUpdateData;
	
	public CampaignCrewMemberAwardsUpdater (Campaign campaign, CampaignUpdateData campaignUpdateData) 
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
		
        adjustCrewMemberSkill();
	}

    private void assignMissionsFlown() throws PWCGException
    {
        for (Integer serialNumber : campaignUpdateData.getPersonnelAcheivements().getMissionsFlown().keySet())
        {
            CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            Integer missionFlown = campaignUpdateData.getPersonnelAcheivements().getMissionsFlown().get(serialNumber);
            crewMember.setBattlesFought(missionFlown);
        }
    }

    private void assignVictories() throws PWCGException
    {
        for (Integer serialNumber : campaignUpdateData.getPersonnelAcheivements().getVictoriesByCrewMember().keySet())
        {
            for (Victory victory : campaignUpdateData.getPersonnelAcheivements().getVictoriesByCrewMember().get(serialNumber)) 
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
                CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
                crewMember.addMedal(medal);
            }
        }
    }


    private void assignPromotions() throws PWCGException
    {
        for (Integer serialNumber : campaignUpdateData.getPersonnelAwards().getPromotions().keySet())
        {
            String rank = campaignUpdateData.getPersonnelAwards().getPromotions().get(serialNumber);
            CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            crewMember.setRank(rank);
        }
    }

    
    private void adjustCrewMemberSkill() throws PWCGException
    {
        CrewMembers squadronMembers = CrewMemberFilter.filterActiveAINoWounded(campaign.getPersonnelManager().getActiveCampaignMembers(), campaign.getDate());
        for (CrewMember crewMember : squadronMembers.getCrewMemberCollection().values())
        {
            if (!crewMember.isHistoricalAce())
            {
                CrewMemberSkill crewMemberSkill = new CrewMemberSkill(campaign);
                crewMemberSkill.advanceCrewMemberSkillForPerformance(crewMember);
            }
        }
    }

    private void addAirVictory(Integer serialNumber, Victory victory) throws PWCGException
    {
        if (serialNumber != 0)
        {
            CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            if (crewMember != null)
            {
                crewMember.addVictory(victory);
                addToHistoricalAceRecordForPersistence(serialNumber, victory);
            }
        }
    }

    private void addGroundVictory(Integer serialNumber, Victory victory) throws PWCGException
    {
        if (serialNumber != null)
        {
            CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            if (crewMember != null)
            {
                crewMember.addGroundVictory(victory);
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
