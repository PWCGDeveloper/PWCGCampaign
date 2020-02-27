package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Date;

import pwcg.aar.data.AARContext;
import pwcg.aar.ui.events.model.EndOfWarEvent;
import pwcg.aar.ui.events.model.SquadronMoveEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class ElapsedTimeEventGenerator
{
    private Campaign campaign;
    private AARContext aarContext;
    private ElapsedTimeEvents elapsedTimeEvents = new ElapsedTimeEvents();
    
    public ElapsedTimeEventGenerator(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }

    public ElapsedTimeEvents createElapsedTimeEvents() throws PWCGException
    {
        if (!endOfWar())
        {
            squadronMove();
        }
        
        return elapsedTimeEvents;
    }

    private boolean endOfWar() throws PWCGException
    {
        Date theEnd = DateUtils.getEndOfWar();
        if (!aarContext.getNewDate().before(theEnd))
        {
            boolean isNewsworthy = true;
            EndOfWarEvent endOfWarEvent = new EndOfWarEvent(theEnd, isNewsworthy);
            elapsedTimeEvents.setEndOfWarEvent(endOfWarEvent);
            return true;
        }
        
        return false;
    }

    private void squadronMove() throws PWCGException
    {
        SquadronMoveHandler squadronMoveHandler = new SquadronMoveHandler(campaign);
        for (SquadronMember player : campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList())
        {
	        SquadronMoveEvent squadronMoveEvent = squadronMoveHandler.squadronMoves(aarContext.getNewDate(), player.determineSquadron());
	        if (squadronMoveEvent != null)
	        {
	            elapsedTimeEvents.addSquadronMoveEvent(squadronMoveEvent);
	        }
        }
    }

}
