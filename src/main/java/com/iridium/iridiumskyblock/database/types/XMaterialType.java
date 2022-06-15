package com.iridium.iridiumskyblock.database.types;

import com.iridium.iridiumskyblock.support.material.IridiumMaterial;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;
import java.sql.SQLException;
import java.util.Optional;

public class XMaterialType extends StringType {

    private static final XMaterialType instance = new XMaterialType();

    public static XMaterialType getSingleton() {
        return instance;
    }

    protected XMaterialType() {
        super(SqlType.STRING, new Class<?>[] { IridiumMaterial.class });
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        String value = (String) super.sqlArgToJava(fieldType, sqlArg, columnPos);
        return IridiumMaterial.valueOf(value);
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object object) throws SQLException {
        IridiumMaterial material = (IridiumMaterial) object;
        return super.javaToSqlArg(fieldType, material.getKey());
    }
    
}
