package pwcg.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.PWCGFrame;

public class CampaignGuiContextManager
{
    private static CampaignGuiContextManager instance = new CampaignGuiContextManager();
    
    private List<PwcgGuiContext> contextStack = new ArrayList<PwcgGuiContext>();

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

    public PwcgGuiContext getCurrentContext() throws PWCGException 
    {
        if (contextStack.size() == 0)
        {
            throw new PWCGException ("No cpntext available");
        }
        
        int index = contextStack.size() - 1;
        return contextStack.get(index);
    }

    public void pushToContextStack(PwcgGuiContext context) throws PWCGException
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
            PwcgGuiContext context = contextStack.get(index);
            
            displayContext(context);
        }
    }

    private void displayContext(PwcgGuiContext context) 
    {
        try
        {            
            PWCGFrame.getInstance().setPanel(context);
            
            if (context.getLeftPanel() != null)
            {
                context.setLeftPanel(context.getLeftPanel());
            }
            if (context.getCenterPanel() != null)
            {
                context.setCenterPanel(context.getCenterPanel());
            }
            if (context.getRightPanel() != null)
            {
                context.setRightPanel(context.getRightPanel());
            }
        }
        catch (Exception e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    /**
     * This allows a context to change some panels without adding a new context to the stack.
     * This is desirable when displaying things like page turning which would entail a newly rendered
     * center panel within the same context (Pilot Log for example)
     * 
     * @throws PWCGException 
     * 
     */
    public void changeCurrentContext(JPanel leftPanel, JPanel centerPanel, JPanel rightPanel) throws PWCGException  
    {
        PwcgGuiContext context = getCurrentContext();
        if (leftPanel != null)
        {
            context.setLeftPanel(leftPanel);
        }
        
        if (centerPanel != null)
        {
            context.setCenterPanel(centerPanel);
        }
        
        if (rightPanel != null)
        {
            context.setRightPanel(rightPanel);
        }
    }
}
