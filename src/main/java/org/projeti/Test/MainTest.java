/*package org.projeti.Test;

import org.projeti.Service.OffreService;
import org.projeti.Service.TutorialService;
import org.projeti.entites.Offre;
import org.projeti.entites.Tutorial;
import org.projeti.utils.Database;
import java.sql.SQLException;
import java.util.List;

public class MainTest {
    public static void main(String[] args) {
        Database m1 = Database.getInstance();
        Offre o1 =new Offre("moklo","azertyu",55,"lina","admin");
        Offre o2 =new Offre("tekaya","azertyugfd",66,"aaa","utilisateur");

        OffreService us = new OffreService();


        Tutorial t1 =new Tutorial("tekaya","2020-12-12","2020-12-12",55,"admin");
        Tutorial t2 =new Tutorial("tekaya","azertyugfd","aaa",66,"utilisateur");
        TutorialService y1 = new TutorialService();
        try{
            //us.insert(o1);
           // us.insert1(o2);
            System.out.println(us.showAll());
            List<Offre> offre = us.showAll();
            y1.insert(t1);
            y1.insert2(t2);
            System.out.println(us.showAll());
            List<Tutorial> tutorials = y1.showAll();


           Offre updaated =new Offre(6,"tekaya","azertyugfd",66,"ammmek","utilisateur");
            us.update(updaated);

           // us.delete(updaated);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
*/