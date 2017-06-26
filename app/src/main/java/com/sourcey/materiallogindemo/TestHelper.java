package com.sourcey.materiallogindemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class TestHelper extends SQLiteAssetHelper {

    Context context;

    public TestHelper(Context context) {
        super(context, "test.db", null, 1);
        this.context = context;

    }

    //이미 배포했던 db에 변경이 있을경우 호출된다.
    //주로 버전의 변경이 있을때 호출
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("CREATE TABLE rand(d TEXT, s INTEGER);");
    }
}