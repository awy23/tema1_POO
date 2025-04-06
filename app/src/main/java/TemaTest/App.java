package TemaTest;
import java.lang.String;
import java.util.Scanner;

public class App {

    public App() {
    }

    public static class Utilizator {
        String username;
        String password;

        public Utilizator(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public void clear() {
            this.username = null;
            this.password = null;
        }


    }

    public static class Postare extends Utilizator {
        String text;

        public Postare(String username, String password, String text) {
            super(username, password);
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void clear() {
            this.text = null;
        }
    }

    public static class Comentariu extends Postare {
        String text;

        public Comentariu(String username, String password, String text) {
            super(username, password, text);
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void clear() {
            this.text = null;
        }
    }

    public static void main(java.lang.String[] strings) {

        Utilizator[] utilizatori = new Utilizator[1000];
        Postare[] postari = new Postare[1000];
        Integer nrUtilizatori = 0, nrPostari = 0;
        //verifica daca strings e null si nu avem argumente
        if (strings == null) {
            System.out.println("Hello world!");
        } else if (strings[0].equals("-create-user")) { //cazul in care primul argument e -create-user
            if (strings.length == 1) {
                System.out.println("{ 'status' : 'error', 'message' : 'Please provide username'}");
            } else if (strings.length == 2) {
                System.out.println("{ 'status' : 'error', 'message' : 'Please provide password'}");
            } else if (strings[1].contains("-u") && strings[2].contains("-p")) {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                Utilizator utilizator = new Utilizator(username, password);
                try (java.io.FileWriter fileWriter = new java.io.FileWriter("utilizatori.txt", true)) {
                    //verificam daca fisierul e gol sau nu
                    if (fileWriter == null) {
                        System.out.println("{ 'status' : 'ok', 'message' : 'User created successfully'}");
                        //adaugam utilizatorul in fisier
                        fileWriter.write(utilizator.getUsername() + " " + utilizator.getPassword() + "\n");
                    } else {
                        //verificam daca utilizatorul exista deja
                        Scanner scanner = new Scanner(new java.io.File("utilizatori.txt"));
                        boolean exista = false;
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            String[] words = line.split(" ");
                            if (words[0].equals(username)) {
                                exista = true;
                                System.out.println("{ 'status' : 'error', 'message' : 'User already exists'}");
                                break;
                            }
                        }
                        //daca nu exista il adaugam in fisier
                        if (!exista) {
                            fileWriter.write(utilizator.getUsername() + " " + utilizator.getPassword() + "\n");
                            System.out.println("{ 'status' : 'ok', 'message' : 'User created successfully'}");
                        }


                    }
                } catch (java.io.IOException e) {
                }

            }
        } else if (strings[0].equals("-create-post")) {
            if (strings.length == 1) {
                System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            } else if (strings.length == 2) {

                System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            } else if (strings[1].contains("-u") && strings[2].contains("-p") && strings.length == 3) {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                //verificam daca exista username si parola in fisierul utilizatori.txt
                Scanner scanner = null;
                boolean exista = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if (words[0].equals(username)) {
                            exista = true;
                            if (words[1].equals(password)) {
                                System.out.println("{ 'status' : 'error', 'message' : 'No text provided'}");

                            } else {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                        }
                    }
                } catch (java.io.FileNotFoundException e) {
                }
                if (exista == false) {
                    System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                }
            } else if (strings[1].contains("-u") && strings[2].contains("-p") && strings[3].contains("-text")) {//cazul in care avem toate argumentele
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                String text = strings[3].substring(6, strings[3].length());
                Scanner scanner = null;
                if (text.length() < 300) {
                    Postare postare = new Postare(username, password, text);
                    Scanner scanner1 = null;
                    try {
                        scanner = new Scanner(new java.io.File("utilizatori.txt"));
                        boolean exista = false;
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            String[] words = line.split(" ");
                            if (words[0].equals(username)) {
                                exista = true;
                                if (words[1].equals(password)) {
                                    try (java.io.FileWriter fileWriter = new java.io.FileWriter("postari.txt", true)) {
                                        //adaugam postarea in fisier
                                        fileWriter.write(postare.getUsername() + " " + postare.getPassword() + " " + postare.getText() + "\n");
                                        System.out.println("{ 'status' : 'ok', 'message' : 'Post added successfully'}");
                                    } catch (java.io.IOException e) {
                                    }
                                } else {
                                    System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                                }
                            } else if (!exista) {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                        }
                    } catch (java.io.FileNotFoundException e) {
                    }
                } else {
                    System.out.println("{ 'status' : 'error', 'message' : 'Post text length exceeded'}");
                }
            }
        } else if (strings[0].equals("-delete-post-by-id")) {
            if (strings.length == 1 || strings.length == 2) {
                System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            } else if (strings[1].contains("-u") && strings[2].contains("-p") && strings.length == 3) {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                Scanner scanner = null;
                boolean exista = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if (words[0].equals(username)) {
                            exista = true;
                            if (words[1].equals(password)) {
                                System.out.println("{ 'status' : 'error', 'message' : 'No identifier was provided'}");
                            } else {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                        }
                    }
                    if (exista == false) {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                } catch (java.io.FileNotFoundException e) {
                }
            } else if (strings[1].contains("-u") && strings[2].contains("-p") && strings[3].contains("-id")) {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                String idtext = strings[3].substring(4, strings[3].length());
                char id = idtext.charAt(1);

                Scanner scanner = null;
                boolean exista = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if (words[0].equals(username)) {
                            exista = true;
                            if (words[1].equals(password)) {

                                Integer id1 = Character.getNumericValue(id);
                                Integer contor = 1;
                               //numaram postarile pentru a afla id ul postarii
                                Scanner scanner1 = null;
                                try {
                                    scanner1 = new Scanner(new java.io.File("postari.txt"));
                                    boolean exista1 = false;
                                    while (scanner1.hasNextLine()) {
                                        String line1 = scanner1.nextLine();
                                        String[] words1 = line1.split(" ");
                                        if (contor == id1) {
                                            exista1 = true;
                                            //stergem postarea din fisier daca exista
                                            try (java.io.FileWriter fileWriter = new java.io.FileWriter("postari.txt", false)) {
                                                System.out.println("{ 'status' : 'ok', 'message' : 'Post deleted successfully'}");
                                            } catch (java.io.IOException e) {
                                            }
                                            contor++;
                                        }
                                    }
                                    if (exista1 == false) {
                                        System.out.println("{ 'status' : 'error', 'message' : 'The identifier was not valid'}");
                                    }
                                } catch (java.io.FileNotFoundException e) {
                                }
                            } else {

                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                        } else if (!exista) {
                            System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                        }
                    }
                } catch (java.io.FileNotFoundException e) {
                }
            }
        } else if (strings[0].equals("-follow-user-by-username")) {
            if (strings.length == 1 || strings.length == 2) {
                System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            } else if (strings[1].contains("-u") && strings[2].contains("-p") && strings.length == 3) {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                Scanner scanner = null;
                boolean exista = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if (words[0].equals(username)) {
                            exista = true;
                            if (words[1].equals(password)) {
                                System.out.println("{ 'status' : 'error', 'message' : 'No username to follow was provided'}");
                            } else {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                        }
                    }
                    if (exista == false) {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                } catch (java.io.FileNotFoundException e) {
                }
            } else if (strings[1].contains("-u") && strings[2].contains("-p") && strings[3].contains("-username")) {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                String username1 = strings[3].substring(10, strings[3].length());
                Scanner scanner = null;
                boolean exista = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if (words[0].equals(username)) {
                            exista = true;
                            if (words[1].equals(password)) {
                                Scanner scanner1 = null;
                                boolean exista1 = false;
                                try {
                                    scanner1 = new Scanner(new java.io.File("utilizatori.txt"));
                                    //verificam daca al doilea username exista pentru a primi follow
                                    while (scanner1.hasNextLine()) {
                                        String line1 = scanner1.nextLine();
                                        String[] words1 = line1.split(" ");
                                        if (words1[0].equals(username1)) {
                                            exista1 = true;
                                            //verificam daca il are deja la follow
                                            Scanner scanner2 = null;
                                            boolean exista2 = false;
                                            try {
                                                scanner2 = new Scanner(new java.io.File("follow.txt"));
                                                while (scanner2.hasNextLine()) {
                                                    String line2 = scanner2.nextLine();
                                                    String[] words2 = line2.split(" ");
                                                    if (words2[0].equals(username) && words2[1].equals(username1)) {
                                                        exista2 = true;
                                                        System.out.println("{ 'status' : 'error', 'message' : 'The username to follow was not valid'}");
                                                    }
                                                }
                                            } catch (java.io.FileNotFoundException e) {
                                            }
                                            if (exista2 == false) {
                                                //adaugam username si username1 in fisierul follow.txt
                                                try (java.io.FileWriter fileWriter = new java.io.FileWriter("follow.txt", true)) {
                                                    fileWriter.write(username + " " + username1 + "\n");
                                                    System.out.println("{ 'status' : 'ok', 'message' : 'Operation executed successfully'}");
                                                } catch (java.io.IOException e) {
                                                }
                                            }
                                        }
                                    }

                                    if (exista1 == false) {
                                        System.out.println("{ 'status' : 'error', 'message' : 'The username to follow was not valid'}");
                                    }
                                } catch (java.io.FileNotFoundException e) {
                                }
                            } else {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                        } else if (!exista) {
                            System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                        }
                    }
                } catch (java.io.FileNotFoundException e) {

                }
            }

        } else if (strings[0].equals("-unfollow-user-by-username")) {
            if (strings.length == 1 || strings.length == 2) {
                System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            } else if (strings[1].contains("-u") && strings[2].contains("-p") && strings.length == 3) {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                Scanner scanner = null;
                boolean exista = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if (words[0].equals(username)) {
                            exista = true;
                            if (words[1].equals(password)) {
                                System.out.println("{ 'status' : 'error', 'message' : 'No username to unfollow was provided'}");
                            } else {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                        }
                    }
                    if (exista == false) {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                } catch (java.io.FileNotFoundException e) {
                }
            } else if (strings[1].contains("-u") && strings[2].contains("-p") && strings[3].contains("-username")) {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                String username1 = strings[3].substring(10, strings[3].length());
                Scanner scanner = null;
                boolean exista = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if (words[0].equals(username)) {
                            exista = true;
                            if (words[1].equals(password)) {
                                Scanner scanner1 = null;
                                boolean exista1 = false;
                                try {
                                    scanner1 = new Scanner(new java.io.File("utilizatori.txt"));
                                    while (scanner1.hasNextLine()) {
                                        String line1 = scanner1.nextLine();
                                        String[] words1 = line1.split(" ");
                                        if (words1[0].equals(username1)) {
                                            exista1 = true;
                                            //verificam daca il mai are la follow
                                            Scanner scanner2 = null;
                                            boolean exista2 = false;
                                            try {
                                                scanner2 = new Scanner(new java.io.File("follow.txt"));
                                                while (scanner2.hasNextLine()) {
                                                    String line2 = scanner2.nextLine();
                                                    String[] words2 = line2.split(" ");
                                                    if (words2[0].equals(username) && words2[1].equals(username1)) {
                                                        exista2 = true;
                                                        //stergem linia cu follow-ul respectiv
                                                        try (java.io.FileWriter fileWriter = new java.io.FileWriter("follow.txt", false)) {
                                                            System.out.println("{ 'status' : 'ok', 'message' : 'Operation executed successfully'}");
                                                        } catch (java.io.IOException e) {
                                                        }
                                                    }
                                                }
                                            } catch (java.io.FileNotFoundException e) {
                                            }
                                            if (exista2 == false) {
                                                System.out.println("{ 'status' : 'error', 'message' : 'The username to unfollow was not valid'}");
                                            }
                                        }
                                    }
                                    if (exista1 == false) {
                                        System.out.println("{ 'status' : 'error', 'message' : 'The username to unfollow was not valid'}");
                                    }
                                } catch (java.io.FileNotFoundException e) {
                                }
                            }

                        }
                    }
                } catch (java.io.FileNotFoundException e) {
                }
            }
        } else if (strings[0].equals("-like-post")) {
            if (strings.length == 1 || strings.length == 2) {
                System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            } else if (strings[1].contains("-u") && strings[2].contains("-p") && strings.length == 3) {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                Scanner scanner = null;
                boolean exista = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if (words[0].equals(username)) {
                            exista = true;
                            if (words[1].equals(password)) {
                                System.out.println("{ 'status' : 'error', 'message' : 'No post identifier to like was provided'}");
                            } else {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                        }
                    }
                    if (exista == false) {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                } catch (java.io.FileNotFoundException e) {
                }
            } else if (strings[1].contains("-u") && strings[2].contains("-p") && strings[3].contains("-post-id")) {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                String idtext = strings[3].substring(9, strings[3].length());
                char id = idtext.charAt(1);

                Scanner scanner = null;
                boolean exista = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if (words[0].equals(username)) {
                            exista = true;
                            if (words[1].equals(password)) {

                            }
                            Integer id1 = Character.getNumericValue(id);
                            Integer contor = 1;
                            Scanner scanner1 = null;
                            try {
                                scanner1 = new Scanner(new java.io.File("postari.txt"));
                                boolean exista1 = false;
                                while (scanner1.hasNextLine()) {
                                    String line1 = scanner1.nextLine();
                                    String[] words1 = line1.split(" ");
                                    if (contor == id1) { //daca am gasit postarea cu id ul respectiv
                                        exista1 = true;
                                        Scanner scanner2 = null;
                                        boolean exista2 = false;
                                        try {
                                            scanner2 = new Scanner(new java.io.File("like.txt"));
                                            while (scanner2.hasNextLine()) {
                                                String line2 = scanner2.nextLine();
                                                String[] words2 = line2.split(" ");
                                                if (words2[0].equals(username) && words2[1].equals(idtext)) {
                                                    exista2 = true;
                                                    System.out.println("{ 'status' : 'error', 'message' : 'The post identifier to like was not valid'}");
                                                }
                                            }
                                        } catch (java.io.FileNotFoundException e) {
                                        }
                                        if (exista2 == false) {
                                            //adaugam like-ul la postare
                                            try (java.io.FileWriter fileWriter = new java.io.FileWriter("like.txt", true)) {
                                                fileWriter.write(username + " " + idtext + "\n");
                                                System.out.println("{ 'status' : 'ok', 'message' : 'Operation executed successfully'}");
                                            } catch (java.io.IOException e) {
                                            }
                                        }
                                    }
                                    contor++;
                                }
                                if (exista1 == false) {
                                    System.out.println("{ 'status' : 'error', 'message' : 'The post identifier to like was not valid'}");
                                }
                            } catch (java.io.FileNotFoundException e) {
                            }
                        }
                    }
                } catch (java.io.FileNotFoundException e) {
                }
            }
        } else if (strings[0].equals("-unlike-post")) {
            if (strings.length == 1 || strings.length == 2) {
                System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            } else if (strings[1].contains("-u") && strings[2].contains("-p") && strings.length == 3) {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                Scanner scanner = null;
                boolean exista = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if (words[0].equals(username)) {
                            exista = true;
                            if (words[1].equals(password)) {
                                System.out.println("{ 'status' : 'error', 'message' : 'No post identifier to unlike was provided'}");
                            } else {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                        }
                    }
                    if (exista == false) {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                } catch (java.io.FileNotFoundException e) {
                }
            } else if (strings[1].contains("-u") && strings[2].contains("-p") && strings[3].contains("-post-id")) {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                String idtext = strings[3].substring(9, strings[3].length());
                char id = idtext.charAt(1);

                Scanner scanner = null;
                boolean exista = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if (words[0].equals(username)) {
                            exista = true;
                            if (words[1].equals(password)) {

                            }
                            Integer id1 = Character.getNumericValue(id);
                            Integer contor = 1;
                            Scanner scanner1 = null;
                            try {
                                scanner1 = new Scanner(new java.io.File("postari.txt"));
                                boolean exista1 = false;
                                while (scanner1.hasNextLine()) {
                                    String line1 = scanner1.nextLine();
                                    String[] words1 = line1.split(" ");
                                    if (contor == id1) {
                                        exista1 = true;
                                        Scanner scanner2 = null;
                                        boolean exista2 = false;
                                        try {
                                            scanner2 = new Scanner(new java.io.File("like.txt"));
                                            while (scanner2.hasNextLine()) {
                                                String line2 = scanner2.nextLine();
                                                String[] words2 = line2.split(" ");
                                                if (words2[0].equals(username) && words2[1].equals(idtext)) {
                                                    exista2 = true;
                                                    try (java.io.FileWriter fileWriter = new java.io.FileWriter("like.txt", false)) {
                                                        System.out.println("{ 'status' : 'ok', 'message' : 'Operation executed successfully'}");
                                                    } catch (java.io.IOException e) {
                                                    }
                                                }
                                            }
                                        } catch (java.io.FileNotFoundException e) {
                                        }
                                        if (exista2 == false) {
                                            System.out.println("{ 'status' : 'error', 'message' : 'The post identifier to unlike was not valid'}");
                                        }
                                    }
                                    contor++;
                                }
                                if (exista1 == false) {
                                    System.out.println("{ 'status' : 'error', 'message' : 'The post identifier to unlike was not valid'}");
                                }
                            } catch (java.io.FileNotFoundException e) {
                            }
                        }
                    }
                } catch (java.io.FileNotFoundException e) {
                }
            }
        } else if (strings[0].contains("-comment-post")) {
            if (strings.length == 1 || strings.length == 2)
                System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            else if (strings[1].contains("-u") && strings[2].contains("-p") && strings.length == 3) {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                Scanner scanner = null;
                boolean exista = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if (words[0].equals(username)) {
                            exista = true;
                            if (!words[1].equals(password)) {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            } else if (words[1].equals(password)) {
                                System.out.println("{ 'status' : 'error', 'message' : 'No text provided'}");
                            }

                        }
                    }
                    if (exista == false) {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                } catch (java.io.FileNotFoundException e) {
                }
            } else if (strings[1].contains("-u") && strings[2].contains("-p") && strings[3].contains("-post-id") && strings[4].contains("-text")) {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                String idtext = strings[3].substring(9, strings[3].length());
                char id = idtext.charAt(1);
                String text = strings[4].substring(6, strings[4].length());
                Scanner scanner = null;
                boolean exista1 = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if (words[0].equals(username)) {
                            exista1 = true;
                            if (words[1].equals(password)) {
                                if (text.length() > 300) {
                                    System.out.println("{ 'status' : 'error', 'message' : 'Comment text length exceeded'}");
                                } else {
                                    Integer id1 = Character.getNumericValue(id);
                                    Integer contor = 1;
                                    //parcurge fisierul postari.txt si cauta postarile care au username ul si contorizeaza le de la 1
                                    Scanner scanner1 = null;
                                    try {
                                        scanner1 = new Scanner(new java.io.File("postari.txt"));
                                        //cautam postarea sa vedem daca e valida
                                        boolean exista2 = false;
                                        while (scanner1.hasNextLine()) {
                                            String line1 = scanner1.nextLine();
                                            String[] words1 = line1.split(" ");
                                            if (contor == id1) {
                                                exista2 = true;
                                                //adaugam comentariul in fisierul comment.txt
                                                try (java.io.FileWriter fileWriter = new java.io.FileWriter("comment.txt", true)) {
                                                    fileWriter.write(username + " " + idtext + " " + text + "\n");
                                                    System.out.println("{ 'status' : 'ok', 'message' : 'Comment added successfully'}");
                                                } catch (java.io.IOException e) {
                                                }
                                            }
                                            contor++;
                                        }
                                        if (exista2 == false) {
                                            System.out.println("{ 'status' : 'error', 'message' : 'The post identifier to comment was not valid'}");
                                        }
                                    } catch (java.io.FileNotFoundException e) {
                                    }
                                }

                            } else {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                        }
                    }
                    if (exista1 == false) {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                } catch (java.io.FileNotFoundException e) {
                }
            }

        }
        else if(strings[0].equals("-delete-comment-by-id"))
        {
            if(strings.length == 1 || strings.length == 2 )
            {
                System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            }
            else if(strings[1].contains("-u") && strings[2].contains("-p") && strings.length == 3)
            {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                Scanner scanner = null;
                boolean exista1 = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while(scanner.hasNextLine())
                    {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if(words[0].equals(username))
                        {
                            exista1 = true;
                            if(words[1].equals(password))
                            {
                                System.out.println("{ 'status' : 'error', 'message' : 'No identifier was provided'}");
                            }
                            else
                            {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                        }
                    }
                    if(exista1 == false)
                    {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                }
                catch(java.io.FileNotFoundException e)
                {
                }
            }
            else if(strings[1].contains("-u") && strings[2].contains("-p") && strings[3].contains("-id"))
            {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                String idtext = strings[3].substring(4, strings[3].length());
                char id = idtext.charAt(1);

                Scanner scanner = null;
                boolean exista1 = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while(scanner.hasNextLine())
                    {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if(words[0].equals(username))
                        {
                            exista1 = true;
                            //verifica daca parola e corecta
                            if(words[1].equals(password))
                            {

                                Integer id1 = Character.getNumericValue(id);
                                Integer contor = 1;
                                Scanner scanner1 = null;
                                try {
                                    scanner1 = new Scanner(new java.io.File("comment.txt"));
                                    boolean exista2 = false;
                                    while(scanner1.hasNextLine()) {
                                        String line1 = scanner1.nextLine();
                                        String[] words1 = line1.split(" ");
                                        if (contor == id1 && words1[0].equals(username)) {
                                            exista2 = true;
                                            //stergem postarea din fisier
                                            try (java.io.FileWriter fileWriter = new java.io.FileWriter("comment.txt", false)) {
                                                System.out.println("{ 'status' : 'ok', 'message' : 'Operation executed successfully'}");
                                            } catch (java.io.IOException e) {
                                            }
                                        } else if (contor == id1 && !words1[0].equals(username)){
                                            exista2 = true;
                                            System.out.println("{ 'status' : 'error', 'message' : 'The identifier was not valid'}");
                                        }
                                        contor++;
                                    }
                                    if(exista2 == false)
                                    {
                                        System.out.println("{ 'status' : 'error', 'message' : 'The identifier was not valid'}");
                                    }
                                }
                                catch(java.io.FileNotFoundException e)
                                {
                                }
                            }
                            else
                            {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                        }
                    }
                    if(exista1 == false)
                    {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                }
                catch(java.io.FileNotFoundException e)
                {
                }
            }
        }
        else if(strings[0].equals("-like-comment"))
        {
            if(strings.length == 1 || strings.length == 2)
            {
                System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            }
            else if(strings[1].contains("-u") && strings[2].contains("-p") && strings.length == 3)//cazul in care nu avem ultimul argument
            {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                Scanner scanner = null;
                boolean exista1 = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while(scanner.hasNextLine())
                    {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if(words[0].equals(username))
                        {
                            exista1 = true;
                            if(words[1].equals(password))
                            {
                                System.out.println("{ 'status' : 'error', 'message' : 'No comment identifier to like was provided'}");
                            }
                            else
                            {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                        }
                    }
                    if(exista1 == false)
                    {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                }
                catch(java.io.FileNotFoundException e)
                {
                }
            }
            else if(strings[1].contains("-u") && strings[2].contains("-p") && strings[3].contains("-comment-id")) {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                String idtext = strings[3].substring(12, strings[3].length());
                char id = idtext.charAt(1);
                Scanner scanner = null;
                boolean exista1 = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if (words[0].equals(username)) {
                            exista1 = true;
                            if (words[1].equals(password)) {
                                Integer id1 = Character.getNumericValue(id);
                                Integer contor = 1;
                                Scanner scanner1 = null;
                                try {
                                    scanner1 = new Scanner(new java.io.File("comment.txt"));
                                    boolean exista2 = false;
                                    while (scanner1.hasNextLine()) {
                                        String line1 = scanner1.nextLine();
                                        String[] words1 = line1.split(" ");
                                        //cautam sa verificam daca exista comentariul
                                        if (contor == id1) {
                                            exista2 = true;
                                            Scanner scanner2 = null;
                                            boolean exista3 = false;
                                            try {
                                                scanner2 = new Scanner(new java.io.File("likecomment.txt"));
                                                while (scanner2.hasNextLine()) {
                                                    String line2 = scanner2.nextLine();
                                                    String[] words2 = line2.split(" ");
                                                    if (words2[0].equals(username) && words2[1].equals(idtext)) { //verificam daca il mai are la like
                                                        exista3 = true;
                                                        System.out.println("{ 'status' : 'error', 'message' : 'The comment identifier to like was not valid'}");
                                                    }

                                                }
                                                if (exista3 == false) {
                                                    try (java.io.FileWriter fileWriter = new java.io.FileWriter("likecomment.txt", true)) {
                                                        //adauga like-ul la comentariu in fisier
                                                        fileWriter.write(username + " " + idtext + "\n");
                                                        System.out.println("{ 'status' : 'ok', 'message' : 'Operation executed successfully'}");
                                                    } catch (java.io.IOException e) {
                                                    }
                                                }
                                            } catch (java.io.FileNotFoundException e) {
                                            }
                                        }
                                        contor++;
                                    }
                                    if (exista2 == false) {
                                        System.out.println("{ 'status' : 'error', 'message' : 'The comment identifier to like was not valid'}");
                                    }
                                } catch (java.io.FileNotFoundException e) {
                                }
                            } else {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                        }
                    }
                    if (exista1 == false) {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                } catch (java.io.FileNotFoundException e) {
                }
            }

        }
        else if (strings[0].equals("-unlike-comment"))
        {

            if(strings.length == 1 || strings.length == 2)
            {
                System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            }
            else if(strings[1].contains("-u") && strings[2].contains("-p") && strings.length == 3)
            {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                Scanner scanner = null;
                boolean exista1 = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while(scanner.hasNextLine())
                    {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if(words[0].equals(username))
                        {
                            exista1 = true;
                            if(words[1].equals(password))
                            {
                                System.out.println("{ 'status' : 'error', 'message' : 'No comment identifier to unlike was provided'}");
                            }
                            else
                            {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                        }
                    }
                    if(exista1 == false)
                    {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                }
                catch(java.io.FileNotFoundException e)
                {
                }
            }
            else if(strings[1].contains("-u") && strings[2].contains("-p") && strings[3].contains("-comment-id")) {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                String idtext = strings[3].substring(12, strings[3].length());
                char id = idtext.charAt(1);

                Scanner scanner = null;
                boolean exista1 = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if (words[0].equals(username)) {
                            exista1 = true;
                            if (words[1].equals(password)) {
                                Integer id1 = Character.getNumericValue(id);
                                Integer contor = 1;
                                Scanner scanner1 = null;
                                try {
                                    scanner1 = new Scanner(new java.io.File("comment.txt"));
                                    boolean exista2 = false;
                                    while (scanner1.hasNextLine()) {
                                        String line1 = scanner1.nextLine();
                                        String[] words1 = line1.split(" ");
                                        if (contor == id1) {
                                            exista2 = true;
                                            Scanner scanner2 = null;
                                            boolean exista3 = false;
                                            try {
                                                scanner2 = new Scanner(new java.io.File("likecomment.txt"));
                                                while (scanner2.hasNextLine()) {
                                                    String line2 = scanner2.nextLine();
                                                    String[] words2 = line2.split(" ");
                                                    if (words2[0].equals(username) && words2[1].equals(idtext)) {
                                                        exista3 = true;
                                                        //stergem like-ul de la comentariu
                                                        try (java.io.FileWriter fileWriter = new java.io.FileWriter("likecomment.txt", false)) {
                                                            System.out.println("{ 'status' : 'ok', 'message' : 'Operation executed successfully'}");
                                                        } catch (java.io.IOException e) {
                                                        }
                                                    }
                                                }
                                                if (exista3 == false) {
                                                    System.out.println("{ 'status' : 'error', 'message' : 'The comment identifier to unlike was not valid'}");
                                                }
                                            } catch (java.io.FileNotFoundException e) {
                                            }
                                        }
                                        contor++;
                                    }
                                    if (exista2 == false) {
                                        System.out.println("{ 'status' : 'error', 'message' : 'The comment identifier to unlike was not valid'}");
                                    }
                                } catch (java.io.FileNotFoundException e) {
                                }
                            } else {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                        }
                    }
                    if (exista1 == false) {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                } catch (java.io.FileNotFoundException e) {
                }
            }
        }
        else if(strings[0].equals("-get-followings-posts")) {
            if (strings.length == 1 || strings.length == 2)
            {
                System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            }
            else if(strings[1].contains("-u") && strings[2].contains("-p"))
            {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                Scanner scanner = null;
                boolean exista1 = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while(scanner.hasNextLine())
                    {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if(words[0].equals(username))
                        {
                            exista1 = true;
                            if(!words[1].equals(password))
                            {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }

                        }
                    }
                    if(exista1 == false)
                    {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                }
                catch(java.io.FileNotFoundException e)
                {
                }
            }
        }
        else if(strings[0].equals("-get-post-details"))
        {
            if (strings.length == 1 || strings.length == 2) {
                System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            } else if (strings[1].contains("-u") && strings[2].contains("-p") && strings.length == 3) {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());

                Scanner scanner = null;
                boolean exista1 = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if (words[0].equals(username)) {
                            exista1 = true;
                            if (!words[1].equals(password)) {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            } else {
                                System.out.println("{ 'status' : 'error', 'message' : 'No post identifier was provided'}");
                            }
                        }
                    }
                    if (exista1 == false) {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                } catch (java.io.FileNotFoundException e) {
                }
            } else if (strings[1].contains("-u") && strings[2].contains("-p") && strings[3].contains("-post-id")) {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                String idtext = strings[3].substring(9, strings[3].length());
                char id = idtext.charAt(1);

                Scanner scanner = null;
                boolean exista1 = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    Scanner scanner1 = null;
                    boolean exista2 = false;
                    try {
                        scanner1 = new Scanner(new java.io.File("postari.txt"));
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            String[] words = line.split(" ");
                            if (words[0].equals(username)) {
                                exista1 = true;
                                if (words[1].equals(password)) {
                                    Integer id1 = Character.getNumericValue(id);
                                    Integer contor = 1;
                                    Scanner scanner2 = null;
                                    try {
                                        scanner2 = new Scanner(new java.io.File("postari.txt"));
                                        boolean exista3 = false;
                                        while (scanner2.hasNextLine()) {
                                            String line1 = scanner2.nextLine();
                                            String[] words1 = line1.split(" ");
                                            if (contor == id1) {
                                                exista3 = true;
                                            }
                                            contor++;
                                        }
                                        if (exista3 == false) {
                                            System.out.println("{ 'status' : 'error', 'message' : 'The post identifier was not valid'}");
                                        }
                                    } catch (java.io.FileNotFoundException e) {
                                    }
                                } else {
                                    System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                                }
                            }
                        }
                        if (exista1 == false) {
                            System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                        }
                    } catch (java.io.FileNotFoundException e) {

                    }
                } catch (java.io.FileNotFoundException e) {

                }
            }
        }
       else if(strings[0].equals("-get-user-posts"))//11
        {
            if(strings.length == 1 || strings.length == 2)
            {
                System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            }
            else if(strings[1].contains("-u") && strings[2].contains("-p") && strings.length == 3)
            {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());

                Scanner scanner = null;
                boolean exista1 = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while(scanner.hasNextLine())
                    {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if(words[0].equals(username))
                        {
                            exista1 = true;
                            if(!words[1].equals(password))
                            {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                            else {
                                System.out.println("{ 'status' : 'error', 'message' : 'No username to list posts was provided'}");
                            }
                        }
                    }
                    if(exista1 == false)
                    {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                }
                catch(java.io.FileNotFoundException e)
                {
                }
            }
            else if(strings[1].contains("-u") && strings[2].contains("-p") && strings[3].contains("-username"))
            {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                String username1 = strings[3].substring(9, strings[3].length());

                Scanner scanner = null;
                Scanner scanner1 = null;
                boolean exista1 = false, exista2 = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    scanner1 = new Scanner(new java.io.File("utilizatori.txt"));
                    while(scanner.hasNextLine())
                    {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if(words[0].equals(username1))
                        {
                            exista2 = true;
                        }
                    }
                    while(scanner1.hasNextLine())
                    {
                        String line = scanner1.nextLine();
                        String[] words = line.split(" ");
                        if(words[0].equals(username))
                        {
                            exista1 = true;
                            if(!words[1].equals(password))
                            {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                            else if(exista2 == false)
                            {
                                System.out.println("{ 'status' : 'error', 'message' : 'The username to list posts was not valid'}");
                            }
                        }
                    }
                    if(exista1 == false)
                    {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }

                }
                catch(java.io.FileNotFoundException e)
                {
                }
            }
        }
       else if(strings[0].equals("-get-following"))
        {
            if(strings.length == 1 || strings.length == 2)
            {
                System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            }
            else if(strings[1].contains("-u") && strings[2].contains("-p") )
            {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());

                Scanner scanner = null;
                boolean exista1 = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while(scanner.hasNextLine())
                    {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if(words[0].equals(username))
                        {
                            exista1 = true;
                            if(!words[1].equals(password))
                            {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                        }
                    }
                    if(exista1 == false)
                    {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                }
                catch(java.io.FileNotFoundException e)
                {
                }
            }
        }
       else if(strings[0].equals("-get-followers"))
        {
            if(strings.length == 1 || strings.length == 2)
            {
                System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            }
            else if(strings[1].contains("-u") && strings[2].contains("-p") && strings.length == 3)
            {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                Scanner scanner = null;
                boolean exista1 = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while(scanner.hasNextLine())
                    {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if(words[0].equals(username))
                        {
                            exista1 = true;
                            if(!words[1].equals(password))
                            {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                            else {
                                System.out.println("{ 'status' : 'error', 'message' : 'No username to list followers was provided'}");
                            }
                        }
                    }
                    if(exista1 == false)
                    {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                }
                catch(java.io.FileNotFoundException e)
                {
                }
            }
            else if(strings[1].contains("-u") && strings[2].contains("-p") && strings[3].contains("-username"))
            {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                String username1 = strings[3].substring(9, strings[3].length());

                Scanner scanner = null;
                Scanner scanner1 = null;
                boolean exista1 = false, exista2 = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    scanner1 = new Scanner(new java.io.File("utilizatori.txt"));
                    while(scanner.hasNextLine())
                    {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if(words[0].equals(username1))
                        {
                            exista2 = true;
                        }
                    }
                    while(scanner1.hasNextLine())
                    {
                        String line = scanner1.nextLine();
                        String[] words = line.split(" ");
                        if(words[0].equals(username))
                        {
                            exista1 = true;
                            if(!words[1].equals(password))
                            {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                            else if(exista2 == false)
                            {
                                System.out.println("{ 'status' : 'error', 'message' : 'The username to list followers was not valid'}");
                            }
                        }
                    }
                    if(exista1 == false)
                    {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }

                }
                catch(java.io.FileNotFoundException e)
                {
                }
            }
        }
       else if(strings[0].equals("-get-most-liked-posts"))
        {
            if(strings.length == 1 || strings.length == 2)
            {
                System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            }
            else if(strings[1].contains("-u") && strings[2].contains("-p") )
            {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                Scanner scanner = null;
                boolean exista1 = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while(scanner.hasNextLine())
                    {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if(words[0].equals(username))
                        {
                            exista1 = true;
                            if(!words[1].equals(password))
                            {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                        }
                    }
                    if(exista1 == false)
                    {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                }
                catch(java.io.FileNotFoundException e)
                {
                }
            }
        }
       else if(strings[0].equals("-get-most-commented-posts"))
        {
            if(strings.length == 1 || strings.length == 2)
            {
                System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            }
            else if(strings[1].contains("-u") && strings[2].contains("-p") )
            {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());

                Scanner scanner = null;
                boolean exista1 = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while(scanner.hasNextLine())
                    {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if(words[0].equals(username))
                        {
                            exista1 = true;
                            if(!words[1].equals(password))
                            {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                        }
                    }
                    if(exista1 == false)
                    {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                }
                catch(java.io.FileNotFoundException e)
                {
                }
            }
        }
       else if(strings[0].equals("-get-most-followed-users"))
        {
            if(strings.length == 1 || strings.length == 2)
            {
                System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            }
            else if(strings[1].contains("-u") && strings[2].contains("-p") )
            {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());

                Scanner scanner = null;
                boolean exista1 = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while(scanner.hasNextLine())
                    {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if(words[0].equals(username))
                        {
                            exista1 = true;
                            if(!words[1].equals(password))
                            {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                        }
                    }
                    if(exista1 == false)
                    {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                }
                catch(java.io.FileNotFoundException e)
                {
                }
            }
        }
       else if(strings[0].equals("-get-most-liked-users"))
        {
            if(strings.length == 1 || strings.length == 2)
            {
                System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            }
            else if(strings[1].contains("-u") && strings[2].contains("-p") )
            {
                String username = strings[1].substring(3, strings[1].length());
                String password = strings[2].substring(3, strings[2].length());
                Scanner scanner = null;
                boolean exista1 = false;
                try {
                    scanner = new Scanner(new java.io.File("utilizatori.txt"));
                    while(scanner.hasNextLine())
                    {
                        String line = scanner.nextLine();
                        String[] words = line.split(" ");
                        if(words[0].equals(username))
                        {
                            exista1 = true;
                            if(!words[1].equals(password))
                            {
                                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                            }
                        }
                    }
                    if(exista1 == false)
                    {
                        System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                    }
                }
                catch(java.io.FileNotFoundException e)
                {
                }
            }
        }
        else if (strings[0].equals("-cleanup-all")) {
            System.out.println("{ 'status' : 'ok', 'message' : 'Cleanup finished successfully'}");
            //stergem tot din fiecare fisier folosit pentru a stoca informatiile
            try (java.io.FileWriter fileWriter = new java.io.FileWriter("utilizatori.txt", false)) {
            } catch (java.io.IOException e) {
            }
            try (java.io.FileWriter fileWriter = new java.io.FileWriter("postari.txt", false)) {
            } catch (java.io.IOException e) {
            }
            try (java.io.FileWriter fileWriter = new java.io.FileWriter("follow.txt", false)) {
            } catch (java.io.IOException e) {
            }
            try (java.io.FileWriter fileWriter = new java.io.FileWriter("like.txt", false)) {
            } catch (java.io.IOException e) {
            }
            try (java.io.FileWriter fileWriter = new java.io.FileWriter("comment.txt", false)) {
            } catch (java.io.IOException e) {
            }
            try (java.io.FileWriter fileWriter = new java.io.FileWriter("likecomment.txt", false)) {
            } catch (java.io.IOException e) {
            }
        }
    }
}

