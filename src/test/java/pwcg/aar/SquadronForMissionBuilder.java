package pwcg.aar;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;

public class SquadronForMissionBuilder
{
    Campaign campaign;
    List<Company> squadronsInMission = new ArrayList<>();


    public SquadronForMissionBuilder (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public List<Company> makeSquadronsInMission() throws PWCGException
    {
        Company jg51 = PWCGContext.getInstance().getCompanyManager().getCompany(20111051);
        Company stg77 = PWCGContext.getInstance().getCompanyManager().getCompany(20121077);
        Company kg76 = PWCGContext.getInstance().getCompanyManager().getCompany(20132076);
        Company reg11 = PWCGContext.getInstance().getCompanyManager().getCompany(10111011);
        Company reg132 = PWCGContext.getInstance().getCompanyManager().getCompany(10131132);
        Company reg175 = PWCGContext.getInstance().getCompanyManager().getCompany(10121175);
        
        squadronsInMission.add(jg51);
        squadronsInMission.add(stg77);
        squadronsInMission.add(kg76);
        squadronsInMission.add(reg11);
        squadronsInMission.add(reg132);
        squadronsInMission.add(reg175);
        
        return squadronsInMission;
    }

}
