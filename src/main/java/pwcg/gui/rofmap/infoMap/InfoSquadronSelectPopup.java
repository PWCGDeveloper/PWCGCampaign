package pwcg.gui.rofmap.infoMap;

import java.util.Date;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;

public class InfoSquadronSelectPopup extends JPopupMenu
{
    private static final long serialVersionUID = 1L;

    public InfoSquadronSelectPopup(InfoMapPanel parent, List<Company> squadronsAtBase, Date date)
    {
        try
        {
            for (Company squadron : squadronsAtBase)
            {
                String squadronName = squadron.determineDisplayName(date);
                JMenuItem squadronMenuItem = new JMenuItem("Select Squadron:"+squadron.getCompanyId()+":"+squadronName);
                squadronMenuItem.addActionListener(parent);
                add(squadronMenuItem);
            }
            
            JMenuItem cancelWpItem = new JMenuItem("Cancel");
            add(cancelWpItem);
            cancelWpItem.addActionListener(parent);     
        }
        catch (PWCGException e)
        {
            e.printStackTrace();
        }
    }

}
