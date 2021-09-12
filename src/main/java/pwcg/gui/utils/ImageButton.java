package pwcg.gui.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.image.ImageCache;

public class ImageButton
{
    public static Button makeButton(String text, String imageName) throws PWCGException
    {
        String imagePath = ContextSpecificImages.imagesNational() + imageName;
        Image icon = new Image(imagePath);
        ImageView view = new ImageView(icon);
        view.setFitHeight(80);
        view.setPreserveRatio(true);
        
        Button button = new Button(text);
        button.setGraphic(view);

        // JAVAFX 
//        button.setBorderPainted(false);
//        button.setFocusPainted(false);
//        button.setOpaque(false);

        Font font = PWCGMonitorFonts.getPrimaryFontSmall();
        button.setFont(font);

        return button;
    }

    public static Button makeTranslucentButton(String filename) throws PWCGException
    {
        String imagePath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Menus\\" + filename;

        BufferedImage bufferedImage = ImageCache.getImageFromFile(imagePath);
        BufferedImage modifiedImage = getModifiedImage(bufferedImage);
        Image icon = SwingFXUtils.toFXImage(modifiedImage, null);

        ImageView view = new ImageView(icon);
        view.setFitHeight(80);
        view.setPreserveRatio(true);

        Button button = new Button();
        button.setGraphic(view);

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

    public static Label makePilotPicButton(Image image) throws PWCGException
    {
        ImageView view = new ImageView(image);
        view.setFitHeight(80);
        view.setPreserveRatio(true);

        Label pilotPicLabel = new Label();
        pilotPicLabel.setGraphic(view);

        return pilotPicLabel;
    }

    public static CheckBox makeCheckBox(String text, String imageName) throws PWCGException
    {
        Image selectedIcon = getOwnedIcon(imageName, true);

        CheckBox checkBox = new CheckBox(text);
        Font font = PWCGMonitorFonts.getPrimaryFontSmall();

        checkBox.setFont(font);
        checkBox.setAlignment(Pos.CENTER_LEFT);
//        checkBox.setOpaque(false);
//        checkBox.setSize(300, 50);
        
        ImageView view = new ImageView(selectedIcon);
        view.setFitHeight(80);
        view.setPreserveRatio(true);
        checkBox.setGraphic(view);

        return checkBox;
    }

    private static Image getOwnedIcon(String imageName, boolean owned)
    {
        String imagePath = ContextSpecificImages.imagesProfiles() + imageName;
        if (!owned)
        {
            imagePath += "No";
        }

        imagePath += ".jpg";

        Image icon = new Image(imagePath);

        return icon;
    }

}
