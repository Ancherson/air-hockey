package air.hockey.prototype.udp;

import java.io.Serializable;

public class Personne implements Serializable {
     private String nom;
     private String prenom;
     private int age;

     public Personne(String n,String p, int a){
         nom = n;
         prenom = p;
         age = a;
     }
     public String toString(){
         return ("nom : "+nom+"\nprenom : "+prenom+"\nage : "+age);
     }
}
