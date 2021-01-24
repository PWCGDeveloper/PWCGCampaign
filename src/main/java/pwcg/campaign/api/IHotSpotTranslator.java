package pwcg.campaign.api;

import java.util.Date;
import java.util.List;

import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.group.airfield.hotspot.HotSpot;
import pwcg.core.exception.PWCGException;

public interface IHotSpotTranslator
{
    public List<HotSpot> getHotSpots(Airfield airfield, Date date) throws PWCGException;
}