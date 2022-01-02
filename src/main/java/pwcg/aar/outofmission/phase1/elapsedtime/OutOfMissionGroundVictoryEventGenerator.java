package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.AirToGroundVictoryBuilder;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.GroundVictimGenerator;
import pwcg.campaign.crewmember.Victory;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.vehicle.IVehicle;

public class OutOfMissionGroundVictoryEventGenerator 
{    
	private Campaign campaign = null;
    private CrewMember crewMember;

	private OutOfMissionVictoryData victoriesOutOMission = new OutOfMissionVictoryData();

	public OutOfMissionGroundVictoryEventGenerator (Campaign campaign, CrewMember crewMember) 
	{
        this.campaign = campaign;
        this.crewMember = crewMember;
	}
	
    public OutOfMissionVictoryData outOfMissionVictoriesForCrewMember() throws PWCGException 
    {
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        int victoryOdds = determineOddsOfOutOfMissionVictory(configManager);
        determineVictory(victoryOdds);
        return victoriesOutOMission;
    }

    private int determineOddsOfOutOfMissionVictory(ConfigManagerCampaign configManager) throws PWCGException
    {
        int victoryOdds = 5;
        if (crewMember.getAiSkillLevel() == AiSkillLevel.COMMON)
        {
        	victoryOdds += 10;
        }
        else if (crewMember.getAiSkillLevel() == AiSkillLevel.VETERAN)
        {
        	victoryOdds += 20;
        }
        else if (crewMember.getAiSkillLevel() == AiSkillLevel.ACE)
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
        GroundVictimGenerator duringCampaignVictimGenerator = new GroundVictimGenerator(campaign.getDate(), crewMember);
        IVehicle victimVehicle = duringCampaignVictimGenerator.generateVictimVehicle();

        AirToGroundVictoryBuilder outOfMissionVictoryGenerator = new AirToGroundVictoryBuilder(crewMember, victimVehicle);
        Victory victory = outOfMissionVictoryGenerator.generateOutOfMissionVictory(campaign.getDate());

        if (victory != null)
        {
            victoriesOutOMission.addVictoryAwards(crewMember.getSerialNumber(), victory);
        }
    }

	public OutOfMissionVictoryData getVictoriesOutOMission()
	{
		return victoriesOutOMission;
	}  
}
