import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;
import java.security.KeyFactory;


import java.io.File;
import java.io.FileInputStream;
import java.io.Writer;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;


import java.security.cert.CertificateFactory;
import java.security.cert.Certificate;
import java.io.ByteArrayInputStream;
import java.nio.channels.FileChannel;
import java.io.ByteArrayOutputStream;
import java.nio.channels.Channels;
import java.nio.charset.Charset;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;
//Andy, try some characteristics of Base64. Read Base64 coding private and public key file and transfer them into PKCS#8 and x.509 file.

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;


public class CD10 {
   public static void main(String args[]) throws Exception{
	   //Creating a Signature object
 				
            //as below, we will read bytes from base64 DEM files and transfer them into key files which have different names slightly.
            // You could replace the base 64 public and private key files to your own ones.	   
			// Your private key must begin with -----BEGIN RSA PRIVATE KEY-----, and end with -----END RSA PRIVATE KEY-----
			//Your public key must begin with -----BEGIN RSA PUBLIC KEY-----, and end with -----END RSA PUBLIC KEY-----
			String privateKeyContent = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("myTang_Private_Base64.pem").toURI())));
			String publicKeyContent = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("myTang_Public_Base64.pem").toURI())));

			privateKeyContent = privateKeyContent.replaceAll("\\n", "").replace("-----BEGIN RSA PRIVATE KEY-----", "").replace("-----END RSA PRIVATE KEY-----", "");
			publicKeyContent = publicKeyContent.replaceAll("\\n", "").replace("-----BEGIN RSA PUBLIC KEY-----", "").replace("-----END RSA PUBLIC KEY-----", "");;

			KeyFactory kf3 = KeyFactory.getInstance("RSA");

			PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
			PrivateKey privKey = kf3.generatePrivate(keySpecPKCS8);

			X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
			RSAPublicKey pubKey = (RSAPublicKey) kf3.generatePublic(keySpecX509);
			System.out.println();
                 System.out.println("****************************************************************");
			System.out.println("Public key transferred from Base64 PEM: "+pubKey);
			System.out.println("Prviate key transferred from Base64 PEM: "+privKey);
			System.out.println("");
			System.out.println("-------------------Before cipher and decipher----------------");
			System.out.println("");
			
		        String outFileName="myTang_Exported_from_Base64_PEM";
				OutputStream  output = new FileOutputStream(outFileName + ".pub");
				output.write(pubKey.getEncoded());
				output.close();

				output = new FileOutputStream(outFileName + ".key");
				output.write(privKey.getEncoded());
				output.close();	
				
				String privateKeyFile="myTang_Exported_from_Base64_PEM.key";
				String publicKeyFile="myTang_Exported_from_Base64_PEM.pub";
					
					Path path = Paths.get(privateKeyFile);
					byte[] bytes = Files.readAllBytes(path);
					/* Generate private key. */
					PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
					KeyFactory kf = KeyFactory.getInstance("RSA");
					PrivateKey private_Key = kf.generatePrivate(ks);
									
				/* Read all the public key bytes  from binary file*/
					
					Path path2 = Paths.get(publicKeyFile);
					bytes = Files.readAllBytes(path2);
					/* Generate public key. */
					X509EncodedKeySpec ks2 = new X509EncodedKeySpec(bytes);
					KeyFactory kf2 = KeyFactory.getInstance("RSA");
					PublicKey public_Key = kf2.generatePublic(ks2);
					
				
				cipher(public_Key);  //using publicKey to Ciph;er
				System.out.println();
				System.out.println("As below is cipher_decipher  ************************************:");
	
   }
   		//As below, use the key receovered from base64 PEM to cipher and decipher as a test.
		/* Read all bytes from the private key binary file */
		
		
