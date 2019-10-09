package pwcg.campaign.squadmember;

import java.util.Date;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTurret;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public class VictoryBuilder 
{
    private Campaign campaign;
    
    public VictoryBuilder (Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public Victory buildVictory(Date victoryDate, LogVictory missionVictory) throws PWCGException
    {
        Victory victory = new Victory();
        initializeVictory(victoryDate, missionVictory, victory);
        completeAirVictory(missionVictory, victory);
        
        return victory;
    }

    private void initializeVictory(Date victoryDate, LogVictory logVictory, Victory victory) throws PWCGException
    {
        String victimName = getSquadronMemberNameForLogEvent(logVictory.getVictim());
        String victorName = getSquadronMemberNameForLogEvent(logVictory.getVictor());
        String eventLocation = getEventLocation(logVictory.getLocation());

        victory.getVictim().initialize(victoryDate, logVictory.getVictim(), victimName);
        victory.getVictor().initialize(victoryDate, logVictory.getVictor(), victorName);
        victory.setLocation(eventLocation);
        victory.setDate(victoryDate);
    }
    
    private String getSquadronMemberNameForLogEvent(LogAIEntity logEntity) throws PWCGException
    {
        String squadronMemberName = "Unknown";
        if (logEntity instanceof LogTurret)
        {
            LogTurret logTurret = (LogTurret)logEntity;
            logEntity = logTurret.getParent();
        }
        if (logEntity instanceof LogPlane)
        {
            LogPlane logPlane = (LogPlane)logEntity;
            SquadronMember squadronMember = logPlane.getSquadronMemberForLogEvent(campaign);
            if (squadronMember != null)
            {
                squadronMemberName = squadronMember.getNameAndRank();
            }
        }
        
        return squadronMemberName;
    }

    private void completeAirVictory(LogVictory missionVictory, Victory victory)
    {
        if (missionVictory.getVictim() instanceof LogPlane)
        {
            LogPlane missionPlane = (LogPlane)missionVictory.getVictim();
            victory.setCrashedInSight(missionPlane.isCrashedInSight());
        }
    }

    private String getEventLocation(Coordinate eventPosition) throws PWCGException
    {
        String eventLocation =  PWCGContext.getInstance().getCurrentMap().getGroupManager().getTownFinder().findClosestTown(eventPosition).getName();
        if (eventLocation == null || eventLocation.isEmpty())
        {
            eventLocation = "";
        }
        
        return eventLocation;
    }
}
