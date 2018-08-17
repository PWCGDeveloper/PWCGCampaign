package pwcg.gui.campaign;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.campaign.home.CampaignPilotChalkBoard;
import pwcg.gui.campaign.pilot.CampaignPilotListPanel;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;

public class CampaignRosterSquadronPanelFactory extends CampaignRosterBasePanelFactory
{
	public CampaignRosterSquadronPanelFactory(ActionListener parent) throws PWCGException  
	{
		super(parent);
	}

	public void makeCampaignHomePanels() throws PWCGException  
	{
		createChalkBoard();
		
		createPilotListPanel();
	}

    private void createChalkBoard()
    {
		CampaignPilotChalkBoard chalkboard = new CampaignPilotChalkBoard();
		chalkboard.makeSquadronPanel(sortedPilots);
		chalkboardPanel = chalkboard;
    }

    private void createPilotListPanel() throws PWCGException
    {
        CampaignPilotListPanel pilotList = new CampaignPilotListPanel(parent);
		pilotListPanel = pilotList.makeSquadronRightPanel(sortedPilots, "  Roster", "CampFlowPilot:");
		
		JPanel descPanel = makeDescPanel();
		pilotListPanel.add(descPanel, BorderLayout.CENTER);
    }

    public void makePilotList() throws PWCGException 
    {
        SquadronMembers pilots = new SquadronMembers();
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(campaign.getPersonnelManager().getPlayerPersonnel().getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember pilot : squadronMembers.getSquadronMemberList())
        {
            if (excludeAces && pilot instanceof Ace)
            {
                continue;
            }
            pilots.addToSquadronMemberCollection(pilot);
        }
        
        sortedPilots = pilots.sortPilots(campaign.getDate());
    }

	private JPanel makeDescPanel() throws PWCGException 
	{
        String imagePath = ContextSpecificImages.imagesMisc() + "Plaque.jpg";
        ImageResizingPanel descPanel = new ImageResizingPanel(imagePath);
        descPanel.setLayout(new BorderLayout());

		JPanel descGridPanel = new JPanel(new GridLayout(0, 1));
		descGridPanel.setOpaque(false);
		
		Font font = MonitorSupport.getPrimaryFont();
		
		
		Color fg = ColorMap.PLAQUE_GOLD;
		
		String spacing = "         ";
		
        
		JLabel lDummy1 = new JLabel("", JLabel.LEFT);
		lDummy1.setOpaque(false);
		descGridPanel.add(lDummy1);
      
		JLabel lDummy2 = new JLabel("", JLabel.LEFT);
		lDummy2.setOpaque(false);
        descGridPanel.add(lDummy2);

        Squadron squad =  campaign.determineSquadron();
        String squadString = spacing + "Assigned to " + squad.determineDisplayName(campaign.getDate());
        JLabel lSquad = new JLabel(squadString, JLabel.LEFT);
        lSquad.setFont(font);
        lSquad.setForeground(fg);
        descGridPanel.add(lSquad);
        
        String airfieldAtString = spacing + "Stationed at ";
        JLabel lAirfieldAt = new JLabel(airfieldAtString, JLabel.LEFT);
        lAirfieldAt.setFont(font);
        lAirfieldAt.setForeground(fg);
        descGridPanel.add(lAirfieldAt);
        
        String airfieldString = spacing + campaign.getAirfieldName();
        JLabel lAirfield = new JLabel(airfieldString, JLabel.LEFT);
        lAirfield.setFont(font);
        lAirfield.setForeground(fg);
        descGridPanel.add(lAirfield);

		JLabel lDate = new JLabel(spacing + DateUtils.getDateString(campaign.getDate()), JLabel.LEFT);
		lDate.setFont(font);
		lDate.setForeground(fg);
		descGridPanel.add(lDate);
		
		PlaneType aircraftType = squad.determineBestPlane(campaign.getDate());
		if (aircraftType != null)
		{
			String aircraftString = "Flying the " + aircraftType.getDisplayName();
			JLabel lAircraft = new JLabel(spacing + aircraftString, JLabel.LEFT);
			lAircraft.setFont(font);
			lAircraft.setForeground(fg);
			descGridPanel.add(lAircraft);
		}
		
		JLabel lDummy3 = new JLabel("", JLabel.LEFT);
		lDummy3.setOpaque(false);
		descGridPanel.add(lDummy3);
		
		JLabel lDummy4 = new JLabel("", JLabel.LEFT);
		lDummy4.setOpaque(false);
		descGridPanel.add(lDummy4);
		
		descPanel.add(descGridPanel, BorderLayout.NORTH);
		
		return descPanel;
	}
}
