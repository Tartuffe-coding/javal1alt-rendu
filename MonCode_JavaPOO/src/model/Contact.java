package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Contact {
    public static final String SEPARATEUR = ";";

    private String nom;
    private String prenom;
    private String numero;
    private String mail;
    private Date dateNaissance;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom.toUpperCase();
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    //getter, return quelque chose
    public String getNumero() {
        return numero;
    }

    //setter ne return pas, il gere l'affectation et la vérification avant
    public void setNumero(String numero) throws ParseException {
        Pattern pat = Pattern.compile("^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$");
        Matcher test = pat.matcher(numero);
        if (test.matches()) {
            this.numero = numero;
        } else {
            throw new ParseException("Format du numéro incorrect.", 0);
        }
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) throws ParseException {
        Pattern pat = Pattern.compile("^[a-zA-Z0-9_.-]+@{1}[a-zA-Z0-9_.-]{2,}\\.[a-zA-Z.]{2,10}$");
        Matcher test = pat.matcher(mail);
        if (test.matches()) {
            this.mail = mail;
        } else {
            throw new ParseException("Format du mail incorrect.", 0);
        }
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) throws ParseException {
        SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy");
        this.dateNaissance = dtf.parse(dateNaissance);
    }

    //fonction qui vient appeler notre fonction toString polymorpher , recupère la string qui est return et le balance dans 
    //ça sera pas mieux de placer enregistrer
    public void enregistrer() throws IOException {
        /*
         * try {
         * FileWriter writer = new FileWriter("contacts.csv", true);
         * writer.write(this.toString());
         * writer.close();
         * } catch (IOException e) {
         * e.printStackTrace();
         * }
         */

         //ici le PO a instancier un printWriter parce que c'est un objet qui possede la fonction .println qui permet de print QUE SUR UNE LIGNE, ça gère le passage a la ligne peut importe l'os , pas besoin de print des "\n"
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("contacts.csv", true)));
        try {

            pw.println(this.toString()); //enregistre une ligne complete en allant a la ligne, marche pour tout les os

        } finally {
            pw.close();
        }
    }


    //fonction générale de la classe, pas une fonction appartenant a chaque objet
    //donc c'est une fonction qui va nous permettre de toucher et faire des choses sur tout nos objet d'un coup
    public static ArrayList<Contact> lister() throws IOException {
        ArrayList<Contact> list = new ArrayList<>();
        BufferedReader buf = new BufferedReader(new FileReader("contacts.csv"));
        try {
            String ligne = buf.readLine();
            while (ligne != null) {
                //on créer un tableau remplie des différent donné recupéré sur une ligne du fichier texte, comme il sont toujours dans le meme ordre, on sait que le prénom est a tel index , de meme pour le numéro etc...
                String[] tab = ligne.split(SEPARATEUR);
                Contact c = new Contact();      
                c.setNom(tab[0]);
                c.setPrenom(tab[1]);
                c.setMail(tab[2]);
                c.setNumero(tab[3]);
                c.setDateNaissance(tab[4]);
                list.add(c);
                ligne = buf.readLine();
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Erreur de lecture sur le fichier");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("erreur l'index chercher est en dehor des limittes");
        } finally {
            buf.close();
        }
        return list; //ça il faut le stocker quelque part 

        //Attention add contact n'ajoute pas directement dans l'arraylist, lui il creer juste une ligne de texte
        //mais notre arrayList n'est pas actualiser
        //Si on mettais juste notre list return en global
        //bah on pourrait pas se permettre de ne manipuler notre programme que comme ça 
        //parce que si entre temps on a fait un addContact, a aucun moment notre arraylist global n'au
    }


    //fonction qui va nous assemblé nos différent champs/info en un seul long string a pouvoir ecrire dans un fichier
    //on fait donc une conquaténation plus efficacce qu'avec des + et on rajoute des ; pour correspondre au bon format
    @Override
    public String toString() {
        // return this.getNom() + ";" + this.getPrenom();*
        StringBuilder build = new StringBuilder();
        build.append(getNom());
        build.append(SEPARATEUR);
        build.append(getPrenom());
        build.append(SEPARATEUR);
        build.append(getMail());
        build.append(SEPARATEUR);
        build.append(getNumero());
        build.append(SEPARATEUR);
        SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy");
        build.append(dtf.format(getDateNaissance()));


        // pourquoi on return pas juste build ? //On auto appel la fonction ???
        //transforme notre objet stringbuilder en un string classique
        return build.toString(); 
        //la la fonction toString appeler c'est pas la tosring qu'on vient d'overrider, c'est une autre, c'est celle de la classe Stringbuilder qui permet de transformer

        //pour etre plus précis, la classe stringbuilder aussi possede Tosting hérité de la classe objet
        // et elle aussi est orverrider mais directement dans le code sourcfe, 
        

        //plutot que de renvoyer le nom de la classe et l'adresse memoire elle te renvoie en string ce que tu as mit dans ton builder
    }

}