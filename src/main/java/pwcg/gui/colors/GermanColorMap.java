package pwcg.gui.colors;

import java.awt.Color;

import pwcg.campaign.tank.PwcgRoleCategory;

public class GermanColorMap extends ServiceColor implements IServiceColorMap
{
    public static final Color BOMBER_COLOR = new Color(0, 0, 0);
    public static final Color RECON_COLOR = new Color(50, 50, 50);
    public static final Color FIGHTER_COLOR = new Color(80, 80, 80);
    
    @Override
    public Color getColorForRole(PwcgRoleCategory roleCategory)
    {
        if (roleCategory == PwcgRoleCategory.BOMBER)
        {
            return BOMBER_COLOR;
        }
        if (roleCategory == PwcgRoleCategory.FIGHTER)
        {
            return FIGHTER_COLOR;            
        }
        
        return RECON_COLOR;
    }
}
