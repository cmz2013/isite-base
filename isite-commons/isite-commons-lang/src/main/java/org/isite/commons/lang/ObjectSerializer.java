package org.isite.commons.lang;

import org.isite.commons.lang.utils.IoUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ObjectSerializer {

    private ObjectSerializer() {
    }

    /**
     * Object序列化操作
     */
    public static byte[] toBytes(Object object) throws IOException {
        ByteArrayOutputStream byteOutput = null;
        ObjectOutputStream objectOutput = null;
        try {
            byteOutput = new ByteArrayOutputStream();
            objectOutput = new ObjectOutputStream(byteOutput);
            objectOutput.writeObject(object);
            return byteOutput.toByteArray();
        } finally {
            IoUtils.close(objectOutput, byteOutput);
        }
    }

    /**
     * Object序列化操作
     */
    public static String toString(Object object) throws IOException {
        /*
         * ByteArrayOutputStream对象调用toString转为字符串时，
         * 会将ObjectOutputStream对象放置在对象流头部的前两个字节（0xac）（0xed）序列化为两个"?"，
         * 当这个字符串使用getByte()时会将两个"?"变为（0x3f ）（0x3f），
         * 然而这两个字符并不能构成有效的对象流头，反序列化时会失败：invalid stream header: EFBFBDEF
         * 注：在转换成字符串的时候如果使用Base64则可以避免该问题
         */
        return Base64.getEncoder().encodeToString(toBytes(object));
    }

    /**
     * Object反序列化操作
     */
    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteInput = null;
        ObjectInputStream objectInput = null;
        try {
            byteInput = new ByteArrayInputStream(bytes);
            objectInput = new ObjectInputStream(byteInput);
            return objectInput.readObject();
        } finally {
            IoUtils.close(objectInput, byteInput);
        }
    }

    /**
     * Object反序列化操作
     */
    public static Object deserialize(String string) throws IOException, ClassNotFoundException {
        return deserialize(Base64.getDecoder().decode(string));
    }
}
