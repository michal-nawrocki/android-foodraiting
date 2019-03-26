package com.mxn672.foodrating.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface EstablishmentDao {
    @Insert
    void insertEstablishment(Establishment estb);

    @Query("SELECT * FROM establishment WHERE estb_id=:id LIMIT 1")
    Establishment getEstablishment(String id);

    @Delete
    void deleteEstablishment(Establishment estb);
}
