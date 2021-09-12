package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javafx.scene.layout.Pane;

import pwcg.aar.ui.events.model.MedalEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.medals.MedalManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.utils.ContextSpecificImages;

public class CampaignReportMedalGUI extends AARDocumentIconPanel
{
	private static final long serialVersionUID = 1L;
	private Campaign campaign;
    private MedalEvent medalEvent = null;
    private SquadronMember medalRecipient;

    public CampaignReportMedalGUI(Campaign campaign, MedalEvent medalEvent) throws PWCGException
    {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.medalEvent = medalEvent;
        this.medalRecipient = campaign.getPersonnelManager().getAnyCampaignMember(medalEvent.getPilotSerialNumber());
            
        makePanel();        
    }

    protected String getHeaderText() throws PWCGException
    {
        String header = "Award For Valor";
        return header;
    }

    protected String getBodyText() throws PWCGException
    {
        String medalText = "Squadron: " + medalEvent.getSquadronName() + "\n";
        medalText += "Date: " + DateUtils.getDateStringPretty(medalEvent.getDate()) + "\n";
        medalText += medalRecipient.getNameAndRank() + " has been awarded the " + medalEvent.getMedal() + ".\n";               

        return medalText;
    }

    protected String getFooterImagePath() throws PWCGException
    {
        Pane medalImagePanel = new Pane(new GridLayout(0, 1));
        medalImagePanel.setOpaque(false);

        ICountry country = CountryFactory.makeCountryByCountry(medalRecipient.getCountry());
        Medal medal =  MedalManager.getMedalFromAnyManager(country, campaign, medalEvent.getMedal());
        if (medal != null)
        {
            return createMedalImagePath(medal);
        }
        else
        {
            return "";
        }
    }

    private String createMedalImagePath(Medal medal)
    {
        SquadronMember pilot = medalRecipient;
        
        String medalPath = ContextSpecificImages.imagesMedals();
        ICountry country = CountryFactory.makeCountryByCountry(pilot.getCountry());
        if (country.getSide() == Side.ALLIED)
        {
            medalPath += "Allied\\";
        }
        else
        {
            medalPath += "Axis\\";
        }

        medalPath += medal.getMedalImage();
        return medalPath;
    }

    @Override
    public void finished()
    {
    }

    @Override
    public boolean isShouldDisplay()
    {
        return shouldDisplay;
    }

    @Override
    public Pane getPanel()
    {
        return this;
    }
}
