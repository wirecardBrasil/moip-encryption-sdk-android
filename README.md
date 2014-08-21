---
title: Mobile SDK Android
anchor: mobile-sdk-android
category: business
isChild: true
---
Estamos trabalhando para disponibilizar o quanto antes o **SDK Android sample**, mas você já pode se familiarizar com sua estrutura.

Veja abaixo o tutorial passo-a-passo como integrar com o SDK para Android.

###1. Iniciando o SDK

O primeiro passo é iniciar o SDK passando seu Token, Key, Chave Publica RSA e o endpoint para criação da order do seu ecommerce.

```java
	Moip moip = new Moip(Environment.SANDBOX, TOKEN, KEY, PUBLIC_KEY);
```


###2. Capturando os dados do pagamento

```java
    Payment payment = new Payment();
    payment.setMoipOrderID(moipOrderID);
    payment.setInstallmentCount(1);

    CreditCard creditCard = new CreditCard();
    creditCard.setExpirationMonth(1);
    creditCard.setExpirationYear(17);


    try {
        KeyManager.clearInstance();
        KeyManager.newInstance(PUBLIC_KEY);
        creditCard.setNumber(KeyManager.getInstance().encrypt("4340948565343648"));
        creditCard.setCvc(KeyManager.getInstance().encrypt("123"));

    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

    Holder holder = new Holder();
    holder.setBirthdate("1990-7-24");
    holder.setFullname("Hip Bot");

    //Tax Document
    TaxDocument taxDocument = new TaxDocument();
    taxDocument.setNumber("40328944011");
    taxDocument.setType(DocumentType.CPF.getDescription());

    Phone phone = new Phone();
    phone.setCountryCode(55);
    phone.setAreaCode(11);
    phone.setNumber("955555555");


    holder.setTaxDocument(taxDocument);
    holder.setPhone(phone);
    creditCard.setHolder(holder);

    FundingInstrument fundingInstrument = new FundingInstrument();
    fundingInstrument.setCreditCard(creditCard);
    fundingInstrument.setMethod("CREDIT_CARD");

    payment.setFundingInstrument(fundingInstrument);
```

###3. Criando o pagmento (PAYMENT)

Após o preenchimento do formulário de pagamento, você já pode enviar os dados para o Moip efetuar a transação.

```java   

	Moip moip = new Moip(Environment.SANDBOX, TOKEN, KEY, PUBLIC_KEY);
	
	moip.createPayment(payment, new MoipCallback<Payment>() {
        	@Override
                public void success(Payment payment) {
			//Lógica caso o pagamento tenha sido realizado com sucesso
                }

                @Override
                public void failure(List<MoipError> moipErrorList) {
			//Lógica caso haja algum erro ao criar o pagamento
                }
            });
```
