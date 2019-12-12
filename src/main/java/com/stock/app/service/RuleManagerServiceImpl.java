package com.stock.app.service;

import com.stock.app.dao.RuleDAO;
import com.stock.app.dao.UserDAO;
import com.stock.app.exceptions.RuleAlreadyExistsException;
import com.stock.app.exceptions.RuleNotFoundException;
import com.stock.app.exceptions.UserNotFoundException;
import com.stock.app.model.RuleEntity;
import com.stock.app.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for rule user management, executes CRUD operations for RuleEntity data model.
 *
 * @author rolland.schnell
 */
@Service
public class RuleManagerServiceImpl implements RuleManagerService {

    private RuleDAO ruleDAO;

    private UserDAO userDAO;

    @Autowired
    public RuleManagerServiceImpl(RuleDAO ruleDAO, UserDAO userDAO) {
        this.ruleDAO = ruleDAO;
        this.userDAO = userDAO;
    }

    /**
     * Create a new RuleEntity for a specified user by email.
     * @param ruleEntity - the ruleEntity to be created
     * @param email - user identified by its email
     * @throws RuleAlreadyExistsException - if the ruleEntity defined for a symbol already exits
     * @throws UserNotFoundException - if the user does not exists we cannot save a ruleEntity
     */
    @Override
    public void createNewRule(RuleEntity ruleEntity, String email) throws RuleAlreadyExistsException, UserNotFoundException {

        try {
            UserEntity userEntity = userDAO.getUserEntitiesByEmail(email);

            if (userEntity != null) {
                RuleEntity ruleEntityFromDb = ruleDAO.getRuleEntityByStockNameAndUserEntity(ruleEntity.getStockName(), userEntity);

                if (ruleEntityFromDb != null) {
                    throw new RuleAlreadyExistsException("RuleEntity already exists for " + ruleEntity.getStockName());
                } else {
                    ruleEntity.setUserEntity(userEntity);
                    ruleDAO.save(ruleEntity);
                }
            } else {
                throw new UserNotFoundException("UserEntity not found");
            }

        } catch (Exception ex) {
            throw new RuleAlreadyExistsException("Could not save ruleEntity " + ruleEntity.toString());
        }
    }

    /**
     * Returns the list of rule objects associated to a give user identified by its email.
     * @param email - user email.
     * @return - list of rule objects
     * @throws UserNotFoundException - if the user does not exists we cannot return any rules
     * @throws Exception - if something unexpected happens
     */
    @Override
    public List<RuleEntity> getRulesByUser(String email) throws UserNotFoundException, Exception {

        try {
            UserEntity userEntity = userDAO.getUserEntitiesByEmail(email);

            if (userEntity != null) {
                return ruleDAO.getRulesByUserEntity(userEntity);
            } else {
                throw new UserNotFoundException("UserEntity not found to retrieve rules from");
            }
        } catch (Exception ex) {
            throw new Exception("Cannot retrieve user rules");
        }
    }

    /**
     * Return a list of all rules regardless of the user (Used for administration purposes)
     * @return - list of rule objects
     * @throws Exception - if something unexpected happens
     */
    @Override
    public List<RuleEntity> getAllRules() throws Exception {
        try {
            return ruleDAO.findAll();
        } catch (Exception ex) {
            throw new Exception("Cannot retrieve user rules");
        }
    }

    /**
     * Update an existing user ruleEntity with a new symbol or a new min stock value.
     * @param ruleEntity - the new ruleEntity details
     * @param email - email of the user that holds the ruleEntity
     * @return - the updated ruleEntity
     * @throws UserNotFoundException - if the user does not exists
     * @throws RuleNotFoundException - if the ruleEntity does not exists
     * @throws Exception - if something unexpected happens
     */
    @Override
    public RuleEntity updateUserRule(RuleEntity ruleEntity, String email) throws UserNotFoundException, RuleNotFoundException, Exception {
        try {
            UserEntity userEntity = userDAO.getUserEntitiesByEmail(email);
            if (userEntity != null) {
                RuleEntity ruleEntityFromDb = ruleDAO.getRuleEntityByStockNameAndUserEntity(ruleEntity.getStockName(), userEntity);
                if (ruleEntityFromDb != null) {
                    ruleEntityFromDb.setMinStockValue(ruleEntity.getMinStockValue());
                    ruleEntityFromDb.setStockName(ruleEntity.getStockName());
                    ruleDAO.save(ruleEntityFromDb);
                    return ruleEntityFromDb;
                } else {
                    throw new RuleNotFoundException("RuleEntity does not exists");
                }
            } else {
                throw new UserNotFoundException("UserEntity does not exists");
            }
        } catch (Exception ex) {
            throw new Exception("Could not update user ruleEntity");
        }

    }
}
