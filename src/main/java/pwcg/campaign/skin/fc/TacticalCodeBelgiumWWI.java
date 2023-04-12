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
import pwcg.product.fc.plane.FCPlaneAttributeMapping;

public class TacticalCodeBelgiumWWI extends TacticalCode
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
        if (isTwoPlaceAircraft(plane))
        {
            codes.add("%20");
            codes.add("%20");
        }
        else
        {
            codes.add("%20");
            codes.add("%20");
            codes.add("%20");
        }
    }

    private void initializeColors(PlaneMcu plane) throws PWCGException
    {
        if (isTwoPlaceAircraft(plane))
        {
            colors.add(TacticalCodeColor.WHITE);
            colors.add(TacticalCodeColor.WHITE);            
        }
        else
        {
            colors.add(TacticalCodeColor.WHITE);
            colors.add(TacticalCodeColor.WHITE);
            colors.add(TacticalCodeColor.WHITE);
        }
    }
    
    private boolean isTwoPlaceAircraft(PlaneMcu plane)
    {
        if (plane.getType().equals(FCPlaneAttributeMapping.BREGUET14.getPlaneType()))
        {
            return true;
        }
        else
        {
            return false;
        }
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

        return TacticalCodeColor.WHITE;
    }
    

    private void formWWICode(PlaneMcu plane, List<String> codes) throws PWCGException
    {
        if (plane.getSkin().getTacticalCodeType() == TacticalCodeType.CODE_POSITION_ONE) 
        {
            codes.set(0, plane.getAircraftIdCode().substring(0,1));
        }
        else if (plane.getSkin().getTacticalCodeType() == TacticalCodeType.CODE_POSITION_TWO)
        {
            if (plane.getType().equals(FCPlaneAttributeMapping.DH4.getPlaneType()))
            {
                throw new PWCGException("Cannot use Belgian position 2 with an Airco DH4");
            }
            codes.set(1, plane.getAircraftIdCode().substring(0,1));
        }
        else if (plane.getSkin().getTacticalCodeType() == TacticalCodeType.CODE_POSITION_THREE)
        {
            if (codes.size() < 3)
            {
                throw new PWCGException("Attempt to use Belgian position 3 when only 2 positions are available");
            }
            codes.set(2, plane.getAircraftIdCode().substring(0,1));
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