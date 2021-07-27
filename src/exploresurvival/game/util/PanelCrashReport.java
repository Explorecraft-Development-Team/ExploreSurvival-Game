package exploresurvival.game.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JFrame;

import org.lwjgl.opengl.Display;


public class PanelCrashReport extends Panel {
	public PanelCrashReport(Throwable e) {
		super();
		System.gc();
		this.setBackground(new Color(0x2e3444));
		this.setLayout(new BorderLayout());
		StringWriter sw=new StringWriter();
		sw.append("      ExploreSurvival has crashed!      \n");
		sw.append("      ----------------------------      \n");
		sw.append("\n");
		sw.append("ExploreSurvival has stopped running because it encountered a problem.\n");
		sw.append("You can report on https://github.com/Explorecraft-Development-Team/ExploreSurvival-Game/issues.\nThere is crash report:\n");
		sw.append("--- BEGIN CRASH REPORT ").append(Integer.toHexString(e.hashCode()).toUpperCase()).append(" --------\n");
		e.printStackTrace(new PrintWriter(sw));
		sw.append("--- END CRASH REPORT ").append(Integer.toHexString(e.hashCode()).toUpperCase()).append(" --------\n");
		TextArea area = new TextArea(sw.toString(), 0, 0, 1);
		area.setFont(new Font("Monospace", 0, 16));
		area.setSize(854,480);
		area.setBackground(new Color(0xaa));
		area.setForeground(Color.white);
		this.add(area, "Center");
		JFrame f=new JFrame("ExploreSurvival CrashReport");
		Dimension dim;
		try {
			dim=new Dimension(Display.getWidth(),Display.getHeight());
			f.setLocation(Display.getX(), Display.getY());
		} catch(Exception exc) {
			dim=new Dimension(854,480);
		}
		f.setPreferredSize(dim);
		f.setMinimumSize(dim);
		f.setResizable(true);
		f.add(this);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent par1WindowEvent)
		    {
		        System.exit(1);
		    }
		});
		f.setVisible(true);
	}
}