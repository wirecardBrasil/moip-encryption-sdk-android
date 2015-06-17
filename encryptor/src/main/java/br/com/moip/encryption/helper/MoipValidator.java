package br.com.moip.encryption.helper;

import br.com.moip.encryption.entities.types.CreditCardBrand;

public class MoipValidator {

    public static boolean isValidCreditCard(final String ccNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(ccNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    public static CreditCardBrand verifyBrand(final String ccNumber) {
        if (ccNumber.matches("^4[0-9]{12}(?:[0-9]{3})?$"))
            return CreditCardBrand.VISA;
        else if (ccNumber.matches("^5[1-5][0-9]{14}$"))
            return CreditCardBrand.MASTERCARD;
        else if (ccNumber.matches("^3[47][0-9]{13}$"))
            return CreditCardBrand.AMEX;
        else if (ccNumber.matches("^3(?:0[0-5]|[68][0-9])[0-9]{11}$"))
            return CreditCardBrand.DINERS;
        else
            return CreditCardBrand.UNKNOWN;
    }

    public static CreditCardBrand quicklyBrand(final String ccNumber) {
        if (ccNumber.length() < 2) {
            return CreditCardBrand.UNKNOWN;
        }

        Integer range = Integer.valueOf(ccNumber.substring(0, 2));

        if (range >= 40 && range <= 49) {
            return CreditCardBrand.VISA;
        } else if (range >= 50 && range <= 59) {
            return CreditCardBrand.MASTERCARD;
        } else if (range == 34 || range == 37) {
            return CreditCardBrand.AMEX;
        } else if (range == 60 || range == 62 || range == 64 || range == 65) {
            return CreditCardBrand.DISCOVERY;
        } else if (range == 35) {
            return CreditCardBrand.JCB;
        } else if (range == 30 || range == 36 || range == 38 || range == 39) {
            return CreditCardBrand.DINERS;
        } else {
            return CreditCardBrand.UNKNOWN;
        }
    }

    public static boolean isValidCVC(final String cvc) {
        if (isNumber(cvc) && Integer.parseInt(cvc) >= 100 && Integer.parseInt(cvc) <= 9999)
            return true;
        else
            return false;
    }

    public static boolean isValidMonth(final String expirationMonth) {
        if (expirationMonth.length() != 1 && expirationMonth.length() != 2)
            return false;

        if(!isNumber(expirationMonth))
            return false;

        int month = Integer.parseInt(expirationMonth);

        if (month > 0 && month <= 12)
            return true;
        else
            return false;
    }

    public static boolean isValidYear(final String expirationYear) {
        if (expirationYear.length() != 2 && expirationYear.length() != 4)
            return false;

        if(!isNumber(expirationYear))
            return false;

        int year = Integer.parseInt(expirationYear);

        if (expirationYear.length() == 4) {
            if (year < 1000 || year >= 3000)
                return false;
        } else {
            if (year < 0 || year > 99)
                return false;
        }
        return true;
    }

    private static boolean isNumber(final String text) {
        if (text.matches("[0-9]+"))
            return true;
        else
            return false;
    }
}
