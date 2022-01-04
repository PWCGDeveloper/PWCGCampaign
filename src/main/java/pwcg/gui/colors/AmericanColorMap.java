package pwcg.gui.colors;

import java.awt.Color;

import pwcg.campaign.tank.PwcgRoleCategory;

public class AmericanColorMap extends ServiceColor implements IServiceColorMap
{
    public static final Color RECON_COLOR = new Color(50, 60, 80);
    public static final Color FIGHTER_COLOR = new Color(80, 95, 130);

    @Override
    public Color getColorForRole(PwcgRoleCategory roleCategory)
    {
        if (roleCategory == PwcgRoleCategory.FIGHTER)
        {
            return FIGHTER_COLOR;            
        }
        
        return RECON_COLOR;
    }
}
