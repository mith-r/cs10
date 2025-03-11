//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class AmazonReview{
//    private String username;
//    private String product;
//    private int stars;
//    private String comments;
//
//    public AmazonReview(String username, String product, int stars, String comments){
//        this.username = username;
//        this.product = product;
//        this.stars = stars;
//        this.comments = comments;
//    }
//
//    public String getUsername(){
//        return username;
//    }
//
//    public String getProduct(){
//        return product;
//    }
//
//    public int getStars(){
//        return stars;
//    }
//
//    public String toString(){
//        return getUsername() + " - Product '" + product + "' - " + stars + " stars: " + "'" + comments + "'";
//    }
//}
//
//class ReviewFilter {
//    private Map<String, List<AmazonReview>> userMap;
//    private Map<String, Map<Integer, List<AmazonReview>>> productStarMap;
//
//    private List<AmazonReview> loadReviews(String filepath) throws IOException {
//        try (BufferedReader reader = new BufferedReader(new FileReader("blah.txt"))) {
//            List<AmazonReview> reviews = new ArrayList<>();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] string = line.split(";");
//                String username = string[0];
//                String product = string[1];
//                int stars = Integer.valueOf(string[2]);
//                String comments = string[3];
//                AmazonReview review = new AmazonReview(username, product, stars, comments);
//                reviews.add(review);
//            }
//            return reviews;
//        } catch (IOException e) {
//            throw new IOException(e);
//        }
//    }
//
//    private void loadMaps(List<AmazonReview> reviews) {
//        for (AmazonReview review : reviews) {
//            String user = review.getUsername();
//            String product = review.getProduct();
//            int stars = review.getStars();
//
//            // Add to user map
//            if (!userMap.containsKey(user)) {
//                userMap.put(user, new ArrayList<>());
//            }
//            userMap.get(user).add(review);
//
//            // Add to product stars map
//            if (!productStarMap.containsKey(product)) {
//                productStarMap.put(product, new HashMap<>());
//            }
//            if (!productStarMap.get(product).containsKey(stars)) {
//                productStarMap.get(product).put(stars, new ArrayList<>());
//            }
//            productStarMap.get(product).get(stars).add(review);
//        }
//    }
//
//
//    private List<AmazonReview> loadReviews(String filepath) {
//        List<AmazonReview> reviews = new ArrayList<>();
//        // Open the buffered reader
//        BufferedReader br;
//        try {
//            br = new BufferedReader(new FileReader(new File(filepath)));
//        } catch (Exception e) {
//            System.out.println(e);
//            return reviews;
//        }
//        try {
//            // Read the lines of the file
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] tokens = line.split(";");
//
//                // Add the review to the review list
//                // (note how we parse the stars from a string to an int)
//                reviews.add(new AmazonReview(tokens[0], tokens[1],
//                        Integer.parseInt(tokens[2]), tokens[3]));
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        } finally {
//            // Close the buffered reader
//            try {
//                br.close();
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//        }
//        return reviews;
//    }
//}
//
