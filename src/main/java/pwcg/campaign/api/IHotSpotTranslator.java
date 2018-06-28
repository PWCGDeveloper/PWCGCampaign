package pwcg.campaign.api;

import java.util.Date;
import java.util.List;

import pwcg.campaign.group.airfield.HotSpot;
import pwcg.core.exception.PWCGException;

public interface IHotSpotTranslator
{
    public List<HotSpot> getHotSpots(IAirfield airfield, Date date) throws PWCGException;
}