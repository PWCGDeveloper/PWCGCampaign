package pwcg.gui.campaign.intel;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import pwcg.core.exception.PWCGException;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;

public class CampaignIntelligenceControlPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private CampaignIntelligenceReportScreen parent;
    private ButtonGroup buttonGroup = new ButtonGroup();

    public CampaignIntelligenceControlPanel(CampaignIntelligenceReportScreen parent)
    {
        this.parent = parent;
    }

	public void makeIntelNavPanel() throws PWCGException
    {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        JPanel navPanel = makeNavigatePanel();
        this.add(navPanel, BorderLayout.NORTH);

        JPanel selectorPanel = makeSelectorPanel();
        this.add(selectorPanel, BorderLayout.CENTER);        
    }

    private JPanel makeNavigatePanel() throws PWCGException  
    {       
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        JButton finished = PWCGButtonFactory.makeTranslucentMenuButton("Finished Reading", "IntelFinished", "Leave Intel", parent);
        buttonPanel.add(finished);
        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        
        navPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return navPanel;
    }

    private JPanel makeSelectorPanel() throws PWCGException  
    {       
        JPanel selectorPanel = new JPanel(new BorderLayout());
        selectorPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,3));
        buttonPanel.setOpaque(false);
        
        JRadioButton friendly = PWCGButtonFactory.makeRadioButton("Friendly", "Friendly", "Show Friendly Companies", null, ColorMap.CHALK_FOREGROUND, true, parent);       
        buttonGroup.add(friendly);

        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        buttonPanel.add(friendly);
        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        
        JRadioButton enemy = PWCGButtonFactory.makeRadioButton("Enemy", "Enemy", "Show Enemy Companies", null, ColorMap.CHALK_FOREGROUND, false, parent);       
        buttonGroup.add(enemy);

        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        buttonPanel.add(enemy);
        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());

        selectorPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return selectorPanel;
    }
}
