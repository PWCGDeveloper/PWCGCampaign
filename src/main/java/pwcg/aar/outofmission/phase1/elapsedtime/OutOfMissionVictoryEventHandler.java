package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.aar.data.AARContext;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.outofmission.DuringCampaignVictimGenerator;
import pwcg.campaign.outofmission.OutOfMissionVictoryGenerator;
import pwcg.campaign.personnel.EnemySquadronFinder;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.ww2.country.BoSServiceManager;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class OutOfMissionVictoryEventHandler 
{    
	private Campaign campaign = null;
    private AARContext aarContext; 

	private OutOfMissionVictoryData victoriesOutOMission = new OutOfMissionVictoryData();

	public OutOfMissionVictoryEventHandler (Campaign campaign, AARContext aarContext) 
	{
        this.campaign = campaign;
        this.aarContext = aarContext;
	}
	
    
    public OutOfMissionVictoryData generateOutOfMissionVictories() throws PWCGException
    {
        for (SquadronMember squadronMember : aarContext.getPreliminaryData().getCampaignMembersOutOfMission().getSquadronMemberCollection().values())
        {
            if (OutOfMissionPilotSelector.shouldPilotBeEvaluated(campaign, squadronMember)) 
            {
                outOfMissionVictoriesForSquadronMember(squadronMember);
            }
        }

        return victoriesOutOMission;
    }
	
    public void outOfMissionVictoriesForSquadronMember(SquadronMember squadronMember) throws PWCGException 
    {
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        int victoryOdds = determineOddsOfOutOfMissionVictory(squadronMember, configManager);
        determineVictory(squadronMember, victoryOdds);
    }

    private int determineOddsOfOutOfMissionVictory(SquadronMember squadronMember, ConfigManagerCampaign configManager) throws PWCGException
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
        
        ArmedService service = squadronMember.determineService(campaign.getDate());
        if (service.equals(BoSServiceManager.LUFTWAFFE))
        {
            victoryOdds += 30;
        }

        return victoryOdds;
    }

    private void determineVictory(SquadronMember victorPilot, int victoryOdds) throws PWCGException 
    {
        int victoryDiceRoll = RandomNumberGenerator.getRandom(1000);
        if (victoryDiceRoll < victoryOdds)
        {
            generateVictory(victorPilot);
        }
    }

    private void generateVictory(SquadronMember victorPilot) throws PWCGException
    {
        Squadron victorSquadron = victorPilot.determineSquadron();
        EnemySquadronFinder enemySquadronFinder = new EnemySquadronFinder(campaign);
        Squadron victimSquadron = enemySquadronFinder.getRandomEnemyViableSquadron(victorSquadron, campaign.getDate());
        if (victimSquadron != null)
        {
            generateVictoryWithSquadron(victorPilot, victimSquadron);
        }
    }

    private void generateVictoryWithSquadron(SquadronMember victorPilot, Squadron victimSquadron)
            throws PWCGException
    {
        DuringCampaignVictimGenerator duringCampaignVictimGenerator = new DuringCampaignVictimGenerator(campaign, victimSquadron);

        OutOfMissionVictoryGenerator outOfMissionVictoryGenerator = new OutOfMissionVictoryGenerator(victimSquadron, duringCampaignVictimGenerator, victorPilot);
        Victory victory = outOfMissionVictoryGenerator.generateOutOfMissionVictory(campaign.getDate());

        if (victory != null)
        {
            SquadronMember shotDownPilot = outOfMissionVictoryGenerator.getVictimPilot();
            EquippedPlane shotDownPlane = outOfMissionVictoryGenerator.getVictimPlane();
            if (shotDownPilot!= null && shotDownPlane != null && shotDownPilot.getSerialNumber() >= SerialNumber.AI_STARTING_SERIAL_NUMBER)
            {
                victoriesOutOMission.addVictoryAwards(victorPilot.getSerialNumber(), victory);
                victoriesOutOMission.addShotDownPilot(shotDownPilot);
                victoriesOutOMission.addShotDownPlane(shotDownPlane);
            }
        }
    }

	public OutOfMissionVictoryData getVictoriesOutOMission()
	{
		return victoriesOutOMission;
	}
    
    
}
