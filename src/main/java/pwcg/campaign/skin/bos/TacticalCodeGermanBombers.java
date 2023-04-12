package pwcg.campaign.skin.bos;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.skin.TacticalCode;
import pwcg.campaign.skin.TacticalCodeColor;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.plane.PlaneMcu;

public class TacticalCodeGermanBombers
{
    private List<String> codes = new ArrayList<>();
    private List<TacticalCodeColor> colors = new ArrayList<>();

    public void buildTacticalCode(Campaign campaign, Squadron squadron, PlaneMcu plane) throws PWCGException
    {
        initializeBomberCode();
        initializeBomberColors();
        
        buildTacticalCodeForBombersAndDestroyers(campaign, squadron, plane);
    }

    private void initializeBomberCode()
    {
        codes.add("%20");
        codes.add("%20");
        codes.add("%20");
        codes.add("%20");
    }

    private void initializeBomberColors()
    {
        colors.add(TacticalCodeColor.BLACK);
        colors.add(TacticalCodeColor.BLACK);
        colors.add(TacticalCodeColor.BLACK);
        colors.add(TacticalCodeColor.BLACK);
    }

    private TacticalCode buildTacticalCodeForBombersAndDestroyers(Campaign campaign, Squadron squadron, PlaneMcu plane) throws PWCGException
    {
        if (plane.getSkin().isUseTacticalCodes() == false)
        {
            return null;
        }
        
        String geschewaderCode = squadron.determineUnitIdCode(campaign.getDate());
        if (geschewaderCode == null || geschewaderCode.isEmpty())
        {
            return null;
        }
        
        String aircraftCode = plane.getAircraftIdCode();
        if (aircraftCode == null || aircraftCode.isEmpty())
        {
            return null;
        }
        
        for (int i = 0; i < geschewaderCode.length(); ++i)
        {
            String code = geschewaderCode.substring(i, i+1);
            codes.set(i, code);
        }

        for (int i = 0; i < aircraftCode.length(); ++i)
        {
            String code = aircraftCode.substring(i, i+1);
            codes.set(2, code);
        }

        String gruppeCode = squadron.determineSubUnitIdCode(campaign.getDate());
        for (int i = 0; i < gruppeCode.length(); ++i)
        {
            String code = gruppeCode.substring(i, i+1);
            codes.set(3, code);
        }

        if (codes.size() == 4)
        {
            TacticalCodeColor planeCodeColor = determineTacticalCodeColorForBombersAndDestroyers(squadron, plane);
            colors.set(0, TacticalCodeColor.BLACK);
            colors.set(1, TacticalCodeColor.BLACK);
            colors.set(2, planeCodeColor);
            colors.set(3, TacticalCodeColor.BLACK);
        }
        
        return null;
    }

    private TacticalCodeColor determineTacticalCodeColorForBombersAndDestroyers(Squadron squadron, PlaneMcu plane) throws PWCGException
    {
        if (squadron.getSquadronTacticalCodeColorOverride() !=  TacticalCodeColor.NONE)
        {
            return squadron.getSquadronTacticalCodeColorOverride();
        }
        
        if (plane.getSkin().getTacticalCodeColor() !=  TacticalCodeColor.NONE)
        {
            return plane.getSkin().getTacticalCodeColor();
        }

        return TacticalCodeColor.RED;
    }

    public List<String> getCodes()
    {
        return codes;
    }

    public List<TacticalCodeColor> getColors()
    {
        return colors;
    }
}
