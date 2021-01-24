package pwcg.mission;

import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.FixedPosition;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;

public class MissionBlockCountry
{
    private Mission mission;

    public MissionBlockCountry(Mission mission)
    {
        this.mission = mission;
    }

    public void setCountriesForFixedPositions(List<FixedPosition> fixedPositions) throws PWCGException
    {
        for (FixedPosition fixedPosition : fixedPositions)
        {
            if (!setCountryFromAirfield(fixedPosition))
            {
                fixedPosition.setCountry(fixedPosition.createCountry(mission.getCampaign().getDate()).getCountry());
            }
        }
    }

    private boolean setCountryFromAirfield(FixedPosition fixedPosition) throws PWCGException
    {
        AirfieldManager airfieldManager = PWCGContext.getInstance().getCurrentMap().getAirfieldManager();
        Airfield field = airfieldManager.getAirfieldFinder().findClosestAirfield(fixedPosition.getPosition());
        if (MathUtils.calcDist(fixedPosition.getPosition(), field.getPosition()) < 5000)
        {
            fixedPosition.setCountry(field.getCountry(mission.getCampaign().getDate()).getCountry());
            return true;
        }

        return false;
    }

}
