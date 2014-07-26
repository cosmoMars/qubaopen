package com.qubaopen.survey.entity.manager;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Created by duel on 2014/6/24.
 * manager与role 通过manytomany 中间表manager_role_relation表来维护
 * manager与authority 通过 manytomany 中间表manager_authority_relation表来维护
 */


//@Entity
//@Table(name = "MANAGER_ROLE_AUTHORITY")
public class ManagerRoleAuthority extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 8808758113705170130L;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manager_role_id")
    private ManagerRole managerRole;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manager_authority_id")
    private ManagerAuthority managerAuthority;

    public ManagerRole getManagerRole() {
        return managerRole;
    }

    public void setManagerRole(ManagerRole managerRole) {
        this.managerRole = managerRole;
    }

    public ManagerAuthority getManagerAuthority() {
        return managerAuthority;
    }

    public void setManagerAuthority(ManagerAuthority managerAuthority) {
        this.managerAuthority = managerAuthority;
    }
}