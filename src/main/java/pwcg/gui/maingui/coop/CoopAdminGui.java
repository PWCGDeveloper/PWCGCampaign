package pwcg.gui.maingui.coop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class CoopAdminGui extends PwcgGuiContext implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private ButtonGroup buttonGroup = new ButtonGroup();
    private CoopUserAccept coopUserAccept = new CoopUserAccept();
    private CoopPilotAccept coopPilotAccept = new CoopPilotAccept();
    private CoopHostPassword coopHostPassword = new CoopHostPassword();

    public CoopAdminGui()
    {
        super();
    }
    
    public void makePanels() 
    {
        try
        {
        	coopUserAccept.makePanels();
        	coopPilotAccept.makePanels();
        	coopHostPassword.makePanels();
        	
            setRightPanel(makeCoopAdminActionSelectPanel());
            setCenterPanel(makeCenterPanel());
            setLeftPanel(makeNavigatePanel());
        }
        catch (Throwable e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    public JPanel makeCenterPanel()  
    {       
        String imagePath = ContextSpecificImages.imagesMisc() + "Paper.jpg";
        ImageResizingPanel blankPanel = new ImageResizingPanel(imagePath);
        blankPanel.setLayout(new BorderLayout());
                
        return blankPanel;
    }

    public JPanel makeNavigatePanel() throws PWCGException  
    {
        String imagePath = getSideImageMain("ConfigLeft.jpg");

        JPanel navPanel = new ImageResizingPanel(imagePath);
        navPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        JButton acceptButton = PWCGButtonFactory.makeMenuButton("Finished", "Finished", this);
        buttonPanel.add(acceptButton);

        navPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return navPanel;
    }

    public JPanel makeCoopAdminActionSelectPanel() throws PWCGException  
    {
        String imagePath = getSideImageMain("ConfigRight.jpg");

        JPanel configPanel = new ImageResizingPanel(imagePath);
        configPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        

        JLabel label = PWCGButtonFactory.makeMenuLabelLarge("Select Coop Admin Action:");
        buttonPanel.add(label);

        JLabel spacer = PWCGButtonFactory.makeMenuLabelLarge("   ");
        buttonPanel.add(spacer);

        buttonPanel.add(makeActionSelectRadioButton("Change Host Password"));
        buttonPanel.add(makeActionSelectRadioButton("Administer Coop Users"));
        buttonPanel.add(makeActionSelectRadioButton("Administer Coop Pilots"));
        
        add (buttonPanel);

        configPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return configPanel;
    }

    private JRadioButton makeActionSelectRadioButton(String buttonText) throws PWCGException 
    {
        Color fgColor = ColorMap.CHALK_FOREGROUND;

        Font font = MonitorSupport.getPrimaryFont();

        JRadioButton button = new JRadioButton(buttonText);
        button.setActionCommand(buttonText);
        button.setHorizontalAlignment(SwingConstants.LEFT );
        button.setBorderPainted(false);
        button.addActionListener(this);
        button.setOpaque(false);
        button.setForeground(fgColor);
        button.setFont(font);

        buttonGroup.add(button);

        return button;
    }

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            if (action.equalsIgnoreCase("Finished"))
            {
            	coopUserAccept.writeResults();
            	coopPilotAccept.writeResults();
            	coopHostPassword.writeResults();

                CampaignGuiContextManager.getInstance().popFromContextStack();
                return;
            }
            else if (action.equalsIgnoreCase("Change Host Password"))
            {
                CampaignGuiContextManager.getInstance().changeCurrentContext(null, coopHostPassword, null);
            }
            else if (action.contains("Administer Coop Users"))
            {
                CampaignGuiContextManager.getInstance().changeCurrentContext(null, coopUserAccept, null);
            }
            else if (action.contains("Administer Coop Pilots"))
            {
                CampaignGuiContextManager.getInstance().changeCurrentContext(null, coopPilotAccept, null);
            }
        }
        catch (Throwable e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
}


