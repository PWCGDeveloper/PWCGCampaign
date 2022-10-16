package pwcg.gui.campaign.intel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneSorter;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.InternationalizationManager;
import pwcg.core.exception.PWCGException;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageToDisplaySizer;
import pwcg.gui.utils.PwcgBorderFactory;

public class CampaignIntelligenceSquadronDetailsPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private JTextArea squadronIntelHeaderText;
    private JTextArea squadronIntelPersonnelText;
    private JTextArea squadronIntelEquipmentText;

    public CampaignIntelligenceSquadronDetailsPanel(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public void makePanel() throws PWCGException
    {
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.setBorder(PwcgBorderFactory.createStandardDocumentBorder());

        JPanel squadronDetailsPanel = makeSquadronDetailsPanel();
        this.add(squadronDetailsPanel, BorderLayout.CENTER);

        ImageToDisplaySizer.setDocumentSize(this);
    }

    public void setSquadronIntelText(int squadronId) throws PWCGException
    {
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
        
        String squadronIntelHeaderDesc = formSquadronHeaderDesc(squadron);
        squadronIntelHeaderText.setText(squadronIntelHeaderDesc);
        
        String squadronPersonnelDesc = formSquadronPersonnelDesc(squadron);
        squadronIntelPersonnelText.setText(squadronPersonnelDesc);

        String squadronEquipmentDesc = formSquadronEquipmentDesc(squadron);
        squadronIntelEquipmentText.setText(squadronEquipmentDesc);
    }

    private JPanel makeSquadronDetailsPanel() throws PWCGException
    {
        ImageResizingPanel squadronDetailsPanel = new ImageResizingPanel("");
        squadronDetailsPanel.setOpaque(false);
        squadronDetailsPanel.setLayout(new BorderLayout());
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        squadronDetailsPanel.setImageFromName(imagePath);
        squadronDetailsPanel.setBorder(PwcgBorderFactory.createStandardDocumentBorder());

        JPanel squadronDetailsHeaderPanel = formSquadronIntelHeader();
        squadronDetailsPanel.add(squadronDetailsHeaderPanel,BorderLayout.NORTH);

        JPanel squadronDetailsInfoPanel = formSquadronDetails();
        squadronDetailsPanel.add(squadronDetailsInfoPanel,BorderLayout.CENTER);

        return squadronDetailsPanel;
    }

    private JPanel formSquadronDetails() throws PWCGException
    {
        JPanel squadronDetailsPanel = new JPanel(new GridLayout(0, 2));
        squadronDetailsPanel.setOpaque(false);

        JPanel squadronPersonnelDetails = formSquadronIntelPersonnel();
        squadronDetailsPanel.add(squadronPersonnelDetails);

        JPanel squadronEquipmentDetails = formSquadronIntelEquipment();
        squadronDetailsPanel.add(squadronEquipmentDetails);
        return squadronDetailsPanel;
    }

    private JPanel formSquadronIntelPersonnel() throws PWCGException
    {
        squadronIntelPersonnelText = makeSquadronInfoTextArea();
        return WrapTextArea(squadronIntelPersonnelText);
    }
    
    private JPanel formSquadronIntelEquipment() throws PWCGException
    {
        squadronIntelEquipmentText = makeSquadronInfoTextArea();
        return WrapTextArea(squadronIntelEquipmentText);
    }
    
    private JPanel formSquadronIntelHeader() throws PWCGException
    {
        squadronIntelHeaderText = makeSquadronInfoTextArea();
        return WrapTextArea(squadronIntelHeaderText);
    }

    private JPanel WrapTextArea(JTextArea textArea)
    {
        JPanel intelTextPanel = new JPanel(new BorderLayout());
        intelTextPanel.setOpaque(false);
        intelTextPanel.add(textArea);
        return intelTextPanel;
    }

    private JTextArea makeSquadronInfoTextArea() throws PWCGException
    {
        JTextArea squadronIntelText;
        
        Font font = PWCGMonitorFonts.getTypewriterFont();
        squadronIntelText = new JTextArea();
        squadronIntelText.setFont(font);
        squadronIntelText.setOpaque(false);
        squadronIntelText.setLineWrap(false);
        squadronIntelText.setWrapStyleWord(true);
        squadronIntelText.setText("");
        
        return squadronIntelText;
    }

    private String formSquadronHeaderDesc(Squadron squadron) throws PWCGException
    {
        String stationedAtText = InternationalizationManager.getTranslation("Stationed At");
        String callSignText = InternationalizationManager.getTranslation("Call Sign");

        StringBuffer intelBuffer = new StringBuffer("");
        intelBuffer.append("\n");
        intelBuffer.append("        "  + squadron.determineDisplayName(campaign.getDate()));          
        intelBuffer.append("\n");
        intelBuffer.append("        " + stationedAtText + ": " + squadron.determineCurrentAirfieldName(campaign.getDate()));          
        intelBuffer.append("\n");
        intelBuffer.append("        " + callSignText + ": " + squadron.determineCurrentCallsign(campaign.getDate()));
        intelBuffer.append("\n");
        return intelBuffer.toString();
    }

    private String formSquadronPersonnelDesc(Squadron squadron) throws PWCGException
    {
        StringBuffer intelBuffer = new StringBuffer("");
        formPersonnel(squadron.getSquadronId(), intelBuffer);
        return intelBuffer.toString();
    }

    private String formSquadronEquipmentDesc(Squadron squadron) throws PWCGException
    {
        StringBuffer intelBuffer = new StringBuffer("");
        formAircraftInventory(squadron, intelBuffer);
        intelBuffer.append("\n");
        return intelBuffer.toString();
    }
    
    private void formPersonnel(int squadronId, StringBuffer intelBuffer) throws PWCGException
    {
        String personnelText = InternationalizationManager.getTranslation("Personnel");

        intelBuffer.append("\n        " + personnelText + "\n");        
        intelBuffer.append("        ----------------------------------------\n");          

        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(squadronId);
        SquadronMembers activeSquadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        List<SquadronMember> sortedPilots = activeSquadronMembers.sortPilots(campaign.getDate());
        for (SquadronMember squadronMember : sortedPilots)
        {
            intelBuffer.append("            " + squadronMember.getNameAndRank());          
            intelBuffer.append("\n");          
        }
        
        for(int i = sortedPilots.size(); i < 35; ++i)
        {
            intelBuffer.append("                              \n");          
        }
    }

    private void formAircraftInventory(Squadron squadron, StringBuffer intelBuffer) throws PWCGException
    {
        String aircraftInventoryText = InternationalizationManager.getTranslation("Aircraft On Inventory");

        intelBuffer.append("\n        " + aircraftInventoryText + "\n");        
        intelBuffer.append("        ----------------------------------------\n");          
        Map<Integer, EquippedPlane> aircraftOnInventory = campaign.getEquipmentManager().getEquipmentForSquadron(squadron.getSquadronId()).getActiveEquippedPlanes();
        List<EquippedPlane> sortedAircraftOnInventory = PlaneSorter.sortEquippedPlanesByGoodness(new ArrayList<EquippedPlane>(aircraftOnInventory.values()));
        for (int i = 0; i < sortedAircraftOnInventory.size(); ++i)
        {
            EquippedPlane plane = sortedAircraftOnInventory.get(i);
            intelBuffer.append("            " + plane.getDisplayName() + " (" + plane.getAircraftIdCode() + ")");
            intelBuffer.append(".\n");          
        }
    }
}
