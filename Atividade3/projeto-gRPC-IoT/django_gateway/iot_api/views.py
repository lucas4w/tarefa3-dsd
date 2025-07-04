import grpc
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
import json
from google.protobuf.json_format import MessageToJson, Parse
from google.protobuf.timestamp_pb2 import Timestamp
from datetime import datetime

from .grpc_client import contrato_pb2
from .grpc_client import contrato_pb2_grpc

GRPC_SERVER_ADDRESS = 'localhost:50051'

def get_grpc_stub():
    channel = grpc.insecure_channel(GRPC_SERVER_ADDRESS)
    stub = contrato_pb2_grpc.MonitorServiceStub(channel)
    return stub, channel

class GenerateSensorDataView(APIView):
    """
    Endpoint para gerar dados aleatórios de sensores.
    Recebe POST: /api/sensors/<str:sensor_id>/generate-data/
    """
    def post(self, request, sensor_id):
        try:
            stub, channel = get_grpc_stub()
            grpc_request = contrato_pb2.GenerateDataRequest(sensor_id=sensor_id)
            grpc_response = stub.GenerateData(grpc_request)

            response_data = {
                "mensagem": grpc_response.mensagem,
                "sucesso": grpc_response.sucesso,
                "sensor_id": sensor_id,
                "temperatura": grpc_response.temperatura_gerada,
                "umidade": grpc_response.umidade_gerada,
                "timestamp": grpc_response.timestamp_gerado.ToDatetime().isoformat() + "Z" # Formato ISO 8601 UTC
            }
            return Response(response_data, status=status.HTTP_200_OK)

        except grpc.RpcError as e:
            error_message = f"Erro gRPC ao gerar dados do sensor: {e.details}"
            return Response({"error": error_message}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        finally:
            channel.close()

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
            usuario_id = int(request.data.get('usuario_id')) 
            nome = request.data.get('nome')
            descricao = request.data.get('descricao', '')
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
                "timestamp_encontrado": None
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

class SensorUpdateView(APIView):
    """
    Endpoint para atualizar um sensor existente.
    Recebe PUT: /api/sensors/<str:sensor_id>/
    """
    def put(self, request, sensor_id):
        # Campos que podem ser atualizados
        nome = request.data.get('nome')
        descricao = request.data.get('descricao')
        # Adicione outros campos do sensor que você permite atualizar

        # Validação básica
        if not nome and not descricao:
            return Response({"error": "Pelo menos 'nome' ou 'descricao' deve ser fornecido para atualização."},
                            status=status.HTTP_400_BAD_REQUEST)
        
        stub, channel = get_grpc_stub()
        try:
            # Cria a requisição gRPC para UpdateSensor
            # Note que os campos nome e descricao são passados condicionalmente
            # O gRPC protobuf Python gera automaticamente métodos 'HasField' para campos opcionais
            # mas para campos string, um valor vazio ('') é o padrão se não for definido.
            # Para campos que você quer que sejam "unset" (sem alteração) se não forem fornecidos,
            # você pode omiti-los da requisição gRPC, se o seu .proto os definir como opcionais.
            # No entanto, para strings, passar '' geralmente é o suficiente.
            
            grpc_request = contrato_pb2.UpdateSensorRequest(
                sensor_id=sensor_id,
                nome=nome,
                descricao=descricao
            )
            
            # Se você tem campos opcionais no .proto e quer enviar apenas os que foram realmente mudados
            # no frontend (o que é mais comum para PUT/PATCH), você precisaria de uma lógica mais elaborada
            # para construir o request, por exemplo:
            # update_request_data = {'sensor_id': sensor_id}
            # if nome is not None:
            #     update_request_data['nome'] = nome
            # if descricao is not None:
            #     update_request_data['descricao'] = descricao
            # grpc_request = contrato_pb2.UpdateSensorRequest(**update_request_data)

            # Chama o método gRPC UpdateSensor
            grpc_response = stub.UpdateSensor(grpc_request)

            response_data = {
                "mensagem": grpc_response.mensagem,
                "sucesso": grpc_response.sucesso,
                "updated_sensor": { # Retorna os dados do sensor atualizado, se disponíveis
                    "sensor_id": grpc_response.updated_sensor.sensor_id,
                    "nome": grpc_response.updated_sensor.nome,
                    "descricao": grpc_response.updated_sensor.descricao
                } if grpc_response.HasField('updated_sensor') else None # Verifica se o campo foi preenchido na resposta
            }
            
            # Decide o status HTTP baseado no sucesso da operação
            if grpc_response.sucesso:
                return Response(response_data, status=status.HTTP_200_OK)
            else:
                # Se a operação gRPC não for bem-sucedida, retorna um 400 Bad Request
                # ou 404 Not Found se o sensor não existia, dependendo da sua API Java.
                # Aqui, estamos retornando 400 para erro lógico reportado pelo gRPC.
                return Response(response_data, status=status.HTTP_400_BAD_REQUEST)

        except grpc.RpcError as e:
            error_message = f"Erro gRPC ao atualizar sensor: {e.details}"
            return Response({"error": error_message}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        except Exception as e:
            # Captura outros erros inesperados no Python
            error_message = f"Erro inesperado ao processar requisição de atualização: {str(e)}"
            return Response({"error": error_message}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        finally:
            channel.close()