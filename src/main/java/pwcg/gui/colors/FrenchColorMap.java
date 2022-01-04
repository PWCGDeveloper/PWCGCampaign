package pwcg.gui.colors;

import java.awt.Color;

import pwcg.campaign.tank.PwcgRoleCategory;

public class FrenchColorMap extends ServiceColor implements IServiceColorMap
{
    public static final Color BOMBER_COLOR = new Color(24, 32, 80);
    public static final Color RECON_COLOR = new Color(45, 59, 150);
    public static final Color FIGHTER_COLOR = new Color(45, 59, 220);
    
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
