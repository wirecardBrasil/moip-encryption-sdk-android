---
# Mobile SDK Android (Beta)

Com o MoipSDK você pode receber pagamentos no seu aplicativo sem se preocupar com criptografia e de uma maneira fácil e simples.

Antes iniciar a integração de seu aplicativo com o SDK Mobile Android, entre no site do Moip. Lá você encontrará os primeiros passos para criar sua conta Moip.
https://moip.com.br/moip-apps/

---

##Release Notes

###### Versão 1.0 Beta 9 -19/03/2015
* Atualização na chamada ao criar um order

###### Versão 1.0 Beta 8 -26/02/2015
* Correção na chamada da chave pública

###### Versão 1.0 Beta 7 - 04/02/2015
* Reescrita as assinaturas dos métodos
* Adicionado o login oauth
* Adicionada novas features para comunicação com o Moip
* Melhoria na performance

###### Versão 1.0 Beta 6 - 09/12/2014
* Adicionado a criação de order e order light
* Diversas atualizações

###### Versão 1.0 Beta 5 - 03/09/2014
* Corrigido um bug na criptografia nos dados do cartão

###### Versão 1.0 Beta 4 - 26/08/2014
* Adicionado a versão JAR da SDK
* Adicionado dependencias da SDK

###### Versão 1.0 Beta 3 - 24/08/2014
* Ajustes na criptografia

###### Versão 1.0 Beta 2 - 22/08/2014
* Ajustes para criação do pagamento com cartão criptografado

###### Versão 1.0 Beta 1 - 21/08/2014
* Melhorias nos componentes de cartão de crédito e cvc do Moip

#Como usar a SDK Android

Estamos trabalhando para disponibilizar o quanto antes o **SDK Android sample**, mas você já pode se familiarizar com sua estrutura.

Veja abaixo o tutorial passo-a-passo como integrar com a SDK para Android.

###1 - Dependências da SDK

Para utilizar a SDK do Moip, antes é preciso adicionar as dependencias dela. Há um zip com as depêndencias neste repositório.
Caso utilize o gradle adicione apenas as seguintes linhas.

```
    compile 'com.madgag.spongycastle:pkix:1.51.0.0'
    compile 'com.netflix.feign:feign-gson:6.1.2'
    compile 'commons-codec:commons-codec:1.9'
```


###2 - Iniciando a SDK

O primeiro passo é iniciar a SDK passando seu Token, Key, Chave Publica RSA e o ambiente que o pagamento será criado.

```java
	Moip.initialize(this);
 	Moip.Environment(Environment.SANDBOX);
 	Moip.AppInfo("APP-9ZYARRPIM0Y5", "plwhy5518vwhoo52tyvg6irc67en7ct");
```

###3 - Autenticando-se na SDK

A autenticação na SDK pode variar dependendo do tipo de sua aplicação e/ou negócio.

#####3.1 - Autenticação Basic Auth

```java
	Moip.BasicAuth("01010101010101010101010101010101","ABABABABABABABABABABABABABABABABABABABAB");
```
Adicionando a chave pública
```java
	Moip.PublicKey("-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoBttaXwRoI1Fbcond5mS
7QOb7X2lykY5hvvDeLJelvFhpeLnS4YDwkrnziM3W00UNH1yiSDU+3JhfHu5G387
O6uN9rIHXvL+TRzkVfa5iIjG+ap2N0/toPzy5ekpgxBicjtyPHEgoU6dRzdszEF4
ItimGk5ACx/lMOvctncS5j3uWBaTPwyn0hshmtDwClf6dEZgQvm/dNaIkxHKV+9j
Mn3ZfK/liT8A3xwaVvRzzuxf09xJTXrAd9v5VQbeWGxwFcW05oJulSFjmJA9Hcmb
DYHJT+sG2mlZDEruCGAzCVubJwGY1aRlcs9AQc1jIm/l8JwH7le2kpk3QoX+gz0w
WwIDAQAB
-----END PUBLIC KEY-----");
```
#####3.2 Autenticação Oauth 

Faça uma chamada para a activity de login.

```java
 Intent i = new Intent(this, MoipOAuthLoginActivity.class);
 startActivityForResult(i, MoipCode.OAUTH);
```

Para receber a  resposta do login em sua aplicação, sobrescreva o método onActivityResult da seguinte forma.

<img src="http://cdn2.hubspot.net/hub/253924/file-2448838267-png/Documentation/login_screen_mpos.png"height="400" />

Após o login será necessário permitir que sua aplicação possa criar pagamentos na conta Moip do vendedor.

<img src="http://cdn2.hubspot.net/hub/253924/file-2446806435-png/Documentation/login_screen_mpos_2.png"height="400" />

```java
 @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 
  if (requestCode == MoipCode.OAUTH) {
   if (resultCode == MoipCode.SUCCESS) {
      //Em caso de sucesso, você pode prosseguir para a tela de venda
   } 
  }
 }
```

###4 - Utilizando os componentes do Moip

