package pwcg.gui.rofmap.debrief;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public abstract class AARPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    public AARPanel ()
    {
        super();
        this.setLayout(new BorderLayout());
    }
}
