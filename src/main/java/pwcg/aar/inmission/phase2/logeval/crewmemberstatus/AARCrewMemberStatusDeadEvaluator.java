package pwcg.aar.inmission.phase2.logeval.crewmemberstatus;

import pwcg.aar.inmission.phase2.logeval.AARDestroyedStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogCrewMember;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.SerialNumber.SerialNumberClassification;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.logfiles.LogParser;
import pwcg.core.logfiles.event.IAType3;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.RandomNumberGenerator;

public class AARCrewMemberStatusDeadEvaluator 
{
    private Coordinate downAt = null;
    private IAType3 destroyedEventForCrewmembersPlane = null;
    private LogCrewMember logCrewMember = null;
    private int oddsOfDeathDueToAiStupidity;
    private Campaign campaign;
    private AARDestroyedStatusEvaluator destroyedStatusEvaluator;

    public AARCrewMemberStatusDeadEvaluator(Campaign campaign, AARDestroyedStatusEvaluator destroyedStatusEvaluator)
    {
        this.campaign = campaign;
    	this.destroyedStatusEvaluator = destroyedStatusEvaluator;
    }

    public void initialize(Coordinate downAt,
                      	   LogCrewMember resultCrewmember,
                      	   IAType3 destroyedEventForCrewMember, 
                      	   int oddsOfDeathDueToAiStupidity)
    {
        this.downAt = downAt;
        this.logCrewMember = resultCrewmember;
        this.destroyedEventForCrewmembersPlane = destroyedEventForCrewMember;
        this.oddsOfDeathDueToAiStupidity = oddsOfDeathDueToAiStupidity;
    }

    public boolean isCrewMemberDead() throws PWCGException
    {
    	boolean isCrewMemberDead = destroyedStatusEvaluator.didCrewMemberDie(logCrewMember.getSerialNumber());
    	if (isCrewMemberDead)
    	{
    	    if (SerialNumber.getSerialNumberClassification(logCrewMember.getSerialNumber()) != SerialNumberClassification.PLAYER)
    	    {
    	        isCrewMemberDead = didSquadronAiMemberDieAfterSpecialConsiderations();
    	    }
    	}
        
        return isCrewMemberDead;
    }

    private boolean didSquadronAiMemberDieAfterSpecialConsiderations() throws PWCGException
    {
        boolean isDead = false;
        
        if (wasCrewMemberShotDown())
        {
            isDead = true;
        }
        else
        {
            isDead = allowCrewMemberToSurviveCrashCloseToField();
            if (isDead)
            {
                isDead = reduceCrewMemberDeathsDueToAccident();
            }
        }

        return isDead;
    }
    
    private boolean wasCrewMemberShotDown()
    {
        if (destroyedEventForCrewmembersPlane != null)
        {
            if (destroyedEventForCrewmembersPlane.getVictor() != null)
            {
                if (!destroyedEventForCrewmembersPlane.getVictor().equals(LogParser.UNKNOWN_MISSION_LOG_ENTITY))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean reduceCrewMemberDeathsDueToAccident() throws PWCGException
    {
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < oddsOfDeathDueToAiStupidity)
        {
            return true;
        }
        
        return false;
    }

    
    private boolean allowCrewMemberToSurviveCrashCloseToField()
    {
        boolean isDead = true;
        try
        {
            if (downAt != null)
            {
            	CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(logCrewMember.getSerialNumber());
            	Coordinate fieldPosition = crewMember.determineSquadron().determineCurrentPosition(campaign.getDate());
                if (fieldPosition != null)
                {
                    double distanceToHomeField = MathUtils.calcDist(downAt, fieldPosition);
                    if (distanceToHomeField < 3000.0)
                    {
                        isDead = false;
                    }
                }
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
        
        return isDead;
    }
}

