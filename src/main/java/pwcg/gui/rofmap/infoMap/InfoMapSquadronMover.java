package pwcg.gui.rofmap.infoMap;

import java.util.Date;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.CompanyIOJson;
import pwcg.core.exception.PWCGException;

public class InfoMapSquadronMover
{
    private Integer squadronIdToMove = -1;

    public void moveSquadron (String targetAirfield, Date assignmentDate) throws PWCGException
    {
        if (squadronIdToMove > 0)
        {
            Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(squadronIdToMove);
            if (squadron != null)
            {
                squadron.assignAirfield(assignmentDate, targetAirfield);
                CompanyIOJson.writeJson(squadron);
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
