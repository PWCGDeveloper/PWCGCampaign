package pwcg.gui;

import javax.swing.JPanel;

import pwcg.gui.utils.ImageResizingPanel;

public abstract class PwcgThreePanelUI extends ImageResizingPanel
{
    public PwcgThreePanelUI(String imagePath)
    {
        super(imagePath);
    }

    protected static final long serialVersionUID = 1L;

    private JPanel leftPanel = null;
    private JPanel rightPanel = null;
    private JPanel centerPanel = null;

    public JPanel getLeftPanel()
    {
        return leftPanel;
    }

    public void setLeftPanel(JPanel leftPanel)
    {
        this.leftPanel = leftPanel;
    }

    public JPanel getRightPanel()
    {
        return rightPanel;
    }

    public void setRightPanel(JPanel rightPanel)
    {
        this.rightPanel = rightPanel;
    }

    public JPanel getCenterPanel()
    {
        return centerPanel;
    }

    public void setCenterPanel(JPanel centerPanel)
    {
        this.centerPanel = centerPanel;
    }
}
