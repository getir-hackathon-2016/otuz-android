package com.otuz.dao;

import com.otuz.model.DAOResponse;

/**
 * Persistence method for Google location to address operation.
 * Created by AhmetOguzhanBasar on 21.02.2016.
 */
public interface IMapDAO {

    DAOResponse getFormattedAddressFromGoogleMaps(double _latitude, double _longitude);

}