package pwcg.campaign;

import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.MapForAirfieldFinder;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.group.airfield.Airfield;
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
        staffCompanies();
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

    private void staffCompanies() throws PWCGException
    {
        List<Company> activeSquadronsOnCampaignStartDate = PWCGContext.getInstance().getCompanyManager().getActiveCompanies(generatorModel.getCampaignDate());
        for (Company company : activeSquadronsOnCampaignStartDate)
        {
            CampaignCompanyGenerator companyGenerator = new CampaignCompanyGenerator(campaign, company);
            companyGenerator.createSquadron(generatorModel);
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
        Company company = PWCGContext.getInstance().getCompanyManager().getCompanyByName(generatorModel.getSquadronName(), generatorModel.getCampaignDate());
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
