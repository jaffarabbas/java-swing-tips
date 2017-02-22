package example;
//-*- mode:java; encoding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
// import java.io.IOException;
import java.util.Arrays;
// import javax.imageio.ImageIO;
import javax.swing.*;

public final class MainPanel extends JPanel {
    private final JRadioButton ra = new JRadioButton("a.png");
    private final JRadioButton rb = new JRadioButton("b.png");
    private final JRadioButton rr = new JRadioButton("diff");
    private final ImageIcon iia = new ImageIcon(getClass().getResource("a.png"));
    private final ImageIcon iib = new ImageIcon(getClass().getResource("b.png"));
    private final JLabel label = new JLabel(iia);

    public MainPanel() {
        super(new BorderLayout());

        int w = iia.getIconWidth();
        int h = iia.getIconHeight();
        int[] pixelsA = getData(iia, w, h);
        int[] pixelsB = getData(iib, w, h);
        MemoryImageSource source = new MemoryImageSource(w, h, pixelsA, 0, w);
        for (int i = 0; i < pixelsA.length; i++) {
            if (pixelsA[i] == pixelsB[i]) {
                pixelsA[i] = pixelsA[i] & 0x44FFFFFF;
            }
        }

        ActionListener al = e -> {
            JComponent c = (JComponent) e.getSource();
            if (ra.equals(c)) {
                label.setIcon(iia);
            } else if (rb.equals(c)) {
                label.setIcon(iib);
            } else {
                label.setIcon(new ImageIcon(createImage(source)));
            }
        };
        JPanel p = new JPanel();
        ButtonGroup bg = new ButtonGroup();
        for (JRadioButton r: Arrays.asList(ra, rb, rr)) {
            r.addActionListener(al);
            bg.add(r);
            p.add(r);
        }
        ra.setSelected(true);
        add(label);
        add(p, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(320, 240));
    }
//     private BufferedImage makeBI(String str) {
//         BufferedImage image;
//         try {
//             image = ImageIO.read(getClass().getResource(str));
//         } catch (IOException ex) {
//             ex.printStackTrace();
//             return null;
//         }
//         return image;
//     }
    private static int[] getData(ImageIcon imageIcon, int w, int h) {
        Image img = imageIcon.getImage();
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
//         int[] pixels = new int[w * h];
//         try {
//             new PixelGrabber(image, 0, 0, width, height, pixels, 0, width).grabPixels();
//         } catch (InterruptedException ex) {
//             ex.printStackTrace();
//         }
//         return pixels;
    }
    public static void main(String... args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                createAndShowGUI();
            }
        });
    }
    public static void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
               | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        JFrame frame = new JFrame("@title@");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
