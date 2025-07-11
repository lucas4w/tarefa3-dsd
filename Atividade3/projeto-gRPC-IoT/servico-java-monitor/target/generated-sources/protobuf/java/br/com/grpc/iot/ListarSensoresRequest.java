// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: contrato.proto

// Protobuf Java Version: 3.25.3
package br.com.grpc.iot;

/**
 * Protobuf type {@code ListarSensoresRequest}
 */
public final class ListarSensoresRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:ListarSensoresRequest)
    ListarSensoresRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use ListarSensoresRequest.newBuilder() to construct.
  private ListarSensoresRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ListarSensoresRequest() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new ListarSensoresRequest();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return br.com.grpc.iot.Contrato.internal_static_ListarSensoresRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return br.com.grpc.iot.Contrato.internal_static_ListarSensoresRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            br.com.grpc.iot.ListarSensoresRequest.class, br.com.grpc.iot.ListarSensoresRequest.Builder.class);
  }

  public static final int USUARIO_ID_FIELD_NUMBER = 1;
  private long usuarioId_ = 0L;
  /**
   * <code>int64 usuario_id = 1;</code>
   * @return The usuarioId.
   */
  @java.lang.Override
  public long getUsuarioId() {
    return usuarioId_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (usuarioId_ != 0L) {
      output.writeInt64(1, usuarioId_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (usuarioId_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(1, usuarioId_);
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof br.com.grpc.iot.ListarSensoresRequest)) {
      return super.equals(obj);
    }
    br.com.grpc.iot.ListarSensoresRequest other = (br.com.grpc.iot.ListarSensoresRequest) obj;

    if (getUsuarioId()
        != other.getUsuarioId()) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + USUARIO_ID_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getUsuarioId());
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static br.com.grpc.iot.ListarSensoresRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static br.com.grpc.iot.ListarSensoresRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static br.com.grpc.iot.ListarSensoresRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static br.com.grpc.iot.ListarSensoresRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static br.com.grpc.iot.ListarSensoresRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static br.com.grpc.iot.ListarSensoresRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static br.com.grpc.iot.ListarSensoresRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static br.com.grpc.iot.ListarSensoresRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static br.com.grpc.iot.ListarSensoresRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static br.com.grpc.iot.ListarSensoresRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static br.com.grpc.iot.ListarSensoresRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static br.com.grpc.iot.ListarSensoresRequest parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(br.com.grpc.iot.ListarSensoresRequest prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code ListarSensoresRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:ListarSensoresRequest)
      br.com.grpc.iot.ListarSensoresRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return br.com.grpc.iot.Contrato.internal_static_ListarSensoresRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return br.com.grpc.iot.Contrato.internal_static_ListarSensoresRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              br.com.grpc.iot.ListarSensoresRequest.class, br.com.grpc.iot.ListarSensoresRequest.Builder.class);
    }

    // Construct using br.com.grpc.iot.ListarSensoresRequest.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      usuarioId_ = 0L;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return br.com.grpc.iot.Contrato.internal_static_ListarSensoresRequest_descriptor;
    }

    @java.lang.Override
    public br.com.grpc.iot.ListarSensoresRequest getDefaultInstanceForType() {
      return br.com.grpc.iot.ListarSensoresRequest.getDefaultInstance();
    }

    @java.lang.Override
    public br.com.grpc.iot.ListarSensoresRequest build() {
      br.com.grpc.iot.ListarSensoresRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public br.com.grpc.iot.ListarSensoresRequest buildPartial() {
      br.com.grpc.iot.ListarSensoresRequest result = new br.com.grpc.iot.ListarSensoresRequest(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(br.com.grpc.iot.ListarSensoresRequest result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.usuarioId_ = usuarioId_;
      }
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof br.com.grpc.iot.ListarSensoresRequest) {
        return mergeFrom((br.com.grpc.iot.ListarSensoresRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(br.com.grpc.iot.ListarSensoresRequest other) {
      if (other == br.com.grpc.iot.ListarSensoresRequest.getDefaultInstance()) return this;
      if (other.getUsuarioId() != 0L) {
        setUsuarioId(other.getUsuarioId());
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 8: {
              usuarioId_ = input.readInt64();
              bitField0_ |= 0x00000001;
              break;
            } // case 8
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private long usuarioId_ ;
    /**
     * <code>int64 usuario_id = 1;</code>
     * @return The usuarioId.
     */
    @java.lang.Override
    public long getUsuarioId() {
      return usuarioId_;
    }
    /**
     * <code>int64 usuario_id = 1;</code>
     * @param value The usuarioId to set.
     * @return This builder for chaining.
     */
    public Builder setUsuarioId(long value) {

      usuarioId_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>int64 usuario_id = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearUsuarioId() {
      bitField0_ = (bitField0_ & ~0x00000001);
      usuarioId_ = 0L;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:ListarSensoresRequest)
  }

  // @@protoc_insertion_point(class_scope:ListarSensoresRequest)
  private static final br.com.grpc.iot.ListarSensoresRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new br.com.grpc.iot.ListarSensoresRequest();
  }

  public static br.com.grpc.iot.ListarSensoresRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ListarSensoresRequest>
      PARSER = new com.google.protobuf.AbstractParser<ListarSensoresRequest>() {
    @java.lang.Override
    public ListarSensoresRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<ListarSensoresRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ListarSensoresRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public br.com.grpc.iot.ListarSensoresRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

