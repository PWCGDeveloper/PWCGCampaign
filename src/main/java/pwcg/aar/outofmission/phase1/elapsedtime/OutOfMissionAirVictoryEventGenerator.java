package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGFront;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.outofmission.DuringCampaignAirVictimGenerator;
import pwcg.campaign.outofmission.IVictimGenerator;
import pwcg.campaign.outofmission.OutOfMissionAirVictoryBuilder;
import pwcg.campaign.personnel.EnemySquadronFinder;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.product.bos.country.BoSServiceManager;

public class OutOfMissionAirVictoryEventGenerator 
{    
	private Campaign campaign = null;
    private AARContext aarContext; 
    private SquadronMember squadronMember;

	private OutOfMissionVictoryData victoriesOutOMission = new OutOfMissionVictoryData();

	public OutOfMissionAirVictoryEventGenerator (Campaign campaign, AARContext aarContext, SquadronMember squadronMember) 
	{
        this.campaign = campaign;
        this.aarContext = aarContext;
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
        
        victoryOdds = addLuftwaffeEasternFrontBonus(victoryOdds);

        return victoryOdds;
    }

    private int addLuftwaffeEasternFrontBonus(int victoryOdds) throws PWCGException
    {
        ArmedService service = squadronMember.determineService(campaign.getDate());
        if (service.getServiceId() == BoSServiceManager.LUFTWAFFE)
        {
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronMember.getSquadronId());
            if (squadron != null)
            {
                PWCGMap map = squadron.getMapForAirfield(campaign.getDate());
                if (map != null)
                {
                    if (map.getMapIdentifier().getFront() == PWCGFront.WWII_EASTERN_FRONT)
                    {
                        if (squadronMember.getAiSkillLevel() == AiSkillLevel.ACE)
                        {
                            victoryOdds += 30;
                        }
                        else if (squadronMember.getAiSkillLevel() == AiSkillLevel.VETERAN)
                        {
                            victoryOdds += 20;
                        }
                        else
                        {
                            victoryOdds += 10;
                        }
                    }
                }
            }
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
        Squadron victorSquadron = squadronMember.determineSquadron();
        EnemySquadronFinder enemySquadronFinder = new EnemySquadronFinder(campaign);
        Squadron victimSquadron = enemySquadronFinder.getEnemyForOutOfMission(victorSquadron, campaign.getDate());
        if (victimSquadron != null)
        {
            IVictimGenerator duringCampaignVictimGenerator = new DuringCampaignAirVictimGenerator(campaign, victimSquadron);
            generateVictoryWithSquadron(victimSquadron, duringCampaignVictimGenerator);
        }
    }

    private void generateVictoryWithSquadron(Squadron victimSquadron, IVictimGenerator victimGenerator) throws PWCGException
    {

        OutOfMissionAirVictoryBuilder outOfMissionVictoryGenerator = new OutOfMissionAirVictoryBuilder(campaign, victimSquadron, victimGenerator, squadronMember);
        Victory victory = outOfMissionVictoryGenerator.generateOutOfMissionVictory(campaign.getDate());

        if (victory != null)
        {
            SquadronMember shotDownPilot = outOfMissionVictoryGenerator.getVictimPilot();
            EquippedPlane shotDownPlane = outOfMissionVictoryGenerator.getVictimPlane();
            if (shotDownPilot!= null && shotDownPlane != null && shotDownPilot.getSerialNumber() >= SerialNumber.AI_STARTING_SERIAL_NUMBER)
            {
                victoriesOutOMission.addVictoryAwards(squadronMember.getSerialNumber(), victory);
                victoriesOutOMission.addShotDownPilot(shotDownPilot);
                
                LogPlane logPlane = new LogPlane(aarContext.getNextOutOfMissionEventSequenceNumber());
                logPlane.initializeFromOutOfMission(campaign, shotDownPlane, shotDownPilot);
                
                victoriesOutOMission.addShotDownPlane(logPlane);
            }
        }
    }

	public OutOfMissionVictoryData getVictoriesOutOMission()
	{
		return victoriesOutOMission;
	}  
}
