package pwcg.gui.rofmap.debrief;

import java.awt.BorderLayout;

import pwcg.gui.PwcgGuiContext;

public abstract class AARPanel extends PwcgGuiContext
{
    private static final long serialVersionUID = 1L;

    public AARPanel ()
    {
        super();
        this.setLayout(new BorderLayout());
    }
}
