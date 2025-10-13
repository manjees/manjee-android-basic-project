package com.manjee.basic.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.manjee.basic.data.proto.UserSettingsProto
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSettingsSerializer @Inject constructor() : Serializer<UserSettingsProto> {

    override val defaultValue: UserSettingsProto = UserSettingsProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserSettingsProto {
        try {
            return UserSettingsProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto", exception)
        }
    }

    override suspend fun writeTo(t: UserSettingsProto, output: OutputStream) {
        t.writeTo(output)
    }
}
