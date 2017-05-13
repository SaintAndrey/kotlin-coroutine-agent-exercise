package agent

import jdk.internal.org.objectweb.asm.MethodVisitor
import jdk.internal.org.objectweb.asm.Opcodes

class MyMethodVisitor(var methodVisitor: MethodVisitor) : MethodVisitor(Opcodes.ASM5, methodVisitor) {
    private val NUMBER_IN_CODE = Opcodes.INVOKESTATIC
    private val PACKAGE_FILE = "example/CoroutineExampleKt"
    private val NAME_METHOD = "test"
    val DETECTED_STRING = "(Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;"

    override fun visitMethodInsn(opcode: Int,
                                 owner: String?,
                                 name: String?,
                                 desc: String?,
                                 itf: Boolean) {

        if (    opcode == NUMBER_IN_CODE
                && owner == PACKAGE_FILE
                && name == NAME_METHOD
                && desc == DETECTED_STRING) {

            methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
            methodVisitor.visitLdcInsn("Detected")
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream",
                    "println", "(Ljava/lang/String;)V", false)

        }

        methodVisitor.visitMethodInsn(opcode, owner, name, desc, itf)
    }

}
