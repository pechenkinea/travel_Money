package com.pechenkin.travelmoney.bd;

import com.pechenkin.travelmoney.bd.firestore.TripFireStore;
import com.pechenkin.travelmoney.bd.firestore.documents.TripDocument;
import com.pechenkin.travelmoney.bd.local.TripLocal;
import com.pechenkin.travelmoney.bd.local.query.TripTableRow;
import com.pechenkin.travelmoney.bd.local.table.TableTrip;

import java.util.ArrayList;
import java.util.List;

/**
 * отвечает за то, какого типа создавать поездку. На текущий момент работаем только с локальной БД и возвращаем только TripLocal, но в дальнейшем будет развиваться
 */
public class TripManager {

    public static final TripManager INSTANCE = new TripManager();

    private Trip activeTrip = null;

    public Trip getActiveTrip() {
        if (activeTrip == null) {
            TripTableRow activeTripRow = TableTrip.INSTANCE.getActiveTrip();

            activeTrip = createTripByTripRow(activeTripRow);
        }
        return activeTrip;
    }

    public Trip add(String name, String comment, TripStore tripStore) {
        TripTableRow tripRow = TableTrip.INSTANCE.add(name, comment, tripStore);

        if (tripStore == TripStore.FIRESTORE) {
            TripDocument.INSTANCE.add(tripRow.uuid);
        }

        return createTripByTripRow(tripRow);
    }

    public void setActive(Trip trip) {

        TableTrip.INSTANCE.set_active(trip.getUUID());
        activeTrip = trip;

    }

    public boolean isActive(Trip trip) {
        return activeTrip.equals(trip);
    }

    public List<Trip> getAll() {

        TripTableRow[] allTrips = TableTrip.INSTANCE.getAll();
        List<Trip> result = new ArrayList<>(allTrips.length);

        for (TripTableRow row : allTrips) {
            result.add(createTripByTripRow(row));
        }
        return result;
    }

    /**
     * Тут должны быть все проверки. Локальная или удаленная поездка
     */
    private Trip createTripByTripRow(TripTableRow trip) {
        if (trip.tripStore == TripStore.FIRESTORE) {
            return new TripCacheDecorator(new TripFireStore(trip));
        } else {
            return new TripCacheDecorator(new TripLocal(trip));
        }
    }


}
