package uk.co.rockstable.experiments.asm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import uk.co.rockstable.experiments.asm.utils.UnsafeUtils;

import static org.objectweb.asm.Opcodes.*;


public class FooBarFactory {
    private static final FooBarFactory INSTANCE = new FooBarFactory();
    private static final String GENERATED_CLASS_NAME = "uk/co/rockstable/experiments/asm/FooBarFactoryImpl";

    private static final Class<?> clazz;

    static {
        clazz = newClazz();
    }

    public static Object newInstance() {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> newClazz() {
        byte[] bytes = INSTANCE.generateByteCode();
        return INSTANCE.createClass(bytes);
    }

    public Class<?> createClass(byte[] bytecode) {
        ClassLoader cl = getClass().getClassLoader();
        return UnsafeUtils.UNSAFE.defineClass(GENERATED_CLASS_NAME, bytecode, 0, bytecode.length, cl, null);
    }

    private byte[] generateByteCode() {
        ClassWriter cw = new ClassWriter(0);
        MethodVisitor mv;

        cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, GENERATED_CLASS_NAME,
                null, "java/lang/Object",
                new String[]{"uk/co/rockstable/experiments/asm/FooFactory", "uk/co/rockstable/experiments/asm/BarFactory"});

        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }

        {
            mv = cw.visitMethod(ACC_PUBLIC, "create", "()Luk/co/rockstable/experiments/asm/Bar;", null, null);
            mv.visitCode();
            mv.visitTypeInsn(NEW, "uk/co/rockstable/experiments/asm/Bar");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "uk/co/rockstable/experiments/asm/Bar", "<init>", "()V", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }

        {
            mv = cw.visitMethod(ACC_PUBLIC, "create", "()Luk/co/rockstable/experiments/asm/Foo;", null, null);
            mv.visitCode();
            mv.visitTypeInsn(NEW, "uk/co/rockstable/experiments/asm/Foo");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "uk/co/rockstable/experiments/asm/Foo", "<init>", "()V", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        cw.visitEnd();
        return cw.toByteArray();
    }

}
