package com.otuz.dao;

import com.otuz.model.DAOResponse;
import com.otuz.model.UserModel;

/**
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public interface IUserDAO {

    DAOResponse getUser(String facebookUserId);

    DAOResponse saveUser(String facebookUserId);

}
