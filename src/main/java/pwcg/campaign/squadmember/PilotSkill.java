package pwcg.campaign.squadmember;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class PilotSkill 
{
    public static int PilotSkillCommonVictories = 1;                // Number of victories to advance pilot skill from novice to common
    public static int PilotSkillMedCommonMissions = 10;             // Number of missions to advance pilot skill from novice to common
    
    public static int PilotSkillVeteranAirVictories = 3;            // Number of victories to advance pilot skill from common to veteran
    public static int PilotSkillVeteranMinAirVictories = 2;         // Minimum victories to advance pilot skill from common to veteran
    public static int PilotSkillVeteranMinAirMissions = 40;         // Minimum victories to advance pilot skill from common to veteran

    public static int PilotSkillAceAirVictories = 5;                // Number of victories to advance pilot skill from veteran to ace

    public static int PilotSkillCommonGroundVictoryPoints = 5;          // Number of victories to advance pilot skill from novice to common
    public static int PilotSkillVeteranGroundVictoryPoints = 25;        // Number of victories to advance pilot skill from common to veteran
    public static int PilotSkillAceGroundVictoryPoints = 50;            // Number of victories to advance pilot skill from veteran to ace

    private Campaign campaign;
    
	public PilotSkill(Campaign campaign) 
	{
		this.campaign = campaign;
	}
    
    public void advancePilotSkillForPerformance(SquadronMember squadronMember) throws PWCGException 
    {
        if (isAdjustSkill(squadronMember))
        {
            adjustSquadronMemberSkillForPerformance(squadronMember);
        }
    }
    
    public void advancePilotSkillForInitialCreation(SquadronMember squadronMember, Squadron squad) throws PWCGException 
    {
        if (isAdjustSkill(squadronMember))
        {
            adjustSquadronMemberSkillForPerformance(squadronMember);
            adjustSquadronMemberSkillForSquadronSkill(squadronMember, squad);
        }
    }

    private boolean isAdjustSkill(SquadronMember squadronMember)
    {
        if (squadronMember.getPilotActiveStatus() == SquadronMemberStatus.STATUS_KIA)
        {
            return false;
        }
        
        if (squadronMember.isPlayer())
        {
            return false;
        }
        
        if (squadronMember instanceof Ace)
        {
            return false;
        }
        
        return true;
    }
	
    private void adjustSquadronMemberSkillForPerformance(SquadronMember squadronMember) throws PWCGException
    {
        int numPilotVictories = squadronMember.getSquadronMemberVictories().getAirToAirVictoryCount();
        int numPilotGroundVictoryPoints = squadronMember.getSquadronMemberVictories().getGroundVictoryPointTotal();
		int numMissions = squadronMember.getMissionFlown();
		
		if (numPilotVictories >= PilotSkillCommonVictories || numPilotGroundVictoryPoints >= PilotSkillCommonGroundVictoryPoints || numMissions > PilotSkillMedCommonMissions)
		{
			if (squadronMember.getAiSkillLevel() != AiSkillLevel.NOVICE)
			{
				squadronMember.setAiSkillLevel(AiSkillLevel.COMMON);
			}
		}
		
        // Veteran pilot skill
        if ((numPilotVictories >= PilotSkillVeteranAirVictories) || 
            (numPilotVictories > PilotSkillVeteranMinAirVictories && numMissions > PilotSkillVeteranMinAirMissions) ||
            (numPilotGroundVictoryPoints > PilotSkillVeteranGroundVictoryPoints && numMissions > PilotSkillVeteranMinAirMissions))
        {
            if (squadronMember.getAiSkillLevel().lessThan(AiSkillLevel.VETERAN))
            {
                squadronMember.setAiSkillLevel(AiSkillLevel.VETERAN);
            }
        }
 
        if (numPilotVictories >= PilotSkillAceAirVictories || numPilotGroundVictoryPoints > PilotSkillAceGroundVictoryPoints)
        {
            if (squadronMember.getAiSkillLevel().lessThan(AiSkillLevel.ACE))
            {
                squadronMember.setAiSkillLevel(AiSkillLevel.ACE);
            }
        }
    }
	
    private void adjustSquadronMemberSkillForSquadronSkill(SquadronMember squadronMember, Squadron squad) throws PWCGException
    {
        // Adjust for squadron skill
        int squadronAdjustmentRoll = RandomNumberGenerator.getRandom(100);

        // Adjustments for elite squadrons
        int squadronQuality = squad.determineSquadronSkill(campaign.getDate());
        if (squadronQuality >= 80)
        {
            // No novices in elite squadrons
            if (squadronMember.getAiSkillLevel() == AiSkillLevel.NOVICE)
            {
                squadronMember.setAiSkillLevel(AiSkillLevel.COMMON);
            }           
            // Mostly veterans
            else if (squadronMember.getAiSkillLevel() == AiSkillLevel.COMMON)
            {
                if  (squadronAdjustmentRoll > 50)
                {
                    squadronMember.setAiSkillLevel(AiSkillLevel.VETERAN);
                }
            }
        }
        // Adjustments for quality squadrons
        else if (squadronQuality >= 60)
        {
            // Very good squadrons will not have many novices
            if (squadronMember.getAiSkillLevel() == AiSkillLevel.NOVICE)
            {
                if (squadronAdjustmentRoll > 30)
                {
                    squadronMember.setAiSkillLevel(AiSkillLevel.COMMON);
                }
            }
            // and will tend to have more veterans
            else if (squadronMember.getAiSkillLevel() == AiSkillLevel.COMMON)
            {
                if (squadronAdjustmentRoll > 70)
                {
                    squadronMember.setAiSkillLevel(AiSkillLevel.VETERAN);
                }
            }
        }
        // Adjustments for common squadrons - no adjustments
        else if (squadronQuality >= 45)
        {
        }
        // Adjustments for poor squadrons
        else 
        {
            // Very good squadrons will not have many veterans
            if (squadronMember.getAiSkillLevel() == AiSkillLevel.VETERAN)
            {
                if (squadronAdjustmentRoll < 60)
                {
                    squadronMember.setAiSkillLevel(AiSkillLevel.COMMON);
                }
            }
            // and will tend to have more novices
            else if (squadronMember.getAiSkillLevel() == AiSkillLevel.COMMON)
            {
                if (squadronAdjustmentRoll < 40)
                {
                    squadronMember.setAiSkillLevel(AiSkillLevel.NOVICE);
                }
            }
        }
    }
}
