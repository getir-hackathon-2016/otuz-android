package com.otuz.dao;

import com.otuz.model.DAOResponse;

/**
 * Persistence method for order operation.
 * Created by AhmetOguzhanBasar on 21.02.2016.
 */
public interface IOrderDAO {

    DAOResponse confirmShoppingList(String facebookUserId, String deliveryDate);

}
