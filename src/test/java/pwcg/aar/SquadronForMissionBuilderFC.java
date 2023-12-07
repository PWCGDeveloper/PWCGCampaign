package pwcg.aar;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class SquadronForMissionBuilderFC
{
    Campaign campaign;
    List<Squadron> squadronsInMission = new ArrayList<>();


    public SquadronForMissionBuilderFC (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public List<Squadron> makeSquadronsInMission() throws PWCGException
    {
        Squadron jasta4 = PWCGContext.getInstance().getSquadronManager().getSquadron(401004);
        Squadron fa19 = PWCGContext.getInstance().getSquadronManager().getSquadron(401219);
        Squadron esc2 = PWCGContext.getInstance().getSquadronManager().getSquadron(301002);
        Squadron esc12 = PWCGContext.getInstance().getSquadronManager().getSquadron(301012);
        Squadron no16Squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(302016);
        Squadron no24Squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(302024);
        
        squadronsInMission.add(jasta4);
        squadronsInMission.add(fa19);
        squadronsInMission.add(esc2);
        squadronsInMission.add(esc12);
        squadronsInMission.add(no16Squadron);
        squadronsInMission.add(no24Squadron);
        
        return squadronsInMission;
    }

}
