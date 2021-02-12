package air.hockey.prototype.udp;

public class Personne {
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
