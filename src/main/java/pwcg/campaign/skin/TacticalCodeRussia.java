package pwcg.campaign.skin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.plane.PlaneMcu;

public class TacticalCodeRussia extends TacticalCode
{
    private static final List<String> russianNumberType1 = Arrays.asList("%21", "%22","%23", "%24", "%25", "%26", "%27", "%28", "%29", "%2a"); 
    private static final List<String> russianNumberType2 = Arrays.asList("0", "1","2", "3", "4", "5", "6", "7", "8", "9"); 
    private static final List<String> russianNumberType3 = Arrays.asList("%3a", "%3b","%3c", "%3d", "%3e", "%3f", "%40", "A", "B", "C"); 
    private static final List<String> russianNumberType4 = Arrays.asList("D", "E","F", "G", "H", "I", "J", "K", "L", "M"); 
    private static final List<String> russianNumberType5 = Arrays.asList("N", "O","P", "Q", "R", "S", "T", "U", "V", "W"); 
    
    private List<String> codes = new ArrayList<>();
    private List<TacticalCodeColor> colors = new ArrayList<>();
   
    public TacticalCode buildTacticalCode(Campaign campaign, PlaneMcu plane, Squadron squadron) throws PWCGException
    {
        initializeCode();
        initializeColors();

        if (plane.getSkin().isUseTacticalCodes() == false)
        {
            return null;
        }
        
        String aircraftCode = plane.getAircraftIdCode();
        if (aircraftCode != null && aircraftCode.length() == 1)
        {
            codes.set(0, "%20");
            codes.set(1, plane.getAircraftIdCode().substring(0, 1));
        }
        else if (aircraftCode != null && aircraftCode.length() == 2)
        {
            codes.set(0, plane.getAircraftIdCode().substring(0, 1));
            codes.set(1, plane.getAircraftIdCode().substring(1, 2));
        }
        else
        {
            return null;
        }
        
        return createTacticalCodeWithStyle(plane, squadron);
    }

    private void initializeCode()
    {
        codes.add("%20");
        codes.add("%20");
    }

    private void initializeColors() throws PWCGException
    {
        colors.add(TacticalCodeColor.RED);
        colors.add(TacticalCodeColor.RED);
    }

    private TacticalCode createTacticalCodeWithStyle(PlaneMcu plane, Squadron squadron) throws PWCGException
    {
        createCodesForStyle(squadron.getTacticalCodeStyle());
        if (codes.size() > 0)
        {
            TacticalCodeColor color = determineTacticalCodeColor(squadron, plane);
            for (int i = 0; i < codes.size(); ++i)
            {
                colors.set(i, color);
            }
            super.createExplicitCodes(codes, colors);
            return this;
        }
        
        return null;
    }
    
    private void createCodesForStyle(int style)
    {
        List<String> styleList = getStyleList(style);
        for (int i = 0; i < codes.size(); ++i)
        {
            String baseCode = codes.get(i);
            int codeIndex = -1;
            if (russianNumberType2.contains(baseCode))
            {
                codeIndex = russianNumberType2.indexOf(baseCode);
            }
            
            if (codeIndex == -1)
            {
                codes.set(i, "%20");
            }
            else
            {
                codes.set(i, styleList.get(codeIndex));
            }
        }
    }

    private TacticalCodeColor determineTacticalCodeColor(Squadron squadron, PlaneMcu plane) throws PWCGException
    {
        if (squadron.getSquadronTacticalCodeColorOverride() != TacticalCodeColor.NONE)
        {
            return squadron.getSquadronTacticalCodeColorOverride();
        }
        
        if (plane.getSkin().getTacticalCodeColor() !=  TacticalCodeColor.NONE)
        {
            return plane.getSkin().getTacticalCodeColor();
        }

        return TacticalCodeColor.BLACK;
    }


    private List<String> getStyleList(int style)
    {
        if (style == 1)
        {
            return russianNumberType1;
        }
        else if (style == 2)
        {
            return russianNumberType2;
        }
        else if (style == 3)
        {
            return russianNumberType3;
        }
        else if (style == 4)
        {
            return russianNumberType4;
        }
        else if (style == 5)
        {
            return russianNumberType5;
        }
        else
        {
            return russianNumberType2;
        }
    }
}
