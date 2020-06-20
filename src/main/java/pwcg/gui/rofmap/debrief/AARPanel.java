package pwcg.gui.rofmap.debrief;

import java.awt.BorderLayout;

import pwcg.gui.PwcgThreePanelUI;
import pwcg.gui.utils.ImageResizingPanel;

public abstract class AARPanel extends PwcgThreePanelUI
{
    private static final long serialVersionUID = 1L;

    public AARPanel ()
    {
        super(ImageResizingPanel.NO_IMAGE);
        this.setLayout(new BorderLayout());
    }
}
