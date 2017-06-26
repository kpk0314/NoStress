package com.sourcey.materiallogindemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MyOpenHelper extends SQLiteAssetHelper {

    Context context;

    public MyOpenHelper(Context context) {
        //2번째 인자는 만들어지는 sqlite파일 이름이고 4번째 인자는 개발자가 만든 sqlite버전이다.
        super(context, "rand.db", null, 1);
        this.context = context;
    }

<<<<<<< HEAD
    //최초에 기존에 없었던 db가 새롭게 만들어질때 1번 호출
    public void onCreate(SQLiteDatabase db) {
        // db.execSQL("CREATE TABLE member (t TEXT, h INTEGER, r INTEGER, x TEXT);");
        db.execSQL("CREATE TABLE datareceived( d text, hr integer, rrInterval real, acc_x real, acc_y real, acc_z real);");
        db.execSQL("CREATE TABLE HRSUM( sum integer);");
=======
>>>>>>> 62f0f0277c4daf8a447d6fb3274e9bbc1a5d58ad

//    //최초에 기존에 없었던 db가 새롭게 만들어질때 1번 호출
//    public void onCreate(SQLiteDatabase db) {
//        // db.execSQL("CREATE TABLE member (t TEXT, h INTEGER, r INTEGER, x TEXT);");
//        db.execSQL("CREATE TABLE datareceived( d text, hr integer, rrInterval real, acc_x real, acc_y real, acc_z real);");
//        db.execSQL("CREATE TABLE rand(date TEXT, stress INTEGER);");
//
//    }

    //이미 배포했던 db에 변경이 있을경우 호출된다.
    //주로 버전의 변경이 있을때 호출
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
<<<<<<< HEAD
        db.execSQL("CREATE TABLE datareceived( d text, hr integer, rrInterval real, acc_x real, acc_y real, acc_z real);");
        db.execSQL("CREATE TABLE HRSUM( sum integer);");

=======
      //  db.execSQL("CREATE TABLE datareceived( d text, hr integer, rrInterval real, acc_x real, acc_y real, acc_z real);");
        db.execSQL("CREATE TABLE rand(date TEXT, stress INTEGER);");
>>>>>>> 62f0f0277c4daf8a447d6fb3274e9bbc1a5d58ad
    }
}