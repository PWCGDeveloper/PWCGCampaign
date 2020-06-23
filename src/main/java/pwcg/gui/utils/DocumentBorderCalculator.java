package pwcg.gui.utils;

import pwcg.gui.dialogs.PWCGMonitorSupport;

public class DocumentBorderCalculator
{
    public static int calculateTopBorder()
    {
        int documentTopBorder = 250;
        if (PWCGMonitorSupport.isVerySmallScreen())
        {
            documentTopBorder = 100;  
        }
        else if (PWCGMonitorSupport.isSmallScreen())
        {
            documentTopBorder = 150;  
        }
        else if (PWCGMonitorSupport.isMediumScreen())
        {
            documentTopBorder = 200;
        }
        return documentTopBorder;
    }
}
