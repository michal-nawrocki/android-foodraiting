package com.mxn672.foodrating.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Establishment.class}, version = 1)
public abstract class EstablishmentDatabase extends RoomDatabase {
    public abstract EstablishmentDao establishmentDao();
}
