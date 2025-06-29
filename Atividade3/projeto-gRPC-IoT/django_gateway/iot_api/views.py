import grpc
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
import json
from google.protobuf.json_format import MessageToJson, Parse  # Para converter Protobuf para JSON e vice-versa
from google.protobuf.timestamp_pb2 import Timestamp
from datetime import datetime

# Importa as classes gRPC geradas
from .grpc_client import contrato_pb2
from .grpc_client import contrato_pb2_grpc

# Endereço do seu servidor gRPC Java
GRPC_SERVER_ADDRESS = 'localhost:50051'

# Helper para obter o stub gRPC (reutilizável)
def get_grpc_stub():
    channel = grpc.insecure_channel(GRPC_SERVER_ADDRESS)
    stub = contrato_pb2_grpc.MonitorServiceStub(channel)
    return stub, channel

class UserRegistrationView(APIView):
    """
    Endpoint para registrar um novo usuário.
    Recebe POST: /api/users/register/
    """
    def post(self, request):
        email = request.data.get('email')
        nome = request.data.get('nome')

        if not email or not nome:
            return Response({"error": "Email e nome são obrigatórios."}, status=status.HTTP_400_BAD_REQUEST)

        stub, channel = get_grpc_stub()
        try:
            grpc_request = contrato_pb2.RegistrarUsuarioRequest(email=email, nome=nome)
            grpc_response = stub.RegistrarUsuario(grpc_request)
            
            # Converte a resposta gRPC para JSON para o cliente React
            # MessageToJson é útil para converter para JSON
            response_data = {
                "mensagem": grpc_response.mensagem,
                "sucesso": grpc_response.sucesso,
                "usuario_id": grpc_response.usuario_id # Note: IDs int64 são strings em JSON
            }
            return Response(response_data, status=status.HTTP_200_OK)

        except grpc.RpcError as e:
            error_message = f"Erro gRPC ao registrar usuário: {e.details}"
            return Response({"error": error_message}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        finally:
            channel.close()

class SensorRegistrationView(APIView):
    """
    Endpoint para registrar um novo sensor.
    Recebe POST: /api/sensors/register/
    """
    def post(self, request):
        try:
            usuario_id = int(request.data.get('usuario_id')) # Recebe como int
            nome = request.data.get('nome')
            descricao = request.data.get('descricao', '') # Opcional
        except (ValueError, TypeError):
            return Response({"error": "ID do usuário inválido ou campos ausentes."}, status=status.HTTP_400_BAD_REQUEST)

        if not nome:
            return Response({"error": "Nome do sensor é obrigatório."}, status=status.HTTP_400_BAD_REQUEST)

        stub, channel = get_grpc_stub()
        try:
            grpc_request = contrato_pb2.RegistrarSensorRequest(
                usuario_id=usuario_id,
                nome=nome,
                descricao=descricao
            )
            grpc_response = stub.RegistrarSensor(grpc_request)
            
            response_data = {
                "mensagem": grpc_response.mensagem,
                "sucesso": grpc_response.sucesso,
                "sensor_id": grpc_response.sensor_id
            }
            return Response(response_data, status=status.HTTP_200_OK)

        except grpc.RpcError as e:
            error_message = f"Erro gRPC ao registrar sensor: {e.details}"
            return Response({"error": error_message}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        finally:
            channel.close()

class SensorDataSendView(APIView):
    """
    Endpoint para enviar dados de leitura de sensor.
    Recebe POST: /api/sensors/data/send/
    """
    def post(self, request):
        sensor_id = request.data.get('sensor_id')
        temperatura = request.data.get('temperatura')
        umidade = request.data.get('umidade')
        
        # Validação básica
        if not sensor_id or temperatura is None or umidade is None:
            return Response({"error": "sensor_id, temperatura e umidade são obrigatórios."}, status=status.HTTP_400_BAD_REQUEST)
        
        try:
            temperatura = float(temperatura)
            umidade = float(umidade)
        except ValueError:
            return Response({"error": "Temperatura e umidade devem ser números válidos."}, status=status.HTTP_400_BAD_REQUEST)

        stub, channel = get_grpc_stub()
        try:
            now = datetime.utcnow()
            ts = Timestamp()
            ts.FromDatetime(now)

            grpc_request = contrato_pb2.SensorData(
                sensor_id=sensor_id,
                temperatura=temperatura,
                umidade=umidade,
                timestamp=ts
            )
            grpc_response = stub.EnviarDadosSensor(grpc_request)
            
            response_data = {
                "mensagem": grpc_response.mensagem,
                "sucesso": grpc_response.sucesso,
                "total_leituras_recebidas": grpc_response.total_leituras_recebidas
            }
            return Response(response_data, status=status.HTTP_200_OK)

        except grpc.RpcError as e:
            error_message = f"Erro gRPC ao enviar dados do sensor: {e.details}"
            return Response({"error": error_message}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        finally:
            channel.close()

class ListUserSensorsView(APIView):
    """
    Endpoint para listar sensores de um usuário.
    Recebe GET: /api/users/<int:user_id>/sensors/
    """
    def get(self, request, user_id):
        stub, channel = get_grpc_stub()
        try:
            grpc_request = contrato_pb2.ListarSensoresRequest(usuario_id=user_id)
            grpc_response = stub.ListarSensores(grpc_request)
            
            # A resposta já vem com a lista de objetos SensorInfo, podemos convertê-los
            # diretamente para uma lista de dicionários Python.
            sensors_list = []
            for sensor_info in grpc_response.sensores:
                sensors_list.append({
                    "sensor_id": sensor_info.sensor_id,
                    "nome": sensor_info.nome,
                    "descricao": sensor_info.descricao
                })

            response_data = {
                "mensagem": grpc_response.mensagem,
                "sucesso": grpc_response.sucesso,
                "sensores": sensors_list
            }
            return Response(response_data, status=status.HTTP_200_OK)

        except grpc.RpcError as e:
            error_message = f"Erro gRPC ao listar sensores: {e.details}"
            return Response({"error": error_message}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        finally:
            channel.close()

class GetLatestSensorDataView(APIView):
    """
    Endpoint para obter a última leitura de um sensor.
    Recebe GET: /api/sensors/<str:sensor_id>/latest-data/
    """
    def get(self, request, sensor_id):
        stub, channel = get_grpc_stub()
        try:
            grpc_request = contrato_pb2.DadosRequest(sensor_id=sensor_id)
            grpc_response = stub.GetDados(grpc_request)

            response_data = {
                "mensagem": grpc_response.mensagem,
                "sucesso": grpc_response.sucesso,
                "sensor_id_encontrado": grpc_response.sensor_id_encontrado,
                "temperatura_encontrada": grpc_response.temperatura_encontrada,
                "umidade_encontrada": grpc_response.umidade_encontrada,
                # Timestamps em Protobuf são objetos com seconds e nanos.
                # Para JSON, geralmente queremos uma string formatada ou epoch milis/segundos.
                # MessageToJson cuidaria disso se fosse o objeto Protobuf completo.
                # Aqui, vamos formatar manualmente para uma string ISO 8601, que é um padrão comum para web.
                "timestamp_encontrado": None # Inicializa como None
            }
            if grpc_response.HasField('timestamp_encontrado'): # Verifica se o campo foi setado
                 dt_object = grpc_response.timestamp_encontrado.ToDatetime()
                 response_data["timestamp_encontrado"] = dt_object.isoformat() + "Z" # Formato ISO 8601 UTC

            return Response(response_data, status=status.HTTP_200_OK)

        except grpc.RpcError as e:
            error_message = f"Erro gRPC ao obter última leitura do sensor: {e.details}"
            return Response({"error": error_message}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        finally:
            channel.close()

class GetUserByEmailView(APIView):
    """
    Endpoint para consultar usuário por email.
    Recebe GET: /api/users/by-email/
    Query param: email
    """
    def get(self, request):
        email = request.query_params.get('email')
        if not email:
            return Response({"error": "Email é obrigatório na query string."}, status=status.HTTP_400_BAD_REQUEST)

        stub, channel = get_grpc_stub()
        try:
            grpc_request = contrato_pb2.UserData(email=email)
            grpc_response = stub.GetUser(grpc_request)
            
            response_data = {
                "sucesso": grpc_response.sucesso,
                "usuario_id_encontrado": grpc_response.usuario_id,
            }
            return Response(response_data, status=status.HTTP_200_OK)

        except grpc.RpcError as e:
            error_message = f"Erro gRPC ao consultar usuário por email: {e.details}"
            return Response({"error": error_message}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        finally:
            channel.close()
