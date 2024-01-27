public class Team {
    /**
     * instance variables
     */
    private String name;
    private int currentScore;

    public Team(String name){
        this.name = name;
        this.currentScore = 0;
    }

    /**
     * Getters
     * @return
     */

    public String getName(){
        return name;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    /**
     * Setters
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    /**
     * a score method that adds two points to the team
     * @return
     */
    public int Score(){
        currentScore += 2;
        System.out.println("Name: " + getName() + ", " + "Score: " + currentScore);
        return currentScore;
    }

    /**
     * main function
     * @param args
     */
    public static void main(String[] args) {
        Team sam = new Team("sam");
        Team tom = new Team("tom");
        sam.setCurrentScore(sam.Score());
        tom.setCurrentScore(tom.Score());
        tom.setCurrentScore(tom.Score());
        //prints out who ever has the higher score
        if(tom.getCurrentScore() < sam.getCurrentScore()){
            System.out.println(sam.getName());
        }
        else{
            System.out.println(tom.getName());
        }


    }

}
