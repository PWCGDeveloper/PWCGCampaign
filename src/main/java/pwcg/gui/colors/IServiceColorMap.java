package pwcg.gui.colors;

import javafx.scene.paint.Color;
import java.util.Date;

import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public interface IServiceColorMap
{
    Color getColorForRole(Role role);

    public Color getColorForSquadron(Squadron squadron, Date date) throws PWCGException;
}