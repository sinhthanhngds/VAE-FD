//package drawing.cosineforce;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

//import graphs.Graph;

/**
 * Class of Menu Bar.
 *
 * @author Meng Xie (mxie4522@it.usyd.edu.au)
 */
@SuppressWarnings("serial")
public class MenuBar extends JMenuBar implements ActionListener {

    /* Class variables */
    private static Logger logger = Logger.getLogger("MenuBar");

    private Frame frame;

    /**
     * Constructor in which initiating menu items.
     *
     * @param f The father frame.
     */
    public MenuBar(Frame f){
        frame = f;

        /* Creates 'File' item. */
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.getAccessibleContext().setAccessibleDescription("The file menu");
        this.add(fileMenu);

        /* Creates 'Open' item. */
        JMenuItem openItem = new JMenuItem("Open", KeyEvent.VK_O);
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        openItem.getAccessibleContext().setAccessibleDescription("Open a file");
        fileMenu.add(openItem);
        openItem.addActionListener(this);

        fileMenu.addSeparator();

        /* Creates 'Exit' item. */
        JMenuItem exitItem = new JMenuItem("Exit", KeyEvent.VK_E);
        exitItem.getAccessibleContext().setAccessibleDescription("Exit program");
        fileMenu.add(exitItem);
        exitItem.addActionListener(this);
    }

    /**
     * Implementation of 'actionPerformed()', in which handling menu items action events.
     *
     * @param e The action event.
     */
    public void actionPerformed(ActionEvent e) {
        /* Handles 'Exit' event. */
        if (e.getActionCommand().equals("Exit")) System.exit(0);

        /* Handles 'Open' event. */
        if (e.getActionCommand().equals("Open")){

            JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));

            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
                frame.graph = new Graph(fc.getSelectedFile().getAbsolutePath());

				//******************

               	//frame.graph.randMove();
                CrossAngle.computeAngleForceElements(frame.graph);

                //frame.graph.printGraph();

                //******************

                frame.tempGraph = frame.graph.copy();

                frame.graphs.clear();
				frame.graphs.add(frame.graph.copy());
				frame.curr = 0;

                frame.repaintGraphPanel();
                frame.graphFileName = fc.getSelectedFile().getAbsolutePath();

                logger.info("Have chosen '" + frame.graphFileName + "'.");
            }
        }
    }
}