Para adicionar os componentes de cartão de crédito e cvv do Moip a seu aplicativo, crie os seguintes campos em sua interface.
```xml
	<br.com.moip.sdk.components.MoipCreditCardEditText
            android:id="@+id/moip_credit_card_edit_text"
            android:layout_width="fill_parent"
            android:layout_height="wrap_content"
            android:hint="Credit Card"/>
            
        <br.com.moip.sdk.components.MoipCVCEditText
            android:id="@+id/moip_cvc_edit_text"
            android:layout_width="96dp"
            android:layout_height="wrap_content"
            android:hint="CVV" />
```

Declaração dos componentes

```java

	private MoipCreditCardEditText moipCC;
	private MoipCVCEditText moipCVC;
```

Inicializando os componentes

```java

        moipCVC = (MoipCVCEditText) findViewById(R.id.moip_cvc_edit_text);
        moipCC = (MoipCreditCardEditText) findViewById(R.id.moip_credit_card_edit_text);
```

###5 - Capturando os dados do Order


```java
	Order order = new Order();
        order.setOwnId("42");

        Amount amount = new Amount();
        amount.setCurrency(Currency.BRL);
        order.setAmount(amount);

        final Item item = new Item();
        item.setDetail("Uma linda bicicleta");
        item.setPrice(1900);
        item.setProduct("Bicicleta");
        item.setQuantity(1);

        order.setItems(new ArrayList<Item>() {{
            add(item);
        }});

        Customer customer = new Customer();
        customer.setOwnId("315");
        customer.setFullname("Jose Silva");
        customer.setEmail("josedasilva@email.com");
        customer.setBirthDate("1988-12-30");

        TaxDocument taxDocument = new TaxDocument();
        taxDocument.setType("CPF");
        taxDocument.setNumber("22222222222");
        customer.setTaxDocument(taxDocument);

        Phone phone = new Phone();
        phone.setNumber("66778899");
        phone.setAreaCode(11);
        phone.setCountryCode(55);
        customer.setPhone(phone);

        order.setCustomer(customer);
        
        ShippingAddress shippingAddress = new ShippingAddress();
     	shippingAddress.setStreet("Avenida Faria Lima");
     	shippingAddress.setStreetNumber("2927");
     	shippingAddress.setComplement("8");
     	shippingAddress.setDistrict("Itaim");
     	shippingAddress.setCity("São Paulo");
     	shippingAddress.setState("SP");
     	shippingAddress.setCountry("BRA");
     	shippingAddress.setZipCode("01234000");

     	order.setShippingAddress(shippingAddress);
```

###6 - Criando o Order

Após o preenchimento do formulário de pedido, lembre-se que o pedido deve ser enviado para seu servidor e de lá ser enviado para o Moip.

```java

	try{
	  order = Moip.createMyOrder(order, "https://endpointdomeuecommerce.com/pedidos");
	}catch(IOException e){
	
	}
```

Agora com o order criado no Moip, você pode pegar o id do order que será utilizado no payment.


###7 - Capturando os dados do payment

```java
        Payment payment = new Payment();
        payment.setMoipOrderID(order.getId());
        payment.setInstallmentCount(1);

        CreditCard creditCard = new CreditCard();
        creditCard.setExpirationMonth(1);
        creditCard.setExpirationYear(17);
        creditCard.setNumber(moipCC.getText().toString());
	creditCard.setCvc(moipCVC.getText().toString());


        Holder holder = new Holder();
        holder.setBirthdate("1990-7-24");
        holder.setFullname("Hip Bot");

        TaxDocument taxDocument = new TaxDocument();
        taxDocument.setNumber("40328944011");
        taxDocument.setType(DocumentType.CPF.getDescription());

        Phone phone = new Phone();
        phone.setCountryCode(55);
        phone.setAreaCode(11);
        phone.setNumber("955555555");


        holder.setTaxDocument(taxDocument);
        holder.setPhone(phone);

        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setStreet("Avenida Faria Lima");
        billingAddress.setStreetNumber("2927");
        billingAddress.setComplement("8");
        billingAddress.setDistrict("Itaim");
        billingAddress.setCity("São Paulo");
        billingAddress.setState("SP");
        billingAddress.setCountry("BRA");
        billingAddress.setZipCode("01234000");
        holder.setBillingAddress(billingAddress);

        creditCard.setHolder(holder);

        FundingInstrument fundingInstrument = new FundingInstrument();
        fundingInstrument.setCreditCard(creditCard);
        fundingInstrument.setMethod("CREDIT_CARD");

        payment.setFundingInstrument(fundingInstrument);
```


###8 - Criando o Payment

Após o preenchimento do formulário de pagamento, você já pode enviar os dados para o Moip efetuar a transação.

```java   
 Moip.createPayment(payment, new MoipCallback<Payment>() {
            @Override
            public void success(final Payment payment) {
              
            }

            @Override
            public void failure(final List<MoipError> moipErrorList) {

            }
        });
```

---
# FAQ

---

###  1- Como consigo um appId e um appSecret?
Entre em contato conosco pelo email suporte.mobile@moip.com.br
