package pwcg.campaign.skin;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.plane.PlaneMcu;

public class TacticalCodeItaly extends TacticalCode
{
    public TacticalCode buildTacticalCode(Campaign campaign, PlaneMcu plane, Squadron squadron) throws PWCGException
    {
        String squadronUnitCode = squadron.determineUnitIdCode(campaign.getDate());

        if (!isValid(plane, squadronUnitCode))
        {
            return null;
        }

        List<String> codes = new ArrayList<>();
        for (int i = 0; i < squadronUnitCode.length(); ++i)
        {
            String code = squadronUnitCode.substring(i, i+1);
            codes.add(code);
        }
        
        TacticalCodeColor color = determineTacticalCodeColorForFighters();
        List<TacticalCodeColor> colors = new ArrayList<>();
        for (int i = 0; i < codes.size(); ++i)
        {
            colors.add(color);
        }
        super.createExplicitCodes(codes, colors);
        return this;
    }

    private boolean isValid(PlaneMcu plane, String squadronUnitCode)
    {
        if (plane.getSkin().isUseTacticalCodes() == false)
        {
            return false;
        }

        if (squadronUnitCode == null || squadronUnitCode.isEmpty())
        {
            return false;
        }
        
        return true;
    }

    private TacticalCodeColor determineTacticalCodeColorForFighters() throws PWCGException
    {
        return TacticalCodeColor.BLACK;
    }
}
