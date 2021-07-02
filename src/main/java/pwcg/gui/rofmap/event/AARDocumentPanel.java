package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public abstract class AARDocumentPanel extends JPanel implements IAAREventPanel
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
    public JPanel getPanel()
    {
        return this;
    }
}
