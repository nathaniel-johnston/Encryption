import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Decrypt
{
    private final int TOTAL_POSSIBLE_CHARACTERS = 256;
    private Encrypt encrypt = new Encrypt();
    private String key = "";
    private JFrame mainFrame;
    private JLabel statusLabel;
    private JLabel headerLabel;
    private JPanel controlPanel;
    private final JTextField keyField = new JTextField(6);

    public Decrypt() 
    {

    }

    public static void main(String[] arg) throws Exception {
        Decrypt decrypt = new Decrypt();
    }

    public ArrayList<String> decipher(ArrayList list) {
        int keyLength = key.length();
        String translatedString = "";
        int decryptIndex = 0;
        ArrayList<String> translatedList = new ArrayList<String>();

        for (int x = 0; x < list.size(); x++) {
            String toTranslate = list.get(x).toString();
            for (int i = 0; i < toTranslate.length(); i++) {
                char toTranslateChar = toTranslate.charAt(i);
                int asciiVal = (int)toTranslate.charAt(i);
                int keyLetter = (int)key.charAt(i % keyLength);
                if (i < 5)
                {
                    System.out.println(asciiVal);
                }

                if ((asciiVal >= 0) && (asciiVal < TOTAL_POSSIBLE_CHARACTERS)) {
                    decryptIndex = (asciiVal - keyLetter + TOTAL_POSSIBLE_CHARACTERS) % TOTAL_POSSIBLE_CHARACTERS;
                    translatedString = translatedString + encrypt.getAlphabet()[decryptIndex];
                }
                else {
                    translatedString = translatedString + toTranslateChar;
                }
            }
            translatedList.add(translatedString);
            translatedString = "";
        }
        return translatedList;
    }

    public ArrayList<String> readFile(File file)throws Exception
    {
        ArrayList<String> list = new ArrayList<String>();
        String text = "";
        BufferedReader reader = null;
        int value = 0;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "ISO-8859-1"));
            while ((value = reader.read()) != -1) {
                char character = (char)value;

                //System.out.println(character);
                text = text + character;
                if (text.length() == Integer.MAX_VALUE)
                {
                    list.add(text);
                    text = "";
                }
            }
            //System.out.println(text);
            list.add(text);
            //System.out.println(list.get(0));
            list = decipher(list);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }

        /*
        for (int i = 0; i < list.size(); i++)
        {
        System.out.println (list.get(i).toString());
        }
         */

        return list;
    }

    public String writeFile(File inputFile) throws Exception {
        File outputFile = new File("decrypted.txt");
        BufferedWriter bufferWrite = null;
        ArrayList<String> list = readFile(inputFile);
        int counter = 0;
        while (outputFile.exists()) {
            outputFile = new File("decrypted(" + ++counter + ")" + ".txt");
        }
        try {
            outputFile.createNewFile();
        }
        catch (IOException localIOException) {}

        try
        {
            bufferWrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "ISO-8859-1"));
            try {
                for (int i = 0; i < list.size(); i++) {
                    bufferWrite.write((list.get(i)).toString());
                }
            }
            catch (IOException localIOException1) {}

            bufferWrite.close();
        }
        catch (FileNotFoundException localFileNotFoundException) {}catch (IOException localIOException2) {}

        return outputFile.getAbsolutePath();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String encrypt(File file) {
        encrypt.setKey(key);
        try {
            return encrypt.writeFile(file);
        }
        catch (Exception e) {}
        return "";
    }
}
