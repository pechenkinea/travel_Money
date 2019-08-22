package com.pechenkin.travelmoney.bd;

import com.pechenkin.travelmoney.bd.local.TripLocal;
import com.pechenkin.travelmoney.bd.local.query.TripTableRow;
import com.pechenkin.travelmoney.bd.local.table.t_trips;

import java.util.ArrayList;
import java.util.List;

/**
 * отвечает за то, какого типа создавать поездку. На текущий момент работаем только с локальной БД и возвращаем только TripLocal, но в дальнейшем будет развиваться
 */
public class TripManager {

    public static TripManager INSTANCE = new TripManager();

    public Trip getActiveTrip() {
        TripTableRow activeTrip = t_trips.getActiveTrip();

        return new TripLocal(activeTrip);
    }

    public List<Trip> getAll() {

        TripTableRow[] allTrips = t_trips.getAll();
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
