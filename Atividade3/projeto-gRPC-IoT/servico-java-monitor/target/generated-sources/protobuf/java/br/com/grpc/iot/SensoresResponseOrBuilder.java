// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: contrato.proto

// Protobuf Java Version: 3.25.3
package br.com.grpc.iot;

public interface SensoresResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:SensoresResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string mensagem = 1;</code>
   * @return The mensagem.
   */
  java.lang.String getMensagem();
  /**
   * <code>string mensagem = 1;</code>
   * @return The bytes for mensagem.
   */
  com.google.protobuf.ByteString
      getMensagemBytes();

  /**
   * <code>bool sucesso = 2;</code>
   * @return The sucesso.
   */
  boolean getSucesso();

  /**
   * <pre>
   * lista de objetos SensorInfo
   * </pre>
   *
   * <code>repeated .SensorInfo sensores = 3;</code>
   */
  java.util.List<br.com.grpc.iot.SensorInfo> 
      getSensoresList();
  /**
   * <pre>
   * lista de objetos SensorInfo
   * </pre>
   *
   * <code>repeated .SensorInfo sensores = 3;</code>
   */
  br.com.grpc.iot.SensorInfo getSensores(int index);
  /**
   * <pre>
   * lista de objetos SensorInfo
   * </pre>
   *
   * <code>repeated .SensorInfo sensores = 3;</code>
   */
  int getSensoresCount();
  /**
   * <pre>
   * lista de objetos SensorInfo
   * </pre>
   *
   * <code>repeated .SensorInfo sensores = 3;</code>
   */
  java.util.List<? extends br.com.grpc.iot.SensorInfoOrBuilder> 
      getSensoresOrBuilderList();
  /**
   * <pre>
   * lista de objetos SensorInfo
   * </pre>
   *
   * <code>repeated .SensorInfo sensores = 3;</code>
   */
  br.com.grpc.iot.SensorInfoOrBuilder getSensoresOrBuilder(
      int index);
}
