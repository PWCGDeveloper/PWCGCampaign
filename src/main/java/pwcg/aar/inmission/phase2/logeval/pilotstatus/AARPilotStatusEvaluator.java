package pwcg.aar.inmission.phase2.logeval.pilotstatus;

import java.util.List;

import pwcg.aar.inmission.phase2.logeval.AARDestroyedStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.AARVehicleBuilder;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SerialNumber.SerialNumberClassification;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.IAType2;
import pwcg.core.logfiles.event.IAType3;

public class AARPilotStatusEvaluator 
{
    private Campaign campaign;
    private PwcgMissionData pwcgMissionData;
    private LogEventData logEventData;
    private AARVehicleBuilder aarVehicleBuilder;
    private AARPilotStatusDeadEvaluator pilotStatusDeadEvaluator;
    private AARPilotStatusCapturedEvaluator pilotStatusCapturedEvaluator;
    private AARPilotStatusWoundedEvaluator pilotStatusWoundedEvaluator;
    
    public AARPilotStatusEvaluator(Campaign campaign, PwcgMissionData pwcgMissionData, AARDestroyedStatusEvaluator destroyedStatusEvaluator, LogEventData logEventData, AARVehicleBuilder aarVehicleBuilder)
    {
        this.campaign = campaign;
        this.pwcgMissionData = pwcgMissionData;
        this.logEventData = logEventData;
        this.aarVehicleBuilder = aarVehicleBuilder;

        pilotStatusDeadEvaluator = new AARPilotStatusDeadEvaluator(campaign, destroyedStatusEvaluator);
        pilotStatusCapturedEvaluator = new AARPilotStatusCapturedEvaluator(campaign.getDate());
        pilotStatusWoundedEvaluator = new AARPilotStatusWoundedEvaluator();
    }

    public void determineFateOfCrewsInMission () throws PWCGException 
    {        
        for (LogPlane resultPlane : aarVehicleBuilder.getLogPlanes().values())
        {            
            LogPilot resultCrewmember = resultPlane.getLogPilot();
            determinePilotStatus(resultPlane, resultCrewmember);
        }
    }


    private void determinePilotStatus(
                    LogPlane resultPlane,
                    LogPilot resultCrewmember) throws PWCGException
    {
        setCrewMemberWounded(resultCrewmember);
        setCrewMemberCaptured(resultPlane, resultCrewmember);
        setCrewMemberDead(resultPlane, resultCrewmember);
        adjustForPlayer(resultCrewmember);
    }

    private void adjustForPlayer(LogPilot resultCrewmember) throws PWCGException
	{
    	AARPlayerStatusAdjuster playerStatusAdjuster = new AARPlayerStatusAdjuster(campaign);
        if (SerialNumber.getSerialNumberClassification(resultCrewmember.getSerialNumber()) == SerialNumberClassification.PLAYER)
    	{
        	playerStatusAdjuster.adjustForPlayer(resultCrewmember);
    	}
	}

	private void setCrewMemberWounded(LogPilot resultCrewmember) throws PWCGException 
    {        
        List<IAType2> damageForBot = logEventData.getDamageForBot(resultCrewmember.getBotId());
        int woundLevel = pilotStatusWoundedEvaluator.getCrewMemberWoundedLevel(damageForBot);
        if (woundLevel == SquadronMemberStatus.STATUS_WOUNDED)
        {
            resultCrewmember.setStatus(woundLevel);
        }
        else if (woundLevel == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            resultCrewmember.setStatus(woundLevel);
        }
    }

    private void setCrewMemberCaptured(LogPlane resultPlane, LogPilot resultCrewmember) 
    {   
        if (resultPlane.getLandAt() != null)
        {
            Coordinate landingCoords = resultPlane.getLandAt();
            String missionMapName = pwcgMissionData.getMissionHeader().getMapName();
            FrontMapIdentifier mapId = FrontMapIdentifier.getFrontMapIdentifierForName(missionMapName);
            boolean wasCaptured = pilotStatusCapturedEvaluator.isCrewMemberCaptured(mapId, landingCoords, resultPlane.getCountry().getSide());
            if (wasCaptured)
            {
                resultCrewmember.setStatus(SquadronMemberStatus.STATUS_CAPTURED);
            }
        }
     }

    private void setCrewMemberDead(LogPlane resultPlane, LogPilot resultCrewmember) throws PWCGException 
    {        
        int oddsOfDeathDueToAiStupidity = 10;
        IAType3 destroyedEventForPlane = logEventData.getDestroyedEventForPlaneByBot(resultCrewmember.getBotId());
        if (destroyedEventForPlane == null)
        {
            return;
        }
        
        pilotStatusDeadEvaluator.initialize(
                        resultPlane.getLandAt(), 
                        resultCrewmember,
                        destroyedEventForPlane, 
                        oddsOfDeathDueToAiStupidity);

        boolean isDead = pilotStatusDeadEvaluator.isCrewMemberDead();
        if (isDead)
        {
            resultCrewmember.setStatus(SquadronMemberStatus.STATUS_KIA);
        }
    }

    public void setAarVehicleBuilder(AARVehicleBuilder aarVehicleBuilder)
    {
        this.aarVehicleBuilder = aarVehicleBuilder;
    }

    public void setAarPilotStatusDeadEvaluator(AARPilotStatusDeadEvaluator aarPilotStatusDeadEvaluator)
    {
        this.pilotStatusDeadEvaluator = aarPilotStatusDeadEvaluator;
    }

    public void setAarPilotStatusCapturedEvaluator(AARPilotStatusCapturedEvaluator aarPilotStatusCapturedEvaluator)
    {
        this.pilotStatusCapturedEvaluator = aarPilotStatusCapturedEvaluator;
    }

    public void setAarPilotStatusWoundedEvaluator(AARPilotStatusWoundedEvaluator aarPilotStatusWoundedEvaluator)
    {
        this.pilotStatusWoundedEvaluator = aarPilotStatusWoundedEvaluator;
    }
}

