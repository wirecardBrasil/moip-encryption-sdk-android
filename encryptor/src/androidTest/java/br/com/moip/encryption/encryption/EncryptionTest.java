package br.com.moip.encryption.encryption;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.NoSuchPaddingException;

import br.com.moip.encryption.RobolectricGradleTestRunner;
import br.com.moip.encryption.entities.CreditCard;
import br.com.moip.encryption.exception.MoipEncryptionException;

import static junit.framework.Assert.assertEquals;

@Config(emulateSdk = 18)
@RunWith(RobolectricGradleTestRunner.class)
public class EncryptionTest {

    final String PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n"+
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoBttaXwRoI1Fbcond5mS\n"+
            "7QOb7X2lykY5hvvDeLJelvFhpeLnS4YDwkrnziM3W00UNH1yiSDU+3JhfHu5G387\n"+
            "O6uN9rIHXvL+TRzkVfa5iIjG+ap2N0/toPzy5ekpgxBicjtyPHEgoU6dRzdszEF4\n"+
            "ItimGk5ACx/lMOvctncS5j3uWBaTPwyn0hshmtDwClf6dEZgQvm/dNaIkxHKV+9j\n"+
            "Mn3ZfK/liT8A3xwaVvRzzuxf09xJTXrAd9v5VQbeWGxwFcW05oJulSFjmJA9Hcmb\n"+
            "DYHJT+sG2mlZDEruCGAzCVubJwGY1aRlcs9AQc1jIm/l8JwH7le2kpk3QoX+gz0w\n"+
            "WwIDAQAB\n"+
            "-----END PUBLIC KEY-----";


    @Before
    public void setup(){
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    public void generateHash() throws MoipEncryptionException {
        CreditCard creditCard = new CreditCard();
        creditCard.setCvc("123");
        creditCard.setNumber("4340948565343648");
        creditCard.setExpirationMonth("12");
        creditCard.setExpirationYear("2030");
        creditCard.setPublicKey(PUBLIC_KEY);

        assertEquals("123", creditCard.getCvc());
        assertEquals("4340948565343648", creditCard.getNumber());
        assertEquals("12", creditCard.getExpirationMonth());
        assertEquals("2030", creditCard.getExpirationYear());
        assertEquals(PUBLIC_KEY, creditCard.getPublicKey());

        creditCard.encrypt();
    }
}