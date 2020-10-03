package pwcg.gui.utils;

import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.image.ImageCache;

public class ImageButton
{
    public static PWCGJButton makeButton(String text, String imageName) throws PWCGException
    {
        String imagePath = ContextSpecificImages.imagesNational() + imageName;
        ImageIcon icon = new ImageIcon(imagePath);

        PWCGJButton button = new PWCGJButton(text);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setIcon(icon);

        Font font = PWCGMonitorFonts.getPrimaryFontSmall();

        button.setFont(font);
        button.setOpaque(false);

        return button;
    }

    public static PWCGJButton makeButton(String text, Image image) throws PWCGException
    {
        ImageIcon icon = new ImageIcon(image);

        PWCGJButton button = new PWCGJButton(text);
        button.setIcon(icon);

        Font font = PWCGMonitorFonts.getPrimaryFontSmall();

        button.setFont(font);
        button.setOpaque(false);

        return button;
    }

    public static PWCGJButton makeTranslucentButton(String filename) throws PWCGException
    {
        String imagePath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Menus\\" + filename;

        BufferedImage bufferedImage = ImageCache.getImageFromFile(imagePath);
        BufferedImage modifiedImage = getModifiedImage(bufferedImage);
        ImageIcon icon = new ImageIcon(modifiedImage);

        PWCGJButton button = new PWCGJButton(icon);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        return button;
    }

    private static BufferedImage getModifiedImage(BufferedImage originalImage)
    {
        BufferedImage modifiedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = modifiedImage.createGraphics();
        AlphaComposite newComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        g2.setComposite(newComposite);
        g2.drawImage(originalImage, 0, 0, null);
        g2.dispose();
        return modifiedImage;
    }

    public static JLabel makePilotPicButton(Image image) throws PWCGException
    {
        ImageIcon icon = new ImageIcon(image);

        JLabel button = new JLabel("");
        button.setIcon(icon);

        return button;
    }

    public static JCheckBox makeCheckBox(String text, String imageName) throws PWCGException
    {
        Icon notSelectedIcon = getOwnedIcon(imageName, false);
        Icon selectedIcon = getOwnedIcon(imageName, true);

        JCheckBox checkBox = new JCheckBox(text);

        Font font = PWCGMonitorFonts.getPrimaryFontSmall();

        checkBox.setFont(font);
        checkBox.setHorizontalAlignment(JLabel.LEFT);
        checkBox.setOpaque(false);
        checkBox.setSize(300, 50);
        checkBox.setIcon(notSelectedIcon);
        checkBox.setSelectedIcon(selectedIcon);

        return checkBox;
    }

    private static Icon getOwnedIcon(String imageName, boolean owned)
    {
        String imagePath = ContextSpecificImages.imagesProfiles() + imageName;
        if (!owned)
        {
            imagePath += "No";
        }

        imagePath += ".jpg";

        ImageIcon icon = new ImageIcon(imagePath);

        return icon;
    }

}
