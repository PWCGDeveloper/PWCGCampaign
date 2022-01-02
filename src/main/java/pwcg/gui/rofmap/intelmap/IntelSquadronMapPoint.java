package pwcg.gui.rofmap.intelmap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.squadron.Company;


public class IntelSquadronMapPoint extends IntelMapPoint
{
	public String desc;
	public ArmedService service;
	public Company squadron;
	public boolean isPlayerSquadron = false;
}
