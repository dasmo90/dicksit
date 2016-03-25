package de.dasmo90;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectSerializer {

    public static <T> String serialize(T object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
        out.writeObject(object);
        out.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toString();
    }

    public static <T> T deserialize(String serializedObject) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedObject.getBytes());
        ObjectInputStream in = new ObjectInputStream(byteArrayInputStream);
        @SuppressWarnings("unchecked")
        T object = (T) in.readObject();
        in.close();
        byteArrayInputStream.close();
        return object;
    }

    public static <T> String serializeHard(T object) {
        try {
            return serialize(object);
        } catch (IOException e) {
            return "";
        }
    }
}
