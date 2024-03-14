package com.huskydreaming.settlements.serializers;

import com.google.gson.*;
import com.huskydreaming.settlements.persistence.Claim;

import java.lang.reflect.Type;

public class ClaimSerializer implements JsonSerializer<Claim>, JsonDeserializer<Claim> {

    @Override
    public Claim deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return Claim.deserialize(jsonElement.getAsString().split(":"));
    }

    @Override
    public JsonElement serialize(Claim claim, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(claim.toString());
    }
}