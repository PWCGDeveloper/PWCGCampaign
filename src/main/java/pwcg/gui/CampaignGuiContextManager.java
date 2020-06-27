package pwcg.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import pwcg.core.exception.PWCGException;
import pwcg.gui.campaign.home.CampaignHome;
import pwcg.gui.maingui.CampaignMainGUI;
import pwcg.gui.utils.PWCGFrame;

public class CampaignGuiContextManager
{
    private static CampaignGuiContextManager instance = new CampaignGuiContextManager();
    
    private List<JPanel> contextStack = new ArrayList<>();

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

    public void refreshCurrentContext(JPanel context) throws PWCGException
    {
        displayCurrentContext();
    }

    public void pushToContextStack(JPanel context) throws PWCGException
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
            JPanel context = contextStack.get(index);
            PWCGFrame.getInstance().setPanel(context);
        }
    }

    public void backToCampaignHome() throws PWCGException
    {
        int index = contextStack.size() - 1;
        JPanel context = contextStack.get(index);
        while (!(context instanceof CampaignHome) && index > 1)
        {
            popFromContextStack();
            index = contextStack.size() - 1;
            context = contextStack.get(index);
        }        
    }

    public void backToMain() throws PWCGException
    {
        int index = contextStack.size() - 1;
        JPanel context = contextStack.get(index);
        while (!(context instanceof CampaignMainGUI) && index > 0)
        {
            popFromContextStack();
            index = contextStack.size() - 1;
            context = contextStack.get(index);
        }        
    }
}
