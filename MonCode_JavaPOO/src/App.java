import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import model.Contact;

public class App {
    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        afficherMenu();
        while (true) {
            String choix = scan.nextLine();
            switch (choix) {
                case "1":
                    ajouterContact();
                    break;
                case "2":
                    listerContact();
                    break;
                case "3":
                    suprContact();
                    break;
                case "4":
                    modifyContact();
                    break;
                case "5":
                    cleanContactFile();
                    break;
                case "6":
                rechercheContact();
                    break;
                case "7":
                    SortByName();
                    break;
                case "8":
                    sortByDate();
                    break;
                case "q":
                    scan.close();
                    return;
                default:
                    System.out.println("Boulet!!!!");
                    break;
            }
            afficherMenu();
        }
    }

// --- RECHERCHER --------------------------------------------------------------------------------//
    private static void rechercheContact(){
        //il sort d'ou ce scanner ?
        System.out.println("Saisir le nom du contact recherché:");
        String NameContactToFind = (scan.nextLine()).toUpperCase(); //on rajoute To upper case pour que l'utilisateur puisse ecrire le nom sans se préoccuper de la CASE

        try {
            ArrayList<Contact> listeForResearch = Contact.lister();
            for (int i=0 ; i < listeForResearch.size() ; i++) {
                var contactCible = listeForResearch.get(i);
                if (NameContactToFind.equals(contactCible.getNom())) {
                    System.out.println(contactCible.getPrenom() + " " + contactCible.getNom() + " " + contactCible.getNumero() + " " + contactCible.getDateNaissance());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
 


// --- AFFICHER --------------------------------------------------------------------------------//
    private static void listerContact() {
        // Contact c = new Contact();
        try {
            //pas besoin d'instanscier un objet , 
            ArrayList<Contact> liste = Contact.lister();
            //je me suis permis de rajouter le numéro pour que l'utilisateur puisse l'utiliser pour cibler un contact unique
            for (Contact contact : liste) {
                System.out.println(contact.getPrenom() + " " + contact.getNom() + " " + contact.getNumero());
            }
        } catch (IOException e) {
            System.out.println("Erreur avec le fichier");
        }

    }



// --- TRIER --------------------------------------------------------------------------------//
     private static void SortByName() { 

        ArrayList<Contact> listToSort = new ArrayList<Contact>();
        try {
            listToSort = Contact.lister();
            Collections.sort(listToSort, new Comparator<Contact>() {
                public int compare(Contact c1, Contact c2)
                {
                    return Integer.valueOf((c1.getNom()).compareTo(c2.getNom()));
                }
            });

        
            //enregistrement des modification faite pour le tri 
            cleanContactFile();
            updateContactFile(listToSort);
            //affichage pour voir que la nouvelle version du registre de contact
            listerContact();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }


    
    private static void sortByDate() {
        ArrayList<Contact> listToSortDate = new ArrayList<Contact>();
        try {
            listToSortDate = Contact.lister();
            Collections.sort(listToSortDate, new Comparator<Contact>() {
                public int compare(Contact c1, Contact c2) 
                {
                    return (c1.getDateNaissance()).compareTo(c2.getDateNaissance());
                }
            });


         //enregistrement des modification faite pour le tri 
         cleanContactFile();
         updateContactFile(listToSortDate);
         //affichage pour voir que la nouvelle version du registre de contact
         listerContact();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




// --- MODIFIER --------------------------------------------------------------------------------//

    private static void modifyContact() 
    {
        listerContact();
        System.out.println("Indiquer le numéro du contact a modifier");
        String NumberContactToBeModify = scan.nextLine();
        System.out.println("Indiquer le champ a modifier parmis les suivant : Prénom - Nom - Mail - Numéro - DateDeNaissance");
        String CategoryToBeModify = scan.nextLine();
        System.out.println("Indiquer la nouvelle valeur de ce champs");
        String ModifyValue = scan.nextLine();


        ArrayList<Contact> listeForModify = new ArrayList<Contact>();
        try 
        {
            listeForModify = Contact.lister();
            //recherche du bon contact a modifier 
            for (int i=0 ; i < listeForModify.size() ; i++)
            {
                var ContactCible = listeForModify.get(i);
                if ( (ContactCible.getNumero()).equals(NumberContactToBeModify) ) 
                {
                    var ContactFinded = ContactCible;
                    System.out.println("Contact to modify Finded !");

                    //Application de la modification sur le contact trouvé
                    switch (CategoryToBeModify)
                    {
                        case "Nom":
                            ContactFinded.setNom(ModifyValue);
                            System.out.println("modification du Nom bien effectué ");
                            break;
                        case "Prénom":
                            ContactFinded.setPrenom(ModifyValue);
                            System.out.println("modification du Prénom bien effectué ");
                            break;
                        case "Mail":
                            try {
                                ContactFinded.setMail(ModifyValue);
                                System.out.println("modification du Mail bien effectué ");
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        case "Numéro":
                            try {
                                ContactFinded.setNumero(ModifyValue);
                                System.out.println("modification du Numéro bien effectué ");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "DateDeNaissance":
                            try {
                                ContactFinded.setDateNaissance(ModifyValue);
                                System.out.println("modification de la DateDeNaissance bien effectué ");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            }            

            //ENREGISTREMENT des modification
            cleanContactFile();
            updateContactFile(listeForModify);
            //affichage pour voir que la nouvelle version du registre de contact
            listerContact();

        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

        
    



// --- SUPPRIMER --------------------------------------------------------------------------------//
    private static void suprContact() {
        listerContact();
        //renitialiser le fichier contact.CSV ?
        //String ContactToSupr = "0404040404";

        System.out.println("Saisir le téléphone:");
        String ContactToSupr = scan.nextLine();
              
        
        try {
            ArrayList<Contact> listeForSupr = Contact.lister(); //renvoie une list

            //strictement inférieur sinon avec le délire de l'index 0 on dépassera, ex : size = 6 , ok donc 6 element, mais ducoup un index max de 5 parce que le premier element est a l'index 0
            for (int i=0 ; i < listeForSupr.size() ; i++) {

                var contactCible = listeForSupr.get(i); //on recupere l'objet contact a l'index i 
                System.out.println(contactCible.getNumero());
                String numeroCible = contactCible.getNumero();

                //DIFFERENTE MANIERE DE TESTER L'EGUALITE 
                //if (ContactToSupr == contactCible.getNumero()) {
                //System.out.println(numeroCible.equalsIgnoreCase(ContactToSupr));
                //System.out.println(numeroCible.equals(ContactToSupr));
                //if (numeroCible == ContactToSupr) { 

                if (numeroCible.equals(ContactToSupr)) {
                    listeForSupr.remove(i);
                    System.out.println("le contact a bien été suprimé");
                }
                else {
                    System.out.println("ce contact n'est pas celui a suprimé");
                }
            }

            System.out.println("fin de la supression");
            //update contact file (.CSV) after loop for finding and deleting cibleContact inside
            updateContactFile(listeForSupr);


        } catch (IOException e) {
            System.out.println("Erreur avec le fichier");
        }
    }



// --- FONCTION GENERALE --------------------------------------------------------------------------------//

    private static void updateContactFile(ArrayList<Contact> modifiedList){
        cleanContactFile();
        for (int i=0 ; i < modifiedList.size() ; i++){
            var ContactToPrint = modifiedList.get(i);
            try {
                ContactToPrint.enregistrer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void cleanContactFile(){
        try {
            FileWriter cleaner = new FileWriter("Contacts.csv");
            cleaner.write(""); //enregistre une ligne complete en allant a la ligne, marche pour tout les os

        } catch (IOException e) {
            e.printStackTrace();
        } 
    }



    private static void ajouterContact() {

        Contact c = new Contact();
        System.out.println("Saisir le nom:");
        c.setNom(scan.nextLine());
        System.out.println("Saisir le prénom:");
        c.setPrenom(scan.nextLine());

        do {
            try {
                System.out.println("Saisir le téléphone:");
                c.setNumero(scan.nextLine());
                break;
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
        } while (true);

        do {
            try {
                System.out.println("Saisir le mail:");
                c.setMail(scan.nextLine());
                break;
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
        } while (true);

        do {
            try {
                System.out.println("Saisir la date de naissance:");
                c.setDateNaissance(scan.nextLine());
                break;
            } catch (ParseException e) {
                System.out.println("Error, try again!");
            }
        } while (true);

        try {
            c.enregistrer();
            System.out.println("Contact enregistré.");
        } catch (IOException e) {
            System.out.println("Erreur à l'enregistrement");
        }

    }


    public static void afficherMenu() {
        // 1
        /*
         * System.out.println("-- MENU --");
         * System.out.println("1- Ajouter un contact");
         * System.out.println("2- Lister les contacts");
         * System.out.println("q- Quitter");
         */
        // 2
        ArrayList<String> menus = new ArrayList<>();
        menus.add("-- MENU --");
        menus.add("1- Ajouter un contact");
        menus.add("2- Lister les contacts");
        menus.add("3- Suprimer un contacts");
        menus.add("4- Modifier un contacts");
        menus.add("5- !!! Nettoyer la liste de contacts !!!");
        menus.add("6- Rechercher un contact par nom");
        menus.add("7- Trier les Contact enregistré par nom");
        menus.add("8- Trier les Contact enregistré par Date");
        menus.add("q- Quitter");
        for (String s : menus) {
            System.out.println(s);
        }
    }
}