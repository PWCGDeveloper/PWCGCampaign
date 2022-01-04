package pwcg.gui.colors;

import java.awt.Color;

import pwcg.campaign.tank.PwcgRoleCategory;

public class RAFColorMap extends ServiceColor implements IServiceColorMap
{
    public static final Color BOMBER_COLOR = new Color(100, 26, 9);
    public static final Color RECON_COLOR = new Color(191, 26, 9);
    public static final Color FIGHTER_COLOR = new Color(223, 26, 9);
    
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
