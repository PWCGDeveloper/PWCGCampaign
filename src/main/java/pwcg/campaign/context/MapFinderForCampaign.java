package pwcg.campaign.context;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.gui.utils.ReferencePlayerFinder;

public class MapFinderForCampaign
{

    public static FrontMapIdentifier findMapForCampaign(Campaign campaign) throws PWCGException
    {
        if (campaign != null)
        {
            Squadron representativePlayerSquadron = ReferencePlayerFinder.getRepresentativeSquadronForCampaign(campaign);
            List<FrontMapIdentifier> mapIdentifiers = MapForAirfieldFinder.getMapForAirfield(representativePlayerSquadron.determineCurrentAirfieldName(campaign.getDate()));
            if (mapIdentifiers.size() > 0)
            {
                return mapIdentifiers.get(0);
            }
        }
        return null;
    }
}
