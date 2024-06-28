# Pix Payment Service

Este projeto é um serviço de pagamento Pix que permite a criação e o acompanhamento de pagamentos utilizando o MercadoPago. 
## Funcionalidades

- **Criação de Pagamentos Pix**: Permite a criação de pagamentos Pix via API REST.
- **Recebimento de Notificações**: Recebe notificações de atualização de status de pagamento via webhook.
- **Envio de Mensagens**: Integração com RabbitMQ para envio de mensagens de confirmação de pagamento.
- **Persistência de Dados**: Armazena informações de pagamento no banco de dados H2.
- **Geração de QR Code**: Gera e retorna o QR Code e link de pagamento para o cliente.

## Tecnologias Utilizadas

- **Spring Boot 3.3.1**
- **Java 17+**
- **MercadoPago SDK 2.1.24**
- **RabbitMQ**
- **H2 Database**
- **Docker e Docker Compose**

## Requisitos

- Java 17+
- Docker e Docker Compose

## Configuração e Execução

### Passo 1: Clone o Repositório

Clone este repositório para sua máquina local:

```sh
git clone https://github.com/Jaoow/pix-payment-service.git
cd pix-payment-service
```

### Passo 2: Configuração do Docker Compose

Utilize o Docker Compose para levantar o contêiner RabbitMQ:

```sh
docker-compose up -d
```

Isso iniciará o RabbitMQ e o painel de gerenciamento estará acessível em [http://localhost:15672](http://localhost:15672) (usuário: `guest`, senha: `guest`).

### Passo 3: Configuração do Spring Boot

Edite o arquivo `application.properties` em `src/main/resources/` e adicione o token de acesso da MercadoPago:

```properties
# Configurações do MercadoPago
mercadopago.access_token=YOUR_ACCESS_TOKEN
```

### Passo 4: Compilar e Executar o Serviço

Compile e execute o projeto usando o Maven:

```sh
./mvnw spring-boot:run
```

O serviço estará disponível em [http://localhost:8080](http://localhost:8080).

## Diagrama de Sequência
[![](https://mermaid.ink/img/pako:eNp9k8GOgjAQhl-l6VlfoAcSoxcPGlaz2QuXEUZshLa2gwkxvvsWC8YCuxwIZb6f-ecvffBcF8gFd3hrUOW4kVBaqDPF_GXAksylAUXs26Gdvv3B08qY6fsU2hoVHdHeZY7T-g5tDoVOodSrdDutb9P9Wiuyuqrm2h7gdJK0-8pUqHXmlkkS3Ai2VZIkEHrNy0aAQtVjsTnB1hY_YGa7KFwvilkvjp0LdkRVvKUFEsjKBWlMLmcaH5Aaq97qSqrrbNeP0f6U9NN5tAtDsI10poJ2hgz3vfYj6zvaV3iL4fvdguW6NhUSujjA2bGSJNorMXhnjoAaxxpT-HCDNCK9ctjGUYq9skbnoOylAzoX4z8dJ9v3nvMFjVqO_pMQpA9KntsRyRe8RluDLPzZeXS6jNMFa8y48I8F2GvGM_X0HDSkj63KuSDb4IJb3ZQXLs5QOb8KZvtTF5DnLziDPwc?type=png)](https://mermaid.live/edit#pako:eNp9k8GOgjAQhl-l6VlfoAcSoxcPGlaz2QuXEUZshLa2gwkxvvsWC8YCuxwIZb6f-ecvffBcF8gFd3hrUOW4kVBaqDPF_GXAksylAUXs26Gdvv3B08qY6fsU2hoVHdHeZY7T-g5tDoVOodSrdDutb9P9Wiuyuqrm2h7gdJK0-8pUqHXmlkkS3Ai2VZIkEHrNy0aAQtVjsTnB1hY_YGa7KFwvilkvjp0LdkRVvKUFEsjKBWlMLmcaH5Aaq97qSqrrbNeP0f6U9NN5tAtDsI10poJ2hgz3vfYj6zvaV3iL4fvdguW6NhUSujjA2bGSJNorMXhnjoAaxxpT-HCDNCK9ctjGUYq9skbnoOylAzoX4z8dJ9v3nvMFjVqO_pMQpA9KntsRyRe8RluDLPzZeXS6jNMFa8y48I8F2GvGM_X0HDSkj63KuSDb4IJb3ZQXLs5QOb8KZvtTF5DnLziDPwc)

## Utilização da API

### Criação de Pagamento Pix

Endpoint para criar um pagamento Pix:

- **URL**: `/api/pix/create`
- **Método**: `POST`
- **Conteúdo**: JSON

Exemplo de requisição:

```json lines
POST /api/pix/create HTTP/1.1
Content-Type: application/json

{
"amount": 100.00,
"email": "test@example.com",
"payerFirstName": "João",
"payerDocumentNumber": "12345678900",
"description": "Pagamento de Teste"
}
```

Exemplo de resposta:

```json
{
"paymentId": "123456789",
"status": "pending",
"qrCode": "00020126600014br.gov.bcb.pix...",
"qrCodeBase64": "iVBORw0KGgoAAAANSUhEUg...",
"ticketUrl": "https://www.mercadopago.com.br/payments/123456789/ticket..."
}
```

### Recebimento de Notificações de Pagamento

Endpoint para receber notificações de atualização de pagamento:

- **URL**: `/api/ipn`
- **Método**: `POST`
- **Conteúdo**: JSON

O MercadoPago enviará notificações para este endpoint com o status do pagamento. O sistema atualizará automaticamente o status no banco de dados.

## Desenvolvimento

### Estrutura do Projeto

- **Controller**:
    - `CreatePaymentController`: Responsável por lidar com requisições de criação de pagamento.
    - `PaymentNotificationController`: Responsável por lidar com notificações de pagamento (IPN).

- **Service**:
    - `PixPaymentService`: Contém a lógica para criar pagamentos e interagir com o MercadoPago.

- **Model**:
    - `PixPaymentRequest`: Modelo para requisições de criação de pagamento.
    - `PixPaymentResponse`: Modelo para respostas de pagamento.
    - `PixPayment`: Entidade que representa um pagamento Pix no banco de dados.

- **Repository**:
    - `PixPaymentRepository`: Interface JPA para acessar e persistir dados de pagamento.

### Como Contribuir

1. Faça um fork do projeto.
2. Crie uma nova branch (`git checkout -b feature/nova-funcionalidade`).
3. Faça commit das suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`).
4. Faça o push para a branch (`git push origin feature/nova-funcionalidade`).
5. Crie um Pull Request.

## Licença

Este projeto é licenciado sob a [MIT License](LICENSE).

