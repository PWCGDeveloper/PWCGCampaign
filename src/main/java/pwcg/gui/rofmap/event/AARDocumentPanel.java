package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import pwcg.core.utils.PWCGLogger;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.utils.ImageResizingPanel;

public abstract class AARDocumentPanel extends ImageResizingPanel implements IAAREventPanel
{
    private static final long serialVersionUID = 1L;

    protected boolean shouldDisplay = false;
    
    public AARDocumentPanel()
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
            this.setImageFromName(imagePath);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
    }
    
    @Override
    public void finished()
    {
    }

    @Override
    public boolean isShouldDisplay()
    {
        return shouldDisplay;
    }

    @Override
    public JPanel getPanel()
    {
        return this;
    }
}
