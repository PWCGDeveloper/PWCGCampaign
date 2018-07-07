package pwcg.aar.inmission.phase2.logeval.pilotstatus;

import pwcg.aar.inmission.phase1.parse.AARLogParser;
import pwcg.aar.inmission.phase1.parse.event.IAType3;
import pwcg.aar.inmission.phase2.logeval.AARDestroyedStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SerialNumber.SerialNumberClassification;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;

public class AARPilotStatusDeadEvaluator 
{
    private Coordinate downAt = null;
    private IAType3 destroyedEventForCrewmembersPlane = null;
    private LogPilot logPilot = null;
    private int oddsOfDeathDueToAiStupidity;
    private IAirfield field;
    private AARDestroyedStatusEvaluator destroyedStatusEvaluator;

    public AARPilotStatusDeadEvaluator(AARDestroyedStatusEvaluator destroyedStatusEvaluator)
    {
    	this.destroyedStatusEvaluator = destroyedStatusEvaluator;
    }

    public void initialize(Coordinate downAt,
                      	   LogPilot resultCrewmember,
                      	   IAType3 destroyedEventForPilot, 
                      	   IAirfield field,
                      	   int oddsOfDeathDueToAiStupidity)
    {
        this.downAt = downAt;
        this.logPilot = resultCrewmember;
        this.destroyedEventForCrewmembersPlane = destroyedEventForPilot;
        this.field = field;
        this.oddsOfDeathDueToAiStupidity = oddsOfDeathDueToAiStupidity;
    }

    public boolean isCrewMemberDead() throws PWCGException
    {
    	boolean isCrewMemberDead = didCrewMemberDie();
    	if (isCrewMemberDead)
    	{
    	    if (SerialNumber.getSerialNumberClassification(logPilot.getSerialNumber()) != SerialNumberClassification.PLAYER)
    	    {
    	        isCrewMemberDead = didSquadronAiMemberDieAfterSpecialConsiderations();
    	    }
    	}
        
        return isCrewMemberDead;
    }

    private boolean didCrewMemberDie()
    {
        for (LogPilot deadCrewMember : destroyedStatusEvaluator.getDeadLogPilots())
        {
            if (deadCrewMember.getSerialNumber() == logPilot.getSerialNumber())
            {
            	return true;
            }
        }

        return false;
    }

    private boolean didSquadronAiMemberDieAfterSpecialConsiderations() throws PWCGException
    {
        boolean isDead = false;
        
        if (wasSquadronMemberShotDown())
        {
            isDead = true;
        }
        else
        {
            isDead = allowSquadronMemberToSurviveCrashCloseToField();
            if (isDead)
            {
                isDead = reduceSquadronMemberDeathsDueToAccident();
            }
        }

        return isDead;
    }
    
    private boolean wasSquadronMemberShotDown()
    {
        if (destroyedEventForCrewmembersPlane != null)
        {
            if (destroyedEventForCrewmembersPlane.getVictor() != null)
            {
                if (!destroyedEventForCrewmembersPlane.getVictor().equals(AARLogParser.UNKNOWN_MISSION_LOG_ENTITY))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean reduceSquadronMemberDeathsDueToAccident() throws PWCGException
    {
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < oddsOfDeathDueToAiStupidity)
        {
            return true;
        }
        
        return false;
    }

    
    private boolean allowSquadronMemberToSurviveCrashCloseToField()
    {
        boolean isDead = true;
        try
        {
            if (downAt != null)
            {
                if (field != null)
                {
                    double distanceToHomeField = MathUtils.calcDist(downAt, field.getPosition());
                    if (distanceToHomeField < 3000.0)
                    {
                        isDead = false;
                    }
                }
            }
        }
        catch (Exception e)
        {
            Logger.logException(e);
        }
        
        return isDead;
    }
}

