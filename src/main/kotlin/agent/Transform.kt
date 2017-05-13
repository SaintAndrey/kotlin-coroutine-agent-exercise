package agent

import jdk.internal.org.objectweb.asm.*
import jdk.internal.org.objectweb.asm.ClassWriter.COMPUTE_MAXS
import java.lang.instrument.ClassFileTransformer
import java.security.ProtectionDomain

class Transform : ClassFileTransformer {
    override fun transform(loader: ClassLoader?,
                           className: String?,
                           classBeingRedefined: Class<*>?,
                           protectionDomain: ProtectionDomain?,
                           classfileBuffer: ByteArray?): ByteArray {

        val writer = ClassWriter(COMPUTE_MAXS)

        val visitor = object : ClassVisitor(Opcodes.ASM5, writer) {
            override fun visitMethod(access: Int,
                                     name: String?,
                                     desc: String?,
                                     signature: String?,
                                     exceptions: Array<out String>?): MethodVisitor {
                return MyMethodVisitor(writer.visitMethod(access, name, desc, signature, exceptions))
            }
        }

        val reader = ClassReader(classfileBuffer)
        reader.accept(visitor, 0)

        return writer.toByteArray()
    }
}
