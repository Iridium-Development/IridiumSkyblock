package com.iridium.iridiumskyblock.support.material;

import java.io.IOException;

import com.iridium.iridiumcore.dependencies.fasterxml.core.JacksonException;
import com.iridium.iridiumcore.dependencies.fasterxml.core.JsonParser;
import com.iridium.iridiumcore.dependencies.fasterxml.databind.DeserializationContext;
import com.iridium.iridiumcore.dependencies.fasterxml.databind.JsonDeserializer;

public class IridiumMaterialDeserialize extends JsonDeserializer<IridiumMaterial> {
    @Override
    public IridiumMaterial deserialize(JsonParser arg0, DeserializationContext arg1)
            throws IOException, JacksonException {
        return IridiumMaterial.valueOf(arg0.readValueAs(String.class));
    }
}
