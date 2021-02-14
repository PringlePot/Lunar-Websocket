package me.moose.random;

public class Letters {
    public static void main(String[] args) {
       System.out.println(upAndDown(""));
    }

    public  static String upAndDown(String input) {
        StringBuilder output = new StringBuilder();
        boolean lastWasUp = false;
        for (char ch: input.toCharArray()) {
            if(lastWasUp) {
                lastWasUp = false;
                output.append(String.valueOf(ch).toLowerCase());
            } else {
                lastWasUp = true;
                output.append(String.valueOf(ch).toUpperCase());
            }
        }

        return output.toString();
    }

}
