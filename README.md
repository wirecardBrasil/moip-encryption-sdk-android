---
# Mobile SDK Android (Beta)

Com o MoipSDK você pode receber pagamentos no seu aplicativo sem se preocupar com criptografia e de uma maneira fácil e simples.

Antes iniciar a integração de seu aplicativo com o SDK Mobile Android, entre no site do Moip. Lá você encontrará os primeiros passos para criar sua conta Moip.
https://moip.com.br/moip-apps/

---

##Release Notes

###### Versão 1.0 Beta 10 -17/04/2015
* Alterado diversos pontos do SDK. Adicionada a nova criptografia de cartão.

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

Para utilizar a SDK do Moip, antes é preciso adicionar as dependencias dela e o jar do moip-encryption. Há um zip com as depêndencias neste repositório.
Caso utilize o gradle adicione apenas as seguintes linhas.

```
    compile 'com.madgag.spongycastle:pkix:1.51.0.0'
```

Adicione o arquivo moip-encryption.aar dentro da pasta libs de seu projeto, e em seu build.gradle:

```
    repositories {
        flatDir {
            dirs 'libs'
        }
    }
```
E compile o sdk de criptografia:

```
    compile(name: 'moip-encryption', ext: 'aar')
```

###2 - Criando o Cartão de Crédito

##### 2.1 - Chave pública
```java
	 final String PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n"+
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoBttaXwRoI1Fbcond5mS\n"+
        "7QOb7X2lykY5hvvDeLJelvFhpeLnS4YDwkrnziM3W00UNH1yiSDU+3JhfHu5G387\n"+
        "O6uN9rIHXvL+TRzkVfa5iIjG+ap2N0/toPzy5ekpgxBicjtyPHEgoU6dRzdszEF4\n"+
        "ItimGk5ACx/lMOvctncS5j3uWBaTPwyn0hshmtDwClf6dEZgQvm/dNaIkxHKV+9j\n"+
        "Mn3ZfK/liT8A3xwaVvRzzuxf09xJTXrAd9v5VQbeWGxwFcW05oJulSFjmJA9Hcmb\n"+
        "DYHJT+sG2mlZDEruCGAzCVubJwGY1aRlcs9AQc1jIm/l8JwH7le2kpk3QoX+gz0w\n"+
        "WwIDAQAB\n"+
        "-----END PUBLIC KEY-----";
```

##### 2.2 - Objeto CreditCard
```java
	CreditCard creditCard = new CreditCard();
        creditCard.setCvc("123");
        creditCard.setNumber("4340948565343648");
        creditCard.setExpirationMonth("12");
        creditCard.setExpirationYear("2030");
        creditCard.setPublicKey(PUBLIC_KEY);
```

###3 - Criptografando o cartão

```java
	try{
		creditCard.encrypt();
	}catch(MoipEncryptionException mee){
	
	}
```

#Validações

O SDK Moip contém diversas validações para checar os dados de cartão.

```java
	MoipValidator.isValidMonth(MONTH);
	MoipValidator.isValidYear(YEAR);
	MoipValidator.isValidCreditCard(CREDIT_CARD_NUMBER);
	MoipValidator.isValidCVC(CVC_NUMBER);
```
O SDK Moip também contém métodos para verificarem a bandeira do cartão.

```java
	CreditCardBrand brand = MoipValidator.verifyBrand(CREDIT_CARD_NUMBER);
```

Também é possivel verificar a bandeira do cartão com apenas os 4 primeiros dígitos.

```java
	CreditCardBrand brand = MoipValidator.quicklyBrand(CREDIT_CARD_NUMBER);
```
