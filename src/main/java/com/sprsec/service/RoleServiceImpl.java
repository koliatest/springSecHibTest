package com.sprsec.service;

import com.sprsec.dao.RoleDAO;
import com.sprsec.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDAO roleDAO;

    public Role getRole(int id)
    {
        return roleDAO.getRole(id);
    }

}