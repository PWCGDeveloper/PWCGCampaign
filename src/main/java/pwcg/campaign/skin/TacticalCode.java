package pwcg.campaign.skin;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.plane.PlaneMcu;

public abstract class TacticalCode
{
    protected List<TacticalCodeElement> tacticalCodeElements = new ArrayList<>();
    
    public abstract TacticalCode buildTacticalCode(Campaign campaign, PlaneMcu plane, Squadron squadron) throws PWCGException;

    public void createExplicitCodes(List<String> codes, List<TacticalCodeColor> colors)
    {
        for (int i = 0; i < codes.size(); ++i)
        {
            String code = codes.get(i);
            TacticalCodeColor color = colors.get(i);
            if (color == null)
            {
                color = colors.get(0);
            }
            TacticalCodeElement element = new TacticalCodeElement(code, color.getColorCode());
            tacticalCodeElements.add(element);
        }
    }

    public String formCodeString()
    {
        String tactcalCode = "";
        for (TacticalCodeElement tacticalCodeElement : tacticalCodeElements)
        {
            tactcalCode += tacticalCodeElement.getCode();
        }
        return tactcalCode;
    }

    public String formCodeColorString()
    {
        String tactcalCodeColors = "";
        for (TacticalCodeElement tacticalCodeElement : tacticalCodeElements)
        {
            tactcalCodeColors += tacticalCodeElement.getColor();
        }
        return tactcalCodeColors;
    }
}
