package com.sprsec.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "project")
public class Project
{
    @Id
    @GeneratedValue
    @Column
    private Integer id;

    @Column
    private String nameOfTheProject;

    @Column
    private String descriptionOfTheProject;

    @OneToOne
    @JoinColumn(name = "leadOfTheProject_id")
    private User leadOfTheProject;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="project_user",
            joinColumns = @JoinColumn(name="proj_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name="usr_id", referencedColumnName="id")
    )
    private Set<User> usersInTheCurrentProject;

    public Project() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameOfTheProject() {
        return nameOfTheProject;
    }

    public void setNameOfTheProject(String nameOfTheProject) {
        this.nameOfTheProject = nameOfTheProject;
    }

    public String getDescriptionOfTheProject() {
        return descriptionOfTheProject;
    }

    public void setDescriptionOfTheProject(String descriptionOfTheProject) {
        this.descriptionOfTheProject = descriptionOfTheProject;
    }

    public User getLeadOfTheProject() {
        return leadOfTheProject;
    }

    public void setLeadOfTheProject(User leadOfTheProject) {
        this.leadOfTheProject = leadOfTheProject;
    }

    public Set<User> getUsersInTheCurrentProject() {
        return usersInTheCurrentProject;
    }

    public void setUsersInTheCurrentProject(Set<User> usersInTheCurrentProject) {
        this.usersInTheCurrentProject = usersInTheCurrentProject;
    }
}