static void cipher_decipher(PrivateKey private_Key,PublicKey public_Key)throws BadPaddingException,IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, InvalidKeySpecException, NoSuchAlgorithmException, IOException, URISyntaxException{
		
		  //Creating a Cipher object
		  Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		  //Initializing a Cipher object
		 // cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		 cipher.init(Cipher.ENCRYPT_MODE, public_Key);  //Andy
		  
		  //Add data to the cipher
		  byte[] input = "Welcome to Las Vegas".getBytes();	  
		  cipher.update(input);
		  
		  //encrypting the data
		  byte[] cipherText = cipher.doFinal();	 
		   System.out.print("Ciphered text:  ");
		  System.out.println( new String(cipherText, "UTF8"));
		  
		  
		  //prepare to save the cipherText into a file, then read it from file, then decrypt as a test. Andy
		  		        String outFileName="Ciphered_text2";
						OutputStream  output = new FileOutputStream(outFileName);
						output.write(cipherText);
						output.close();	
						
							FileInputStream encryptedTextFis = new FileInputStream("Ciphered_text2");
							byte[] encText = new byte[encryptedTextFis.available()];
							encryptedTextFis.read(encText);
							encryptedTextFis.close();
						
						
						byte[] cipherText2 = encText;

      //Initializing the same cipher for decryption
	  
		  // PrivateKey privKey = pair.getPrivate();   
		//  cipher.init(Cipher.DECRYPT_MODE, privKey);
		 cipher.init(Cipher.DECRYPT_MODE, private_Key);
				System.out.println();
				System.out.println("Keys generated");
				System.out.println("public key readed from File:   "+public_Key);
				System.out.println("private key readed from File:   "+private_Key);
				System.out.println();		

		  //Decrypting the text
		  byte[] decipheredText = cipher.doFinal(cipherText);
		  System.out.print("Deciphered Text:  ");
		  System.out.println(new String(decipheredText));
		  
		  byte[] decipheredText2 = cipher.doFinal(cipherText2);
		  System.out.print("Deciphered Text2:  ");
		  System.out.println(new String(decipheredText2));
		  
				  System.err.println("Private key format: " + private_Key.getFormat());
					// prints "Private key format: PKCS#8" on my machine
				 System.err.println("Public key format: " + public_Key.getFormat());
		}  
		
				
static void cipher(PublicKey public_Key)throws BadPaddingException,IllegalBlockSizeException, InvalidKeyException,NoSuchPaddingException, InvalidKeySpecException, NoSuchAlgorithmException, IOException, URISyntaxException{

		   //Creating a Cipher object
		  Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		  //Initializing a Cipher object
		 // cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		 cipher.init(Cipher.ENCRYPT_MODE, public_Key);  //Andy

        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			jfc.setCurrentDirectory(new File("."));
        int returnValue = jfc.showOpenDialog(null);
        // int returnValue = jfc.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
			
			//String fileString=selectedFile.toString();
			System.out.println(selectedFile);
            System.out.println(selectedFile.getAbsolutePath());
			
			doCipher(selectedFile, cipher, public_Key);  //if put this function outside of if, java will generate mistake in compiling.
        }
	}
	
	private static void doCipher(File selectedFile, Cipher cipher, PublicKey public_Key)throws BadPaddingException,IllegalBlockSizeException, InvalidKeyException,NoSuchPaddingException, InvalidKeySpecException, NoSuchAlgorithmException, IOException, URISyntaxException{
		File file = selectedFile;
			byte[] input= readContentIntoByteArray(file);
				//byte[] input= Base64.getDecoder().decode(readContentIntoByteArray(file));  //to avoide Exception in thread "main" javax.crypto.IllegalBlockSizeException: Data must not be longer than 245 bytes
			//	byte[] input=new String(readContentIntoByteArray(file), "UTF-8");
				
		   cipher.update(input);
		  
		  //encrypting the data
		  byte[] cipherText = cipher.doFinal();	 
		  System.out.println();
		   System.out.println("Ciphered text:  ");
		  System.out.println( new String(cipherText, "UTF8"));

		  //prepare to save the cipherText into a file, then read it from file, then decrypt as a test. Andy
	  
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("Specify a file to save");   
					 fileChooser.setCurrentDirectory(new File("."));  //setup the default folder as current working folder.
					int userSelection = fileChooser.showSaveDialog(null);
					 
					if (userSelection == JFileChooser.APPROVE_OPTION) {
						File fileToSave = fileChooser.getSelectedFile();
						System.out.println("Save as file: " + fileToSave.getAbsolutePath());
						
						//Tbe below 9 lines have to be inside if loop, otherwise java will said symbol not found error.
										String outFileName=fileToSave.toString();
										OutputStream  output = new FileOutputStream(outFileName);
										output.write(cipherText);
										output.close();	
												
										System.out.println();
										System.out.println("Keys generated");
										System.out.println("public key readed from File:   "+public_Key);

										System.out.println();		
										System.err.println("Public key format: " + public_Key.getFormat());
					}
	
	}
		
	private static byte[] readContentIntoByteArray(File file)
	   {
		  FileInputStream fileInputStream = null;
		  byte[] bFile = new byte[(int) file.length()];
		  try
		  {
			 //convert file into array of bytes
			 fileInputStream = new FileInputStream(file);
			 fileInputStream.read(bFile);
			 fileInputStream.close();
			 for (int i = 0; i < bFile.length; i++)
			 {
				System.out.print((char) bFile[i]);
			 }
		  }
		  catch (Exception e)
		  {
			 e.printStackTrace();
		  }
		  return bFile;
	   }

}





