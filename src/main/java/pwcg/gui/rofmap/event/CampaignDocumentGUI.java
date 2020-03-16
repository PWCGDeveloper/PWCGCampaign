package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.debrief.AAREventPanel;
import pwcg.gui.utils.CampaignDocumentPage;
import pwcg.gui.utils.ContextSpecificImages;

public abstract class CampaignDocumentGUI extends AAREventPanel
{
    private static final long serialVersionUID = 1L;

    public CampaignDocumentGUI()
    {
        super();
        setLayout(new BorderLayout());
        this.shouldDisplay = true;
    }

    public void makePanel() 
    {
        try
        {
            String imagePath = ContextSpecificImages.imagesMisc() + "PaperFull.jpg";
            setImage(imagePath);
            
            String headerText = getHeaderText();
            String bodyText = getBodyText();

            CampaignDocumentPage campaignDocumentPage = new CampaignDocumentPage();
            campaignDocumentPage.formDocument(headerText, bodyText);
            
            this.add(campaignDocumentPage, BorderLayout.CENTER);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }       
    }

    protected abstract String getHeaderText() throws PWCGException;

    protected abstract String getBodyText() throws PWCGException;


    @Override
    public void finished()
    {
    }
}
