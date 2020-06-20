package pwcg.gui;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;

public class CampaignGuiContextManager
{
    private static CampaignGuiContextManager instance = new CampaignGuiContextManager();
    
    private List<PwcgThreePanelUI> contextStack = new ArrayList<>();
    private PwcgGuiCurrentPanel currentPanel = new PwcgGuiCurrentPanel();

    private CampaignGuiContextManager()
    {
    }

    public static CampaignGuiContextManager getInstance()
    {
        return instance;
    }

    public void clearContextStack()
    {
        contextStack.clear();
    }

    public void refreshCurrentContext(PwcgThreePanelUI context) throws PWCGException
    {
        popFromContextStack();
        pushToContextStack(context);
    }

    public void pushToContextStack(PwcgThreePanelUI context) throws PWCGException
    {
        contextStack.add(context);
        displayCurrentContext();
    }

    public void popFromContextStack() throws PWCGException
    {
        if (contextStack.size() > 1)
        {
            int index = contextStack.size() - 1;
            contextStack.remove(index);
            displayCurrentContext();
        }
    }

    private void displayCurrentContext() throws PWCGException
    {
        if (contextStack.size() > 0)
        {
            int index = contextStack.size() - 1;
            PwcgThreePanelUI context = contextStack.get(index);
            
            currentPanel.displayContext(context);
        }
    }
}
