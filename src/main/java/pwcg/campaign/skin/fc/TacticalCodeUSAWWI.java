package pwcg.campaign.skin.fc;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.skin.TacticalCode;
import pwcg.campaign.skin.TacticalCodeColor;
import pwcg.campaign.skin.TacticalCodeType;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.plane.PlaneMcu;

public class TacticalCodeUSAWWI extends TacticalCode
{
    private List<String> codes = new ArrayList<>();
    private List<TacticalCodeColor> colors = new ArrayList<>();

    @Override
    public TacticalCode buildTacticalCode(Campaign campaign, PlaneMcu plane, Squadron squadron) throws PWCGException
    {
        initializeCode(plane);
        initializeColors(plane);

        if (!isValid(plane))
        {
            return null;
        }
        
        formWWICode(plane, codes);

        TacticalCodeColor color = determineTacticalCodeColor(squadron, plane);
        for (int i = 0; i < codes.size(); ++i)
        {
            colors.set(i, color);
        }
        
        super.createExplicitCodes(codes, colors);
        return this;
    }

    protected boolean isValid(PlaneMcu plane)
    {
        if (plane.getSkin().isUseTacticalCodes() == false)
        {
            return false;
        }

        String aircraftCode = plane.getAircraftIdCode();
        if (aircraftCode == null || aircraftCode.isEmpty())
        {
            return false;
        }
        
        return true;
    }

    private void initializeCode(PlaneMcu plane)
    {
        codes.add("%20");
        codes.add("%20");
    }

    private void initializeColors(PlaneMcu plane) throws PWCGException
    {
        colors.add(TacticalCodeColor.RED);
        colors.add(TacticalCodeColor.RED);            
    }

    private TacticalCodeColor determineTacticalCodeColor(Squadron squadron, PlaneMcu plane)
    {
        if (squadron.getSquadronTacticalCodeColorOverride() != TacticalCodeColor.NONE)
        {
            return squadron.getSquadronTacticalCodeColorOverride();
        }

        if (plane.getSkin().getTacticalCodeColor() != TacticalCodeColor.NONE)
        {
            return plane.getSkin().getTacticalCodeColor();
        }

        return TacticalCodeColor.RED;
    }

    private void formWWICode(PlaneMcu plane, List<String> codes) throws PWCGException
    {
        if (plane.getSkin().getTacticalCodeType() == TacticalCodeType.CODE_POSITION_ONE) 
        {
            throw new PWCGException("Invalid code type: WWI USA does not support position 1");
        }
        else if (plane.getSkin().getTacticalCodeType() == TacticalCodeType.CODE_POSITION_TWO)
        {
            throw new PWCGException("Invalid code type: WWI USA does not support position 2");
        }
        else if (plane.getSkin().getTacticalCodeType() == TacticalCodeType.CODE_POSITION_THREE)
        {
            throw new PWCGException("Invalid code type: WWI USA does not support position 3");
        }
        else if (plane.getSkin().getTacticalCodeType() == TacticalCodeType.CODE_POSITION_ONE_AND_TWO)
        {
            if (plane.getAircraftIdCode().length() == 1) 
            {
                codes.set(1, plane.getAircraftIdCode().substring(0,1));
            }
            else
            {
                codes.set(0, plane.getAircraftIdCode().substring(0,1));
                codes.set(1, plane.getAircraftIdCode().substring(1,2));
            }
        }
    }
}
