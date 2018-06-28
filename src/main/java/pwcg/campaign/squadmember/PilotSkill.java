package pwcg.campaign.squadmember;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class PilotSkill 
{
    public static int PilotSkillMedVictories = 1;           // Number of victories to advance pilot skill from low to medium
    public static int PilotSkillMedMinMissions = 5;         // Number of missions to advance pilot skill from low to medium
    
    public static int PilotSkillHighVictories = 2;          // Number of victories to advance pilot skill from medium to high
    public static int PilotSkillHighMinVictories = 1;       // Minimum victories to advance pilot skill from medium to high
    public static int PilotSkillHighMinMissions = 10;       // Minimum victories to advance pilot skill from medium to high

    public static int PilotSkillAceVictories = 5;           // Number of victories to advance pilot skill from high to ace

    public static int PilotSkillMedGroundVictories = 1;        // Number of victories to advance pilot skill from low to medium
    public static int PilotSkillHighGroundVictories = 20;      // Number of victories to advance pilot skill from medium to high
    public static int PilotSkillHighMinGroundVictories = 8;    // Minimum victories to advance pilot skill from medium to high

    private Campaign campaign;
    
	public PilotSkill(Campaign campaign) 
	{
		this.campaign = campaign;
	}
	public void advanceSkillofPilot(SquadronMember squadronMember, Squadron squad) throws PWCGException 
	{
		if (squadronMember.getPilotActiveStatus() == SquadronMemberStatus.STATUS_KIA)
		{
			return;
		}
		
		if (squadronMember.isPlayer())
		{
			return;
		}
		
		if (squadronMember instanceof Ace)
		{
			return;
		}
		
        int numPilotVictories = squadronMember.getSquadronMemberVictories().getAirToAirVictories();
        int numPilotGroundVictories = squadronMember.getGroundVictories().size();
		int numMissions = squadronMember.getMissionFlown();
		
		if (numPilotVictories >= PilotSkillMedVictories || numPilotGroundVictories >= PilotSkillMedGroundVictories || numMissions > PilotSkillMedMinMissions)
		{
			if (squadronMember.getAiSkillLevel() != AiSkillLevel.NOVICE)
			{
				squadronMember.setAiSkillLevel(AiSkillLevel.COMMON);
			}
		}
		
        // Veteran pilot skill
        if ((numPilotVictories >= PilotSkillHighVictories) || 
            (numPilotGroundVictories >= PilotSkillHighGroundVictories) || 
            (numPilotVictories > PilotSkillHighMinVictories && numMissions > PilotSkillHighMinMissions) ||
            (numPilotGroundVictories > PilotSkillHighMinGroundVictories && numMissions > PilotSkillHighMinMissions))
        {
            if (squadronMember.getAiSkillLevel().lessThan(AiSkillLevel.VETERAN))
            {
                squadronMember.setAiSkillLevel(AiSkillLevel.VETERAN);
            }
        }
 
        if (numPilotVictories >= PilotSkillAceVictories)
        {
            if (squadronMember.getAiSkillLevel().lessThan(AiSkillLevel.ACE))
            {
                squadronMember.setAiSkillLevel(AiSkillLevel.ACE);
            }
        }
		
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
        // Adjustments for average squadrons - no adjustments
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
