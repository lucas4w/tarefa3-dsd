import grpc
import time
import random
from datetime import datetime
from google.protobuf.timestamp_pb2 import Timestamp

# Importa as classes geradas pelo gRPC
import contrato_pb2
import contrato_pb2_grpc

# O ID do nosso sensor simulado
SENSOR_ID = "cozinha-temp-1"

def gerar_dados_sensor():
    """
    Esta função é um gerador (generator). Ela 'yields' (produz) um novo dado de sensor
    a cada 2 segundos, simulando um fluxo contínuo.
    """
    print(f"Iniciando simulação do sensor '{SENSOR_ID}'. Pressione Ctrl+C para parar.")
    while True:
        try:
            # Gera dados aleatórios para temperatura e umidade
            temperatura = random.uniform(20.0, 35.0)
            umidade = random.uniform(40.0, 60.0)
            
            # Pega o timestamp atual
            now = datetime.utcnow()
            ts = Timestamp()
            ts.FromDatetime(now)

            # Cria a mensagem SensorData
            dado = contrato_pb2.SensorData(
                sensor_id=SENSOR_ID,
                temperatura=temperatura,
                umidade=umidade,
                timestamp=ts
            )
            
            print(f"  -> Enviando: Temp={temperatura:.2f}°C, Umid={umidade:.2f}%")
            
            # 'yield' produz o valor para o stream
            yield dado
            
            # Espera 2 segundos para a próxima leitura
            time.sleep(2)
        except KeyboardInterrupt:
            print("\nSimulação interrompida pelo usuário.")
            return # Finaliza o gerador

def run():
    """
    Função principal que configura e executa o cliente gRPC.
    """
    # Cria um canal de comunicação com o servidor.
    # 'localhost:50051' deve ser o endereço e a porta do servidor Java.
    with grpc.insecure_channel('localhost:50051') as channel:
        # Cria um 'stub' (cliente) para o nosso serviço.
        stub = contrato_pb2_grpc.MonitorServiceStub(channel)
        
        print("Conectado ao servidor gRPC. Iniciando o envio de dados...")
        
        # Chama o método remoto, passando nosso gerador de dados como argumento.
        # O gRPC irá, por baixo dos panos, pegar cada item produzido pelo gerador
        # e enviar para o servidor como parte do stream.
        gerador_de_dados = gerar_dados_sensor()
        resposta_servidor = stub.EnviarDadosSensor(gerador_de_dados)
        
        # Após o stream ser finalizado (pelo Ctrl+C), imprime a resposta final do servidor.
        print("\n--- RESPOSTA FINAL DO SERVIDOR ---")
        print(f"Mensagem: {resposta_servidor.mensagem}")
        print(f"Total de leituras recebidas: {resposta_servidor.total_leituras_recebidas}")
        print("---------------------------------")


if __name__ == '__main__':
    run()