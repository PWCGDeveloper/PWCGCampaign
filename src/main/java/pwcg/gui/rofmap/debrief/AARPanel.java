package pwcg.gui.rofmap.debrief;

import java.awt.BorderLayout;

import pwcg.aar.AARCoordinator;
import pwcg.aar.data.AARContext;
import pwcg.gui.PwcgGuiContext;

public abstract class AARPanel extends PwcgGuiContext
{
    private static final long serialVersionUID = 1L;

    protected AARContext aarContext;

    public AARPanel ()
    {
        super();
        aarContext = AARCoordinator.getInstance().getAarContext();

        this.setLayout(new BorderLayout());
    }
}
