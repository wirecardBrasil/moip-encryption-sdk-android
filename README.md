---
title: Mobile SDK iOS (Beta)
anchor: mobile
---

Com o MoipSDK você pode receber pagamentos no seu aplicativo sem se preocupar com criptografia e de uma maneira fácil e simples.

Veja abaixo como integrar o seu app com o Moip.

### 1. Iniciar o SDK

O primeiro passo iniciar o SDK passando seu Token, Key, Chave Publica RSA e o endpoint para criação da order do seu ecommerce.

```objective-c

#import <MoipSDK/MoipSDK.h>
#import <MoipSDK/MPKMessage.h>

@interface PaymentViewController ()

@property MPKView *paymentView;
@property MPKCreditCard *card;

@end

@implementation PaymentViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    NSString *path = [[NSBundle mainBundle] pathForResource:@"pk" ofType:@"txt"];
    NSString *publicKeyText = [NSString stringWithContentsOfFile:path
                                                        encoding:NSUTF8StringEncoding
                                                           error:nil];
    
    [MoipSDK startSessionWithToken:TOKEN
                               key:KEY
                         publicKey:publicKeyText
                       environment:MPKEnvironmentSANDBOX];
}
```

### 2. Criar o pedido (ORDER)

Para criar o pedido usando o MoipSDK, é necessario especificar o endpoint para criação do pedido no seu ecommerce, e o seu ecommerce deve repassar o json de order para o Moip.

```objective-c

- (void) createOrder
{
    [self showHud:@"Criando Pedido"];
    
    MPKCustomer *customer = [MPKCustomer new];
    customer.fullname = @"José Silva";
    customer.email = @"jose@gmail.com";
    customer.phoneAreaCode = 11;
    customer.phoneNumber = 999999999;
    customer.birthDate = [NSDate date];
    customer.documentType = MPKDocumentTypeCPF;
    customer.documentNumber = 99999999999;
    
    MPKAmount *amount = [MPKAmount new];
    amount.shipping = 1000;
    amount.addition = 0;
    amount.discount = 0;
    
    NSMutableArray *mpkItems = [NSMutableArray new];
    for (Product *p in self.productList)
    {
        MPKItem *newItem = [MPKItem new];
        newItem.product = p.name;
        newItem.quantity = p.quantity;
        newItem.detail = p.detail;
        newItem.price = p.amount;
        
        [mpkItems addObject:newItem];
    }
    
    MPKOrder *newOrder = [MPKOrder new];
    newOrder.ownId = @"sandbox_OrderID_xxx";
    newOrder.amount = amount;
    newOrder.items = mpkItems;
    newOrder.customer = customer;
    
    NSMutableURLRequest *rq = [NSMutableURLRequest new];
    rq.HTTPMethod = @"POST";
    rq.URL = [NSURL URLWithString:@"https://api.seuecommerce.com.br/criapedido"];

    [[MoipSDK session] createOrder:rq order:newOrder success:^(MPKOrder *order, NSString *moipOrderId) {
        NSLog(@"Order Created at Moip: %@", moipOrderId);
        [self createPayment:moipOrderId];
    } failure:^(NSArray *errorList) {
        [self hideHud];
        [self showErrorFeedback:errorList];
    }];
}

```


### 3. Usar o componente de Cartão de Credito Moip

A classe ```MPKView``` é responsável por capturar o número do cartão de credito, data de validade e também o CVC. Essa classe já criptografa os dados do cartão com a chave publica que você adicionou no passo anterior.

```objective-c
	// Adicionando a MPKView no seu formulário de pagamento.
    self.paymentView = [[MPKView alloc] initWithFrame:CGRectMake(5, 5, 300, 55) borderStyle:MPKViewBorderStyleNone delegate:self];
    self.paymentView.defaultTextFieldFont = DEFAULT_FONT;
    [self.view addSubview:self.paymentView];
    
```

##### 3.1 Delegate

Após o preenchimento do número, data de validade e cvc do cartão, se o cartão for valido, o método ```- (void)paymentViewWithCard:(MPKCreditCard *)aCard isValid:(BOOL)valid``` receberá os dados do cartão.

```objective-c
#pragma mark -
#pragma mark MPKViewDelegate
- (void)paymentViewWithCard:(MPKCreditCard *)aCard isValid:(BOOL)valid
{
    self.card = aCard;
    if (valid)
    {
        [self resignFirstResponder];
        
        [UIView animateWithDuration:0.3f animations:^{
            self.btnPayment.alpha = 1;
        }];
    }
}
```

### 4. Efetuar o pagamento

Após o preenchimento do formulário de pagamento, você já pode enviar os dados para o Moip efetuar a transação.

```objective-c



- (void) createPayment:(NSString *)moipOrderId
{
    MPKCardHolder *holder = [MPKCardHolder new];
    holder.fullname = @"José da Silva";
    holder.birthdate = @"1980-01-22";
    holder.documentType = MPKDocumentTypeCPF;
    holder.documentNumber = @"99999999999";
    holder.phoneCountryCode = @"55";
    holder.phoneAreaCode = @"11";
    holder.phoneNumber = @"999999999";
    
    self.card.cardholder = holder;
    
    MPKFundingInstrument *instrument = [MPKFundingInstrument new];
    instrument.creditCard = self.card;
    instrument.method = MPKMethodTypeCreditCard;
    
    MPKPayment *payment = [MPKPayment new];
    payment.moipOrderId = @"ORD-123456789";
    payment.installmentCount = 1;
    payment.fundingInstrument = instrument;
    
    [[MoipSDK session] submitPayment:payment success:^(MPKPaymentTransaction *transaction) {

        [self showSuccessFeedback:transaction];
        
    } failure:^(NSArray *errorList) {
        [self showErrorFeedback:errorList];
    }];
}	

```
##### 4.1 Mostrando mensagens de erro e sucesso

```objective-c
- (void) showSuccessFeedback:(MPKPaymentTransaction *)transaction
{
    NSString *message = @"Seu pagamento foi criado com sucesso!";
    if (transaction.status == MPKPaymentStatusAuthorized)
    {
        message = @"Seu pagamento foi autorizado com sucesso!";
    }
    else if (transaction.status == MPKPaymentStatusConcluded)
    {
        message = @"Seu pagamento foi concluido com sucesso!";
    }
    else if (transaction.status == MPKPaymentStatusInAnalysis)
    {
        message = @"Seu pagamento foi criado e está em Analise";
    }

    [MPKMessage showNotificationInViewController:self
                                           title:@"Pagamento criado"
                                        subtitle:message
                                            type:MPKMessageNotificationTypeSuccess
                                        duration:7.20f];
}

- (void) showErrorFeedback:(NSArray *)errors
{
    NSMutableString *errorMessage = [NSMutableString string];
    for (MPKError *er in errors)
    {
        [errorMessage appendFormat:@"%@\n", er.localizedFailureReason];
    }
    
    [MPKMessage showNotificationInViewController:self
                                           title:@"Oops! Ocorreu um imprevisto..."
                                        subtitle:errorMessage
                                            type:MPKMessageNotificationTypeWarning
                                        duration:7.0f];
}
	
```