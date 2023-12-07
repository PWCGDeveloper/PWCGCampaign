package pwcg.aar;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class SquadronForMissionBuilderBoS
{
    Campaign campaign;
    List<Squadron> squadronsInMission = new ArrayList<>();


    public SquadronForMissionBuilderBoS (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public List<Squadron> makeSquadronsInMission() throws PWCGException
    {
        Squadron jg51 = PWCGContext.getInstance().getSquadronManager().getSquadron(20111051);
        Squadron stg77 = PWCGContext.getInstance().getSquadronManager().getSquadron(20121077);
        Squadron kg76 = PWCGContext.getInstance().getSquadronManager().getSquadron(20132076);
        Squadron reg11 = PWCGContext.getInstance().getSquadronManager().getSquadron(10111011);
        Squadron reg132 = PWCGContext.getInstance().getSquadronManager().getSquadron(10131132);
        Squadron reg175 = PWCGContext.getInstance().getSquadronManager().getSquadron(10121175);
        
        squadronsInMission.add(jg51);
        squadronsInMission.add(stg77);
        squadronsInMission.add(kg76);
        squadronsInMission.add(reg11);
        squadronsInMission.add(reg132);
        squadronsInMission.add(reg175);
        
        return squadronsInMission;
    }

}
