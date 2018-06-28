package pwcg.gui.rofmap.editmap;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.factory.CountryFactory;

public class CountryChooserInfoPopup extends JPopupMenu
{
	private static final long serialVersionUID = 1L;

	public CountryChooserInfoPopup(EditorMapPanel parent, EditorMapPoint mapPoint)
    {
		JMenuItem mapPointItemName = new JMenuItem(mapPoint.name + " (" + mapPoint.index + ")");
    	add(mapPointItemName);

        ICountry alliedCountry = CountryFactory.makeMapReferenceCountry(Side.ALLIED);
        ICountry axisCountry = CountryFactory.makeMapReferenceCountry(Side.AXIS);

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
