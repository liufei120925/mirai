/*
 * Copyright 2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */


@file:JvmName("Utils")
@file:JvmMultifileClass

package net.mamoe.mirai.utils


import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.io.ByteWriteChannel
import kotlinx.coroutines.io.readAvailable
import kotlinx.io.OutputStream
import kotlinx.serialization.InternalSerializationApi
import kotlinx.io.core.Output
import kotlinx.io.pool.useInstance
import net.mamoe.mirai.utils.io.ByteArrayPool
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName

// copyTo

/**
 * 从接收者管道读取所有数据并写入 [dst]. 不会关闭 [dst]
 */
@OptIn(InternalSerializationApi::class)
suspend fun ByteReadChannel.copyTo(dst: OutputStream) {
    @OptIn(MiraiInternalAPI::class)
    ByteArrayPool.useInstance { buffer ->
        var size: Int
        while (this.readAvailable(buffer).also { size = it } > 0) {
            dst.write(buffer, 0, size)
        }
    }
}

/**
 * 从接收者管道读取所有数据并写入 [dst]. 不会关闭 [dst]
 */
suspend fun ByteReadChannel.copyTo(dst: Output) {
    @OptIn(MiraiInternalAPI::class)
    ByteArrayPool.useInstance { buffer ->
        var size: Int
        while (this.readAvailable(buffer).also { size = it } > 0) {
            dst.writeFully(buffer, 0, size)
        }
    }
}

/**
 * 从接收者管道读取所有数据并写入 [dst]. 不会关闭 [dst]
 */
suspend fun ByteReadChannel.copyTo(dst: ByteWriteChannel) {
    @OptIn(MiraiInternalAPI::class)
    ByteArrayPool.useInstance { buffer ->
        var size: Int
        while (this.readAvailable(buffer).also { size = it } > 0) {
            dst.writeFully(buffer, 0, size)
        }
    }
}


/* // 垃圾 kotlin, Unresolved reference: ByteWriteChannel
/**
 * 从接收者管道读取所有数据并写入 [dst]. 不会关闭 [dst]
 */
suspend fun ByteReadChannel.copyTo(dst: kotlinx.coroutines.io.ByteWriteChannel) {
    ByteArrayPool.useInstance {
        do {
            val size = this.readAvailable(it)
            dst.writeFully(it, 0, size)
        } while (size != 0)
    }
}
*/

// copyAndClose


/**
 * 从接收者管道读取所有数据并写入 [dst], 最终关闭 [dst]
 */
@OptIn(InternalSerializationApi::class)
suspend fun ByteReadChannel.copyAndClose(dst: OutputStream) { // 在 JVM 这个 API 不是 internal 的
    try {
        @OptIn(MiraiInternalAPI::class)
        ByteArrayPool.useInstance { buffer ->
            var size: Int
            while (this.readAvailable(buffer).also { size = it } > 0) {
                dst.write(buffer, 0, size)
            }
        }
    } finally {
        dst.close()
    }
}

/**
 * 从接收者管道读取所有数据并写入 [dst], 最终关闭 [dst]
 */
suspend fun ByteReadChannel.copyAndClose(dst: Output) {
    try {
        @OptIn(MiraiInternalAPI::class)
        ByteArrayPool.useInstance { buffer ->
            var size: Int
            while (this.readAvailable(buffer).also { size = it } > 0) {
                dst.writeFully(buffer, 0, size)
            }
        }
    } finally {
        dst.close()
    }
}

/**
 * 从接收者管道读取所有数据并写入 [dst], 最终关闭 [dst]
 */
suspend fun ByteReadChannel.copyAndClose(dst: ByteWriteChannel) {
    @Suppress("DuplicatedCode")
    try {
        @OptIn(MiraiInternalAPI::class)
        ByteArrayPool.useInstance { buffer ->
            var size: Int
            while (this.readAvailable(buffer).also { size = it } > 0) {
                dst.writeFully(buffer, 0, size)
            }
        }
    } finally {
        @Suppress("DuplicatedCode")
        dst.close(null)
    }
}

/**
 * 从接收者管道读取所有数据并写入 [dst], 最终关闭 [dst]
 */
suspend fun ByteReadChannel.copyAndClose(dst: io.ktor.utils.io.ByteWriteChannel) {
    @Suppress("DuplicatedCode")
    try {
        @OptIn(MiraiInternalAPI::class)
        ByteArrayPool.useInstance { buffer ->
            var size: Int
            while (this.readAvailable(buffer).also { size = it } > 0) {
                dst.writeFully(buffer, 0, size)
            }
        }
    } finally {
        @Suppress("DuplicatedCode")
        dst.close(null)
    }
}

/*// 垃圾 kotlin, Unresolved reference: ByteWriteChannel
/**
 * 从接收者管道读取所有数据并写入 [dst], 最终关闭 [dst]
 */
suspend fun ByteReadChannel.copyAndClose(dst: kotlinx.coroutines.io.ByteWriteChannel) {
    dst.close(kotlin.runCatching {
        ByteArrayPool.useInstance {
            do {
                val size = this.readAvailable(it)
                dst.writeFully(it, 0, size)
            } while (size != 0)
        }
    }.exceptionOrNull())
}

 */