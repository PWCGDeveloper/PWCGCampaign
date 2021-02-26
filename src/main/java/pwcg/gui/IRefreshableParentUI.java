package pwcg.gui;

import javax.swing.JPanel;

import pwcg.core.exception.PWCGException;

public interface IRefreshableParentUI
{
    void refreshInformation() throws PWCGException;

    JPanel getScreen();
}
