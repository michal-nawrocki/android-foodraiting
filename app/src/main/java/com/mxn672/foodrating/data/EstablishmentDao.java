package com.mxn672.foodrating.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface EstablishmentDao {
    @Insert
    void insertEstablishment(Establishment estb);

    @Query("SELECT * FROM establishment WHERE estb_id=:id LIMIT 1")
    public Establishment getEstablishment(String id);

    @Query("SELECT * FROM establishment")
    public List<Establishment> getAll();

    @Delete
    public void deleteEstablishment(Establishment estb);
}
