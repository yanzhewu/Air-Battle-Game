package edu.uchicago.cs.java.finalproject.game.view;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GameFrame extends JFrame {

	private JPanel contentPane;
	private BorderLayout borderLayout1 = new BorderLayout();
    private Image backgroundImage = null;

	public GameFrame() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Component initialization
	private void initialize() throws Exception {
		contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(borderLayout1);
        setBack();
    }

    private void setBack(){
        ((JPanel) contentPane).setOpaque(false);
        loadRecources();
        ImageIcon img = new ImageIcon(backgroundImage);
        JLabel background = new JLabel(img);
        this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
        background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
    }

	@Override
	//Overridden so we can exit when window is closed
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			System.exit(0);
		}
	}

    public void loadRecources()
    {
        if(backgroundImage == null)
        {
            try
            {
                backgroundImage = ImageIO.read(new File("src/edu/uchicago/cs/java/finalproject/images/Starsinthesky.jpg"));
            } catch (IOException e)
            {
                System.out.println("No image");
                JOptionPane.showMessageDialog(this, "Can't find image!", "file not found", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

//    @Override
//    public void paint(Graphics g) {
//        //
//        loadRecources();
//        if(backgroundImage != null)
//        {
//            g.drawImage(backgroundImage, 0, 0, this);
//        }
//        super.paint(g);
//    }
}
