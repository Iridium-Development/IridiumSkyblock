package com.iridium.iridiumskyblock.support.material;

import java.io.IOException;

import com.iridium.iridiumcore.dependencies.fasterxml.core.JsonGenerator;
import com.iridium.iridiumcore.dependencies.fasterxml.databind.JsonSerializer;
import com.iridium.iridiumcore.dependencies.fasterxml.databind.SerializerProvider;

public class IridiumMaterialSerialize extends JsonSerializer<IridiumMaterial> {

    @Override
    public void serialize(IridiumMaterial arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
       arg1.writeString(arg0.getKey());
    }
    
}
