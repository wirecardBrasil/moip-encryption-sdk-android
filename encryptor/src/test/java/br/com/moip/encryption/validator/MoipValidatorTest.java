package br.com.moip.encryption.validator;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import br.com.moip.encryption.entities.types.CreditCardBrand;
import br.com.moip.encryption.helper.MoipValidator;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class MoipValidatorTest {

    private static final String VALID_CVC = "123";
    private static final String INVALID_CVC = "123123";

    private static final String VISA = "4340948565343648";
    private static final String MASTER = "5592653853140092";
    private static final String AMEX = "379222464259053";
    private static final String DINERS = "30111122223331";
    private static final String ELO = "6362979350887844";
    private static final String INVALID_CC = "111111111111111";

    @Test
    public void shouldBeValidMonth() {
        isvValidMonth("1");
        isvValidMonth("5");
        isvValidMonth("08");
        isvValidMonth("10");
        isvValidMonth("12");
    }

    @Test
    public void shouldBeInvalidMonth() {
        isvInvalidMonth("-1");
        isvInvalidMonth("50");
        isvInvalidMonth("13");
        isvInvalidMonth("-10");
    }

    @Test
    public void shouldBeValidYear() {
        isValidYear("08");
        isValidYear("2300");
        isValidYear("12");
        isValidYear("2010");
        isValidYear("1005");
    }

    @Test
    public void shouldBeInvalidYear() {
        isInvalidYear("1");
        isInvalidYear("492");
        isInvalidYear("-14");
        isInvalidYear("3010");
        isInvalidYear("0003");
        isInvalidYear("008");
    }

    @Test
    public void shouldBeValidCreditCardNumber() {
        isValidCreditCardNumber(VISA);
        isValidCreditCardNumber(MASTER);
        isValidCreditCardNumber(AMEX);
        isValidCreditCardNumber(DINERS);
        isValidCreditCardNumber(ELO);
        isInvalidCreditCardNumber(INVALID_CC);
    }

    @Test
    public void verifyCVC() {
        isValidCVC(VALID_CVC);
        isInvalidCVC(INVALID_CVC);
    }

    @Test
    public void verifyBrand() {
        isVisa(VISA);
        isMaster(MASTER);
        isAmex(AMEX);
        isDiners(DINERS);
        isElo(ELO);
        isUnknow(INVALID_CC);
    }

    public void isvValidMonth(final String expirationMonth) {
        assertTrue(MoipValidator.isValidMonth(expirationMonth));
    }

    public void isvInvalidMonth(final String expirationMonth) {
        assertFalse(MoipValidator.isValidMonth(expirationMonth));
    }

    public void isValidYear(final String expirationYear) {
        assertTrue(MoipValidator.isValidYear(expirationYear));
    }

    public void isInvalidYear(final String expirationYear) {
        assertFalse(MoipValidator.isValidYear(expirationYear));
    }

    public void isValidCVC(final String cvv) {
        assertTrue(MoipValidator.isValidCVC(cvv));
    }

    public void isInvalidCVC(final String cvc) {
        assertFalse(MoipValidator.isValidCVC(cvc));
    }

    public void isValidCreditCardNumber(final String creditCardNumber) {
        assertTrue(MoipValidator.isValidCreditCard(creditCardNumber));
    }

    public void isInvalidCreditCardNumber(final String creditCardNumber) {
        assertFalse(MoipValidator.isValidCreditCard(creditCardNumber));
    }

    public void isVisa(final String creditCardNumber) {
        assertEquals(CreditCardBrand.VISA, MoipValidator.verifyBrand(creditCardNumber));
        assertEquals(CreditCardBrand.VISA, MoipValidator.quicklyBrand(creditCardNumber));
    }

    public void isMaster(final String creditCardNumber) {
        assertEquals(CreditCardBrand.MASTERCARD, MoipValidator.verifyBrand(creditCardNumber));
        assertEquals(CreditCardBrand.MASTERCARD, MoipValidator.quicklyBrand(creditCardNumber));
    }

    public void isAmex(final String creditCardNumber) {
        assertEquals(CreditCardBrand.AMEX, MoipValidator.verifyBrand(creditCardNumber));
        assertEquals(CreditCardBrand.AMEX, MoipValidator.quicklyBrand(creditCardNumber));
    }

    public void isDiners(final String creditCardNumber) {
        assertEquals(CreditCardBrand.DINERS, MoipValidator.verifyBrand(creditCardNumber));
        assertEquals(CreditCardBrand.DINERS, MoipValidator.quicklyBrand(creditCardNumber));
    }

    public void isElo(final String creditCardNumber) {
        assertEquals(CreditCardBrand.ELO, MoipValidator.verifyBrand(creditCardNumber));
        assertEquals(CreditCardBrand.ELO, MoipValidator.quicklyBrand(creditCardNumber));
    }

    public void isJCB(final String creditCardNumber) {
        assertEquals(CreditCardBrand.JCB, MoipValidator.verifyBrand(creditCardNumber));
        assertEquals(CreditCardBrand.JCB, MoipValidator.quicklyBrand(creditCardNumber));
    }

    public void isUnknow(final String creditCardNumber) {
        assertEquals(CreditCardBrand.UNKNOWN, MoipValidator.verifyBrand(creditCardNumber));
        assertEquals(CreditCardBrand.UNKNOWN, MoipValidator.quicklyBrand(creditCardNumber));
    }
}