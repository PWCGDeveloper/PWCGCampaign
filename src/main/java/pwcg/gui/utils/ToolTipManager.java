package pwcg.gui.utils;

import javax.swing.JComponent;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;

public class ToolTipManager
{
    static public void setToolTip (JComponent swingComponent, String toolTipText) throws PWCGException
    {
        int useToolTip = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.UseToolTipKey);
        if (useToolTip == 1)
        {
            swingComponent.setToolTipText(toolTipText);
        }
    }
}
