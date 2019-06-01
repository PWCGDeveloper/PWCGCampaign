package pwcg.gui.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.dialogs.ErrorDialog;

public class SelectorGUI implements ActionListener
{

    private MultiSelectGUI accepted;
    private MultiSelectGUI notAccepted;
    private ISelectorGUICallback selectCallback;

	public SelectorGUI()
	{
	    super();
	}
	
	public void registerCallback(ISelectorGUICallback selectCallback)
	{
		this.selectCallback = selectCallback;
	}
	
	public JPanel build(boolean allowReject) throws PWCGException 
	{
        JPanel selectPanel = new JPanel(new BorderLayout());
        selectPanel.setOpaque(false);

        notAccepted = new MultiSelectGUI();
        JPanel notAcceptedPanel = notAccepted.build();
        selectPanel.add(notAcceptedPanel, BorderLayout.WEST);
        
        JPanel buttonPanel = makeButtonPanel(allowReject);
        selectPanel.add(buttonPanel, BorderLayout.CENTER);

        accepted = new MultiSelectGUI();
        JPanel acceptedPanel = accepted.build();
        selectPanel.add(acceptedPanel, BorderLayout.EAST);
		
		return selectPanel;
	}
	
    
    private JPanel makeButtonPanel(boolean allowReject) throws PWCGException
    {
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        buttonPanel.setOpaque(false);

        JLabel spacerLabel = PWCGButtonFactory.makePaperLabelMedium("");        
        buttonPanel.add(spacerLabel);

        spacerLabel = PWCGButtonFactory.makePaperLabelMedium("");        
        buttonPanel.add(spacerLabel);

        spacerLabel = PWCGButtonFactory.makePaperLabelMedium("");        
        buttonPanel.add(spacerLabel);

        JButton acceptButton = PWCGButtonFactory.makePaperButtonWithBorder("Accept ===>", "Accept", this);
        acceptButton.setHorizontalAlignment(SwingConstants.CENTER);
        buttonPanel.add(acceptButton);

        spacerLabel = PWCGButtonFactory.makePaperLabelMedium("");        
        buttonPanel.add(spacerLabel);

        if (allowReject)
        {
            JButton rejectButton = PWCGButtonFactory.makePaperButtonWithBorder("<=== Reject", "Reject", this);
            rejectButton.setHorizontalAlignment(SwingConstants.CENTER);
            buttonPanel.add(rejectButton);
        }
        
        spacerLabel = PWCGButtonFactory.makePaperLabelMedium("");        
        buttonPanel.add(spacerLabel);

        spacerLabel = PWCGButtonFactory.makePaperLabelMedium("");        
        buttonPanel.add(spacerLabel);

        spacerLabel = PWCGButtonFactory.makePaperLabelMedium("");        
        buttonPanel.add(spacerLabel);
        
        JPanel buttonGridContainer = new JPanel(new BorderLayout());
        buttonGridContainer.setOpaque(false);
        buttonGridContainer.add(buttonPanel, BorderLayout.NORTH);
        buttonGridContainer.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.GRAY, Color.BLACK));

        return buttonGridContainer;
    }
    
    public List<MultiSelectData> getAccepted()
    {
        return accepted.getAll();
    }
    
    public List<MultiSelectData> getNotAccepted()
    {
        return notAccepted.getAll();
    }
    
    public void addAccepted(MultiSelectData selectData) throws PWCGException
    {
        accepted.addSelection(selectData);
    }
    
    public void addNotAccepted(MultiSelectData selectData) throws PWCGException
    {
        notAccepted.addSelection(selectData);
    }

    public void setAcceptedItemOK(String key)
    {
    	accepted.setTextColor(key, Color.BLACK);
    }

    public void setAcceptedItemWarn(String key)
    {
    	accepted.setTextColor(key, Color.RED);
    }

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            if (action.equalsIgnoreCase("Accept"))
            {
                List<MultiSelectData> acceptedItems = notAccepted.getSelected();
                for (MultiSelectData acceptedItem : acceptedItems)
                {
                    accepted.addSelection(acceptedItem);
                    notAccepted.removeSelection(acceptedItem.getName());
                    invokeCallback();
                }
            }
            else if (action.equalsIgnoreCase("Reject"))
            {
                List<MultiSelectData> rejectedItems = accepted.getSelected();
                for (MultiSelectData rejectedItem : rejectedItems)
                {
                    notAccepted.addSelection(rejectedItem);
                    accepted.removeSelection(rejectedItem.getName());
                    invokeCallback();
                }
            }
        }
        catch (Throwable e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

	private void invokeCallback() 
	{
		if (selectCallback != null)
		{
			selectCallback.onSelectCallback();
		}
	}
}
