package pwcg.gui.rofmap.event;

import javafx.scene.layout.Pane;

import pwcg.core.exception.PWCGException;

public interface IAAREventPanel
{
    boolean isShouldDisplay();
    void makePanel() throws PWCGException;
    void finished();
    Pane getPanel();
}
