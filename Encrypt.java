import java.io.*;
import static java.nio.file.StandardOpenOption.*;
import java.nio.file.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Encrypt
{
    private final int TOTAL_POSSIBLE_CHARACTERS = 256;

    private String [] alphabet;
    private String key = "";
    private JFrame mainFrame;
    private JLabel statusLabel;
    private JLabel headerLabel;
    private JPanel controlPanel;
    private final JTextField keyField;

    public Encrypt ()
    {
        int startingPoint = 0;
        int ending = TOTAL_POSSIBLE_CHARACTERS;
        alphabet = new String [TOTAL_POSSIBLE_CHARACTERS];
        keyField = new JTextField(6);

        for (int i = 0; i < TOTAL_POSSIBLE_CHARACTERS;i++)
        {
            alphabet [i] = String.valueOf (Character.toChars (i));
        }
    }

    public static void main (String[] arg) throws Exception
    {
        Encrypt encryptor = new Encrypt();
    }

    public ArrayList<String> translate(ArrayList list)
    {
        int keyLength = key.length();
        String toTranslate;
        String translatedString = "";
        ArrayList<String> translatedList = new ArrayList<String>();

        for (int x = 0; x < list.size(); x++)
        {
            toTranslate = list.get(x).toString();
            for (int i = 0; i < toTranslate.length(); i++)
            {
                char toTranslateChar = toTranslate.charAt (i);
                int asciiVal = (int) toTranslateChar;
                // this gets the letter of the key; the % is to loop back around if i > length of key - 1
                int keyLetter = (int) key.charAt (i % keyLength);

                // this tests if the character is within my ascii limit, I am not currently supporting more characters
                // the 256 ascii characters
                if (asciiVal >= 0 && asciiVal < TOTAL_POSSIBLE_CHARACTERS)
                {
                    // this formula does the actual encrypting
                    translatedString = translatedString + (alphabet [(asciiVal + keyLetter + TOTAL_POSSIBLE_CHARACTERS) % (TOTAL_POSSIBLE_CHARACTERS)]);
                }
                else
                {
                    translatedString = translatedString + toTranslateChar;
                }
            }
            translatedList.add(translatedString);
            translatedString = "";
        }

        return translatedList;
    }

    public ArrayList<String> readFile(File file) throws Exception
    {
        ArrayList<String> list = new ArrayList<String>();
        String text = "";
        char character;
        int value;
        BufferedReader reader = null;
        //File file = null;

        value = 0;
        try
        {
            //file = new File(file); 
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"ISO-8859-1"));

            while ((value = reader.read()) != -1)
            {
                character = (char) value;
                
                
                text = text + character;
                if (text.length() == Integer.MAX_VALUE)
                {
                    list.add(text);
                    text = "";
                }
            }
            list.add(text);
            System.out.println (text);

            list = translate(list);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (reader != null)
            {
                reader.close();
            }
        }

        return list;
    }

    public String writeFile(File inputFile) throws Exception
    {
        File outputFile = new File("encrypted.txt");
        BufferedWriter bufferWrite = null;
        ArrayList<String> list = readFile(inputFile);
        int counter = 0;

        while(outputFile.exists())
        {
            counter++;
            outputFile = new File("encrypted" + "(" + counter + ")" + ".txt");
        }
        
        try
        {
            outputFile.createNewFile();
        }
        catch (IOException e)
        {
        }

        try
        { 
            bufferWrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile),"ISO-8859-1"));
            try
            {
                for (int i = 0; i < list.size(); i++)
                {
                    bufferWrite.write (list.get(i).toString());
                }
            } 
            catch (IOException x) 
            {
            }

            bufferWrite.close();
        }

        catch (FileNotFoundException e)
        {
        }
        catch (IOException x)
        {
        }

        return outputFile.getAbsolutePath();
    }

    public void setKey (String key)
    {
        this.key = key;
    }

    public String getKey()
    {
        return key;
    }

    public String[] getAlphabet()
    {
        return alphabet;
    }
}