package classification;

import com.google.gson.*;

import java.lang.reflect.Type;

// Allows Polymorphic variables in Classes to be stored
// We do this by storing the CLASSNAME i.e. its subclass
// and its INSTANCE data
// This code is entirely based on this stack overflow post http://stackoverflow.com/questions/5800433/polymorphism-with-gson
public class AbstractAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    private static String CLASSNAME = "CLASSNAME";
    private static String INSTANCE = "INSTANCE";

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject retValue = new JsonObject();
        String className = src.getClass().getCanonicalName();

        retValue.addProperty(CLASSNAME, className);
        JsonElement element = context.serialize(src);
        retValue.add(INSTANCE, element);

        return retValue;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
        String className = prim.getAsString();

        Class<?> klass = null;
        try {
            klass = Class.forName(className);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }

        return context.deserialize(jsonObject.get(INSTANCE), klass);
    }
}
