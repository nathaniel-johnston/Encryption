import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class GUIStuff
{
    private JLabel statusLabel;
    private Decrypt decryptor;
    private JPasswordField keyField;
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JPanel controlPanel;
    private JButton encryptButton;
    private JButton decryptButton;
    private File file;
    private JFileChooser fileDialog;

    public GUIStuff()
    {
        decryptor = new Decrypt();

        keyField = new JPasswordField(6);

        fileDialog = new JFileChooser();
    }

    public static void main (String[] args)
    {
        GUIStuff gui = new GUIStuff();

        gui.createTextFields();
        gui.makeButtons();
        //gui.makeFileChooser();
    }

    public void makeFrame()
    {
        mainFrame = new JFrame("Nathaniel's encryption program");
        mainFrame.setSize(400,400);
        mainFrame.setLayout(new GridLayout(3, 1));
        mainFrame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent windowEvent)
                {
                    System.exit(0);
                }        
            });    
        headerLabel = new JLabel("", JLabel.CENTER);        
        statusLabel = new JLabel("",JLabel.CENTER);    

        statusLabel.setSize(350,100);

        controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());

        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        mainFrame.setResizable(false);
        //mainFrame.setVisible(true); 
    }

    public void createTextFields()
    {
        makeFrame();
        JPanel panel = new JPanel();
        panel.setLayout (new FlowLayout());

        headerLabel.setText("Encryption program"); 

        JLabel label = new JLabel("Key: ", JLabel.CENTER);

        panel.add (label);
        panel.add (keyField);

        controlPanel.add(panel, BorderLayout.CENTER);
        //controlPanel.add (label, BorderLayout.CENTER);
        //mainFrame.setVisible(true);  
    }

    public void makeButtons()
    {
        JButton helpButton = new JButton("Help");

        decryptButton = new JButton("Decrypt file");
        encryptButton = new JButton("Encrypt file");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout (new FlowLayout());

        helpButton.addActionListener (new HelpButtonListener());

        decryptButton.addActionListener(new EncryptButtonListener());
        encryptButton.addActionListener(new EncryptButtonListener());

        buttonPanel.add (encryptButton);
        buttonPanel.add (decryptButton);
        buttonPanel.add (helpButton);

        controlPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainFrame.setVisible (true);
    }

    public void openFile(String file)
    {
        try
        {
            ProcessBuilder pb = new ProcessBuilder("Notepad.exe", file);
            pb.start();
        }
        catch (IOException exception)
        {
            
        }
    }

    public class EncryptButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        { 
            new Thread()
            {
                public void run()
                {
                    int returnVal = fileDialog.showOpenDialog(mainFrame);

                    Object source = event.getSource();
                    decryptor.setKey (new String (keyField.getPassword()));

                    if (returnVal == JFileChooser.APPROVE_OPTION)
                    {
                        file = fileDialog.getSelectedFile();

                        if (decryptor.getKey().length() >= 1)
                        {

                            try
                            {
                                //statusLabel.validate();
                                statusLabel.setText ("working...");
                                statusLabel.validate();

                                if (source == decryptButton)
                                {
                                    //decryptor.writeFile(file);
                                    openFile(decryptor.writeFile(file));
                                }
                                else if (source == encryptButton)
                                {
                                    //decryptor.encrypt(file);
                                    openFile(decryptor.encrypt(file));
                                }

                                statusLabel.setText ("done!");
                                statusLabel.validate();
                            }
                            catch (Exception x)
                            {
                                statusLabel.setText ("couldn't write to the file :(");
                                statusLabel.validate();
                            }
                        }
                        else
                        {
                            statusLabel.setText ("Empty field, please enter a key.");
                            statusLabel.validate();
                        }
                    }
                    else
                    {
                        statusLabel.setText("Open command cancelled.");
                        statusLabel.validate();
                    }
                }
            } .start();
        }
    }

    public class HelpButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {  
            JFrame helpFrame = new JFrame("Help");
            helpFrame.setSize(500,300);
            //helpFrame.setLayout(new GridLayout(3, 1));

            JLabel label = new JLabel("<html>Welcome to Nathaniel\'s encryption program!<br>"
                    + " To Encrypt a file, enter the key you wish to use to encrypt your file press the Encrypt file button and select the file"
                    + " you wish to encrypt. The encrypted text should appear in"
                    + " a file called encrypted.txt within the same folder as this program. To decrypt your message, enter the key used to"
                    + " encrypt the message, press the Decrypt file button and select the file you wish to decrypt."
                    + " The decrypted text should appear in a file called decrypted.txt in the same folder as this program. ATTENTION THE FILES"
                    + " YOU SELECT MUST ALWAYS BE .TXT FILES!!!<br> Enjoy"
                    + " your encrypting!<html>");

            helpFrame.add (label);
            helpFrame.setResizable(false);
            helpFrame.setVisible(true);
        }
    }
}
