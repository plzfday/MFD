package MFD;

import java.util.ArrayList;

class MusicList {
    static ArrayList<String> paths = new ArrayList<>();
    static ArrayList<String> name = new ArrayList<>();

    static void printAll() {
        for (String s : paths) {
            System.out.println(s);
        }
    }
}
