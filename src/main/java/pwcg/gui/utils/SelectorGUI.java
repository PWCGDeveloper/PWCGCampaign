package pwcg.gui.utils;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
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
	
	public Pane build(boolean allowReject) throws PWCGException 
	{
        Pane selectPanel = new Pane(new BorderLayout());
        selectPanel.setOpaque(false);

        notAccepted = new MultiSelectGUI();
        Pane notAcceptedPanel = notAccepted.build(1);
        selectPanel.add(notAcceptedPanel, BorderLayout.WEST);
        
        Pane buttonPanel = makeButtonPanel(allowReject);
        selectPanel.add(buttonPanel, BorderLayout.CENTER);

        accepted = new MultiSelectGUI();
        Pane acceptedPanel = accepted.build(1);
        selectPanel.add(acceptedPanel, BorderLayout.EAST);
		
		return selectPanel;
	}
	
    
    private Pane makeButtonPanel(boolean allowReject) throws PWCGException
    {
        Pane buttonPanel = new Pane(new GridLayout(0, 1));
        buttonPanel.setOpaque(false);

        Label spacerLabel = ButtonFactory.makePaperLabelMedium("");        
        buttonPanel.add(spacerLabel);

        spacerLabel = ButtonFactory.makePaperLabelMedium("");        
        buttonPanel.add(spacerLabel);

        spacerLabel = ButtonFactory.makePaperLabelMedium("");        
        buttonPanel.add(spacerLabel);

        Button acceptButton = ButtonFactory.makePaperButtonWithBorder("Accept ===>", "Accept", this);
        acceptButton.setOpaque(false);
        acceptButton.setAlignment(Pos.CENTER_LEFT);
        buttonPanel.add(acceptButton);

        spacerLabel = ButtonFactory.makePaperLabelMedium("");        
        buttonPanel.add(spacerLabel);

        if (allowReject)
        {
            Button rejectButton = ButtonFactory.makePaperButtonWithBorder("<=== Reject", "Reject", this);
            rejectButton.setOpaque(false);
            rejectButton.setAlignment(Pos.CENTER_LEFT);
            buttonPanel.add(rejectButton);
        }
        
        spacerLabel = ButtonFactory.makePaperLabelMedium("");        
        buttonPanel.add(spacerLabel);

        spacerLabel = ButtonFactory.makePaperLabelMedium("");        
        buttonPanel.add(spacerLabel);

        spacerLabel = ButtonFactory.makePaperLabelMedium("");        
        buttonPanel.add(spacerLabel);
        
        Pane buttonGridContainer = new Pane(new BorderLayout());
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
            PWCGLogger.logException(e);
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
