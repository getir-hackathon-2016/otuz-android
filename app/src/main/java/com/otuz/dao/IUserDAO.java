package com.otuz.dao;

import com.otuz.model.AddressModel;
import com.otuz.model.DAOResponse;
import com.otuz.model.UserModel;

/**
 * Persistence methods for User data operations.
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public interface IUserDAO {

    DAOResponse getUser(String facebookUserId);

    DAOResponse saveUser(String facebookUserId);

    DAOResponse saveNewUserProduct(String facebookUserId, String productId);

    DAOResponse updateUserProductQuantity(String productId, String facebookUserId, String quantity);

    DAOResponse updateUserAddress(AddressModel addressModel, String facebookUserId);

}
