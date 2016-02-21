package com.otuz.dao;

import com.otuz.model.DAOResponse;

/**
 * Persistence method for product info operation.
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public interface IProductDAO {

    DAOResponse getProductViaBarcodeNumber(String barcodeNumber);

}
