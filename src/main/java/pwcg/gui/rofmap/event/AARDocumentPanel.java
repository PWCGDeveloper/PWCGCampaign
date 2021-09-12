package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;

import javafx.scene.layout.Pane;

public abstract class AARDocumentPanel extends Pane implements IAAREventPanel
{
    private static final long serialVersionUID = 1L;

    protected boolean shouldDisplay = false;
    
    public AARDocumentPanel()
    {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
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
    public Pane getPanel()
    {
        return this;
    }
}
