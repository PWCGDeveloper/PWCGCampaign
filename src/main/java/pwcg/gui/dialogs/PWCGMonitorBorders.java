package pwcg.gui.dialogs;

import java.awt.Dimension;
import java.awt.Insets;

public class PWCGMonitorBorders 
{
    public static Insets calculateBorderMargins(int top, int left, int bottom, int right)
    {
        int marginTop = calculateInsetVertical(top);
        int marginLeft = calculateInsetHorixontal(left);
        int marginBottom = calculateInsetVertical(bottom);
        int marginRight = calculateInsetHorixontal(right);
        
        return new Insets(marginTop, marginLeft, marginBottom, marginRight);
    }

    private static int calculateInsetVertical(int insetBaseValue)
    {
        double ratioDivisor = getScreenRatioDivisor();

        Dimension screenSize = PWCGMonitorSupport.getPWCGFrameSize();
        Double screenHeight = Double.valueOf(screenSize.height);
        Double screenRatio = screenHeight / ratioDivisor;
        Double insetVertical = insetBaseValue * screenRatio;

        return insetVertical.intValue();
    }

    private static int calculateInsetHorixontal(int insetBaseValue)
    {
        double ratioDivisor = getScreenRatioDivisor();
        
        Dimension screenSize = PWCGMonitorSupport.getPWCGFrameSize();
        Double screenWidth = Double.valueOf(screenSize.width);
        Double screenRatio = screenWidth / ratioDivisor;
        Double insetHorizontal = insetBaseValue * screenRatio;

        return insetHorizontal.intValue();
    }

    private static double getScreenRatioDivisor()
    {
        double ratioDivisor = 900.0;
        Dimension monitorSize = PWCGMonitorSupport.getPWCGMonitorSize();
        
        if (monitorSize.getWidth() > 3000)
        {
            ratioDivisor = 600.0;
        }
        else if (monitorSize.getWidth() > 2000)
        {
            ratioDivisor = 700.0;
        }
        else if (monitorSize.getWidth() > 1700)
        {
            ratioDivisor = 800;
        }
        else if (monitorSize.getWidth() > 1200)
        {
            ratioDivisor = 900;
        }
        else
        {
            ratioDivisor = 1000;
        }
        
        return ratioDivisor;
    }
}
