package pwcg.gui;

import javafx.scene.layout.Pane;

import pwcg.core.exception.PWCGException;

public interface IRefreshableParentUI
{
    void refreshInformation() throws PWCGException;

    Pane getScreen();
}
