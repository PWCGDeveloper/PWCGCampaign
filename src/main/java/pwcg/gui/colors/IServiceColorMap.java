package pwcg.gui.colors;

import java.awt.Color;
import java.util.Date;

import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public interface IServiceColorMap
{
    Color getColorForRole(PwcgRole role);

    public Color getColorForSquadron(Squadron squadron, Date date) throws PWCGException;
}