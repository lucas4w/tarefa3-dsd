# Projeto: Monitoramento de sensores IoT com gRPC

Este é um sistema de demonstração desenvolvido para a disciplina de Desenvolvimento de Sistemas Distribuídos (DSD), lecionado pelo professor Gracon Huttennberg E. L. de Lima, que utiliza **gRPC** para estabelecer uma comunicação de alta performance entre um servidor central (monitor) e múltiplos clientes (sensores).

O projeto é um exemplo prático de uma arquitetura de microsserviços **poliglota**, com o servidor desenvolvido em **Java** e o cliente (simulador de sensor) em **Python**.

## Arquitetura do teste em Rede Local

Para os testes em sala de aula, operaremos no seguinte modelo:

-   **Um Servidor central:** executará o microsserviço do `servico-java-monitor`, um painel de monitoramento que receberá os dados de todos os sensores.
-   **Múltiplos Clientes:** colegas e professor executarão o microsserviço `servico-python-simulador` em suas próprias máquinas. Cada cliente atuará como um sensor IoT, enviando um fluxo contínuo de dados para o servidor central através da rede local.

```
                    +--------------------------------+
                    |            Servidor            |
                    |    IP: xxx.xxx.xxx.xxx:50051   |
                    | (Executando o Servidor Java)   |
                    +---------------^----------------+
                                    |
                  (Comunicação via gRPC na rede local)
                                    |
      +-----------------------------+-----------------------------+
      |                             |                             |
+-----v------+             +--------v-------+             +------v-----+
|  Cliente 1 |             |    Cliente 2   |             |  Cliente N |
| (Python)   |             |    (Python)    |             |  (Python)  |
+------------+             +----------------+             +------------+
```

<br>

## Instruções para os Clientes

Para participar do teste, você atuará como um "sensor". Siga os passos abaixo para configurar e executar o cliente Python em sua máquina.

### Pré-requisitos

Antes de começar, garanta que você tenha os seguintes programas instalados:

1.  **Git:** Para clonar o repositório.
2.  **Python:** Versão 3.8 ou superior.
3.  **Pip:** O gerenciador de pacotes do Python (geralmente já vem com o Python).

### Passo a Passo

1.  **Clone o Repositório:** Abra um terminal ou prompt de comando e execute o comando abaixo para baixar o projeto.
    ```bash
    git clone https://github.com/lucas-pinheiro-costa/tarefa3-dsd.git
    cd Atividade3/projeto-gRPC-IoT
    ```

2.  **Crie e ative um Ambiente Virtual:**
    ```bash
    # Cria o ambiente virtual
    python -m venv .venv

    # Ativa o ambiente virtual
    # No Windows:
    source .venv\Scripts\activate
    # No Linux ou macOS:
    source .venv/bin/activate
    ```
    > [!NOTE]
    > Após ativar, você deverá ver um `(.venv)` no início da linha de seu terminal.

3.  **Instale as dependências Python:** Instale as bibliotecas necessárias, incluindo o gRPC.
    ```bash
    # Instale os pacotes
    pip install -r requirements.txt
    ```

4.  **Configure o IP do Servidor:** Esta é a etapa mais importante! Você precisa dizer ao seu cliente para onde enviar os dados.
    -   Abra o arquivo `sensor_client.py` com um editor.
    -   Encontre a linha que contém `grpc.insecure_channel`.
    -   Substitua `'localhost:50051'` pelo **endereço IP do Servidor a ser fornecido**, mantendo a porta `50051`.

    ```python
    # Linha original
    # with grpc.insecure_channel('localhost:50051') as channel:

    # Exemplo de como deve ficar (substitua pelo IP fornecido)
    with grpc.insecure_channel('XXX.XXX.X.XXX:50051') as channel:
    ```

5.  **Execute o Cliente:** com tudo configurado, execute o script!
    ```bash
    python servico-python-simulador/sensor_client.py
    ```

6.  **Verifique a saída:** seu terminal começará a exibir mensagens indicando o envio dos dados do sensor. Parabéns, você está conectado ao servidor e enviando dados em tempo real!

    ```
    Conectado ao servidor gRPC. Iniciando o envio de dados...
    Iniciando simulação do sensor 'cozinha-temp-1'. Pressione Ctrl+C para parar.
      -> Enviando: Temp=28.12°C, Umid=45.91%
      -> Enviando: Temp=33.45°C, Umid=51.22%
    ```

<br>

## Instruções para execução do Servidor

Para iniciar o servidor na sua máquina e permitir que os clientes se conectem, siga estes passos.

### Pré-requisitos
-   JDK (Java Development Kit) 11 ou superior.
-   Maven.

### Passo a Passo

1.  **Encontre seu Endereço IP Local:** os clientes precisarão deste endereço.
    -   No **Windows**, abra o `cmd` e digite `ipconfig`. Procure pelo "Endereço IPv4".
    -   No **Linux** ou **macOS**, abra o terminal e digite `ifconfig` ou `ip a`. Procure pelo seu endereço de rede local (geralmente começa com `192.168`, `10.0` ou `172.16`).

2.  **Navegue para a pasta do Servidor:** Em um terminal, na raiz do projeto, entre na pasta do serviço Java.
    ```bash
    cd Atividade3/projeto-gRPC-IoT/servico-java-monitor
    ```

4.  **Execute o Servidor Java:**
    ```bash
    mvn exec:java -Dexec.mainClass="br.com.grpc.iot.MonitorServer"
    ```

5.  **Monitore as conexões:** Seu terminal mostrará que o servidor está no ar. Conforme os clientes se conectarem e enviarem dados, as leituras dos sensores aparecerão em tempo real no seu console.

<br>

## Autores

1. **Lucas de Moraes dos Santos e** 
2. **Lucas Pinheiro Costa**