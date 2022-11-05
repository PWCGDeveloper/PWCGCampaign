package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.StructureVictoryBuilder;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.StructureVictimGenerator;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.building.PwcgStructure;

public class OutOfMissionStructureVictoryEventGenerator 
{    
	private Campaign campaign = null;
    private SquadronMember squadronMember;

	private OutOfMissionVictoryData victoriesOutOMission = new OutOfMissionVictoryData();

	public OutOfMissionStructureVictoryEventGenerator (Campaign campaign, SquadronMember squadronMember) 
	{
        this.campaign = campaign;
        this.squadronMember = squadronMember;
	}
	
    public OutOfMissionVictoryData outOfMissionVictoriesForSquadronMember() throws PWCGException 
    {
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        int victoryOdds = determineOddsOfOutOfMissionVictory(configManager);
        determineVictory(victoryOdds);
        return victoriesOutOMission;
    }

    private int determineOddsOfOutOfMissionVictory(ConfigManagerCampaign configManager) throws PWCGException
    {
        int victoryOdds = 5;
        if (squadronMember.getAiSkillLevel() == AiSkillLevel.COMMON)
        {
        	victoryOdds += 10;
        }
        else if (squadronMember.getAiSkillLevel() == AiSkillLevel.VETERAN)
        {
        	victoryOdds += 20;
        }
        else if (squadronMember.getAiSkillLevel() == AiSkillLevel.ACE)
        {
        	victoryOdds += 40;
        }
        
        return victoryOdds;
    }

    private void determineVictory(int victoryOdds) throws PWCGException 
    {
        int victoryDiceRoll = RandomNumberGenerator.getRandom(1000);
        if (victoryDiceRoll < victoryOdds)
        {
            generateVictory();
        }
    }

    private void generateVictory() throws PWCGException
    {
        StructureVictimGenerator duringCampaignVictimGenerator = new StructureVictimGenerator();
        PwcgStructure victimStructure = duringCampaignVictimGenerator.generateVictimStructure();

        StructureVictoryBuilder outOfMissionVictoryGenerator = new StructureVictoryBuilder(campaign, squadronMember, victimStructure);
        Victory victory = outOfMissionVictoryGenerator.generateOutOfMissionVictory(campaign.getDate());

        if (victory != null)
        {
            victoriesOutOMission.addVictoryAwards(squadronMember.getSerialNumber(), victory);
        }
    }

	public OutOfMissionVictoryData getVictoriesOutOMission()
	{
		return victoriesOutOMission;
	}  
}
