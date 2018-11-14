package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase1.parse.event.IAType3;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;


public class AARDestroyedStatusEvaluator 
{
    private List <LogVictory> deadLogVehicles = new ArrayList <LogVictory>();
    private List <LogPilot> deadLogPilots = new ArrayList <LogPilot>();

    private AARVehicleBuilder aarVehicleBuilder;
    private AARLogEventData logEventData;
    private AARCrossedPathWithPlayerEvaluator aarCrossedPathWithPlayerEvaluator;
    private AARDamageStatusEvaluator aarDamageStatusEvaluator;
    
    public AARDestroyedStatusEvaluator(
                    Campaign campaign,
                    AARLogEventData logEventData, 
                    AARVehicleBuilder aarVehicleBuilder, 
                    AARDamageStatusEvaluator aarDamageStatusEvaluator)
    {
        this.logEventData = logEventData;        
        this.aarVehicleBuilder = aarVehicleBuilder;
        this.aarCrossedPathWithPlayerEvaluator = new AARCrossedPathWithPlayerEvaluator(campaign);
        this.aarDamageStatusEvaluator = aarDamageStatusEvaluator;        
    }

    public void buildDeadLists() throws PWCGException 
    {
        for (IAType3 atype3 : logEventData.getDestroyedEvents())
        {
            LogAIEntity logVictor = aarVehicleBuilder.getVehicle(atype3.getVictor());
            LogAIEntity logVictim = aarVehicleBuilder.getVehicle(atype3.getVictim());
            
            LogVictory logVictory = new LogVictory(atype3.getSequenceNum());
            logVictory.setLocation(atype3.getLocation());
            
            if (logVictim != null)
            {
                logVictory.setVictim(logVictim);
                if (logVictor != null)
                {
                    logVictory.setVictor(logVictor);
                }

                setCrossedPathWithPlayer(logVictory);

                deadLogVehicles.add(logVictory);                
            }
            else
            {
            	LogPilot deadPilot = matchDeadBotToCrewMember(atype3);
            	if (deadPilot != null)
            	{
            		deadLogPilots.add(deadPilot);
            	}
            }
        }
    }
    
    private LogPilot matchDeadBotToCrewMember(IAType3 atype3)
    {
    	for (LogPlane planeEntity : aarVehicleBuilder.getLogPlanes().values())
    	{
    	    LogPilot crewMemberEntity = planeEntity.getLogPilot();
            if (crewMemberEntity.getBotId().equals(atype3.getVictim()))
            {
                return crewMemberEntity;
            }
    	}
    	
    	return null;
    }

    private void setCrossedPathWithPlayer(LogVictory victoryResult) throws PWCGException
    {
        boolean crossedPathWithPlayer = aarCrossedPathWithPlayerEvaluator.isCrossedPathWithPlayerFlight(
                        victoryResult, 
                        aarDamageStatusEvaluator.getVehiclesDamaged(),
                        logEventData.getWaypointEvents());
        
        victoryResult.setCrossedPlayerPath(crossedPathWithPlayer);
    }
    
    public void setAarCrossedPathWithPlayerEvaluator(AARCrossedPathWithPlayerEvaluator aarCrossedPathWithPlayerEvaluator)
    {
        this.aarCrossedPathWithPlayerEvaluator = aarCrossedPathWithPlayerEvaluator;
    }

    public List<LogVictory> getDeadLogVehicleList()
    {
        return deadLogVehicles;
    }

    public List<LogPilot> getDeadLogPilots()
    {
        return deadLogPilots;
    }
}

