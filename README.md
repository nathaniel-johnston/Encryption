# Encryption
This Java application uses the Vigenère cipher to encrypt and decrypt files for safer storage. This cipher is polyalphabetic, meaning that the same word can be encrypted in different ways, depending on the key provided. It is fairly secure in that the key used is not stored anywhere. Instead, when someone tries to decrypt an encrypted file, the program will attempt to decrypt it using the provided key. However, it's output will likely be meaningless without the correct key.

## How it works
There are two main classes in this program. One for encrypting, and one for decrypting. In either case, the file is read, stored in a list of strings (a list in case the contents of the file are too large), the list is encrypted, then written to anoter file. The following code snippets will only show the encryption method as the others are more trivial and the decryption is basically the same thing but in reverse.

```Java
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
```

## Limitations and improvements
The obvious improvement is the fact that it isn't all that fast. With large files, it can take a long time to read, translate, and write. I plan on looking into how to optimize the speed. However, each character needs to be translated one by one, so in order to increase the speed, a large change to the overall architecture of the program must be made.

In addition, it only translates .txt files currently. Technically it can take in any file as input, but the output is still a .txt. This isn't really an issue for encrypting files since the encrypted file can't be used anyway, but once the file is decrypted again, the file is stroed in a .txt, meaning that you must manually change the file type yourself.
