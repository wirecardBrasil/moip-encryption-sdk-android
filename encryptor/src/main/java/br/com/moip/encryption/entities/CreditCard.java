package br.com.moip.encryption.entities;

import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;

import br.com.moip.encryption.exception.MoipEncryptionException;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by caueferreira on 17/04/15.
 */
public class CreditCard {

    private String expirationMonth;
    private String expirationYear;
    private String number;
    private String cvc;
    private String publicKey;

    public CreditCard() {
    }

    public CreditCard(final String expirationMonth, final String expirationYear, final String number, final String cvc, final String publicKey) {
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
        this.number = number;
        this.cvc = cvc;
        this.publicKey = publicKey;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(final String expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(final String expirationYear) {
        this.expirationYear = expirationYear;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(final String cvc) {
        this.cvc = cvc;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(final String publicKey) {
        this.publicKey = publicKey;
    }

    private static Cipher cipher;

    private String toHash() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("number=");
        stringBuilder.append(number);
        stringBuilder.append("&");
        stringBuilder.append("cvc=");
        stringBuilder.append(cvc);
        stringBuilder.append("&");
        stringBuilder.append("expirationMonth=");
        stringBuilder.append(expirationMonth);
        stringBuilder.append("&");
        stringBuilder.append("expirationYear=");
        stringBuilder.append(expirationYear);

        return stringBuilder.toString();
    }

    public String encrypt() throws MoipEncryptionException {
        try {
            try {
                cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "SC");
            } catch (SecurityException se) {
                //workaround for tests
                Log.i("Moip SDK","No SC provider, running test profile");
                cipher = Cipher.getInstance("RSA");
            }

            BufferedReader pemReader = null;
            pemReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(publicKey.getBytes("UTF-8"))));

            StringBuffer content = new StringBuffer();
            String line = null;
            while ((line = pemReader.readLine()) != null) {
                if (line.indexOf("-----BEGIN PUBLIC KEY-----") != -1) {
                    while ((line = pemReader.readLine()) != null) {
                        if (line.indexOf("-----END PUBLIC KEY") != -1) {
                            break;
                        }
                        content.append(line.trim());
                    }
                    break;
                }
            }
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decode(content.toString(), Base64.DEFAULT))));
            byte[] cipherText = cipher.doFinal(toHash().getBytes());

            return Base64.encodeToString(cipherText, Base64.DEFAULT);
        } catch (SecurityException e) {
            e.printStackTrace();
            throw new MoipEncryptionException(e);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new MoipEncryptionException(e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new MoipEncryptionException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new MoipEncryptionException(e);
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new MoipEncryptionException(e);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new MoipEncryptionException(e);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new MoipEncryptionException(e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new MoipEncryptionException(e);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new MoipEncryptionException(e);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            throw new MoipEncryptionException(e);
        }
    }
}