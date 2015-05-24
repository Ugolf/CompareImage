package pl.matik;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.abs;

/**
 * Created by ... on 2015-03-24.
 *
 * Problems with Java heap space when loading big files
 * Sometimes loading file error
 */
public class Gui extends JFrame{

    private JPanel rootPanel;
    private JButton loadImageButton;
    private JButton loadImageButton1;
    private JButton compareButton;
    private JLabel fileName;
    private JLabel fileName1;
    private JLabel showImage1;
    private JLabel showImage2;
    private JButton resultButton;
    private JToggleButton showImageBtn1;
    private JToggleButton showImageBtn2;
    private BufferedImage img=null, newImage=null;
    Color[][] firstImagePixels, secondImagePixels;
    File inImage1, inImage2;
    File outputfile = new File("result.png");

    Gui() {
        super("Compare image");
        setContentPane(rootPanel);
        pack();
        loadImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                firstImagePixels = new Color[0][0];
                secondImagePixels = new Color[0][0];
                outputfile = new File("result.png");
                final JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setCurrentDirectory(new File("."));
                int returnVal = jFileChooser.showOpenDialog(Gui.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = jFileChooser.getSelectedFile();
                    readFile(file.getName(),1);
                    //readImage(file, showImage1, 1);
                    pack();
                } else if (returnVal == JFileChooser.CANCEL_OPTION) {
                    fileName.setText("Brak");
                } else if (returnVal == JFileChooser.ERROR_OPTION) {
                    fileName.setText("Bląd!");
                } else {
                    fileName.setText("Nieznany...");
                }
            }
        });

        loadImageButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setCurrentDirectory(new File("."));
                int returnVal = jFileChooser.showOpenDialog(Gui.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = jFileChooser.getSelectedFile();
                    readFile(file.getName(),2);
                    //readImage(file, showImage2, 2);
                    pack();
                } else if (returnVal == JFileChooser.CANCEL_OPTION) {
                    fileName.setText("Brak");
                } else if (returnVal == JFileChooser.ERROR_OPTION) {
                    fileName.setText("Bląd!");
                } else {
                    fileName.setText("Nieznany...");
                }
            }
        });

        compareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compare(firstImagePixels, secondImagePixels);
                //readImage(outputfile, showResult, 3);
                Image imgResized = null;
                try {
                    imgResized = ImageIO.read(outputfile);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                double w = resizeImage(imgResized,200).getWidth();
                double h = resizeImage(imgResized,200).getHeight();
                ImageIcon icon = new ImageIcon(imgResized.getScaledInstance((int)w,(int)h,Image.SCALE_DEFAULT));
                icon.getImage().flush();
                JOptionPane.showMessageDialog(null, null, "Result", JOptionPane.INFORMATION_MESSAGE, icon);
                pack();
            }
        });

        resultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Image imgResized = null;
                try {
                    imgResized = ImageIO.read(outputfile);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                double w = resizeImage(imgResized,200).getWidth();
                double h = resizeImage(imgResized,200).getHeight();
                ImageIcon icon = new ImageIcon(imgResized.getScaledInstance((int) w, (int) h, Image.SCALE_DEFAULT));
                icon.getImage().flush();
                JOptionPane.showMessageDialog(null, null, "Result", JOptionPane.INFORMATION_MESSAGE, icon);
            }
        });

        showImageBtn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(showImageBtn1.isSelected()){
                    readImage(inImage1, showImage1,1);
                    showImageBtn1.setText("Hide image 1");
                }else{
                    showImage1.setIcon(null);
                    showImageBtn1.setText("Show image 1");
                    repaint(0, 0, showImage1);
                }

            }
        });

        showImageBtn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(showImageBtn2.isSelected()){
                    readImage(inImage2, showImage2,2);
                    showImageBtn2.setText("Hide image 2");
                    //System.out.println(showImage2.getWidth() + "x" + showImage2.getHeight());
                }else{
                    showImage2.setIcon(null);
                    showImageBtn2.setText("Show image 2");
                    repaint(0, 0, showImage2);
                }
            }
        });
    }

    void compare(Color[][] colorsFirst, Color[][]  colorsSecond){
        if(colorsFirst.length > colorsSecond.length){
            newImage = new BufferedImage(colorsFirst.length, colorsFirst[0].length, BufferedImage.TYPE_INT_RGB);
        }else{
            newImage = new BufferedImage(colorsSecond.length, colorsSecond[0].length, BufferedImage.TYPE_INT_RGB);
        }
        int newHeight;
        if(newImage.getHeight() > colorsSecond[0].length){
            newHeight = colorsSecond[0].length;
        }else {
            newHeight = colorsFirst[0].length;
        }
        int newWidth;
        if(colorsFirst.length > colorsSecond.length){
            newWidth = colorsSecond.length;
        }else {
            newWidth = colorsFirst.length;
        }
/*
        System.out.println(newWidth+"x"+newHeight+" "+newImage.getWidth()+"x"+newImage.getHeight()+" 1: "+
                + colorsFirst.length +"x"+colorsFirst[0].length+" 2:"+
                + colorsSecond.length+"x"+colorsSecond[0].length);
*/
        for(int i = 1; i < newWidth ; i++){
            for(int j = 1; j < newHeight; j++){
                //System.out.println(i+" "+j);
                if(colorsFirst[i][j].equals(colorsSecond[i][j])){
                    Color newColor = new Color(255,255,255);
                    int rgb = newColor.getRGB();
                    newImage.setRGB(i, j, rgb);
                }else{
                    int grayFrist = (colorsFirst[i][j].getRed()+colorsFirst[i][j].getGreen()+colorsFirst[i][j].getBlue())/3;
                    int graySecond = (colorsSecond[i][j].getRed()+colorsSecond[i][j].getGreen()+colorsSecond[i][j].getBlue())/3;
                    Color newColor = grayScale(abs(grayFrist-graySecond));
                    int rgb = newColor.getRGB();
                    newImage.setRGB(i,j,rgb);
                }
            }
        }
        try {
            ImageIO.write(newImage, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Color grayScale(int difference){
        //System.out.println(difference);
        if(difference<1){difference=1;}
        if(difference>255){difference=255;}
        int r,g,b;
        r=g=b=difference;
        return new Color(r,g,b);
    }

    void readImage(File file, JLabel label, int button){
        try {
            img=ImageIO.read(file);
            double w = resizeImage(img,400).getWidth();
            double h = resizeImage(img,400).getHeight();
            ImageIcon icon = new ImageIcon(img.getScaledInstance((int)w,(int)h,Image.SCALE_DEFAULT));
            if(button == 1){
                  showImage1.setIcon(icon);
            }else{
                  showImage2.setIcon(icon);
            }
            Dimension imageSize = new Dimension(icon.getIconWidth(),icon.getIconHeight());
            label.setPreferredSize(imageSize);
            label.revalidate();
            label.repaint();
        }
        catch(IOException e1) {}
        pack();
        centreWindow(Gui.this);
    }

    void readFile(String name, int button){
        BufferedImage image=null;
        File imageFile = new File(name);
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            System.out.println("Problem z wczytaniem zdjecia");
            e.printStackTrace();
        }

        if(button==1) {
            fileName.setText("<html>" + imageFile.getName()+"<br>Size: "+image.getWidth()+"x"+image.getHeight()+"</html>");
            firstImagePixels = new Color[image.getWidth()][image.getHeight()];
            for (int i = 1; i < image.getWidth(); i++) {
                for (int j = 1; j < image.getHeight(); j++) {
                    firstImagePixels[i][j] = new Color(image.getRGB(i, j));
                }
            }
            inImage1 = new File(name);
        }else{
            fileName1.setText("<html>" + imageFile.getName()+"<br>Size: "+image.getWidth()+"x"+image.getHeight()+"</html>");
            secondImagePixels = new Color[image.getWidth()][image.getHeight()];
            for (int i = 1; i < image.getWidth(); i++) {
                for (int j = 1; j < image.getHeight(); j++) {
                    secondImagePixels[i][j] = new Color(image.getRGB(i, j));
                }
            }
            inImage2 = new File(name);
        }
        centreWindow(Gui.this);
    }

    public Dimension resizeImage(Image image, int diff){
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = screen.getWidth();
        double screenHeight = screen.getHeight();
/*
        Dimension window = rootPanel.getBounds().getSize();
        double windowWidth = window.getWidth();
        double windowHeight = window.getHeight();
*/
        double imgH = image.getHeight(null);
        double imgW = image.getWidth(null);
        Dimension newDim = new Dimension((int)imgW, (int)imgH);

        if(imgW > screenWidth || imgH > screenHeight){
            //System.out.println("okno za duże "+imgW+" "+screenWidth+" "+imgH+" "+screenHeight);
            double newH = screenHeight - diff;
            double newW = newH * (imgW/imgH);
            newDim = new Dimension((int)newW, (int)newH);
            //System.out.println(newW+"x"+newH);
            return newDim;
        }else {
            //System.out.println("jest spoko "+imgW+" "+screenWidth+" "+imgH+" "+screenHeight);
            return newDim;
        }
    }

    public void repaint(int w, int h, JLabel label){
        Dimension dim = new Dimension(w,h);
        label.setPreferredSize(dim);
        label.revalidate();
        label.repaint();
        pack();
    }

    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
}
