# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# NO CHECKED-IN PROTOBUF GENCODE
# source: contrato.proto
# Protobuf Python Version: 6.31.0
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import runtime_version as _runtime_version
from google.protobuf import symbol_database as _symbol_database
from google.protobuf.internal import builder as _builder
_runtime_version.ValidateProtobufRuntimeVersion(
    _runtime_version.Domain.PUBLIC,
    6,
    31,
    0,
    '',
    'contrato.proto'
)
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


from google.protobuf import timestamp_pb2 as google_dot_protobuf_dot_timestamp__pb2


DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x0e\x63ontrato.proto\x1a\x1fgoogle/protobuf/timestamp.proto\"!\n\x0c\x44\x61\x64osRequest\x12\x11\n\tsensor_id\x18\x01 \x01(\t\"\xc6\x01\n\rDadosResponse\x12\x10\n\x08mensagem\x18\x01 \x01(\t\x12\x0f\n\x07sucesso\x18\x02 \x01(\x08\x12\x1c\n\x14sensor_id_encontrado\x18\x03 \x01(\t\x12\x1e\n\x16temperatura_encontrada\x18\x04 \x01(\x02\x12\x1a\n\x12umidade_encontrada\x18\x05 \x01(\x02\x12\x38\n\x14timestamp_encontrado\x18\x06 \x01(\x0b\x32\x1a.google.protobuf.Timestamp\"+\n\x15ListarSensoresRequest\x12\x12\n\nusuario_id\x18\x01 \x01(\x03\"@\n\nSensorInfo\x12\x11\n\tsensor_id\x18\x01 \x01(\t\x12\x0c\n\x04nome\x18\x02 \x01(\t\x12\x11\n\tdescricao\x18\x03 \x01(\t\"T\n\x10SensoresResponse\x12\x10\n\x08mensagem\x18\x01 \x01(\t\x12\x0f\n\x07sucesso\x18\x02 \x01(\x08\x12\x1d\n\x08sensores\x18\x03 \x03(\x0b\x32\x0b.SensorInfo\"\x19\n\x08UserData\x12\r\n\x05\x65mail\x18\x01 \x01(\t\"3\n\x0cUserResponse\x12\x0f\n\x07sucesso\x18\x01 \x01(\x08\x12\x12\n\nusuario_id\x18\x02 \x01(\x03\"6\n\x17RegistrarUsuarioRequest\x12\r\n\x05\x65mail\x18\x01 \x01(\t\x12\x0c\n\x04nome\x18\x02 \x01(\t\"Q\n\x18RegistrarUsuarioResponse\x12\x10\n\x08mensagem\x18\x01 \x01(\t\x12\x12\n\nusuario_id\x18\x02 \x01(\x03\x12\x0f\n\x07sucesso\x18\x03 \x01(\x08\"M\n\x16RegistrarSensorRequest\x12\x12\n\nusuario_id\x18\x01 \x01(\x03\x12\x0c\n\x04nome\x18\x02 \x01(\t\x12\x11\n\tdescricao\x18\x03 \x01(\t\"O\n\x17RegistrarSensorResponse\x12\x10\n\x08mensagem\x18\x01 \x01(\t\x12\x11\n\tsensor_id\x18\x02 \x01(\t\x12\x0f\n\x07sucesso\x18\x03 \x01(\x08\"\x97\x01\n\nSensorData\x12\x11\n\tsensor_id\x18\x01 \x01(\t\x12\x13\n\x0btemperatura\x18\x02 \x01(\x02\x12\x0f\n\x07umidade\x18\x03 \x01(\x02\x12\x10\n\x08mensagem\x18\x04 \x01(\t\x12\x0f\n\x07sucesso\x18\x05 \x01(\x08\x12-\n\ttimestamp\x18\x06 \x01(\x0b\x32\x1a.google.protobuf.Timestamp\"U\n\x0eStatusResposta\x12\x10\n\x08mensagem\x18\x01 \x01(\t\x12 \n\x18total_leituras_recebidas\x18\x02 \x01(\x05\x12\x0f\n\x07sucesso\x18\x03 \x01(\x08\x32\xdf\x02\n\x0eMonitorService\x12G\n\x10RegistrarUsuario\x12\x18.RegistrarUsuarioRequest\x1a\x19.RegistrarUsuarioResponse\x12\x44\n\x0fRegistrarSensor\x12\x17.RegistrarSensorRequest\x1a\x18.RegistrarSensorResponse\x12\x31\n\x11\x45nviarDadosSensor\x12\x0b.SensorData\x1a\x0f.StatusResposta\x12#\n\x07GetUser\x12\t.UserData\x1a\r.UserResponse\x12;\n\x0eListarSensores\x12\x16.ListarSensoresRequest\x1a\x11.SensoresResponse\x12)\n\x08GetDados\x12\r.DadosRequest\x1a\x0e.DadosResponseB\x13\n\x0f\x62r.com.grpc.iotP\x01\x62\x06proto3')

_globals = globals()
_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, _globals)
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'contrato_pb2', _globals)
if not _descriptor._USE_C_DESCRIPTORS:
  _globals['DESCRIPTOR']._loaded_options = None
  _globals['DESCRIPTOR']._serialized_options = b'\n\017br.com.grpc.iotP\001'
  _globals['_DADOSREQUEST']._serialized_start=51
  _globals['_DADOSREQUEST']._serialized_end=84
  _globals['_DADOSRESPONSE']._serialized_start=87
  _globals['_DADOSRESPONSE']._serialized_end=285
  _globals['_LISTARSENSORESREQUEST']._serialized_start=287
  _globals['_LISTARSENSORESREQUEST']._serialized_end=330
  _globals['_SENSORINFO']._serialized_start=332
  _globals['_SENSORINFO']._serialized_end=396
  _globals['_SENSORESRESPONSE']._serialized_start=398
  _globals['_SENSORESRESPONSE']._serialized_end=482
  _globals['_USERDATA']._serialized_start=484
  _globals['_USERDATA']._serialized_end=509
  _globals['_USERRESPONSE']._serialized_start=511
  _globals['_USERRESPONSE']._serialized_end=562
  _globals['_REGISTRARUSUARIOREQUEST']._serialized_start=564
  _globals['_REGISTRARUSUARIOREQUEST']._serialized_end=618
  _globals['_REGISTRARUSUARIORESPONSE']._serialized_start=620
  _globals['_REGISTRARUSUARIORESPONSE']._serialized_end=701
  _globals['_REGISTRARSENSORREQUEST']._serialized_start=703
  _globals['_REGISTRARSENSORREQUEST']._serialized_end=780
  _globals['_REGISTRARSENSORRESPONSE']._serialized_start=782
  _globals['_REGISTRARSENSORRESPONSE']._serialized_end=861
  _globals['_SENSORDATA']._serialized_start=864
  _globals['_SENSORDATA']._serialized_end=1015
  _globals['_STATUSRESPOSTA']._serialized_start=1017
  _globals['_STATUSRESPOSTA']._serialized_end=1102
  _globals['_MONITORSERVICE']._serialized_start=1105
  _globals['_MONITORSERVICE']._serialized_end=1456
# @@protoc_insertion_point(module_scope)
