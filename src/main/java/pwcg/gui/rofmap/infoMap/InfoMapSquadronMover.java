package pwcg.gui.rofmap.infoMap;

import java.util.Date;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.SquadronIOJson;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class InfoMapSquadronMover
{
    private Integer squadronIdToMove = -1;

    public void moveSquadron (String targetAirfield, Date assignmentDate) throws PWCGException
    {
        if (squadronIdToMove > 0)
        {
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronIdToMove);
            System.out.println("Move squadron " + squadron.getFileName() + " to airfield "  + targetAirfield + " on " + assignmentDate);
            if (squadron != null)
            {
                squadron.assignAirfield(assignmentDate, targetAirfield);
                SquadronIOJson.writeJson(squadron);
                squadronIdToMove = -1;
            }
        }
    }
    
    public Integer getSquadronIdToMove()
    {
        return squadronIdToMove;
    }

    public void setSquadronIdToMove(Integer squadronIdToMove)
    {
        this.squadronIdToMove = squadronIdToMove;
    }

    
}
