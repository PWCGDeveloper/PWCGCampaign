package pwcg.campaign;

import java.util.List;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.MapForAirfieldFinder;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;

public class CampaignGenerator 
{
    private CampaignGeneratorModel generatorModel;
    private Campaign campaign;
    
    public CampaignGenerator(CampaignGeneratorModel generatorModel)
    {
        this.generatorModel = generatorModel;
    }

    public Campaign generate() throws PWCGException
    {
        generatorModel.validateCampaignInputs();
        createCampaignBasis();
        staffSquadrons();
        createPersonnelReplacements();
        createEquipmentReplacements();
        return campaign;
    }

    private void createCampaignBasis() throws PWCGException
    {
        setMapForNewCampaign();
        createCampaign();
        setCampaignAces();
    }

    private void createCampaign() throws PWCGException
	{
		campaign = new Campaign();
        campaign.initializeCampaignConfigs();
        campaign.setDate(generatorModel.getCampaignDate());
        campaign.getCampaignData().setName(generatorModel.getCampaignName());
        campaign.getCampaignData().setCampaignMode(generatorModel.getCampaignMode());
	}

    private void staffSquadrons() throws PWCGException
    {
        List<Company> activeSquadronsOnCampaignStartDate = PWCGContext.getInstance().getSquadronManager().getActiveSquadrons(generatorModel.getCampaignDate());
        for (Company squadron : activeSquadronsOnCampaignStartDate)
        {
            CampaignSquadronGenerator squadronGenerator = new CampaignSquadronGenerator(campaign, squadron);
            squadronGenerator.createSquadron(generatorModel);
        }
        
        useCampaignPlayerToSetReferencePlayer();
    }

    private void useCampaignPlayerToSetReferencePlayer() throws PWCGException
    {
        CrewMember referencePlayer = campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList().get(0);
        campaign.getCampaignData().setReferencePlayerSerialNumber(referencePlayer.getSerialNumber());
    }

    private void createPersonnelReplacements() throws PWCGException
    {
        List<ArmedService> armedServices = ArmedServiceFactory.createServiceManager().getAllActiveArmedServices(campaign.getDate());
        for (ArmedService armedService : armedServices)
        {
            CampaignPersonnelManager personnelManager = campaign.getPersonnelManager();
            personnelManager.createPersonnelReplacements(armedService);
        }
    }

    private void createEquipmentReplacements() throws PWCGException
    {
        List<ArmedService> armedServices = ArmedServiceFactory.createServiceManager().getAllActiveArmedServices(campaign.getDate());
        for (ArmedService armedService : armedServices)
        {
            CampaignEquipmentManager equipmentGenerator = campaign.getEquipmentManager();
            equipmentGenerator.createEquipmentDepot(armedService);
        }
    }

    private void setMapForNewCampaign() throws PWCGException
    {
        Company company = PWCGContext.getInstance().getSquadronManager().getSquadronByName(generatorModel.getSquadronName(), generatorModel.getCampaignDate());
        Airfield airfield = company.determineCurrentAirfieldAnyMap(generatorModel.getCampaignDate());
        List<FrontMapIdentifier> airfieldMaps = MapForAirfieldFinder.getMapForAirfield(airfield.getName());
        FrontMapIdentifier initialAirfieldMap = airfieldMaps.get(0);

        PWCGContext.getInstance().changeContext(initialAirfieldMap);
    }

	private void setCampaignAces() throws PWCGException
	{
		CampaignAces aces =  PWCGContext.getInstance().getAceManager().loadFromHistoricalAces(generatorModel.getCampaignDate());
        campaign.getPersonnelManager().setCampaignAces(aces);
	}
}
