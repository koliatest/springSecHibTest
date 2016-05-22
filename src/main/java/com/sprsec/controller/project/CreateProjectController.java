package com.sprsec.controller.project;

import com.sprsec.dto.ProjectDto;
import com.sprsec.model.Project;
import com.sprsec.model.User;
import com.sprsec.service.projectService.ProjectService;
import com.sprsec.service.userService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CreateProjectController
{
    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @RequestMapping(value = "/project/create", method = RequestMethod.GET)
    public ModelAndView createProjectGet()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUser(auth.getName());

        ModelAndView modelAndView = new ModelAndView("project/project-create-page");

        modelAndView.addObject("currentUser", currentUser);
        modelAndView.addObject("dto", new ProjectDto());
        modelAndView.addObject("userList", userService.listOfUsers());
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @RequestMapping(value = "/project/create", method = RequestMethod.POST)
    public String createProjectPost(@ModelAttribute(value = "dto") ProjectDto dto)
    {
        Project project = new Project();
        project.setNameOfTheProject(dto.getNameOfTheProject());
        project.setDescriptionOfTheProject(dto.getDescriptionOfTheProject());
        project.setLeadOfTheProject(userService.getUser(dto.getLeadOfTheProject()));
        String dtoLogins = dto.getUsersInTheCurrentProject();
        if(dtoLogins.length() > 0)
        {
            dtoLogins = dtoLogins.trim();
            String[] logins = dtoLogins.split(";");
            for (String login : logins)
            {
                project.getUsersInTheCurrentProject().add(userService.getUser(login.trim()));
            }
        }
        projectService.createProject(project);
        return "redirect:/profile";
    }
}
