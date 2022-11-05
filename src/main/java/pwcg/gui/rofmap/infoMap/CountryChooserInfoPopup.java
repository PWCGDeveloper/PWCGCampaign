package pwcg.gui.rofmap.infoMap;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.factory.CountryFactory;

public class CountryChooserInfoPopup extends JPopupMenu
{
	private static final long serialVersionUID = 1L;

	public CountryChooserInfoPopup(FrontMapIdentifier mapIdentifier, InfoMapPanel parent, InfoMapPoint mapPoint)
    {
		JMenuItem mapPointItemName = new JMenuItem(mapPoint.name + " (" + mapPoint.index + ")");
    	add(mapPointItemName);

        ICountry alliedCountry = CountryFactory.makeMapReferenceCountry(mapIdentifier, Side.ALLIED);
        ICountry axisCountry = CountryFactory.makeMapReferenceCountry(mapIdentifier, Side.AXIS);

        JMenuItem mapPointItemGermany = new JMenuItem(axisCountry.getNationality());
        mapPointItemGermany.setActionCommand(axisCountry.getNationality());
        add(mapPointItemGermany);
        mapPointItemGermany.addActionListener(parent);

		JMenuItem mapPointItemStalingrad = new JMenuItem(alliedCountry.getNationality());
		mapPointItemStalingrad.setActionCommand(alliedCountry.getNationality());
    	add(mapPointItemStalingrad);
    	mapPointItemStalingrad.addActionListener(parent);

		JMenuItem mapPointItemCancel = new JMenuItem("Cancel");
		mapPointItemCancel.setActionCommand("Cancel");
    	add(mapPointItemCancel);
    	mapPointItemCancel.addActionListener(parent);
    }
}
