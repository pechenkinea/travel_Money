package com.pechenkin.travelmoney.bd;

import com.pechenkin.travelmoney.bd.local.TripLocal;
import com.pechenkin.travelmoney.bd.local.query.TripTableRow;
import com.pechenkin.travelmoney.bd.local.table.TripsTable;

import java.util.ArrayList;
import java.util.List;

/**
 * отвечает за то, какого типа создавать поездку. На текущий момент работаем только с локальной БД и возвращаем только TripLocal, но в дальнейшем будет развиваться
 */
public class TripManager {

    public static TripManager INSTANCE = new TripManager();

    private Trip activeTrip = null;

    public Trip getActiveTrip() {
        if (activeTrip == null) {
            TripTableRow activeTripRow = TripsTable.INSTANCE.getActiveTrip();

            activeTrip = createTripByTripRow(activeTripRow);
        }
        return activeTrip;
    }

    public Trip add(String name, String comment) {
        long id = TripsTable.INSTANCE.add(name, comment);
        TripTableRow tripRow = TripsTable.INSTANCE.getById(id);
        return createTripByTripRow(tripRow);
    }

    public void setActive(Trip trip) {

        TripsTable.INSTANCE.set_active(trip.getId());
        activeTrip = trip;

    }

    public List<Trip> getAll() {

        TripTableRow[] allTrips = TripsTable.INSTANCE.getAll();
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
        return new TripLocal(trip);
    }


}
