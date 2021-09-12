package pwcg.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.Pane;

import pwcg.core.exception.PWCGException;
import pwcg.gui.campaign.home.CampaignHomeScreen;
import pwcg.gui.maingui.PwcgMainScreen;
import pwcg.gui.utils.PWCGFrame;

public class CampaignGuiContextManager
{
    private static CampaignGuiContextManager instance = new CampaignGuiContextManager();
    
    private List<Pane> contextStack = new ArrayList<>();

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

    public void refreshCurrentContext(Pane context) throws PWCGException
    {
        displayCurrentContext();
    }

    public void pushToContextStack(Pane context) throws PWCGException
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
            Pane context = contextStack.get(index);
            PWCGFrame.getInstance().setPanel(context);
        }
    }

    public void backToCampaignHome() throws PWCGException
    {
        int index = contextStack.size() - 1;
        Pane context = contextStack.get(index);
        while (!(context instanceof CampaignHomeScreen) && index > 1)
        {
            popFromContextStack();
            index = contextStack.size() - 1;
            context = contextStack.get(index);
        }        
    }

    public void backToMain() throws PWCGException
    {
        int index = contextStack.size() - 1;
        Pane context = contextStack.get(index);
        while (!(context instanceof PwcgMainScreen) && index > 0)
        {
            popFromContextStack();
            index = contextStack.size() - 1;
            context = contextStack.get(index);
        }        
    }
}
