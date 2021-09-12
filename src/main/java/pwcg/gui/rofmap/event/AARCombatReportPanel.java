package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javafx.scene.control.Button;
import javax.swing.JOptionPane;
import javafx.scene.layout.Pane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pwcg.aar.AARCoordinator;
import pwcg.campaign.Campaign;
import pwcg.campaign.CombatReport;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.display.model.CombatReportBuilder;
import pwcg.gui.utils.ButtonFactory;

public class AARCombatReportPanel extends AARDocumentPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private AARCoordinator aarCoordinator;
    private CombatReportPanel combatReportPanel = null;
    private boolean shouldDisplay = false;
    private CombatReport combatReport;

	public AARCombatReportPanel()
	{
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.shouldDisplay = true;
        this.aarCoordinator = AARCoordinator.getInstance();
		this.campaign = PWCGContext.getInstance().getCampaign();
	}

	public void makePanel()  
	{
        try
        {
            buildCombatReport();
            createCombatReportGUI();
            this.add(combatReportPanel, BorderLayout.CENTER);
            
            Pane narrativeButton = createNarrativeButton();
            this.add(narrativeButton, BorderLayout.SOUTH);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void buildCombatReport() throws PWCGException 
    {                
        SquadronMember referencePlayer = campaign.findReferencePlayer();
        CombatReportBuilder combatReportBuilder = new CombatReportBuilder(campaign, referencePlayer, aarCoordinator);
        combatReport = combatReportBuilder.createCombatReport();
    }

    private void createCombatReportGUI() throws PWCGException
    {
        combatReportPanel = new CombatReportPanel (combatReport);
        combatReportPanel.makePanel();
    }

    private Pane createNarrativeButton() throws PWCGException
    {
        Pane narrativeButtonPanel = new Pane();
        narrativeButtonPanel.setLayout(new BorderLayout());
        narrativeButtonPanel.setOpaque(false);
        
        Button addNarrativeButton = ButtonFactory.makeTranslucentMenuButton("Edit Narrative", "Add Narrative", "Add narrative to combat report", this);
        narrativeButtonPanel.add(addNarrativeButton, BorderLayout.SOUTH);
        return narrativeButtonPanel;
    }

    @Override
	public void finished() 
	{
		try
		{
	        Campaign campaign = PWCGContext.getInstance().getCampaign();
			combatReportPanel.writeCombatReport(campaign);
		}
		catch (Exception e)
		{	
			PWCGLogger.logException(e);
		}
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

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        try
        {
            JTextArea narrativeText = new JTextArea(20, 20);
            narrativeText.setBackground(ColorMap.NEWSPAPER_BACKGROUND);
            narrativeText.setForeground(ColorMap.NEWSPAPER_FOREGROUND);            
            narrativeText.setText(combatReport.getNarrative());
            narrativeText.setLineWrap(true);
            
            switch (JOptionPane.showConfirmDialog(
                    null, 
                    new JScrollPane(narrativeText),
                    "Narrative",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE)) 
            {
                case JOptionPane.OK_OPTION:
                    this.remove(combatReportPanel);
                    combatReport.setNarrative(narrativeText.getText());
                    createCombatReportGUI();
                    this.add(combatReportPanel, BorderLayout.CENTER);
                    this.revalidate();
                    this.repaint();
                    break;
            }
        }
        catch (Exception exp)
        {
            PWCGLogger.logException(exp);
        }
    }
}
