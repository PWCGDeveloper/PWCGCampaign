package pwcg.campaign.skin;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.plane.PlaneMcu;

public class TacticalCodeUSA extends TacticalCode
{
    private List<String> codes = new ArrayList<>();
    private List<TacticalCodeColor> colors = new ArrayList<>();

    @Override
    public TacticalCode buildTacticalCode(Campaign campaign, PlaneMcu plane, Squadron squadron) throws PWCGException
    {
        initializeCode();
        initializeColors(campaign);

        String squadronUnitCode = squadron.determineUnitIdCode(campaign.getDate());
        String aircraftCode = plane.getAircraftIdCode();
        if (!isValid(plane, squadronUnitCode, aircraftCode))
        {
            return null;
        }

        codes.set(0, squadronUnitCode.substring(0, 1));
        codes.set(1, squadronUnitCode.substring(1, 2));
        codes.set(2, aircraftCode.substring(0, 1));

        TacticalCodeColor color = determineTacticalCodeColor(campaign, squadron, plane);
        for (int i = 0; i < codes.size(); ++i)
        {
            colors.set(i, color);
        }
        super.createExplicitCodes(codes, colors);
        return this;
    }

    private void initializeCode()
    {
        codes.add("%20");
        codes.add("%20");
        codes.add("%20");
    }

    private void initializeColors(Campaign campaign) throws PWCGException
    {
        if (campaign.getDate().before(DateUtils.getDateYYYYMMDD("19440101")))
        {
            colors.add(TacticalCodeColor.WHITE);
            colors.add(TacticalCodeColor.WHITE);
            colors.add(TacticalCodeColor.WHITE);
        }
        else
        {
            colors.add(TacticalCodeColor.BLACK);
            colors.add(TacticalCodeColor.BLACK);
            colors.add(TacticalCodeColor.BLACK);
        }
    }

    private boolean isValid(PlaneMcu plane, String squadronUnitCode, String aircraftCode)
    {
        if (plane.getSkin().isUseTacticalCodes() == false)
        {
            return false;
        }

        if (squadronUnitCode == null || squadronUnitCode.isEmpty())
        {
            return false;
        }

        if (aircraftCode == null || aircraftCode.isEmpty())
        {
            return false;
        }
        
        return true;
    }

    private TacticalCodeColor determineTacticalCodeColor(Campaign campaign, Squadron squadron, PlaneMcu plane) throws PWCGException
    {
        if (squadron.getSquadronTacticalCodeColorOverride() != TacticalCodeColor.NONE)
        {
            return squadron.getSquadronTacticalCodeColorOverride();
        }

        if (plane.getSkin().getTacticalCodeColor() != TacticalCodeColor.NONE)
        {
            return plane.getSkin().getTacticalCodeColor();
        }

        if (campaign.getDate().before(DateUtils.getDateYYYYMMDD("19440101")))
        {
            return TacticalCodeColor.WHITE;
        }
        return TacticalCodeColor.BLACK;
    }
}
