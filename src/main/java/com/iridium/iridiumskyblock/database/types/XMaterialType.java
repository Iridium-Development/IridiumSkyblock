package com.iridium.iridiumskyblock.database.types;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;
import java.sql.SQLException;
import java.util.Optional;

public class XMaterialType extends StringType{

    private static final XMaterialType singleTon = new XMaterialType();

    public static XMaterialType getSingleton() {
		return singleTon;
	}

    protected XMaterialType() {
        super(SqlType.STRING, new Class<?>[] { XMaterial.class });
    }

    @Override
	public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        String value = (String) super.sqlArgToJava(fieldType, sqlArg, columnPos);
        Optional<XMaterial> material = XMaterial.matchXMaterial(value);
        if (material.isPresent())
            return material.get();
        return null;
    }

    @Override
	public Object javaToSqlArg(FieldType fieldType, Object obj) throws SQLException {
        XMaterial material = (XMaterial) obj;
        return super.javaToSqlArg(fieldType, material.toString());
    }
    
}
