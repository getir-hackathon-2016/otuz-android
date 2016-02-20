package com.otuz.dao;

import com.otuz.model.DAOResponse;

/**
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public interface IProductDAO {

    DAOResponse getProductViaBarcodeNumber(String barcodeNumber);

}
