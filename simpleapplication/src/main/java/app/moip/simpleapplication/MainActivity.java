package app.moip.simpleapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

import br.com.moip.encryption.entities.CreditCard;
import br.com.moip.encryption.exception.MoipEncryptionException;

public class MainActivity extends AppCompatActivity {

    final String PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n"+
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoBttaXwRoI1Fbcond5mS\n"+
            "7QOb7X2lykY5hvvDeLJelvFhpeLnS4YDwkrnziM3W00UNH1yiSDU+3JhfHu5G387\n"+
            "O6uN9rIHXvL+TRzkVfa5iIjG+ap2N0/toPzy5ekpgxBicjtyPHEgoU6dRzdszEF4\n"+
            "ItimGk5ACx/lMOvctncS5j3uWBaTPwyn0hshmtDwClf6dEZgQvm/dNaIkxHKV+9j\n"+
            "Mn3ZfK/liT8A3xwaVvRzzuxf09xJTXrAd9v5VQbeWGxwFcW05oJulSFjmJA9Hcmb\n"+
            "DYHJT+sG2mlZDEruCGAzCVubJwGY1aRlcs9AQc1jIm/l8JwH7le2kpk3QoX+gz0w\n"+
            "WwIDAQAB\n"+
            "-----END PUBLIC KEY-----";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Security.addProvider(new BouncyCastleProvider());

        CreditCard creditCard = new CreditCard();
        creditCard.setCvc("123");
        creditCard.setNumber("4340948565343648");
        creditCard.setExpirationMonth("12");
        creditCard.setExpirationYear("2030");
        creditCard.setPublicKey(PUBLIC_KEY);

        try{
            String encryptedCreditCard = creditCard.encrypt();
            Log.i("encryptedCreditCard", encryptedCreditCard);
        } catch (MoipEncryptionException mee) {
            Log.e("MoipEncryptionException", mee.getLocalizedMessage());
        }
    }

}
