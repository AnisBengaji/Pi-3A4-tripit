/*package org.projeti.Test;
import org.projeti.Service.CategorieService;
import org.projeti.entites.Categorie;
import org.projeti.entites.Publication;
import org.projeti.Service.PublicationService;
import org.projeti.utils.Database;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class MainTest {

    public static void main(String[] args) {
        Database m1 = Database.getInstance();
        Date sqlDate = Date.valueOf("1987-12-21");
       //  Publication p1 = new Publication("new","w",sqlDate,"t","f","c:/");
        //Publication p2 = new Publication(12,"Jery","fev",sqlDate,"ahmed","visible","url2");

        PublicationService publicationService = new PublicationService();
        CategorieService categorieService = new CategorieService();
        Categorie categorie = new Categorie("sci_fii12", "film sci_fi2025", null);


        try {
            List<Publication> publications = publicationService.showAll();
            //publicationService.insert(p1);
            //publicationService.insert(p2);

            System.out.println(publicationService.showAll());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        try {
            Publication publicationDelete = new Publication();
            publicationDelete.setId_publication(4);

            int rowsDeleted = publicationService.delete(publicationDelete);

            if (rowsDeleted > 0) {
                System.out.println("publication supprimer avec succés");
            } else {
                System.out.println("aucune publication avec cette id");
            }
        } catch (SQLException e) {
        }


        try {
            Publication publicationUpdate = new Publication();
            publicationUpdate.setId_publication(199);
            publicationUpdate.setTitle("titre 200");
            publicationUpdate.setContenu("contenu 200");
            publicationUpdate.setDate_publication(java.sql.Date.valueOf("2025-02-10"));
            publicationUpdate.setAuthor("anis bengaji");
            publicationUpdate.setVisibility("public");
            publicationUpdate.setImage("new_image_url.jpg22");

            int rowsUpdated = publicationService.update(publicationUpdate);

            if (rowsUpdated > 0) {
                System.out.println("publication mis a jour avec success !");
            } else {
                System.out.println("aucune pub avec cette id");
            }
        } catch (SQLException e) {

        }


        //crud categorie:D


        try {
            List<Publication> publications = new ArrayList<>();
            Categorie categorieToInsert = new Categorie("Category Name", "Category Description", publications);

            int rowsInserted = categorieService.insert(categorieToInsert);
            if (rowsInserted > 0) {
                System.out.println("Categorie ajoutée avec succès");
            } else {
                System.out.println("Erreur lors de l'ajout de la catégorie");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion de la catégorie : " + e.getMessage());
        }







        try {
            Categorie categorieDelete = new Categorie();
            categorieDelete.setIdCategorie(8);  // Assuming idCategorie 4 exists

            int rowsDeleted = categorieService.delete(categorieDelete);
            if (rowsDeleted > 0) {
                System.out.println("Categorie supprimée avec succès");
            } else {
                System.out.println("Aucune catégorie avec cet id");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la catégorie : " + e.getMessage());
        }



        try {

            Categorie categorieToUpdate = new Categorie("nom mis a jour", "description mis a jour", null);
            categorieToUpdate.setIdCategorie(7);

            int rowsUpdated = categorieService.update(categorieToUpdate);
            if (rowsUpdated > 0) {
                System.out.println("Categorie mise à jour avec succès");
            } else {
                System.out.println("Aucune catégorie avec cet id ");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la catégorie : " + e.getMessage());
        }
    }}


*/










