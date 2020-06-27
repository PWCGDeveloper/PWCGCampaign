package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.debrief.IAAREventPanel;
import pwcg.gui.utils.CampaignDocumentPage;
import pwcg.gui.utils.ImageResizingPanel;

public abstract class CampaignDocumentGUI extends ImageResizingPanel implements IAAREventPanel
{
    private static final long serialVersionUID = 1L;
    protected boolean shouldDisplay = false;

    public CampaignDocumentGUI()
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.shouldDisplay = true;
    }

    public void makePanel() 
    {
        try
        {
            String imagePath = UiImageResolver.getImageMain("document.png");
            this.setImage(imagePath);

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
