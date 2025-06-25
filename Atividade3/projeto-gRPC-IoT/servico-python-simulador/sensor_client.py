import grpc
import time
import random
from datetime import datetime
from google.protobuf.timestamp_pb2 import Timestamp

# Importa as classes geradas pelo gRPC
import contrato_pb2
import contrato_pb2_grpc

# Endereço e porta do servidor Java gRPC
SERVER_ADDRESS = 'localhost:50051'

def display_menu():
    """Exibe o menu de opções para o usuário."""
    print("\n--- Menu do Cliente gRPC IoT ---")
    print("1. Registrar Novo Usuário")
    print("2. Registrar Novo Sensor para um Usuário")
    print("3. Enviar Dados de Leitura para um Sensor")
    print("4. Sair")
    print("-------------------------------")

def register_user(stub):
    """
    Função para registrar um novo usuário via RPC RegistrarUsuario.
    """
    print("\n--- Registrar Novo Usuário ---")
    email = input("Digite o email do usuário: ")
    nome = input("Digite o nome do usuário: ")

    request = contrato_pb2.RegistrarUsuarioRequest(email=email, nome=nome)
    
    try:
        response = stub.RegistrarUsuario(request)
        print("\n--- RESPOSTA DO SERVIDOR (Registrar Usuário) ---")
        print(f"Mensagem: {response.mensagem}")
        print(f"Sucesso: {response.sucesso}")
        if response.sucesso:
            print(f"ID do Usuário: {response.usuario_id}")
            print(f"Por favor, guarde este ID para registrar sensores para este usuário.")
        print("---------------------------------------------")
    except grpc.RpcError as e:
        print(f"Erro ao registrar usuário: {e.details}")

def register_sensor(stub):
    """
    Função para registrar um novo sensor via RPC RegistrarSensor.
    """
    print("\n--- Registrar Novo Sensor ---")
    try:
        usuario_id = int(input("Digite o ID do usuário (numérico) para associar o sensor: "))
    except ValueError:
        print("ID de usuário inválido. Por favor, insira um número.")
        return

    nome = input("Digite um nome amigável para o sensor (ex: 'Sensor de Cozinha'): ")
    descricao = input("Digite uma breve descrição para o sensor (opcional): ")

    request = contrato_pb2.RegistrarSensorRequest(
        usuario_id=usuario_id,
        nome=nome,
        descricao=descricao
    )

    try:
        response = stub.RegistrarSensor(request)
        print("\n--- RESPOSTA DO SERVIDOR (Registrar Sensor) ---")
        print(f"Mensagem: {response.mensagem}")
        print(f"Sucesso: {response.sucesso}")
        if response.sucesso:
            print(f"ID do Sensor: {response.sensor_id}")
            print(f"Por favor, guarde este ID para enviar dados para este sensor.")
        print("------------------------------------------")
    except grpc.RpcError as e:
        print(f"Erro ao registrar sensor: {e.details}")

def generate_single_sensor_data(sensor_id_param):
    """
    Gera uma única mensagem SensorData com dados aleatórios.
    """
    temperatura = random.uniform(20.0, 35.0)
    umidade = random.uniform(40.0, 60.0)
    
    now = datetime.utcnow()
    ts = Timestamp()
    ts.FromDatetime(now)

    print(f"  -> Gerando dados para Sensor ID '{sensor_id_param}': Temp={temperatura:.2f}°C, Umid={umidade:.2f}%")
    
    return contrato_pb2.SensorData(
        sensor_id=sensor_id_param,
        temperatura=temperatura,
        umidade=umidade,
        timestamp=ts
    )

def send_sensor_data(stub):
    """
    Função para enviar dados de leitura para um sensor via RPC EnviarDadosSensor (unário).
    """
    print("\n--- Enviar Dados de Leitura para um Sensor ---")
    sensor_id = input("Digite o ID do sensor para o qual deseja enviar dados: ")

    sensor_data_message = generate_single_sensor_data(sensor_id)

    try:
        response = stub.EnviarDadosSensor(sensor_data_message) # Chamada unária
        print("\n--- RESPOSTA DO SERVIDOR (Enviar Dados) ---")
        print(f"Mensagem: {response.mensagem}")
        print(f"Sucesso: {response.sucesso}")
        print(f"Total de leituras processadas: {response.total_leituras_recebidas}")
        print("------------------------------------------")
    except grpc.RpcError as e:
        print(f"Erro ao enviar dados do sensor: {e.details}")

def run():
    """
    Função principal que configura e executa o cliente gRPC com menu interativo.
    """
    with grpc.insecure_channel(SERVER_ADDRESS) as channel:
        stub = contrato_pb2_grpc.MonitorServiceStub(channel)
        print(f"Conectado ao servidor gRPC em {SERVER_ADDRESS}.")

        while True:
            display_menu()
            choice = input("Escolha uma opção: ")

            if choice == '1':
                register_user(stub)
            elif choice == '2':
                register_sensor(stub)
            elif choice == '3':
                send_sensor_data(stub)
            elif choice == '4':
                print("Saindo...")
                break
            else:
                print("Opção inválida. Tente novamente.")
            
            time.sleep(1) # Pequena pausa para melhor leitura do terminal

if __name__ == '__main__':
    run()
