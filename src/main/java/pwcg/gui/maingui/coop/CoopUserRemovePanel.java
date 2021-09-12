package pwcg.gui.maingui.coop;

import java.awt.BorderLayout;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javax.swing.border.Border;

import pwcg.coop.CoopUserManager;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.MultiSelectData;
import pwcg.gui.utils.MultiSelectGUI;

public class CoopUserRemovePanel extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private MultiSelectGUI userSelector;

    public CoopUserRemovePanel()
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
    }

    public void makePanels()
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
            this.setImageFromName(imagePath);

            Pane centerPanel = makeAcceptancePanel();
            this.add(centerPanel, BorderLayout.CENTER);
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private Pane makeAcceptancePanel() throws PWCGException
    {
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();

        Pane dataEntryPanel = new Pane();
        dataEntryPanel.setLayout(new BorderLayout());
        dataEntryPanel.setOpaque(false);

        userSelector = new MultiSelectGUI();
        Pane removePanel = userSelector.build(3);
        Pane buttonPanel = buildCoopUserButtons(font);

        dataEntryPanel.add(removePanel, BorderLayout.CENTER);
        dataEntryPanel.add(buttonPanel, BorderLayout.SOUTH);

        return dataEntryPanel;
    }

    private Pane buildCoopUserButtons(Font font)
    {
        Button removeUsersButton = new Button("Remove Coop Users");
        removeUsersButton.setActionCommand("Remove Coop Users");
        removeUsersButton.addActionListener(this);
        removeUsersButton.setFont(font);
        removeUsersButton.setForeground(ColorMap.NEWSPAPER_FOREGROUND);
        removeUsersButton.setBackground(ColorMap.NEWSPAPER_BACKGROUND);
 
        Border raisedBorder = BorderFactory.createRaisedBevelBorder();
        removeUsersButton.setBorder(raisedBorder);

        Pane controlPanel = new Pane(new GridLayout(0, 1));
        controlPanel.setOpaque(false);
        controlPanel.add(removeUsersButton);
        return controlPanel;
    }

    public void loadPanels() throws PWCGException
    {
        userSelector.clear();
        for (CoopUser coopUser : CoopUserManager.getIntance().getAllCoopUsers())
        {
            MultiSelectData selectData = buildSelectData(coopUser);
            userSelector.addSelection(selectData);
        }
    }

    private MultiSelectData buildSelectData(CoopUser coopUser)
    {
        MultiSelectData selectData = new MultiSelectData();
        selectData.setName(coopUser.getUsername());
        selectData.setText(coopUser.getUsername());
        selectData.setInfo("User: " + coopUser.getUsername());
        return selectData;
    }

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            if (action.equalsIgnoreCase("Remove Coop Users"))
            {
                removeUsers();
                loadPanels();
            }
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void removeUsers() throws PWCGException
    {
        for (MultiSelectData selectData : userSelector.getSelected())
        {
            CoopUserManager.getIntance().removeCoopUser(selectData.getName());
        }
    }

}
