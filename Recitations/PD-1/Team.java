public class Team {
    String mascot;
    int score;

    public Team(String mascot){
        this.mascot = mascot;
        score = 0;
    }

    public String getMascot(){
        return mascot;
    }

    public int getScore(){
        return score;
    }

    public void score(){
        score += 2;
    }

    public static void main(String[] args) {
        Team blue = new Team("Bear");
        Team red = new Team("Tiger");

        blue.score();
        red.score();
        red.score();

        if (blue.getScore() > red.getScore()){
            System.out.println(blue.getMascot());
        } else if (red.getScore()> blue.getScore()){
            System.out.println(red.getMascot());
        }
    }
}
