package com.example.my_automation.utils;


import org.testng.xml.XmlClass;

import java.util.ArrayList;
import java.util.List;

public class ListClasses {

    public static List<XmlClass> getTestClasses(String suite) {

      // List<XmlClass> listClasses = new ArrayList<>();
      // switch (suite) {
      //     case "Students":
      //         listClasses.add(new XmlClass(Projects.class));
      //         break;

      //     case "Subjects":
      //         listClasses.add(new XmlClass(Clients.class));
      //         break;

      //     case "Exams":
      //         listClasses.add(new XmlClass(Users.class));
      //         break;

      //     case "UsersAndLogIn":
      //         listClasses.add(new XmlClass(UsersAndGroupsManagement.class));
      //         break;

      //     case "RunAll":
      //         listClasses.add(new XmlClass(Projects.class));
      //         listClasses.add(new XmlClass(Clients.class));
      //         listClasses.add(new XmlClass(Users.class));
      //         listClasses.add(new XmlClass(UsersAndGroupsManagement.class));
      //         break;
      // }

        // return listClasses;
        return  new ArrayList<>();
    }
}
