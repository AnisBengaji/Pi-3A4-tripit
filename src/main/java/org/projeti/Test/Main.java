package org.projeti.Test;

import org.projeti.Service.DestinationService;
import org.projeti.entites.Activity;
import org.projeti.entites.Destination;
import org.projeti.utils.Database;

import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Destination dest = new Destination("tunisie","sousse",7894,30.123f,10.23f);

        DestinationService destService = new DestinationService();
        try {
            destService.delete(1);
            System.out.println(destService.showAll());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}